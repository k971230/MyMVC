package com.spring.app.myshop.controller;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.spring.app.domain.CartVO;
import com.spring.app.domain.MemberVO;

import com.spring.app.myshop.service.CartService;


@Controller
@EnableAspectJAutoProxy  // AOP(Aspect Oriented Programming)를 사용하기 위한 용도
public class CartController {
	
	@Autowired  
	private CartService service;
	
	// 장바구니 보기
	@GetMapping("/shop/cartList.up")
	public ModelAndView cartList(HttpServletRequest request,HttpServletResponse response,ModelAndView mav) {
		
		HttpSession session = request.getSession();
		
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
	 	if(loginuser == null) {
	 		String message = "먼저 로그인 하세요~~";
	 		String loc = request.getContextPath()+"/index.up";
	 		
	 		mav.addObject("message", message);
	 		mav.addObject("loc", loc);
	 		
	 		mav.setViewName("msg");
	 		
	 		return mav;
	 	}
		
		List<CartVO> cartList = service.selectProductCart(loginuser.getUserid());
		
		Map<String,String> sumMap = service.selectCartSumPricePoint(loginuser.getUserid());
		
		/*
		 * for(CartVO amap : cartList) { System.out.println(amap.getProd().getPqty()); }
		 */
		
		mav.addObject("cartList", cartList); 
		mav.addObject("sumMap", sumMap);
		
		mav.setViewName("tiles1.myshop.cartList");
		
		return mav;
	}
		
		
	@RequestMapping("/shop/cartAdd.up")
	public ModelAndView requiredLogin_cartAdd(HttpServletRequest request,HttpServletResponse response,ModelAndView mav) {	
		
		HttpSession session = request.getSession();
		
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
	 	if(loginuser == null) {
	 		String message = "먼저 로그인 하세요~~";
	 		String loc = request.getContextPath()+"/index.up";
	 		
	 		mav.addObject("message", message);
	 		mav.addObject("loc", loc);
	 		
	 		mav.setViewName("msg");
	 		
	 		return mav;
	 	}
	 	
		String method = request.getMethod();
		
		if("POST".equalsIgnoreCase(method)) {
			//POST 방식이라면
			
			String oqty = request.getParameter("oqty"); // 주문량
			String pnum = request.getParameter("pnum"); // 제품번호
			
			
			String userid = loginuser.getUserid();
			
			
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("oqty", oqty);
			paraMap.put("pnum", pnum);
			paraMap.put("userid", userid );
			try {
			int n = service.addCart(paraMap); // 장바구니에 해당사용자의 기존 제품이 없을경우 insert 하고 
								   		   // 장바구니에 해당사용자의 기존 제품이 있을경우 update 한다.
			if(n==1) {
				mav.addObject("message", "장바구니 담기 성공!!");
				mav.addObject("loc",request.getContextPath()+"/shop/cartList.up");
	            // 장바구니 목록 보여주기 페이지로 이동한다.
	            
	           
	        }
				
			}catch (SQLException e) {
				mav.addObject("message", "장바구니 담기 실패!!");
				mav.addObject("loc","javascript:history.back()");
				
			}
			// super.setRedirect(false);   
              mav.setViewName("msg");
		}
		else {
			
			  //GET 방식이라면
			  String message = "비정상적인 경로로 들어왔습니다";
              String loc = "javascript:history.back()";
               
              mav.addObject("message", message);
              mav.addObject("loc", loc);
              
              //  super.setRedirect(false);   
              mav.setViewName("msㅎ");
		}
	
	
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/shop/cartEdit.up", produces="text/plain;charset=UTF-8")
	public String requiredLogin_cartEdit(HttpServletRequest request,HttpServletResponse response) {	
		
		String method = request.getMethod();
	      
		
		 JSONObject jsonObj = new JSONObject();
		 
	      if(!"POST".equalsIgnoreCase(method)) {
	         // GET 방식이라면
	         
	    	 try {
					jsonObj.put("n", 100);            
					
				} catch (JSONException e) {
					e.printStackTrace();
				} 
	         
	      }
	      else if("POST".equalsIgnoreCase(method)) {
	    	  //post 방식이고 로그인을 했다라면
	    	  
	    	  String cartno = request.getParameter("cartno");
	    	  String oqty = request.getParameter("oqty");
	    	  
	    	  
	    	  
	    	  Map<String, String> paraMap = new HashMap<>();
	    	  paraMap.put("cartno", cartno);
	    	  paraMap.put("oqty", oqty);
	    	  
	    	  // 장바구니 테이블에서 특정제품의 주문량 변경시키기
	    	  int n = service.updateCart(paraMap);
	    	  
	    	  try {
					jsonObj.put("n", n);
					
				} catch (JSONException e) {
					e.printStackTrace();
				} 
	         
	    	}
	      return jsonObj.toString();
	
	}// end of public String requiredLogin_cartEdit(HttpServletRequest request,HttpServletResponse response)
	
	
	@ResponseBody
	@RequestMapping(value="/shop/cartDel.up", produces="text/plain;charset=UTF-8")
	public String requiredLogin_cartDel(HttpServletRequest request,HttpServletResponse response) {	
		
		String method = request.getMethod();
	      
		 JSONObject jsonObj = new JSONObject();
		 
	      if(!"POST".equalsIgnoreCase(method)) {
	         // GET 방식이라면
	         
	    	 try {
					jsonObj.put("n", 100);            
					
				} catch (JSONException e) {
					e.printStackTrace();
				} 
	         
	      }
	      else if("POST".equalsIgnoreCase(method)) {
	    	  //post 방식이고 로그인을 했다라면
	    	  
	    	  String cartno = request.getParameter("cartno");
	    	  
	    	 
	    	  
	    	  //장바구니 테이블에서 특정제품을 장바구니에서 비우기
	    	  int n = service.delCart(cartno);
	    	  
	    	  try {
					jsonObj.put("n", n);
					
				} catch (JSONException e) {
					e.printStackTrace();
				} 
	    	  
	    	}
		
		return jsonObj.toString();
	}
}
