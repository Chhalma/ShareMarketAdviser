package com.city.cw.stockadvisor.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.pubsub.v1.Subscription;
import com.google.pubsub.v1.Topic;


@Controller
public class SubscriberManagementController {

private final PubSubAdminController pubSubAdminController;


public SubscriberManagementController(PubSubAdminController pubSubAdminController) {
    this.pubSubAdminController = pubSubAdminController;
}


@GetMapping("/manage-subscribers")
public String manageSubscriber(Model model) throws IOException {
	
	List<Topic> topics = pubSubAdminController.listTopics();
    model.addAttribute("topics", topics);

    List<Subscription> subscriptions = pubSubAdminController.listSubscriptions();
    model.addAttribute("subscriptions", subscriptions);

    return "manage-subscribers";
	}

@PostMapping("/create-subscriber")
public String createSubscriber(@RequestParam("subscriberName") String subscriberName,
		@RequestParam("topicName") String topicName ,Model model) {
	pubSubAdminController.createSubscription(subscriberName, topicName, null, null);
	List<Subscription> subscriptions = pubSubAdminController.listSubscriptions();
    model.addAttribute("subscriptions", subscriptions);

    return "redirect:/manage-subscribers";
	}

@PostMapping("/delete-subscriber")
public String deleteSubscriber(@RequestParam("subscriberName") String subscriberName, Model model) {
	pubSubAdminController.deleteSubscription(subscriberName);
    
	List<Subscription> subscriptions = pubSubAdminController.listSubscriptions();
    model.addAttribute("subscriptions", subscriptions);

    return "redirect:/manage-subscribers";
	}

}