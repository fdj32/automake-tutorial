<%@page import="com.active.services.secure.report.BatchDetailReport"%>
<%@page import="com.active.services.ms.api.SecureTransactionInfo"%>
<%@page import="com.active.services.ms.api.TransactionsReport"%>
<%@page import="com.active.services.ms.api.DailyDetailReportRequest"%>
<%@page import="com.active.services.secure.request.MerchantAccount"%>
<%@page import="com.active.services.secure.report.OnlineDetailReport"%>
<%@page import="com.active.services.secure.ccg.CCGateway"%>
<%@page import="com.active.services.secure.report.Reporter"%>
<%@page import="com.active.services.secure.ccg.CCGatewayFactory"%>
<% String sidebar = "report"; %>
<%@ include file="/includes/util.inc"%>
<%@ include file="/includes/header.inc" %>
<%@ include file="/includes/passwordReset.inc" %>

<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="com.active.services.*"%>
<%@ page import="com.active.services.secure.*"%>

<script type="text/javascript">
<!--
function checkCybersourceReport(transactionId, dateCreated, maId, maInstanceId) {
	//alert(transactionId + ',' + dateCreated + ',' + maId + ',' + maInstanceId);
	window.open('checkCybersourceReport.do?transactionId=' + transactionId
			+ '&dateCreated=' + encodeURIComponent(dateCreated)
			+ '&maId=' + maId + '&maInstanceId=' + maInstanceId);
}
//-->
</script>

<%
String action = request.getParameter("action");
int pagesInt = 0;
%>


<table border="0" cellspacing="3" cellpadding="5">
<tr>
<td>
<P><h3><bean:message key="dailyDetail.heading"/></h3></P>
</td>
</tr>
</table>



<%

  //Create interface to Credit Card service
    CreditCard cc = Factory.<CreditCard>create(CreditCard.class);
    
    DailyDetailReportRequest req1 = new DailyDetailReportRequest();

    SecureTransactionInfo resp1 = null;
    List<OnlineDetailReport> tranList2 = null;
    List<BatchDetailReport> tranList3 = null;

  	//AMS-35 add related tran info
    TransactionsReport resp_relatedTrans = null;
    List<SecureTransactionInfo> relatedTransList = null;
    

	req1.setTransactionId(request.getParameter("transaction_id"));
	req1.setUserId(ssi.getUserID());
	req1.setUserInstanceId(ssi.getUserInstanceId());
	req1.setType(sai.getType());

    resp1 = cc.transactions(req1, true).getTransactions().get(0);
    	
    String maId = resp1.getMerchantId() + "";
    String maInstanceId = resp1.getMerchantInstanceId() + "";
    SecureMerchantAccount sma = Factory.<SecureMerchantAccount>create(SecureMerchantAccount.class);
    sai = sa.getAccessInfo(ssi.getUserID(), ssi.getUserInstanceId());

    int theType = 0;
    if (sai.getType() != null){
    	theType = sai.getType().intValue();
    }
    
    List<MerchantAccount> smaList = null;
    if (theType <= 2){
    	smaList = sma.getSMAList();
    } else{
    	smaList = sma.getSMAListByUser(ssi.getUserID().intValue(), ssi.getUserInstanceId());
    }
    
	boolean found = false;
	for (MerchantAccount mai : smaList) {
		String thisID = Integer.toString(mai.getId());	
		String thisInstanceId = Integer.toString(mai.getInstanceId());
		if (thisID.equals(maId) && thisInstanceId.equals(maInstanceId)) {
			found = true;
			break;
		}
	}
	
	if (!found) {
		throw new Exception("User cannot view the transaction whose merchant account is not in his organization.");
	}
	
	String processor = resp1.getProcessor();
	CCGateway gateway = CCGatewayFactory.getCCGGateway(processor);
	if(processor != null && processor.equals("paymentech")) {
		String searchString = resp1.getCcgTransactionID();
		//System.out.println(searchString);
		
		tranList2 = cc.searchOnlinePT(searchString);
		tranList3 = cc.searchBatchPT(searchString);
    }
	
	//AMS-35 add related tran info
    String transType = null;
	resp_relatedTrans = cc.searchRelatedTrans(request.getParameter("transaction_id"));
	relatedTransList = resp_relatedTrans.getTransactions();

