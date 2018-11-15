package com.heartlandportico.hps.daf;

import org.apache.commons.lang.StringUtils;

public class ReconciliationHeader70 extends DafLine {

	/**
	 * 1-10, Refer to Control Section – Record Type = '70'
	 */
//	private ControlSection cs;

	/**
	 * 11-16, ‘RECON’
	 */
	private String batchIdentifier;

	/* Filler, 17-250, AN 234, Space Filled. */

	public String getBatchIdentifier() {
		return batchIdentifier;
	}

	public void setBatchIdentifier(String batchIdentifier) {
		this.batchIdentifier = batchIdentifier;
	}

//	public ControlSection getCs() {
//		return cs;
//	}
//
//	public void setCs(ControlSection cs) {
//		this.cs = cs;
//	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getCs().toString());
		sb.append(batchIdentifier);
		sb.append(StringUtils.repeat(" ", 234));
		return sb.toString();
	}

	@Override
	public DafLine fromString(String s) {
		if (StringUtils.isEmpty(s) || s.length() != 250) {
			return null;
		}
		ReconciliationHeader70 o = new ReconciliationHeader70();
		o.setCs(ControlSection.fromString(s.substring(0, 10)));
		o.setBatchIdentifier(s.substring(10, 16));
		return o;
	}

}
