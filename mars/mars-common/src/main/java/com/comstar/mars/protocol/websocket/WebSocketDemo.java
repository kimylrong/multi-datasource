/*
 * WebSocketDemo.java 2015年12月17日
 */
package com.comstar.mars.protocol.websocket;

/**
 * 
 *
 * @author Li Rong
 */
public class WebSocketDemo {

	public static void main(String[] args) throws Exception {
		WebSocketConection conection = new WebSocketConection("wss://localhost:8443/websocket/endpoint", "admin",
				"admin");
		WebSocketConection.setSslCertificate("../keystore", "123456");
		conection.setup();

		conection.subscribe("/topic/19/hello", new SubscribeListener() {
			@Override
			public void onMessage(String message) {
				System.out.println(message);
			}
		});

		Thread.sleep(600000);
	}

}
