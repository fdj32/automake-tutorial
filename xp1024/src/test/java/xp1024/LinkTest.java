package xp1024;

public class LinkTest {

	public static void main(String[] args) {
		String a = "read.php?tid=824920&fpage=357";
		System.out.println(a.split("&")[0]);
		a = "read.php?tid=824920";
		System.out.println(a.split("&")[0]);
	}

}
