package com.spring.app.myshop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.common.Sha256;
import com.spring.app.domain.MemberVO;
import com.spring.app.member.service.MemberService;
import com.spring.app.myshop.service.CartService;

@Controller
public class CartController {
	
	@Autowired  
	private CartService service;
	
	// === 또는 ===
		@PostMapping("/shop/cartList.up")
		public ModelAndView requiredLogin_cartList(ModelAndView mav, HttpServletRequest request) {
			
			HttpSession session = request.getSession();
			MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
			
			if(loginuser != null) { 
				
				
			}
			else {
				
				
				
			}
			
			return mav;
		}
	
	
}
