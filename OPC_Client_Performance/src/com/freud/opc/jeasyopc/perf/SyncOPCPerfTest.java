package com.freud.opc.jeasyopc.perf;

import java.util.Date;

import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;

import org.apache.log4j.Logger;

public class SyncOPCPerfTest {

	private static final int NUMBER = 10000;

	private static Logger LOGGER = Logger.getLogger(SyncOPCPerfTest.class);

	private static JEasyOpc opc;
	private static OpcGroup opcGroup;
	private static OpcItem item;

	public static void main(String[] args) throws Exception {

		LOGGER.info("===================Sync Log Start=============================");
		LOGGER.info("================================================");
		LOGGER.info("================================================");
		LOGGER.info("================================================");
		for (int i = 1; i <= 10; i++) {
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

		opcGroup = new OpcGroup("group1", true, 3 * 1000, 0.0f);

		for (int i = 1; i < count * NUMBER; i++) {
			item = new OpcItem("Random.Real" + i, true, "");
			opcGroup.addItem(item);
		}

		LOGGER.info("Item traversal Date[" + new Date() + "],CurrentMillis:"
				+ System.currentTimeMillis());

		opc.addGroup(opcGroup);

		opc.connect();
		opc.registerGroups();

		synchronized (SyncOPCPerfTest.class) {
			SyncOPCPerfTest.class.wait(2000);
		}

		LOGGER.info("Read startDate[" + new Date() + "],CurrentMillis:"
				+ System.currentTimeMillis());
		long readStart = System.currentTimeMillis();
		opc.synchReadGroup(opcGroup);

		LOGGER.info("EndDate[" + new Date() + "],CurrentMillis:"
				+ System.currentTimeMillis());

		long end = System.currentTimeMillis();

		LOGGER.info("total used[" + (end - start) + "],readUsed["
				+ (end - readStart) + "]");

		opc.unregisterGroups();
		opc.terminate();
		JOpc.coUninitialize();
	}
}
