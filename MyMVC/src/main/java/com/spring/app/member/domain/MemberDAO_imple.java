package com.spring.app.member.domain;

import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.spring.app.domain.MemberVO;


@Repository  
public class MemberDAO_imple implements MemberDAO {
	
	@Resource
	private SqlSessionTemplate sqlsession;
	
	// ==== 로그인 처리하기 ==== //
	@Override
	public MemberVO getLoginMember(Map<String, String> paraMap) {
		
		MemberVO loginuser = sqlsession.selectOne("member.getLoginMember", paraMap);
		return loginuser;
	
	}

	// tbl_member 테이블의 idle 컬럼의 값을 1로 변경하기
	@Override
	public int updateIdle(String userid) {
		int n = sqlsession.update("member.updateIdle", userid);
		return n;
	}

}
