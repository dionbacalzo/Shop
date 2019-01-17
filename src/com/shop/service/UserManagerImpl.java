package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.shop.constant.AppConstant;
import com.shop.dao.UserDao;
import com.shop.domain.UserDomainObject;
import com.shop.dto.User;
import com.shop.dto.adapter.UserAdapter;

@Service
@Component("userManagerImpl")
public class UserManagerImpl implements UserManager {

protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private UserDao userDaoImpl;
	
	@Override
	public List<User> getAccountsToReset() {
		logger.debug(AppConstant.METHOD_IN);
		
		List<User> userList = new ArrayList<User>();
		List<UserDomainObject> userDomainList = userDaoImpl.findByTryCounter(3);
		
		for (UserDomainObject userdomain: userDomainList) {
			userList.add(new UserAdapter(userdomain));
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return userList;
	}

	@Override
	public List<User> resetAccounts(List<User> userList) {
		List<UserDomainObject> userdomainList = new ArrayList<UserDomainObject>();
		for (User user: userList){
			UserDomainObject userdomain = userDaoImpl.findByUserName(user.getUsername());
			userdomain.setTryCounter(0);
			userdomainList.add(userdomain);
		}
		if(!userdomainList.isEmpty()){
			userDaoImpl.saveAll(userdomainList);
		}
		
		return getAccountsToReset();
	}

}
