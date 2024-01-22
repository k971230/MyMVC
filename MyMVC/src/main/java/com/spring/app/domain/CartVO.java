package com.spring.app.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartVO { // (오라클로 말하면 제품테이블의 자식테이블)
	
	 private int cartno;        //  장바구니 번호             
	 private String fk_userid;  //  사용자ID            
	 private int fk_pnum;       //  제품번호                
	 private int oqty;          //  주문량                   
	 
	 private String registerday;//장바구니 입력날짜
	 
	 private ProductVO prod;	// 제품정보객체 (오라클로 말하면 부모테이블)
	 
	 public CartVO() {};
	 
	 
	 public CartVO(int cartno, String fk_userid, int fk_pnum, int oqty, String registerday) {
		
		this.cartno = cartno;
		this.fk_userid = fk_userid;
		this.fk_pnum = fk_pnum;
		this.oqty = oqty;
		this.registerday = registerday;
	}

	

}
