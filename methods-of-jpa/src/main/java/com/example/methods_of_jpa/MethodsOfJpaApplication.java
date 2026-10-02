package com.example.methods_of_jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor 
public class MethodsOfJpaApplication {
	private final ProductRepository productRepository;
	private final OrdersService ordersService;

	public static void main(String[] args) {
		SpringApplication.run(MethodsOfJpaApplication.class, args);
	}

	@Bean 
	public CommandLineRunner commandLineRunner() {
		return args -> {
			Product product = Product.builder()
				.productName("Iphone 17 Pro Max")
				.productBrand("Apple")
				.productPrice(180000.99)
				.build();

			// SAVE
			// Product savedProduct = productRepository.save(product);
			// System.out.println("saved product is:- " + savedProduct);

			// SAVEALL
			// productRepository.saveAll(getProducts());

			// COUNT
			// long totalProducts = productRepository.count();
			// System.out.println("total number of products is:-" + totalProducts);

			// Exists & Exists By ID
			// boolean isIphoneExists = productRepository.existsById(100);
			// System.out.println(" is Iphone 17 exists " + isIphoneExists);
			
			// Product existingProduct = productRepository.findById(1).orElseThrow();
			// boolean isIphoneExists2 = productRepository.exists(Example.of(existingProduct));
			// System.out.println(" is Iphone 17 exists 2:- " + isIphoneExists2);

			// List<Product> products = productRepository.findAll();
			// productRepository.deleteAll(products);
			
			// List<Product> products = productRepository.findAll(Sort.by("productPrice"));
			// products.forEach(System.out::println);
			
			// Page<Product> products = productRepository.findAll(PageRequest.of(0, 5, 
			// 																											Direction.DESC, "productId"));
			// System.out.println("page information is " + products);
			// // pageNumber -> 0 based indexing 
			// // pagesize -> number of data inside the page
			// products.forEach(System.out::println);

			// Product iPhone17 = productRepository.findById(11).orElseThrow();
			// iPhone17.setProductBrand("Samsung");
			// productRepository.save(iPhone17);


			// Product optGalaxy = productRepository.findByProductName("Galaxy S24").orElseThrow();
			// System.out.println(optGalaxy);

			// productRepository.findAllByProductPriceBetween(10000, 50000)
			// 																											.forEach(System.out::println);

			// productRepository
			// 	.findAllByProductPriceGreaterThanEqual(50000, Sort.by(Direction.ASC, "productPrice"))
			// 	.forEach(System.out::println);																											

			// productRepository
			// 	.findByProductNameAndProductBrand("product-112", "brand-1")
			// 	.ifPresent(p -> System.out.println(p));

			// productRepository
			// 	.getProduct("product-2", "brand-2")
			// 	.ifPresent(p -> System.out.println(p));

			// int affectedRow = productRepository.updatePrice(12, 12000);
			// System.out.println("No of affected rows:- " + affectedRow);

			ordersService.placeOrder(11, 10);
		};	
	}

	private List<Product> getProducts() {
		return IntStream.range(1, 10).mapToObj(i -> Product.builder()
				.productName("product-" + i)
				.productBrand("brand-" + i)
				.productPrice(10000 * i)
				.build())
			.toList();
	}
}