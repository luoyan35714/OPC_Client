package com.freud.opc.jeasyopc;

import com.freud.opc.jeasyopc.listener.OpcAsynchListener;

import javafish.clients.opc.JEasyOpc;
import javafish.clients.opc.JOpc;
import javafish.clients.opc.component.OpcGroup;
import javafish.clients.opc.component.OpcItem;

/**
 * “Ï≤Ω∂©‘ƒ Ω∂¡»°
 * 
 * @author Freud
 * 
 */
public class JeasyopcTest11 {

	public static void main(String[] args) throws Exception {
		OpcAsynchListener listener = new OpcAsynchListener();

		JOpc.coInitialize();

		JEasyOpc jopc = new JEasyOpc("localhost", "Matrikon.OPC.Simulation",
				"JOPC");

		OpcItem item1 = new OpcItem("Random.Real8", true, "");
		OpcItem item2 = new OpcItem("Random.Real8", true, "");
		OpcItem item3 = new OpcItem("Random.Real8", true, "");
		OpcItem item4 = new OpcItem("Random.Real8", true, "");

		OpcGroup group1 = new OpcGroup("group1", true, 2000, 0.0f);

		group1.addItem(item1);
		group1.addItem(item2);
		group1.addItem(item3);
		group1.addItem(item4);

		group1.addAsynchListener(listener);

		jopc.addGroup(group1);

		jopc.start();

		System.out
				.println("--------------------------JOPC started--------------------------");

		synchronized (listener) {
			listener.wait(3000);
		}

		System.out.println("--------------------------JOPC active:["
				+ jopc.ping() + "]--------------------------");

		synchronized (listener) {
			listener.wait(8000);
		}

		jopc.setGroupActivity(group1, false);
		System.out.println("--------------------------Set JOPC status:["
				+ group1.isActive() + "]--------------------------");

		synchronized (listener) {
			listener.wait(4000);
		}

		jopc.setGroupActivity(group1, true);
		System.out.println("--------------------------Set JOPC status:["
				+ group1.isActive() + "]--------------------------");

		synchronized (listener) {
			listener.wait(4000);
		}

		jopc.setGroupUpdateTime(group1, 100);
		System.out.println("--------------------------Set JOPC update rate : ["
				+ group1.getUpdateRate() + "]--------------------------");

		synchronized (listener) {
			listener.wait(2000);
		}

		jopc.terminate();

		JOpc.coUninitialize();
	}
}
