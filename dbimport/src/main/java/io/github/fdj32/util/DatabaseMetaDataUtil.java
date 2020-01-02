package io.github.fdj32.util;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMetaDataUtil {

    private static String defaultNull(String s) {
        return null == s ? "null" : s;
    }

    public static void showTables(DatabaseMetaData metaData, String catalog, String schema) throws SQLException {
        ResultSet rs = metaData.getTables(catalog, schema, null, null);
        while (rs.next()) {
            String table = rs.getString(3);
            System.out.println(table);
            ResultSet rsPk = metaData.getPrimaryKeys(catalog, schema, table);
            List<String[]> pkList = new ArrayList<>();
            pkList.add(new String[]{"TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "KEY_SEQ", "PK_NAME"});
            StringBuilder sbPK = new StringBuilder();
            while (rsPk.next()) {
                pkList.add(new String[]{
                        defaultNull(rsPk.getString(1)),
                        defaultNull(rsPk.getString(2)),
                        defaultNull(rsPk.getString(3)),
                        defaultNull(rsPk.getString(4)),
                        defaultNull(rsPk.getString(5)),
                        defaultNull(rsPk.getString(6))
                });
            }
            BeautifyOutputUtil.printTable(pkList);

            ResultSet rsColumn = metaData.getColumns(catalog, schema, table, null);
            List<String[]> columnList = new ArrayList<>();
            columnList.add(new String[]{"TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE",
                    "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX",
                    "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
                    "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATALOG", "SCOPE_SCHEMA",
                    "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN"});

            StringBuilder sbColumn = new StringBuilder();

            while (rsColumn.next()) {
                columnList.add(new String[]{
                        defaultNull(rsColumn.getString(1)),
                        defaultNull(rsColumn.getString(2)),
                        defaultNull(rsColumn.getString(3)),
                        defaultNull(rsColumn.getString(4)),
                        rsColumn.getInt(5) + "",

                        defaultNull(rsColumn.getString(6)),
                        rsColumn.getInt(7) + "",
                        defaultNull(rsColumn.getString(8)),
                        rsColumn.getInt(9) + "",
                        rsColumn.getInt(10) + "",

                        rsColumn.getInt(11) + "",
                        defaultNull(rsColumn.getString(12)),
                        defaultNull(rsColumn.getString(13)),
                        rsColumn.getInt(14) + "",
                        rsColumn.getInt(15) + "",

                        rsColumn.getInt(16) + "",
                        rsColumn.getInt(17) + "",
                        defaultNull(rsColumn.getString(18)),
                        defaultNull(rsColumn.getString(19)),
                        defaultNull(rsColumn.getString(20)),

                        defaultNull(rsColumn.getString(21)),
                        rsColumn.getShort(22) + "",
                        defaultNull(rsColumn.getString(23)),
                        defaultNull(rsColumn.getString(24))
                });
                BeautifyOutputUtil.printTable(columnList);
            }
        }
    }
}
