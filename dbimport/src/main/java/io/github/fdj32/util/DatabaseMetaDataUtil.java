package io.github.fdj32.util;

import org.apache.commons.lang3.StringUtils;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseMetaDataUtil {

    private static final String MSSQLSERVER = "MSSQLSERVER";
    private static final String MySQL = "MySQL";

    private static final String[][] DATA_TYPE_MAP = new String[][]{
            new String[]{MSSQLSERVER, MySQL},
            new String[]{"char", "CHAR"},
            new String[]{"varchar", "VARCHAR"},
            new String[]{"nvarchar", "NVARCHAR"},
            new String[]{"varbinary", "VARBINARY"},
            new String[]{"datetime", "DATETIME"},
            new String[]{"smallint", "SMALLINT"},
            new String[]{"int", "INTEGER"},
            new String[]{"bigint", "BIGINT"},
            new String[]{"decimal", "DECIMAL"},
            // remove AUTO_INCREMENT if you jst want to import data
            new String[]{"int identity", "INTEGER AUTO_INCREMENT"},
            new String[]{"bigint identity", "BIGINT AUTO_INCREMENT"},
            new String[]{"decimal() identity", "DECIMAL AUTO_INCREMENT"},
            new String[]{"money", "DECIMAL(19, 2)"},
            new String[]{"tinyint", "TINYINT"},
            new String[]{"float", "FLOAT"},
            new String[]{"text", "TEXT"},
            new String[]{"image", "BLOB"},
            new String[]{"uniqueidentifier", "VARCHAR(36)"},
            new String[]{"bit", "BIT"}
    };

    private static final List LENGTH_REQUIED = Arrays.asList(1, 2, 3, 4);

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
            }
            // add primary key info
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
            //BeautifyOutputUtil.printTable(columnList);
            toMysql(columnList);
            // add index info
            List<Object[]> indexColumnList = new ArrayList<>();
            ResultSet rsIndex = metaData.getIndexInfo(catalog, schema, table, false, false);
            while (rsIndex.next()) {
                if (null == rsIndex.getString(6) || 1 == rsIndex.getShort(7)) {
                    continue; // tableIndexStatistic, primary key index
                }
                indexColumnList.add(new Object[]{
                        rsIndex.getString(1),
                        rsIndex.getString(2),
                        rsIndex.getString(3),
                        rsIndex.getBoolean(4),

                        rsIndex.getString(5),
                        rsIndex.getString(6),
                        rsIndex.getShort(7),
                        rsIndex.getShort(8),

                        rsIndex.getString(9),
                        rsIndex.getString(10),
                        rsIndex.getLong(11),
                        rsIndex.getLong(12),

                        rsIndex.getString(13)
                });
            }
            createIndex(indexColumnList);
        }
    }

    private static void createIndex(List<Object[]> indexColumnList) {
        indexColumnList.stream().collect(Collectors.groupingBy(obis -> obis[5])).entrySet().forEach(entry -> {
            String indexName = (String) entry.getKey();
            List<Object[]> columns = entry.getValue();
            StringBuilder sbIndex = new StringBuilder();
            sbIndex.append("CREATE INDEX ").append(indexName).append(" ON ");
            sbIndex.append(columns.get(0)[2]).append('(');
            for (int i = 0; i < columns.size(); i++) {
                final short columnIndex = (short) (i + 1);
                sbIndex.append(columns.stream().filter(cols -> columnIndex == (short) cols[7]).findFirst().get()[8]);
                if (i != columns.size() - 1) {
                    sbIndex.append(", ");
                }
            }
            sbIndex.append(");");
            System.out.println(sbIndex.toString());
        });
    }

    private static void toMysql(List<String[]> list) {
        if (null == list || list.size() < 2)
            return;
        String table = list.get(1)[2];
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ").append(table).append(" (\n");
        StringBuilder sbSelect = new StringBuilder();
        sbSelect.append("SELECT ");
        StringBuilder sbInsert = new StringBuilder();
        sbInsert.append("INSERT INTO ").append(table).append(" (");
        List<String[]> pkList = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            if ("YES".equals(list.get(i)[24])) {
                pkList.add(list.get(i));
            }
            sb.append("\t").append(list.get(i)[3]).append(" ");
            sbSelect.append(list.get(i)[3]);
            sbInsert.append(list.get(i)[3]);
            if (i != list.size() - 1) {
                sbSelect.append(", ");
                sbInsert.append(", ");
            }
            int[] indexes = dataType(list.get(i)[5], MSSQLSERVER, MySQL);
            sb.append(DATA_TYPE_MAP[indexes[0]][indexes[1]]); // append type
            if (LENGTH_REQUIED.contains(indexes[0]) && !(Integer.MAX_VALUE + "").equals(list.get(i)[6])) { // append length
                sb.append('(').append(list.get(i)[6]).append(')');
            }
            if ("0".equals(list.get(i)[10])) {
                sb.append(" NOT NULL");
            }
            if (i == list.size() - 1) {
                if (!pkList.isEmpty()) {
                    sb.append(",\n\tPRIMARY KEY(");
                    for (int j = 0; j < pkList.size() - 1; j++) {
                        String keySeq = (j + 1) + "";
                        sb.append(pkList.stream().filter(pk -> keySeq.equals(pk[25])).findFirst().get()[3]).append(", ");
                    }
                    sb.append(pkList.stream().filter(pk -> (pkList.size() + "").equals(pk[25])).findFirst().get()[3]);
                    sb.append(')');
                }
            } else {
                sb.append(',');
            }
            sb.append(System.lineSeparator());
        }
        sb.append(") engine=MyISAM;");
        System.out.println(sb.toString());
        sbSelect.append(" FROM ").append(table);
//        System.out.println(sbSelect.toString());
        sbInsert.append(") VALUES(");
        for (int i = 1; i < list.size(); i++) {
            sbInsert.append('?');
            if (i != list.size() - 1) {
                sbInsert.append(", ");
            }
        }
        sbInsert.append(')');
//        System.out.println(sbInsert.toString());
    }

    private static int[] dataType(String srcDataType, String srcDatabase, String destDatabase) {
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
                return new int[]{i, destIndex};
            }
        }
        return null;
    }
}
