package com.heartlandportico.hps.fdf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

public class FdfFile {

	private static Map<String, Class> TYPES = new HashMap<String, Class>();

	static {
		TYPES.put("10", FileHeader10.class);
		TYPES.put("15", MerchantTotalFunding15.class);
		TYPES.put("20", MerchantBatchHeaderControl20.class);
		TYPES.put("30", MerchantBatchHeader30.class);
		TYPES.put("31", MerchantDeposit31.class);
		TYPES.put("32", MerchantFundedTransactionLevelFees32.class);
		TYPES.put("33", MerchantFutureFundedTransactionLevelFees33.class);
		TYPES.put("34", MerchantFundedBatchHMSActivity34.class);
		TYPES.put("35", MerchantFundedTransactionLevelFees35.class);
		TYPES.put("39", MerchantFundedBatchByCardType39.class);
		TYPES.put("40", MerchantFundedHMSActivity40.class);
		TYPES.put("41", MerchantFundedByCardType41.class);
		TYPES.put("50", MerchantFundedOtherFundingControl50.class);
		TYPES.put("51", MerchantFundedReturnedItems51.class);
		TYPES.put("52", MerchantFundedDisputes52.class);
		TYPES.put("53", MerchantFundedAdjustments53.class);
		TYPES.put("70", ChainTotalFunding70.class);
		TYPES.put("71", ChainTotalFundingByCardType71.class);
		TYPES.put("72", ChainFundedBatchHMSActivity72.class);
		TYPES.put("90", FileTrailer90.class);
	}

	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
		Gson gson = new Gson();
		List<FdfLine> flList = new ArrayList<FdfLine>();
		Files.lines(Paths.get("/Users/nickfeng/Desktop/FDF100_FS364_20181109_095606.FDF"))
				.forEach(i -> processLine(i, flList));
		System.out.println(gson.toJson(flList));
	}

	private static void processLine(String s, List<FdfLine> flList) {
		if (StringUtils.isEmpty(s) || s.length() != 350)
			return;
		Gson gson = new Gson();
		String type = s.substring(0, 2);
		try {
			FdfLine fl = (FdfLine) (TYPES.get(type).newInstance());
			fl = fl.fromString(s);
			flList.add(fl);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
