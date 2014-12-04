package com.freud.opc.jeasyopc;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.CoInitializeException;

/**
 * 连接到OPC Server, 并添加一个Group，在Group下添加一个Item
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest02 {

	public static void main(String[] args) throws Exception {

		try {
			JOpc.coInitialize();
		} catch (CoInitializeException e1) {
			e1.printStackTrace();
		}

		JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation", "JOPC1");

		OpcItem item1 = new OpcItem("Random.Real8", true, "");
		OpcGroup group = new OpcGroup("group1", false, 500, 0.0f);

		group.addItem(item1);

		jopc.addGroup(group);

		jopc.connect();
		System.out.println("Connected...");

		JOpc.coUninitialize();
		System.out.println("Test terminated...");
	}
}
