package com.city.cw.stockadvisor.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AckDeadLineManagementController {
	private final PubSubAdminController pubSubAdminController;


	public AckDeadLineManagementController(PubSubAdminController pubSubAdminController) {
	    this.pubSubAdminController = pubSubAdminController;
	}


	@GetMapping("/manage-ackDeadline")
	public String showAckDeadLineForm(Model model) {
		
		int defaultAckDeadline = pubSubAdminController.getDefaultAckDeadline();
		
		model.addAttribute("defaultAckDeadLine", defaultAckDeadline);


	    return "manage-ackDeadline";
		}



	@PostMapping("/set-default-ackDeadLine")
	public String setDefaultAckDeadLine(@RequestParam("newAckDeadLine") String ackDeadLine, Model model) {
		
		int newAckDeadLine = Integer.parseInt(ackDeadLine);
		pubSubAdminController.setDefaultAckDeadline(newAckDeadLine);
	    
		model.addAttribute("subscriptions", newAckDeadLine);

	    return "redirect:/manage-ackDeadline";
		}

	}