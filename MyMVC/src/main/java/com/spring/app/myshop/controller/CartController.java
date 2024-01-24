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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.common.Sha256;
import com.spring.app.domain.CartVO;
import com.spring.app.domain.MemberVO;
import com.spring.app.member.service.MemberService;
import com.spring.app.myshop.service.CartService;

@Controller
public class CartController {
	
	@Autowired  
	private CartService service;
	
	// 장바구니 보기
	@GetMapping("/shop/cartList.up")
	public ModelAndView requiredLogin_cartList(HttpServletRequest request,HttpServletResponse response,ModelAndView mav) {
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
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
		
		
		// 로그인을 한 상태이라면
		// 장바구니 테이블(tbl_cart)에 해당 제품을 담아야 한다.
		// 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고, 
        // 장바구니 테이블에 해당 제품이 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다라면 tbl_cart 테이블에 update 를 해야한다.
		
		String method = request.getMethod();
		
		if("POST".equalsIgnoreCase(method)) {
			//POST 방식이라면
			
			String oqty = request.getParameter("oqty"); // 주문량
			String pnum = request.getParameter("pnum"); // 제품번호
			
			HttpSession session = request.getSession();// 사용자ID 
			
			MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
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
              mav.setViewName("/WEB-INF/msg.jsp");
		}
		else {
			
			  //GET 방식이라면
			  String message = "비정상적인 경로로 들어왔습니다";
              String loc = "javascript:history.back()";
               
              mav.addObject("message", message);
              mav.addObject("loc", loc);
              
              //  super.setRedirect(false);   
              mav.setViewName("/WEB-INF/msg.jsp");
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
