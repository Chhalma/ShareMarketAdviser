package com.city.cw.stockadvisor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "USER_SUBSCRIPTION", schema = "STOCK_ADVISOR")
public class UserSubscription {
	
	@Id
	@Column(name = "USER_SUBID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userSubID;
	
	@Column( name = "SUB_ID")
	private Long subID;
	
	@Column(name = "USER_ID")
	private Long userID;

	public void setUserID(Long uID) {
		// TODO Auto-generated method stub
		this.userID = uID;
		
	}


	public void setSubID(Object id) {
		// TODO Auto-generated method stub
		this.subID = (Long) id;
		
	}
	
	

}
