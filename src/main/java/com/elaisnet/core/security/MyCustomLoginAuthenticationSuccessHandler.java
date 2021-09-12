package com.elaisnet.core.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;



public class MyCustomLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		//addWelcomeCookie(getUserName(authentication), response);
		
        redirectStrategy.sendRedirect(request, response, "/homepage");

        final HttpSession session = request.getSession(false);
        
        if (session != null) {
        	
            session.setMaxInactiveInterval(15 * 60);
            //String username;
            /*if (authentication.getPrincipal() instanceof User) {
            	username = ((User)authentication.getPrincipal()).getEmail();
            }
            else {
            	username = authentication.getName();
            }*/
            String username = getUserName(authentication);
            
            session.setAttribute("user", username);
        }
        clearAuthenticationAttributes(request);
		
	}
	
	private String getUserName(final Authentication authentication) {
		
		/*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		
		User user = userService.getUserByEmail(userDetail.getUsername());
		
        return user.getName() + " " + user.getLastName();*/
		
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    /*private void addWelcomeCookie(final String user, final HttpServletResponse response) {
        Cookie welcomeCookie = getWelcomeCookie(user);
        response.addCookie(welcomeCookie);
    }

    private Cookie getWelcomeCookie(String user) {
    	user = user.replace(" ","");
        Cookie welcomeCookie = new Cookie("welcome", user);
        welcomeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        return welcomeCookie;
    }*/

    protected void clearAuthenticationAttributes(final HttpServletRequest request) {
        final HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
    
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    

}
