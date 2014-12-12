package com.freud.opc.dcom.perf;

import static com.freud.opc.utgard.perf.config.ConfigReader.CLSID;
import static com.freud.opc.utgard.perf.config.ConfigReader.DOMAIN;
import static com.freud.opc.utgard.perf.config.ConfigReader.HOST;
import static com.freud.opc.utgard.perf.config.ConfigReader.PASSWORD;
import static com.freud.opc.utgard.perf.config.ConfigReader.USERNAME;
import static com.freud.opc.utgard.perf.config.ConfigReader.getProp;

import java.util.Random;

import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIClsid;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JISession;
import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.ResultSet;
import org.openscada.opc.dcom.da.IOPCDataCallback;
import org.openscada.opc.dcom.da.OPCITEMDEF;
import org.openscada.opc.dcom.da.OPCITEMRESULT;
import org.openscada.opc.dcom.da.ValueData;
import org.openscada.opc.dcom.da.impl.OPCGroupStateMgt;
import org.openscada.opc.dcom.da.impl.OPCServer;

/**
 * 
 * @author Freud
 * 
 */
public class SyncPerfTest {

	public static void main(String[] args) throws Exception {
		JISystem.setAutoRegisteration(true);

		JISession session = JISession.createSession(getProp(DOMAIN),
				getProp(USERNAME), getProp(PASSWORD));

		final JIComServer comServer = new JIComServer(
				JIClsid.valueOf(getProp(CLSID)), getProp(HOST), session);

		final IJIComObject serverObject = comServer.createInstance();

		OPCServer server = new OPCServer(serverObject);

		OPCGroupStateMgt group = server.addGroup("", true, 1000, 1234, 0, 0.0f,
				1033);

		// group.getSyncIO().read(source, serverHandles)

		OPCITEMDEF item = new OPCITEMDEF();

		item.setActive(true);
		item.setItemID("Random.real10");
		item.setClientHandle(new Random().nextInt());

		group.attach(new IOPCDataCallback() {

			public void writeComplete(int arg0, int arg1, int arg2,
					ResultSet<Integer> arg3) {
			}

			public void readComplete(int arg0, int arg1, int arg2, int arg3,
					KeyedResultSet<Integer, ValueData> arg4) {

			}

			public void dataChange(int arg0, int arg1, int arg2, int arg3,
					KeyedResultSet<Integer, ValueData> items) {
				for (KeyedResult<Integer, ValueData> item : items) {
					System.out.println(item.getKey() + "-------"
							+ item.getValue());
				}
			}

			public void cancelComplete(int arg0, int arg1) {

			}
		});

		KeyedResultSet<OPCITEMDEF, OPCITEMRESULT> test = group
				.getItemManagement().add(item);

		System.out.println(test.size());

		Thread.sleep(10 * 1000);

	}
}
