package avaliacao.conquer.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import avaliacao.conquer.model.Gasto;
import avaliacao.conquer.repository.GastosRepository;
import avaliacao.conquer.service.GastosService;

/**
 * Classe controladora de eventos.
 * 
 * @author ricardo
 */
@Controller
public class GastosController {

	@Autowired
	private GastosRepository repository;
	private GastosService service = new GastosService();

	/**
	 * Classe responsável por recuperar os gastos no Portal Transparência via API
	 * Rest atráves das informações abaixo:
	 * 
	 * @param codCidade    : Código da cidade.
	 * @param chechAuxilio : Variável que informa se é para buscar os gastos com
	 *                     Aunxílio emergencial.
	 * @param chechBolsa   : Variável que informa se é para buscar os gastos com
	 *                     Bolsa família.
	 * @param anoInic      : Ano de início.
	 * @param mesInic      : Mês de início.
	 * @param anoFim       : Ano de fim.
	 * @param mesFim       : Mês de fim.
	 * @return
	 */
	@GetMapping(value = "/gastos")
	public ModelAndView buscaGastos(final @RequestParam(value = "codCidade") Optional<String> codCidade,
			final @RequestParam(value = "chechAuxilio") Optional<String> chechAuxilio,
			final @RequestParam(value = "chechBolsa") Optional<String> chechBolsa,
			final @RequestParam(value = "anoInic", required = true) Optional<String> anoInic,
			final @RequestParam("mesInic") Optional<String> mesInic,
			final @RequestParam(value = "anoFim", required = true) Optional<String> anoFim,
			final @RequestParam("mesFim") Optional<String> mesFim) {

		if (!anoInic.isPresent() || !anoFim.isPresent()) {
			// Erro bad request e para
		}

		final Gasto gasto = service.getGastoByAPI(codCidade, chechAuxilio, chechBolsa, anoInic, mesInic, anoFim,
				mesFim);
		this.repository.save(gasto);

		final List<Gasto> gastos = (List<Gasto>) this.repository.findAll();
		final ModelAndView mv = new ModelAndView("paginas/gastos");
		mv.addObject("gastosPorMunicipio", gastos);

		return mv;
	}

	/**
	 * Método responsável por realizar a exportação e download dos gastos para CSV.
	 * 
	 * @param response {@link HttpServletResponse}
	 * @throws IOException {@link IOException}
	 */
	@RequestMapping("/target/gastos-por-minicipio.csv")
	public void baixarCsv(final HttpServletResponse response) throws IOException {
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; file=gastos-por-minicipio.csv");
		service.baixarCsv(response.getWriter(), (List<Gasto>) this.repository.findAll());
	}
}
