/*
 * CustomHandshake.java 2015年8月24日
 */
package com.comstar.mars.protocol.websocket;

import java.security.Principal;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.comstar.mars.service.system.ShiroRealm.ShiroUser;

/**
 * WebSocket握手时，从Shiro获取用户名
 *
 * @author Li Rong
 */
public class CustomHandshake extends DefaultHandshakeHandler {
	@Override
	protected Principal determineUser(ServerHttpRequest request,
			WebSocketHandler wsHandler, Map<String, Object> attributes) {
		Subject user = SecurityUtils.getSubject();
		ShiroUser shiroUser = (ShiroUser) user.getPrincipal();
		return shiroUser;
	}
}
