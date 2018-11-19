package com.shop.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;  
import javax.servlet.ServletException;  
import javax.servlet.ServletRegistration.Dynamic;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;  
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;  
import org.springframework.web.servlet.DispatcherServlet;

import com.shop.util.PropertyUtil;  
public class WebAppInitializer implements WebApplicationInitializer {
	
	private String TMP_FOLDER = PropertyUtil.getProperty("upload.temp.path"); 
	private int MAX_UPLOAD_SIZE = Integer.parseInt(PropertyUtil.getProperty("max.upload.size"));
	
	public void onStartup(ServletContext servletContext) throws ServletException {  
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();  
        ctx.register(AppConfig.class);
        servletContext.addListener(new SessionListener());
        ctx.setServletContext(servletContext);    
        Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        dynamic.addMapping("/");  
        dynamic.setLoadOnStartup(1);
        
        //file upload configuration
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(TMP_FOLDER, 
                MAX_UPLOAD_SIZE, MAX_UPLOAD_SIZE * 2, MAX_UPLOAD_SIZE / 2);
               
        dynamic.setMultipartConfig(multipartConfigElement);
        
        //set logging level for mongoDB
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);
   }  
} 
