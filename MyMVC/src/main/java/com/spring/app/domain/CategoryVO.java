package com.spring.app.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryVO {

	private int    cnum;   // 카테고리 대분류 번호
	private String code;   // 카테고리 코드
	private String cname;  // 카테고리명
	
	public CategoryVO() { }
	
	public CategoryVO(int cnum, String code, String cname) {
		this.cnum = cnum;
		this.code = code;
		this.cname = cname;
	}

	
	
}
