
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;

public class GroupEmail {
	
	private static final String[][] KEY_WORD_VS_SUB_FOLDER = new String[][]{
		{"and the merchant user does not exist", "merchant"},
		{"MagStripeParseException", "MagStripeParseException"},
		{"RisTransportException", "RisTransportException"},
		{"duration exceed", "duration"},
		{"run profitstars getTransactionReport error", "profitstars_batch"},
		{"java.net.UnknownHostException: api.globalgatewaye4.firstdata.com", "firstdatae4net"},
		{"Response returned from paymentech", "paymentech_not_error"},
		{"Account not valid", "Account_not_valid"},
		{"BeanstreamGateway : Gateway exception.", "BeanstreamGateway"},
		{"Invalid Refund Request", "moneris_refund"},
		{"SocketTimeoutException invoking https://ws.firstdataglobalgateway.com/fdggwsapi/services: Read timed out", "fdggtimeout"},
		{"MSServer1_1 : process returns null response.", "null_response"},
		{"java.lang.StringIndexOutOfBoundsException: start > length()", "StringIndexOutOfBoundsException"},
		{"PTCommunication : An exception occurred while creating a socket", "PTCommunication"},
		{"FirstDataE4Gateway : Gateway exception.HTTP status code:522", "FirstDataE4Gateway522"},
		{"getTransactionID() returns null", "getTransactionID_returns_null"},
		{"String or binary data would be truncated", "data_over_length"},
		{"FirstDataE4Gateway : Gateway exception.HTTP status code:500", "FirstDataE4Gateway500"},
		{"Moneris4CANGateway : validation error", "moneris4can_validation_error"},
		{"FirstDataE4Gateway : Gateway exception.HTTP status code:502", "FirstDataE4Gateway502"},
		{"Transaction Not Completed  Timed Out", "moneris4usa"},
		{"Security Data : UsernameToken authentication failed.", "cybersource"},
		{"No Pre-auth corresponds to the store Id and order Id and transaction Id entered", "moneris_complete"}
		};
	
	private static final String WORK_DIR = "D:/Outlook/save/1020/";
	
	public static void main(String[] args) throws Exception {
		String workDir = null;
		if(null != args && 1 == args.length) {
			workDir = args[0];
		} else {
			workDir = WORK_DIR;
		}
		workDir += workDir.endsWith("/") ? "" : "/"; 
		for(String[] strs : KEY_WORD_VS_SUB_FOLDER) {
			groupEmailFiles(workDir, strs[0], strs[1]);
		}
	}
	
	public static void groupEmailFiles(String workDir, String keyword, String subfolder) throws Exception {
		File root = new File(workDir);
		File sub = new File(workDir + subfolder);
		Collection<File> all = FileUtils.listFiles(root, new String[]{"txt"}, false);
		Iterator<?> itrt = all.iterator();
		File file = null;
		String content = null;
		int count = 0;
		while(itrt.hasNext()) {
			file = (File)itrt.next();
			content = FileUtils.readFileToString(file);
			if(content.contains(keyword)) {
				FileUtils.moveToDirectory(file, sub, true);
				count++;
			}
		}
		System.out.println(workDir + subfolder + " = " + count);
	}

}
