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
import org.openscada.opc.dcom.da.IORequest;
import org.openscada.opc.dcom.da.impl.OPCGroupStateMgt;
import org.openscada.opc.dcom.da.impl.OPCItemIO;
import org.openscada.opc.dcom.da.impl.OPCServer;

/**
 * Query指定的Item
 * 
 * @author Freud
 * 
 */
public class DCOMTest6 {

	public static void main(String[] args) throws Exception {
		JISystem.setAutoRegisteration(true);

		/**
		 * Session获取
		 */
		JISession _session = JISession.createSession(
				getEntryValue(CONFIG_DOMAIN), getEntryValue(CONFIG_USERNAME),
				getEntryValue(CONFIG_PASSWORD));

		final JIComServer comServer = new JIComServer(
				JIClsid.valueOf(getEntryValue(CONFIG_CLSID)),
				getEntryValue(CONFIG_HOST), _session);

		final IJIComObject serverObject = comServer.createInstance();

		OPCServer server = new OPCServer(serverObject);

		/**
		 * 添加一个Group的信息
		 */
		OPCGroupStateMgt group = server.addGroup("test", true, 100, 1234, 60,
				0.0f, 1033);

		final OPCItemIO itemIO = server.getItemIOService();
		queryItems(itemIO, "Saw-toothed Waves.Int1");

		// clean up
		server.removeGroup(group, true);
	}

	public static void queryItems(final OPCItemIO itemIO, final String... items)
			throws JIException {
		final IORequest[] requests = new IORequest[items.length];

		for (int i = 0; i < items.length; i++) {
			requests[i] = new IORequest(items[i], 0);
		}

		itemIO.read(requests);
	}
}
