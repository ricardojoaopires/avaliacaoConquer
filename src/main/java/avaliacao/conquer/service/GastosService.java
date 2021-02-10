package avaliacao.conquer.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import avaliacao.conquer.model.Gasto;
import avaliacao.conquer.util.BuscaInfos;
import avaliacao.conquer.util.CsvManipulation;
import avaliacao.conquer.util.JsonInfos;

/**
 * Classe responsável pela comunicação com o Portal Transparência.
 * 
 * @author ricardo
 */
@Service
public class GastosService {

	/** URL's */
	private String URL = "http://www.transparencia.gov.br/api-de-dados";
	private String URL_COMPLEMENTAR_AUXILIO = "/auxilio-emergencial-por-municipio";
	private String URL_COMPLEMENTAR_BOLSA = "/bolsa-familia-por-municipio";
	private String URL_BPC = "/bpc-por-municipio"; // Benefício de Prestação Continuada
	private String URL_GARANTIA_SAFRA = "/safra-por-municipio";
	private String URL_PETI = "/peti-por-municipio"; // Programa de Erradicação do Trabalho Infantil
	private String URL_SEGURO_DEFESO = "/seguro-defeso-por-municipio";

	/** Chave da API - Ricardo */
	private String KEY_API = "025c1e08af70e9ece7c7511ea5598bc8";

	/** Names das requisições */
	private static final String KEY_API_NAME = "chave-api-dados";
	private static final String MES_ANO_NAME = "mesAno";
	private static final String CODIGO_IBGE_NAME = "codigoIbge";
	private static final String PAGINA_NAME = "pagina";

	/**
	 * Método responsável por recuperar os gastos no Portal Transparência via API
	 * Rest.
	 * 
	 * @param buscaInfos {@link BuscaInfos} : Objeto contendo as informações de
	 *                   requisição para realizar a chamada.
	 * @return {@link Gasto} : objeto contendo todas as informações da entidade
	 *         gasto.
	 */
	public Gasto getGastoByAPI(final BuscaInfos buscaInfos) {
		Gasto gasto = null;
		String json = null;

		final RestTemplate restTemplate = new RestTemplate();

		final int anoBase = Calendar.getInstance().get(Calendar.YEAR) - 1;

		/**
		 * Faz o tratamento dos anos quando não forem preenchidas na view. Considerei o
		 * ano anterior ao atual como base
		 */
		final int anoInic = buscaInfos.getAnoInic() == 0 ? anoBase : buscaInfos.getAnoInic();
		final int mesInic = buscaInfos.getMesInic() == 0 ? 1 : buscaInfos.getMesInic();
		final int anoFim = buscaInfos.getAnoFim() == 0 ? anoBase : buscaInfos.getAnoFim();
		final int mesFim = buscaInfos.getMesFim() == 0 ? 12 : buscaInfos.getMesFim();

		//@formatter:off
		final boolean auxilioChecked = buscaInfos.getCheckAuxilio();
		final boolean bolsaChecked = buscaInfos.getCheckBolsa();
		final boolean bpcChecked = buscaInfos.getCheckBPC();
		final boolean safraChecked = buscaInfos.getCheckSafra();
		final boolean petiChecked = buscaInfos.getCheckPeti();
		final boolean seguroChecked = buscaInfos.getCheckSeguro();
		//@formatter:on

		final List<String> urls = this.getUrls(auxilioChecked, bolsaChecked, bpcChecked, safraChecked, petiChecked,
				seguroChecked);
		final List<String> anoMeses = this.getPeriodos(anoInic, mesInic, anoFim, mesFim);

		int nroBeneficiados = 0;
		Long gastoTotal = 0L;
		String municipio = null;

		for (String url : urls) {
			for (String anoMes : anoMeses) {

				final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
				builder.queryParam(MES_ANO_NAME, anoMes);
				builder.queryParam(CODIGO_IBGE_NAME, buscaInfos.getCodMunicipio());
				builder.queryParam(PAGINA_NAME, "1");

				final HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.add(KEY_API_NAME, KEY_API);

				final HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);

				ResponseEntity<String> responseEntity = null;

				responseEntity = restTemplate.exchange(builder.build().encode().toUri(), HttpMethod.GET, requestEntity,
						String.class);

				final HttpStatus statusCode = responseEntity.getStatusCode();
				if (statusCode == HttpStatus.OK) {
					/** Remove os colchetes externo do response para desserializar o json */
					json = responseEntity.getBody().replace("[", "").replace("]", "");

					if (json != null && !json.isEmpty()) {
						final JsonInfos jsonInfos = this.jsonParse(json);

						if (gasto == null) {
							gasto = new Gasto();
							municipio = jsonInfos.getMunicipio();
						}

						final int newNroBeneficiados = jsonInfos.getNroBeneficiados();
						final Long newGasto = jsonInfos.getGasto();

						nroBeneficiados += newNroBeneficiados;
						gastoTotal += newGasto;

						gasto.setNroBeneficiados(newNroBeneficiados);
						gasto.setGasto(newGasto);
					}
				}
			}
		}

