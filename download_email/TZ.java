import java.util.TimeZone;


public class TZ {

	public static void main(String[] args) {
		for(String str : TimeZone.getAvailableIDs()) {
			System.out.println(str);
		}
	}

}
