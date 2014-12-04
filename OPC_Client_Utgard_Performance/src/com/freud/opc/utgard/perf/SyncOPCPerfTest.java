package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.CLSID;
import static com.freud.opc.utgard.perf.config.ConfigReader.DOMAIN;
import static com.freud.opc.utgard.perf.config.ConfigReader.HOST;
import static com.freud.opc.utgard.perf.config.ConfigReader.PASSWORD;
import static com.freud.opc.utgard.perf.config.ConfigReader.USERNAME;
import static com.freud.opc.utgard.perf.config.ConfigReader.getProp;

import java.util.concurrent.Executors;

import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;

public class SyncOPCPerfTest {

	public static void main(String[] args) throws Exception {
		ConnectionInformation ci = new ConnectionInformation();
		ci.setHost(getProp(HOST));
		ci.setDomain(getProp(DOMAIN));
		ci.setUser(getProp(USERNAME));
		ci.setPassword(getProp(PASSWORD));
		ci.setClsid(getProp(CLSID));

		Server server = new Server(ci,
				Executors.newSingleThreadScheduledExecutor());

		server.connect();

		Group group = server.addGroup();
		Item item = group.addItem("Random.Real");

		group.read(true, item);

		ItemState is = item.read(true);

	}
}
