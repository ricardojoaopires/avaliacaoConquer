package avaliacao.conquer.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entidade Gasto.
 * 
 * @author ricardo
 */
@Entity
public class Gasto implements Serializable {

	private static final long serialVersionUID = 7174804341791529200L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String municipio;
	private int nroBeneficiados;
	private Long gasto;
	private int anoInic;
	private int mesInic;
	private int anoFim;
	private int mesFim;
	private String fontes;

	/**
	 * @return o Identificador da entidade.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Insere o identificador de entidade.
	 * 
	 * @param id : o Identificador da entidade.
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * @return o município.
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * Insere o munícipio na entidade.
	 * 
	 * @param municipio : o munícipio.
	 */
	public void setMunicipio(final String municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return o nº de beneficiados.
	 */
	public int getNroBeneficiados() {
		return nroBeneficiados;
	}

	/**
	 * Insere o nº de beneficiados na entidade.
	 * 
	 * @param nroBeneficiados : o nº de beneficiados.
	 */
	public void setNroBeneficiados(final int nroBeneficiados) {
		this.nroBeneficiados = nroBeneficiados;
	}

	/**
	 * @return o gasto total.
	 */
	public Long getGasto() {
		return gasto;
	}

	/**
	 * Insere o gasto total na entidade.
	 * 
	 * @param gasto : o gasto total.
	 */
	public void setGasto(final Long gasto) {
		this.gasto = gasto;
	}

	/**
	 * @return o ano inicial.
	 */
	public int getAnoInic() {
		return anoInic;
	}

	/**
	 * Insere o ano inicial na entidade.
	 * 
	 * @param anoInic : o ano inicial.
	 */
	public void setAnoInic(final int anoInic) {
		this.anoInic = anoInic;
	}

	/**
	 * @return o mês inicial.
	 */
	public int getMesInic() {
		return mesInic;
	}

	/**
	 * Insere o mês inicial na entidade.
	 * 
	 * @param mesInic : o mês inicial.
	 */
	public void setMesInic(final int mesInic) {
		this.mesInic = mesInic;
	}

	/**
	 * @return o ano final.
	 */
	public int getAnoFim() {
		return anoFim;
	}

	/**
	 * Insere o ano final na entidade.
	 * 
	 * @param anoFim : o ano final.
	 */
	public void setAnoFim(final int anoFim) {
		this.anoFim = anoFim;
	}

	/**
	 * @return o mês final.
	 */
	public int getMesFim() {
		return mesFim;
	}

	/**
	 * Insere o mês final na entidade.
	 * 
	 * @param mesFim : o mês de fim
	 */
	public void setMesFim(final int mesFim) {
		this.mesFim = mesFim;
	}

	/**
	 * @return as fontes de informações de busca concatenadas.
	 */
	public String getFontes() {
		return fontes;
	}

	/**
	 * Insere as fontes de informações de busca na entidade.
	 * 
	 * @param fontes : as fontes de informações de busca.
	 */
	public void setFontes(final String fontes) {
		this.fontes = fontes;
	}

	@Override
	public String toString() {
		return "Gasto [municipio=" + municipio + ", nroBeneficiados=" + nroBeneficiados + ", gasto=" + gasto
				+ ", anoInic=" + anoInic + ", mesInic=" + mesInic + ", anoFim=" + anoFim + ", mesFim=" + mesFim + "]";
	}

	/**
	 * Recupera as informações que serão exportadas já formatadas no formato
	 * experado pelo CSV.
	 * 
	 * @return as informações que serão exportadas.
	 */
	public String getInfosGastoParaExportacao() {
		return municipio + ", " + nroBeneficiados + ", " + gasto + ", " + anoInic + ", " + mesInic + ", " + anoFim
				+ ", " + mesFim;
	}
}
