/*
 * WebSocketAuto.java 2015年12月23日
 */
package com.comstar.mars.protocol.websocket;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 *
 * @author Li Rong
 */
@Component
public class WebSocketAuto {
	@Scheduled(fixedDelay = 10000)
	public void sendHello2Client() {
		MessagePublisher.getInstance().getMessagingTemplate().convertAndSend("/topic/19/hello", "Hello World");
	}
}
