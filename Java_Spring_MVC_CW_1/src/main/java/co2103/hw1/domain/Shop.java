package co2103.hw1.domain;

import java.util.ArrayList;
import java.util.List;

public class Shop {
	private int id;
	private String chain;
	private String address;
	private List<Product> products;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getChain() {
		return chain;
	}
	
	public void setChain(String chain) {
		this.chain = chain;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public List<Product> getProducts() {
		return products;
	}
	
	public void setProducts(List<Product> products) {
		this.products=products;
		//this.products = new ArrayList<Product>(products);
	}
	
	public void addProduct(Product product) {
		if (this.products == null) {
			products=new ArrayList<Product>();
		}
		this.products.add(product);
	}

}
