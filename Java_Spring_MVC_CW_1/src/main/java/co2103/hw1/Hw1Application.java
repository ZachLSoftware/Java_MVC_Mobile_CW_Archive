package co2103.hw1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import co2103.hw1.domain.Product;
import co2103.hw1.domain.Shop;

@SpringBootApplication
public class Hw1Application {
	
	//Create Shop List
	public static List<Shop> shops = new ArrayList<Shop>();

	public static void main(String[] args) {
		SpringApplication.run(Hw1Application.class, args);
		
		List<Product> products = new ArrayList<Product>();
		Shop shop = new Shop();
		
		//Create Shop
		shop.setId(0);
		shop.setChain("Alfonzo's");
		shop.setAddress("123 Little Italy, New York, New York");
		
		Product product = new Product();
		
		product.setName("Mama's Spaghetti");
		product.setDescription("Sphagetti the way mama made.");
		product.setShelf("Food");
		product.setStock(200);
		products.add(product);
		
		product = new Product();
		product.setName("Luigi's Finest Pasta Sauce");
		product.setDescription("Tasty Pasta Sauce from the fields of Italy");
		product.setShelf("Food");
		product.setStock(100);
		products.add(product);
		
		//Add list to the shop
		shop.setProducts(products);
		
		//Add shop to List
		shops.add(shop);
		
	}

}
