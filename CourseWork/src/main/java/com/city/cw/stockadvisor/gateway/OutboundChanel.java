package com.city.cw.stockadvisor.gateway;

import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway(name = "pubSubGateway", defaultRequestChannel = "outboundMsgChannel")
public interface OutboundChanel {

    void sendMsgToPubSub( String msg);
}
