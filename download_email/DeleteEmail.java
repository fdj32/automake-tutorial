import java.util.Scanner;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FolderView;

public class DeleteEmail {
	
	private static final String DOMAIN = "active";
	private static String emailAddress;
	private static String domainUsername;
	private static String domainPassword;
	
	private static final String[] foldersDelete = new String[]{"ams", "amsint", "amsqa", "crucible", "dberrors", "dev", "hudson", "jenkins", "jira", "monitor", "ms_lv_int", "msprod", "mstest", "worldpay"};

	public static void main(String[] args) throws Exception {
		getParam();
		DeleteFolders();
	}
	
	private static void printParam(){
		System.out.println("Email Address  : " + emailAddress);
		System.out.println("Domain Username: " + domainUsername);
		System.out.println("Domain Password: " + domainPassword);
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
			
			System.out.println("4.Confirm with the answers (y/n):");
			printParam();
			line = sc.nextLine().trim();

			if("y".equalsIgnoreCase(line)||"yes".equalsIgnoreCase(line)) {
				confirmed = true;
			}
		}
		sc.close();
	}
	

	private static void DeleteFolders() throws Exception {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = ExchangeCredentials.getExchangeCredentialsFromNetworkCredential(domainUsername, domainPassword, DOMAIN);
		service.setCredentials(credentials);
		service.autodiscoverUrl(emailAddress);
		
	    FindFoldersResults findResults = service.findFolders(WellKnownFolderName.MsgFolderRoot, new FolderView(Integer.MAX_VALUE));
	    for (Folder folder : findResults.getFolders()) {
	    	for(String str : foldersDelete) {
		    	if(str.equals(folder.getDisplayName())) {
		    		service.emptyFolder(folder.getId(), DeleteMode.HardDelete, true);
		    		System.out.println(str + " folder has been emptied.");
		    	}
	    	}
	    }
		service.close();
	}
	
}
