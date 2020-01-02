package io.github.fdj32.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
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
        DatabaseMetaData md = odsJdbcTemplate.getDataSource().getConnection().getMetaData();
        ResultSet rs = md.getTables(CATALOG, SCHEMA, null, null);
        while (rs.next()) {
            String table = rs.getString(3);
            System.out.println(table);
            ResultSet rsPk = md.getPrimaryKeys(CATALOG, SCHEMA, table);
            while (rsPk.next()) {
                System.out.println("PK: " + rsPk.getString(4));
            }
            ResultSet rsColumn = md.getColumns(CATALOG, SCHEMA, table, null);
            while (rsColumn.next()) {
                System.out.println("Column: " + rsColumn.getString(4));
            }
            System.out.println(StringUtils.repeat("-", 80));
        }
        LOG.info("end");


    }
}
