package com.city.cw.stockadvisor.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.city.cw.stockadvisor.gateway.OutboundChanel;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.outbound.PubSubMessageHandler;
import com.google.pubsub.v1.Topic;

@Controller
public class ProducerController {

    @Autowired
    private OutboundChanel gateway;

    @Autowired
    private PubSubTemplate pubsubTemplate;
    
    private final PubSubAdminController pubSubAdminController;
    
    private  String topicName = "stock_price_1";


    public ProducerController(PubSubAdminController pubSubAdminController) {
        this.pubSubAdminController = pubSubAdminController;
    }

    
    @GetMapping("/publish-message")
    public String showPublishMessageForm(Model model,RedirectAttributes redirectAttributes) throws IOException {
    	List<Topic> topics = pubSubAdminController.listTopics();
        model.addAttribute("topics", topics);
        redirectAttributes.addFlashAttribute("publishSuccess", true);
        
    	return "publish-message";
    }

    @PostMapping("/publishMessage")
    public String publishMessage(@RequestParam String topicName, 
    		@RequestParam String stockMsg
    		){
    	this.topicName = topicName;
    	gateway.sendMsgToPubSub(stockMsg);
    	
        
	    return "redirect:/publish-message";
    }

    @Bean
    @ServiceActivator(inputChannel = "outboundMsgChannel")
    public PubSubMessageHandler messageSender() {
        return new PubSubMessageHandler(pubsubTemplate, topicName); // default topic
    }
}
