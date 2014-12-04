package com.freud.opc.jeasyopc.perf.listener;

import javafish.clients.opc.JCustomOpc;
import javafish.clients.opc.asynch.AsynchEvent;
import javafish.clients.opc.asynch.OpcAsynchGroupListener;

import org.apache.log4j.Logger;

public class JeasyOPCListener implements OpcAsynchGroupListener {

	private static Logger LOGGER = Logger.getLogger(JeasyOPCListener.class);

	@Override
	public void getAsynchEvent(AsynchEvent event) {
		// LOGGER.info("EndDate[" + new Date() + "],CurrentMillis:"
		// + System.currentTimeMillis());
		// LOGGER.info(((JCustomOpc) event.getSource()).getFullOpcServerName()
		// + "=>" + "Package: " + event.getID() + ",count:"
		// + event.getOPCGroup().getItemCount());
		//
		System.out.println(((JCustomOpc) event.getSource())
				.getFullOpcServerName() + "=>");
		System.out.println("Package: " + event.getID());
		System.out.println(event.getOPCGroup());
	}

}
