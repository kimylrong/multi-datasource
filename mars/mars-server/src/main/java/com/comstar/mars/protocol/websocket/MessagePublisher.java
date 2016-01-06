/*
 * MessagePublisher.java 2015年8月25日
 */
package com.comstar.mars.protocol.websocket;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息发布
 *
 * @author Li Rong
 */
@Service
public class MessagePublisher implements ApplicationContextAware {
	private static ApplicationContext context;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

	public static MessagePublisher getInstance() {
		return context.getBean(MessagePublisher.class);
	}

	public SimpMessagingTemplate getMessagingTemplate() {
		return messagingTemplate;
	}
}
