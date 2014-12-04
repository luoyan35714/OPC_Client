package com.freud.opc.utgard.cases;

import java.util.concurrent.Executors;

import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.Async20Access;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;

import com.freud.opc.utgard.BaseConfiguration;

/**
 * 异步Access，订阅读取点位的值,并且只有值有变化的时候才会触发CallBack函数
 * 
 * @author Freud
 * 
 */
public class OPCTest5 {

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
		AccessBase access = new Async20Access(server, PERIOD, false);

		/**
		 * 只有Item的值有变化的时候才会触发CallBack函数
		 */
		access.addItem("Random.Real5", new DataCallback() {

			private int count;

			@Override
			public void changed(Item item, ItemState itemstate) {
				System.out.println("[" + (++count) + "],ItemName:["
						+ item.getId() + "],value:" + itemstate.getValue());
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
