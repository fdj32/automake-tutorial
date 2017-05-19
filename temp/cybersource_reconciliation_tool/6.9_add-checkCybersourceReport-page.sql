-- Add checkCybersourceReport.jsp page in access_pages

SET XACT_ABORT ON
BEGIN TRANSACTION

DECLARE @ap_id INT
IF NOT EXISTS (SELECT 1 FROM access_pages WHERE ap_name='checkCybersourceReport')
BEGIN
	INSERT INTO access_pages(ap_name, ap_link, ap_level) VALUES('checkCybersourceReport','checkCybersourceReport.do', 1100)
	SELECT @ap_id=SCOPE_IDENTITY()
	INSERT INTO access_types_access_pages(atap_frn_at_id, atap_frn_ap_id) VALUES(1, @ap_id)
	INSERT INTO access_types_access_pages(atap_frn_at_id, atap_frn_ap_id) VALUES(2, @ap_id)
	INSERT INTO access_types_access_pages(atap_frn_at_id, atap_frn_ap_id) VALUES(3, @ap_id)
	
	INSERT INTO access_pages(ap_name, ap_link, ap_level) VALUES('checkCybersourceReport Page','checkCybersourceReport.jsp', 1100)
    SELECT @ap_id=SCOPE_IDENTITY()
    INSERT INTO access_types_access_pages(atap_frn_at_id, atap_frn_ap_id) VALUES(1, @ap_id)
    INSERT INTO access_types_access_pages(atap_frn_at_id, atap_frn_ap_id) VALUES(2, @ap_id)
    INSERT INTO access_types_access_pages(atap_frn_at_id, atap_frn_ap_id) VALUES(3, @ap_id)
END

GO

COMMIT TRANSACTION