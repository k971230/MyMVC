package com.spring.app.myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.ProductVO;
import com.spring.app.domain.PurchaseReviewsVO;
import com.spring.app.myshop.service.ProductService;
import org.springframework.web.bind.annotation.RequestParam;


@EnableAspectJAutoProxy
@Component
@Controller  
public class MyshopController {
	
	@Autowired  
	private ProductService productservice;
	
	// 쇼핑몰홈[더보기] 페이지
	@GetMapping("/shop/mallHomeMore.up")
	public ModelAndView mallHomeMore(ModelAndView mav, HttpServletRequest request) {
		
		int totalHITCount = productservice.totalPspecCount("1");	// HIT 상품의 전체개수를 알아온다. 
		
	//	System.out.println("확인용 totalHITCount => " + totalHITCount);
		
		mav.addObject("totalHITCount", totalHITCount);
		
		mav.setViewName("myshop.mallHomeMore");
		
		return mav;
	}
	
	
	// 쇼핑몰홈[스크롤] 페이지
	@GetMapping("/shop/mallHomeScroll.up")
	public ModelAndView mallHomeScroll(ModelAndView mav, HttpServletRequest request) {
		
		int totalHITCount = productservice.totalPspecCount("1");	// HIT 상품의 전체개수를 알아온다. 
		
		//	System.out.println("확인용 totalHITCount => " + totalHITCount);
		
		mav.addObject("totalHITCount", totalHITCount);
		
		mav.setViewName("myshop.mallHomeScroll");
		
		return mav;
	}
	
	
	
