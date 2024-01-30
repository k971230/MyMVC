package com.spring.app.order.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.spring.app.common.GoogleMail;
import com.spring.app.domain.MemberVO;
import com.spring.app.domain.ProductVO;
import com.spring.app.order.service.OrderService;

@EnableAspectJAutoProxy
@Component
@Controller
public class OrderController {
	
	@Autowired 
	private OrderService service;
	
	@Autowired
	private AES256 aes;
	
	private String getOdrcode() {
		
		// 주문코드(명세서번호) 형식 : s+날짜+sequence s20231101-1
		
		// 날짜 생성
		Date now = new Date();
		SimpleDateFormat smdatefm = new SimpleDateFormat("yyyyMMdd");
		String today = smdatefm.format(now);
		
		long seq = service.get_seq_tbl_order();
		   // pdao.get_seq_tbl_order(); 는 시퀀스 seq_tbl_order 값("주문코드")을 채번해오는 것.
		
		return "jy"+today+"-"+seq;
		
		
	}// end of private String getOdrcode()---------------
	
	
	@GetMapping(value="/shop/orderList.up") 
	public ModelAndView requiredLogin_orderList(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		int totalCount = 0;    		
		int sizePerPage = 10;  	
		int currentShowPageNo = 0; 
		int totalPage = 0;         
		
		totalCount = service.getTotalCountOrder(loginuser.getUserid());
		
		System.out.println(totalCount);
		
		totalPage = (int) Math.ceil((double)totalCount/sizePerPage); 
		
		
		if(str_currentShowPageNo == null) {
			  currentShowPageNo = 1;
		}
		else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);

