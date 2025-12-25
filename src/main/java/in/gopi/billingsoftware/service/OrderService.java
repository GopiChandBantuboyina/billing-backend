package in.gopi.billingsoftware.service;


import java.time.LocalDate;
import java.util.List;

import in.gopi.billingsoftware.io.OrderRequest;
import in.gopi.billingsoftware.io.OrderResponse;
import in.gopi.billingsoftware.io.PayementVerificationRequest;

public interface OrderService {
	
	OrderResponse createOrder(OrderRequest request);
	
	void deleteOrder(String orderId);
	
	List<OrderResponse> getLatestOrders();
	
	OrderResponse verifyPayment(PayementVerificationRequest request);
	
	Double sumSalesByDate(LocalDate date);
	
	Long countByOrderDate(LocalDate date);
	
	List<OrderResponse> findRecentOrders();
}
