package com.freud.opc.jeasyopc;

import javafish.clients.opc.browser.JOpcBrowser;

/**
 * 通过OPC名字查找所有Branch
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest04 {

	public static void main(String[] args) throws Exception {

		JOpcBrowser jbrowser = new JOpcBrowser("localhost",
				"Matrikon.OPC.Simulation.1", "JOPCBrowser1");

		jbrowser.connect();
		String[] branches = jbrowser.getOpcBranch("");
		System.out.println("------------List All Branches------------");
		for (String branch : branches) {
			System.out.println("[branch]:" + branch);
		}

		// uninitialize COM components
		JOpcBrowser.coUninitialize();
	}
}
