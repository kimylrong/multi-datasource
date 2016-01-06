/*
 * RestAccessor.java 2015年7月22日
 */
package com.comstar.mars.protocol.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * REST服务访问器
 *
 * @author Li Rong
 */
public final class RestAccessor {
	private static String userName;
	private static String password;

	private static String rootPath;
	@SuppressWarnings("unused")
	private static String authInfo;

	private static List<Object> providers;

	static {
		providers = new ArrayList<Object>();
		providers.add(new JacksonJaxbJsonProvider());
	}

	public static void setup(String root, String userName, String password) {
		if (StringUtils.isEmpty(root) || StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
			throw new IllegalArgumentException("参数不能为空");
		}

		RestAccessor.userName = userName;
		RestAccessor.password = password;

		rootPath = root;
		authInfo = getBasicAuthorizationInfo(userName, password);
	}

	public static void setSslCertificate(String certificatePath, String certificatePassword) {
		System.setProperty("javax.net.debug", "ssl,handshake");
		System.setProperty("javax.net.ssl.keyStore", certificatePath);
		System.setProperty("javax.net.ssl.keyStorePassword", certificatePassword);
		System.setProperty("javax.net.ssl.trustStore", certificatePath);
		System.setProperty("javax.net.ssl.trustStorePassword", certificatePassword);
	}

	public static <T> T buildResource(Class<T> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("相对路径不能为空");
		}

		WebClient client = WebClient.create(rootPath, providers, userName, password, null);
		TLSClientParameters clientParameters = new TLSClientParameters();
		clientParameters.setDisableCNCheck(true);

		WebClient.getConfig(client).getHttpConduit().setTlsClientParameters(clientParameters);

		return (T) JAXRSClientFactory.fromClient(client, clazz);
	}

	private static String getBasicAuthorizationInfo(String userName, String password) {
		String authString = userName + ":" + password;
		String base64Encoded = Base64.encodeBase64String(authString.getBytes());
		return "Basic " + base64Encoded;
	}
}
