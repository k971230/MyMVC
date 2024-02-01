package com.spring.app.member.controller;


import java.text.DecimalFormat;
import java.util.HashMap;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.common.AES256;
import com.spring.app.common.Sha256;
import com.spring.app.domain.MemberVO;
import com.spring.app.member.service.MemberService;


@EnableAspectJAutoProxy
@Component
@Controller  
public class MemberController {
	
	@Autowired  
	private MemberService memberservice;
	
	@Autowired
 	private AES256 aES256;
	
	// === 또는 ===
	@PostMapping("/login/login.up")
	public ModelAndView login(ModelAndView mav, HttpServletRequest request) {
		
		String userid = request.getParameter("userid");
		System.out.println("userid=>"+userid);
		String pwd = request.getParameter("pwd");
		System.out.println("pwd=>"+pwd);
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("userid", userid);
		paraMap.put("pwd", Sha256.encrypt(pwd));
		
		System.out.println("암호화된 +>"+Sha256.encrypt(pwd));
		
		mav = memberservice.login(mav, paraMap, request); // 사용자가 입력한 값들을 Map 에 담아서 서비스 객체에게 넘겨 처리하도록 한다.
		
		return mav;
	}
	
	// 로그아웃 처리하기
	@GetMapping("/login/logout.up")
	public ModelAndView logout(ModelAndView mav, HttpServletRequest request) {
		mav = memberservice.logout(mav, request);
		return mav;
	}

	// 회원가입 페이지 불러오기
	@GetMapping("member/memberRegister.up")
	public ModelAndView memberRegister(ModelAndView mav) {
		
		mav.setViewName("tiles1.member.memberRegister");
		return mav;
	}

	
	@PostMapping("member/memberRegisterEnd.up")
	public ModelAndView memberRegisterEnd(ModelAndView mav, MemberVO mvo, HttpServletRequest request) throws Exception {
	
		String userid = request.getParameter("userid");
		String pwd = request.getParameter("pwd");
		
		int n = memberservice.register(mvo, request);
		
		if(n==1) {
		   request.setAttribute("userid", userid); 
		   request.setAttribute("pwd", pwd); 
		
		   mav.setViewName("login/memberRegister_after_autoLogin");
		}	
		else {
			String message = "SQL구문 에러발생";
			String loc = "javascript:history.back()"; // 자바스크립트를 이용한 이전페이지로 이동하는 것. 
			
			request.setAttribute("message", message);
			request.setAttribute("loc", loc);
			
			mav.setViewName("msg");

		}

		return mav;

	}
	
	@ResponseBody
	@PostMapping("member/idDuplicateCheck.up")
	public String idDuplicateCheck(HttpServletRequest request) throws JSONException {
	
		String userid = request.getParameter("userid");
		
		// System.out.println(">>> 확인용 userid => " + userid);
			
			boolean isExists = memberservice.idDuplicateCheck(userid);
			
			JSONObject jsonObj = new JSONObject(); // {}
			jsonObj.put("isExists", isExists);     // {"isExists":true} 또는 {"isExists":false} 으로 만들어준다. 
			
			return jsonObj.toString(); // 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}" 으로 만들어준다. 
		//	System.out.println(">>> 확인용 json => " + json);
		    // >>> 확인용 json => {"isExists":true}
			// >>> 확인용 json => {"isExists":false} 
	}
	
	

	@ResponseBody
	@PostMapping("member/emailDuplicateCheck.up")
	public String emailDuplicateCheck(HttpServletRequest request) throws JSONException {
	
		String email = request.getParameter("email");		
		// System.out.println(">>> 확인용 userid => " + userid);
			
			boolean isExists = memberservice.emailDuplicateCheck(email);
			
			JSONObject jsonObj = new JSONObject(); // {}
			jsonObj.put("isExists", isExists);     // {"isExists":true} 또는 {"isExists":false} 으로 만들어준다. 
			
			return jsonObj.toString(); // 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}" 으로 만들어준다. 
		//	System.out.println(">>> 확인용 json => " + json);
		    // >>> 확인용 json => {"isExists":true}
			// >>> 확인용 json => {"isExists":false} 
	}
	

