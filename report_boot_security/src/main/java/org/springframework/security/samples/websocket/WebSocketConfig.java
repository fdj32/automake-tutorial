package org.springframework.security.samples.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
    	System.out.println("enableSimpleBroker /topic");
    	config.enableSimpleBroker("/topic");
    	System.out.println("setApplicationDestinationPrefixes /app");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	System.out.println("registerStompEndpoints /gs-guide-websocket");
        registry.addEndpoint("/gs-guide-websocket").withSockJS();
    }

	@Override
	protected void configureInbound(
			MessageSecurityMetadataSourceRegistry messages) {
//		super.configureInbound(messages);
		System.out.println("configureInbound anyMessage permitAll");
		messages.anyMessage().permitAll();
	}

}