package com.shop.domain.adapter;

import com.lambdaworks.crypto.SCryptUtil;
import com.shop.domain.UserDomainObject;
import com.shop.dto.User;

public class UserDomainObjectAdapter extends UserDomainObject {

	public UserDomainObjectAdapter(User user) {
		setUserName(user.getUserName());
		setPassword(SCryptUtil.scrypt(user.getPassword(), 16, 16, 16));
	}
	
	public static UserDomainObject parseUser(User user){
		UserDomainObject userDomainObject = new UserDomainObject();
		userDomainObject.setUserName(user.getUserName());
		userDomainObject.setPassword(SCryptUtil.scrypt(user.getPassword(), 16, 16, 16));
		return userDomainObject;
	}
	
}
