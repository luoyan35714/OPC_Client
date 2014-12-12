package com.freud.opc.utgard.perf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CountAverageNumber {

	private static int i = 5;

	public static void main(String[] args) throws Exception {
		Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
		StringBuilder fileName = new StringBuilder("result/result_count_" + i
				+ ".txt");
		InputStream is = new FileInputStream(new File(fileName.toString()));
		Reader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);

		String line = null;

		int index = 0;
		int tmp = 0;
		List<Integer> list = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.contains("W-")) {
				tmp = Integer.valueOf(line.split("-")[0].replace("W", "")
						.replace("w", ""));
				if (tmp != index) {
					if (list != null) {
						map.put(index, list);
					}
					index = tmp;
					list = new ArrayList<Integer>();

				}
			} else {
				if (line != null && !line.isEmpty())
					list.add(Integer.valueOf(line.trim().split(
							"Asynch read total used")[1].replace("] s", "")
							.replace("[", "")));
			}
		}
		map.put(index, list);

		for (Entry<Integer, List<Integer>> entry : map.entrySet()) {
			if (entry.getValue().size() > 0) {
				int total = 0;
				for (int item : entry.getValue()) {
					total += item;
				}
				System.out.println(entry.getKey() + "w-average["
						+ (total / entry.getValue().size()) + "ms]");
			}
		}
		if (is != null) {
			is.close();
		}

		if (isr != null) {
			isr.close();
		}
		if (br != null) {
			br.close();
		}
	}
}
