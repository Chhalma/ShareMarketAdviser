package com.city.cw.stockadvisor.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.city.cw.stockadvisor.model.User;
import com.city.cw.stockadvisor.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private  UserRepository usrRepository;
	
	public void saveUser(User user) {
		System.out.println("Saving user :" + user);
		Optional<User> optUser = usrRepository.findByUsername(user.getUsername());
		
		if(optUser.isEmpty()) {
			usrRepository.save(user);
		}else {
			System.out.println("User " + user.getUsername() + " already exists in db");
		}
	}

	public Optional<User> findByUsername(String uName) {
		// TODO Auto-generated method stub
		return usrRepository.findByUsername(uName);
	}

}
