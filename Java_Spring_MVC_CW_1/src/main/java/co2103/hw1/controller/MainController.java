package co2103.hw1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
	
	@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
	public String start() {
		return "start";
	}

}
