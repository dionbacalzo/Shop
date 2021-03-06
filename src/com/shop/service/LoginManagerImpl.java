package com.shop.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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

	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	private UserDao userDaoImpl;
	
	@Autowired
	private TokenDao tokenDao;
	
	@Autowired
    private UserDetailsService userDetailsService;
	
	private static String[] ROLE = {
			"USER", 
			"ADMIN", 
	}; 
	
	/**
	 * validate user content
	 * @param user
	 * @return
	 */
	private boolean validateInput(User user){
		boolean valid = true;
		if(user.getUsername() == null || user.getUsername().trim().isEmpty()){
			valid = false;
		}
		if(user.getPassword() == null || user.getPassword().trim().isEmpty()){
			valid = false;
		}
		
		return valid;
	}
	
	/**
	 * validate user content when signing up
	 * @param user
	 * @return
	 */
	private boolean validateSignUpInput(User user){
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
	
	/**
	 * login a user
	 * @param user
	 * @return result: an object containing a status and message of the login result 
	 */
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
	 * @return result: an object containing a status and message when storing user to the session
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
	
	/**
	 * Check if the login attempt is valid
	 * @param Authentication
	 * @return Authentication 
	 */
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
    
    /**
     * Attempt to sign up a given user
     * @param user
	 * @return result: an object containing a status and message when signing up
     */
    @Override
	public Result signup(User user) {
		logger.debug(AppConstant.METHOD_IN);
		
		Result result = new Result(AppConstant.SHOP_SIGNUP_SUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_SUCCESSFUL_MESSAGE);
		UserDomainObject availableUser = null;
		if(validateSignUpInput(user)){
			try {
				availableUser = userDaoImpl.insert(UserDomainObjectAdapter.parseUser(user));
				
				if(availableUser == null){
					logger.error(AppConstant.SHOP_SIGNUP_STORE_UNSUCCESSFUL);
					result = new Result(AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_MESSAGE_GENERIC);
				} else {
					logger.debug(MessageFormat.format(AppConstant.SHOP_SIGNUP_STORE_SUCCESSFUL, availableUser.getUserName()));
				}
				
			} catch (DuplicateKeyException e) {
				logger.debug(AppConstant.SHOP_SIGNUP_STORE_UNSUCCESSFUL);
				result = new Result(AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_DUPLICATE_MESSAGE);
			} catch (Exception e) {
				logger.error(AppConstant.SHOP_SIGNUP_STORE_UNSUCCESSFUL);
				result = new Result(AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_MESSAGE_GENERIC);
			}
			
		} else {
			logger.error(AppConstant.SHOP_SIGNUP_INVALID_CREDENTIALS);
			result = new Result(AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_STATUS, AppConstant.SHOP_SIGNUP_UNSUCCESSFUL_MESSAGE_GENERIC);
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return result;
	}
    
    /**
     * Check if a user session is allowed to be remembered
     */
    @Override
	public boolean allowUserRememberMeToken(User user) {
    	boolean allowtoken = false;
		logger.debug(AppConstant.METHOD_IN);
		
		try {
			List<TokenDomainObject> tokenList = tokenDao.findByUserLogin(user.getUsername());
			
			if(tokenList == null || (tokenList != null && tokenList.size() < AppConstant.TOKEN_MAX)) {
				allowtoken = true;
			} else {			
				if(cleanRememberMeTokens(user, tokenList)) {
					allowtoken = true;				
				} else {
					logger.error(AppConstant.TOKEN_CREATE_ERROR_MESSAGE);
				}
			}
		} catch (Exception e) {			
			logger.error(MessageFormat.format(AppConstant.TOKEN_RETRIEVE_ERROR_MESSAGE, user.getUsername()));
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		return allowtoken;
    }
	
	/**
     * Remove a token of a user when it exceeds the maximum total allowed
     * @param User, tokenList
     */
	public boolean cleanRememberMeTokens(User user, List<TokenDomainObject> tokenList) {
		logger.debug(AppConstant.METHOD_IN);
		
		boolean hasDeleted = false;
		try {
			if(tokenList != null && !tokenList.isEmpty()) {				

				// Remove all tokens from List and DB that have exceeded the days they are only valid
				tokenList.removeIf(token -> 
					{
						boolean exceed = token.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
								.plusDays(AppConstant.TOKEN_VALIDITY_DAYS).isBefore(LocalDateTime.now());
						if (exceed) {
							logger.debug(MessageFormat.format(AppConstant.TOKEN_DELETE_MESSAGE, tokenList.get(0).get_id(), user.getUsername()));
							tokenDao.deleteById(token.get_id().toString());
						}
						return exceed;
					}
				);
				
				// If the user account still exceeds the maximum number of tokens then delete the oldest token
				if (tokenList.size() >= AppConstant.TOKEN_MAX) {
					// sort dates from oldest to recent
					tokenList.stream()
				     .sorted(Comparator.comparing(TokenDomainObject::getDate, Comparator.nullsLast(Comparator.reverseOrder())));
					
					logger.debug(MessageFormat.format(AppConstant.TOKEN_DELETE_MESSAGE, tokenList.get(0).get_id(), user.getUsername()));
					tokenDao.deleteById(tokenList.get(0).get_id().toString());					
				}
				hasDeleted = true;
			}
		} catch (Exception e) {
			hasDeleted = false;
			logger.error(MessageFormat.format(AppConstant.TOKEN_DELETE_ERROR_MESSAGE, user.getUsername()));
		}
		
		logger.debug(AppConstant.METHOD_OUT);
		
		return hasDeleted;
	}

}
