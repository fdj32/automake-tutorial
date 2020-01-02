package io.github.fdj32.service;

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
        showTables(odsJdbcTemplate);
        showTables(mysqlJdbcTemplate);
        LOG.info("end");
    }

    private void showTables(JdbcTemplate jdbcTemplate) throws SQLException {
        DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        DatabaseMetaDataUtil.showTables(metaData, CATALOG, SCHEMA);
    }
}
