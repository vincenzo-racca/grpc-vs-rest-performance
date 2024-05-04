package com.vincenzoracca.restserver;

import lombok.Builder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class RestServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestServerApplication.class, args);
	}

	@RestController
	@RequestMapping("orders")
	public static class OrderController {

		@GetMapping("{orderCode}")
		public ResponseEntity<OrderResponse> getOrder(@PathVariable Integer orderCode) {
			var books = new ArrayList<Book>(100);
			for(int i = 0; i < 100; i++) {
				var book = Book.builder()
						.code(i)
						.company("Company")
						.name("name")
						.pagesNumber(200)
						.price(20.0)
						.build();

				books.add(book);
			}

			return ResponseEntity.ok(new OrderResponse(orderCode, books));
		}
	}

	public record OrderResponse(int orderCode, List<Book> books){}

	@Builder
	public record Book(int code, String name, String company, double price, int pagesNumber){}

}
