package com.city.cw.stockadvisor.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.city.cw.stockadvisor.model.User; 

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

	Optional<User> findByUsername(String uName);

}
