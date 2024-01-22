package com.spring.app.myshop.model;

import java.util.List;

import com.spring.app.domain.ImageVO;

public interface ProductDAO {
	
	//인덱스 이미지 가져오기
	List<ImageVO> imageSelectAll();

}
