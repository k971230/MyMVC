package com.spring.app.admin.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.admin.model.AdminDAO;
import com.spring.app.common.AES256;
import com.spring.app.domain.CategoryVO;
import com.spring.app.domain.MemberVO;
import com.spring.app.domain.SpecVO;
import com.spring.app.myshop.model.CartDAO;

@Service
public class AdminService_imple implements AdminService {
	
	@Autowired  
	private AdminDAO dao;
	
	@Autowired
	private AES256 AES256;

	// 페이징 처리를 위한 검색이 있는 또는 검색이 없는 회원에 대한 총페이지 수 알아오기 //
	@Override
	public int getTotalPage(Map<String, String> paraMap) {
		int getTotalPage = dao.getTotalPage(paraMap);
		return getTotalPage;
	}

	// 검색이 없는 회원목록
	@Override
	public List<MemberVO> select_Member_paging(Map<String, String> paraMap) {
		
		String searchType = paraMap.get("searchType");
		String searchWord = paraMap.get("searchWord");
		
		System.out.println("searchType => " + searchType);
		System.out.println("searchWord => " + searchWord);
		
		if("email".equals(searchType)) {
			// 검색대상이 email 인경우
			try {
				searchWord = AES256.encrypt(searchWord);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			System.out.println("이메일 searchWord => " + searchWord);
		}
		// 검색이 있다면
		if( (searchType != null && !searchType.trim().isEmpty()) &&
			(searchWord != null && !searchWord.trim().isEmpty()) ) {
			
		}
		List<MemberVO> select_Member_paging = dao.select_Member_paging(paraMap);
		return select_Member_paging;
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
