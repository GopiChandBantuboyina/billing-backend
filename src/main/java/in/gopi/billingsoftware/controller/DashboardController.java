package in.gopi.billingsoftware.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gopi.billingsoftware.io.DashboardResponse;
import in.gopi.billingsoftware.io.OrderResponse;
import in.gopi.billingsoftware.service.OrderService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
	
	private final OrderService orderService;

	@GetMapping
	public DashboardResponse getDashboardData()
	{
		LocalDate today = LocalDate.now();
		Double todaySale = orderService.sumSalesByDate(today);
		Long todayOrderCount = orderService.countByOrderDate(today);
		List<OrderResponse> recentOrders = orderService.findRecentOrders();
		return new DashboardResponse(
				todaySale!=null ? todaySale : 0.0,
						
				todayOrderCount != null ? todayOrderCount : 0,
						
				recentOrders);
	}
}
