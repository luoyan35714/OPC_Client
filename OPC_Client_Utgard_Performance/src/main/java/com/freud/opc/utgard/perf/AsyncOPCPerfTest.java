package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;

import com.freud.opc.utgard.perf.common.DataCallBackListener;

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

		AccessBase access = new SyncAccess(server, 1000);
		int limit = count * NUMBER;
		for (int i = 0; i < limit; i++) {
			access.addItem("Random.Real" + i, new DataCallBackListener(limit));
		}

		access.bind();
		Thread.sleep(5000);
		access.unbind();

		server.dispose();
	}

}
