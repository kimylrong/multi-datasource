/*
 * SubscribeListener.java 2015年12月17日
 */
package com.comstar.mars.protocol.websocket;

/**
 * WebSocket订阅监听器
 *
 * @author Li Rong
 */
public interface SubscribeListener {
	/**
	 * 收到消息调用
	 * 
	 * @param message
	 *            消息
	 */
	void onMessage(String message);
}
