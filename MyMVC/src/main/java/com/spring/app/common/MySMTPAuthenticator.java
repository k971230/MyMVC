package com.spring.app.common;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

//==== #188. Spring Scheduler(스프링스케줄러06)
//=== Spring Scheduler(스프링스케줄러)를 사용한 email 발송하기 === 
//=== GoogleMail 을 사용할 수 있도록 Google email 계정 및 암호 입력하기 ===

public class MySMTPAuthenticator extends Authenticator {
	
	@Override
	   public PasswordAuthentication getPasswordAuthentication() {
		
		// Gmail 의 경우 @gmail.com 을 제외한 아이디만 입력한다.
		return new PasswordAuthentication("qkrdbstn777","ocmm ahdy nnwy ufow");
		// "ocmm ahdy nnwy ufow" 은 Google에 로그인 하기위한 앱 비밀번호 이다.
	}
}
