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

    private static final String CATALOG = "ams_secure";
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
        String select = "SELECT account_id, account_ms_id, account_type, account_zip, account_owner, account_date_created, account_date_updated, account_address, account_state, account_country, instance_id, account_sub_type, account_number_pre_six FROM account_info WHERE account_id >= ? AND account_id < ?";
        String insert = "INSERT INTO account_info (account_id, account_ms_id, account_type, account_zip, account_owner, account_date_created, account_date_updated, account_address, account_state, account_country, instance_id, account_sub_type, account_number_pre_six) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int min = 8530001;
        int max= 16660000;
        int batchSize = 10000;
        DataReplicateUtil.replicate(odsJdbcTemplate, select, mysqlJdbcTemplate, insert, min, max, batchSize);
    }
}
