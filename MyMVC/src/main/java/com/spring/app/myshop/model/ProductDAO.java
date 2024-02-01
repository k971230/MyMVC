package com.spring.app.myshop.model;

import java.util.*;

import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.ImageVO;
import com.spring.app.domain.ProductVO;
import com.spring.app.domain.PurchaseReviewsVO;

public interface ProductDAO {
	
	//인덱스 이미지 가져오기
	List<ImageVO> imageSelectAll();

	// 제품의 스펙별(HIT, NEW, BEST) 상품의 전체개수를 알아오기
	int totalPspecCount(String fk_snum);

	// tbl_category 테이블에서 카테고리 대분류 번호(cnum), 카테고리코드(code), 카테고리명(cname)을 조회해오기 
	List<CategoryVO> getCategoryList();

	// 입력받은 cnum 이 DB 에 존재하는지 알아온다. (존재하지 않는 경우는 사용자가 장난친 경우)
	boolean isExist_cnum(String cnum);
	
	// 페이징 처리를 위한 특정 카테고리에 대한 총 페이지 수 알아오기
	int getTotalPge(String cnum);
	
	// 특정한 카테고리에서 사용자가 보고자 하는 특정 페이지번호에 해당하는 제품들을 조회해오기
	List<ProductVO> selectProductByCategory(Map<String, String> paraMap);
	
	// 더보기 방식(페이징처리)으로 상품정보를 8개씩 잘라서(start ~ end) 조회해오기
	List<ProductVO> selectBySpecName(Map<String, String> paraMap);

	// 제품번호를 가지고서 해당 제품의 정보를 조회해오기
	ProductVO selectOneProduct(String pnum);

	// 제품번호를 가지고서 해당 제품의 추가된 이미지 정보를 조회해오기
	List<String> getImagesByPnum(String pnum);

	int addReview(Map<String, String> paraMap);

	List<PurchaseReviewsVO> reviewList(String fk_pnum);

	int isOrderCheck(Map<String, String> paraMap);

	int reviewDel(String review_seq);

	int reviewUpdate(Map<String, String> paraMap);

	int likeAdd(Map<String, String> paraMap);

	int delAdd(Map<String, String> paraMap);

	Map<String, Integer> getLikeDislikeCnt(String pnum);

	int likedisAdd(Map<String, String> paraMap);

	int deldisAdd(Map<String, String> paraMap);



}
