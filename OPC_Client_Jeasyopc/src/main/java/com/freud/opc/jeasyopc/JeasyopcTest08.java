package com.freud.opc.jeasyopc;

import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;
import javafish.clients.opc.variant.Variant;
import javafish.clients.opc.variant.VariantList;

/**
 * Õ¨≤Ω∂¡–¥Item÷µ
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest08 {

	public static void main(String[] args) throws Exception {

		JOpc jopc = new JOpc("localhost", "Matrikon.OPC.Simulation.1", "JOPC1");

		JOpc.coInitialize();

		OpcItem item1 = new OpcItem("Bucket Brigade.ArrayOfReal8", true, "");

		OpcGroup group = new OpcGroup("Random", true, 500, 0.0f);

		group.addItem(item1);

		jopc.connect();
		System.out.println("OPC is connected...");
		jopc.addGroup(group);

		jopc.registerGroup(group);
		System.out.println("Group was registred...");
		jopc.registerItem(group, item1);
		System.out.println("Item was registred...");

		// synchronous reading
		OpcItem itemRead = null;
		for (int i = 0; i < 2; i++) {
			Thread.sleep(2000);

			itemRead = jopc.synchReadItem(group, item1);
			System.out.println(itemRead);
		}

		// synchronous writing (example with array writing ;-)
		// prepare array
		VariantList list = new VariantList(Variant.VT_R8);
		list.add(new Variant(2.0));
		list.add(new Variant(2.0));
		list.add(new Variant(3.0));
		Variant varin = new Variant(list);
		System.out
				.println("Original Variant type: "
						+ Variant.getVariantName(varin.getVariantType()) + ", "
						+ varin);
		item1.setValue(varin);

		// write to opc-server
		jopc.synchWriteItem(group, item1);

		Thread.sleep(2000);

		// read item from opc-server
		itemRead = jopc.synchReadItem(group, item1);

		// show item and its variant type
		System.out.println("WRITE ITEM IS: " + itemRead);
		System.out.println("VALUE TYPE: "
				+ Variant.getVariantName(itemRead.getDataType()));

		jopc.unregisterItem(group, item1);
		System.out.println("Item was unregistred...");
		jopc.unregisterGroup(group);
		System.out.println("Group was unregistred...");
		JOpc.coUninitialize();
		System.out.println("OPC is disconnected...");

	}
}
