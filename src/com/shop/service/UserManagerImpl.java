package com.shop.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaworks.crypto.SCryptUtil;
import com.shop.constant.AppConstant;
import com.shop.dao.UserDao;
import com.shop.domain.UserDomainObject;
import com.shop.dto.Result;
import com.shop.dto.User;
import com.shop.dto.adapter.UserAdapter;

@Service
@Component("userManagerImpl")
public class UserManagerImpl implements UserManager {

	protected final Logger logger = Logger.getLogger(getClass());

	@Autowired
	private UserDao userDaoImpl;
	
	@Autowired
    private UserDetailsService userDetailsService;

	@Override
	public List<User> getAccountsToReset() {
		logger.debug(AppConstant.METHOD_IN);

		List<User> userList = new ArrayList<User>();
		List<UserDomainObject> userDomainList = userDaoImpl.findByTryCounter(3);

		for (UserDomainObject userdomain : userDomainList) {
			userList.add(new UserAdapter(userdomain));
		}

		logger.debug(AppConstant.METHOD_OUT);

		return userList;
	}

	@Override
	public List<User> resetAccounts(List<User> userList) {
		logger.debug(AppConstant.METHOD_IN);
		List<UserDomainObject> userdomainList = new ArrayList<UserDomainObject>();
		for (User user : userList) {
			UserDomainObject userdomain = userDaoImpl.findByUserName(user.getUsername());
			userdomain.setTryCounter(0);
			userdomainList.add(userdomain);
		}
		if (!userdomainList.isEmpty()) {
			userDaoImpl.saveAll(userdomainList);
		}
		logger.debug(AppConstant.METHOD_OUT);
		return getAccountsToReset();
	}

	@Override
	public User retrieveByUsername(String username) {
		logger.debug(AppConstant.METHOD_IN);

		UserDomainObject user = userDaoImpl.findByUserName(username);

		logger.debug(AppConstant.METHOD_OUT);
		return new UserAdapter(user);
	}

	private boolean validateNameInput(User user) {
		boolean valid = true;
		if (user.getFirstname() == null || user.getFirstname().trim().isEmpty()) {
			valid = false;
		} else if (user.getLastname() == null || user.getLastname().trim().isEmpty()) {
			valid = false;
		}

		return valid;
	}

	@Override
	public User updateNameByUsername(String username, User user) {
		logger.debug(AppConstant.METHOD_IN);

		User updatedUser = null;
		if (validateNameInput(user)) {
			updatedUser = new UserAdapter(userDaoImpl.UpdateNameByUserName(username, user));
		} else {
			logger.error(AppConstant.SHOP_PROFILE_INVALID_INPUT);
		}

		logger.debug(AppConstant.METHOD_OUT);
		return updatedUser;
	}

	private Result validatePasswords(String oldPassword, String newPassword, String newPasswordRetype) {
		Result result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_SUCCESSFUL_STATUS,
				AppConstant.SHOP_PASSWORD_UPDATE_MESSAGE_SUCCESS);
		if (oldPassword == null || oldPassword.trim().isEmpty()) {
			result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS,
					AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_MISSING_PASSWORD);
		} else if (newPassword == null || newPassword.trim().isEmpty()) {
			result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS,
					AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_MISSING_NEWPASSWORD);
		} else if (newPasswordRetype == null || newPasswordRetype.trim().isEmpty()) {
			result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS,
					AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_MISSING_NEWPASSWORD2);
		} else if (!newPasswordRetype.equals(newPassword)) {
			result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS,
					AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_INCORRECT_NEWPASSWORD);
		} else if (oldPassword.equals(newPassword)) {
			result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS,
					AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_MATCHING_NEWPASSWORD);
		}
		
		return result;
	}

	@Override
	public Result updatePassword(UserDetails userInSession, Object object) {
		logger.debug(AppConstant.METHOD_IN);

		Result result = new Result();
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(object);

			JsonNode jsonNode = mapper.readTree(json);
			String oldPassword = jsonNode.get("password").asText();
			String newPassword = jsonNode.get("newPassword").asText();
			String newPasswordRetype = jsonNode.get("newPasswordRetype").asText();
			result = validatePasswords(oldPassword, newPassword, newPasswordRetype);
			if (result.getStatus() != AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS) {
				// check if password given matches current password
				boolean matched = SCryptUtil.check(oldPassword, userInSession.getPassword());
				if (matched) {
					result = userDaoImpl.UpdatePasswordByUserName(userInSession.getUsername(),
							SCryptUtil.scrypt(newPassword, 16, 16, 16));
					// update the current password in session
					if (result.getStatus() != AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS) {
						UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
								userDetailsService.loadUserByUsername(userInSession.getUsername()), newPassword, userInSession.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(authToken);
					}
				} else {
					result = new Result(AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_STATUS,
							AppConstant.SHOP_PASSWORD_UPDATE_UNSUCCESSFUL_INCORRECT_PASSWORD);
					logger.error(result.getMessage());
				}
			} else {
				logger.error(result.getMessage());
			}

		} catch (IOException e) {
			result = new Result(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS,
					AppConstant.SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_GENERIC);
			logger.error(result.getMessage());
		}
		logger.debug(AppConstant.METHOD_OUT);

		return result;
	}

}
