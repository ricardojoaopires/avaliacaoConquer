package avaliacao.conquer.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
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

	/**
	 * Consulta que recupera um curso atrás das informações: municipio, anoInic,
	 * mesInic, anoFim, mesFim e nroBeneficiados.
	 */
	@Query("SELECT g FROM Gasto g WHERE g.municipio = ?1 and g.anoInic = ?2 and "
			+ "g.mesInic = ?3 and g.anoFim = ?4 and g.mesFim = ?5 and g.nroBeneficiados = ?6")
	Collection<Gasto> findGasto(final String municipio, final int anoInic, final int mesInic, final int anoFim,
			final int mesFim, final int nroBeneficiados);

}
