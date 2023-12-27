package com.city.cw.stockadvisor.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.city.cw.stockadvisor.model.Adviser;
import com.city.cw.stockadvisor.repository.AdviserRepository;

@Service
public class AdviserDetailsService implements UserDetailsService {

	private final AdviserRepository adviserRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AdviserDetailsService(AdviserRepository adviserRepository, BCryptPasswordEncoder passwordEncoder) {
        this.adviserRepository = adviserRepository;
        this.passwordEncoder = passwordEncoder;
    }
	    public void registerAdviser(Adviser adviser) {
	        System.out.println("Saving Advisor: " + adviser);
	        Adviser existingAdvisor = adviserRepository.findByEmail(adviser.getEmail());

	        if (existingAdvisor == null) {
	        	adviser.setPassword(passwordEncoder.encode(adviser.getPassword()));
	            adviserRepository.save(adviser);
	        } else {
	            System.out.println("Advisor " + adviser.getEmail() + " already exists in the database");
	        }
	    }

	    public Adviser findByEmail(String email) {
	        return adviserRepository.findByEmail(email);
	    }
	    
	    @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
	        Adviser adviser = adviserRepository.findByEmail(email);

	        if (adviser == null) {
	        	System.out.println("Not ofund in AdvisorDetailService");
	            throw new UsernameNotFoundException("User not found with username: " + email);
	        }
	        System.out.println("Found user: " + adviser.getEmail());
	        UserDetails userDetails = User.withUsername(adviser.getEmail())
	                .password(adviser.getPassword())
	                .roles(adviser.getRole().name()) // Assuming role names are the same as enum values
	                .build();
	        return userDetails;
	        
	    }
}