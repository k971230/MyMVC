package com.spring.app.admin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.ModelAndView;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.spring.app.admin.service.AdminService;
import com.spring.app.common.AES256;
import com.spring.app.common.MyUtil;
import com.spring.app.common.Sha256;
import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.MemberVO;
import com.spring.app.domain.SpecVO;


@EnableAspectJAutoProxy
@Component
@Controller
public class AdminController {
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private AdminService service;
	
	@Autowired
	private AES256 AES256;

	@GetMapping(value="/admin/memberList.up")
	public ModelAndView memberList(ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		if( loginuser != null && "admin".equals(loginuser.getUserid()) ) {
			
			String searchType = request.getParameter("searchType");
			String searchWord = request.getParameter("searchWord");
			String sizePerPage = request.getParameter("sizePerPage");
			String currentShowPageNo = request.getParameter("currentShowPageNo");
			
			if("email".equals(searchType)) {
				try {
					searchWord = AES256.encrypt(searchWord);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
			}
			
			
		
			if(searchType == null ||
			   (!"name".equals(searchType) &&
				!"userid".equals(searchType) &&
				!"email".equals(searchType)) ) {
				searchType = "";
			}
			
			if(searchWord == null ||
			  (searchWord != null && searchWord.trim().isEmpty()) ) {
				searchWord = "";
			}
			
			if(sizePerPage == null ||
			  (!"10".equals(sizePerPage) &&
			   !"5".equals(sizePerPage) &&
			   !"3".equals(sizePerPage) ) ) {
				sizePerPage = "10";
			}
			
			if(currentShowPageNo == null) {
				currentShowPageNo = "1";
			}
			
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("searchType", searchType);
			paraMap.put("searchWord", searchWord);
			paraMap.put("sizePerPage", sizePerPage); // 한 페이지당 보여줄 행의 개수
			paraMap.put("currentShowPageNo", currentShowPageNo); // 조회하고자 하는 페이지 번호
			
			// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지 수 알아오기 //
			int totalPage = service.getTotalPage(paraMap);
			System.out.println("~~~~ 확인용 totalPage => " + totalPage);
			
			try {
				if( Integer.parseInt(currentShowPageNo) > totalPage || 
					Integer.parseInt(currentShowPageNo) <= 0) {
					currentShowPageNo = "1";
					paraMap.put("currentShowPageNo", currentShowPageNo);
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = "1";
				paraMap.put("currentShowPageNo", currentShowPageNo);
			}
			
			String pageBar = "";
			
			int blockSize = 10;
			// blockSize 는 블럭(토막)당 보여지는 페이지 번호의 개수이다.
			
			int loop = 1;
			// loop 는 1 부터 증가하여 1개 블럭을 이루는 페이지 번호의 개수(지금은 10개)까지만 증가하는 용도이다.
			
			// ==== !!! 다음은 pageNo 구하는 공식이다 !!! ==== //
			int pageNo = ( ( Integer.parseInt(currentShowPageNo) - 1)/blockSize ) * blockSize + 1;
			// pageNo 는 페이지바에서 보여지는 첫번째 번호이다.
			
			
			// **** [맨처음] [이전] 만들기 **** //
			//
			pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?searchType="+searchType+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'>[맨처음]</a></li>";
			
			if( pageNo != 1) {
				pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?searchType="+searchType+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
			}
			
			while( !(loop > blockSize || pageNo > totalPage) ) {
				
				if(pageNo == Integer.parseInt(currentShowPageNo)) {
					pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
					System.out.println("pageBar =" + pageBar);
					System.out.println("pageNo =" + pageNo);
				}
				else {
					pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?searchType="+searchType+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
				}
				
				loop ++;  // 1 2 3 4 5 6 7 8 9 10
				
				pageNo++; // 1 2 3 4 5 6 7 8 9 10
						  // 11 12 13 14 15 16 17 18 19 20 
						  // 21 22 23 24 25 26 27 28 29 30 
						  // 31 32 33 34 35 36 37 38 39 40
				  		  // 41 42
			}// end of while-----------------------------------------------
			
			// **** [다음] [마지막] 만들기 **** //
			// pageNo ==> 11
			if(pageNo <= totalPage) {
				pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?searchType="+searchType+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			}
			pageBar += "<li class='page-item'><a class='page-link' href='memberList.up?searchType="+searchType+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
				
			// *** ==== 페이지바 만들기 끝 ==== *** //
			
			// *** ====== 현재 페이지를 돌아갈 페이지(goBackURL)로 주소 지정하기 ====== *** //
			String currentURL = MyUtil.getCurrentURL(request);
			// 회원조회를 했을 시 현재 그 페이지로 그대로 되돌아가기 위한 용도로 쓰임
			
			System.out.println("~~~~ 확인용 searchType : " + searchType);
			System.out.println("~~~~ 확인용 searchWord : " + searchWord);
			System.out.println("~~~~ 확인용 sizePerPage : " + sizePerPage);
			System.out.println("~~~~ 확인용 currentShowPageNo : " + currentShowPageNo);
			List<MemberVO> memberList = service.select_Member_paging(paraMap);
			
			if(memberList.isEmpty() ) {
				System.out.println("memberList null 임");
			}
			
			
			mav.addObject("memberList", memberList);
			mav.addObject("searchType", searchType);
			mav.addObject("searchWord", searchWord);
			mav.addObject("sizePerPage", sizePerPage);
			mav.addObject("pageBar", pageBar);
			mav.addObject("currentURL", currentURL);
			
			mav.setViewName("tiles1.admin.memberList");
			return mav;
			
		}
		else {
			// 로그인을 안한 경우 또는 일반사용자로 로그인 한 경우 
			String message = "관리자만 접근이 가능합니다.";
			String loc = "javascript:history.back()";
			
			mav.addObject("message", message);
			mav.addObject("loc", loc);
			
			mav.setViewName("msg");
			
			return mav;
		}
		
		
	}
	
	// === 관리자(admin)로 로그인 했을때만 조회가 가능하도록 한다. === //
	@PostMapping(value="/admin/memberOneDetail.up")
	public ModelAndView memberOneDetail(ModelAndView mav, HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		if(loginuser != null && "admin".equals(loginuser.getUserid())) {
			// 관리자(admin)로 로그인 했을 경우
			
			String method = request.getMethod(); // "GET" 또는 "POST"
			
			String userid = request.getParameter("userid");
			String goBackURL = request.getParameter("goBackURL");
			
			System.out.println("goBackURL => "+goBackURL);
			// goBackURL => /member/memberList.up?searchType=name&searchWord=%EC%83%88%ED%95%9C&sizePerPage=5
			
			MemberVO mvo = service.selectOneMember(userid);
			
			System.out.println("email => " +mvo.getEmail());
			String email = mvo.getEmail();
			String mobile = mvo.getMobile();
			try {
				mvo.setEmail(AES256.decrypt(email));
				mvo.setMobile(AES256.decrypt(mobile));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			
			System.out.println("mvo => " + mvo);
			
			request.setAttribute("mvo", mvo);
			request.setAttribute("goBackURL", goBackURL);
			
			mav.setViewName("tiles1.admin.memberOneDetail");
			}
		
		return mav;
	}
	
		
	
	
	
	@RequestMapping(value="/admin/productRegister.up")
	public ModelAndView productRegister(ModelAndView mav, HttpServletRequest request, MultipartHttpServletRequest mrequest) {
		
		// == 관리자(admin)로 로그인 했을 때만 제품등록이 가능하도록 한다. == //
		HttpSession session = request.getSession();
		
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		if( loginuser != null && "admin".equals(loginuser.getUserid()) ) {
			// 관리자(admin)로 로그인했을 경우
			
			String method = request.getMethod();
			
			if(!"POST".equalsIgnoreCase(method)) { // "GET" 이라면
				
				// 카테고리 목록을 조회해오기
				List<CategoryVO> categoryList = service.selectCategoryList();
				request.setAttribute("categoryList", categoryList);
				
				// SPEC 목록을 조회해오기
				List<SpecVO> specList = service.selectSpecList();
				request.setAttribute("specList", specList);
				
			//	super.setRedirect(false);
				mav.setViewName("tiles1.admin.productRegister");
			}
			else { // "POST" 라면  
				
				// 새로운 제품 등록시 form 태그에서 입력한 값들을 얻어오기
				MultipartRequest mtrequest = null;
				
				// 1. 첨부되어진 파일을 디스크의 어느 경로에 업로드 할 것인지 그 경로를 설정해야 한다.
				ServletContext svlCtx = session.getServletContext();
				String uploadFileDir = "C:\\git\\MyMVC\\MyMVC\\src\\main\\resources\\static\\images";
				
				System.out.println("=== 첨부되어지는 이미지 파일이 올라가는 절대경로 uploadFileDir ==> " + uploadFileDir);
				
			/*
	             MultipartRequest의 객체가 생성됨과 동시에 파일 업로드가 이루어 진다.
	                   
	             MultipartRequest(HttpServletRequest request,
	                              String saveDirectory, -- 파일이 저장될 경로
	                              int maxPostSize,      -- 업로드할 파일 1개의 최대 크기(byte)
	                              String encoding,
	                              FileRenamePolicy policy) -- 중복된 파일명이 올라갈 경우 파일명다음에 자동으로 숫자가 붙어서 올라간다.   
	                  
	             파일을 저장할 디렉토리를 지정할 수 있으며, 업로드제한 용량을 설정할 수 있다.(바이트단위). 
	             이때 업로드 제한 용량을 넘어서 업로드를 시도하면 IOException 발생된다. 
	             또한 국제화 지원을 위한 인코딩 방식을 지정할 수 있으며, 중복 파일 처리 인터페이스를사용할 수 있다.
	                        
	             이때 업로드 파일 크기의 최대크기를 초과하는 경우이라면 
	             IOException 이 발생된다.
	             그러므로 Exception 처리를 해주어야 한다.                
	        */
//				=== 파일을 업로드 해준다. ===
				try {
					mtrequest = new MultipartRequest(request, uploadFileDir, 10*1024*1024, "UTF-8", new DefaultFileRenamePolicy());  
				} catch (IOException e) {
					System.out.println("~~~ 파일업로드 실패 에러메시지 ==> " + e.getMessage());
					// ~~~ 파일업로드 실패 에러메시지 ==> Posted content length of 178143886 exceeds limit of 10485760
					
					request.setAttribute("message", "업로드 되어질 경로가 잘못되었거나 또는 최대용량 10MB를 초과했으므로 파일업로드 실패함!!");
					request.setAttribute("loc", request.getContextPath()+"/admin/productRegister.up");
					
					mav.setViewName("tiles1.admin.productRegister");
				}
				
			}
		}
		mav.setViewName("tiles1.admin.productRegister");
		return mav;
		
	}
	
	
	
	
	
	
	
		

}
