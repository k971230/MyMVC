package com.spring.app.myshop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.app.myshop.model.CartDAO;

@Service
public class CartService_imple implements CartService {
	
	@Autowired  
	private CartDAO dao;
}
