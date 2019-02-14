package com.shop.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lambdaworks.crypto.SCryptUtil;
import com.shop.constant.AppConstant;
import com.shop.dao.TokenDao;
import com.shop.dao.UserDao;
import com.shop.domain.TokenDomainObject;
import com.shop.domain.UserDomainObject;
import com.shop.domain.adapter.UserDomainObjectAdapter;
import com.shop.dto.Result;
import com.shop.dto.User;
import com.shop.dto.adapter.UserAdapter;
import com.shop.exception.ShopException;

@Service
@Component("loginManagerImpl")
public class LoginManagerImpl implements LoginManager, AuthenticationProvider {

	protected final Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private UserDao userDaoImpl;
	
	@Autowired
	private TokenDao tokenDao;
	
	private static String[] ROLE = {
			"USER", 
			"ADMIN", 
	}; 
	
	private static boolean validateInput(User user){
		boolean valid = true;
		if(user.getUsername() == null || user.getUsername().trim().isEmpty()){
			valid = false;
		}
		if(user.getPassword() == null || user.getPassword().trim().isEmpty()){
			valid = false;
		}
		
		return valid;
	}
	
	private static boolean validateSignUpInput(User user){
		boolean valid = validateInput(user);
		if(user.getFirstname() == null || user.getFirstname().trim().isEmpty()){
			valid = false;
		}
		if(user.getLastname() == null || user.getLastname().trim().isEmpty()){
			valid = false;
		}
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
	public Result login(User user) {
		logger.debug(AppConstant.METHOD_IN);
		Result result = new Result(AppConstant.SHOP_LOGIN_SUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_SUCCESSFUL_MESSAGE);
		try {
			if(validateInput(user)){
				result = storeUser(user);
			} else {
				logger.debug(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS + user.getUsername());
				result = new Result(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_GENERIC);
			}
		} catch(Exception e){
			logger.error(e.getMessage());
			result = new Result(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_GENERIC);
		}
		logger.debug(AppConstant.METHOD_OUT);
		return result;
	}
	
	/**
	 * Store user to session
	 * @param user
	 * @return
	 */
	public Result storeUser(User user){
		logger.debug(AppConstant.METHOD_IN);
		Result result = new Result(AppConstant.SHOP_LOGIN_SUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_SUCCESSFUL_MESSAGE);
		try {
			UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

			Authentication auth = authenticate(authReq);
	        
	        SecurityContextHolder.getContext().setAuthentication(auth);
	        UserDetails userInSession = (UserDetails) auth.getPrincipal();
	        result.setDetails(new UserAdapter(userDaoImpl.findByUserName(userInSession.getUsername())));
		} catch(ShopException e){
			logger.debug(e.getMessage());
			result = new Result(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS, e.getMessage());
		} catch(Exception e){
			logger.error(e.getMessage());
			result = new Result(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_GENERIC);
		}
		logger.debug(AppConstant.METHOD_OUT);
		return result;
	}
	
	@Autowired
    private UserDetailsService userDetailsService;

	@Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
		Result result = new Result(AppConstant.SHOP_LOGIN_SUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_SUCCESSFUL_MESSAGE);
		UsernamePasswordAuthenticationToken authToken = null;
		
		String username = auth.getName();
        String password = auth.getCredentials().toString();

        UserDomainObject availableUser = userDaoImpl.findByUserName(username);
		if(availableUser == null){
			result = new Result(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_GENERIC);
			logger.debug(MessageFormat.format(AppConstant.SHOP_USER_NOT_FOUND, username));
		} else {
			if(availableUser.getTryCounter() >= 3){
				logger.debug(MessageFormat.format(AppConstant.SHOP_USER_EXCEEDED_LOGIN_ATTEMPT, username));
				result = new Result(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_LIMIT);
			} else {
				boolean matched = SCryptUtil.check(password, availableUser.getPassword());
				if(!matched){
					//add to total login again
					if(availableUser.getTryCounter() < 4){
						availableUser.setTryCounter(availableUser.getTryCounter()+1);
					}
					userDaoImpl.save(availableUser);
					logger.debug(MessageFormat.format(AppConstant.SHOP_USER_PASSWORD_MISMATCH, username));
					result = new Result(AppConstant.SHOP_LOGIN_UNSUCCESSFUL_STATUS, AppConstant.SHOP_LOGIN_UNSUCCESSFUL_MESSAGE_GENERIC);
				}
			}
		}
		
		if(result.getStatus().equalsIgnoreCase(AppConstant.SHOP_LOGIN_SUCCESSFUL_STATUS)){
			//get role
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
            grantedAuthorities.add(new SimpleGrantedAuthority(availableUser.getRole()));
	        authToken = new UsernamePasswordAuthenticationToken(userDetailsService.loadUserByUsername(username), password, grantedAuthorities);
	        //reset counter for a successful login
	        if(availableUser.getTryCounter() > 0){
				availableUser.setTryCounter(0);
				userDaoImpl.save(availableUser);
			}
		} else {
			throw new ShopException(result.getMessage());
		}
		
        return authToken;
    }
 
    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    @Override
	public Result signup(User user) {
		logger.debug(AppConstant.METHOD_IN);
		
		Result result = new Result(AppConstant.SHOP_SIGNUP_SUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_SUCCESSFUL_MESSAGE);
		UserDomainObject availableUser = null;
		if(validateSignUpInput(user)){
			try {
				availableUser = userDaoImpl.insert(UserDomainObjectAdapter.parseUser(user));
			} catch (Exception e) {
				logger.error(AppConstant.SHOP_SIGNUP_STORE_UNSUCCESSFUL);
				result = new Result(AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_MESSAGE_GENERIC);
			}
			
			if(availableUser == null){
				logger.error(AppConstant.SHOP_SIGNUP_STORE_UNSUCCESSFUL);
				result = new Result(AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_MESSAGE_GENERIC);
			} else {
				logger.debug(AppConstant.SHOP_SIGNUP_STORE_SUCCESSFUL);
			}
		} else {
			logger.error(AppConstant.SHOP_SIGNUP_INVALID_CREDENTIALS);
			result = new Result(AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_MESSAGE_GENERIC);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return result;
	}
    
    @Override
	public boolean allowUserRememberMeToken(User user) {
    	boolean allowtoken = false;
		logger.debug(AppConstant.METHOD_IN);
		
		List<TokenDomainObject> tokenList = tokenDao.findByUserLogin(user.getUsername());
		
		if(tokenList == null || (tokenList != null && tokenList.size() < 10)) {
			allowtoken = true;
			logger.debug(tokenList.size());
		} else {
			logger.debug("exceeded remember me tokens for account " + user.getUsername());
			cleanUserRememberMeToken(tokenList);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return allowtoken;
    }
    
	public void cleanUserRememberMeToken(List<TokenDomainObject> tokenList) {
		logger.debug(AppConstant.METHOD_IN);

		for (TokenDomainObject token : tokenList) {
			if (token.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
					.plusDays(AppConstant.TOKEN_VALIDITY_DAYS).isBefore(LocalDateTime.now())) {
				tokenDao.deleteById(token.get_id().toString());
			}
		}

		logger.debug(AppConstant.METHOD_OUT);
	}

}
