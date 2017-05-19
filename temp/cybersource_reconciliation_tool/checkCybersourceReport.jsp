<%@ page import="com.active.dao.DAOException"%>
<%@ page import="com.active.dao.GatewayDAO"%>
<%@ page import="com.active.services.secure.MSInvalidMerchantLoginException"%>
<%@ page import="com.active.services.secure.request.MerchantAccount"%>
<%@ page import="com.active.services.secure.processor.comm.HttpException"%>
<%@ page import="com.active.services.secure.processor.cybersource.reconciliation.Report"%>
<%@ page import="com.active.services.secure.processor.cybersource.reconciliation.SingleTransactionReportClient"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.ParseException"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="java.util.Date"%>

<html>
<head>
<title>Check Cybersource Report</title>
<style type="text/css">
    pre {outline: 1px solid #ccc; padding: 5px; margin: 5px; }
    .string { color: green; }
    .number { color: darkorange; }
    .boolean { color: blue; }
    .null { color: magenta; }
    .key { color: red; }
</style>
<script type="text/javascript">

	function syntaxHighlight(json) {
	    if (typeof json != 'string') {
	        json = JSON.stringify(json, undefined, 2);
	    }
	    json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
	    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
	        var cls = 'number';
	        if (/^"/.test(match)) {
	            if (/:$/.test(match)) {
	                cls = 'key';
	            } else {
	                cls = 'string';
	            }
	        } else if (/true|false/.test(match)) {
	            cls = 'boolean';
	        } else if (/null/.test(match)) {
	            cls = 'null';
	        }
	        return '<span class="' + cls + '">' + match + '</span>';
	    });
	}
</script>
</head>
<body onload="display()">
<%
	String transactionId = request.getParameter("transactionId");
	String dateCreated = request.getParameter("dateCreated");
	String maId = request.getParameter("maId");
	String maInstanceId = request.getParameter("maInstanceId");
	
	MerchantAccount ma = null;
	try {
		int id = Integer.parseInt(maId);
		int instanceId = Integer.parseInt(maInstanceId);
		ma = GatewayDAO.getInstance().getMerchantAccount(
				GatewayDAO.LookupByID, id, instanceId, null, null);
	} catch (NumberFormatException e) {
		out.print("maId/maInstanceId is not valid integer");
		return;
	} catch (MSInvalidMerchantLoginException e) {
		out.print("merchant account not found");
		return;
	} catch (DAOException e) {
		out.print("db access error");
		return;
	}
	
	if(null == ma) {
		out.print("merchant account is null");
		return;
	}
	
	String merchantID = ma.getLogin("login1");
	String username = ma.getLogin("login2");
	String password = ma.getLogin("login3");
	
	SimpleDateFormat sdfSrc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	SimpleDateFormat sdfDest = new SimpleDateFormat("yyyyMMdd");
	Date date = null;
	try {
		date = sdfSrc.parse(dateCreated);
	} catch (ParseException e) {
		out.print("dateCreated is not like yyyy-MM-dd HH:mm:ss.S");
		return;
	}
	
	Report report = null;
	try {
		report = SingleTransactionReportClient.getReport(merchantID, username, password, transactionId, sdfDest.format(date));
		if(null == report || 0 == report.getRequests().size()) {
			Calendar yesterday = Calendar.getInstance();
			yesterday.setTime(date);
			yesterday.add(Calendar.DATE, -1);
			report = SingleTransactionReportClient.getReport(merchantID, username, password, transactionId, sdfDest.format(yesterday.getTime()));
		}
		if(null == report || 0 == report.getRequests().size()) {
			Calendar tomorrow = Calendar.getInstance();
			tomorrow.setTime(date);
			tomorrow.add(Calendar.DATE, 1);
			report = SingleTransactionReportClient.getReport(merchantID, username, password, transactionId, sdfDest.format(tomorrow.getTime()));
		}
	} catch (HttpException e) {
		out.print("HttpException.getStatusCode=" + e.getStatusCode());
		return;
	}
	String reportJson = null;
	if(null != report) {
		reportJson = new Gson().toJson(report);
		out.print("<script type=\"text/javascript\">"
				+ "function display() {"
				+ "var jsonStr = \"" + reportJson.replaceAll("\"", "\\\\\"") + "\";"
				+ "document.getElementById(\"reportJson\").innerHTML = \"<pre>\" + syntaxHighlight(JSON.parse(jsonStr)) + \"</pre>\";"
				+ "}</script>");
	}
%>
<div id="reportJson"></div>
</body>
</html>