		if (municipio != null) {
			gasto.setMunicipio(municipio);
			gasto.setNroBeneficiados(nroBeneficiados);
			gasto.setGasto(gastoTotal);
			gasto.setAnoInic(anoInic);
			gasto.setMesInic(mesInic);
			gasto.setAnoFim(anoFim);
			gasto.setMesFim(mesFim);
			gasto.setFontes(
					this.getFontes(auxilioChecked, bolsaChecked, bpcChecked, safraChecked, petiChecked, seguroChecked));
		}

		return gasto;
	}

	/**
	 * Recupera as fontes de informações de acordo com os check's selecionados no
	 * formulário.
	 * 
	 * @param auxilioChecked <code>true</code> caso a fonte Auxílio emergencial
	 *                       tenha sido selecionada na view.
	 * @param bolsaChecked   <code>true</code> caso a fonte Bolsa família
	 *                       emergencial tenha sido selecionada na view.
	 * @param bpcChecked     <code>true</code> caso a fonte Benefício de prestação
	 *                       continuada emergencial (BPC) tenha sido selecionada na
	 *                       view.
	 * @param safraChecked   <code>true</code> caso a fonte Garantia-Safra
	 *                       emergencial tenha sido selecionada na view.
	 * @param petiChecked    <code>true</code> caso a fonte Programa de erradicação
	 *                       do trabalho infantil emergencial (PETI) tenha sido
	 *                       selecionada na view.
	 * @param seguroChecked  <code>true</code> caso a fonte Seguro defeso
	 *                       emergencial tenha sido selecionada na view.
	 * @return as URL's que serão utilizadas na requisição.
	 */
	private String getFontes(final boolean auxilioChecked, final boolean bolsaChecked, final boolean bpcChecked,
			final boolean safraChecked, final boolean petiChecked, final boolean seguroChecked) {
		final StringBuilder sb = new StringBuilder();
		if (auxilioChecked) {
			sb.append(" - ").append("Auxílio emergencial");
		}
		if (bolsaChecked) {
			sb.append(" - ").append("Bolsa família");
		}
		if (bpcChecked) {
			sb.append(" - ").append("BPC");
		}
		if (safraChecked) {
			sb.append(" - ").append("Garantia-Safra");
		}
		if (petiChecked) {
			sb.append(" - ").append("PETI");
		}
		if (seguroChecked) {
			sb.append(" - ").append("Seguro defeso");
		}
		if (!sb.isEmpty()) {
			sb.append(" -");
		}
		return sb.toString();
	}

	/**
	 * Método responsável por recuperar as URL's de acordo com as informações
	 * fornecidas no formulário. Caso nenhum seja selecionada a busca será feita nas
	 * duas URL's.
	 * 
	 * @param auxilioChecked <code>true</code> caso seja para recuperar os gastos da
	 *                       fonte Auxílio emergencial.
	 * @param bolsaChecked   <code>true</code> caso seja para recuperar os gastos da
	 *                       fonte Bolsa família.
	 * @param bpcChecked     <code>true</code> caso seja para recuperar os gastos da
	 *                       fonte Benefício de prestação continuada emergencial
	 *                       (BPC).
	 * @param safraChecked   <code>true</code> caso seja para recuperar os gastos da
	 *                       fonte Garantia-Safra.
	 * @param petiChecked    <code>true</code> caso seja para recuperar os gastos da
	 *                       fonte Programa de erradicação do trabalho infantil
	 *                       (PETI).
	 * @param seguroChecked  <code>true</code> caso seja para recuperar os gastos da
	 *                       fonte Seguro defeso.
	 * @return as URL's que serão utilizadas na requisição.
	 */
	private List<String> getUrls(final boolean auxilioChecked, final boolean bolsaChecked, final boolean bpcChecked,
			final boolean safraChecked, final boolean petiChecked, final boolean seguroChecked) {
		final List<String> urls = new ArrayList<>();

		if (auxilioChecked) {
			urls.add(URL + URL_COMPLEMENTAR_AUXILIO);
		}
		if (bolsaChecked) {
			urls.add(URL + URL_COMPLEMENTAR_BOLSA);
		}
		if (bpcChecked) {
			urls.add(URL + URL_BPC);
		}
		if (safraChecked) {
			urls.add(URL + URL_GARANTIA_SAFRA);
		}
		if (petiChecked) {
			urls.add(URL + URL_PETI);
		}
		if (seguroChecked) {
			urls.add(URL + URL_SEGURO_DEFESO);
		}

		if (urls.isEmpty()) {
			urls.add(URL + URL_COMPLEMENTAR_AUXILIO);
			urls.add(URL + URL_COMPLEMENTAR_BOLSA);
			urls.add(URL + URL_BPC);
			urls.add(URL + URL_GARANTIA_SAFRA);
			urls.add(URL + URL_PETI);
			urls.add(URL + URL_SEGURO_DEFESO);
		}

		return urls;
	}

	/**
	 * Método responsável por recuperar os períodos no formato esperado pela API.
	 * Ex.: aaaaMM - 202006.
	 * 
	 * @param anoInic : ano de início.
	 * @param mesInic : mês de início. É considerado Janeiro = 1, caso não seja
	 *                informado.
	 * @param anoFim  : ano de fim.
	 * @param mesFim  : mês de fim. É considerado Dezembro = 12, caso não seja
	 *                informado.
	 * @return os períodos no formato esperado pela API.
	 */
	private List<String> getPeriodos(final int anoInic, final int mesInic, final int anoFim, final int mesFim) {
		final List<String> periodos = new ArrayList<>();

		final int primeiroMes = 1;
		final int ultimoMes = 12;

		int counAno = anoInic;
		int countMes = mesInic;
		for (; countMes <= ultimoMes;) {
			/**
			 * Tratamento feito pois a requisão espera o mês com 2 digitos e o Integer
			 * remove o "0" à esquerda.
			 */
			final String mesStr = countMes < 10 ? "0".concat(String.valueOf(countMes)) : String.valueOf(countMes);
			periodos.add(String.valueOf(counAno).concat(mesStr));

			// Caso o ano de fim e o mês de fim sejam o mesmo informado no formulário
			// signica que todos os períodos já foram montados.
			if (counAno == anoFim && countMes == mesFim) {
				break;
				// Caso o mês seja Dezembro (12), volta para Janeiros e incrememta o ano.
			} else if (countMes == ultimoMes) {
				countMes = primeiroMes;
				counAno++;
			} else {
				countMes++;
			}
		}

		return periodos;
	}

	/**
	 * Método responsável por disserializar o Json recebido na requisição ao site do
	 * Portal da Transparância com as informações dos gastos.
	 * 
	 * @param json : o Json a ser desserializado.
	 * @return {@link JsonInfos} : Objeto contendo as informações dos gastos
	 *         recebidas via Json da requisição ao Portal da Transparência.
	 */
	private JsonInfos jsonParse(final String json) {
		final JSONObject obj = new JSONObject(json);
		final String municipio = obj.getJSONObject("municipio").getString("nomeIBGE");
		final int nroBeneficiados = obj.getInt("quantidadeBeneficiados");
		final Long gastoTotal = obj.getLong("valor");

		return new JsonInfos(municipio, nroBeneficiados, gastoTotal);
	}

	/**
	 * Método responsável por concretizar a exportação/download dos gastos para CSV.
	 * 
	 * @param pw     {@link PrintWriter}
	 * @param gastos : os gastos a serem exportados/baixados.
	 */
	public void baixarCsv(final PrintWriter pw, final List<Gasto> gastos) {
		try {
			final CsvManipulation csvManipulation = new CsvManipulation(pw);
			csvManipulation.write(pw, gastos);
		} catch (IOException e) {
			e.printStackTrace();
			new RuntimeException("Exceção ao extrair os gastos para CSV.", e);
		}
	}
}
