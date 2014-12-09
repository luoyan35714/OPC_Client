package com.freud.opc.utgard.cases;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.browser.Branch;
import org.openscada.opc.lib.da.browser.FlatBrowser;
import org.openscada.opc.lib.da.browser.Leaf;

import com.freud.opc.utgard.BaseConfiguration;

/**
 * 
 * According the clsid get the connection and print all the group and item
 * information
 * 
 * @author Freud
 * 
 */
public class OPCTest2 {

	public static void main(String[] args) throws Exception {

		Server server = new Server(
				BaseConfiguration.getCLSIDConnectionInfomation(),
				Executors.newSingleThreadScheduledExecutor());

		server.connect();

		/** 获得OPC连接下所有的Group和Item */
		dumpTree(server.getTreeBrowser().browse(), 0);
		/** 获得OPC下所有的Item */
		dumpFlat(server.getFlatBrowser());

		server.disconnect();
	}

	/**
	 * 遍历Flat结构的所有Item值
	 * 
	 * @param browser
	 * @throws IllegalArgumentException
	 * @throws UnknownHostException
	 * @throws JIException
	 */
	private static void dumpFlat(final FlatBrowser browser)
			throws IllegalArgumentException, UnknownHostException, JIException {
		for (String name : browser.browse()) {
			System.out.println(name);
		}
	}

	/**
	 * 遍历Tree结构
	 * 
	 * @param branch
	 * @param level
	 */
	private static void dumpTree(final Branch branch, final int level) {

		for (final Leaf leaf : branch.getLeaves()) {
			dumpLeaf(leaf, level);
		}
		for (final Branch subBranch : branch.getBranches()) {
			dumpBranch(subBranch, level);
			dumpTree(subBranch, level + 1);
		}
	}

	private static String printTab(int level) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < level; i++) {
			sb.append("\t");
		}
		return sb.toString();
	}

	/**
	 * 打印Item
	 * 
	 * @param leaf
	 */
	private static void dumpLeaf(final Leaf leaf, final int level) {
		System.out.println(printTab(level) + "Leaf: " + leaf.getName() + ":"
				+ leaf.getItemId());
	}

	/**
	 * 打印Group
	 * 
	 * @param branch
	 */
	private static void dumpBranch(final Branch branch, final int level) {
		System.out.println(printTab(level) + "Branch: " + branch.getName());
	}
}
