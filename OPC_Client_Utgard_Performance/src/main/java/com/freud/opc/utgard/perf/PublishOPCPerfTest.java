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
import org.openscada.opc.lib.da.Server;

public class PublishOPCPerfTest {

	private static Logger LOGGER = Logger.getLogger(PublishOPCPerfTest.class);

	//private static final int NUMBER = 10000;
	private static final int WAN_NUMBER = 1;

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

		Group group = server.addGroup("Group");

		//Map<String, Item> map = group.addItems("Random.int" + 1);

		group.attach(new IOPCDataCallback() {

			public void writeComplete(int i, int j, int k,
					ResultSet<Integer> resultset) {
			}

			public void readComplete(int i, int j, int k, int l,
					KeyedResultSet<Integer, ValueData> keyedresultset) {
			}

			public void dataChange(int i, int j, int k, int l,
					KeyedResultSet<Integer, ValueData> keyedresultset) {
				System.out.println("DataChanged");
			}

			public void cancelComplete(int i, int j) {
			}
		});

		long end = System.currentTimeMillis();
		LOGGER.info("EndDate[" + new Date() + "],CurrentMillis:" + end);
		LOGGER.info("Total Spend:[" + (end - start) + "]");
		server.dispose();
	}
}