%>

<table border="0" cellspacing="0" cellpadding="0">
<tr bgcolor="ffffff">
	<td>
		<table border="0" cellspacing="0" cellpadding="0">
			<tr class="titleHeader">
				<td><strong>Transaction</strong></td><td>&nbsp;</td>
				<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			<tr class="results2">
				<td>Transaction ID:</td><td bgcolor="ffffff"><%=escape(resp1.getTransactionID())%></td>
				<td>Status:</td><td bgcolor="ffffff"><%=escape(resp1.getStatus())%></td>
			</tr>
			<tr class="results2">
				<td>Tender Type:</td><td bgcolor="ffffff"><%=escape(resp1.getTenderType())%></td>
				<td>Result:</td><td bgcolor="ffffff"><%=escape(resp1.getResult())%></td>
			</tr>
			<tr class="results2">
				<td>Transaction Type:</td><td bgcolor="ffffff"><%=escape(resp1.getTransactionType())%></td>
				<td>Message:</td><td bgcolor="ffffff"><%=escape(resp1.getMsg())%></td>
			</tr>
			<tr class="results2">
				<td>ID:</td><td bgcolor="ffffff"><%=resp1.getId()%>-<%=resp1.getInstanceId()%></td>
				<td>Request Server:</td><td bgcolor="ffffff"><%=escape(resp1.getRequestServer())%></td>
			</tr>
			<tr class="results2">
				<td>Merchant Account ID:</td>
				<td bgcolor="ffffff"><a href="adminMAI.do?action=edit&maiList=<%=resp1.getMerchantId()%>-<%=resp1.getMerchantInstanceId()%>"><%=resp1.getMerchantId()%>-<%=resp1.getMerchantInstanceId()%></a></td>
				<td>Client Transaction ID:</td><td bgcolor="ffffff"><%=escape(resp1.getClientTransactionID())%></td>
			</tr>
			<tr class="results2">
				<td>Deposit Status:</td><td bgcolor="ffffff"><%=escape(resp1.getDepositStatus())%></td>
				<td>Merchant Descriptor:</td><td bgcolor="ffffff"><%=escape(resp1.getMerchantDescriptor())%></td>
			</tr>
			<tr class="results2">
				<td>Application Agency ID:</td><td bgcolor="ffffff"><%=escape(resp1.getApplicationAgencyID())%></td>
				<td>Application Agency Name:</td><td bgcolor="ffffff"><%=escape(resp1.getApplicationAgencyName())%></td>
			</tr>
			<tr class="results2">
				<td>Customer IP:</td><td bgcolor="ffffff"><%=escape(resp1.getCustomerIP())%></td>
				<td bgcolor="ffffff"></td><td bgcolor="ffffff"></td>
			</tr>
			<tr class="titleHeader">
				<td><strong>Order</strong></td><td>&nbsp;</td>
				<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			<tr class="results2">
				<td>Account Owner:</td><td bgcolor="ffffff"><%=escape(resp1.getCCholder())%></td>
				<td>Account Number:</td><td bgcolor="ffffff"><%=escape(resp1.getCCNumber())%></td>
			</tr>
			<tr class="results2">
				<td>Amount:</td><td bgcolor="ffffff"><%=MoneyUtil.format(escape(resp1.getAmount()))%></td>
				<td>Date:</td><td bgcolor="ffffff"><%=escape(resp1.getDateCreated())%></td>
			</tr>
			<tr class="results2">
				<td>Order ID:</td><td bgcolor="ffffff"><%=escape(resp1.getOrderID())%></td>
				<td>Order Descriptor:</td><td bgcolor="ffffff"><%=escape(resp1.getOrderDescriptor())%></td>
			</tr>
			<tr class="titleHeader">
				<td><strong>Gateway</strong></td><td>&nbsp;</td>
				<td>&nbsp;</td><td>&nbsp;</td>
			</tr>			
			<tr class="results2">
				<td>Transaction ID:</td><td bgcolor="ffffff"><%=escape(resp1.getCcgTransactionID())%></td>
				<td>AVS Address:</td><td bgcolor="ffffff"><%=escape(resp1.getCcgAVSAddr())%></td>
			</tr>
			<tr class="results2">
				<td>Result Code:</td><td bgcolor="ffffff"><%=escape(resp1.getCcgResultCode())%></td>
				<td>AVS ZIP:</td><td bgcolor="ffffff"><%=escape(resp1.getCcgAVSZIP())%></td>
			</tr>
			<tr class="results2">
				<td>ccgAuthCode:</td><td bgcolor="ffffff"><%=escape(resp1.getCcgAuthCode())%></td>
				<td>CSC:</td><td bgcolor="ffffff"><%=escape(resp1.getCcgCSC())%></td>
			</tr>
			<tr class="results2">
				<td>Processor:</td><td bgcolor="ffffff"><%=escape(resp1.getProcessor())%></td>
				<td>CCGAffluentCategory:</td><td bgcolor="ffffff"><%=escape((null != resp1.getCcgCTI01() && 15 == resp1.getCcgCTI01().length()) ? resp1.getCcgCTI01().substring(12, 13) : "")%></td>
			</tr>
			<%
				if("cybersource".equals(resp1.getProcessor()) && !"0".equals(resp1.getResult())) {
			%>
			<tr class="titleHeader">
				<td><strong>TroubleShooting</strong></td><td>&nbsp;</td>
				<td>&nbsp;</td><td>&nbsp;</td>
			</tr>
			<tr class="results2">
				<td><button onclick="checkCybersourceReport('<%=resp1.getTransactionID()%>', '<%=resp1.getDateCreated()%>', '<%=resp1.getMerchantId()%>', '<%=resp1.getMerchantInstanceId()%>')">Check Cybersource Report</button></td>
				<td id="cybersourceReport" bgcolor="ffffff">&nbsp;</td>
			</tr>
			<%
				}
			%>
		</table>
	</td>
