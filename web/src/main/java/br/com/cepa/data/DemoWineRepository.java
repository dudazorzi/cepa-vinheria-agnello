package br.com.cepa.data;

import br.com.cepa.model.Wine;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class DemoWineRepository implements WineRepository {
    private final ConcurrentHashMap<String, Wine> wines = new ConcurrentHashMap<>();
    public DemoWineRepository() { SeedWines.all().forEach(this::save); }
    public List<Wine> findAll() { return new ArrayList<>(wines.values()).stream().sorted((a,b) -> a.name().compareToIgnoreCase(b.name())).toList(); }
    public Optional<Wine> findById(String id) { return Optional.ofNullable(wines.get(id)); }
    public void save(Wine wine) { wines.put(wine.id(), wine); }
    public String mode() { return "Demonstração local"; }
}
