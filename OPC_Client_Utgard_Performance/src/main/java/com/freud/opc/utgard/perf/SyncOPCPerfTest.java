package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.text.MessageFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.Server;

public class SyncOPCPerfTest {

	private static Logger LOGGER = Logger.getLogger(SyncOPCPerfTest.class);

	private static final int NUMBER = 100;
	private static final int WAN_NUMBER = 1;

	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= WAN_NUMBER; i++) {
			testSteps(i);
		}
	}

	private static void testSteps(int count) throws Exception {
		long start = System.currentTimeMillis();

		LOGGER.info("Step-" + count + "W:");
		LOGGER.info("startDate[" + new Date() + "],CurrentMillis:" + start);

		Server server = new Server(config(), null);

		server.connect();

		Group group = server.addGroup();

		int limit = count * NUMBER;
		Item[] items = new Item[limit];

		long createStart = System.currentTimeMillis();
		LOGGER.info("Create the items[" + new Date() + "],CurrentMillis:"
				+ createStart);

		for (int i = 0; i < limit; i++) {
			Item item = group.addItem("Random.Real" + i);
			items[i] = item;
		}

		long createEnd = System.currentTimeMillis();
		LOGGER.info("Create finish [" + new Date() + "],CurrentMillis:"
				+ createEnd);

		long read = System.currentTimeMillis();
		LOGGER.info("Start Read[" + new Date() + "],CurrentMillis:" + read);

		group.read(true, items);

		long end = System.currentTimeMillis();
		LOGGER.info("End Read[" + new Date() + "],CurrentMillis:" + end);

		LOGGER.info(MessageFormat.format(
				"Total[{0}], CreateItem[{1}], Read[{2}]", end - start,
				createEnd - createStart, end - read));

		server.dispose();
	}
}
