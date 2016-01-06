/*
 * CustomerTextHandler.java 2015年8月19日
 */
package com.comstar.mars.protocol.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 
 *
 * @author Li Rong
 */
public class CustomerTextHandler extends TextWebSocketHandler {
	private static final Logger LOG = LoggerFactory
			.getLogger(CustomerTextHandler.class);

	@Override
	public void afterConnectionClosed(WebSocketSession session,
			CloseStatus status) throws Exception {
		super.afterConnectionClosed(session, status);
		LOG.info("connection closed");
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session)
			throws Exception {
		super.afterConnectionEstablished(session);
		LOG.info("connection established");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		TextMessage returnMessage = new TextMessage(message.getPayload()
				+ " received at server");
		session.sendMessage(returnMessage);
		LOG.info("handle message");
	}

}
