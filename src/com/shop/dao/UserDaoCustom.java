package com.shop.dao;

import com.shop.domain.UserDomainObject;
import com.shop.dto.Result;
import com.shop.dto.User;

public interface UserDaoCustom {
	
	public UserDomainObject UpdateNameByUserName(String username, User user);
	
	public Result UpdatePasswordByUserName(String username, String newPassword);
	
}
