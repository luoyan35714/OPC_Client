package com.freud.opc.utgard.cases;

import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.Server;

import com.freud.opc.utgard.BaseConfiguration;

/**
 * 异步写入
 * 
 * @author Freud
 * 
 */
public class OPCTest8 {

	public static void main(String[] args) throws Exception {

		Server server = new Server(
				BaseConfiguration.getCLSIDConnectionInfomation(),
				Executors.newSingleThreadScheduledExecutor());

		server.connect();

		Group group = server.addGroup();
		Item item = group.addItem("Square Waves.Real4");

		/** 构造写入数据 */
		final Float[] integerData = new Float[] { 1202f, 1203f, 1204f };
		final JIArray array = new JIArray(integerData, false);
		final JIVariant value = new JIVariant(array);

		/** 同步写入 */
		item.write(value);
		Thread.sleep(1000);

		/** Dump出item的值 */
		dumpItem(item);

		server.dispose();
	}

	private static void dumpItem(Item item) throws JIException {
		System.out.println("[" + (++count) + "],ItemName:[" + item.getId()
				+ "],value:" + item.read(true).getValue());
	}

	private static int count;
}
