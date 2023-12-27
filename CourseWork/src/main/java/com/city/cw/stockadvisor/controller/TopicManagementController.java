package com.city.cw.stockadvisor.controller;


import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.pubsub.v1.Topic;

@Controller
public class TopicManagementController {


private final PubSubAdminController pubSubAdminController;


public TopicManagementController(PubSubAdminController pubSubAdminController) {
    this.pubSubAdminController = pubSubAdminController;
}

@GetMapping("/view-stock")
public String viewStock(Model model) throws IOException {
    List<Topic> topics = pubSubAdminController.listTopics();
    model.addAttribute("topics", topics);

    return "view-stock";
	}

@GetMapping("/manage-stock")
public String manageStock(Model model) throws IOException {
    List<Topic> topics = pubSubAdminController.listTopics();
    model.addAttribute("topics", topics);

    return "manage-stock";
	}

@PostMapping("/create-stock")
public String createStock(@RequestParam("topicName") String topicName, Model model) throws IOException {
	pubSubAdminController.createTopic(topicName);
    List<Topic> topics = pubSubAdminController.listTopics();
    model.addAttribute("topics", topics);

    return "manage-stock";
	}

@PostMapping("/delete-stock")
public String deleteStock(@RequestParam("topicName") String topicName, Model model) throws IOException {
	pubSubAdminController.deleteTopic(topicName);
    List<Topic> topics = pubSubAdminController.listTopics();
    model.addAttribute("topics", topics);

    return "manage-stock";
	}

}