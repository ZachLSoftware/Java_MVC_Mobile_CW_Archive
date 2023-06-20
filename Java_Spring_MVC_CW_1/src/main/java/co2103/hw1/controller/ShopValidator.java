package co2103.hw1.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import co2103.hw1.Hw1Application;
import co2103.hw1.domain.Shop;


public class ShopValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Shop.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		//Cast to Shop
		Shop shop = (Shop) target;
		
		//Test if shop ID is unique against main shop list
		for(Shop s : Hw1Application.shops) {
			if(shop.getId()==s.getId()) {
				errors.rejectValue("id", "", "This is not a Unique Shop ID");
			}
		}
		
		//Ensure Shop ID is positive
		if(shop.getId()<0) {
			errors.rejectValue("id", "", "Shop ID should be a positive number.");
		}
		
		//Ensure that the chain name is not empty
		if(shop.getChain().isEmpty()) {
			errors.rejectValue("chain", "", "Chain cannot be empty.");
		}
		
		//Ensure that address above 20 Characters and not empty
		if(shop.getAddress().length() <20 || shop.getAddress().isEmpty()) {
			errors.rejectValue("address", "", "Address must be 20 or more characters.");
		}
		
	}
	

}
