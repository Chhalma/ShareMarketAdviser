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
@Table(name ="SUBSCRIPTION", schema = "STOCK_ADVISOR")
public class Subscription {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column( name = "NAME", unique = true)
	private String name;

	public Object getId() {
		// TODO Auto-generated method stub
		return id;
	}

}
