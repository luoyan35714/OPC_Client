package com.freud.dcom.utgard.cases;

import static com.freud.opc.utgard.BaseConfiguration.CONFIG_CLSID;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_DOMAIN;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_HOST;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_PASSWORD;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_USERNAME;
import static com.freud.opc.utgard.BaseConfiguration.getEntryValue;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIClsid;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JISession;
import org.openscada.opc.dcom.da.OPCSERVERSTATUS;
import org.openscada.opc.dcom.da.impl.OPCServer;

/**
 * 获取Server的Status信息 中文
 * 
 * @author Freud
 * 
 */
public class DCOMTest1 {

	public static void main(String[] args) throws Exception {

		JISystem.setAutoRegisteration(true);

		JISession _session = JISession.createSession(
				getEntryValue(CONFIG_DOMAIN), getEntryValue(CONFIG_USERNAME),
				getEntryValue(CONFIG_PASSWORD));

		final JIComServer comServer = new JIComServer(
				JIClsid.valueOf(getEntryValue(CONFIG_CLSID)),
				getEntryValue(CONFIG_HOST), _session);

		final IJIComObject serverObject = comServer.createInstance();

		OPCServer server = new OPCServer(serverObject);

		dumpServerStatus(server);

	}

	public static void dumpServerStatus(final OPCServer server)
			throws JIException {

		final OPCSERVERSTATUS status = server.getStatus();

		System.out.println("===== SERVER STATUS ======");
		System.out.println("State: " + status.getServerState().toString());
		System.out.println("Vendor: " + status.getVendorInfo());
		System.out.println(String.format("Version: %d.%d.%d",
				status.getMajorVersion(), status.getMinorVersion(),
				status.getBuildNumber()));
		System.out.println("Groups: " + status.getGroupCount());
		System.out.println("Bandwidth: " + status.getBandWidth());
		System.out.println(String.format("Start Time: %tc", status
				.getStartTime().asCalendar()));
		System.out.println(String.format("Current Time: %tc", status
				.getCurrentTime().asCalendar()));
		System.out.println(String.format("Last Update Time: %tc", status
				.getLastUpdateTime().asCalendar()));
		System.out.println("===== SERVER STATUS ======");
	}
}
