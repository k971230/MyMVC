package com.spring.app.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SpecVO {

	private int snum;      // 스펙번호       
	private String sname;  // 스펙명 
	
	public SpecVO() {}
	
	public SpecVO(int snum, String sname) {
	
		this.snum = snum;
		this.sname = sname;
	}

	
	
}