	@GetMapping("member/memberEdit.up")
	public ModelAndView memberEdit(ModelAndView mav, HttpServletRequest request) {
	    String userid = request.getParameter("userid");
	    HttpSession session = request.getSession();
	    MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

	    if (loginuser != null && loginuser.getUserid().equals(userid)) {
	        // 로그인한 사용자가 자신의 정보를 수정하는 경우
	        mav.setViewName("member/memberEdit");
	    } else {
	        // 로그인한 사용자가 다른 사용자의 정보를 수정하려고 시도하는 경우
	        String message = "다른 사용자의 정보 변경은 불가합니다.!!";
	        String loc = "javascript:history.back()";

	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        mav.setViewName("/WEB-INF/views/msg.jsp");
	    }

	    return mav;
	}
	
	
	@ResponseBody
	@PostMapping("member/emailDuplicateCheck2.up")
	public String emailDuplicateCheck2(HttpServletRequest request) throws JSONException {
	
		String email = request.getParameter("email");		
		String userid  = request.getParameter("userid");
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("userid", userid);
		paraMap.put("email", email);
		// System.out.println(">>> 확인용 userid => " + userid);
			
			boolean isExists = memberservice.emailDuplicateCheck2(paraMap);
			
			JSONObject jsonObj = new JSONObject(); // {}
			jsonObj.put("isExists", isExists);     // {"isExists":true} 또는 {"isExists":false} 으로 만들어준다. 
			
			return jsonObj.toString(); // 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}" 으로 만들어준다. 
		//	System.out.println(">>> 확인용 json => " + json);
		    // >>> 확인용 json => {"isExists":true}
			// >>> 확인용 json => {"isExists":false} 
	}
	

