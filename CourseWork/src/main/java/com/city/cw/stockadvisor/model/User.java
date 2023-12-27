package com.city.cw.stockadvisor.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;
import lombok.Setter;
import lombok.Getter;

@Entity
@Data
@Table(name = "USER" , schema = "STOCK_ADVISOR")
public class User {

	@Id
	@Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Setter
	@Getter
	@Column(name = "NAME", unique = true)
	private String username;
	@Setter
	@Getter
	@Column(name = "EMAIL", unique = true)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(name="role")
    private RoleType role;

	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	 public void setUsername(String username) {
	        this.username = username;
	    }
	public void setEmail(String email2) {
		// TODO Auto-generated method stub
		this.email=email2;
		
	}
	public void setRole(RoleType user) {
		// TODO Auto-generated method stub
		this.role = user;
	}
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}


}
