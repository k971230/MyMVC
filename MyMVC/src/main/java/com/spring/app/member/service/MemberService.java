package com.spring.app.member.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public interface MemberService {
	
	// 로그인 하기
	ModelAndView login(ModelAndView mav, Map<String, String> paraMap, HttpServletRequest request);
	
	// 로그아웃 처리하기
	ModelAndView logout(ModelAndView mav, HttpServletRequest request);

}
