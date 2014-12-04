package com.freud.dcom.utgard.cases;

import org.openscada.opc.dcom.common.KeyedResult;
import org.openscada.opc.dcom.common.KeyedResultSet;
import org.openscada.opc.dcom.common.ResultSet;
import org.openscada.opc.dcom.da.IOPCDataCallback;
import org.openscada.opc.dcom.da.ValueData;

public class DumpDataCallback implements IOPCDataCallback {

	public void cancelComplete(final int transactionId,
			final int serverGroupHandle) {
		System.out.println(String.format("cancelComplete: %08X, Group: %08X",
				transactionId, serverGroupHandle));
	}

	public void dataChange(final int transactionId,
			final int serverGroupHandle, final int masterQuality,
			final int masterErrorCode,
			final KeyedResultSet<Integer, ValueData> result) {

		System.out.println(String.format(
				"dataChange: %d, Group: %08X, MasterQ: %d, Error: %d",
				transactionId, serverGroupHandle, masterQuality,
				masterErrorCode));

		for (final KeyedResult<Integer, ValueData> entry : result) {
			System.out.println(String.format(
					"%08X - Error: %08X, Quality: %d, %Tc - %s",
					entry.getKey(), entry.getErrorCode(), entry.getValue()
							.getQuality(), entry.getValue().getTimestamp(),
					entry.getValue().getValue().toString()));
		}

	}

	public void readComplete(final int transactionId,
			final int serverGroupHandle, final int masterQuality,
			final int masterErrorCode,
			final KeyedResultSet<Integer, ValueData> result) {
		System.out.println(String.format(
				"readComplete: %d, Group: %08X, MasterQ: %d, Error: %d",
				transactionId, serverGroupHandle, masterQuality,
				masterErrorCode));

		for (final KeyedResult<Integer, ValueData> entry : result) {
			System.out.println(String.format(
					"%08X - Error: %08X, Quality: %d, %Tc - %s",
					entry.getKey(), entry.getErrorCode(), entry.getValue()
							.getQuality(), entry.getValue().getTimestamp(),
					entry.getValue().getValue().toString()));
		}
	}

	public void writeComplete(final int transactionId,
			final int serverGroupHandle, final int masterErrorCode,
			final ResultSet<Integer> result) {
		// TODO Auto-generated method stub
	}
}
