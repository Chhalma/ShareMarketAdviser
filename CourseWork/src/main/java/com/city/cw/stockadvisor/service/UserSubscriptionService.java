package com.city.cw.stockadvisor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.city.cw.stockadvisor.model.Subscription;
import com.city.cw.stockadvisor.model.User;
import com.city.cw.stockadvisor.model.UserSubscription;
import com.city.cw.stockadvisor.repository.SubscriptionRepository;
import com.city.cw.stockadvisor.repository.UserRepository;
import com.city.cw.stockadvisor.repository.UserSubscriptionRepository;

@Service
public class UserSubscriptionService {
	
	@Autowired
	UserSubscriptionRepository usrSubRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	SubscriptionRepository subscriptionRepository;
	
	
	public void saveUserSubscription(String userName, Set<String> subnames) {
		System.out.println("saveUserSubscription for user " + userName + " called with subscriptions " +  subnames);
		
        Optional<User> optionalUser = userRepository.findByUsername(userName);
        
        System.out.println("saveUserSubscription retrieved user  " + optionalUser);
        if(optionalUser.isPresent()) {
        	Long uID = optionalUser.get().getId();
        	
        	List<Subscription> subscriptions = subscriptionRepository.findAllByNameIn(subnames);
            List<UserSubscription> userSubscriptions = new ArrayList<>();

        	for(Subscription sub: subscriptions) {
        		UserSubscription userSub = new UserSubscription();
        		userSub.setUserID(uID);
        		userSub.setSubID(sub.getId());
        		userSubscriptions.add(userSub);
        	}
        	
        	System.out.println("Subscription for " + optionalUser.get().getUsername() + " are " + userSubscriptions );
        	usrSubRepository.saveAll(userSubscriptions);
        }else {
        	System.out.println("failed to retrieve user " );

        }
        
	}

}
