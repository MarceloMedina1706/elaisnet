package com.elaisnet.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
	      .authorizeRequests()
	      .antMatchers(resources).permitAll()
	      .antMatchers("/", "/index*", "/login*", "/registrarte**", "/verificarEmail", 
	    		  "/confirmarRegistro*", "/reenviarToken*", "/recuperarCuenta",
	    		  "/resetPassword", "/changePassword*", "/savePassword", "/badResetPassword").permitAll()
	      .anyRequest().authenticated()
	      .and()
	    .formLogin()
		      .loginPage("/index")
		      .usernameParameter("email")
	          .passwordParameter("password")
	          .failureHandler(authenticationFailureHandler)
	          .successHandler(authenticationSuccessHandler())
	          .permitAll()
        .and()
        .logout()
	        .logoutSuccessHandler(logoutSuccessHandler())
	        .invalidateHttpSession(false)
	        .logoutSuccessUrl("/index")
	        .deleteCookies("JSESSIONID")
	        .permitAll()
	    .and()
	    .rememberMe().tokenValiditySeconds(60 * 5).key("secret").rememberMeParameter("checkRememberMe");
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new MyCustomLoginAuthenticationSuccessHandler();
    }
	
	@Bean
	LogoutSuccessHandler logoutSuccessHandler(){
        return new MyCustomLogoutSuccessHandler();
    }
	
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	
	
	private String[] resources = new String[]{
            "/include/**","/css/**","/icons/**","/img/**","/js/**","/layer/**"};
}
