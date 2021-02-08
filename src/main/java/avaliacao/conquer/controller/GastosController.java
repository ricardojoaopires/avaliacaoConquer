package avaliacao.conquer.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

	/**
	 * Método responsável por recuperar os gastos no Portal Transparência
	 * recuperados via API Rest a partir das dados informados no formulário.
	 * 
	 * @param buscaInfos {@link BuscaInfos} : dados informados no formulário.
	 * 
	 * @return os gastos no Portal Transparência recuperados.
	 */
	@GetMapping(value = "/gastos")
	public ModelAndView buscaGastos(final BuscaInfos buscaInfos, final RedirectAttributes attributes) {
		/**
		 * Não funcionou o redirect para ficar na página index e apresentar a mensagem
		 * de Código da cidade não informado
		 */
		// final Long codCidade = buscaInfos.getCodCidade();
		// if (codCidade == null || codCidade.equals(0L)) {
//			final ModelAndView mv = new ModelAndView("redirect:/index");
//			mv.addObject("buscaInfos", buscaInfos);
//			mv.addObject("msg", "Código da idade não informado");
//			return mv;
//		}

		/** Recupera o gasto do Portal da Transparência */
		final Gasto gasto = service.getGastoByAPI(buscaInfos);
		/** Persite o gasto na base de dados */
		this.repository.save(gasto);

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
