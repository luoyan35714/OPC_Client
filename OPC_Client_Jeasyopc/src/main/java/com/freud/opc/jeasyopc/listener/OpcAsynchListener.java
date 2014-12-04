package com.freud.opc.jeasyopc.listener;

import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.asynch.AsynchEvent;
import javafish.clients.opc.asynch.OpcAsynchGroupListener;

public class OpcAsynchListener implements OpcAsynchGroupListener {

	@Override
	public void getAsynchEvent(AsynchEvent event) {
		System.out.println(((JCustomOpc) event.getSource())
				.getFullOpcServerName() + "=>");
		System.out.println("Package: " + event.getID());
		System.out.println(event.getOPCGroup());

		// System.out.println(((JCustomOpc) event.getSource())
		// .getFullOpcServerName() + "=>");
		// System.out.println("Package: " + event.getID());
		// System.out.println("Group name: " +
		// event.getOPCGroup().getGroupName());
		// System.out
		// .println("Items count: " + event.getOPCGroup().getItemCount());
	}

}
