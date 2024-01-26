package com.spring.app.admin.service;

import java.util.List;
import java.util.Map;

import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.MemberVO;
import com.spring.app.domain.SpecVO;

public interface AdminService {

	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지 수 알아오기 //
	int getTotalPage(Map<String, String> paraMap);
	
	// 검색이 없는 회원목록
	List<MemberVO> select_Member_paging(Map<String, String> paraMap);

	// 검색이 있는 회원목록
	List<MemberVO> searchMemberList(Map<String, String> paraMap);

	// 회원 상세
	MemberVO selectOneMember(String userid);

	// 카테고리 목록을 조회해오기
	List<CategoryVO> selectCategoryList();

	// SPEC 목록을 조회해오기
	List<SpecVO> selectSpecList();
	
	
}
