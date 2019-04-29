package com.shop.config;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.shop.constant.AppConstant;

public class SessionListener implements HttpSessionListener {
	
	protected final Logger logger = LogManager.getLogger(getClass());
	
	@Override
    public void sessionCreated(HttpSessionEvent event) {
		logger.debug("Session created");
        event.getSession().setMaxInactiveInterval(AppConstant.SESSION_TIMEOUT);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
    	logger.debug("Session destroyed");
    }
}
