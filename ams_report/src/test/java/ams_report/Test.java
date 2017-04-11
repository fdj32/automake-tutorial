package ams_report;

import org.apache.shiro.crypto.hash.Sha256Hash;

public class Test {

	public static void main(String[] args) {
		System.out.println(new Sha256Hash("admin").toHex());
	}

}
