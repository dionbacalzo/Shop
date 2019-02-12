package com.shop.dto.adapter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

import com.lambdaworks.crypto.SCryptUtil;
import com.shop.domain.UserDomainObject;
import com.shop.dto.User;

public class UserAdapter extends User {

	public UserAdapter(UserDomainObject user) {
		setUsername(user.getUserName());
		setRole(user.getRole());
	}
	
	public UserAdapter(User user) {
		setUsername(user.getUsername());
		setPassword(SCryptUtil.scrypt(user.getPassword(), 16, 16, 16));
	}
	
	public UserAdapter(HttpServletRequest request) {
		setUsername(request.getParameter("username"));
		setPassword(request.getParameter("password"));
		setRememberMe(request.getParameter("rememberMe"));
	}
	
	public UserAdapter(Authentication authentication) {
		//setUsername(authentication.getName());
	}
	
}
