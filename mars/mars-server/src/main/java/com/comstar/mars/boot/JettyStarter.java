/*
 * JettyStarter.java  2012-11-23
 */
package com.comstar.mars.boot;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jetty服务器启动入口
 * 
 * @author Li,Rong
 * @version 1.0
 */
public class JettyStarter {
	private static final Logger LOG = LoggerFactory
			.getLogger(JettyStarter.class);
	private static Properties systemConfig = new Properties();
	private static final String JETTY_PATH = "/jetty.xml";
	private static final String JETTY_PATH_SSL = "/jetty-ssl.xml";

	static {
		try {
			systemConfig.load(JettyStarter.class
					.getResourceAsStream("/system.properties"));
		} catch (IOException e) {
			LOG.error("没有在CLASSPATH里面找到system.properties文件", e);
		}
	}

	public static void main(String[] args) throws Exception {
		try {
			boolean useSsl = false;
			String useSslStr = systemConfig.getProperty("USE_SSL");
			if ("Y".equals(useSslStr) || "yes".equalsIgnoreCase(useSslStr)) {
				useSsl = true;
			}

			String jettyPath = useSsl ? JETTY_PATH_SSL : JETTY_PATH;

			XmlConfiguration jettyConf = new XmlConfiguration(
					JettyStarter.class.getResourceAsStream(jettyPath));
			Server jettyServer = new Server();
			jettyConf.configure(jettyServer);
			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			LOG.error("Jetty启动失败", e);
		}
	}
}
