package br.com.cepa.model;

import java.io.Serializable;
import java.util.Map;

public record Wine(String id, String name, String type, String style, String region,
                   String description, String pairing, int priceCents, int stock,
                   String color) implements Serializable {
    public static Wine fromMap(String id, Map<String, Object> data) {
        return new Wine(id, string(data, "name"), string(data, "type"), string(data, "style"),
            string(data, "region"), string(data, "description"), string(data, "pairing"),
            number(data, "priceCents"), number(data, "stock"), string(data, "color"));
    }
    private static String string(Map<String, Object> data, String key) {
        Object value = data.get(key); return value == null ? "" : value.toString();
    }
    private static int number(Map<String, Object> data, String key) {
        Object value = data.get(key); return value instanceof Number n ? n.intValue() : 0;
    }
    public Map<String, Object> toMap() {
        return Map.of("name", name, "type", type, "style", style, "region", region,
            "description", description, "pairing", pairing, "priceCents", priceCents,
            "stock", stock, "color", color);
    }
    public String formattedPrice() {
        return String.format(java.util.Locale.forLanguageTag("pt-BR"), "R$ %.2f", priceCents / 100.0);
    }
}
