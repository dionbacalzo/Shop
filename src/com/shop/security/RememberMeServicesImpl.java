package com.shop.security;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.stereotype.Service;

import com.lambdaworks.crypto.SCryptUtil;
import com.shop.constant.AppConstant;
import com.shop.dao.TokenDao;
import com.shop.domain.TokenDomainObject;

@Service
public class RememberMeServicesImpl extends AbstractRememberMeServices  {
	
	private final Logger logger = LogManager.getLogger(getClass());
	

    private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * AppConstant.TOKEN_VALIDITY_DAYS;

	private static final int DEFAULT_TOKEN_LENGTH = 16;
	
	private SecureRandom random;
	
	@Autowired
	private TokenDao tokenDao;
	
	@Autowired
	private RememberMeServicesImpl(Environment env, UserDetailsService userDetailsService) {
		super(AppConstant.REMEMBER_ME_KEY, userDetailsService);
        super.setParameter(AppConstant.REMEMBER_ME_KEY);
        random = new SecureRandom();
	}

	@Override
	protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication successfulAuthentication) {
		String user = successfulAuthentication.getName();

		logger.debug("Creating new persistent login token for user " + user);
        TokenDomainObject token = new TokenDomainObject();
        token.setUserLogin(user);
        token.setValue(generateTokenData());
        token.setDate(new Date());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        try {
        	TokenDomainObject tokenWithId = tokenDao.save(token);
            addCookie(tokenWithId, request, response);
        } catch (Exception e) {
        	logger.error("Failed to save persistent token ", e);
        }
	}

	@Override
	protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request,
			HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {
		TokenDomainObject token = getPersistentToken(cookieTokens);
        String login = token.getUserLogin();

        // Token also matches, so login is valid. Update the token value, keeping the *same* series number.
        logger.debug(MessageFormat.format("Refreshing persistent login token for user {0}, series {1}", login, token.get_id()));
        token.setDate(new Date());
        token.setValue(generateTokenData());
        token.setIpAddress(request.getRemoteAddr());
        token.setUserAgent(request.getHeader("User-Agent"));
        try {
            tokenDao.save(token);
            addCookie(token, request, response);
        } catch (Exception e) {
            logger.error("Failed to update token: ", e);
            throw new RememberMeAuthenticationException("Autologin failed ", e);
        }
        return getUserDetailsService().loadUserByUsername(login);
	}
	
	 /**
     * Validate the token and return it.
     */
    private TokenDomainObject getPersistentToken(String[] cookieTokens) {
        if (cookieTokens.length != 2) {
            throw new InvalidCookieException("Cookie token did not contain " + 2 + " tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
        }

        final String presentedSeries = cookieTokens[0];
        final String presentedToken = cookieTokens[1];

        TokenDomainObject token = null;
        try {
        	Optional<TokenDomainObject> tokenData = tokenDao.findById(presentedSeries);
            if(tokenData.isPresent()){
            	token = tokenData.get();
            } else {
            	// No series match, so we can't authenticate using this cookie
                //throw new RememberMeAuthenticationException("No persistent token found for series id: " + presentedSeries);
            	logger.error("No persistent token found for series id: " + presentedSeries);
            }
        } catch (Exception e) {
            logger.error("Error to access database", e );
        }

        if(token != null){
	        // We have a match for this user/series combination
	        logger.info(MessageFormat.format("presentedToken={0} / tokenValue={1}", presentedToken, token.getValue()));
	        if (!presentedToken.equals(token.getValue())) {
	            // Token doesn't match series value. Delete this session and throw an exception.
	            tokenDao.deleteById(token.get_id().toString());
	            throw new CookieTheftException("Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.");
	        }
	        
	        if (token.getDate().toInstant().atZone(ZoneId.systemDefault())
	        	      .toLocalDateTime().plusDays(AppConstant.TOKEN_VALIDITY_DAYS).isBefore(LocalDateTime.now())) {
	        	tokenDao.deleteById(token.get_id().toString());
	            throw new RememberMeAuthenticationException("Remember-me login has expired");
	        }
        }
        return token;
    }
    
    /**
     * When logout occurs, only invalidate the current token, and not all user sessions.
     * <p/>
     * The standard Spring Security implementations are too basic: they invalidate all tokens for the
     * current user, so when he logs out from one browser, all his other sessions are destroyed.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String rememberMeCookie = extractRememberMeCookie(request);
        if (rememberMeCookie != null && rememberMeCookie.length() != 0) {
            try {
                String[] cookieTokens = decodeCookie(rememberMeCookie);
                TokenDomainObject token = getPersistentToken(cookieTokens);
                if (token != null) {
                	tokenDao.deleteById(token.get_id().toString());
                }
            } catch (InvalidCookieException iE) {
                logger.info("Invalid cookie, no persistent token could be deleted");
            } catch (RememberMeAuthenticationException rE) {
                logger.debug("No persistent token found, so no token could be deleted");
            }
        }
        super.logout(request, response, authentication);
    }
	
	private String generateTokenData() {
        byte[] newToken = new byte[DEFAULT_TOKEN_LENGTH];
        random.nextBytes(newToken);
        return new String(SCryptUtil.scrypt(new String (newToken), 16, 16, 16));
    }
	
	private void addCookie(TokenDomainObject token, HttpServletRequest request, HttpServletResponse response) {
        setCookie(new String[]{token.get_id().toString(), token.getValue()}, TOKEN_VALIDITY_SECONDS, request, response);
    }

}
