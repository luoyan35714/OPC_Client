package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.util.Date;

import org.apache.log4j.Logger;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.Server;

public class SyncMultiThreadTest2 {
	private static Logger LOGGER = Logger.getLogger(SyncMultiThreadTest2.class);

	private static long[] start = new long[10];
	private static long[] end = new long[10];

	public synchronized static void start(int time, long in) {
		if (start[time] == 0)
			start[time] = in;
	}

	public synchronized static void end(int time, long in) {
		end[time] = in;
		LOGGER.info(time + "-Asynch read total used["
				+ (end[time] - start[time]) + "] s");
	}

	private static final int count = 1;

	public static void main(String[] args) throws Exception {
		for (int time = 1; time <= 10; time++) {
			for (int i = 1; i <= count; i++) {
				new Thread(new TestMultiple2(time, i)).start();
			}
		}
	}
}

class TestMultiple2 implements Runnable {

	private static Logger LOGGER = Logger.getLogger(TestMultiple2.class);

	private static final int NUMBER = 2 * 10000;
	private int time;
	private int count_number;

	public TestMultiple2(int time, int count_number) {
		this.time = time;
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
		SyncMultiThreadTest2.start(time, read);
		group.read(true, items);

		long end = System.currentTimeMillis();
		// LOGGER.info("[" + limit + "W:]" + "End Read[" + new Date()
		// + "],CurrentMillis:" + end);
		SyncMultiThreadTest2.end(time, end);
		// LOGGER.info(MessageFormat.format("[" + limit + "W:]"
		// + "Total[{0}], CreateItem[{1}], Read[{2}]", end - start,
		// createEnd - createStart, end - read));

		group.remove();
		server.dispose();
	}
}