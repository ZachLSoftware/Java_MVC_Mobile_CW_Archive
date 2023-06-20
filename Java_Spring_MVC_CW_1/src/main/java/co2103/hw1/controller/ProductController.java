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
import org.springframework.web.bind.annotation.RequestParam;

import co2103.hw1.Hw1Application;
import co2103.hw1.domain.Shop;
import co2103.hw1.domain.Product;

@Controller
public class ProductController {
	
	
	@RequestMapping(value="/products", method=RequestMethod.GET)
	public String listProducts(@RequestParam int shop, Model model) {
		
		//Find the correct shop within the ArrayList and pass it to the JSP.
		for(Shop s : Hw1Application.shops) {
			if(s.getId() == shop) {
				model.addAttribute("shop", s);
				break;
			}
		}
		
		return "products/list";
	}
	
	
	@RequestMapping(value="/newProduct", method=RequestMethod.GET)
	public String newProduct(@RequestParam int shop, Model model) {
		
		//Create a new product for the JSP Form
		model.addAttribute("product", new Product());
		
		//Send the shop ID to the JSP form
		model.addAttribute("shopId", shop);
		
		return "products/form";
	}
	
	@RequestMapping(value="/addProduct", method=RequestMethod.POST)
	public String addProduct(@Valid @ModelAttribute Product product, BindingResult result, @RequestParam int shop, Model model) {
		
		//If the product Form finds any errors, display them on the JSP
		if(result.hasErrors()) {
			
			//Resend the Shop ID to the JSP form
			model.addAttribute("shopId", shop);
			
			return "products/form";
		}
		
		//Ensure Correct format of shelf
		//Despite user input format, this will set the shelf to first letter uppercase.
		product.setShelf(product.getShelf().substring(0,1).toUpperCase()+product.getShelf().substring(1).toLowerCase());
		
		//Check the Shop ID to find the correct shop to add the product to.
		for(Shop s : Hw1Application.shops) {
			if(s.getId()==shop) {
				s.addProduct(product);
				break;
			}
		}
		
		return "redirect:/shops";
	}
	
	@InitBinder("product")
	protected void initBinder(WebDataBinder binder) {
		binder.addValidators(new ProductValidator());
	}

}
