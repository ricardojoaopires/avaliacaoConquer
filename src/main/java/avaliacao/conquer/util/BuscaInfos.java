package avaliacao.conquer.util;

/**
 * Classe auxiliar para facilitar a busca.
 * 
 * @author ricardo
 */
public class BuscaInfos {

	private Long codCidade = 0L;
	private String chechAuxilio;
	private String chechBolsa;
	private Integer anoInic = 0;
	private Integer mesInic = 0;
	private Integer anoFim = 0;
	private Integer mesFim = 0;

	/**
	 * @return o Identificados da entidade.
	 */
	public Long getCodCidade() {
		return codCidade;
	}

	/**
	 * Recupera o código da cidade.
	 * 
	 * @param codCidade : o código da cidade.
	 */
	public void setCodCidade(final Long codCidade) {
		this.codCidade = codCidade;
	}

	/**
	 * @return o município.
	 */
	public String getChechAuxilio() {
		return chechAuxilio;
	}

	/**
	 * Recupera o munícipio.
	 * 
	 * @param chechAuxilio : o munícipio.
	 */
	public void setChechAuxilio(final String chechAuxilio) {
		this.chechAuxilio = chechAuxilio;
	}

	/**
	 * @return o nº de beneficiados.
	 */
	public String getChechBolsa() {
		return chechBolsa;
	}

	/**
	 * Recupera o nº de beneficiados.
	 * 
	 * @param chechBolsa : o nº de beneficiados.
	 */
	public void setChechBolsa(final String chechBolsa) {
		this.chechBolsa = chechBolsa;
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

}
