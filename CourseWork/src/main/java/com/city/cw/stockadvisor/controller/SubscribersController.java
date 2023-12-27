package com.city.cw.stockadvisor.controller;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.integration.AckMode;
import com.google.cloud.spring.pubsub.integration.inbound.PubSubInboundChannelAdapter;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.cloud.spring.pubsub.support.GcpPubSubHeaders;

@Controller

public class SubscribersController {

	private String subName = "stock_price_1";
	private String message;
	
	@GetMapping("getMessage")
	public String getMessage(String subName) {
		this.subName = subName;
		System.out.println(this.subName+"  "+message);
		return message;
	}
	
	// Create a message channel for messages arriving from the subscription `user1`.
			@Bean
			public MessageChannel inputMessageChannel() {
			  return new PublishSubscribeChannel();
			}

	
		@Bean
		public PubSubInboundChannelAdapter inboundChannelAdapter(
			    @Qualifier("inputMessageChannel") MessageChannel messageChannel,
			    PubSubTemplate pubSubTemplate) {
			//PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, "user1");
			PubSubInboundChannelAdapter adapter = new PubSubInboundChannelAdapter(pubSubTemplate, subName);
			adapter.setOutputChannel(messageChannel);
			adapter.setAckMode(AckMode.MANUAL);
			  adapter.setPayloadType(String.class);
			  return adapter;
		}


	// Define what happens to the messages arriving in the message channel.
	@ServiceActivator(inputChannel = "inputMessageChannel")
	public void messageReceiver(
	    String payload,
	    @Header(GcpPubSubHeaders.ORIGINAL_MESSAGE) BasicAcknowledgeablePubsubMessage message) {
		System.out.println("Message arrived via an inbound channel adapter! Payload: " + payload);
		this.message=payload;
		//message.ack();
	  
	}
	
}