package io.github.fdj32.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMetaDataUtil {

    private static final String MSSQLSERVER = "MSSQLSERVER";
    private static final String MySQL = "MySQL";

    private static final String[][] DATA_TYPE_MAP = new String[][]{
            new String[]{MSSQLSERVER, MySQL},
            new String[]{"int", "INTEGER"},
            new String[]{"char", "CHAR"},
            new String[]{"varchar", "VARCHAR"},
            new String[]{"datetime", "DATETIME"},
            new String[]{"int identity", "INTEGER AUTOINCREMENT"},
            new String[]{"money", "DECIMAL"},
            new String[]{"tinyint", "TINYINT"},
            new String[]{"uniqueidentifier", "VARCHAR"},
            new String[]{"varbinary", "VARBINARY"},
            new String[]{"bit", "BIT"}
    };

    private static String defaultNull(String s) {
        return null == s ? "null" : s;
    }

    public static void showTables(DatabaseMetaData metaData, String catalog, String schema) throws SQLException {
        ResultSet rs = metaData.getTables(catalog, schema, null, null);
        while (rs.next()) {
            String table = rs.getString(3);

            ResultSet rsColumn = metaData.getColumns(catalog, schema, table, null);
            List<String[]> columnList = new ArrayList<>();
            columnList.add(new String[]{"TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE",
                    "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX",
                    "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB",
                    "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE", "SCOPE_CATALOG", "SCOPE_SCHEMA",
                    "SCOPE_TABLE", "SOURCE_DATA_TYPE", "IS_AUTOINCREMENT", "IS_GENERATEDCOLUMN",
                    "PRIMARY_KEY", "KEY_SEQ", "PK_NAME"});

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
                        defaultNull(rsColumn.getString(24)),
                        "", "", ""
                });

                ResultSet rsPk = metaData.getPrimaryKeys(catalog, schema, table);
                while (rsPk.next()) {
                    for (String[] array : columnList) {
                        if (array[3].equals(rsPk.getString(4))) {
                            array[24] = "YES";
                            array[25] = rsPk.getString(5);
                            array[26] = rsPk.getString(6);
                        }
                    }
                }
            }
            BeautifyOutputUtil.printTable(columnList);
            toMysql(columnList);
        }
    }

    private static void toMysql(List<String[]> list) {
        if (null == list || list.size() < 2)
            return;
        String table = list.get(1)[2];
        StringBuilder sb = new StringBuilder();
        sb.append("DROP TABLE ").append(table).append(";\n");
        sb.append("CREATE TABLE ").append(table).append(" (\n");
        for (int i = 1; i < list.size(); i++) {
            sb.append("\t").append(list.get(i)[3]).append(" ");
            sb.append(dataType(list.get(i)[5], MSSQLSERVER, MySQL));
            if ("0".equals(list.get(i)[10])) {
                sb.append(" NOT NULL");
            }
            if(i != list.size() - 1)
                sb.append(",");
            sb.append("\n");
        }
        sb.append(");");
        System.out.println(sb.toString());
    }

    private static String dataType(String srcDataType, String srcDatabase, String destDatabase) {
        if (StringUtils.isEmpty(srcDataType) || StringUtils.isEmpty(srcDatabase) || StringUtils.isEmpty(destDatabase)) {
            return null;
        }
        int srcIndex = 0;
        int destIndex = 0;
        for (int i = 0; i < DATA_TYPE_MAP[0].length; i++) {
            if (DATA_TYPE_MAP[0][i].equals(srcDatabase)) {
                srcIndex = i;
            }
            if (DATA_TYPE_MAP[0][i].equals(destDatabase)) {
                destIndex = i;
            }
        }
        for (int i = 1; i < DATA_TYPE_MAP.length; i++) {
            if (srcDataType.equals(DATA_TYPE_MAP[i][srcIndex])) {
                return DATA_TYPE_MAP[i][destIndex];
            }
        }
        return null;
    }
}
