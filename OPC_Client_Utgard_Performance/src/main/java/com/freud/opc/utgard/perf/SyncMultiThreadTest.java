package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.Server;

public class SyncMultiThreadTest {
	private static Logger LOGGER = Logger.getLogger(SyncMultiThreadTest.class);

	public synchronized static void start(long in) {
		if (start == 0)
			start = in;
	}

	public synchronized static void end(long in) {
		end = in;
		LOGGER.info("Asynch read total used[" + (end - start) + "] s");
	}

	private static long start;
	private static long end;

	private static final int count = 100;

	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= count; i++) {
			new Thread(new TestMultiple(i)).start();
		}
	}
}

class TestMultiple implements Runnable {

	private static Logger LOGGER = Logger.getLogger(TestMultiple.class);

	private static final int NUMBER = 4000;

	private int count_number;

	public TestMultiple(int count_number) {
		this.count_number = count_number;
	}

	public void run() {
		try {
			testSteps();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void testSteps() throws Exception {

		long start = System.currentTimeMillis();

		int limit = count_number * NUMBER;

		LOGGER.info("[" + limit + ":]" + "Step-" + limit + ":");
		LOGGER.info("[" + limit + ":]" + "startDate[" + new Date()
				+ "],CurrentMillis:" + start);

		Server server = new Server(config(), null);

		server.connect();

		Group group = server.addGroup();

		Item[] items = new Item[NUMBER];

		long createStart = System.currentTimeMillis();
		LOGGER.info("[" + limit + "W:]" + "Create the items[" + new Date()
				+ "],CurrentMillis:" + createStart);

		for (int i = (count_number - 1) * NUMBER; i < limit; i++) {
			Item item = group.addItem("Random.Real" + i);
			items[i % NUMBER] = item;
		}

		long createEnd = System.currentTimeMillis();
		LOGGER.info("[" + limit + "W:]" + "Create finish [" + new Date()
				+ "],CurrentMillis:" + createEnd);

		long read = System.currentTimeMillis();
		LOGGER.info("[" + limit + "W:]" + "Start Read[" + new Date()
				+ "],CurrentMillis:" + read);
		SyncMultiThreadTest.start(read);
		group.read(true, items);

		long end = System.currentTimeMillis();
		// LOGGER.info("[" + limit + "W:]" + "End Read[" + new Date()
		// + "],CurrentMillis:" + end);
		SyncMultiThreadTest.end(end);
		// LOGGER.info(MessageFormat.format("[" + limit + "W:]"
		// + "Total[{0}], CreateItem[{1}], Read[{2}]", end - start,
		// createEnd - createStart, end - read));

		group.remove();
		server.dispose();
	}
}