	// 사이드에 띄워줄 카테고리 목록 ajax 처리하기
	@ResponseBody
	@GetMapping(value="/shop/categoryListJSON.up", produces="text/plain;charset=UTF-8")
	public String categoryListJSON() throws Exception {
		
		// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
		List<CategoryVO> categoryList = productservice.getCategoryList();

		JSONArray jsonArr = new JSONArray(); // []

		if (categoryList.size() > 0) {
			// DB에서 조회해온 결과물이 있을 경우

			for (CategoryVO cvo : categoryList) {

				JSONObject jsonObj = new JSONObject();

				jsonObj.put("cnum", cvo.getCnum());
				jsonObj.put("code", cvo.getCode());
				jsonObj.put("cname", cvo.getCname());

				jsonArr.put(jsonObj);
			} // end of for------------------
	         
			}// end of if----------------------------
	      
			String json = jsonArr.toString(); // 문자열로 변환 
	      
//			System.out.println("~~~ 확인용 json => " + json);
	     /* ~~~ 확인용 json => [{"code":"100000","cname":"전자제품","cnum":1}
	                          ,{"code":"200000","cname":"의류","cnum":2}
	                          ,{"code":"300000","cname":"도서","cnum":3}]
	     */
	      
		return json.toString();
	}
	
	
	// 카테고리 목록으로 이동하는 페이지 처리
	@GetMapping(value="/shop/mallByCategory.up")
	public ModelAndView mallByCategory(ModelAndView mav, HttpServletRequest request) {
		
		String cnum = request.getParameter("cnum"); // 카테고리 번호
		
		try {
			int n_cnum = Integer.parseInt(cnum);
			
			if(n_cnum < 1) {
				mav.setViewName("/index.up");
				return mav;
			}
			
			if ( !productservice.isExist_cnum(cnum) ) {
				// 입력받은 cnum 이 DB 에 존재하지 않는 경우는 사용자가 장난친 경우이다.
				
				mav.setViewName("/index.up");
			}
			
			else {
				// 입력받은 cnum 이 DB 에 존재하는 경우
				
				// **** 카테고리번호에 해당하는 제품들을 페이징 처리하여 보여주기 **** //
				String currentShowPageNo = request.getParameter("currentShowPageNo");
				// currentShowPageNo 은 사용자가 보고자하는 페이지바의 페이지번호 이다.
				// 카테고리 메뉴에서 카테고리명만을 클릭했을 경우에는 currentShowPageNo 은 null 이 된다.
				// currentShowPageNo 이 null 이라면 currentShowPageNo 을 1 페이지로 바꾸어야 한다.
				
				if(currentShowPageNo == null) {
					currentShowPageNo = "1";
				}
				
				// 페이징 처리를 위한 특정 카테고리에 대한 총 페이지 수 알아오기 //
				int totalPage = productservice.getTotalPage(cnum);
			//	System.out.println("~~~~ 확인용 totalPage => " + totalPage);

				// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 totalPage 값보다 더 큰 값을 입력하여 장난친 경우
				// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 0 또는 음수를 입력하여 장난친 경우
				// === GET 방식이므로 사용자가 웹브라우저 주소창에서 currentShowPageNo 에 숫자가 아닌 문자열을 입력하여 장난친 경우
				// 아래처럼 막아주도록 하겠다.
				try {
					if( Integer.parseInt(currentShowPageNo) > totalPage || 
						Integer.parseInt(currentShowPageNo) <= 0) {
						currentShowPageNo = "1";
					}
				} catch (NumberFormatException e) {
					currentShowPageNo = "1";
				}
				
				// *** == 특정한 카테고리에서 사용자가 보고자 하는 특정 페이지번호에 해당하는 제품들을 조회해오기 == *** //
				Map<String, String> paraMap = new HashMap<>();
				paraMap.put("cnum", cnum);
				paraMap.put("currentShowPageNo", currentShowPageNo);
				
				List<ProductVO> productList = productservice.selectProductByCategory(paraMap);
				
			//	System.out.println("~~~ 확인용 productList.size() => " + productList.size());
				
				request.setAttribute("productList", productList);
				
				// *** ==== 페이지바 만들기 시작 ==== *** //
				/*
		            1개 블럭당 10개씩 잘라서 페이지 만든다.
		            1개 페이지당 10개행을 보여준다면 총 몇개 블럭이 나와야 할까? 
		                총 제품의 개수가 412명 이고, 1개 페이지당 보여줄 제품수가 10 이라면
		            412/10 = 41.2 ==> 42(totalPage)        
		                
		            1블럭               1 2 3 4 5 6 7 8 9 10 [다음]
		            2블럭   [이전] 11 12 13 14 15 16 17 18 19 20 [다음]
		            3블럭   [이전] 21 22 23 24 25 26 27 28 29 30 [다음]
		            4블럭   [이전] 31 32 33 34 35 36 37 38 39 40 [다음]
		            5블럭   [이전] 41 42 
				*/
		        
				// ==== !!! pageNo 구하는 공식 !!! ==== // 
		     /*
		         1  2  3  4  5  6  7  8  9  10  -- 첫번째 블럭의 페이지번호 시작값(pageNo)은  1 이다.
		         11 12 13 14 15 16 17 18 19 20  -- 두번째 블럭의 페이지번호 시작값(pageNo)은 11 이다.   
		         21 22 23 24 25 26 27 28 29 30  -- 세번째 블럭의 페이지번호 시작값(pageNo)은 21 이다.
		         
		          currentShowPageNo        pageNo  ==> ( (currentShowPageNo - 1)/blockSize ) * blockSize + 1 
		         ---------------------------------------------------------------------------------------------
		                1                   1 = ( (1 - 1)/10 ) * 10 + 1 
		                2                   1 = ( (2 - 1)/10 ) * 10 + 1 
		                3                   1 = ( (3 - 1)/10 ) * 10 + 1 
		                4                   1 = ( (4 - 1)/10 ) * 10 + 1  
		                5                   1 = ( (5 - 1)/10 ) * 10 + 1 
		                6                   1 = ( (6 - 1)/10 ) * 10 + 1 
		                7                   1 = ( (7 - 1)/10 ) * 10 + 1 
		                8                   1 = ( (8 - 1)/10 ) * 10 + 1 
		                9                   1 = ( (9 - 1)/10 ) * 10 + 1 
		               10                   1 = ( (10 - 1)/10 ) * 10 + 1 
		                
		               11                  11 = ( (11 - 1)/10 ) * 10 + 1 
		               12                  11 = ( (12 - 1)/10 ) * 10 + 1
		               13                  11 = ( (13 - 1)/10 ) * 10 + 1
		               14                  11 = ( (14 - 1)/10 ) * 10 + 1
		               15                  11 = ( (15 - 1)/10 ) * 10 + 1
		               16                  11 = ( (16 - 1)/10 ) * 10 + 1
		               17                  11 = ( (17 - 1)/10 ) * 10 + 1
		               18                  11 = ( (18 - 1)/10 ) * 10 + 1 
		               19                  11 = ( (19 - 1)/10 ) * 10 + 1
		               20                  11 = ( (20 - 1)/10 ) * 10 + 1
		                
		               21                  21 = ( (21 - 1)/10 ) * 10 + 1 
		               22                  21 = ( (22 - 1)/10 ) * 10 + 1
		               23                  21 = ( (23 - 1)/10 ) * 10 + 1
		               24                  21 = ( (24 - 1)/10 ) * 10 + 1
		               25                  21 = ( (25 - 1)/10 ) * 10 + 1
		               26                  21 = ( (26 - 1)/10 ) * 10 + 1
		               27                  21 = ( (27 - 1)/10 ) * 10 + 1
		               28                  21 = ( (28 - 1)/10 ) * 10 + 1 
		               29                  21 = ( (29 - 1)/10 ) * 10 + 1
		               30                  21 = ( (30 - 1)/10 ) * 10 + 1                    

		      */
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
				pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?cnum="+cnum+"&currentShowPageNo=1'>[맨처음]</a></li>";
				
				if( pageNo != 1) {
					pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?cnum="+cnum+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
				}
				
				while( !(loop > blockSize || pageNo > totalPage) ) {
					
					if(pageNo == Integer.parseInt(currentShowPageNo)) {
						pageBar += "<li class='page-item active'><a class='page-link' href='#'>"+pageNo+"</a></li>";
					}
					else {
						pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?cnum="+cnum+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
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
					pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?cnum="+cnum+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
				}
				pageBar += "<li class='page-item'><a class='page-link' href='mallByCategory.up?cnum="+cnum+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
					

				// *** ==== 페이지바 만들기 끝 ==== *** //
				
				request.setAttribute("pageBar", pageBar);
				
				mav.setViewName("myshop.mallByCategory");
			}
			
		} catch (NumberFormatException e) {
			mav.setViewName("/index.up");
		}
		
		return mav;
	}
	
	
	// 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기
	@ResponseBody
	@GetMapping(value="/shop/mallDisplayJSON.up", produces="text/plain;charset=UTF-8")
	public String mallDisplayJSON(HttpServletRequest request) throws Exception {
		
		String sname = request.getParameter("sname"); // "HIT"  "NEW"  "BEST"
		String start = request.getParameter("start");
		String len = request.getParameter("len");
	/*
        맨 처음에는 sname("HIT")상품을  start("1") 부터 len("8")개를 보여준다.
        더보기... 버튼을 클릭하면  sname("HIT")상품을  start("9") 부터 len("8")개를 보여준다.
        또  더보기... 버튼을 클릭하면  sname("HIT")상품을  start("17") 부터 len("8")개를 보여준다.      
	*/
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("sname", sname); // 	   "HIT"  "NEW"  "BEST"
		paraMap.put("start", start); // start  "1"  "9"  "17"  "25"  "33"
		
		String end = String.valueOf(Integer.parseInt(start) + Integer.parseInt(len) - 1);
		paraMap.put("end", end);	 // end => start + len - 1;
									 // end    "8"    "16"    "24"    "32"    "40"
		
		
		List<ProductVO> productList = productservice.selectBySpecName(paraMap);
		
		JSONArray jsonArr = new JSONArray();// [] 저장소인 배열을 생성해야한다. JSONArray 를 이용해 리스트를 [] 형태로 변환한다.
		
		if(productList.size() > 0) {
			// DB에서 조회해온 결과물이 있을 경우 리스트에서 한행씩 꺼내와야한다. => 반복문
			
			for(ProductVO pvo : productList) {
				
				JSONObject jsonObj = new JSONObject(); // {} 객체 형태를 만들었으니 안에 내용물을 넣어야 한다.
				
				// pnum, pname, cname, pcompany, pimage1, pimage2, pqty, price, saleprice, sname, pcontent, point, pinputdate
				
				jsonObj.put("pnum", pvo.getPnum()); 			   // {"pnum":1}
				jsonObj.put("pname", pvo.getPname()); 			   // {"pnum":1, "pname":"스마트TV"}
				jsonObj.put("cname", pvo.getCategvo().getCname()); // {"pnum":1, "pname":"스마트TV", "cname":"전자제품}
				jsonObj.put("pcompany", pvo.getPcompany());
				jsonObj.put("pimage1", pvo.getPimage1());
				jsonObj.put("pimage2", pvo.getPimage2());
				jsonObj.put("pqty", pvo.getPqty());
				jsonObj.put("price", pvo.getPrice());
				jsonObj.put("saleprice", pvo.getSaleprice());
				jsonObj.put("sname", pvo.getSpvo().getSname());
				jsonObj.put("pcontent", pvo.getPcontent());
				jsonObj.put("point", pvo.getPoint());
				jsonObj.put("pinputdate", pvo.getPinputdate());
				
				jsonObj.put("discountPercent", pvo.getDiscountPercent());
				
				// jsonObj ==> {"pnum":1, "pname":"스마트TV", "cname":"전자제품", ..... , "pinputdate":"2023-10-19", "discountPercent":15}
				
				jsonArr.put(jsonObj); // [{"pnum":1, "pname":"스마트TV", "cname":"전자제품", ..... , "pinputdate":"2023-10-19", "discountPercent":15}, {}, {}] 배열 속에 객체를 넣는다.
			}// end of for--------------------
			
		}// end of if----------------
		
		String json = jsonArr.toString(); // 배열을 문자열로 변환
		
	//	System.out.println("~~~ 확인용 json => " + json);
			
		return json;
		
	}
	
	
	// 제품 상세 페이지
	@GetMapping("/shop/prodView.up")
	public ModelAndView prodView(ModelAndView mav, HttpServletRequest request) {
		
		String pnum = request.getParameter("pnum"); // 제품번호
		
		// 제품번호를 가지고서 해당 제품의 정보를 조회해오기
		ProductVO pvo = productservice.selectOneProduct(pnum);
		
		// 제품번호를 가지고서 해당 제품의 추가된 이미지 정보를 조회해오기
		List<String> imgList = productservice.getImagesByPnum(pnum);
		
		if(pvo == null) {
			// GET 방식이므로 사용자가 웹브라우저 주소창에서 장난쳐서 존재하지 않는 제품번호를 입력한 경우
	        String message = "검색하신 제품은 존재하지 않습니다.";
	        String loc = "javascript:history.back()";
	        
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        
	        mav.setViewName("msg");
	         
	        return mav;
		}
		else {
			// 제품이 있는 경우
			
			mav.addObject("pvo", pvo);			// 제품의 정보
			mav.addObject("imgList", imgList);	// 해당 제품의 추가된 이미지 정보
			
			mav.setViewName("myshop.prodView");
			
		}
		
		return mav;
	}
	
	
	
	
	
	
	@ResponseBody
	@PostMapping(value="/shop/reviewRegister.up", produces="text/plain;charset=UTF-8")
	public String reviewRegister(HttpServletRequest request) throws Exception {
		
		String contents = request.getParameter("contents");
		String fk_userid = request.getParameter("fk_userid");
		String fk_pnum = request.getParameter("fk_pnum");
		
		// **** 크로스 사이트 스크립트 공격에 대응하는 안전한 코드(시큐어 코드) 작성하기 **** // 
		contents = contents.replaceAll("<", "&lt;");
		contents = contents.replaceAll(">", "&gt;");
		
		// 입력한 내용에서 엔터는 <br>로 변환시키기
		contents = contents.replaceAll("\r\n", "<br>");
		
		Map<String, String> paraMap = new HashMap<>();
		
		paraMap.put("contents", contents);
		paraMap.put("fk_userid", fk_userid);
		paraMap.put("fk_pnum", fk_pnum);
		
		int i = productservice.addReview(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("i", i);
		
		String json = jsonObj.toString();
		
		return json.toString();
	
	}
	
	@ResponseBody
	@GetMapping(value="/shop/reviewList.up", produces="text/plain;charset=UTF-8")
	public String reviewList(HttpServletRequest request) throws Exception {
		
		String fk_pnum = request.getParameter("fk_pnum"); // 제품번호
		
		List<PurchaseReviewsVO> reviewList = productservice.reviewList(fk_pnum);
		
		JSONArray jsArr = new JSONArray(); // []
		System.out.println("reviewList.size()+"+reviewList.size());
		if(reviewList.size() > 0) {
			for(PurchaseReviewsVO reviewsvo : reviewList) {
				
				JSONObject jsobj = new JSONObject();                
				jsobj.put("contents", reviewsvo.getContents());     
				jsobj.put("name", reviewsvo.getName());    
				jsobj.put("writeDate", reviewsvo.getWriteDate());   
				jsobj.put("userid", reviewsvo.getFk_userid());      
				jsobj.put("review_seq", reviewsvo.getReview_seq()); 
				
				jsArr.put(jsobj);

			}// end of for----------------------
		}
		
		String json = jsArr.toString(); 
			
		return json;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
