package com.shop.service;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class LoginManagerImpl implements LoginManager, AuthenticationProvider {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private UserDao userDaoImpl;
	
	private static String[] ROLE = {
			"USER", 
			"ADMIN", 
	}; 
	
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
	
	private static boolean validateSignUpInput(User user){
		boolean valid = validateInput(user);
		if(user.getRole() == null || user.getRole().trim().isEmpty()){
			valid = false;
		} else {
			if(!Arrays.asList(ROLE).contains(user.getRole())){
				valid = false;
			}
		}
		return valid;
	}
	
	@Override
	public String login(User user) {
		logger.debug(AppConstant.METHOD_IN);
		String result = AppConstant.SHOP_SUCCESSFUL_LOGIN;
		try {
			if(validateInput(user)){
				result = storeUser(user);
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
	
	public String storeUser(User user){
		logger.debug(AppConstant.METHOD_IN);
		String result = AppConstant.SHOP_SUCCESSFUL_LOGIN;
		try {
			UsernamePasswordAuthenticationToken authReq =
		            new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
	        
			Authentication auth = authenticate(authReq);
	        
	        SecurityContextHolder.getContext().setAuthentication(auth);
		} catch(Exception e){
			logger.error(e.getMessage());
			result = AppConstant.SHOP_UNSUCCESSFUL_LOGIN;
		}
		logger.debug(AppConstant.METHOD_OUT);
		return result;
	}

	@Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
		String result = AppConstant.SHOP_SUCCESSFUL_LOGIN;
		UsernamePasswordAuthenticationToken authToken = null;
		
		String username = auth.getName();
        String password = auth.getCredentials().toString();
        
		UserDomainObject availableUser = userDaoImpl.findByUserName(username);
		if(availableUser == null){
			result = AppConstant.SHOP_UNSUCCESSFUL_LOGIN;
			logger.debug(MessageFormat.format(AppConstant.SHOP_USER_NOT_FOUND, username));
		} else {
			if(availableUser.getTryCounter() >= 3){
				logger.debug(MessageFormat.format(AppConstant.SHOP_USER_EXCEEDED_LOGIN_ATTEMPT, username));
				result = AppConstant.SHOP_EXCEEDED_LOGIN_ATTEMPT;
			} else {
				boolean matched = SCryptUtil.check(password, availableUser.getPassword());
				if(!matched){
					logger.debug(MessageFormat.format(AppConstant.SHOP_USER_PASSWORD_MISMATCH, username));
					result = AppConstant.SHOP_UNSUCCESSFUL_LOGIN;
					
					//add to total login again
					if(availableUser.getTryCounter() < 4){
						availableUser.setTryCounter(availableUser.getTryCounter()+1);
					}
					userDaoImpl.save(availableUser);
				}
			}
		}
		
		if(result.equalsIgnoreCase(AppConstant.SHOP_SUCCESSFUL_LOGIN)){
			//get role
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(availableUser.getRole()));
	        authToken = new UsernamePasswordAuthenticationToken(username, password, grantedAuthorities);
		} else {
			throw new BadCredentialsException(AppConstant.SHOP_UNSUCCESSFUL_LOGIN);
		}
		
        return authToken;
    }
 
    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    @Override
	public String signup(User user) {
		logger.debug(AppConstant.METHOD_IN);
		
		String result = AppConstant.SHOP_SUCCESSFUL_SIGNUP;
		
		if(validateSignUpInput(user)){
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
