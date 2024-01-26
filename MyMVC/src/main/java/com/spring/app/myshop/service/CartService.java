package com.spring.app.myshop.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.spring.app.domain.CartVO;

public interface CartService {
	
	// 로그인한 사용자의 장바구니 목록 조회
	List<CartVO> selectProductCart(String userid);
	
	// 로그인한 사용자의 장바구니에 담긴 주문총액합계 및 총포인트합계 알아오기 
	Map<String, String> selectCartSumPricePoint(String userid);
	
	// 장바구니 등록하기
	int addCart(Map<String, String> paraMap) throws SQLException;
	
	// 장바구니 테이블에서 특정제품의 주문량 변경시키기
	int updateCart(Map<String, String> paraMap);
	
	//장바구니 테이블에서 특정제품을 장바구니에서 비우기
	int delCart(String cartno);

}