				if (currentShowPageNo < 1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}

			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
			
		
		int startRno = ((currentShowPageNo - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		  
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("userid", loginuser.getUserid());
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		  
		  
		List<Map<String, String>> order_map_List = service.getOrderList(paraMap);

		mav.addObject("order_map_List", order_map_List);
		  
		String pageBar = "";
		int blockSize = 10;
			
		int loop = 1;

		int pageNo = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;
			
			
		pageBar += "<li class='page-item'><a class='page-link' href='orderList.up?currentShowPageNo=1'>[맨처음]</a></li>";
			
		if( pageNo != 1) {
			pageBar += "<li class='page-item'><a class='page-link' href='orderList.up?currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
			
		while( !(loop > blockSize || pageNo > totalPage) ) {
				
			if(pageNo == currentShowPageNo) {
				pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
			}
			else {
				pageBar += "<li class='page-item'><a class='page-link' href='orderList.up?currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop ++;  
			pageNo++; 
			
		}// end of while-----------------------------------------------
			
		if(pageNo <= totalPage) {
			pageBar += "<li class='page-item'><a class='page-link' href='orderList.up?currentShowPageNo="+pageNo+"'>[다음]</a></li>";
		}
		pageBar += "<li class='page-item'><a class='page-link' href='orderList.up?currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
			
	
		mav.addObject("pageBar", pageBar);
		 
		mav.setViewName("tiles1.order.orderList");
		return mav;
	}
	
	
	
	@ResponseBody
	@GetMapping(value="/order/odrcodeOwnerMemberInfoJSON.up", produces="text/plain;charset=UTF-8")
	public String odrcodeOwnerMemberInfoJSON(HttpServletRequest request) {
		
		String odrcode = request.getParameter("odrcode");
		
		HttpSession session = request.getSession();
		
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		String userid = loginuser.getUserid();
		JSONObject jsonObj = new JSONObject();
		if("admin".equals(userid) ) {
			
			Map<String, String> paraMap = service.odrcodeOwnerMemberInfo(odrcode);
			
			try {
				jsonObj.put("userid", paraMap.get("USERID"));
				jsonObj.put("name", paraMap.get("NAME"));
				jsonObj.put("email", aes.decrypt(paraMap.get("EMAIL")));
				jsonObj.put("mobile", aes.decrypt(paraMap.get("MOBILE")));
				jsonObj.put("postcode", paraMap.get("POSTCODE"));
				jsonObj.put("address", paraMap.get("ADDRESS"));
				jsonObj.put("detailaddress", paraMap.get("DETAILADDRESS"));
				jsonObj.put("extraaddress", paraMap.get("EXTRAADDRESS"));
				jsonObj.put("gender", paraMap.get("GENDER"));
				jsonObj.put("birthday", paraMap.get("BIRTHDAY"));
				jsonObj.put("coin", paraMap.get("COIN"));
				jsonObj.put("point", paraMap.get("POINT"));
				jsonObj.put("registerday", paraMap.get("REGISTERDAY"));
				
			} catch (JSONException | GeneralSecurityException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		}
		
		return jsonObj.toString();
			
		
	}
	
	
	
	
	
	// === 전표(주문코드)를 생성해주는 메소드 생성하기 === //
	@ResponseBody
	@PostMapping(value="/order/orderAdd.up", produces="text/plain;charset=UTF-8")
	public String orderAdd(HttpServletRequest request) {
		
		String sum_totalPrice = request.getParameter("n_sum_totalPrice");
		String sum_totalPoint = request.getParameter("n_sum_totalPoint");
		String str_pnum_join = request.getParameter("str_pnum_join");
		String str_oqty_join = request.getParameter("str_oqty_join");
		String str_totalPrice_join = request.getParameter("str_totalPrice_join");
		String str_cartno_join = request.getParameter("str_cartno_join");
		
	/*	
		System.out.println("~~~~~ 확인용 sum_totalPrice : " + n_sum_totalPrice);		 // ~~~~~ 확인용 n_sum_totalPrice : 86000
		System.out.println("~~~~~ 확인용 sum_totalPoint : " + n_sum_totalPoint);		 // ~~~~~ 확인용 n_sum_totalPoint : 530
		System.out.println("~~~~~ 확인용 str_pnum_join : " + str_pnum_join);				 // ~~~~~ 확인용 str_pnum_join : 5,4,61
		System.out.println("~~~~~ 확인용 str_oqty_join : " + str_oqty_join);				 // ~~~~~ 확인용 str_oqty_join : 1,1,5
		System.out.println("~~~~~ 확인용 str_totalPrice_join : " + str_totalPrice_join);  // ~~~~~ 확인용 str_totalPrice_join : 33000,13000,40000
		System.out.println("~~~~~ 확인용 str_cartno_join : " + str_cartno_join);			// ~~~~~ 확인용 str_cartno_join : 12,8,7
	*/
		
		// ===== Transaction 처리하기 ===== // 
	    // 1. 주문 테이블에 입력되어야할 주문전표를 채번(select)하기 
	    // 2. 주문 테이블에 채번해온 주문전표, 로그인한 사용자, 현재시각을 insert 하기(수동커밋처리)
	    // 3. 주문상세 테이블에 채번해온 주문전표, 제품번호, 주문량, 주문금액을 insert 하기(수동커밋처리)
	    // 4. 제품 테이블에서 제품번호에 해당하는 잔고량을 주문량 만큼 감하기(수동커밋처리) 
	        
	    // 5. 장바구니 테이블에서 str_cartno_join 값에 해당하는 행들을 삭제(delete)하기(수동커밋처리)
	    // >> 장바구니에서 주문을 한 것이 아니라 특정제품을 바로주문하기를 한 경우에는 장바구니 테이블에서 행들을 삭제할 작업은 없다. << 
	        
	    // 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sum_totalPrice 만큼 감하고, point 를 sum_totalPoint 만큼 더하기(update)(수동커밋처리) 
	    // 7. **** 모든처리가 성공되었을시 commit 하기(commit) **** 
	    // 8. **** SQL 장애 발생시 rollback 하기(rollback) **** 
	   
	    // === Transaction 처리가 성공시 세션에 저장되어져 있는 loginuser 정보를 새로이 갱신하기 ===
	    // === 주문이 완료되었을시 주문이 완료되었다라는 email 보내주기  === // 
		
		
		Map<String, Object> paraMap = new HashMap<>();
		String odrcode = getOdrcode();
		// === 주문테이블(tbl_order)에 insert 할 데이터 === //
		paraMap.put("odrcode", odrcode); // 주문코드(명세서번호) s+날짜+sequence
		// getOdrcode() 메소드는 위에서 정의한 전표(주문코드)를 생성해주는 것이다.
		
		HttpSession session = request.getSession();
		MemberVO loginuser = (MemberVO) session.getAttribute("loginuser");
		
		paraMap.put("userid", loginuser.getUserid());  // 회원아이디
		paraMap.put("sum_totalPrice", sum_totalPrice); // 주문총액
		paraMap.put("sum_totalPoint", sum_totalPoint); // 주문총포인트
		
		
		// === 주문상세테이블(tbl_orderdetail)에 insert 할 데이터 === //
		String[] pnum_arr = str_pnum_join.split("\\,"); // 여러개 제품을 주문한 경우  		  ex) "5,4,61".split("\\,"); ==> ["5","4","61"]
														// 장바구니에서 제품을 1개만 주문한 경우  ex) "5".split("\\,"); 	 ==> ["5"]
														// 특정제품을 바로주문하기를 한 경우	  ex) "5".split("\\,"); 	 ==> ["5"]
		
		String[] oqty_arr = str_oqty_join.split("\\,");

		String[] totalPrice_arr = str_totalPrice_join.split("\\,");
		
		paraMap.put("pnum_arr", pnum_arr);
		paraMap.put("oqty_arr", oqty_arr);
		paraMap.put("totalPrice_arr", totalPrice_arr);
		
		// === 장바구니(tbl_cart)에 delete 할 데이터 === //
		if(str_cartno_join != null) {
			// 특정 제품을 바로주문하기를 한 경우라면 str_cartno_join 의 값은 null 이 된다.
			
			String[] cartno_arr = str_cartno_join.split("\\,");
			paraMap.put("cartno_arr", cartno_arr);
		}
		
		
		// *** Transaction 처리를 해주는 메소드 호출하기 *** //
		int isSuccess = service.orderAdd(paraMap); // Transaction 처리를 해주는 메소드 호출하기
		System.out.println(isSuccess);
		// **** 주문이 완료되었을시 세션에 저장되어져 있는 loginuser 정보를 갱신하고
		//      이어서 주문이 완료되었다라는 email 보내주기  **** // 
		if(isSuccess == 1) {
			
			// 세션에 저장되어져 있는 loginuser 정보를 갱신
			loginuser.setCoin( loginuser.getCoin() - Integer.parseInt(sum_totalPrice));
			loginuser.setPoint( loginuser.getPoint() + Integer.parseInt(sum_totalPoint));
			

			////////// === 주문이 완료되었다는 email 보내기 시작 === ///////////
			GoogleMail mail = new GoogleMail();
			
			// str_pnum_join ==> "5,4,61"
			
			String pnumes = "'"+String.join("','", str_pnum_join.split("\\,"))+"'"; 
			// ==> ["5", "4", "61"]
			// "5','4','61"
			// "'5','4','61'"
			
			// System.out.println("~~~~ 확인용 주문한 제품번호 pnumes : " + pnumes);
			// ~~~~ 확인용 주문한 제품번호 pnumes : '5','4','61'
			
			
			// 주문한 제품에 대해 email 보내기시 email 내용에 넣을 주문한 제품번호들에 대한 제품정보를 얻어오는 것.
			/*List<ProductVO> jumunProductList = service.getJumunProductList(pnumes);
			
			StringBuilder sb = new StringBuilder();
	         
			sb.append("주문코드번호 : <span style='color: blue; font-weight: bold;'>"+odrcode+"</span><br/><br/>");
			sb.append("<주문상품><br/>");
			
			for(int i=0; i<jumunProductList.size(); i++) {
				sb.append(jumunProductList.get(i).getPname()+"&nbsp;"+oqty_arr[i]+"개&nbsp;&nbsp;");
	            sb.append("<img src='http://127.0.0.1:9090/MyMVC/images/"+jumunProductList.get(i).getPimage1()+"'/>");  
	            sb.append("<br/>");
			}// end of for----------------
			
			sb.append("<br/>이용해 주셔서 감사합니다.");
	         
	        String emailContents = sb.toString();
	         
	        mail.sendmail_OrderFinish(loginuser.getEmail(), loginuser.getName(), emailContents);*/
	        ////////// === 주문이 완료되었다는 email 보내기 끝 === ///////////
		}
		
		JSONObject jsobj = new JSONObject(); // {}
	    try {
			jsobj.put("isSuccess", isSuccess);
		} catch (JSONException e) {
			e.printStackTrace();
		}   // {"isSuccess":1} 또는 {"isSuccess":0}
	       
	    String json = jsobj.toString();
	    request.setAttribute("json", json);
	       
	    return jsobj.toString();
		
	
	
	
	
		
	}// end of private String getOdrcode()---------------
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping(value = "/order/admin/deliverStart.up")
	public ModelAndView deliverStart(HttpServletRequest request, ModelAndView mav) {
		// admin(관리자)으로 로그인 한 경우
		String[] odrcodeArr = request.getParameterValues("odrcode");
		String[] pnumArr = request.getParameterValues("pnum");
			
		Map<String, Object> paraMap = new HashMap<>();

		StringBuilder sb = new StringBuilder();
		// 's20231103-4/60','s20231103-4/5','s20231103-5/3'
		// 's20231103-4은 주문코드(전표)'이고 /뒤에 붙은 60 은 제품번호이다.
		// 's20231103-4은 주문코드(전표)'이고 /뒤에 붙은 5 는 제품번호이다.
		// 's20231103-5은 주문코드(전표)'이고 /뒤에 붙은 3 은 제품번호이다.
		// 이것은 오라클에서 주문코드(전표)컬럼||'/'||제품번호 로 하겠다는 말이다.

		for (int i = 0; i < odrcodeArr.length; i++) {
			sb.append(odrcodeArr[i] + "/" + pnumArr[i] + ",");
			// sql 문의 where 절에 fk_odrcode || '/' || fk_pnum in('전표/제품번호','전표/제품번호','전표/제품번호') 을 사용하기 위한 것이다.
		}
			
		String odrcodePnum = sb.toString();

		// 맨뒤의 콤마(,)제거하기
		odrcodePnum = odrcodePnum.substring(0, odrcodePnum.length() - 1);

		if (odrcodePnum != null) {
			String[] odrcodePnum_arr = odrcodePnum.split("\\,");
			paraMap.put("odrcodePnum_arr", odrcodePnum_arr);

		}
			
		// tbl_orderdetail 테이블의 deliverstatus(배송상태) 컬럼의 값을 2(배송시작)로 변경하기
		int n = service.updateDeliverStart(paraMap);

		System.out.println(n);

		// === *** 배송을 했다라는 확인 문자(SMS)를 주문을 한 사람(여러명)에게 보내기 종료 *** === //
		if (n == 1) {
			String message = "선택하신 제품들은 배송시작으로 변경되었습니다.";
			String loc = request.getContextPath() + "/shop/orderList.up";

			mav.addObject("message", message);
			mav.addObject("loc", loc);

			mav.setViewName("msg");

		}
		return mav;
	}
	
	
	@PostMapping(value = "/order/admin/deliverEnd.up")
	public ModelAndView deliverEnd(HttpServletRequest request, ModelAndView mav) {
		// admin(관리자)으로 로그인 한 경우
		String[] odrcodeArr = request.getParameterValues("odrcode");
		String[] pnumArr = request.getParameterValues("pnum");
			
		Map<String, Object> paraMap = new HashMap<>();

		StringBuilder sb = new StringBuilder();
		// 's20231103-4/60','s20231103-4/5','s20231103-5/3'
		// 's20231103-4은 주문코드(전표)'이고 /뒤에 붙은 60 은 제품번호이다.
		// 's20231103-4은 주문코드(전표)'이고 /뒤에 붙은 5 는 제품번호이다.
		// 's20231103-5은 주문코드(전표)'이고 /뒤에 붙은 3 은 제품번호이다.
		// 이것은 오라클에서 주문코드(전표)컬럼||'/'||제품번호 로 하겠다는 말이다.

		for (int i = 0; i < odrcodeArr.length; i++) {
			sb.append(odrcodeArr[i] + "/" + pnumArr[i] + ",");
			// sql 문의 where 절에 fk_odrcode || '/' || fk_pnum in('전표/제품번호','전표/제품번호','전표/제품번호') 을 사용하기 위한 것이다.
		}
			
		String odrcodePnum = sb.toString();

		// 맨뒤의 콤마(,)제거하기
		odrcodePnum = odrcodePnum.substring(0, odrcodePnum.length() - 1);

		if (odrcodePnum != null) {
			String[] odrcodePnum_arr = odrcodePnum.split("\\,");
			paraMap.put("odrcodePnum_arr", odrcodePnum_arr);

		}
			
		// tbl_orderdetail 테이블의 deliverstatus(배송상태) 컬럼의 값을 2(배송시작)로 변경하기
		int n = service.updateDeliverEnd(paraMap);

		System.out.println(n);

		// === *** 배송을 했다라는 확인 문자(SMS)를 주문을 한 사람(여러명)에게 보내기 종료 *** === //
		if (n == 1) {
			String message = "선택하신 제품들은 배송완료로 변경되었습니다.";
			String loc = request.getContextPath() + "/shop/orderList.up";

			mav.addObject("message", message);
			mav.addObject("loc", loc);

			mav.setViewName("msg");

		}
		return mav;
	}
		  
		  
	  
	  
	  
	 
	
	
	
	
}
