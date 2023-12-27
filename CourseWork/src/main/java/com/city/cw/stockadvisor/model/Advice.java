package com.city.cw.stockadvisor.model;

import java.util.List;

import lombok.Data;

@Data
public class Advice {
	
	private String shareName;
	
	private List<String> adviceList;

	public void setShareName(String share) {
		// TODO Auto-generated method stub
		this.shareName = share;
		
	}

	public void setAdviceList(List<String> desc) {
		// TODO Auto-generated method stub
		this.adviceList = desc;
		
	}

}
