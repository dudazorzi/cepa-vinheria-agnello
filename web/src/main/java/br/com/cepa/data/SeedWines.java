package br.com.cepa.data;

import br.com.cepa.model.Wine;
import java.util.List;

public final class SeedWines {
    private SeedWines() {}
    public static List<Wine> all() {
        return List.of(
            new Wine("quinta-do-vale", "Quinta do Vale — Reserva 2021", "Tinto", "Encorpado", "Vale dos Vinhedos, RS", "Notas de frutas maduras, especiarias e taninos macios. Uma escolha para refeições especiais.", "Carnes vermelhas", 8990, 18, "burgundy"),
            new Wine("serra-alta", "Serra Alta Sauvignon Blanc", "Branco", "Leve", "Campanha Gaúcha, RS", "Branco fresco, de acidez viva e notas cítricas. Ideal para dias mais leves.", "Peixes e frutos do mar", 6200, 11, "cream"),
            new Wine("casa-bruno", "Casa Bruno Brut Rosé", "Espumante", "Leve", "Pinto Bandeira, RS", "Espumante delicado, frutado e refrescante para celebrações.", "Queijos", 7450, 7, "burgundy"),
            new Wine("herdade-nobile", "Herdade Nobile Cabernet 2020", "Tinto", "Encorpado", "Serra Gaúcha, RS", "Cabernet de corpo marcante, com notas de frutas negras e final prolongado.", "Carnes vermelhas", 11900, 6, "burgundy"),
            new Wine("jardim-das-uvas", "Jardim das Uvas Rosé", "Rosé", "Leve", "Vale dos Vinhedos, RS", "Rosé aromático e versátil, com frescor para encontros descontraídos.", "Queijos", 5590, 14, "cream"),
            new Wine("aurora-do-sul", "Aurora do Sul Chardonnay", "Branco", "Suave", "Campanha Gaúcha, RS", "Branco macio e equilibrado, com notas de frutas de polpa branca.", "Peixes e frutos do mar", 6900, 9, "cream")
        );
    }
}
