package com.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.shop.constant.AppConstant;
import com.shop.security.RestAuthenticationEntryPoint;
import com.shop.security.ShopAccessDeniedHandler;
import com.shop.security.ShopAuthenticationSuccessHandler;
import com.shop.security.ShopLogoutSuccessHandler;
import com.shop.service.LoginManagerImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMongoRepositories(basePackages = "com.shop.dao")
@ComponentScan(basePackages = {"com.shop.security"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
	public SecurityConfig() {
        super();
    }
	
    @Autowired
    private ShopAccessDeniedHandler accessDeniedHandler;
    
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    @Autowired
    private ShopAuthenticationSuccessHandler mySuccessHandler;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private RememberMeServices rememberMeServices;
    
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(new LoginManagerImpl());
      auth.userDetailsService(userDetailsService);
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
      web
        .ignoring()
           .antMatchers("/resources/**");
    }
    
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new ShopLogoutSuccessHandler();
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable()
        .cors()
        .and()
        .exceptionHandling()
	        .accessDeniedHandler(accessDeniedHandler)
	        .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
        .authorizeRequests()
	        .antMatchers("/content").permitAll()
	        .antMatchers("/login").permitAll()
	        .antMatchers("/profile").authenticated()
	        .antMatchers("/upload").authenticated()
	        .antMatchers("/save").authenticated()
	        .antMatchers("/delete").authenticated()
	        .antMatchers("/uploadItems").authenticated()
	        .antMatchers("/updateUser").authenticated()
	        .antMatchers("/updatePassword").authenticated()
	        .antMatchers("/admin").hasAuthority("ADMIN")
	        .antMatchers("/accountResetList").hasAuthority("ADMIN")
	        .antMatchers("/resetAccount").hasAuthority("ADMIN")
        .and()
        .formLogin()
        	.successHandler(mySuccessHandler)
        .and()
        .httpBasic()
        .and()
        .logout()
        	.logoutSuccessHandler(logoutSuccessHandler())
	        .logoutUrl("/logout")
	        .invalidateHttpSession(true)
	        .deleteCookies("JSESSIONID")
    	.and()
    	.rememberMe()
	        .rememberMeServices(rememberMeServices)
	        .key(AppConstant.REMEMBER_ME_KEY);
    }
}