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

	/**
	 * @return o Identificados da entidade.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Recupera o identificador de entidade.
	 * 
	 * @param id : o Identificados da entidade.
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
	 * Recupera o munícipio.
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
	 * Recupera o nº de beneficiados.
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
	 * Recupera o gasto total.
	 * 
	 * @param gasto : o gasto total.
	 */
	public void setGasto(final Long gasto) {
		this.gasto = gasto;
	}

	/**
	 * @return o ano de início.
	 */
	public int getAnoInic() {
		return anoInic;
	}

	/**
	 * Recupera o ano de início.
	 * 
	 * @param anoInic : o ano de início.
	 */
	public void setAnoInic(final int anoInic) {
		this.anoInic = anoInic;
	}

	/**
	 * @return o mês de início.
	 */
	public int getMesInic() {
		return mesInic;
	}

	/**
	 * Recupera o mês de início.
	 * 
	 * @param mesInic : o mês de início.
	 */
	public void setMesInic(final int mesInic) {
		this.mesInic = mesInic;
	}

	/**
	 * @return o ano de fim.
	 */
	public int getAnoFim() {
		return anoFim;
	}

	/**
	 * Recupera o ano de fim.
	 * 
	 * @param anoFim : o ano de fim.
	 */
	public void setAnoFim(final int anoFim) {
		this.anoFim = anoFim;
	}

	/**
	 * @return o mês de fim.
	 */
	public int getMesFim() {
		return mesFim;
	}

	/**
	 * Recupera o mês de fim.
	 * 
	 * @param mesFim : o mês de fim.
	 */
	public void setMesFim(final int mesFim) {
		this.mesFim = mesFim;
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