</tr>
</table>
<%
	if(processor != null && processor.equals("paymentech")) {
%>
<br>
<br>
<table border="0" cellpadding="2" cellspacing="2">
<tr height="30" class="titleHeader">
  <td colspan="10" align="center">
  <FONT size=4><STRONG><bean:message key="dailyDetail.message1"/></STRONG></FONT>
  </td>
</tr>
<tr height="23" class="titleHeader">
  <td><font><b>ID</b></font></td>
  <td><font><b>Paymentech ID</b></font></td>
  <td><font><b>Amount</b></font></td>
  <td><font><b>Type</b></font></td>
  <td><font><b>Date</b></font></td>
  <td><font><b>Action</b></font></td>
  <td><font><b>Result</b></font></td>
  <td><font><b>Auth Code</b></font></td>
  <td><font><b>AVS Code</b></font></td>
  <td><font><b>Updated Date</b></font></td>
</tr>

<%
	for (OnlineDetailReport tran : tranList2) {
%>

<tr class="results2">
  <td><%=tran.getId()%>-<%=tran.getInstanceId()%></td>
  <td><%=escape(tran.getTransactionIdentifier())%></td>
  <td class="money"><%=MoneyUtil.format(escape(tran.getAmount()))%></td>
  <td><%=escape(tran.getType())%></td>
  <td><%=escape(tran.getCreatedDate())%></td>
  <td><%=escape(tran.getAction())%></td>
  <td><%=escape(tran.getResultCode())%></td>
  <td><%=escape(tran.getAuthCode())%></td>
  <td><%=escape(tran.getAvsCode())%></td>
  <td><%=escape(tran.getUpdatedDate())%></td>
</td>

<%
	}
%>
</table>
<br>
<br>
<table border="0" cellpadding="2" cellspacing="2">
<tr height="30" class="titleHeader">
  <td colspan="10" align="center">
  <FONT size=4><STRONG><bean:message key="dailyDetail.message2"/></STRONG></FONT>
  </td>
</tr>
<tr height="23" class="titleHeader">
  <td><font><b>ID</b></font></td>
  <td><font><b>Paymentech ID</b></font></td>
  <td><font><b>Batch ID</b></font></td>
  <td><font><b>Amount</b></font></td>
  <td><font><b>Status</b></font></td>
  <td><font><b>Date</b></font></td>
  <td><font><b>Action</b></font></td>
  <td><font><b>Response</b></font></td>
  <td><font><b>Deposit</b></font></td>
  <td><font><b>Updated Date</b></font></td>
</tr>

<%
	for (BatchDetailReport tran : tranList3) {
%>

<tr class="results2">
  <td><%=tran.getId()%>-<%=tran.getInstanceId()%></td>
  <td><%=escape(tran.getOrderId())%></td>
  <%
  	if (tran.getBatchId() != 0) {
  %>
  <td><A href="batchDetails.do?processor=paymentech&batch_id=<%=tran.getBatchId()%>&batchInstanceId=<%=tran.getBatchInstanceId()%>"><%=tran.getBatchId()%>-<%=tran.getBatchInstanceId()%></a></td>
  <%
  	} else {
  %>
  <td>-</td>
  <%
  	}
  %>
  <td class="money"><%=MoneyUtil.format(escape(tran.getAmount()))%></td>
  <td><%=escape(tran.getStatus())%></td>
  <td><%=escape(tran.getCreatedDate())%></td>
  <td><%=escape(tran.getAction())%></td>
  <td><%=escape(tran.getResponseCode())%></td>
  <td><%=escape(tran.getDepositFlag())%></td>
  <td><%=escape(tran.getUpdatedDate())%></td>
</td>

<%
	}
%>
</table>
<br>
<%
	} // processor == paymentech
