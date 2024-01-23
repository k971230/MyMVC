package com.spring.app.order.controller;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@EnableAspectJAutoProxy
@Component
@Controller  
public class OrderController {
	
	@GetMapping(value="/order/orderList.up") 
	public ModelAndView orderList(ModelAndView mav) {
		
		
		
		mav.setViewName("tiles1.order.orderList");
		return mav;
	}
	
}
