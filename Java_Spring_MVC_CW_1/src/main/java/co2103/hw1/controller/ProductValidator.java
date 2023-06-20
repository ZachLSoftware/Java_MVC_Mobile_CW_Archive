package co2103.hw1.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import co2103.hw1.domain.Product;


public class ProductValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Product.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		//Cast to Product
		Product product = (Product) target;
		
		//Create a list to validate shelves
		List<String> shelves = new ArrayList<String>();
		shelves.add("FOOD");
		shelves.add("DECORATION");
		shelves.add("TOOLS");
		
		//Test if the Product name is empty
		if(product.getName().isEmpty()) {
			errors.rejectValue("name", "", "Name cannot be empty.");
		}
		
		//Test if product description is empty
		if(product.getDescription().isEmpty()) {
			errors.rejectValue("description", "", "Description cannot be empty.");
		}
		
		//Test if shelf is in list.
		if(!shelves.contains(product.getShelf().toUpperCase())) {
			errors.rejectValue("shelf", "", "Shelf must be Food, Decoration, or Tools");
		}
		
		//Test that stock is within 0 to 500 quantity
		if(0>product.getStock() || product.getStock()>500){
			errors.rejectValue("stock", "", "Stock needs to be between 0 and 500");
		}
		
	}
		
}
