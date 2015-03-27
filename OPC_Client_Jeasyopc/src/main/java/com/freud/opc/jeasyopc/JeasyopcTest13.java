package com.freud.opc.jeasyopc;

import javafish.clients.opc.EmptyValueExample;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.exception.ComponentNotFoundException;
import javafish.clients.opc.exception.SynchReadException;
import javafish.clients.opc.variant.Variant;

/**
 * 空值示例
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest13 {

	public static void main(String[] args) throws Exception {
		EmptyValueExample test = new EmptyValueExample();

		JOpc.coInitialize();

		JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation", "JOPC1");

		OpcItem item = new OpcItem("Random.ArrayOfReal8", true, "");

		OpcGroup group = new OpcGroup("group1", true, 10, 0.0f);

		group.addItem(item);
		jopc.addGroup(group);

		jopc.connect();
		System.out.println("JOPC client is connected...");

		jopc.registerGroups();
		System.out.println("OPCGroup are registered...");

		// not waiting for registration
		synchReadItem(jopc, group, item);
		synchronized (test) {
			test.wait(2000);
		}
		synchReadItem(jopc, group, item);

		JOpc.coUninitialize();

	}

	private static void synchReadItem(JOpc jopc, OpcGroup group, OpcItem item)
			throws ComponentNotFoundException, SynchReadException {
		// read again
		OpcItem responseItem = jopc.synchReadItem(group, item);
		System.out.println(responseItem);
		System.out.println(!responseItem.isQuality() ? "Quality: BAD!!!"
				: "Quality: GOOD");
		// processing
		if (!responseItem.isQuality()) {
			System.out
					.println("This next processing is WRONG!!! You haven't quality!!!");
		}
		System.out.println("Processing: Data type: "
				+ Variant.getVariantName(responseItem.getDataType())
				+ " Value: " + responseItem.getValue());

	}
}
