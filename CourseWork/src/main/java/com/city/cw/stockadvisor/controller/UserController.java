package com.city.cw.stockadvisor.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import com.city.cw.stockadvisor.model.Advice;
import com.city.cw.stockadvisor.service.AdviceService;
import com.city.cw.stockadvisor.service.UserSubscriptionService;
@Controller
public class UserController {
	
	@Autowired
	UserSubscriptionService userSubService;
	
	@Autowired
	AdviceService adviceService;
	
	//private Set<String> selectedList = new HashSet<>();
	
	private Set<Map<String, String>> shareMsgPairs = new HashSet<>();

	private SubscribersController subscriberController;
	
	 public UserController(SubscribersController subscriberController) {
	     this.subscriberController = subscriberController;
	 }

	
	@PostMapping("/submitShares")
    public String submitShares(@RequestParam(value = "share", required = false) String[] selectedShares) {
        // Process the selected shares here
		System.out.println("submitShares() called " );
		if (selectedShares != null) {
            for (String share : selectedShares) {
                System.out.println("Selected share: " + share);
                // Perform actions with the selected shares, e.g., store in database, process, etc.
            }
         // Retrieve the authentication object from the security context
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                Map<String,Object> userData = oauth2User.getAttributes();
    			System.out.println(userData.get("name"));
    			System.out.println(userData.get("email"));
    			userSubService.saveUserSubscription(userData.get("name").toString(), Set.of(selectedShares));
            }
            
            // retrieve advices from pusub
            
            if (selectedShares != null) {
                for (String share : selectedShares) {
                	//String msg = subscriberController.getMessage(share);
                    System.out.println("Selected share: " + share);
                    
                    int lastSlashIndex = share.lastIndexOf("/");

                    // Extract the substring after the last "/"
                    String shareName = share.substring(lastSlashIndex + 1);

                    String msg = subscriberController.getMessage(share);
                    System.out.println("Advice: " + msg);
                    Map<String, String> shareMsgPair = new HashMap<>();
                    
                    shareMsgPair.put("share", shareName);
                    shareMsgPair.put("msg", msg);

                    // Add the Map to the set
                    shareMsgPairs.add(shareMsgPair);
                    // Perform actions with the selected share, e.g., store in database, process, etc.
                }
                // ... rest of the code ...
            }
            
            //selectedList.addAll(Set.of(selectedShares));
            
            
            //Set<Advice> advises = adviceService.getAdvices(Set.of(selectedShares));
            
            
        }
     
		RedirectAttributes attributes = new RedirectAttributesModelMap();
		attributes.addFlashAttribute("shareMsgPairs", shareMsgPairs);
		return "redirect:/share-advice";
    }
	
	@GetMapping("/share-advices")
	public String getAdvices(Model model,@ModelAttribute("shareMsgPairs") Set<Map<String, String>> shareMsgPairs) {
        System.out.println("In getAdvices() " );

		//Set<Advice> advises = adviceService.getAdvices(selectedList);
		//model.addAttribute("advices", advises);
        RedirectAttributes attributes = new RedirectAttributesModelMap();
		attributes.addFlashAttribute("advices", shareMsgPairs);
		return "share-advices";
	}

}
