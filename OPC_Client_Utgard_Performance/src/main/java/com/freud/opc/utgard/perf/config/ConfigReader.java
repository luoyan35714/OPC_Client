package com.freud.opc.utgard.perf.config;

import java.io.IOException;
import java.util.Properties;

import org.openscada.opc.lib.common.ConnectionInformation;

public class ConfigReader {

	private static Properties prop;

	public static final String HOST = "host";
	public static final String DOMAIN = "domain";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String CLSID = "clsid";
	public static final String PROGID = "progid";

	static {
		try {
			prop = new Properties();
			prop.load(ConfigReader.class.getClassLoader().getResourceAsStream(
					"config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getProp(String name) {
		return prop.getProperty(name);
	}

	public static ConnectionInformation config() {
		ConnectionInformation ci = new ConnectionInformation();
		ci.setHost(getProp(HOST));
		ci.setDomain(getProp(DOMAIN));
		ci.setUser(getProp(USERNAME));
		ci.setPassword(getProp(PASSWORD));
		ci.setClsid(getProp(CLSID));
		return ci;
	}
}
