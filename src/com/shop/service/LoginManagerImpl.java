package com.shop.service;

import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lambdaworks.crypto.SCryptUtil;
import com.shop.constant.AppConstant;
import com.shop.dao.UserDao;
import com.shop.domain.UserDomainObject;
import com.shop.domain.adapter.UserDomainObjectAdapter;
import com.shop.dto.User;

@Service
@Component("loginManagerImpl")
public class LoginManagerImpl implements LoginManager {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private UserDao userDaoImpl;
	
	
	private static boolean validateInput(User user){
		boolean valid = true;
		if(user.getUserName() == null || user.getUserName().trim().isEmpty()){
			valid = false;
		}
		if(user.getPassword() == null || user.getPassword().trim().isEmpty()){
			valid = false;
		}
		return valid;
	}
	
	@Override
	public String login(User user) {
		logger.debug(AppConstant.METHOD_IN);
		String result = AppConstant.SHOP_SUCCESSFUL_LOGIN;
		try {
			if(validateInput(user)){
				UserDomainObject availableUser = userDaoImpl.findByUserName(user.getUserName());
				if(availableUser == null){
					result = AppConstant.SHOP_UNSUCCESSFUL_LOGIN;
					logger.debug(MessageFormat.format(AppConstant.SHOP_USER_NOT_FOUND, user.getUserName()));
				} else {
					if(availableUser.getTryCounter() >= 3){
						logger.debug(MessageFormat.format(AppConstant.SHOP_USER_EXCEEDED_LOGIN_ATTEMPT, user.getUserName()));
						result = AppConstant.SHOP_EXCEEDED_LOGIN_ATTEMPT;
					} else {
						boolean matched = SCryptUtil.check(user.getPassword(), availableUser.getPassword());
						if(!matched){
							logger.debug(MessageFormat.format(AppConstant.SHOP_USER_PASSWORD_MISMATCH, user.getUserName()));
							result = AppConstant.SHOP_UNSUCCESSFUL_LOGIN;
							
							//add to total login again
							if(availableUser.getTryCounter() < 4){
								availableUser.setTryCounter(availableUser.getTryCounter()+1);
							}
							userDaoImpl.save(availableUser);
						}
					}
				}
			} else {
				result = AppConstant.SHOP_UNSUCCESSFUL_LOGIN;
			}
		} catch(Exception e){
			logger.error(e.getMessage());
			result = AppConstant.SHOP_UNSUCCESSFUL_LOGIN;
		}
		logger.debug(AppConstant.METHOD_OUT);
		return result;
	}

	@Override
	public String signup(User user) {
		logger.debug(AppConstant.METHOD_IN);
		
		String result = AppConstant.SHOP_SUCCESSFUL_SIGNUP;
		
		if(validateInput(user)){
			UserDomainObject availableUser = userDaoImpl.insert(UserDomainObjectAdapter.parseUser(user));
			
			if(availableUser == null){
				result = AppConstant.SHOP_UNSUCCESSFUL_SIGNUP;
			}
		} else {
			result = AppConstant.SHOP_UNSUCCESSFUL_SIGNUP;
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return result;
	}

}
