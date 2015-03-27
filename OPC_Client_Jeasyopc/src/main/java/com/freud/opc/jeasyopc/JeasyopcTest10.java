package com.freud.opc.jeasyopc;

import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;

/**
 * 异步10和异步20读取
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest10 {

	public static void main(String[] args) throws Exception {

		JOpc.coInitialize();

		JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation", "JOPC1");
		JEasyOpc jopc2 = new JEasyOpc("localhost", "Matrikon.OPC.Simulation",
				"JOPC2");

		OpcItem item1 = new OpcItem("Random.Real8", true, "");
		OpcItem item2 = new OpcItem("Random.Real8", true, "");
		OpcGroup group = new OpcGroup("group1", true, 1000, 0.0f);

		OpcItem item3 = new OpcItem("Random.Real8", true, "");
		OpcItem item4 = new OpcItem("Random.Real8", true, "");

		OpcGroup group2 = new OpcGroup("group2", true, 2500, 0.0f);

		group.addItem(item1);
		group.addItem(item2);

		group2.addItem(item3);
		group2.addItem(item4);

		jopc.addGroup(group);
		jopc2.addGroup(group2);

		jopc.connect();
		jopc2.connect();
		System.out.println("OPC client is connected...");

		jopc.registerGroups();
		jopc2.registerGroups();
		System.out.println("OPC groups are registered...");

		jopc.asynch10Read(group);
		jopc2.asynch20Read(group2);
		System.out.println("OPC asynchronous reading is applied...");

		OpcGroup downGroup;
		OpcGroup downGroup2;

		long start = System.currentTimeMillis();
		while ((System.currentTimeMillis() - start) < 30000) {
			jopc.ping();
			jopc2.ping();

			downGroup = jopc.getDownloadGroup();
			if (downGroup != null) {
				System.out.println("[Group1]" + downGroup);
			}

			downGroup2 = jopc2.getDownloadGroup();
			if (downGroup2 != null) {
				System.out.println("[Group2]" + downGroup2);
			}

			synchronized (JeasyopcTest10.class) {
				JeasyopcTest10.class.wait(50);
			}
		}

		jopc.asynch10Unadvise(group);
		jopc2.asynch20Unadvise(group2);
		System.out.println("OPC asynchronous reading is unadvise...");

		JOpc.coUninitialize();
		System.out.println("Program terminated...");

	}
}
