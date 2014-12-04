package com.freud.opc.jeasyopc.perf;

import java.util.Date;

import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;

import org.apache.log4j.Logger;

import com.freud.opc.jeasyopc.perf.listener.JeasyOPCListener;

public class Async10OPCPerfTest {

	private static final int NUMBER = 1000;

	private static final int WAN_NUMBER = 1;

	private static Logger LOGGER = Logger.getLogger(Async10OPCPerfTest.class);

	private static JEasyOpc opc;
	private static OpcGroup opcGroup;
	private static OpcItem item;

	public static void main(String[] args) throws Exception {
		LOGGER.info("===================Aync Log Start=============================");
		LOGGER.info("================================================");
		LOGGER.info("================================================");
		LOGGER.info("================================================");

		for (int i = 1; i <= WAN_NUMBER; i++) {
			testSteps(i);
		}

		LOGGER.info("================================================");
		LOGGER.info("================================================");
		LOGGER.info("================================================");
		LOGGER.info("===================Aync Log end=============================");
	}

	private static void testSteps(int count) throws Exception {

		LOGGER.info("Step-" + count + "W:");
		LOGGER.info("startDate[" + new Date() + "],CurrentMillis:"
				+ System.currentTimeMillis());
		long start = System.currentTimeMillis();

		JOpc.coInitialize();

		opc = new JEasyOpc("localhost", "Matrikon.OPC.Simulation", "JOPC");

		opcGroup = new OpcGroup("group1", true, 1 * 1000, 0.0f);

		for (int i = 1; i <= count * NUMBER; i++) {
			item = new OpcItem("Random.Real" + i, true, "");
			opcGroup.addItem(item);
		}

		LOGGER.info("Item traversal Date[" + new Date() + "],CurrentMillis:"
				+ System.currentTimeMillis());

		JeasyOPCListener listener = new JeasyOPCListener();

		opcGroup.addAsynchListener(listener);

		opc.addGroup(opcGroup);

		opc.start();

		synchronized (listener) {
			listener.wait(1000);
		}

		opc.unregisterGroups();
		opc.terminate();
		JOpc.coUninitialize();
	}
}
