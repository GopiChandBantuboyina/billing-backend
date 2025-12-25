package in.gopi.billingsoftware.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import in.gopi.billingsoftware.entity.OrderEntity;
import in.gopi.billingsoftware.entity.OrderItemEntity;
import in.gopi.billingsoftware.io.OrderRequest;
import in.gopi.billingsoftware.io.OrderResponse;
import in.gopi.billingsoftware.io.PayementVerificationRequest;
import in.gopi.billingsoftware.io.PaymentDetails;
import in.gopi.billingsoftware.io.PaymentMethod;
import in.gopi.billingsoftware.repository.OrderEntityRepository;
import in.gopi.billingsoftware.service.OrderService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
	
	private final OrderEntityRepository orderEntityRepository;

	@Override
	public OrderResponse createOrder(OrderRequest request) {
		//convert request to entity
		OrderEntity newOrder =  convertToOrderEntity(request);
		
		PaymentDetails paymentDetails = new PaymentDetails();
		paymentDetails.setStatus(newOrder.getPaymentMethod() == PaymentMethod.CASH ?
				PaymentDetails.PaymentStatus.COMPLETED : PaymentDetails.PaymentStatus.PENDING);
	
		newOrder.setPaymentDetails(paymentDetails);
		
		List<OrderItemEntity> orderItems =  request.getCartItems().stream()
		.map(this::convertToOrderItemEntity)
		.collect(Collectors.toList());
		
		newOrder.setItems(orderItems);
		
		newOrder = orderEntityRepository.save(newOrder);
		
		return convertToResponse(newOrder);
	}
	
	private OrderEntity convertToOrderEntity(OrderRequest request)
	{
		
		return OrderEntity.builder()
		.customerName(request.getCustomerName())
		.phoneNumber(request.getPhoneNumber())
		.subtotal(request.getSubtotal())
		.tax(request.getTax())
		.grandTotal(request.getGrandTotal())
		.paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
		.build();
		
		
	}
	
	private OrderResponse convertToResponse(OrderEntity newOrder)
	{
		
		return OrderResponse.builder()
		.orderId(newOrder.getOrderId())
		.customerName(newOrder.getCustomerName())
		.phoneNumber(newOrder.getPhoneNumber())
		.subtotal(newOrder.getSubtotal())
		.tax(newOrder.getTax())
		.grandTotal(newOrder.getGrandTotal())
		.paymentMethod(newOrder.getPaymentMethod())
		.items(newOrder.getItems().stream()
				.map(this::convertToItemResponse)
				.collect(Collectors.toList()))
		.paymentDetails(newOrder.getPaymentDetails())
		.createdAt(newOrder.getCreatedAt())
		.build();
		
		
	}
	
	private OrderItemEntity convertToOrderItemEntity(OrderRequest.OrderItemRequest orderItemRequest)
	{
		return OrderItemEntity.builder()
		.itemId(orderItemRequest.getItemId())
		.name(orderItemRequest.getName())
		.price(orderItemRequest.getPrice())
		.quantity(orderItemRequest.getQuantity())
		.build();
		
	}
	
	
	private OrderResponse.OrderItemResponse convertToItemResponse(OrderItemEntity orderItemEntity)
	{
		return OrderResponse.OrderItemResponse.builder()
		.itemId(orderItemEntity.getItemId())
		.name(orderItemEntity.getName())
		.price(orderItemEntity.getPrice())
		.quantity(orderItemEntity.getQuantity())
		.build();
		
		
	}
	
	

	@Override
	public void deleteOrder(String orderId) {

		OrderEntity orderEntity =  orderEntityRepository.findByOrderId(orderId)
		.orElseThrow(() -> new RuntimeException("Order not found"));
		
		orderEntityRepository.delete(orderEntity);
	}

	@Override
	public List<OrderResponse> getLatestOrders() {
		
		return orderEntityRepository.findAllByOrderByCreatedAtDesc()
		.stream()
		.map(this::convertToResponse)
		.collect(Collectors.toList());
	}

	@Override
	public OrderResponse verifyPayment(PayementVerificationRequest request) {
		OrderEntity existingOrder =  orderEntityRepository.findByOrderId(request.getOrderId())
				.orElseThrow(() -> new RuntimeException("Order not found"));
				
				if(!verifyRazorpaySignature(request.getRazorpayOrderId(),
						request.getRazorpayPaymentId(),
						request.getRazorPaySignature()))
				{
					throw new RuntimeException("Payment verification failed");
				}
				
				PaymentDetails paymentDetails = existingOrder.getPaymentDetails();
				paymentDetails.setRazorpayOrderId(request.getRazorpayOrderId());
				paymentDetails.setRazorpayPaymentId(request.getRazorpayPaymentId());
				paymentDetails.setRazorpaySignature(request.getRazorPaySignature());
				paymentDetails.setStatus(PaymentDetails.PaymentStatus.COMPLETED);
				
				existingOrder = orderEntityRepository.save(existingOrder);
				
				return convertToResponse(existingOrder);

			}
			
			private boolean verifyRazorpaySignature(String razorpayOrderId, String razorpayPayementId, String razorpaySignature)
			{
				//for time being return true..
				//but in production proper signature verification.... 
				return true;
			}

			@Override
			public Double sumSalesByDate(LocalDate date) {
				return orderEntityRepository.sumSalesByDate(date);
			}

			@Override
			public Long countByOrderDate(LocalDate date) {
				return orderEntityRepository.countByOrderDate(date);
			}

			public List<OrderResponse> findRecentOrders() {
				
				return orderEntityRepository.findRecentOrders(PageRequest.of(0, 5))
						.stream()
						.map(orderEntity -> convertToResponse(orderEntity))
						.collect(Collectors.toList());
			}
	

}
