package com.shop.service;

import org.springframework.stereotype.Service;

import com.shop.dto.User;

@Service
public interface LoginManager {

	public String login(User user);

	public String signup(User user);

	public boolean allowUserRememberMeToken(User user);

}
