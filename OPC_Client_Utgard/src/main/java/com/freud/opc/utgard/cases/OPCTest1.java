package com.freud.opc.utgard.cases;

import static com.freud.opc.utgard.BaseConfiguration.CONFIG_DOMAIN;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_HOST;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_PASSWORD;
import static com.freud.opc.utgard.BaseConfiguration.CONFIG_USERNAME;
import static com.freud.opc.utgard.BaseConfiguration.getEntryValue;

import java.util.Collection;

import org.openscada.opc.dcom.list.ClassDetails;
import org.openscada.opc.lib.list.Categories;
import org.openscada.opc.lib.list.Category;
import org.openscada.opc.lib.list.ServerList;

/**
 * Get all the opc connection info from Server
 * 
 * @author Freud
 * 
 */
public class OPCTest1 {

	public static void main(String[] args) throws Exception {

		ServerList serverList = new ServerList(getEntryValue(CONFIG_HOST),
				getEntryValue(CONFIG_USERNAME), getEntryValue(CONFIG_PASSWORD),
				getEntryValue(CONFIG_DOMAIN));

		/** According the progid get the clsid, then get the classdetail */
		// final String cls = serverList
		// .getClsIdFromProgId("Matrikon.OPC.Simulation.1");
		//
		// ClassDetails cd = serverList.getDetails(cls);
		//
		// if (cd != null) {
		// System.out.println(cd.getProgId() + "=" + cd.getDescription());
		// }

		Collection<ClassDetails> classDetails = serverList
				.listServersWithDetails(new Category[] {
						Categories.OPCDAServer10, Categories.OPCDAServer20,
						Categories.OPCDAServer30 }, new Category[] {});

		for (ClassDetails cds : classDetails) {
			System.out.println(cds.getProgId() + "=" + cds.getDescription());
		}

	}
}
