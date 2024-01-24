package com.spring.app.myshop.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.spring.app.domain.CartVO;

@Repository
public class CartDAO_imple implements CartDAO {
	
	@Resource
	private SqlSessionTemplate sqlsession;
	
	// 로그인한 사용자의 장바구니 목록 조회
	@Override
	public List<CartVO> selectProductCart(String userid) {
		List<CartVO> cartList = sqlsession.selectList("cart.selectProductCart", userid);
		return cartList;
	}
	
	// 로그인한 사용자의 장바구니에 담긴 주문총액합계 및 총포인트합계 알아오기
	@Override
	public Map<String, String> selectCartSumPricePoint(String userid) {
		Map<String, String> sumMap = sqlsession.selectOne("cart.selectCartSumPricePoint", userid);
		return sumMap;
	}
	
	//장바구니가 존재하는지 알아보기
	@Override
	public String addCartselect(Map<String, String> paraMap) {
		String cartno = sqlsession.selectOne("cart.addCartselect", paraMap);
		return cartno;
	}
	
	//장바구니에 존재하는 경우 update 하기
	@Override
	public int addUpdateCart(Map<String, String> paraMap) {
		int n = sqlsession.update("cart.addUpdateCart", paraMap);
		return n;
	}
	
	//장바구니에 존재하지 않는 경우 insert 하기
	@Override
	public int addInsertCart(Map<String, String> paraMap) {
		int n = sqlsession.insert("cart.addInsertCart", paraMap);
		return n;
	}
	
	// 장바구니 테이블에서 특정제품의 주문량 변경시키기
	@Override
	public int updateCart(Map<String, String> paraMap) {
		int n = sqlsession.update("cart.updateCart", paraMap);
		return n;
	}
	
	//장바구니 테이블에서 특정제품을 장바구니에서 비우기
	@Override
	public int delCart(String cartno) {
		int n = sqlsession.delete("cart.delCart", cartno);
		return n;
	}
}
