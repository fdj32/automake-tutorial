import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


public class TZ {

	public static void main(String[] args) {
		for(String str : TimeZone.getAvailableIDs()) {
			System.out.println(str);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c6 = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT-6"));
		System.out.println("Etc/GMT-6:" + sdf.format(c6.getTime()));
		Calendar c8 = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT-8"));
		System.out.println("Etc/GMT-8:" + sdf.format(c8.getTime()));
	}

}
