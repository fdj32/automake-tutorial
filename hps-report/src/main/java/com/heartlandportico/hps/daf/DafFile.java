package com.heartlandportico.hps.daf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

public class DafFile {

	private static Map<String, Class> TYPES = new HashMap<String, Class>();

	static {
		TYPES.put("10", FileHeader10.class);
		TYPES.put("20", DetailHeader20.class);
		TYPES.put("51", CreditDetail51.class);
		TYPES.put("52", CreditDetail52.class);
		TYPES.put("53", CreditDetail53.class);
		TYPES.put("56", CreditAdjustmentDetail56.class);
		TYPES.put("57", PinDebitAdjustmentDetail57.class);
		TYPES.put("70", ReconciliationHeader70.class);
		TYPES.put("71", Reconciliation71.class);
		TYPES.put("73", Reconciliation73.class);
		TYPES.put("75", Reconciliation75.class);
		TYPES.put("76", Reconciliation76.class);
		TYPES.put("79", ReconciliationTrailer79.class);
		TYPES.put("80", DetailTrailer80.class);
		TYPES.put("90", FileTrailer90.class);
	}

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
		Gson gson = new Gson();
		List<DafLine> dlList = new ArrayList<DafLine>();
		Files.lines(Paths.get("/Users/nickfeng/Desktop/DAF250_FS285_20181109_123551.DAF"))
				.forEach(i -> processLine(i, dlList));
		System.out.println(gson.toJson(dlList));
	}

	private static void processLine(String s, List<DafLine> dlList) {
		if (StringUtils.isEmpty(s) || s.length() != 250)
			return;
		Gson gson = new Gson();
		String type = s.substring(8, 10);
		try {
			DafLine dl = (DafLine) (TYPES.get(type).newInstance());
			dl = dl.fromString(s);
			dlList.add(dl);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
