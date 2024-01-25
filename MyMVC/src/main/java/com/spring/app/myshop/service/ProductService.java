package com.spring.app.myshop.service;

import java.util.List;
import java.util.Map;

import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.ImageVO;
import com.spring.app.domain.ProductVO;

public interface ProductService {
	
	// 인덱스 이미지 가져오기
	List<ImageVO> imageSelectAll();

	// HIT 상품의 전체개수를 알아온다. 
	int totalPspecCount(String fk_snum);

	// 카테고리 리스트를 얻어온다.
	List<CategoryVO> getCategoryList();

	// 입력받은 cnum 이 DB 에 존재하는지 알아온다. (존재하지 않는 경우는 사용자가 장난친 경우)
	boolean isExist_cnum(String cnum);

	// 페이징 처리를 위한 특정 카테고리에 대한 총 페이지 수 알아오기
	int getTotalPage(String cnum);

	// 특정한 카테고리에서 사용자가 보고자 하는 특정 페이지번호에 해당하는 제품들을 조회해오기
	List<ProductVO> selectProductByCategory(Map<String, String> paraMap);
	
	// 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기
	List<ProductVO> selectBySpecName(Map<String, String> paraMap);

	// 제품번호를 가지고서 해당 제품의 정보를 조회해오기
	ProductVO selectOneProduct(String pnum);

	// 제품번호를 가지고서 해당 제품의 추가된 이미지 정보를 조회해오기
	List<String> getImagesByPnum(String pnum);




}
