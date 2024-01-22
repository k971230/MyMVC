package com.spring.app.myshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.domain.ImageVO;
import com.spring.app.myshop.model.ProductDAO;


@Service
public class ProductService_imple implements ProductService {
	
	@Autowired
	private ProductDAO pdao;
	
	//인덱스 이미지 가져오기
	@Override
	public List<ImageVO> imageSelectAll() {
		
		List<ImageVO> imgList = pdao.imageSelectAll();
				
		return imgList;
	}

}
