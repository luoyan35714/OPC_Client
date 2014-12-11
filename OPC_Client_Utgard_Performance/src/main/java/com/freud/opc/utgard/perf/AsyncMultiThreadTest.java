package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.ResultSet;
import org.openscada.opc.dcom.da.IOPCDataCallback;
import org.openscada.opc.dcom.da.OPCDATASOURCE;
import org.openscada.opc.dcom.da.ValueData;
import org.openscada.opc.dcom.da.impl.OPCAsyncIO2;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.Group;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.Server;

public class AsyncMultiThreadTest {

	private static final int count = 1;

	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= count; i++) {
			new Thread(new AsyncMulti(i)).start();
		}
	}
}

class AsyncMulti implements Runnable {

	private static Logger LOGGER = Logger.getLogger(AsyncMulti.class);

	private static final int NUMBER = 10000;
	private int count_number;

	private static long start;
	private static long read;
	private static long end;

	public AsyncMulti(int count_number) {
		this.count_number = count_number;
	}

	public void run() {
		try {
			start = System.currentTimeMillis();

			LOGGER.info("Step-" + count_number * NUMBER + " site:");
			LOGGER.info("StartDate[" + new Date() + "],CurrentMillis:" + start);

			Server server = new Server(config(),
					Executors.newScheduledThreadPool(4));

			server.connect();

			final Group group = server.addGroup("Group-" + count_number);
			group.setActive(true);
			Item[] items = new Item[NUMBER];

			for (int i = (count_number - 1) * NUMBER; i < count_number * NUMBER; i++) {
				items[i % NUMBER] = group.addItem("Random.Int" + i);
			}

			read = System.currentTimeMillis();
			group.attach(new IOPCDataCallback() {

				public void writeComplete(int arg0, int arg1, int arg2,
						ResultSet<Integer> arg3) {
				}

				public void readComplete(int arg0, int arg1, int arg2,
						int arg3, KeyedResultSet<Integer, ValueData> arg4) {
				}

				public void dataChange(int arg0, int arg1, int arg2, int arg3,
						KeyedResultSet<Integer, ValueData> result) {
					int i = 0;
					for (final KeyedResult<Integer, ValueData> entry : result) {
						i++;
					}

					if (i == NUMBER) {
						end = System.currentTimeMillis();
						LOGGER.info("Total Use[" + (end - start)
								+ "] and Async Read[" + (end - read) + "]");
					}

				}

				public void cancelComplete(int arg0, int arg1) {
				}
			});

			final OPCAsyncIO2 async20 = group.getAsyncIO20();
			if (async20 == null) {
				throw new NotConnectedException();
			}

			group.getAsyncIO20().refresh(OPCDATASOURCE.OPC_DS_CACHE, 0);

			group.setActive(false);

			Thread.sleep(3000);

			group.clear();
			group.remove();

			server.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
