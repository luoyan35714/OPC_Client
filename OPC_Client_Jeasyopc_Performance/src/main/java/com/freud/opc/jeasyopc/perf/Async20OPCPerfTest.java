package com.freud.opc.jeasyopc.perf;

import java.util.Date;

import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;

import org.apache.log4j.Logger;

import com.freud.opc.jeasyopc.perf.listener.JeasyOPCListener;

public class Async20OPCPerfTest {

	private static final int NUMBER = 10000;

	private static Logger LOGGER = Logger.getLogger(Async20OPCPerfTest.class);

	private static JEasyOpc opc;
	private static OpcGroup opcGroup;
	private static OpcItem item;

	public static void main(String[] args) throws Exception {
		LOGGER.info("===================Sync Log Start=============================");
		LOGGER.info("================================================");
		LOGGER.info("================================================");
		LOGGER.info("================================================");
		for (int i = 1; i <= 1; i++) {
			testSteps(i);
		}
		LOGGER.info("================================================");
		LOGGER.info("================================================");
		LOGGER.info("================================================");
		LOGGER.info("===================Sync Log end=============================");
	}

	private static void testSteps(int count) throws Exception {

		LOGGER.info("Step-" + count + "W:");
		LOGGER.info("startDate[" + new Date() + "],CurrentMillis:"
				+ System.currentTimeMillis());
		long start = System.currentTimeMillis();
		JOpc.coInitialize();

		opc = new JEasyOpc("localhost", "Matrikon.OPC.Simulation", "JOPC");

		opcGroup = new OpcGroup("group1", true, 1 * 1000, 0.0f);

		for (int i = 1; i < count * NUMBER; i++) {
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
			listener.wait(4000);
		}

		// LOGGER.info("Read startDate[" + new Date() + "],CurrentMillis:"
		// + System.currentTimeMillis());
		// long readStart = System.currentTimeMillis();
		//
		// LOGGER.info("EndDate[" + new Date() + "],CurrentMillis:"
		// + System.currentTimeMillis());
		//
		// long end = System.currentTimeMillis();

		// LOGGER.info("total used[" + (end - start) + "],readUsed["
		// + (end - readStart) + "]");

		opc.terminate();

		JOpc.coUninitialize();
	}

}
