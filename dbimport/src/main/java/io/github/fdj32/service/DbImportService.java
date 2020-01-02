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
        showTables(odsJdbcTemplate);
        showTables(mysqlJdbcTemplate);
        LOG.info("end");
    }

    private void showTables(JdbcTemplate jdbcTemplate) throws SQLException {
        DatabaseMetaData md = jdbcTemplate.getDataSource().getConnection().getMetaData();
        ResultSet rs = md.getTables(CATALOG, SCHEMA, null, null);
        while (rs.next()) {
            String table = rs.getString(3);
            System.out.println(table);
            ResultSet rsPk = md.getPrimaryKeys(CATALOG, SCHEMA, table);
            StringBuilder sbPK = new StringBuilder();
            while (rsPk.next()) {
                sbPK.append("TABLE_CAT:\t").append(rsPk.getString(1)).append("\t");
                sbPK.append("TABLE_SCHEM:\t").append(rsPk.getString(2)).append("\t");
                sbPK.append("TABLE_NAME:\t").append(rsPk.getString(3)).append("\t");
                sbPK.append("COLUMN_NAME:\t").append(rsPk.getString(4)).append("\t");
                sbPK.append("KEY_SEQ:\t").append(rsPk.getString(5)).append("\t");
                sbPK.append("PK_NAME:\t").append(rsPk.getString(6)).append("\n");
            }
            System.out.println(sbPK.toString());
            ResultSet rsColumn = md.getColumns(CATALOG, SCHEMA, table, null);
            StringBuilder sbColumn = new StringBuilder();
            while (rsColumn.next()) {
                sbColumn.append("TABLE_CAT:\t").append(rsColumn.getString(1)).append("\t");
                sbColumn.append("TABLE_SCHEM:\t").append(rsColumn.getString(2)).append("\t");
                sbColumn.append("TABLE_NAME:\t").append(rsColumn.getString(3)).append("\t");
                sbColumn.append("COLUMN_NAME:\t").append(rsColumn.getString(4)).append("\t");
                sbColumn.append("DATA_TYPE:\t").append(rsColumn.getInt(5)).append("\t");

                sbColumn.append("TYPE_NAME:\t").append(rsColumn.getString(6)).append("\t");
                sbColumn.append("COLUMN_SIZE:\t").append(rsColumn.getInt(7)).append("\t");
                sbColumn.append("BUFFER_LENGTH:\t").append(rsColumn.getString(8)).append("\t");
                sbColumn.append("DECIMAL_DIGITS:\t").append(rsColumn.getInt(9)).append("\t");
                sbColumn.append("NUM_PREC_RADIX:\t").append(rsColumn.getInt(10)).append("\t");

                sbColumn.append("NULLABLE:\t").append(rsColumn.getInt(11)).append("\t");
                sbColumn.append("REMARKS:\t").append(rsColumn.getString(12)).append("\t");
                sbColumn.append("COLUMN_DEF:\t").append(rsColumn.getString(13)).append("\t");
                sbColumn.append("SQL_DATA_TYPE:\t").append(rsColumn.getInt(14)).append("\t");
                sbColumn.append("SQL_DATETIME_SUB:\t").append(rsColumn.getInt(15)).append("\t");

                sbColumn.append("CHAR_OCTET_LENGTH:\t").append(rsColumn.getInt(16)).append("\t");
                sbColumn.append("ORDINAL_POSITION:\t").append(rsColumn.getInt(17)).append("\t");
                sbColumn.append("IS_NULLABLE:\t").append(rsColumn.getString(18)).append("\t");
                sbColumn.append("SCOPE_CATALOG:\t").append(rsColumn.getString(19)).append("\t");
                sbColumn.append("SCOPE_SCHEMA:\t").append(rsColumn.getString(20)).append("\t");

                sbColumn.append("SCOPE_TABLE:\t").append(rsColumn.getString(21)).append("\t");
                sbColumn.append("SOURCE_DATA_TYPE:\t").append(rsColumn.getShort(22)).append("\t");
                sbColumn.append("IS_AUTOINCREMENT:\t").append(rsColumn.getString(23)).append("\t");
                sbColumn.append("IS_GENERATEDCOLUMN:\t").append(rsColumn.getString(24)).append("\n");
            }
            System.out.println(sbColumn.toString());
            System.out.println(StringUtils.repeat("-", 80));
        }
    }
}
