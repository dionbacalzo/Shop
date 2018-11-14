package com.shop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.shop.security.RestAuthenticationEntryPoint;
import com.shop.security.ShopAccessDeniedHandler;
import com.shop.security.ShopAuthenticationSuccessHandler;
import com.shop.service.LoginManagerImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"com.shop.security"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private ShopAccessDeniedHandler accessDeniedHandler;
    
    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    
    @Autowired
    private ShopAuthenticationSuccessHandler mySuccessHandler;
    
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth.authenticationProvider(new LoginManagerImpl());
    }
    
    @Override
    public void configure(WebSecurity web) throws Exception {
      web
        .ignoring()
           .antMatchers("/resources/**");
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable()
        .authorizeRequests()
        .and()
        .exceptionHandling()
	        .accessDeniedHandler(accessDeniedHandler)
	        .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
        .authorizeRequests()
	        .antMatchers("/content").permitAll()
	        .antMatchers("/login").permitAll()
	        .antMatchers("/upload").authenticated()
	        .antMatchers("/save").authenticated()
	        .antMatchers("/delete").authenticated()
	        .antMatchers("/uploadItems").authenticated()
        .and()
        .formLogin()
        	.successHandler(mySuccessHandler)
        .and()
        .httpBasic()
        .and()
        .logout()
	        .logoutUrl("/logout")
	        .invalidateHttpSession(true)
	        .deleteCookies("JSESSIONID");
    }
}