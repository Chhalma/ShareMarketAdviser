package com.city.cw.stockadvisor.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.city.cw.stockadvisor.model.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long>{
	
	Subscription findByName(String name);
	

	List<Subscription> findAllByNameIn(Set<String> subscriptions);

}
