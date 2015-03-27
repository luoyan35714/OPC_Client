package com.freud.opc.jeasyopc;

import javafish.clients.opc.browser.JOpcBrowser;

/**
 * 通过Branch名字查找所有Item
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest05 {

	public static void main(String[] args) throws Exception {

		JOpcBrowser jbrowser = new JOpcBrowser("localhost",
				"Matrikon.OPC.Simulation.1", "JOPCBrowser1");
		String[] items = jbrowser.getOpcItems("Simulation Items.Random", true);

		if (items != null) {
			System.out
					.println("------------List All Leafs under[Simulation Items.Random]------------");
			for (int i = 0; i < items.length; i++) {
				String item = items[i];
				System.out.println(item);
			}
		}

		// uninitialize COM components
		JOpcBrowser.coUninitialize();
	}
}
