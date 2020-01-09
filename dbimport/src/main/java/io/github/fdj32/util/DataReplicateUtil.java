package io.github.fdj32.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataReplicateUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DataReplicateUtil.class);

    /**
     * @param from      Source Database JdbcTemplate
     * @param select    select col1, col2, col3 from src_table where id >= index and id < index+batchSize
     * @param to        Dest Database JdbcTemplate
     * @param insert    insert into table dest_table values (col1Value, col2Value, col3Value);
     * @param min       min value of src_table id
     * @param max       max value of src_table id
     * @param batchSize batchSize of select from src_table and insert into dest_table
     */
    public static void replicate(JdbcTemplate from, String select, JdbcTemplate to, String insert, int min, int max, int batchSize) {
        for (int index = min; index < max; index += batchSize) {
            List<Map<String, Object>> resultList = from.queryForList(select, index, index + batchSize);
            LOG.info("min={}, max={}, from database result set size: {}", index, index + batchSize, null == resultList ? 0 : resultList.size());
            /*
            List<Map<String, Object>> toList = to.queryForList(select, index, index + batchSize);
            LOG.info("min={}, max={}, to database result set size: {}", index, index + batchSize, null == toList ? 0 : toList.size());
            if (null != resultList && null != toList && resultList.size() == toList.size()) {
                LOG.info("min={}, max={}, from and to database result set size are the same: {}", index, index + batchSize, resultList.size());
                continue;
            }
            resultList.removeAll(toList);
            if (null == resultList || 0 == resultList.size()) {
                LOG.info("min={}, max={}, after removeAll result set size: {}", index, index + batchSize, null == resultList ? 0 : resultList.size());
                continue;
            }

            // works for mysql, not for postgresql, change account_number data type to bytea, and code changes as following, and change isvc_new_user change to INTEGER
            int[] returnCodes = to.batchUpdate(insert, resultList.stream().map(m -> m.values().toArray()).collect(Collectors.toList()));

2020-01-09 15:51:37.892 CST [59570] STATEMENT:  INSERT INTO account_info (account_id, account_ms_id, account_number, account_type, account_cc_exp_month, account_cc_exp_year, account_zip, account_owner, account_date_created, account_date_updated, account_active, account_address, account_state, account_country, account_ecp_bank_id, account_tender, account_issue_date, account_issue_number, account_eudd_country_code, account_eudd_bank_sort_code, account_eudd_bank_rib_code, encryption_type, instance_id, account_date_retention, account_email, account_city, account_phone_number, account_sub_type, account_update_response_code, account_driving_license_number, account_driving_license_state, account_paypal_login_email, account_number_pre_six, visa_checkout_call_id, visa_checkout_api_key, isvc_new_user, vat_number, account_origin, account_number_type) VALUES($1, $2, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16, $17, $18, $19, $20, $21, $22, $23, $24, $25, $26, $27, $28, $29, $30, $31, $32, $33, $34, $35, $36, $37, $38, $39)
2020-01-09 15:51:37.894 CST [59570] ERROR:  column "account_number" is of type bytea but expression is of type character varying at character 820
2020-01-09 15:51:37.894 CST [59570] HINT:  You will need to rewrite or cast the expression.

            */
            int[] returnCodes = to.batchUpdate(insert, resultList.stream().map(m -> {
                m.put("account_number", ((String)(m.get("account_number"))).getBytes());
                return m.values().toArray();
            }).collect(Collectors.toList()));
            LOG.info("{} rows affected", returnCodes.length);
        }
    }
}
