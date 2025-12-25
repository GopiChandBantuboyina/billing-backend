package in.gopi.billingsoftware.service;

import com.razorpay.RazorpayException;

import in.gopi.billingsoftware.io.RazorpayOrderResponse;

public interface RazorpayService {
	
	RazorpayOrderResponse createOrder(Double amount,String currency) throws RazorpayException;

}
