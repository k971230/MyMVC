package com.spring.app.member.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.common.AES256;
import com.spring.app.common.Sha256;
import com.spring.app.domain.MemberVO;
import com.spring.app.member.domain.MemberDAO;

@Service
public class MemberService_imple implements MemberService {

	@Autowired  
	private MemberDAO memberdao;
	
	@Autowired
 	private AES256 aES256;
	
	
	///////////////////////////////////////////////////////////////////////////  
	@Override
	public ModelAndView login(ModelAndView mav, Map<String, String> paraMap, HttpServletRequest request) {
		
		// ==== 로그인 처리하기 ==== //
		MemberVO loginuser = memberdao.getLoginMember(paraMap);
		
		if(loginuser != null && loginuser.getPwdchangegap() >= 3) {
			// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 
			loginuser.setRequirePwdChange(true); // 로그인시 암호를 변경해라는 alert 를 띄우도록 한다.
		}
		
		if(loginuser != null && loginuser.getLastlogingap() >= 12) {
			// 마지막으로 로그인 한 날짜시간이 현재시각으로 부터 1년이 지났으면 휴면으로 지정
			loginuser.setIdle(1);
			
			// === tbl_member 테이블의 idle 컬럼의 값을 1로 변경하기 === // 
			int n = memberdao.updateIdle(paraMap.get("userid"));
		}
		
		if(loginuser != null) {
			try {
				 String email = aES256.decrypt(loginuser.getEmail()); 
				 loginuser.setEmail(email);
				 String mobile = aES256.decrypt(loginuser.getMobile()); 
				 loginuser.setMobile(mobile);
			} catch (UnsupportedEncodingException | GeneralSecurityException e) {
				e.printStackTrace();
			}
		}
		
		if(loginuser == null) { // 로그인 실패시
			String message = "아이디 또는 암호가 틀립니다.";
		 // String loc = "javascript:history.back()";
		 // String referer = request.getHeader("referer"); 
		 // request.getHeader("referer"); 은 이전 페이지의 URL을 가져오는 것이다.
			String loc = request.getHeader("referer");
			
			mav.addObject("message", message);
			mav.addObject("loc", loc);
			
			mav.setViewName("msg");
			// /WEB-INF/views/msg.jsp 파일을 생성한다.
		}
		else { // 아이디와 암호가 존재하는 경우 
			
			if(loginuser.getIdle() == 1) { // 로그인 한지 1년이 경과한 경우
				
				String message = "로그인을 한지 1년이 지나서 휴면상태로 되었습니다.\\n관리자에게 문의 바랍니다.";
				String loc = "/MyMVC/index.up";
				// 원래는 위와 같이 index.action 이 아니라 휴면인 계정을 풀어주는 페이지로 잡아주어야 한다.
				
				mav.addObject("message", message);
				mav.addObject("loc", loc);
				
				mav.setViewName("msg");
			}
			
			else {// 로그인 한지 1년 이내인 경우
				
				HttpSession session = request.getSession();
				// 메모리에 생성되어져 있는 session 을 불러온다.
				
				session.setAttribute("loginuser", loginuser);
				// session(세션)에 로그인 되어진 사용자 정보인 loginuser 을 키이름을 "loginuser" 으로 저장시켜두는 것이다.
				
			
				if(loginuser.isRequirePwdChange() == true) { // 암호를 마지막으로 변경한 것이 3개월이 경과한 경우
					
					String message = "비밀번호를 변경하신지 3개월이 지났습니다.\\n암호를 변경하시는 것을 추천합니다.";
					String loc = "/MyMVC/index.up";
					// 원래는 위와 같이 index.action 이 아니라 사용자의 비밀번호를 변경해주는 페이지로 잡아주어야 한다.
					
					mav.addObject("message", message);
					mav.addObject("loc", loc);
					
					mav.setViewName("msg");
				}
				
				else { // 암호를 마지막으로 변경한 것이 3개월 이내인 경우
					
					// 로그인을 해야만 접근할 수 있는 페이지에 로그인을 하지 않은 상태에서 접근을 시도한 경우 
					// "먼저 로그인을 하세요!!" 라는 메시지를 받고서 사용자가 로그인을 성공했다라면
					// 화면에 보여주는 페이지는 시작페이지로 가는 것이 아니라
					// 조금전 사용자가 시도하였던 로그인을 해야만 접근할 수 있는 페이지로 가기 위한 것이다.
					String goBackURL = (String) session.getAttribute("goBackURL");
					
					if(goBackURL != null) {
						mav.setViewName("redirect:"+goBackURL);
						session.removeAttribute("goBackURL"); // 세션에서 반드시 제거해주어야 한다. 
					}
					else {
						mav.setViewName("redirect:/index.up"); // 시작페이지로 이동
					}
				}
			}
		}
		
		return mav;
	}
	
