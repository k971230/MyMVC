package com.spring.app.member.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.spring.app.domain.MemberVO;

public interface MemberService {
	
	// 로그인 하기
	ModelAndView login(ModelAndView mav, Map<String, String> paraMap, HttpServletRequest request);
	
	// 로그아웃 처리하기
	ModelAndView logout(ModelAndView mav, HttpServletRequest request);

	// 회원가입
	int register(MemberVO mvo, HttpServletRequest request)  throws Exception;
	
	//아이디 중복검사
	boolean idDuplicateCheck(String userid);

	//메일 중복검사
	boolean emailDuplicateCheck(String email);

	//개인정보 변경시 메일 중복검사
	boolean emailDuplicateCheck2(Map<String, String> paraMap);

	// 사용중인 비밀번호인지 확인
	boolean duplicatePwdCheck(Map<String, String> paraMap);

	// 회원정보 수정
	int updateMember(MemberVO mvo);
	
	// 코인충전
	int coinUpdateLoginUser(Map<String, String> paraMap);
	
	

}
