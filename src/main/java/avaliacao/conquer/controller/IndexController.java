package avaliacao.conquer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Classe controladora da página principal.
 * 
 * @author ricardo
 */
@Controller
public class IndexController {

	@GetMapping("/")
	public String index() {
		return "index";
	}
}
