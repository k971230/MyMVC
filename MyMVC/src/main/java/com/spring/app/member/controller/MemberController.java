package com.spring.app.member.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.common.Sha256;
import com.spring.app.domain.ImageVO;
import com.spring.app.member.service.MemberService;
import com.spring.app.myshop.service.ProductService;

@EnableAspectJAutoProxy
@Component
@Controller  
public class MemberController {
	
	@Autowired  
	private MemberService memberservice;
	
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

}
