package com.freud.opc.jeasyopc;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;

/**
 * 异步读写Item值
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest09 {

	public static void main(String[] args) throws Exception {

		JOpc.coInitialize();

		JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation", "JOPC1");

		OpcItem item1 = new OpcItem("Random.Real8", true, "");
		OpcItem item2 = new OpcItem("Random.Real8", true, "");
		OpcGroup group = new OpcGroup("group1", true, 2000, 0.0f);

		group.addItem(item1);
		group.addItem(item2);

		jopc.addGroup(group);

		jopc.connect();
		System.out.println("OPC client is connected...");

		jopc.registerGroups();
		System.out.println("OPC groups are registered...");

		jopc.asynch10Read(group);
		System.out.println("OPC asynchronous reading is applied...");

		OpcGroup downGroup;

		long start = System.currentTimeMillis();
		while ((System.currentTimeMillis() - start) < 10000) {
			jopc.ping();
			downGroup = jopc.getDownloadGroup();
			if (downGroup != null) {
				System.out.println(downGroup);
			}

			if ((System.currentTimeMillis() - start) >= 6000) {
				jopc.setGroupActivity(group, false);
			}

			synchronized (JeasyopcTest09.class) {
				JeasyopcTest09.class.wait(1000);
			}
		}

		// change activity
		jopc.setGroupActivity(group, true);

		// change updateTime
		jopc.setGroupUpdateTime(group, 100);

		start = System.currentTimeMillis();
		while ((System.currentTimeMillis() - start) < 5000) {
			jopc.ping();
			downGroup = jopc.getDownloadGroup();
			if (downGroup != null) {
				System.out.println(downGroup);
			}

			synchronized (JeasyopcTest09.class) {
				JeasyopcTest09.class.wait(50);
			}
		}

		jopc.asynch10Unadvise(group);
		System.out.println("OPC asynchronous reading is unadvise...");

		JOpc.coUninitialize();
		System.out.println("Program terminated...");

		System.out.println("");

	}
}
