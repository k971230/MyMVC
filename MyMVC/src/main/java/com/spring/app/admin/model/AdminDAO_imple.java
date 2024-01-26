package com.spring.app.admin.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.MemberVO;
import com.spring.app.domain.SpecVO;

@Repository
public class AdminDAO_imple implements AdminDAO {
	
	@Resource
	private SqlSessionTemplate sqlsession;

	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지 수 알아오기 //
	@Override
	public int getTotalPage(Map<String, String> paraMap) {
		int getTotalPage = sqlsession.selectOne("admin.getTotalPage", paraMap);
		return getTotalPage;
	}

	// 검색이 없는 회원목록
	@Override
	public List<MemberVO> select_Member_paging(Map<String, String> paraMap) {
		
		List<MemberVO> select_Member_paging = sqlsession.selectList("admin.select_Member_paging", paraMap);
		return select_Member_paging;
	}

	// 검색이 있는 회원목록
	@Override
	public List<MemberVO> searchMemberList(Map<String, String> paraMap) {
		List<MemberVO> searchMemberList = sqlsession.selectList("admin.searchMemberList", paraMap);
		return searchMemberList;
	}

	// 회원 상세
	@Override
	public MemberVO selectOneMember(String userid) {
		MemberVO selectOneMember = (MemberVO) sqlsession.selectOne("admin.selectOneMember", userid);
		return selectOneMember;
	}

	// 카테고리 목록을 조회해오기
	@Override
	public List<CategoryVO> selectCategoryList() {
		List<CategoryVO> selectCategoryList = sqlsession.selectList("admin.selectCategoryList");
		return selectCategoryList;
	}

	// SPEC 목록을 조회해오기
	@Override
	public List<SpecVO> selectSpecList() {
		List<SpecVO> selectSpecList = sqlsession.selectList("admin.selectSpecList");
		return selectSpecList;
	}
}
