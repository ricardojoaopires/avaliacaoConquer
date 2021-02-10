package avaliacao.conquer.util;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Classe auxiliar para facilitar a busca.
 * 
 * @author ricardo
 */
public class BuscaInfos {

	@NotNull(message = "Código do município não informado!")
	@NotEmpty(message = "Código do município não informado!")
	private String codMunicipio;
	private boolean checkAuxilio;
	private boolean checkBolsa;
	private boolean checkBPC;
	private boolean checkSafra;
	private boolean checkPeti;
	private boolean checkSeguro;
	private Integer anoInic = 0;
	private Integer mesInic = 0;
	private Integer anoFim = 0;
	private Integer mesFim = 0;

	/**
	 * @return o código do município.
	 */
	public String getCodMunicipio() {
		return codMunicipio;
	}

	/**
	 * Recupera o código do município.
	 * 
	 * @param codMunicipio : o código do município.
	 */
	public void setCodMunicipio(final String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}

	/**
	 * @return <code>true</code> caso o check Auxilio emergencial tenha sido
	 *         selecionado.
	 */
	public boolean getCheckAuxilio() {
		return checkAuxilio;
	}

	/**
	 * Recupera o valor do check Auxilio emergencial.
	 * 
	 * @param checkAuxilio : <code>true</code> caso o check Auxilio emergencial
	 *                     tenha sido selecionado.Neste caso é para recuperar os
	 *                     gasto deste item.
	 */
	public void setCheckAuxilio(final boolean checkAuxilio) {
		this.checkAuxilio = checkAuxilio;
	}

	/**
	 * @return <code>true</code> caso o check Bolsa família tenha sido selecionado.
	 */
	public boolean getCheckBolsa() {
		return checkBolsa;
	}

	/**
	 * Recupera o valor do check Bolsa família.
	 * 
	 * @param checkAuxilio : <code>true</code> caso o check Bolsa família tenha sido
	 *                     selecionado. Neste caso é para recuperar os gasto deste
	 *                     item.
	 */
	public void setCheckBolsa(final boolean checkBolsa) {
		this.checkBolsa = checkBolsa;
	}

	/**
	 * @return <code>true</code> caso o check BPC (Benefício de prestação
	 *         continuada) tenha sido selecionado.
	 */
	public boolean getCheckBPC() {
		return checkBPC;
	}

	/**
	 * Recupera o valor do check BPC (Benefício de prestação continuada).
	 * 
	 * @param checkBPC : <code>true</code> caso o check BPC (Benefício de prestação
	 *                 continuada) tenha sido selecionado. Neste caso é para
	 *                 recuperar os gasto deste item.
	 */
	public void setCheckBPC(final boolean checkBPC) {
		this.checkBPC = checkBPC;
	}

	/**
	 * @return <code>true</code> caso o check PETI (Programa de erradicação do
	 *         trabalho) tenha sido selecionado.
	 */
	public boolean getCheckPeti() {
		return checkPeti;
	}

	/**
	 * Recupera o valor do check PETI (Programa de erradicação do trabalho).
	 * 
	 * @param checkPeti : <code>true</code> caso o check PETI (Programa de
	 *                  erradicação do trabalho) tenha sido selecionado. Neste caso
	 *                  é para recuperar os gasto deste item.
	 */
	public void setCheckPeti(final boolean checkPeti) {
		this.checkPeti = checkPeti;
	}

	/**
	 * @return <code>true</code> caso o check Seguro defeso tenha sido selecionado.
	 */
	public boolean getCheckSeguro() {
		return checkSeguro;
	}

	/**
	 * Recupera o valor do check Seguro defeso.
	 * 
	 * @param checkSeguro : <code>true</code> caso o check Seguro defeso tenha sido
	 *                    selecionado. Neste caso é para recuperar os gasto deste
	 *                    item.
	 */
	public void setcheckSeguro(final boolean checkSeguro) {
		this.checkSeguro = checkSeguro;
	}

	/**
	 * @return <code>true</code> caso o check Garantia-Safra tenha sido selecionado.
	 */
	public boolean getCheckSafra() {
		return checkSafra;
	}

	/**
	 * Recupera o valor do check Garantia-Safra tenha sido selecionado..
	 * 
	 * @param checkSafra : <code>true</code> caso o check Garantia-Safra tenha sido
	 *                   selecionado. Neste caso é para recuperar os gasto deste
	 *                   item.
	 */
	public void setCheckSafra(final boolean checkSafra) {
		this.checkSafra = checkSafra;
	}

	/**
	 * @return o ano inicial.
	 */
	public int getAnoInic() {
		return anoInic;
	}

	/**
	 * Recupera o ano inicial.
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
	 * Recupera o mês inicial.
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
	 * Recupera o ano final.
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
	 * Recupera o mês final.
	 * 
	 * @param mesFim : o mês final.
	 */
	public void setMesFim(final int mesFim) {
		this.mesFim = mesFim;
	}
}
