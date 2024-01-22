package com.spring.app.common;

import javax.servlet.http.HttpServletRequest;

public class MyUtil {

	// *** ? 다음의 데이터까지 포함한 현재 URL 주소를 알려주는 메소드를 생성 *** //
	public static String getCurrentURL(HttpServletRequest request) {
		
		String currentURL = request.getRequestURL().toString();
	//	currentURL => http://localhost:9090/MyMVC/member/memberList.up
		
		String queryString = request.getQueryString();
	//	queryString => searchType=name&searchWord=%EC%83%88%ED%95%9C&sizePerPage=5&currentShowPageNo=7
	//  queryString => null (POST 방식일 경우)
		
		if(queryString != null) { // GET 방식일 경우
			currentURL += "?"+queryString;
    //		currentURL => http://localhost:9090/MyMVC/member/memberList.up?searchType=name&searchWord=%EC%83%88%ED%95%9C&sizePerPage=5&currentShowPageNo=7	
		}
		
		String ctxPath = request.getContextPath();
		//     /MyMVC
		
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();
		//     27      =          21                 +        6
		
		
		currentURL = currentURL.substring(beginIndex);
		//           /member/memberList.up?searchType=name&searchWord=%EC%83%88%ED%95%9C&sizePerPage=5&currentShowPageNo=7
		
		return currentURL;
	}// end of public static String getCurrentURL(HttpServletRequest request)------
	
	
}
