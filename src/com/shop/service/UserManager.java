package com.shop.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.shop.dto.Result;
import com.shop.dto.User;

@Service
public interface UserManager {

	public List<User> getAccountsToReset();

	public List<User> resetAccounts(List<User> userList);
	
	public User retrieveByUsername(String username);

	public User updateNameByUsername(String username, User user);

	public Result updatePassword(UserDetails userInSession, Object object);
	
}
