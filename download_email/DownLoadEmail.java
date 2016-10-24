import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.LogicalOperator;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.core.service.schema.EmailMessageSchema;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.ItemView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;

public class DownLoadEmail {
	
	private static final String DOMAIN = "active";
	private static final char[] CHAR_SEED = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	private static String emailAddress;
	private static String domainUsername;
	private static String domainPassword;
	private static String downloadFolder;
	private static String emailFolder;
	private static String date;
	private static int totalAlerts = 0;
	private static int totalErrors = 0;
	
	public static void main(String[] args) throws Exception {
		getParam();
		downloadEmail();
	}
	
	private static void printParam(){
		System.out.println("Email Address  : " + emailAddress);
		System.out.println("Domain Username: " + domainUsername);
		System.out.println("Domain Password: " + domainPassword);
		System.out.println("Download Folder: " + downloadFolder);
		System.out.println("Emain Folder   : " + emailFolder);
		System.out.println("Date [yyyyMMdd]: " + date);
	}
	
	private static void getParam(){
		boolean confirmed = false;
		Scanner sc = null;
		while(!confirmed) {
			System.out.println("Please answer following questions to set up parameters to run this program.");
			System.out.println("1.What's your email Address xxx.xxx@activenetwork.com ?");
			sc = new Scanner(System.in);
			String line = sc.nextLine().trim();
			if(line.endsWith("@activenetwork.com")) {
				emailAddress = line;
			} else {
				emailAddress = line + "@activenetwork.com";
			}
			System.out.println("2.What's your domain username ?");
			line = sc.nextLine();
			domainUsername = line;
			
			System.out.println("3.What's your domain password ?");
			line = sc.nextLine().trim();
			domainPassword = line;
			//char[] password = System.console().readPassword("3.What's your domain password: "); 
			//domainPassword = new String(password);
			
			System.out.println("4.What's the absolute path of the folder you want to download emails ?");
			line = sc.nextLine().trim();
			downloadFolder = line.endsWith("/") ? line : line + "/";
			
			System.out.println("5.What's the name of the email folder you want to download ?");
			line = sc.nextLine().trim();
			emailFolder = line;
			
			System.out.println("6.Date [yyyyMMdd] ?");
			line = sc.nextLine().trim();
			date = line;
			
			System.out.println("7.Confirm with the answers (y/n):");
			printParam();
			line = sc.nextLine().trim();

			if("y".equalsIgnoreCase(line)||"yes".equalsIgnoreCase(line)) {
				confirmed = true;
			}
		}
		sc.close();
	}
	
	private static void downloadEmail() throws Exception {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = ExchangeCredentials.getExchangeCredentialsFromNetworkCredential(domainUsername, domainPassword, DOMAIN);
		service.setCredentials(credentials);
		service.autodiscoverUrl(emailAddress);
		
		Folder folder = findFolder(service, emailFolder);
		
		for(int i = 0; i < 24; i++) {
			downloadByHour(service, folder, i);
		}
		System.out.println("Total Alerts: " + totalAlerts + " total download Errors: " + totalErrors);
		service.close();
	}
	
	private static void downloadByHour(ExchangeService service, Folder folder, int hour) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		SearchFilter sf = searchFilterWithInOneHour(hour);
		FindItemsResults<Item> findResults = service.findItems(folder.getId(), sf, new ItemView(10000));
		int count = findResults.getTotalCount();
		totalAlerts += count;
		System.out.println("There are alerts: " + count);
		String fileName = null;
		int errors = 0;
		for(Item item : findResults) {
			try {
				EmailMessage message = EmailMessage.bind(service, item.getId());
				fileName = sdf.format(message.getDateTimeSent()) + CHAR_SEED[RandomUtils.nextInt(0, 51)] + ".txt";
				FileUtils.writeStringToFile(new File(downloadFolder + fileName), MessageBody.getStringFromMessageBody(message.getBody()));
			} catch (Exception e) {
				errors++;
				continue;
			}
		}
		totalErrors += errors;
		System.out.println("There are download errors: " + errors + " in total: " + count);
	}
	
	private static SearchFilter searchFilterWithInOneHour(int hour) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date day = sdf.parse(date);
		Calendar cBgn = Calendar.getInstance();
		cBgn.setTime(day);
		cBgn.set(Calendar.HOUR_OF_DAY, hour);
		Calendar cEnd = (Calendar)(cBgn.clone());
		cEnd.add(Calendar.HOUR, 1);
		System.out.println("Start timestamp : " + full.format(cBgn.getTime()));
		System.out.println("End timestamp   : " + full.format(cEnd.getTime()));
		SearchFilter.IsGreaterThanOrEqualTo dateBgnFilter = new SearchFilter.IsGreaterThanOrEqualTo(EmailMessageSchema.DateTimeSent, cBgn.getTime());
		SearchFilter.IsLessThanOrEqualTo dateEndFilter = new SearchFilter.IsLessThanOrEqualTo(EmailMessageSchema.DateTimeSent, cEnd.getTime());
		SearchFilter.SearchFilterCollection filters = new SearchFilter.SearchFilterCollection(LogicalOperator.And, dateBgnFilter, dateEndFilter);
		return filters;
	}
	
	public static Folder findFolder(ExchangeService service, String displayName) throws Exception {
	    FindFoldersResults findResults = service.findFolders(WellKnownFolderName.MsgFolderRoot, new FolderView(Integer.MAX_VALUE));
	    for (Folder folder : findResults.getFolders()) {
	    	if(displayName.equals(folder.getDisplayName())) {
	    		return folder;
	    	}
	    }
	    return null;
	}

}
