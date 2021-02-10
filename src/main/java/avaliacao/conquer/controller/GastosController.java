package avaliacao.conquer.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import avaliacao.conquer.model.Gasto;
import avaliacao.conquer.repository.GastosRepository;
import avaliacao.conquer.service.GastosService;
import avaliacao.conquer.util.BuscaInfos;

/**
 * Classe controladora de eventos relacionados aos gastos.
 * 
 * @author ricardo
 */
@Controller
public class GastosController {

	@Autowired
	private GastosRepository repository;

	private GastosService service = new GastosService();

	@RequestMapping("/gasto")
	public ModelAndView home() {
		final ModelAndView mv = new ModelAndView("paginas/gasto");
		mv.addObject("buscaInfos", new BuscaInfos());
		return mv;
	}

	/**
	 * Método responsável por recuperar os gastos no Portal Transparência via API
	 * Rest a persistir na base de dados.
	 * 
	 * @param buscaInfos {@link BuscaInfos} : dados informados no formulário para
	 *                   montagem da requisição..
	 * 
	 * @return ModelAndView {@link ModelAndView}
	 */
	@PostMapping(value = "/gasto/salvar")
	public ModelAndView salvarGasto(final @Valid BuscaInfos buscaInfos, final BindingResult bindingResult,
			final RedirectAttributes attributes) {

		if (bindingResult.hasErrors()) {
			final ModelAndView mv = new ModelAndView("paginas/gasto");
			mv.addObject("buscaInfos", buscaInfos);
			for (ObjectError error : bindingResult.getAllErrors()) {
				mv.addObject("msg_erro", error.getDefaultMessage());
			}
			return mv;
		}

		/** Recupera o gasto no Portal da Transparência */
		final Gasto gasto = service.getGastoByAPI(buscaInfos);
		if (gasto != null) {
			final Collection<Gasto> findGasto = this.repository.findGasto(gasto.getMunicipio(), gasto.getAnoInic(),
					gasto.getMesInic(), gasto.getAnoFim(), gasto.getMesFim(), gasto.getNroBeneficiados());

			/**
			 * Persite o gasto na base de dados apenas se a mesma consulta já não foi
			 * realizada
			 */
			if (findGasto == null || findGasto.isEmpty()) {
				this.repository.save(gasto);

				final ModelAndView mv = new ModelAndView("paginas/gasto");
				mv.addObject("buscaInfos", new BuscaInfos());
				mv.addObject("msg_sucesso", "Busca realizada e salva na base de dados com sucesso! Para verificar "
						+ "todos os gastos basta clicar no link VER GASTOS logo abaixo do formulário.");
				return mv;
			}

		}

		return null;
	}

	/**
	 * Método responsável por recuperar os gastos da base de dados e encaminhar para
	 * view.
	 * 
	 * @return ModelAndView {@link ModelAndView}
	 */
	@GetMapping(value = "/gasto/buscar")
	public ModelAndView buscaGastos() {
		final ModelAndView mv = new ModelAndView("paginas/gastos");
		final List<Gasto> gastos = (List<Gasto>) this.repository.findAll();
		mv.addObject("gastos", gastos);
		return mv;
	}

	/**
	 * Método responsável por realizar a exportação/download dos gastos para CSV.
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
