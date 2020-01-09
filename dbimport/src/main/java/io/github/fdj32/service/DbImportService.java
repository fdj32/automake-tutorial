package io.github.fdj32.service;

import io.github.fdj32.util.DataReplicateUtil;
import io.github.fdj32.util.DatabaseMetaDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Service
public class DbImportService {

    private static final Logger LOG = LoggerFactory.getLogger(DbImportService.class);

    private static final String CATALOG = "secure_test";
    private static final String SCHEMA = "dbo";

    @Autowired
    @Qualifier("odsJdbcTemplate")
    private JdbcTemplate odsJdbcTemplate;

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate mysqlJdbcTemplate;

    public void importToMysql() throws SQLException {
        LOG.info("begin");
//        showTables(odsJdbcTemplate);
//        showTables(mysqlJdbcTemplate);
        replicate();
        LOG.info("end");
    }

    private void showTables(JdbcTemplate jdbcTemplate) throws SQLException {
        DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        DatabaseMetaDataUtil.showTables(metaData, CATALOG, SCHEMA);
    }

    private void replicate() {
        String select = "SELECT account_id, account_ms_id, account_number, account_type, account_cc_exp_month, account_cc_exp_year, account_zip, account_owner, account_date_created, account_date_updated, account_active, account_address, account_state, account_country, account_ecp_bank_id, account_tender, account_issue_date, account_issue_number, account_eudd_country_code, account_eudd_bank_sort_code, account_eudd_bank_rib_code, encryption_type, instance_id, account_date_retention, account_email, account_city, account_phone_number, account_sub_type, account_update_response_code, account_driving_license_number, account_driving_license_state, account_paypal_login_email, account_number_pre_six, visa_checkout_call_id, visa_checkout_api_key, isvc_new_user, vat_number, account_origin, account_number_type FROM account_info WHERE account_id >= ? AND account_id < ?";
        String insert = "INSERT INTO account_info (account_id, account_ms_id, account_number, account_type, account_cc_exp_month, account_cc_exp_year, account_zip, account_owner, account_date_created, account_date_updated, account_active, account_address, account_state, account_country, account_ecp_bank_id, account_tender, account_issue_date, account_issue_number, account_eudd_country_code, account_eudd_bank_sort_code, account_eudd_bank_rib_code, encryption_type, instance_id, account_date_retention, account_email, account_city, account_phone_number, account_sub_type, account_update_response_code, account_driving_license_number, account_driving_license_state, account_paypal_login_email, account_number_pre_six, visa_checkout_call_id, visa_checkout_api_key, isvc_new_user, vat_number, account_origin, account_number_type) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int min = 1;
        int max= 48870000;
        int batchSize = 10000;
        DataReplicateUtil.replicate(odsJdbcTemplate, select, mysqlJdbcTemplate, insert, min, max, batchSize);
    }
}
