/*
 * WebSocketConection.java 2015年12月17日
 */
package com.comstar.mars.protocol.websocket;

import java.lang.reflect.Type;
import java.util.Base64;

import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.client.ClientProperties;
import org.glassfish.tyrus.client.SslContextConfigurator;
import org.glassfish.tyrus.client.SslEngineConfigurator;
import org.glassfish.tyrus.client.auth.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

/**
 * WebSocket连接管理器。对应一个Server端的WebSocket Endpoint
 *
 * @author Li Rong
 */
public class WebSocketConection {
	private static Logger log = LoggerFactory.getLogger(WebSocketConection.class);

	private String endpointUrl;
	private String userName;
	private String password;

	private StompSession session;

	/**
	 * @param endpointUrl
	 *            URL of WebSocket endpoint. http, https, ws, wss supported.
	 * @param password
	 *            if web server require authentication，fill with token.
	 */
	public WebSocketConection(String endpointUrl, String userName, String password) {
		this.endpointUrl = endpointUrl;
		this.userName = userName;
		this.password = password;
	}

	public static void setSslCertificate(String certificatePath, String certificatePassword) {
		System.setProperty("javax.net.debug", "ssl,handshake");
		System.setProperty("javax.net.ssl.keyStore", certificatePath);
		System.setProperty("javax.net.ssl.keyStorePassword", certificatePassword);
		System.setProperty("javax.net.ssl.trustStore", certificatePath);
		System.setProperty("javax.net.ssl.trustStorePassword", certificatePassword);
	}

	public void setup() {
		if (StringUtils.isEmpty(endpointUrl)) {
			throw new RuntimeException("endpoint can't be empty");
		}

		// Standaed WebSocket
		final SSLContextConfigurator defaultConfig = new SSLContextConfigurator();
		defaultConfig.retrieve(System.getProperties());

		SslEngineConfigurator sslEngineConfigurator = new SslEngineConfigurator(new SslContextConfigurator());
		sslEngineConfigurator.setHostVerificationEnabled(false);

		ClientManager clientManager = ClientManager.createClient();
		clientManager.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);
		clientManager.getProperties().put(ClientProperties.CREDENTIALS, new Credentials(this.userName, this.password));
		clientManager.getProperties().put(ClientProperties.SSL_ENGINE_CONFIGURATOR, sslEngineConfigurator);

		// Stomp Protocol
		WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
		headers.set("Authorization", getBasicAuthorizationInfo(userName, password));
		StompSessionHandler handler = new SelfSessionHandler();

		WebSocketClient socketClient = new StandardWebSocketClient(clientManager);
		WebSocketStompClient stompClient = new WebSocketStompClient(socketClient);
		ListenableFuture<StompSession> future = stompClient.connect(endpointUrl, headers, handler);

		try {
			session = future.get();
		} catch (Exception e) {
			log.error("构建WebSocket连接失败", e);
			throw new RuntimeException("构建WebSocket连接失败", e);
		}
	}

	public Subscription subscribe(String topic, final SubscribeListener listener) {
		if (StringUtils.isEmpty(topic)) {
			throw new IllegalArgumentException("topic不能为空");
		}
		if (session == null || !session.isConnected()) {
			throw new IllegalArgumentException("WebSocket还没有连接");
		}

		return session.subscribe(topic, new StompFrameHandler() {
			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				String json = new String((byte[]) payload);
				log.debug("收到消息: " + json);
				listener.onMessage(json);
			}

			@Override
			public Type getPayloadType(StompHeaders headers) {
				return byte[].class;
			}
		});
	}

	class SelfSessionHandler extends StompSessionHandlerAdapter {
		@Override
		public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
			super.afterConnected(session, connectedHeaders);
			log.info("WebSocket连接成功");
		}

		@Override
		public void handleTransportError(StompSession session, Throwable exception) {
			super.handleTransportError(session, exception);
			log.info("WebSocket连接失败");
		}
	}

	private static String getBasicAuthorizationInfo(String userName, String password) {
		String authString = userName + ":" + password;
		String base64Encoded = Base64.getEncoder().encodeToString(authString.getBytes());
		return "Basic " + base64Encoded;
	}

}
