package avaliacao.conquer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import avaliacao.conquer.model.Gasto;

@Transactional
@Repository
public interface GastosRepository extends CrudRepository<Gasto, Long> {

}
