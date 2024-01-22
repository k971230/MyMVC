package com.spring.app.myshop.model;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.spring.app.domain.ImageVO;
import com.spring.app.myshop.service.ProductService;

@Repository 
public class ProductDAO_imple implements ProductDAO {
	
	@Resource
	private SqlSessionTemplate sqlsession;

	@Override
	public List<ImageVO> imageSelectAll() {
		
		//인덱스 이미지 가져오기
		List<ImageVO> imgList = sqlsession.selectList("yunsu.imageSelectAll"); 
		
		return imgList;
	}

}
