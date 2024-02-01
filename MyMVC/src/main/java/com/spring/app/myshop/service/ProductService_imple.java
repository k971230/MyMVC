package com.spring.app.myshop.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.ImageVO;
import com.spring.app.domain.ProductVO;
import com.spring.app.domain.PurchaseReviewsVO;
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

	// 제품의 스펙별(HIT, NEW, BEST) 상품의 전체개수를 알아오기
	@Override
	public int totalPspecCount(String fk_snum) {
		
		int n = pdao.totalPspecCount(fk_snum);
		
		return n;
	}

	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	@Override
	public List<CategoryVO> getCategoryList() {
		
		List<CategoryVO> categoryList = pdao.getCategoryList();
		
		return categoryList;
	}
	
	// 입력받은 cnum 이 DB 에 존재하는지 알아온다. (존재하지 않는 경우는 사용자가 장난친 경우)
	@Override
	public boolean isExist_cnum(String cnum) {

		boolean isExist_cnum = pdao.isExist_cnum(cnum);
		
		return isExist_cnum;
	}

	// 페이징 처리를 위한 특정 카테고리에 대한 총 페이지 수 알아오기
	@Override
	public int getTotalPage(String cnum) {
		
		int totalPage = pdao.getTotalPge(cnum);
		
		return totalPage;
	}
	
	// 특정한 카테고리에서 사용자가 보고자 하는 특정 페이지번호에 해당하는 제품들을 조회해오기
	@Override
	public List<ProductVO> selectProductByCategory(Map<String, String> paraMap) {
		
		List<ProductVO> productList = pdao.selectProductByCategory(paraMap);
		
		return productList;
	}
	
	// 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기
	@Override
	public List<ProductVO> selectBySpecName(Map<String, String> paraMap) {

		List<ProductVO> productList = pdao.selectBySpecName(paraMap);
		
		return productList;
	}

	// 제품번호를 가지고서 해당 제품의 정보를 조회해오기
	@Override
	public ProductVO selectOneProduct(String pnum) {
		
		ProductVO pvo = pdao.selectOneProduct(pnum);
		
		return pvo;
	}

	// 제품번호를 가지고서 해당 제품의 추가된 이미지 정보를 조회해오기
	@Override
	public List<String> getImagesByPnum(String pnum) {
		
		List<String> imgList = pdao.getImagesByPnum(pnum);
		
		return imgList;
	}

	@Override
	public int addReview(Map<String, String> paraMap) {
		int i = pdao.addReview(paraMap);
		
		return i;
	}

	@Override
	public List<PurchaseReviewsVO> reviewList(String fk_pnum) {
		List<PurchaseReviewsVO> reviewList = pdao.reviewList(fk_pnum);
		
		return reviewList;
	}

	@Override
	public int isOrderCheck(Map<String, String> paraMap) {
		int n = pdao.isOrderCheck(paraMap);
		
		return n;
	}

	@Override
	public int reviewDel(String review_seq) {
		int n = pdao.reviewDel(review_seq);
		
		return n;
	}

	@Override
	public int reviewUpdate(Map<String, String> paraMap) {
		int n = pdao.reviewUpdate(paraMap);
		
		return n;
	}

	@Override
	public int likeAdd(Map<String, String> paraMap) {
		
		pdao.delAdd(paraMap);
		
		int n = pdao.likeAdd(paraMap);
		
		return n;
	}
	
	@Override
	public int dislikeAdd(Map<String, String> paraMap) {
		
		pdao.likedisAdd(paraMap);
		
		int n = pdao.deldisAdd(paraMap);
		
		return n;
	}

	@Override
	public Map<String, Integer> getLikeDislikeCnt(String pnum) {
		
		Map<String, Integer> map = pdao.getLikeDislikeCnt(pnum);
		return map;
	}



	

}