    ///////////////////////////////////////////////////////////////////////////
	
	// 로그아웃 처리하기
	@Override
	public ModelAndView logout(ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		session.invalidate();
		
		String message = "로그아웃 되었습니다.";
		String loc = "/MyMVC/index.up";
		
		mav.addObject("message", message);
		mav.addObject("loc", loc);
		
		mav.setViewName("msg");
		
		return mav;
	}

	@Override
	public int register(MemberVO mvo, HttpServletRequest request) throws Exception {
		
		String pwd = request.getParameter("pwd");
		String email = request.getParameter("email");
		String hp1 = request.getParameter("hp1");
		String hp2 = request.getParameter("hp2");
		String hp3 = request.getParameter("hp3");
		// System.out.println("파라미터로 hp1" + hp1); 
		String mobile = hp1+hp2+hp3;
		mvo.setPwd(Sha256.encrypt(pwd));
		mvo.setMobile(aES256.encrypt(mobile));
		mvo.setEmail(aES256.encrypt(email));
		//System.out.println("VO로 mobile" + mvo.getMobile());
		
		int n = memberdao.registerMember(mvo);

		return n;
	}

	@Override
	public boolean idDuplicateCheck(String userid) {
		boolean isExists = false;
		
		String result  = memberdao.idDuplicateCheck(userid); // 행이 있으면(중복된 userid) true,
		                      // 행이 없으면(사용가능한 userid) false
		// 결과값이 비어있는지 확인
		if (result != null && !result.isEmpty()) {
			isExists = true;
		} else {
			isExists = false;
		}
		return isExists;
	}

	@Override
	public boolean emailDuplicateCheck(String email) {
		boolean isExists = false;
		try {
			email = aES256.encrypt(email);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result  = memberdao.emailDuplicateCheck(email); // 행이 있으면(중복된 email) true,
		                      // 행이 없으면(사용가능한 email) false
		// 결과값이 비어있는지 확인
		if (result != null && !result.isEmpty()) {
			isExists = true;
		} else {
			isExists = false;
		}
		return isExists;
	}
	

	@Override
	public boolean emailDuplicateCheck2(Map<String, String> paraMap) {
		boolean isExists = false;
		try {
			paraMap.put("email", aES256.encrypt(paraMap.get("email")));
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result  = memberdao.emailDuplicateCheck2(paraMap); // 행이 있으면(중복된 email) true,
		                      // 행이 없으면(사용가능한 email) false

		// 결과값이 비어있는지 확인
		if (result != null && !result.isEmpty()) {
			isExists = true;
		} else {
			isExists = false;
		}
		return isExists;
	}

	@Override
	public boolean duplicatePwdCheck(Map<String, String> paraMap) {
		
		boolean isExists = false;

		String result  = memberdao.duplicatePwdCheck(paraMap); // 행이 있으면(중복된 email) true,
		// 결과값이 비어있는지 확인
		if (result != null && !result.isEmpty()) {
			isExists = true;
		} else {
			isExists = false;
		}
		return isExists;
	}

	@Override
	public int updateMember(MemberVO mvo) {

		int n = memberdao.updateMember(mvo); // 행이 있으면(중복된 email) true,
		
		return n;
	}

	@Override
	public int coinUpdateLoginUser(Map<String, String> paraMap) {

		int n = memberdao.coinUpdateLoginUser(paraMap); // 행이 있으면(중복된 email) true,
		
		return n;
	}

	
}
