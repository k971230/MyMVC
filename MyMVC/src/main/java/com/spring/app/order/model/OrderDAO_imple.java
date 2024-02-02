package com.spring.app.order.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.spring.app.domain.MemberVO;
import com.spring.app.domain.ProductVO;

//==== #32. Repository(DAO) 선언 ====
//@Component
@Repository
public class OrderDAO_imple implements OrderDAO {

	@Resource
	private SqlSessionTemplate sqlsession;

	@Override
	public int getTotalCountOrder(String userid) {
		int totalCount = sqlsession.selectOne("order.getTotalCountOrder", userid);
		return totalCount;
	}

	@Override
	public List<Map<String, String>> getOrderList(Map<String, String> paraMap) {
		List<Map<String, String>> order_map_List = sqlsession.selectList("order.getOrderList", paraMap);
		return order_map_List;
	}


	@Override
	public Map<String, String> odrcodeOwnerMemberInfo(String key) {
		Map<String, String> paraMap = sqlsession.selectOne("order.odrcodeOwnerMemberInfo", key);
		return paraMap;
	}

	@Override
	public long get_seq_tbl_order() {
		long seq = sqlsession.selectOne("order.get_seq_tbl_order");
		return seq;
	}

	@Override
	public int orderAdd(Map<String, Object> paraMap) {
		int isSuccess = 0;
		int n1=0, n2=0, n3=0, n4=0, n5=0;
		
		
			
			n1 = sqlsession.insert("order.insert_order", paraMap);
			
			System.out.println("~~~~~~ 확인용 n1 : " + n1);
		//	~~~~~~ 확인용 n1 : 1
			
			
			if(n1 == 1) {
				// 주문코드(명세서번호) --> (String)paraMap.get("odrcode")
				String[] pnum_arr = (String[]) paraMap.get("pnum_arr"); // 제품번호
				String[] oqty_arr = (String[]) paraMap.get("oqty_arr"); // 주문량
				String[] totalPrice_arr = (String[]) paraMap.get("totalPrice_arr");// 주문가격
				String odrcode = (String)paraMap.get("odrcode");
				
				Map<String, String> detailMap = new HashMap<>();
				detailMap.put("odrcode", odrcode);
				int cnt = 0;
				for(int i=0; i<pnum_arr.length; i++) {
					
					detailMap.put("pnum", pnum_arr[i]);
					detailMap.put("oqty", oqty_arr[i]);
					detailMap.put("totalPrice", totalPrice_arr[i]);
					
					n2 = sqlsession.insert("order.insert_orderdetail", detailMap);
					
					cnt++;
				}// end of for----------------------------
				
				if(cnt == pnum_arr.length) {
					n2=1;
				}
					System.out.println("~~~~~~ 확인용 n2 : " + n2);
				//	~~~~~~ 확인용 n2 : 1
				
			}// end of if(n1 == 1)--------------------------------
			
			if(n2 == 1) {
		    	String[] pnum_arr = (String[]) paraMap.get("pnum_arr"); // 제품번호
				String[] oqty_arr = (String[]) paraMap.get("oqty_arr"); // 주문량
		    	
				Map<String, String> oqtyMap = new HashMap<>();
				
				int cnt = 0;
				for(int i=0; i<pnum_arr.length; i++) {
					
					oqtyMap.put("pnum", pnum_arr[i]);
					oqtyMap.put("oqty", oqty_arr[i]);
					
					n3 = sqlsession.update("order.oqty_update", oqtyMap);
					
					cnt++;
				}// end of for----------------------------
		    	
				if(cnt == pnum_arr.length) {
					n3=1;
				}
				
					System.out.println("~~~~~~ 확인용 n3 : " + n3);
				//	~~~~~~ 확인용 n3 : 1
				
		    }// end of if(n2 == 1)--------------------------------
			
			
			if(n3 == 1 && paraMap.get("cartno_arr") != null) {
					
				String[] cartno_arr = (String[]) paraMap.get("cartno_arr"); // 배열로 받아서 스트링타입으로 바꿔준다.
				// cartno_arr 은 ["11","8","7"]
				
				int cnt = 0;
				for(int i=0; i<cartno_arr.length; i++) {
					String cartno = cartno_arr[i];
					n4 = sqlsession.delete("order.delete_cart", cartno);
					
					cnt++;
				}// end of for----------------------------
				
				System.out.println("~~~~~~ 확인용 n4 : " + n4);
				//	~~~~~~ 확인용 n4 : 3
				
				if(cnt == cartno_arr.length) {
					n4 = 1;
				}
			}
			
			if(n3 == 1 && paraMap.get("cartno_arr") == null) {
				// "제품 상세 정보" 페이지에서 "바로주문하기" 를 한 경우
				// 장바구니 번호인 paraMap.get("cartno_arr") 이 없는 것이다.
				n4 = 1;
			}
			
			// 6. 회원 테이블에서 로그인한 사용자의 coin 액을 sum_totalPrice 만큼 감하고, point 를 sum_totalPoint 만큼 더하기(update)(수동커밋처리) 
			if(n4==1) {
				n5 = sqlsession.update("order.coin_point_update", paraMap);
				
				System.out.println("~~~~~~ 확인용 n5 : " + n5);
				//	~~~~~~ 확인용 n5 : 1
				
			}// end of if(n4==1)---------------------------------
			
			if(n1*n2*n3*n4*n5 == 1) {
				
				
				
					System.out.println("~~~~~~ 확인용 n1*n2*n3*n4*n5 : " + n1*n2*n3*n4*n5);
				//	~~~~~~ 확인용 n1*n2*n3*n4*n5 : 1
				
				isSuccess = 1;
			}	
			

        return isSuccess;
    }
	

	@Override
	public int updateDeliverStart(String odrcodePnum) {
		int	n = sqlsession.update("order.updateDeliverStart", odrcodePnum);
		return n;
	}

	@Override
	public int updateDeliverEnd(String odrcodePnum) {
		int	n = sqlsession.update("order.updateDeliverEnd", odrcodePnum);
		return n;
	}

	@Override
	public List<ProductVO> getJumunProductList(String pnum) {
		List<ProductVO> jumunProductList = sqlsession.selectList("order.getJumunProductList", pnum);
		return jumunProductList;
	}

	
	
	
	
}
