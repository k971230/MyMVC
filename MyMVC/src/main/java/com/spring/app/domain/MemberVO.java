package com.spring.app.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberVO {

	private String userid;             // 회원아이디
	private String pwd;                // 비밀번호 (SHA-256 암호화 대상)
	private String name;               // 회원명
	private String email;              // 이메일 (AES-256 암호화/복호화 대상)
	private String mobile;             // 연락처 (AES-256 암호화/복호화 대상) 
	private String postcode;           // 우편번호
	private String address;            // 주소
	private String detailaddress;      // 상세주소
	private String extraaddress;       // 참고항목
	private String gender;             // 성별   남자:1  / 여자:2
	private String birthday;           // 생년월일   
	private int coin;                  // 코인액
	private int point;                 // 포인트 
	private String registerday;        // 가입일자 
	private String lastpwdchangedate;  // 마지막으로 암호를 변경한 날짜  
	private int status;                // 회원탈퇴유무   1: 사용가능(가입중) / 0:사용불능(탈퇴) 
	private int idle;                  // 휴면유무      0 : 활동중  /  1 : 휴면중
	                                   // 마지막으로 로그인 한 날짜시간이 현재시각으로 부터 1년이 지났으면 휴면으로 지정
	
    ////////////////////////////////////////////////////////////////
	
	private boolean requirePwdChange = false;
	// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지났으면 true
	// 마지막으로 암호를 변경한 날짜가 현재시각으로 부터 3개월이 지나지 않았으면 false

    ////////////////////////////////////////////////////////////////
	public MemberVO() {} // 기본 생성자.
	
	// 회원가입시 생성자.
	public MemberVO(String userid, String pwd, String name, String email, String mobile, String postcode,
			        String address, String detailaddress, String extraaddress, String gender, String birthday) { 
		this.userid = userid;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
		this.mobile = mobile;
		this.postcode = postcode;
		this.address = address;
		this.detailaddress = detailaddress;
		this.extraaddress = extraaddress;
		this.gender = gender;
		this.birthday = birthday;
	}
	//회원수정시 생성자.
	public MemberVO(String userid, String pwd, String name, String email, String mobile, String postcode,
	        String address, String detailaddress, String extraaddress) { 
	this.userid = userid;
	this.pwd = pwd;
	this.name = name;
	this.email = email;
	this.mobile = mobile;
	this.postcode = postcode;
	this.address = address;
	this.detailaddress = detailaddress;
	this.extraaddress = extraaddress;
	
}
	
	
	
	/////////////////////////////////////////////////////////////////
	
	public int getAge() {
		
	      int age = 0;
	      
	      // 회원의 올해생일이 현재날짜 보다 이전이라면 
	      // 만나이 = 현재년도 - 회원의 태어난년도 
	            
	      // 회원의 올해생일이 현재날짜 보다 이후이라면
	      // 만나이 = 현재년도 - 회원의 태어난년도 - 1
	            
	      Date now = new Date(); // 현재시각
	      SimpleDateFormat sdfmt = new SimpleDateFormat("yyyyMMdd");
	      String str_now = sdfmt.format(now); // "20231018"
	            
	      // 회원의 올해생일(문자열 타입)
	      String str_now_birthday = str_now.substring(0, 4) + birthday.substring(5,7) + birthday.substring(8); 
	            
	      // 회원의 태어난년도
	      int birth_year = Integer.parseInt(birthday.substring(0, 4));
	            
	      // 현재년도
	      int now_year = Integer.parseInt(str_now.substring(0, 4)); 
	            
	      try {
	         Date now_birthday = sdfmt.parse(str_now_birthday); // 회원의 올해생일(연월일) 날짜 타입
	         now = sdfmt.parse(str_now); // 오늘날짜(연월일) 날짜타입
	         
	         if(now_birthday.before(now)) {
	            // 회원의 올해생일이 현재날짜 보다 이전이라면
	            
	            age = now_year - birth_year; 
	            // 나이 = 현재년도 - 회원의 태어난년도
	         }
	         else {
	            // 회원의 올해생일이 현재날짜 보다 이후이라면
	            
	            age = now_year - birth_year - 1;
	            // 나이 = 현재년도 - 회원의 태어난년도 - 1
	         }
	         
	      } catch (ParseException e) {
	         
	      }
	      
	      return age;
	      
	   /*
	      >> 한국나이 << 
	      
	      Calendar currentDate = Calendar.getInstance(); 
	      // 현재날짜와 시간을 얻어온다.
	      
	      int currentYear = currentDate.get(Calendar.YEAR);
	      
	      age =  currentYear - Integer.parseInt( birthday.substring(0, 4) ) + 1;
	      
	      return age;
	   */
		
		
	}
	
	
	
	
}