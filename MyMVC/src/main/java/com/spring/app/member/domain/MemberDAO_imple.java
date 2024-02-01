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

	@Override
	public int registerMember(MemberVO mvo) {
		
		int n = sqlsession.update("member.registerMember", mvo);
		
		return n;
	}

	@Override
	public String idDuplicateCheck(String userid) {

		String result = sqlsession.selectOne("member.idDuplicateCheck", userid);
		
		return result;
	}

	@Override
	public String emailDuplicateCheck(String email) {
		
	    
        String result = sqlsession.selectOne("member.emailDuplicateCheck", email);
        
        return result;
	    
	}


	@Override
	public String emailDuplicateCheck2(Map<String, String> paraMap) {
		
        String result = sqlsession.selectOne("member.emailDuplicateCheck2", paraMap);
        
        return result;
	    
	}

	@Override
	public String duplicatePwdCheck(Map<String, String> paraMap) {
		
		String result = sqlsession.selectOne("member.duplicatePwdCheck", paraMap);
	        
        return result;
	}

	@Override
	public int updateMember(MemberVO mvo) {
		
		int n = sqlsession.update("member.updateMember", mvo);
		
		return n;
	}

	@Override
	public int coinUpdateLoginUser(Map<String, String> paraMap) {
		
		int n = sqlsession.update("member.coinUpdateLoginUser", paraMap);
		
		return n;
	}
	

	
}
