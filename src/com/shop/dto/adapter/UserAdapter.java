package com.shop.dto.adapter;

import com.lambdaworks.crypto.SCryptUtil;
import com.shop.dto.User;

public class UserAdapter extends User {

	public UserAdapter(User user) {
		setUserName(user.getUserName());
		setPassword(SCryptUtil.scrypt(user.getPassword(), 16, 16, 16));
	}
	
}
