package com.spring.app.order.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.domain.MemberVO;
import com.spring.app.domain.ProductVO;
import com.spring.app.order.model.OrderDAO;

// ==== #31. Service 선언 ====
// 트랜잭션 처리를 담당하는 곳, 업무를 처리하는 곳, 비지니스(Business)단 
// @Component
@Service
public class OrderService_imple implements OrderService {

	@Autowired
	private OrderDAO dao;

	@Override
	public int getTotalCountOrder(String userid) {
		int totalCount = dao.getTotalCountOrder(userid);
		return totalCount;
	}

	@Override
	public List<Map<String, String>> getOrderList(Map<String, String> paraMap) {
		List<Map<String, String>> order_map_List = dao.getOrderList(paraMap);
		return order_map_List;
	}

	@Override
	public Map<String, String> odrcodeOwnerMemberInfo(String key) {
		Map<String, String> paraMap = dao.odrcodeOwnerMemberInfo(key);
		return paraMap;
	}

	@Override
	public long get_seq_tbl_order() {
		long seq = dao.get_seq_tbl_order();
		return seq;
	}

	
	@Override
	public int orderAdd(Map<String, Object> paraMap) {
		
		return dao.orderAdd(paraMap);
	}
	
	@Override
	public int updateDeliverStart(Map<String, Object> paraMap) {
		int n = 0;

		String[] odrcodePnum_arr = (String[]) paraMap.get("odrcodePnum_arr");
		System.out.println(odrcodePnum_arr);

		int cnt = 0;
		for (int i = 0; i < odrcodePnum_arr.length; i++) {
			
			String odrcodePnum = odrcodePnum_arr[i];
			n = dao.updateDeliverStart(odrcodePnum);
			cnt++;
		} // end of for----------------------------

		if (cnt == odrcodePnum_arr.length) {
			n = 1;
		}

		return n;
	}

	@Override
	public int updateDeliverEnd(Map<String, Object> paraMap) {
		int n = 0;

		String[] odrcodePnum_arr = (String[]) paraMap.get("odrcodePnum_arr");
		System.out.println(odrcodePnum_arr);

		int cnt = 0;
		for (int i = 0; i < odrcodePnum_arr.length; i++) {
			String odrcodePnum = odrcodePnum_arr[i];
			n = dao.updateDeliverEnd(odrcodePnum);
			cnt++;
		} // end of for----------------------------

		if (cnt == odrcodePnum_arr.length) {
			n = 1;
		}

		return n;
	}

	@Override
	public List<ProductVO> getJumunProductList(Map<String, Object> paraMap) {
		
		String[] pnum_arr = (String[]) paraMap.get("pnum_arr");
		
		List<ProductVO> jumunProductList = null;
		for(int i=0; i<pnum_arr.length; i++) {
			
			String pnum = pnum_arr[i];
			
			jumunProductList = dao.getJumunProductList(pnum);
			
			
		}// end of for----------------------------
		
		
		
		return jumunProductList;
	}
	
	
	
	
}