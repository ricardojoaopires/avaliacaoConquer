package avaliacao.conquer.util;

/**
 * Classe utilitária responsável por guardar as váriavels extraídas do Json.
 * 
 * @author ricardo
 */
public class JsonInfos {

	private String municipio;
	private int nroBeneficiados;
	private Long gasto;

	/**
	 * Construtor.
	 * 
	 * @param municipio       : O município.
	 * @param nroBeneficiados : O número de beneficiados.
	 * @param gasto           : O total de gastos.
	 */
	public JsonInfos(final String municipio, final int nroBeneficiados, final Long gasto) {
		this.municipio = municipio;
		this.nroBeneficiados = nroBeneficiados;
		this.gasto = gasto;
	}

	/**
	 * @return o município.
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @return o nº de beneficiados.
	 */
	public int getNroBeneficiados() {
		return nroBeneficiados;
	}

	/**
	 * @return o gasto.
	 */
	public Long getGasto() {
		return gasto;
	}
}
