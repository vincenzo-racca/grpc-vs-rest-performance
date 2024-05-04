package com.vincenzoracca.grpcserver;

import com.vincenzoracca.grpcserver.proto.Book;
import com.vincenzoracca.grpcserver.proto.OrderRequest;
import com.vincenzoracca.grpcserver.proto.OrderResponse;
import com.vincenzoracca.grpcserver.proto.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class GrpcServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcServerApplication.class, args);
	}

	@GrpcService
	public static class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {

		@Override
		public void getOrder(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
			var books = new ArrayList<Book>(100);
			for(int i = 0; i < 100; i++) {
				var book = Book.newBuilder()
						.setCode(i)
						.setCompany("Company")
						.setName("name")
						.setPagesNumber(200)
						.setPrice(20.0)
						.build();

				books.add(book);
			}

			responseObserver.onNext(OrderResponse.newBuilder()
					.setOrderCode(request.getOrderCode())
					.addAllBooks(books)
					.build());

			responseObserver.onCompleted();
		}
	}

}
