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


@Entity
@Data
@Table(name = "ADVISER" , schema = "STOCK_ADVISOR")
public class Adviser {
	
	@Id
	@Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "NAME", unique = false)
	private String username;
	
	@Column(name = "PASSWORD", unique = false)
	private String password;
	
	@Column(name = "EMAIL", unique = true)
	private String email;
	
	@Enumerated(EnumType.STRING)
	@Column(name="role")
    private RoleType role = RoleType.ADVISER;

	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}
	 public void setUsername(String username) {
	        this.username = username;
	    }
	 public String getPassword() {
			// TODO Auto-generated method stub
			return password;
			
		}
	public void setPassword(String password) {
		// TODO Auto-generated method stub
		this.password=password;
		
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public RoleType getRole() {
		// TODO Auto-generated method stub
		return role;
		
	}
	public void setRole(RoleType advisor) {
		// TODO Auto-generated method stub
		this.role = advisor;
	}
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	


}
