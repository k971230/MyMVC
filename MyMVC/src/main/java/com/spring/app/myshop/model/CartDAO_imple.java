package com.spring.app.myshop.model;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CartDAO_imple implements CartDAO {
	
	@Resource
	private SqlSessionTemplate sqlsession;
}
