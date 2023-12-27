package com.city.cw.stockadvisor.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.city.cw.stockadvisor.model.Advice;

@Service
public class AdviceService {
	
	public Set<Advice> getAdvices(Set<String> shares) {
		System.out.println("In getAdvices(), getting advices for " + shares);
		Set<Advice> advices = new HashSet<>();
		shares.forEach( share -> {
			Advice ad = new Advice();
			ad.setShareName(share);
			List<String> desc = new ArrayList<>();
			desc.add(share + " : Strong quarterly earnings, promising growth potential. Consider buying for mid-term investment.\"\n"
					+ "\n"
					+ "This advice provides a brief overview of the company's positive quarterly earnings and suggests considering it for a mid-term investment due to its potential growth.");
			ad.setAdviceList(desc);
			advices.add(ad);
		});
		
		return advices;
	}

}
