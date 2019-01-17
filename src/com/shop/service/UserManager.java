package com.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shop.dto.User;

@Service
public interface UserManager {

	public List<User> getAccountsToReset();

	public List<User> resetAccounts(List<User> userList);
	
}
