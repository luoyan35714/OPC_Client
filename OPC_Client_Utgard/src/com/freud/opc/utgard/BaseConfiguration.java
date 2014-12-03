package com.freud.opc.utgard;

import java.io.IOException;
import java.util.Properties;

import org.openscada.opc.lib.common.ConnectionInformation;

/**
 * 配置文件工具类
 * 
 * @author Freud
 * 
 */
public final class BaseConfiguration {

	private final static ConnectionInformation ci;
	private final static Properties prop;

	public final static String CONFIG_USERNAME = "username";
	public final static String CONFIG_PASSWORD = "password";
	public final static String CONFIG_HOST = "host";
	public final static String CONFIG_DOMAIN = "domain";
	public final static String CONFIG_CLSID = "clsid";
	public final static String CONFIG_PROGID = "progid";

	private final static String CONFIG_FILE_NAME = "config.properties";

	/**
	 * 加载配置文件
	 */
	static {
		ci = new ConnectionInformation();
		prop = new Properties();
		try {
			prop.load(BaseConfiguration.class.getClassLoader()
					.getResourceAsStream(CONFIG_FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过名字获得配置的值
	 * 
	 * @param name
	 * @return
	 */
	public static String getEntryValue(String name) {
		return prop.getProperty(name);
	}

	/**
	 * 获得包含ClsId的连接信息
	 * 
	 * @return
	 */
	public static ConnectionInformation getCLSIDConnectionInfomation() {
		ci.setProgId(null);
		getConnectionInfomation();
		ci.setClsid(prop.getProperty(CONFIG_CLSID));
		return ci;
	}

	/**
	 * 获得包含ProgId的连接信息
	 * 
	 * @return
	 */
	public static ConnectionInformation getPROGIDConnectionInfomation() {
		ci.setClsid(null);
		getConnectionInfomation();
		ci.setProgId(prop.getProperty(CONFIG_PROGID));
		return ci;
	}

	/**
	 * 获得基础的连接信息
	 */
	private static void getConnectionInfomation() {
		ci.setHost(prop.getProperty(CONFIG_HOST));
		ci.setDomain(prop.getProperty(CONFIG_DOMAIN));
		ci.setUser(prop.getProperty(CONFIG_USERNAME));
		ci.setPassword(prop.getProperty(CONFIG_PASSWORD));
	}
}
