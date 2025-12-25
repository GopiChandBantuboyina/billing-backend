package in.gopi.billingsoftware.service.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import in.gopi.billingsoftware.io.RazorpayOrderResponse;
import in.gopi.billingsoftware.service.RazorpayService;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RazorpayServiceImpl implements RazorpayService {

	
	@Value("${razorpay.key.id}")
	private String razorpayKeyId;
	
	@Value("${razorpay.key.secret}")
	private String razorpayKeySecret;
	
	@Override
	public RazorpayOrderResponse createOrder(Double amount, String currency)
	        throws RazorpayException {

	    RazorpayClient razorpayClient =
	            new RazorpayClient(razorpayKeyId, razorpayKeySecret);

	    if (currency == null || currency.isBlank()) {
	        throw new IllegalArgumentException("Currency must not be null or empty");
	    }

	    int amountInPaise = (int) Math.round(amount * 100); 

	    JSONObject orderRequest = new JSONObject();
	    orderRequest.put("amount", amountInPaise);
	    orderRequest.put("currency", currency);
	    orderRequest.put("receipt", "order_rcpid_" + System.currentTimeMillis());
	    orderRequest.put("payment_capture", 1);

	    Order order = razorpayClient.orders.create(orderRequest);

	    return convertToResponse(order);
	}

	private RazorpayOrderResponse convertToResponse(Order order)
	{
		return RazorpayOrderResponse.builder()
		.id(order.get("id"))
		.entity(order.get("entity"))
		.amount(order.get("amount"))
		.currency(order.get("currency"))
		.status(order.get("status"))
		.created_at(order.get("created_at"))
		.receipt(order.get("receipt"))
		.build();
		
		
	}
	
	
	
}
