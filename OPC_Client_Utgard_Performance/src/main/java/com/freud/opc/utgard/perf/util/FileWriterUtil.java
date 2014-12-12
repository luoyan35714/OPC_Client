package com.freud.opc.utgard.perf.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileWriterUtil {

	private static File file;

	private static FileOutputStream fos;
	private static BufferedWriter bw;

	public static void init(String fileName) throws FileNotFoundException {
		file = new File(fileName);
		fos = new FileOutputStream(file, true);
		bw = new BufferedWriter(new OutputStreamWriter(fos));
	}

	public static void write(String content) throws IOException {
		bw.write(content + "\r\n");
	}

	public static void finish() throws IOException {
		if (fos != null) {
			fos.close();
		}

		if (bw != null) {
			bw.close();
		}
	}
}
