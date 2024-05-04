package com.vincenzoracca.simpleclient;

import com.vincenzoracca.grpcserver.proto.OrderRequest;
import com.vincenzoracca.grpcserver.proto.OrderResponse;
import com.vincenzoracca.grpcserver.proto.OrderServiceGrpc;
import lombok.Builder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@SpringBootApplication
public class SimpleClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleClientApplication.class, args);
	}


	@RestController
	public static class OrderRestController {

		private final RestClient restClient;
		private final OrderServiceGrpc.OrderServiceBlockingStub grpcClient;


		public OrderRestController(RestClient.Builder builder,
								   @GrpcClient("orderService") OrderServiceGrpc.OrderServiceBlockingStub grpcClient) {
			this.restClient =  builder.requestFactory(new JdkClientHttpRequestFactory()).build();
			this.grpcClient = grpcClient;

		}

		@GetMapping("rest")
		public ResponseEntity<SimpleResponse> getOrderRest() {
			OrderRestResponse order = restClient.get()
					.uri("http://localhost:8081/orders/1")
					.retrieve()
					.body(new ParameterizedTypeReference<>() {});

			return ResponseEntity.ok(new SimpleResponse(order.orderCode()));
		}

		@GetMapping("grpc")
		public ResponseEntity<SimpleResponse> getOrderGrpc() {
			var order = grpcClient.getOrder(OrderRequest.newBuilder().setOrderCode(1).build());
			return ResponseEntity.ok(new SimpleResponse(order.getOrderCode()));
		}
	}

	public record SimpleResponse(int code){}

	public record OrderRestResponse(int orderCode, List<Book> books){}

	@Builder
	public record Book(int code, String name, String company, double price, int pagesNumber){}

}
