package com.spring.app.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.admin.model.AdminDAO;
import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.MemberVO;
import com.spring.app.domain.SpecVO;
import com.spring.app.myshop.model.CartDAO;

@Service
public class AdminService_imple implements AdminService {
	
	@Autowired  
	private AdminDAO dao;

	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지 수 알아오기 //
	@Override
	public int getTotalPage(Map<String, String> paraMap) {
		int getTotalPage = dao.getTotalPage(paraMap);
		return getTotalPage;
	}

	// 검색이 없는 회원목록
	@Override
	public List<MemberVO> select_Member_paging(Map<String, String> paraMap) {
		List<MemberVO> select_Member_paging = dao.select_Member_paging(paraMap);
		return select_Member_paging;
	}

	// 검색이 있는 회원목록
	@Override
	public List<MemberVO> searchMemberList(Map<String, String> paraMap) {
		List<MemberVO> searchMemberList = dao.searchMemberList(paraMap);
		return searchMemberList;
	}

	// 회원 상세
	@Override
	public MemberVO selectOneMember(String userid) {
		MemberVO selectOneMember = dao.selectOneMember(userid);
		return selectOneMember;
	}

	// 카테고리 목록을 조회해오기
	@Override
	public List<CategoryVO> selectCategoryList() {
		List<CategoryVO> selectCategoryList = dao.selectCategoryList();
		return selectCategoryList;
	}

	// SPEC 목록을 조회해오기
	@Override
	public List<SpecVO> selectSpecList() {
		List<SpecVO> selectSpecList = dao.selectSpecList();
		return selectSpecList;
	}
}
