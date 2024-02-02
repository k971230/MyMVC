package com.spring.app.order.model;

import java.util.List;
import java.util.Map;

import com.spring.app.domain.MemberVO;
import com.spring.app.domain.ProductVO;

public interface OrderDAO {

	int getTotalCountOrder(String userid);

	List<Map<String, String>> getOrderList(Map<String, String> paraMap);

	Map<String, String> odrcodeOwnerMemberInfo(String key);

	long get_seq_tbl_order();

	int orderAdd(Map<String, Object> paraMap);

	int updateDeliverStart(String odrcodePnum);

	int updateDeliverEnd(String odrcodePnum);

	List<ProductVO> getJumunProductList(String pnum);

	
}
