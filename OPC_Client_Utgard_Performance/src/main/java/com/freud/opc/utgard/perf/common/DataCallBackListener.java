package com.freud.opc.utgard.perf.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;

import com.freud.opc.utgard.perf.AsyncOPCPerfTest;

public class DataCallBackListener implements DataCallback {

	private static Logger LOGGER = Logger.getLogger(DataCallBackListener.class);

	private static List<String> items = new ArrayList<String>();

	private int size;

	public DataCallBackListener(int size) {
		this.size = size;
	}

	@Override
	public void changed(Item item, ItemState is) {
		LOGGER.info("Item:[" + item.getId() + "], Value:[" + is.getValue()
				+ "]");
		items.add(item.getId());
		if (items.size() == size) {
			long end = System.currentTimeMillis();
			LOGGER.info("EndDate[" + new Date() + "],CurrentMillis:" + end);
			LOGGER.info("Total Spend:[" + (end - AsyncOPCPerfTest.start) + "]");
			System.exit(0);
		}
	}
}
