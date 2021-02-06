package avaliacao.conquer.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import avaliacao.conquer.util.CsvManipulation;
import avaliacao.conquer.util.JsonInfos;

/**
 * Classe responsável pela comunicação com o Portal Transparência.
 * 
 * @author ricardo
 *
 */
@Service
public class GastosService {

	/** URL's */
	private String URL = "http://www.transparencia.gov.br/api-de-dados";
	private String URL_COMPLEMENTAR_AUXILIO = "/auxilio-emergencial-por-municipio";
	private String URL_COMPLEMENTAR_BOLSA = "/bolsa-familia-por-municipio";

	/** Chave da API Ricardo */
	private String KEY_API = "025c1e08af70e9ece7c7511ea5598bc8";

	/** Names das requisições */
	private static final String KEY_API_NAME = "chave-api-dados";
	private static final String MES_ANO_NAME = "mesAno";
	private static final String CODIGO_IBGE_NAME = "codigoIbge";
	private static final String PAGINA_NAME = "pagina";

	public Gasto getGastoByAPI(final Optional<String> codCidade, Optional<String> chechAuxilio,
			Optional<String> chechBolsa, final Optional<String> anoInic, final Optional<String> mesInic,
			final Optional<String> anoFim, final Optional<String> mesFim) {
		Gasto gasto = null;
		String json = null;

		final RestTemplate restTemplate = new RestTemplate();

		final List<String> urls = this.getUrls(chechAuxilio, chechBolsa);
		final List<String> anoMeses = this.getPeriodos(anoInic, mesInic, anoFim, mesFim);

		int nroBeneficiados = 0;
		Long gastoTotal = 0L;
		String municipio = null;

		for (String url : urls) {
			for (String anoMes : anoMeses) {
				final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
				builder.queryParam(MES_ANO_NAME, anoMes);
				builder.queryParam(CODIGO_IBGE_NAME, codCidade);
				builder.queryParam(PAGINA_NAME, "1");

				final HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.add(KEY_API_NAME, KEY_API);

				HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(headers);

				ResponseEntity<String> responseEntity = restTemplate.exchange(builder.build().encode().toUri(),
						HttpMethod.GET, requestEntity, String.class);

				HttpStatus statusCode = responseEntity.getStatusCode();
				if (statusCode == HttpStatus.OK) {
					/** Remove os colchetes externo do response para desserializar o json */
					json = responseEntity.getBody().replace("[", "").replace("]", "");

					if (json != null && !json.isEmpty()) {
						final JsonInfos jsonInfos = this.jsonParse(json);

						if (gasto == null) {
							gasto = new Gasto();
							municipio = jsonInfos.getMunicipio();
						}

						nroBeneficiados += jsonInfos.getNroBeneficiados();
						gastoTotal += jsonInfos.getGasto();
					}
				}
			}
		}

		gasto.setMunicipio(municipio);
		gasto.setNroBeneficiados(nroBeneficiados);
		gasto.setGasto(gastoTotal);
		gasto.setAnoInic(Integer.parseInt(anoInic.get()));
		gasto.setMesInic(mesInic.get().isEmpty() ? 0 : Integer.parseInt(mesInic.get()));
		gasto.setAnoFim(Integer.parseInt(anoFim.get()));
		gasto.setMesFim(mesFim.get().isEmpty() ? 0 : Integer.parseInt(mesFim.get()));

		return gasto;
	}

	/**
	 * Método responsável por recuperar as URL's de acordo com as informações
	 * fornecidas no formulário. Caso nenhum seja selecionada a busca será feita nas
	 * duas URL's.
	 * 
	 * @param chechAuxilio <code>true</code> caso seja para recuperar os gastos
	 *                     relacionados ao Auxílio emergencial.
	 * @param chechBolsa   <code>true</code> caso seja para recuperar os gastos
	 *                     relacionados ao Bolsa fampilia.
	 * @return as URL's que serão utilizadas na requisição.
	 */
	private List<String> getUrls(final Optional<String> chechAuxilio, final Optional<String> chechBolsa) {
		final List<String> urls = new ArrayList<>();

		if (chechAuxilio.isPresent()) {
			urls.add(URL + URL_COMPLEMENTAR_AUXILIO);
		}

		if (chechBolsa.isPresent()) {
			urls.add(URL + URL_COMPLEMENTAR_BOLSA);
		}

		if (urls.isEmpty()) {
			urls.add(URL + URL_COMPLEMENTAR_AUXILIO);
			urls.add(URL + URL_COMPLEMENTAR_BOLSA);
		}

		return urls;
	}

	/**
	 * Método responsável por recuperar os períodos no formato esperado pela API que
	 * serão urilizados nas requisições através do anos e meses informados no
	 * formulário. Ex.: aaaaMM - 201006
	 * 
	 * @param anoInic : ano de início.
	 * @param mesInic : mês de início. É considerado Janeiro = 1, caso não seja
	 *                informado.
	 * @param anoFim  : ano de fim.
	 * @param mesFim  : mês de fim. É considerado Dezembro = 12, caso não seja
	 *                informado.
	 * @return os períodos no formato esperado pela API que serão urilizados nas
	 *         requisições
	 */
	private List<String> getPeriodos(final Optional<String> anoInic, final Optional<String> mesInic,
			final Optional<String> anoFim, final Optional<String> mesFim) {
		final List<String> periodos = new ArrayList<>();
		final int newAnoInic = Integer.parseInt(anoInic.get());
		final int newMesInic = mesInic.get().isEmpty() ? 1 : Integer.parseInt(mesInic.get());
		final int newAnoFim = Integer.parseInt(anoFim.get());
		final int newMesFim = mesFim.get().isEmpty() ? 12 : Integer.parseInt(mesFim.get());

		final int primeiroMes = 1;
		final int ultimoMes = 12;

		int counAno = newAnoInic;
		int countMes = newMesInic;
		for (; countMes <= ultimoMes;) {
			/**
			 * Tratamento feito pois a requisão espera o mês com 2 digitos e o Integer
			 * remove o "0" da esquerda.
			 */
			final String mesStr = countMes < 10 ? "0".concat(String.valueOf(countMes)) : String.valueOf(countMes);
			periodos.add(String.valueOf(counAno).concat(mesStr));

			// Caso o ano de fim e o mês de fim sejam o mesmo informado no formulário
			// segnica que todos os períodos já foram montado.
			if (counAno == newAnoFim && countMes == newMesFim) {
				break;
				// Caso seja o mes seja Dezembro, ou seja, 12, volta para Janeiros e incrememta
				// o ano.
			} else if (countMes == ultimoMes) {
				countMes = primeiroMes;
				counAno++;
			} else {
				countMes++;
			}
		}

		return periodos;
	}

	private JsonInfos jsonParse(final String json) {
		final JSONObject obj = new JSONObject(json);
		final String municipio = obj.getJSONObject("municipio").getString("nomeIBGE");
		final int nroBeneficiados = obj.getInt("quantidadeBeneficiados");
		final Long gastoTotal = obj.getLong("valor");

		return new JsonInfos(municipio, nroBeneficiados, gastoTotal);
	}

	/**
	 * Método responsável por concretizar a exportação e download dos gastos para
	 * CSV.
	 * 
	 * @param pw     {@link PrintWriter}
	 * @param gastos : os gastos a serem exportados/baicados.
	 */
	public void baixarCsv(final PrintWriter pw, final List<Gasto> gastos) {
		try {
			final CsvManipulation csvManipulation = new CsvManipulation(pw);
			csvManipulation.write(pw, gastos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
