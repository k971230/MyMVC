package com.spring.app.myshop.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.domain.CartVO;
import com.spring.app.myshop.model.CartDAO;

@Service
public class CartService_imple implements CartService {
	
	@Autowired  
	private CartDAO dao;
	
	// 로그인한 사용자의 장바구니 목록 조회
	@Override
	public List<CartVO> selectProductCart(String userid) {
		List<CartVO> cartList = dao.selectProductCart(userid);
		return cartList;
	}
	
	// 로그인한 사용자의 장바구니에 담긴 주문총액합계 및 총포인트합계 알아오기 
	@Override
	public Map<String, String> selectCartSumPricePoint(String userid) {
		Map<String, String> sumMap = dao.selectCartSumPricePoint(userid);
		return sumMap;
	}
	
	// 장바구니 담기 
	// 장바구니 테이블(tbl_cart)에 해당 제품을 담아야 한다.
	// 장바구니 테이블에 해당 제품이 존재하지 않는 경우에는 tbl_cart 테이블에 insert 를 해야하고, 
	// 장바구니 테이블에 해당 제품이 존재하는 경우에는 또 그 제품을 추가해서 장바구니 담기를 한다라면 tbl_cart 테이블에 update 를 해야한다.
	@Override
	public int addCart(Map<String, String> paraMap) throws SQLException {
		
		int n = 0;
		
		//장바구니 번호가 존재하는지 select 하기
		String cartno = dao.addCartselect(paraMap);
		paraMap.put("cartno", cartno);
		
		if(cartno != null) {
			
			n = dao.addUpdateCart(paraMap);
			 // 어떤 제품을 추가로 장바구니에 넣고자 하는 경우
				
		}
		else {
			
			n = dao.addInsertCart(paraMap);
			// 장바구니에 존재하지 않는 새로운 제품을 넣고자 하는 경우
			
		}
			
		return n;
	}// end of public int addCart(Map<String, String> paraMap) throws SQLException
	
	// 장바구니 테이블에서 특정제품의 주문량 변경시키기
	@Override
	public int updateCart(Map<String, String> paraMap) {
		int n = dao.updateCart(paraMap);
		return n;
	}
	
	//장바구니 테이블에서 특정제품을 장바구니에서 비우기
	@Override
	public int delCart(String cartno) {
		int n = dao.delCart(cartno);
		return n;
	}
	
	
	
	
	
}
