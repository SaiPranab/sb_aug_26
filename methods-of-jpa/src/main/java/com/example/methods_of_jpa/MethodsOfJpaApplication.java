package com.example.methods_of_jpa;

import java.util.List;
import java.util.stream.IntStream;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor 
public class MethodsOfJpaApplication {
	private final ProductRepository productRepository;

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