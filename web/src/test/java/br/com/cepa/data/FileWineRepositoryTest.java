package br.com.cepa.data;

import br.com.cepa.model.Wine;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileWineRepositoryTest {
    @TempDir Path tempDir;

    @Test void savesAndReloadsCatalog() throws Exception {
        Path file = tempDir.resolve("data/vinhos.xml");
        FileWineRepository first = new FileWineRepository(file);
        assertEquals(6, first.findAll().size());
        Wine newWine = new Wine("teste-1", "Vinho de Teste", "Tinto", "Leve", "RS",
            "Descrição de teste", "Queijos", 4990, 3, "burgundy");
        first.save(newWine);

        FileWineRepository afterRestart = new FileWineRepository(file);
        assertEquals(7, afterRestart.findAll().size());
        assertEquals(newWine, afterRestart.findById("teste-1").orElseThrow());
        assertEquals("Quinta do Vale — Reserva 2021",
            afterRestart.findById("quinta-do-vale").orElseThrow().name());
        assertFalse(afterRestart.findById("nao-existe").isPresent());
    }

    @Test void refusesRelativePathAndDoesNotOverwriteCorruptFile() throws Exception {
        assertThrows(IllegalArgumentException.class,
            () -> new FileWineRepository(Path.of("vinhos.xml")));
        Path file = tempDir.resolve("vinhos.xml");
        Files.writeString(file, "invalido");
        assertThrows(Exception.class, () -> new FileWineRepository(file));
        assertEquals("invalido", Files.readString(file));
    }
}
