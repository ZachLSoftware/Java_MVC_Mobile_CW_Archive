package co2103.hw1.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import co2103.hw1.Hw1Application;
import co2103.hw1.domain.Shop;

@Controller
public class ShopController {
	
	@RequestMapping(value="/shops", method = RequestMethod.GET)
	public String listShops(Model model) {
		
		//Send list of shops to JSP
		model.addAttribute("shops", Hw1Application.shops);
		
		return "shops/list";
	}
	
	@RequestMapping("/newShop")
	public String newShop(Model model) {
		
		//Create a new shop object for the JSP form
		model.addAttribute("shop", new Shop());
		return "shops/form";
	}
	
	@RequestMapping(value="/addShop", method=RequestMethod.POST)
	public String addShop(@Valid @ModelAttribute Shop shop, BindingResult result) {
		
		//Check for any validation errors
		if(result.hasErrors()) {
			return "shops/form";
		}
		
		//If validation passed, add to main list of shops
		Hw1Application.shops.add(shop);
		
		return "redirect:/shops";
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new ShopValidator());
	}
}

