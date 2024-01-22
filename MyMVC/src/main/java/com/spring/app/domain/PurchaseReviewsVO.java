package com.spring.app.domain;

import com.spring.app.domain.MemberVO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PurchaseReviewsVO {

	private int review_seq; 
	private String fk_userid;
	private int fk_pnum; 
	private String contents; 
	private String writeDate;
	
	private MemberVO mvo;
	private ProductVO pvo;
	
	public PurchaseReviewsVO() { }

	public PurchaseReviewsVO(int review_seq, String fk_userid, int fk_pnum, String contents, String writeDate,
			MemberVO mvo, ProductVO pvo) {
		this.review_seq = review_seq;
		this.fk_userid = fk_userid;
		this.fk_pnum = fk_pnum;
		this.contents = contents;
		this.writeDate = writeDate;
		this.mvo = mvo;
		this.pvo = pvo;
	}

	
		
}