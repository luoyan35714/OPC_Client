package com.freud.opc.utgard.cases;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.Server;

import com.freud.opc.utgard.BaseConfiguration;

/**
 * 同步读取某个点位的值
 * 
 * @author Freud
 * 
 */
public class OPCTest3 {

	public static void main(String[] args) throws Exception {

		Server server = new Server(
				BaseConfiguration.getCLSIDConnectionInfomation(),
				Executors.newSingleThreadScheduledExecutor());

		server.connect();

		Group group = server.addGroup();
		Item item = group.addItem("Random.Real5");

		Map<String, Item> items = group.addItems("Random.Real1",
				"Random.Real2", "Random.Real3", "Random.Real4");

		dumpItem(item);

		for (Entry<String, Item> temp : items.entrySet()) {
			dumpItem(temp.getValue());
		}

		server.dispose();
	}

	private static void dumpItem(Item item) throws JIException {
		System.out.println("[" + (++count) + "],ItemName:[" + item.getId()
				+ "],value:" + item.read(false).getValue());
	}

	private static int count;
}
