package com.spring.app.myshop.service;

import java.util.List;

import com.spring.app.domain.ImageVO;

public interface ProductService {
	
	// 인덱스 이미지 가져오기
	List<ImageVO> imageSelectAll();

}
