package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.Async20Access;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;

public class PublishOPCPerfTest {

	private static Logger LOGGER = Logger.getLogger(PublishOPCPerfTest.class);

	private static final int NUMBER = 10;
	private static final int WAN_NUMBER = 10;

	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= WAN_NUMBER; i++) {
			testSteps(i);
		}
	}

	private static void testSteps(int count) throws Exception {
		long start = System.currentTimeMillis();

		LOGGER.info("Step-" + count + "W:");
		LOGGER.info("StartDate[" + new Date() + "],CurrentMillis:" + start);

		Server server = new Server(config(),
				Executors.newSingleThreadScheduledExecutor());

		server.connect();

		AccessBase access = new Async20Access(server, 1000, true);

		int limit = count * NUMBER;
		for (int i = 0; i < limit; i++) {
			access.addItem("Random.Real" + i, new DataCallback() {
				@Override
				public void changed(Item item, ItemState is) {
					LOGGER.info("Item:[" + item.getId() + "], Value:["
							+ is.getValue() + "]");
				}
			});
		}

		access.bind();
		Thread.sleep(1000);
		access.unbind();
		long end = System.currentTimeMillis();
		LOGGER.info("EndDate[" + new Date() + "],CurrentMillis:" + end);
		LOGGER.info("Total Spend:[" + (end - start) + "]");
		server.dispose();
	}
}
