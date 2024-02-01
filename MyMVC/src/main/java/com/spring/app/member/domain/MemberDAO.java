package com.spring.app.member.domain;

import java.util.Map;

import com.spring.app.domain.MemberVO;

public interface MemberDAO {
	
	// ==== 로그인 처리하기 ==== //
	MemberVO getLoginMember(Map<String, String> paraMap);
	
	// tbl_member 테이블의 idle 컬럼의 값을 1로 변경하기
	int updateIdle(String string);

	int registerMember(MemberVO mvo);

	String idDuplicateCheck(String userid);

	String emailDuplicateCheck(String email);

	String emailDuplicateCheck2(Map<String, String> paraMap);

	String duplicatePwdCheck(Map<String, String> paraMap);

	int updateMember(MemberVO mvo);

	int coinUpdateLoginUser(Map<String, String> paraMap);
	

	
}
