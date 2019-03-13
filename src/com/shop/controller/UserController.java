package com.shop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.constant.AppConstant;
import com.shop.dto.Result;
import com.shop.dto.User;
import com.shop.service.UserManager;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true") //allow CORS for angular
@RestController
public class UserController {
	
	private final Logger logger = Logger.getLogger(getClass());
	
	@Autowired @Qualifier("userManagerImpl")	
	private UserManager userManagerImpl;
	
	@RequestMapping(value = "/admin")
	protected ModelAndView viewAdminPage() throws Exception {
		logger.debug(AppConstant.METHOD_IN);

		ModelAndView model = new ModelAndView("admin");
		
		logger.debug(AppConstant.METHOD_OUT);
		return model;
	}
	
	@RequestMapping(value = "/profile")
	protected ModelAndView viewProfilePage() throws Exception {
		logger.debug(AppConstant.METHOD_IN);

		ModelAndView model = new ModelAndView("profile");
		
		logger.debug(AppConstant.METHOD_OUT);
		return model;
	}

	/**
	 * retrieves the current user from the session if available
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/retrieveUser")
    @ResponseBody
    public String currentUserName(Authentication authentication) {
		logger.debug(AppConstant.METHOD_IN);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (authentication != null && authentication.getPrincipal() != null) {
				UserDetails userInSession = (UserDetails) authentication.getPrincipal();
				User user = userManagerImpl.retrieveByUsername(userInSession.getUsername());
				json = mapper.writeValueAsString(user);
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		
		logger.debug(AppConstant.METHOD_OUT);
        return json;
    }
	
	/**
	 * returns a list of accounts that has exceeded the maximum number of login attempts
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "accountResetList")
	protected String accountResetList() throws Exception {
		logger.debug(AppConstant.METHOD_IN);
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("users", userManagerImpl.getAccountsToReset());
			json = mapper.writeValueAsString(items);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return json;
	}
	
	/**
	 * resets accounts that has exceeded the maximum number of login attempts
	 * @return the list of users wrapped in a json object, format is {users:[]} 
	 * @throws Exception
	 */
	@RequestMapping(value = "resetAccount")
	protected String resetAccount(@RequestBody List<User> userList) throws Exception {
		logger.debug(AppConstant.METHOD_IN);
		
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			Map<String, Object> items = new HashMap<String, Object>();
			items.put("users", userManagerImpl.resetAccounts(userList));
			json = mapper.writeValueAsString(items);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return json;
	}
	
	/**
	 * Updates the firstname and lastname of logged in user
	 * @param authentication
	 * @param user
	 * @return the updated user details
	 * @throws Exception
	 */
	@RequestMapping(value = "updateUser")
	protected String updateUser(Authentication authentication, @RequestBody User user) throws Exception {
		logger.debug(AppConstant.METHOD_IN);
		String json = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (authentication != null && authentication.getPrincipal() != null) {
				UserDetails userInSession = (UserDetails) authentication.getPrincipal();
				user = userManagerImpl.updateNameByUsername(userInSession.getUsername(), user);
				json = mapper.writeValueAsString(user);
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		
		logger.debug(AppConstant.METHOD_OUT);
        return json;
	}
	
	/**
	 * Updates the password of logged in user, object contains the old and new password
	 * @param authentication
	 * @param object
	 * @return result object containing the status and the result message
	 * @throws Exception
	 */
	@RequestMapping(value = "updatePassword")
	protected String updatePassword(Authentication authentication, @RequestBody Object object) throws Exception {
		logger.debug(AppConstant.METHOD_IN);
		
		Result result = new Result();
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (authentication != null && authentication.getPrincipal() != null) {
				UserDetails userInSession = (UserDetails) authentication.getPrincipal();
				result = userManagerImpl.updatePassword(userInSession, object);
				json = mapper.writeValueAsString(result);
			}
		} catch (Exception e){
			logger.error(e.getMessage());
		}
		
		logger.debug(AppConstant.METHOD_OUT);
        return json;
	}

}
