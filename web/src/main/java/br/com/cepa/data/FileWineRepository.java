package br.com.cepa.data;

import br.com.cepa.model.Wine;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/** Catálogo persistido em um único arquivo no disco da instância (EBS na EC2). */
public final class FileWineRepository implements WineRepository {
    private static final String PREFIX = "wine.";
    private final Path file;
    private final Map<String, Wine> wines = new LinkedHashMap<>();

    public FileWineRepository(Path file) throws Exception {
        if (!file.isAbsolute()) throw new IllegalArgumentException("CEPA_DATA_FILE deve ser um caminho absoluto.");
        this.file = file.normalize();
        Files.createDirectories(this.file.getParent());
        if (Files.exists(this.file)) {
            load();
        } else {
            for (Wine wine : SeedWines.all()) wines.put(wine.id(), wine);
            persist();
        }
    }

    @Override public synchronized List<Wine> findAll() {
        List<Wine> result = new ArrayList<>(wines.values());
        result.sort(Comparator.comparing(Wine::name, String.CASE_INSENSITIVE_ORDER));
        return result;
    }

    @Override public synchronized Optional<Wine> findById(String id) {
        return Optional.ofNullable(wines.get(id));
    }

    @Override public synchronized void save(Wine wine) throws Exception {
        Wine previous = wines.put(wine.id(), wine);
        try {
            persist();
        } catch (Exception ex) {
            if (previous == null) wines.remove(wine.id());
            else wines.put(previous.id(), previous);
            throw ex;
        }
    }

    @Override public String mode() { return "Catálogo persistente"; }

    private void load() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(file)) {
            properties.loadFromXML(input);
        }
        String ids = properties.getProperty("wine.ids");
        if (ids == null) throw new IllegalStateException("Arquivo de catálogo inválido: wine.ids ausente.");
        for (String id : ids.split(",")) {
            if (id.isBlank()) continue;
            String key = PREFIX + id + ".";
            Wine wine = new Wine(id,
                required(properties, key + "name"), required(properties, key + "type"),
                required(properties, key + "style"), required(properties, key + "region"),
                required(properties, key + "description"), required(properties, key + "pairing"),
                Integer.parseInt(required(properties, key + "priceCents")),
                Integer.parseInt(required(properties, key + "stock")),
                required(properties, key + "color"));
            wines.put(id, wine);
        }
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null) throw new IllegalStateException("Arquivo de catálogo inválido: " + key + " ausente.");
        return value;
    }

    private void persist() throws Exception {
        Properties properties = new Properties();
        properties.setProperty("wine.ids", String.join(",", wines.keySet()));
        for (Wine wine : wines.values()) {
            String key = PREFIX + wine.id() + ".";
            properties.setProperty(key + "name", wine.name());
            properties.setProperty(key + "type", wine.type());
            properties.setProperty(key + "style", wine.style());
            properties.setProperty(key + "region", wine.region());
            properties.setProperty(key + "description", wine.description());
            properties.setProperty(key + "pairing", wine.pairing());
            properties.setProperty(key + "priceCents", Integer.toString(wine.priceCents()));
            properties.setProperty(key + "stock", Integer.toString(wine.stock()));
            properties.setProperty(key + "color", wine.color());
        }
        Path temporary = Files.createTempFile(file.getParent(), "cepa-", ".tmp");
        try {
            try (OutputStream output = Files.newOutputStream(temporary)) {
                properties.storeToXML(output, "CEPA wine catalog", "UTF-8");
            }
            try {
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException ex) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }
}
