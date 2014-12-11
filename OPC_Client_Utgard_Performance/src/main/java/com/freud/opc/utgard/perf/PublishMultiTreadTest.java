package com.freud.opc.utgard.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.config;

import java.util.Date;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.Async20Access;
import org.openscada.opc.lib.da.AutoReconnectController;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;

public class PublishMultiTreadTest {

	public static void main(String[] args) throws Exception {
		for (int i = 1; i <= 10; i++) {
			new Thread(new PublishMultiThread(i)).start();
		}
	}

}

class PublishMultiThread implements Runnable {

	private static Logger LOGGER = Logger.getLogger(PublishMultiThread.class);

	private static final int NUMBER = 4000;
	private int countNumber;

	public PublishMultiThread(int countNumber) {
		this.countNumber = countNumber;
	}

	public void run() {
		try {
			long start = System.currentTimeMillis();
			LOGGER.info("Step-" + countNumber * NUMBER + " site:");
			LOGGER.info("StartDate[" + new Date() + "],CurrentMillis:" + start);
			Server server = new Server(config(),
					Executors.newSingleThreadScheduledExecutor());
			AutoReconnectController controller = new AutoReconnectController(
					server);
			controller.connect();
			AccessBase access = new Async20Access(server, 0, true);

			for (int i = (countNumber - 1) * NUMBER; i < countNumber * NUMBER; i++) {
				access.addItem("Random.Real" + i, new DataCallback() {
					public void changed(Item item, ItemState is) {
					}
				});
			}
			access.bind();
			Thread.sleep(12 * 60 * 60 * 1000);
			access.unbind();
			long end = System.currentTimeMillis();
			LOGGER.info("EndDate[" + new Date() + "],CurrentMillis:" + end);
			LOGGER.info("Total Spend:[" + (end - start) + "]");
			server.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}