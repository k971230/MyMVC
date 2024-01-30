package com.spring.app.myshop.model;

import java.util.*;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.ImageVO;
import com.spring.app.domain.ProductVO;
import com.spring.app.domain.PurchaseReviewsVO;
import com.spring.app.myshop.service.ProductService;

@Repository 
public class ProductDAO_imple implements ProductDAO {
	
	@Resource
	private SqlSessionTemplate sqlsession;

	@Override
	public List<ImageVO> imageSelectAll() {
		
		//인덱스 이미지 가져오기
		List<ImageVO> imgList = sqlsession.selectList("cart.imageSelectAll"); 
		
		return imgList;
	}

	
	// 제품의 스펙별(HIT, NEW, BEST) 상품의 전체개수를 알아오기
	@Override
	public int totalPspecCount(String fk_snum) {
		
		int n = sqlsession.selectOne("product.totalPspecCount", fk_snum);
		
		return n;
	}

	
	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	@Override
	public List<CategoryVO> getCategoryList() {
		
		List<CategoryVO> categoryList = sqlsession.selectList("product.getCategoryList");
		
		return categoryList;
	}

	
	// 입력받은 cnum 이 DB 에 존재하는지 알아온다. (존재하지 않는 경우는 사용자가 장난친 경우)
	@Override
	public boolean isExist_cnum(String cnum) {

		boolean isExist_cnum = sqlsession.selectOne("product.isExist_cnum", cnum);
		
		return isExist_cnum;
	}
	
	
	// 페이징 처리를 위한 특정 카테고리에 대한 총 페이지 수 알아오기
	@Override
	public int getTotalPge(String cnum) {
		
		int totalPage = sqlsession.selectOne("product.getTotalPge", cnum);
		
		return totalPage;
	}
	
	
	// 특정한 카테고리에서 사용자가 보고자 하는 특정 페이지번호에 해당하는 제품들을 조회해오기
	@Override
	public List<ProductVO> selectProductByCategory(Map<String, String> paraMap) {
		
		List<ProductVO> productList = sqlsession.selectList("product.selectProductByCategory", paraMap);
		
		return productList;
	}
	
	
	// 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기
	@Override
	public List<ProductVO> selectBySpecName(Map<String, String> paraMap) {
		
		List<ProductVO> productList = sqlsession.selectList("product.selectBySpecName", paraMap);
		
		return productList;
	}


	// 제품번호를 가지고서 해당 제품의 정보를 조회해오기
	@Override
	public ProductVO selectOneProduct(String pnum) {
		
		ProductVO pvo = sqlsession.selectOne("product.selectOneProduct", pnum);
		
		return pvo;
	}

	
	// 제품번호를 가지고서 해당 제품의 추가된 이미지 정보를 조회해오기
	@Override
	public List<String> getImagesByPnum(String pnum) {
		
		List<String> imgList = sqlsession.selectList("product.getImagesByPnum", pnum);
		
		return imgList;
	}


	@Override
	public int addReview(Map<String, String> paraMap) {
		
		int n = sqlsession.insert("product.addReview", paraMap);
		
		return n;
	}

	
	@Override
	public List<PurchaseReviewsVO> reviewList(String fk_pnum) {
		List<PurchaseReviewsVO> reviewList = sqlsession.selectList("product.reviewList", fk_pnum);
		return reviewList;
	}



}
