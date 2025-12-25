package in.gopi.billingsoftware.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.razorpay.RazorpayException;

import in.gopi.billingsoftware.io.OrderResponse;
import in.gopi.billingsoftware.io.PayementVerificationRequest;
import in.gopi.billingsoftware.io.PaymentRequest;
import in.gopi.billingsoftware.io.RazorpayOrderResponse;
import in.gopi.billingsoftware.service.OrderService;
import in.gopi.billingsoftware.service.RazorpayService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
	
	private final RazorpayService razorpayService;
	private final OrderService orderService;
	@PostMapping("/create-order")
	@ResponseStatus(HttpStatus.CREATED)
	public RazorpayOrderResponse createRazorPayOrder(@RequestBody PaymentRequest request) throws RazorpayException
	{
		return razorpayService.createOrder(request.getAmount(), request.getCurrency());
	}
	
	@PostMapping("/verify")
	public OrderResponse verifyPayment(@RequestBody PayementVerificationRequest request)
	{
		return orderService.verifyPayment(request);
		
	}

}
