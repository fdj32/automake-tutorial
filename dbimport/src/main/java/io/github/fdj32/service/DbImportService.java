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
            sbPK.append("TABLE_CAT\t");
            sbPK.append("TABLE_SCHEM\t");
            sbPK.append("TABLE_NAME\t");
            sbPK.append("KEY_SEQ\t");
            sbPK.append("PK_NAME\t");
            sbPK.append("COLUMN_NAME\n");
            while (rsPk.next()) {
                sbPK.append(rsPk.getString(1)).append("\t");
                sbPK.append(rsPk.getString(2)).append("\t");
                sbPK.append(rsPk.getString(3)).append("\t");

                sbPK.append(rsPk.getString(5)).append("\t");
                sbPK.append(rsPk.getString(6)).append("\t");

                sbPK.append(rsPk.getString(4)).append("\n");
            }
            System.out.println(sbPK.toString());
            ResultSet rsColumn = md.getColumns(CATALOG, SCHEMA, table, null);
            StringBuilder sbColumn = new StringBuilder();
            sbColumn.append("TABLE_CAT\t");
            sbColumn.append("TABLE_SCHEM\t");
            sbColumn.append("TABLE_NAME\t");

            sbColumn.append("DATA_TYPE\t");

            sbColumn.append("TYPE_NAME\t");
            sbColumn.append("COLUMN_SIZE\t");
            sbColumn.append("BUFFER_LENGTH\t");
            sbColumn.append("DECIMAL_DIGITS\t");
            sbColumn.append("NUM_PREC_RADIX\t");

            sbColumn.append("NULLABLE\t");
            sbColumn.append("REMARKS\t");
            sbColumn.append("COLUMN_DEF\t");
            sbColumn.append("SQL_DATA_TYPE\t");
            sbColumn.append("SQL_DATETIME_SUB\t");

            sbColumn.append("CHAR_OCTET_LENGTH\t");
            sbColumn.append("ORDINAL_POSITION\t");
            sbColumn.append("IS_NULLABLE\t");
            sbColumn.append("SCOPE_CATALOG\t");
            sbColumn.append("SCOPE_SCHEMA\t");

            sbColumn.append("SCOPE_TABLE\t");
            sbColumn.append("SOURCE_DATA_TYPE\t");
            sbColumn.append("IS_AUTOINCREMENT\t");
            sbColumn.append("IS_GENERATEDCOLUMN\t");

            sbColumn.append("COLUMN_NAME\n");

            while (rsColumn.next()) {
                sbColumn.append(rsColumn.getString(1)).append("\t");
                sbColumn.append(rsColumn.getString(2)).append("\t");
                sbColumn.append(rsColumn.getString(3)).append("\t");

                sbColumn.append(rsColumn.getInt(5)).append("\t");

                sbColumn.append(rsColumn.getString(6)).append("\t");
                sbColumn.append(rsColumn.getInt(7)).append("\t");
                sbColumn.append(rsColumn.getString(8)).append("\t");
                sbColumn.append(rsColumn.getInt(9)).append("\t");
                sbColumn.append(rsColumn.getInt(10)).append("\t");

                sbColumn.append(rsColumn.getInt(11)).append("\t");
                sbColumn.append(rsColumn.getString(12)).append("\t");
                sbColumn.append(rsColumn.getString(13)).append("\t");
                sbColumn.append(rsColumn.getInt(14)).append("\t");
                sbColumn.append(rsColumn.getInt(15)).append("\t");

                sbColumn.append(rsColumn.getInt(16)).append("\t");
                sbColumn.append(rsColumn.getInt(17)).append("\t");
                sbColumn.append(rsColumn.getString(18)).append("\t");
                sbColumn.append(rsColumn.getString(19)).append("\t");
                sbColumn.append(rsColumn.getString(20)).append("\t");

                sbColumn.append(rsColumn.getString(21)).append("\t");
                sbColumn.append(rsColumn.getShort(22)).append("\t");
                sbColumn.append(rsColumn.getString(23)).append("\t");
                sbColumn.append(rsColumn.getString(24)).append("\t");

                sbColumn.append(rsColumn.getString(4)).append("\n");
            }
            System.out.println(sbColumn.toString());
            System.out.println(StringUtils.repeat("-", 80));
        }
    }
}
