package com.freud.dcom.utgard.cases;

import static com.freud.opc.utgard.BaseConfiguration.CONFIG_CLSID;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_DOMAIN;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_HOST;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_PASSWORD;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_USERNAME;
import static com.freud.opc.utgard.BaseConfiguration.getEntryValue;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIClsid;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.da.PropertyDescription;
import org.openscada.opc.dcom.da.impl.OPCGroupStateMgt;
import org.openscada.opc.dcom.da.impl.OPCItemProperties;
import org.openscada.opc.dcom.da.impl.OPCServer;

/**
 * 获取指定Item的Properties
 * 
 * @author Freud
 * 
 */
public class DCOMTest5 {

	private static Logger LOGGER = Logger.getLogger(DCOMTest5.class);

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

		final OPCItemProperties itemProperties = server
				.getItemPropertiesService();
		dumpItemProperties(itemProperties, "Saw-toothed Waves.Int");

		// clean up
		server.removeGroup(group, true);
		LOGGER.info("test");
	}

	public static void dumpItemProperties(
			final OPCItemProperties itemProperties, final String itemID)
			throws JIException {
		final Collection<PropertyDescription> properties = itemProperties
				.queryAvailableProperties(itemID);
		final int[] ids = new int[properties.size()];

		System.out.println(String.format("Item Properties for '%s' (count:%d)",
				itemID, properties.size()));

		int i = 0;
		for (final PropertyDescription pd : properties) {
			ids[i] = pd.getId();
			System.out.println("ID: " + pd.getId());
			System.out.println("Description: " + pd.getDescription());
			System.out.println("Variable Type: " + pd.getVarType());
			i++;
		}

		System.out.println("Lookup");
		dumpItemPropertiesLookup(itemProperties, itemID, ids);

		System.out.println("Query");
		dumpItemProperties2(itemProperties, itemID, ids);
	}

	public static void dumpItemProperties2(
			final OPCItemProperties itemProperties, final String itemID,
			final int... ids) throws JIException {
		final KeyedResultSet<Integer, JIVariant> values = itemProperties
				.getItemProperties(itemID, ids);
		for (final KeyedResult<Integer, JIVariant> entry : values) {
			System.out.println(String.format(
					"ID: %d, Value: %s, Error Code: %08x", entry.getKey(),
					entry.getValue().toString(), entry.getErrorCode()));
		}
	}

	public static void dumpItemPropertiesLookup(
			final OPCItemProperties itemProperties, final String itemID,
			final int... ids) throws JIException {
		final KeyedResultSet<Integer, String> values = itemProperties
				.lookupItemIDs(itemID, ids);
		for (final KeyedResult<Integer, String> entry : values) {
			System.out.println(String.format(
					"ID: %d, Item ID: %s, Error Code: %08x", entry.getKey(),
					entry.getValue(), entry.getErrorCode()));
		}
	}
}
