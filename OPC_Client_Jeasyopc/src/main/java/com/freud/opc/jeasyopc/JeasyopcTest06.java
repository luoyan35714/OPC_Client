package com.freud.opc.jeasyopc;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;

/**
 * 同步读取Group
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest06 {

	public static void main(String[] args) throws Exception {

		JOpc.coInitialize();

		JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation.1", "JOPC1");

		OpcItem item1 = new OpcItem("Random.Real8", true, "");
		OpcItem item2 = new OpcItem("Random.Real8", true, "");
		OpcItem item3 = new OpcItem("Random.Real8", true, "");

		OpcGroup group = new OpcGroup("group1", true, 10, 0.0f);

		group.addItem(item1);
		group.addItem(item2);
		group.addItem(item3);

		jopc.addGroup(group);

		jopc.connect();
		System.out.println("JOPC client is connected...");

		jopc.registerGroups();
		System.out.println("OPCGroup are registered...");

		synchronized (JeasyopcTest06.class) {
			JeasyopcTest06.class.wait(2000);
		}

		// Synchronous reading of group
		int cycles = 30;
		int acycle = 0;
		/**
		 * 设置读取30次
		 */
		while (acycle++ < cycles) {
			/**
			 * 设置每2秒钟读取一次
			 */
			synchronized (JeasyopcTest06.class) {
				JeasyopcTest06.class.wait(2000);
			}

			OpcGroup responseGroup = jopc.synchReadGroup(group);
			System.out.println(responseGroup);

		}

		JOpc.coUninitialize();
	}
}