else if (gateway != null && gateway instanceof Reporter) { 
	String transactionId = request.getParameter("transaction_id");
%>
<jsp:include page='<%=processor + "/detail.jsp"%>' flush="true">
	<jsp:param value="<%=transactionId%>" name="transaction_id"/>
</jsp:include>
<%
	}
%>

<br>
<table border="0" cellpadding="2" cellspacing="2">
<tr height="30" class="titleHeader">
  <td colspan="10" align="center">
  <FONT size=4><STRONG><bean:message key="dailyDetail.message3"/></STRONG></FONT>
  </td>
</tr>
<tr height="23" class="titleHeader">
  <td><font><b>Transaction ID</b></font></td>
  <td><font><b>Time</b></font></td>
  <td><font><b>Type</b></font></td>
  <td><font><b>Cardholder</b></font></td>
  <td><font><b>CC</b></font></td>
  <td><font><b>Amount</b></font></td>
  <td><font><b>Result</b></font></td>
  <td><font><b>Response Msg</b></font></td>
  <td><font><b>OrderID</b></font></td>
  <td><font><b>Merchant Desc.</b></font></td>
</tr>

<%
	//AMS-35: related transactions
	if (relatedTransList != null)
	for (SecureTransactionInfo tran: relatedTransList) {
%>

<tr class="results2">
  <td><a href="dailyDetail.do?transaction_id=<%=escape(tran.getTransactionID())%>"><%=escape(tran.getTransactionID())%></a></td>
  <td><%=escape(tran.getTime())%></td>
  <td><%=escape(tran.getTransactionType())%></td>
  <td><%=escape(tran.getCCholder())%></td>
  <td><%=escape(tran.getCCNumber())%></td>
  <td class="money"><%=MoneyUtil.format(escape(tran.getAmount()))%></td>
  <td><%=escape(tran.getResult())%></td>
  <td><%=escape(tran.getMsg())%></td>
  <td><%=escape(tran.getOrderID())%></td>
  <td><%=escape(tran.getMerchantDescriptor())%></td>
</td>

<%
  }
%>
</table>
<!--  end of related transactions -->
<br>

<%@ include file="/includes/footer.inc" %>
