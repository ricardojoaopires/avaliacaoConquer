package avaliacao.conquer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import avaliacao.conquer.model.Gasto;

/**
 * Interface responsável pela manipulação do formulário.
 * 
 * @author ricardo
 */
@Transactional
@Repository
public interface GastosRepository extends CrudRepository<Gasto, Long> {

}
