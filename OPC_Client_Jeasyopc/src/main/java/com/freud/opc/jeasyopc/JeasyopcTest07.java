package com.freud.opc.jeasyopc;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.SynchReadItemExample;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;

/**
 * 同步读取Item值
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest07 {

	public static void main(String[] args) throws Exception {

		SynchReadItemExample test = new SynchReadItemExample();

		JOpc.coInitialize();

		JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation", "JOPC1");

		OpcItem item1 = new OpcItem("Random.int1", true, "");

		OpcGroup group = new OpcGroup("group1", true, 500, 0.0f);

		group.addItem(item1);
		jopc.addGroup(group);

		jopc.connect();
		System.out.println("JOPC client is connected...");

		jopc.registerGroups();
		System.out.println("OPCGroup are registered...");

		synchronized (test) {
			test.wait(50);
		}

		// Synchronous reading of item
		int cycles = 7;
		int acycle = 0;
		while (acycle++ < cycles) {
			synchronized (test) {
				test.wait(1000);
			}
			OpcItem responseItem = jopc.synchReadItem(group, item1);
			System.out.println(responseItem);
			System.out.println(Variant.getVariantName(responseItem
					.getDataType()) + ": " + responseItem.getValue());

		}

		JOpc.coUninitialize();
	}
}
