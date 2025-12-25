package in.gopi.billingsoftware.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayementVerificationRequest {
	
	private String razorpayOrderId;
	private String razorpayPaymentId;
	private String razorPaySignature;
	private String orderId; //we created

}
