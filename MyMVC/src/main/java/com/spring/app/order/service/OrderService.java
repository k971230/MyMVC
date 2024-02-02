package com.spring.app.order.service;

import java.util.List;
import java.util.Map;

import com.spring.app.domain.MemberVO;
import com.spring.app.domain.ProductVO;

public interface OrderService {

	int getTotalCountOrder(String userid);

	List<Map<String, String>> getOrderList(Map<String, String> paraMap);

	Map<String, String> odrcodeOwnerMemberInfo(String key);

	long get_seq_tbl_order();

	int orderAdd(Map<String, Object> paraMap);

	int updateDeliverStart(Map<String, Object> paraMap);

	int updateDeliverEnd(Map<String, Object> paraMap);

	List<ProductVO> getJumunProductList(Map<String, Object> paraMap);

	
}
