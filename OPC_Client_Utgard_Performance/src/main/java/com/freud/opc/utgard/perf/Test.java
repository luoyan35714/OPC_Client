package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.ResultSet;
import org.openscada.opc.dcom.da.IOPCDataCallback;
import org.openscada.opc.dcom.da.ValueData;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.Server;

public class Test {
	private static Logger LOGGER = Logger.getLogger(Test.class);

	public static void main(String[] args) throws Exception {
		long start = System.currentTimeMillis();

		LOGGER.info("Step-" + 10 + "W:");
		LOGGER.info("StartDate[" + new Date() + "],CurrentMillis:" + start);

		Server server = new Server(config(),
				Executors.newSingleThreadScheduledExecutor());

		server.connect();

		Group group = server.addGroup();
		Item item = group.addItem("Random.int4");

		group.setActive(true);
		group.setActive(true, item);

		group.attach(new IOPCDataCallback() {

			@Override
			public void writeComplete(int i, int j, int k,
					ResultSet<Integer> resultset) {
				System.out.println("Write Complete");
			}

			@Override
			public void readComplete(int i, int j, int k, int l,
					KeyedResultSet<Integer, ValueData> keyedresultset) {
				System.out.println("Read Complete");
			}

			@Override
			public void dataChange(int i, int j, int k, int l,
					KeyedResultSet<Integer, ValueData> keyedresultset) {
				System.out.println("Data change");
			}

			@Override
			public void cancelComplete(int i, int j) {
				System.out.println("Cancel Complete");
			}
		});

		Thread.sleep(10 * 1000);

		server.disconnect();
	}
}
