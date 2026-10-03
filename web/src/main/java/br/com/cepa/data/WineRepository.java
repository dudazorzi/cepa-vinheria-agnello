package br.com.cepa.data;

import br.com.cepa.model.Wine;
import java.util.List;
import java.util.Optional;

public interface WineRepository {
    List<Wine> findAll() throws Exception;
    Optional<Wine> findById(String id) throws Exception;
    void save(Wine wine) throws Exception;
    String mode();
}