	@ResponseBody
	@PostMapping("member/duplicatePwdCheck.up")
	public String duplicatePwdCheck(HttpServletRequest request) throws JSONException {
	
		String new_pwd = request.getParameter("new_pwd");
		String userid = request.getParameter("userid");
					
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("userid", userid);
		paraMap.put("new_pwd", Sha256.encrypt(new_pwd));
		// System.out.println(">>> 확인용 userid => " + userid);
			
			boolean isExists = memberservice.duplicatePwdCheck(paraMap);
			
			JSONObject jsonObj = new JSONObject(); // {}
			jsonObj.put("isExists", isExists);     // {"isExists":true} 또는 {"isExists":false} 으로 만들어준다. 
			
			return jsonObj.toString(); // 문자열 형태인 "{"isExists":true}" 또는 "{"isExists":false}" 으로 만들어준다. 
		//	System.out.println(">>> 확인용 json => " + json);
		    // >>> 확인용 json => {"isExists":true}
			// >>> 확인용 json => {"isExists":false} 
	}

	
	// 회원 정보 수정 처리
	@PostMapping("member/memberEditEnd.up")
	public ModelAndView memberEditEnd(ModelAndView mav, HttpServletRequest request) {
	    // POST 방식으로 요청이 들어왔을 때 실행되는 메서드

	    // 요청 파라미터에서 회원 정보를 받아옴
	    String userid = request.getParameter("userid");
	    String name = request.getParameter("name");
	    String pwd = request.getParameter("pwd"); // 암호화할 비밀번호
	    String email = request.getParameter("email");
	    String hp1 = request.getParameter("hp1");
	    String hp2 = request.getParameter("hp2");
	    String hp3 = request.getParameter("hp3");
	    String postcode = request.getParameter("postcode");
	    String address = request.getParameter("address");
	    String detailaddress = request.getParameter("detailaddress");
	    String extraaddress = request.getParameter("extraaddress");

	    // 전화번호 조합
	    String mobile = hp1 + hp2 + hp3;

	    try {
	        // 비밀번호를 SHA-256 해시 함수를 사용하여 암호화
	    	
	    	String new_pwd = Sha256.encrypt(pwd);
	        
	    	System.out.println(pwd);
	    	System.out.println(new_pwd);
	        // 회원 정보 객체 생성 및 세팅
	        MemberVO mvo = new MemberVO(userid, new_pwd, name, email, mobile, postcode, address, detailaddress, extraaddress);

	        // DAO를 통해 회원 정보를 업데이트
	        int n = memberservice.updateMember(mvo);

	        if (n == 1) {
	            // 회원 정보 업데이트에 성공한 경우
	            HttpSession session = request.getSession();
	            MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");

	            // 세션에 저장된 사용자 정보 업데이트
	            loginuser.setName(name);
				loginuser.setEmail(email);
				loginuser.setMobile(mobile);
	            loginuser.setPostcode(postcode);
	            loginuser.setAddress(address);
	            loginuser.setDetailaddress(detailaddress);
	            loginuser.setExtraaddress(extraaddress);

	            // 메시지 및 이동 경로 설정
	            String message = "회원정보 수정 성공!!";
	            String loc = request.getContextPath() + "/index.up"; // 시작페이지로 이동

	            // ModelAndView 객체에 메시지와 이동 경로 설정
	            mav.addObject("message", message);
	            mav.addObject("loc", loc);
	            mav.setViewName("msg");
	        } else {
	            // 회원 정보 업데이트에 실패한 경우
	            String message = "회원정보 수정 실패";
	            String loc = "javascript:history.back()"; // 이전 페이지로 이동

	            // ModelAndView 객체에 메시지와 이동 경로 설정
	            mav.addObject("message", message);
	            mav.addObject("loc", loc);
	            mav.setViewName("msg");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 예외 처리
	    }

	    // ModelAndView 객체 반환
	    return mav;
	}
	

	@GetMapping("member/coinPurchaseTypeChoice.up")
	public ModelAndView coinPurchaseTypeChoice(ModelAndView mav, HttpServletRequest request) {
	    String userid = request.getParameter("userid");
	    
	    HttpSession session = request.getSession();
	    MemberVO loginuser = (MemberVO) session.getAttribute("loginuser"); 
	    
	    if(loginuser != null && loginuser.getUserid().equals(userid)) {
	        // 로그인한 사용자가 자신의 코인을 수정하는 경우 
	        mav.setViewName("member/coinPurchaseTypeChoice");
	    } else {
	        // 로그인한 사용자가 다른 사용자의 코인을 충전하려고 시도하는 경우 
	        String message = "다른 사용자의 코인 충전 시도는 불가합니다.!!";
	        String loc = "javascript:history.back()";
	        
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        mav.setViewName("/WEB-INF/msg");
	    }
	    return mav;
	}


	@GetMapping("member/coinPurchaseEnd.up")
	public ModelAndView coinPurchaseEnd(ModelAndView mav, HttpServletRequest request) {
	    String userid = request.getParameter("userid");
	    
	    HttpSession session = request.getSession();
	    MemberVO loginuser = (MemberVO) session.getAttribute("loginuser"); 
	    
	    if(loginuser != null && loginuser.getUserid().equals(userid)) {
	        // 로그인한 사용자가 자신의 코인을 수정하는 경우 
	        
	        String coinmoney = request.getParameter("coinmoney");
	        
	        String productName = "코인충전"; // "새우깡";
	        int productPrice = 100;
	        
	        request.setAttribute("productName", productName);
	        request.setAttribute("productPrice", productPrice);
	        request.setAttribute("email", loginuser.getEmail());
	        request.setAttribute("name", loginuser.getName());
	        request.setAttribute("mobile", loginuser.getMobile());
	        
	        request.setAttribute("userid", userid);
	        request.setAttribute("coinmoney", coinmoney);
	        
	        mav.setViewName("member/paymentGateway");
	    } else {
	        // 로그인한 사용자가 다른 사용자의 코인을 충전하려고 결제를 시도하는 경우 
	        String message = "다른 사용자의 코인충전 결제 시도는 불가합니다.!!";
	        String loc = "javascript:history.back()";
	        
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        
	        mav.setViewName("msg");
	    }
	    
	    return mav;
	}




	@PostMapping("member/coinUpdateLoginUser.up")
	public ModelAndView coinUpdateLoginUser(ModelAndView mav, HttpServletRequest request) {
	    
	    String userid = request.getParameter("userid");
	    String coinmoney = request.getParameter("coinmoney");
	    
	    Map<String, String> paraMap = new HashMap<>();
	    paraMap.put("userid", userid);
	    paraMap.put("coinmoney", coinmoney);
	    paraMap.put("point", String.valueOf(Integer.parseInt(coinmoney) / 100));
	    	        
	    String message = "";
	    String loc = "";
	    
	    try {
	        int n = memberservice.coinUpdateLoginUser(paraMap); // DB에 코인 및 포인트 증가하기 
	        
	        if(n==1) {
	            
	            HttpSession session = request.getSession();
	            MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
	            
	            // !!!!! 세션값을 변경하기 !!!!! //
	            loginuser.setCoin( loginuser.getCoin() + Integer.parseInt(coinmoney) );
	            loginuser.setPoint( loginuser.getPoint() + (int) (Integer.parseInt(coinmoney)*0.01) );
	            
	            
	            DecimalFormat df = new DecimalFormat("#,###");
	            //  예를 들면
	            //  System.out.println(df.format(2005100));
	            //  "2,005,100" 
	            
	            message = loginuser.getName() + "님의 "+ df.format(Long.parseLong(coinmoney)) +"원 결제가 완료되었습니다.";
	            loc = request.getContextPath()+"/index.up";
	        }
	        
	    } catch(Exception e) {
	        message = "코인액 결제가 DB오류로 인해 실패되었습니다.";
	        loc = "javascript:history.back()";
	    }
	    
	    mav.addObject("message", message);
	    mav.addObject("loc", loc);
	    
	    mav.setViewName("msg");
	    
	    return mav;
	}


	
	


}
	
	
	


	

