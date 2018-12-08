package com.heartlandportico.hps.daf;

public abstract class DafLine {

	/**
	 * 1-10, Refer to Control Section – Record Type = '56'
	 */
	private ControlSection cs;

	public ControlSection getCs() {
		return cs;
	}

	public void setCs(ControlSection cs) {
		this.cs = cs;
	}

	public abstract DafLine fromString(String s);

}
