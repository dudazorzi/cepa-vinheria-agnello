package br.com.cepa.web;

import br.com.cepa.data.DemoWineRepository;
import br.com.cepa.data.FileWineRepository;
import br.com.cepa.data.WineRepository;
import br.com.cepa.model.Wine;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@WebServlet(urlPatterns = {"/inicio", "/catalogo", "/vinho", "/quiz", "/carrinho", "/adega", "/painel", "/entrar", "/sair"})
public final class CepaServlet extends HttpServlet {
    private WineRepository repository;
    private String startupError;
    private String adminPassword;

    @Override public void init() {
        String dataFile = System.getenv("CEPA_DATA_FILE");
        adminPassword = System.getenv("CEPA_ADMIN_PASSWORD");
        if (dataFile == null || dataFile.isBlank()) {
            repository = new DemoWineRepository();
            if (adminPassword == null || adminPassword.isBlank()) adminPassword = "cepa-demo";
            return;
        }
        if (adminPassword == null || adminPassword.isBlank()) {
            startupError = "Configure CEPA_ADMIN_PASSWORD para proteger as gravações no catálogo persistente.";
            return;
        }
        try { repository = new FileWineRepository(Path.of(dataFile)); }
        catch (Exception ex) { startupError = "Não foi possível abrir o catálogo persistente: " + ex.getMessage(); }
    }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ready(response)) return;
        try {
            String path = request.getServletPath();
            request.setAttribute("mode", repository.mode());
            request.setAttribute("cartCount", cart(request.getSession()).values().stream().mapToInt(Integer::intValue).sum());
            switch (path) {
                case "/inicio" -> render(request, response, "inicio", "Início", "inicio");
                case "/catalogo" -> {
                    List<Wine> wines = repository.findAll();
                    String type = value(request.getParameter("tipo"));
                    String search = value(request.getParameter("busca"));
                    String style = value(request.getParameter("estilo"));
                    List<Wine> filtered = wines.stream().filter(w -> type.isBlank() || w.type().equalsIgnoreCase(type))
                        .filter(w -> style.isBlank() || w.style().equalsIgnoreCase(style))
                        .filter(w -> search.isBlank() || normalize(w.name() + " " + w.region() + " " + w.pairing()).contains(normalize(search)))
                        .toList();
                    request.setAttribute("wines", filtered);
                    request.setAttribute("type", type);
                    request.setAttribute("search", search);
                    request.setAttribute("style", style);
                    render(request, response, "catalogo", "Catálogo", "catalogo");
                }
                case "/vinho" -> {
                    Wine wine = repository.findById(value(request.getParameter("id"))).orElse(null);
                    if (wine == null) { response.sendError(404, "Vinho não encontrado"); return; }
                    request.setAttribute("wine", wine);
                    render(request, response, "vinho", wine.name(), "catalogo");
                }
                case "/quiz" -> {
                    request.setAttribute("quizStyle", value((String)request.getSession().getAttribute("quizStyle")));
                    render(request, response, "quiz", "Quiz de paladar", "quiz");
                }
                case "/carrinho" -> {
                    List<CartLine> lines = new ArrayList<>(); int total = 0;
                    for (var entry : cart(request.getSession()).entrySet()) {
                        Wine wine = repository.findById(entry.getKey()).orElse(null);
                        if (wine != null) { lines.add(new CartLine(wine, entry.getValue())); total += wine.priceCents() * entry.getValue(); }
                    }
                    request.setAttribute("lines", lines);
                    request.setAttribute("total", String.format(java.util.Locale.forLanguageTag("pt-BR"), "R$ %.2f", total / 100.0));
                    render(request, response, "carrinho", "Carrinho", "carrinho");
                }
                case "/adega" -> {
                    @SuppressWarnings("unchecked") List<String> history = (List<String>)request.getSession().getAttribute("history");
                    List<Wine> wines = new ArrayList<>();
                    if (history != null) for (String id : history) repository.findById(id).ifPresent(wines::add);
                    request.setAttribute("historyWines", wines);
                    render(request, response, "adega", "Minha Adega", "adega");
                }
                case "/entrar" -> render(request, response, "entrar", "Acesso da vinheria", "painel");
                case "/sair" -> {
                    request.getSession().removeAttribute("admin");
                    redirect(request, response, "/inicio");
                }
                case "/painel" -> {
                    if (!isAdmin(request)) { redirect(request, response, "/entrar"); return; }
                    List<Wine> wines = repository.findAll();
                    request.setAttribute("wines", wines);
                    request.setAttribute("stockTotal", wines.stream().mapToInt(Wine::stock).sum());
                    request.setAttribute("lowStock", wines.stream().filter(w -> w.stock() < 8).count());
                    render(request, response, "painel", "Painel da Vinheria", "painel");
                }
                default -> response.sendError(404);
            }
        } catch (Exception ex) { throw new ServletException("Erro ao consultar o catálogo", ex); }
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ready(response)) return;
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        try {
            switch (path) {
                case "/entrar" -> {
                    String password = value(request.getParameter("password"));
                    if (!java.security.MessageDigest.isEqual(password.getBytes(StandardCharsets.UTF_8), adminPassword.getBytes(StandardCharsets.UTF_8))) {
                        request.getSession().setAttribute("notice", "Senha incorreta.");
                        redirect(request, response, "/entrar"); return;
                    }
                    request.getSession().setAttribute("admin", Boolean.TRUE);
                    redirect(request, response, "/painel");
                }
                case "/quiz" -> {
                    String style = value(request.getParameter("estilo"));
                    if (!List.of("Leve", "Encorpado", "Suave").contains(style)) { response.sendError(400); return; }
                    request.getSession().setAttribute("quizStyle", style);
                    redirect(request, response, "/catalogo?estilo=" + encode(style));
                }
                case "/carrinho" -> {
                    String action = value(request.getParameter("acao"));
                    Map<String, Integer> cart = cart(request.getSession());
                    if ("adicionar".equals(action)) {
                        String id = value(request.getParameter("id"));
                        Wine wine = repository.findById(id).orElse(null);
                        if (wine == null || wine.stock() < 1) { response.sendError(400, "Produto indisponível"); return; }
                        cart.merge(id, 1, (current, one) -> Math.min(current + one, wine.stock()));
                    } else if ("remover".equals(action)) {
                        cart.remove(value(request.getParameter("id")));
                    } else if ("finalizar".equals(action)) {
                        if (cart.isEmpty()) { response.sendError(400, "Carrinho vazio"); return; }
                        @SuppressWarnings("unchecked") List<String> previous = (List<String>)request.getSession().getAttribute("history");
                        List<String> history = previous == null ? new ArrayList<>() : new ArrayList<>(previous);
                        for (String id : cart.keySet()) if (!history.contains(id)) history.add(id);
                        request.getSession().setAttribute("history", history);
                        cart.clear();
                        request.getSession().setAttribute("notice", "Pedido demonstrativo registrado nesta sessão. Nenhum pagamento foi processado.");
                    } else { response.sendError(400); return; }
                    redirect(request, response, "finalizar".equals(action) ? "/adega" : "/carrinho");
                }
                case "/painel" -> {
                    if (!isAdmin(request)) { response.sendError(403); return; }
                    String name = value(request.getParameter("name"));
                    String type = value(request.getParameter("type"));
                    String style = value(request.getParameter("style"));
                    String region = value(request.getParameter("region"));
                    String pairing = value(request.getParameter("pairing"));
                    String description = value(request.getParameter("description"));
                    String color = "Branco".equals(type) || "Rosé".equals(type) ? "cream" : "burgundy";
                    int price, stock;
                    try { price = new java.math.BigDecimal(value(request.getParameter("price")).replace(',', '.')).movePointRight(2).intValueExact(); stock = Integer.parseInt(value(request.getParameter("stock"))); }
                    catch (Exception ex) { response.sendError(400, "Preço ou estoque inválido"); return; }
                    if (name.length() < 3 || name.length() > 100 || region.isBlank() || description.isBlank() || price <= 0 || price > 1_000_000 || stock < 0 || stock > 10000 ||
                        !List.of("Tinto", "Branco", "Rosé", "Espumante").contains(type) || !List.of("Leve", "Encorpado", "Suave").contains(style) ||
                        !List.of("Carnes vermelhas", "Peixes e frutos do mar", "Queijos", "Sobremesas").contains(pairing)) {
                        response.sendError(400, "Dados do vinho inválidos"); return;
                    }
                    repository.save(new Wine(UUID.randomUUID().toString(), name, type, style, region, description, pairing, price, stock, color));
                    request.getSession().setAttribute("notice", "Vinho cadastrado no modo " + repository.mode() + ".");
                    redirect(request, response, "/painel");
                }
                default -> response.sendError(405);
            }
        } catch (Exception ex) { throw new ServletException("Não foi possível concluir a operação", ex); }
    }

    private boolean ready(HttpServletResponse response) throws IOException {
        if (startupError == null) return true;
        response.sendError(503, startupError); return false;
    }
    private void render(HttpServletRequest request, HttpServletResponse response, String view, String title, String active) throws ServletException, IOException {
        request.setAttribute("pageTitle", title + " | CEPA"); request.setAttribute("active", active);
        String notice = (String)request.getSession().getAttribute("notice");
        request.getSession().removeAttribute("notice"); request.setAttribute("notice", notice);
        request.setAttribute("contentPage", "/WEB-INF/views/" + view + ".jsp");
        request.getRequestDispatcher("/WEB-INF/views/layout.jsp").forward(request, response);
    }
    private void redirect(HttpServletRequest request, HttpServletResponse response, String target) throws IOException {
        response.sendRedirect(request.getContextPath() + target);
    }
    private static String value(String input) { return input == null ? "" : input.trim(); }
    private static String encode(String value) { return URLEncoder.encode(value, StandardCharsets.UTF_8); }
    private static String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase(java.util.Locale.ROOT);
    }
    private static boolean isAdmin(HttpServletRequest request) { return Boolean.TRUE.equals(request.getSession().getAttribute("admin")); }
    @SuppressWarnings("unchecked") private static Map<String, Integer> cart(HttpSession session) {
        Map<String, Integer> cart = (Map<String, Integer>)session.getAttribute("cart");
        if (cart == null) { cart = new LinkedHashMap<>(); session.setAttribute("cart", cart); }
        return cart;
    }
    public record CartLine(Wine wine, int quantity) {
        public String formattedSubtotal() { return String.format(java.util.Locale.forLanguageTag("pt-BR"), "R$ %.2f", wine.priceCents() * quantity / 100.0); }
    }
}
