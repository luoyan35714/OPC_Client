package com.freud.opc.utgard.cases;

import java.util.concurrent.Executors;

import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;

import com.freud.opc.utgard.BaseConfiguration;

/**
 * 同步Access，读取某个点的变化值
 * 
 * @author Freud
 * 
 */
public class OPCTest4 {

	/** 间隔时间 */
	private static final int PERIOD = 100;

	/** 休眠时间 */
	private static final int SLEEP = 2000;

	public static void main(String[] args) throws Exception {

		Server server = new Server(
				BaseConfiguration.getCLSIDConnectionInfomation(),
				Executors.newSingleThreadScheduledExecutor());

		server.connect();

		/**
		 * 其中100单位为毫秒，为每次从OPC获取刷新的间隔时间
		 */
		AccessBase access = new SyncAccess(server, PERIOD);

		/**
		 * 定时每隔间隔时间获取一次值
		 */
		access.addItem("Random.Real5", new DataCallback() {
			private int i;

			public void changed(Item item, ItemState itemstate) {
				System.out.println("[" + (++i) + "],ItemName:[" + item.getId()
						+ "],value:" + itemstate.getValue());
			}
		});

		/** 开始监听 */
		access.bind();

		/** 当前线程休眠时间单位：毫秒 */
		Thread.sleep(SLEEP);
		/** 监听 结束 */
		access.unbind();

		server.dispose();
	}
}
