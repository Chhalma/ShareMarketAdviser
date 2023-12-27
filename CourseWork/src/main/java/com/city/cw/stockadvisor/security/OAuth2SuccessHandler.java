package com.city.cw.stockadvisor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.city.cw.stockadvisor.model.RoleType;
import com.city.cw.stockadvisor.model.User;
import com.city.cw.stockadvisor.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;

import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
    private  UserService userService;

    
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    	System.out.println("In Success");
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            
            // Retrieve user information from OAuth2User
            String email = oAuth2User.getAttribute("email");
            String username = oAuth2User.getAttribute("name");
            
            // Create User object and save it using UserService
            User user1 = new User();
            user1.setEmail(email);
            user1.setUsername(username);
            user1.setRole(RoleType.USER);
            userService.saveUser(user1);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
