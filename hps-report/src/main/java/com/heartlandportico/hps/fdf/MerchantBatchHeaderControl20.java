package com.heartlandportico.hps.fdf;

import org.apache.commons.lang.StringUtils;

public class MerchantBatchHeaderControl20 extends FdfLine {

	/**
	 * 1-2, '20'
	 */
//	private String recordType;

	/**
	 * 3-8, Sequential number of the record within file. Incremented by 1 for each
	 * record.
	 */
	private String recordSequenceNumber;

	/**
	 * 9-14, Total number of merchant batches reported
	 */
	private String totalMerchantBatches;

	/* Filler, 15-350, AN 336, Space Filled. */

//	public String getRecordType() {
//		return recordType;
//	}
//
//	public void setRecordType(String recordType) {
//		this.recordType = recordType;
//	}

	public String getRecordSequenceNumber() {
		return recordSequenceNumber;
	}

	public void setRecordSequenceNumber(String recordSequenceNumber) {
		this.recordSequenceNumber = recordSequenceNumber;
	}

	public String getTotalMerchantBatches() {
		return totalMerchantBatches;
	}

	public void setTotalMerchantBatches(String totalMerchantBatches) {
		this.totalMerchantBatches = totalMerchantBatches;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getRecordType());
		sb.append(recordSequenceNumber);
		sb.append(totalMerchantBatches);
		sb.append(StringUtils.repeat(" ", 336));
		return sb.toString();
	}

	@Override
	public FdfLine fromString(String s) {
		if (StringUtils.isEmpty(s) || s.length() != 350) {
			return null;
		}
		MerchantBatchHeaderControl20 o = new MerchantBatchHeaderControl20();
		o.setRecordType(s.substring(0, 2));
		o.setRecordSequenceNumber(s.substring(2, 8));
		o.setTotalMerchantBatches(s.substring(8, 14));
		return o;
	}

}
