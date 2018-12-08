package com.heartlandportico.hps.fdf;

public abstract class FdfLine {

	/**
	 * 1-2
	 */
	private String recordType;

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public abstract FdfLine fromString(String s);

}
