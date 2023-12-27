package com.city.cw.stockadvisor.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.city.cw.stockadvisor.model.RoleType;
import com.city.cw.stockadvisor.model.User;
import com.city.cw.stockadvisor.model.Adviser;
import com.city.cw.stockadvisor.service.AdviserDetailsService;
import com.city.cw.stockadvisor.service.SubscriptionService;
import com.city.cw.stockadvisor.service.UserService;
import com.google.pubsub.v1.Subscription;

@Controller
public class LoginController {

	@Autowired
    private  UserService userService;

	
	@Autowired
	private SubscriptionService subService;
	
	 @Autowired
	    private AdviserDetailsService adviserDetailsService;
	 
	 private final PubSubAdminController pubSubAdminController;


	 public LoginController(PubSubAdminController pubSubAdminController) {
	     this.pubSubAdminController = pubSubAdminController;
	 }

	
	
	@GetMapping("/")
	public String userLogin(@AuthenticationPrincipal OAuth2User oauth2User, Model model) {
		if(oauth2User != null) {
			Map<String,Object> userData = oauth2User.getAttributes();
			System.out.println(userData.get("name"));
			System.out.println(userData.get("email"));
			String uName = userData.get("name").toString();
			
            
         // Check if the user already exists in the database
            Optional<User> existingUser = userService.findByUsername(uName);
            
            if (existingUser.isEmpty()) {
                // User doesn't exist, create a new User entity and save it to the database
            	User user1 = new User();
                user1.setEmail(userData.get("email").toString());
                user1.setUsername(uName);
                user1.setRole(RoleType.USER);
                System.out.println("userLogin() Setting Role " + RoleType.USER);
                System.out.println("userLogin() Creating User " + user1.toString());
                userService.saveUser(user1);
            } else {
            	System.out.println("userLogin() User " + uName + "already exists.");
             }
			
			model.addAttribute("name", userData.get("name"));
			
			 List<Subscription> subscriptions = pubSubAdminController.listSubscriptions();
			 model.addAttribute("shareOptions", subscriptions);

			//model.addAttribute("shareOptions", subService.getAllSubscriptions());
			return "user-home";
		}else {
			return "home";
		}
		
		
		// Retrieve user information from OAuth2User
		
	}
	
	@GetMapping("/adviser-reg")
    public String showRegistrationForm() {
        return "adviser-reg";
    }
	
	@PostMapping("/adviserReg")
	public String processRegistration(Adviser adviser, RedirectAttributes redirectAttributes) {
	    System.out.println("inside processReg");
	    adviserDetailsService.registerAdviser(adviser);
	    redirectAttributes.addFlashAttribute("registrationSuccess", true);
	    return "redirect:/adviser-login"; // Redirect to the login page after successful registration
	}



	
	@GetMapping("/adviser-login")
	public String showAdviserLogin() {
		
	    return "adviser-login";
	}
	
	
	@PostMapping("/adviserHome")
	public String precessAdviserLogin() {
		 System.out.println("inside processAdviserLogin");
		   
		return "adviser-home";
	           
	}
	@GetMapping("/adviser-home")
	public String ShowAdviserHome() {
		return "adviser-home";
	}

}

