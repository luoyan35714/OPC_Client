package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.util.Date;
import java.util.Iterator;
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
import org.openscada.opc.lib.da.Server;

public class AsyncOPCPerfTest {

	private static Logger LOGGER = Logger.getLogger(AsyncOPCPerfTest.class);

	private static final int NUMBER = 10;
	private static final int count = 1;
	public static long start;

	public static void main(String[] args) throws Exception {

		start = System.currentTimeMillis();

		LOGGER.info("Step-" + count + "W:");
		LOGGER.info("StartDate[" + new Date() + "],CurrentMillis:" + start);

		Server server = new Server(config(),
				Executors.newScheduledThreadPool(4));

		server.connect();

		Group group = server.addGroup("Freud");
		// AsyncResult result = group.getAsyncIO20().read(transactionId,
		// serverHandles);

		group.addItem("Read Error.Int1");
		group.addItem("Random.Int1");

		OPCAsyncIO2 async20 = group.getAsyncIO20();

		if (async20 == null)
			throw new NotConnectedException();
		async20.refresh(OPCDATASOURCE.OPC_DS_DEVICE, 0);

		group.attach(new IOPCDataCallback() {

			@Override
			public void writeComplete(int arg0, int arg1, int arg2,
					ResultSet<Integer> arg3) {
				System.out.println("Write Complete");
			}

			@Override
			public void readComplete(int arg0, int arg1, int arg2, int arg3,
					KeyedResultSet<Integer, ValueData> arg4) {
				System.out.println("Read Complete");
			}

			@Override
			public void dataChange(int arg0, int arg1, int arg2, int arg3,
					KeyedResultSet<Integer, ValueData> arg4) {
				Iterator<KeyedResult<Integer, ValueData>> ite = arg4.iterator();
				int i = 0;
				while (ite.hasNext()) {
					KeyedResult<Integer, ValueData> value = ite.next();
					System.out.println((++i) + "-" + value.getKey() + "--"
							+ value.getValue().getValue());
				}
			}

			@Override
			public void cancelComplete(int arg0, int arg1) {
				System.out.println("Cancel Complete");
			}
		});

		// AccessBase access = new SyncAccess(server, 1000);
		//
		// int limit = count * NUMBER;
		// for (int i = 0; i < limit; i++) {
		// access.addItem("Random.Real" + i, new DataCallBackListener(limit));
		// }
		//
		// access.bind();
		Thread.sleep(100000);
		// access.unbind();

		server.dispose();
	}
}
