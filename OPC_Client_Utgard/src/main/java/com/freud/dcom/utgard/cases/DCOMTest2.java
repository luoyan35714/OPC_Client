package com.freud.dcom.utgard.cases;

import static com.freud.opc.utgard.BaseConfiguration.CONFIG_CLSID;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_DOMAIN;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_HOST;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_PASSWORD;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_USERNAME;
import static com.freud.opc.utgard.BaseConfiguration.getEntryValue;

import java.net.UnknownHostException;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIClsid;
import org.jinterop.dcom.core.JIComServer;
import org.jinterop.dcom.core.JISession;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.dcom.da.OPCBROWSEDIRECTION;
import org.openscada.opc.dcom.da.OPCBROWSETYPE;
import org.openscada.opc.dcom.da.OPCNAMESPACETYPE;
import org.openscada.opc.dcom.da.impl.OPCBrowseServerAddressSpace;
import org.openscada.opc.dcom.da.impl.OPCServer;

/**
 * 遍历OPC连接下的所有Group和Item
 * 
 * @author Freud
 * 
 */
public class DCOMTest2 {

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
		final OPCBrowseServerAddressSpace serverBrowser = server.getBrowser();

		/**
		 * Flat形式获取所有Item信息
		 */
		browseFlat(serverBrowser);

		/**
		 * 获取所有的Group和Item信息
		 */
		browseTree(serverBrowser);

	}

	/**
	 * Tree形式获取所有Group和Item的信息
	 */
	private static void browseTree(final OPCBrowseServerAddressSpace browser)
			throws IllegalArgumentException, UnknownHostException, JIException {
		System.out.println("Showing hierarchial address space");
		System.out.println(String.format("Organization: %s",
				browser.queryOrganization()));

		if (!browser.queryOrganization().equals(
				OPCNAMESPACETYPE.OPC_NS_HIERARCHIAL)) {
			return;
		}

		browser.changePosition(null, OPCBROWSEDIRECTION.OPC_BROWSE_TO);
		browseTree(browser, 0);
	}

	private static String printTab(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	private static void browseTree(final OPCBrowseServerAddressSpace browser,
			final int level) throws JIException, IllegalArgumentException,
			UnknownHostException {

		for (final String item : browser.browse(OPCBROWSETYPE.OPC_LEAF, "", 0,
				JIVariant.VT_EMPTY).asCollection()) {

			System.out.println(printTab(level) + "Leaf: " + item + "\tName: "
					+ browser.getItemID(item));
		}

		for (final String item : browser.browse(OPCBROWSETYPE.OPC_BRANCH, "",
				0, JIVariant.VT_EMPTY).asCollection()) {
			System.out.println(printTab(level) + "Branch: " + item);
			browser.changePosition(item, OPCBROWSEDIRECTION.OPC_BROWSE_DOWN);
			browseTree(browser, level + 1);
			browser.changePosition(null, OPCBROWSEDIRECTION.OPC_BROWSE_UP);
		}
	}

	/**
	 * Flat形式获取Item的信息
	 */
	private static void browseFlat(final OPCBrowseServerAddressSpace browser)
			throws JIException, IllegalArgumentException, UnknownHostException {

		System.out.println(String.format("Organization: %s",
				browser.queryOrganization()));

		browser.changePosition(null, OPCBROWSEDIRECTION.OPC_BROWSE_TO);

		System.out.println("Showing flat address space");
		for (final String id : browser.browse(OPCBROWSETYPE.OPC_FLAT, "", 0,
				JIVariant.VT_EMPTY).asCollection()) {
			System.out.println("Item: " + id);
		}
	}

}
