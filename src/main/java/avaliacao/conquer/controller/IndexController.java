package avaliacao.conquer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import avaliacao.conquer.util.BuscaInfos;

/**
 * Classe controladora da página principal.
 * 
 * @author ricardo
 */
@Controller
public class IndexController {

	@RequestMapping("/")
	public ModelAndView index() {
		final ModelAndView mv = new ModelAndView("index");
		mv.addObject("buscaInfos", new BuscaInfos());
		return mv;
	}
}
