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
            LOG.info("min={}, max={}, from database result set size: {}", min, max, null == resultList ? 0 : resultList.size());
            List<Map<String, Object>> toList = to.queryForList(select, index, index + batchSize);
            LOG.info("min={}, max={}, from database result set size: {}", min, max, null == resultList ? 0 : toList.size());
            resultList.retainAll(toList);
            if (null == resultList || 0 == resultList.size()) {
                LOG.info("min={}, max={}, after retainAll result set size: {}", min, max, null == resultList ? 0 : resultList.size());
                continue;
            }
            int[] returnCodes = to.batchUpdate(insert, resultList.stream().map(m -> m.values().toArray()).collect(Collectors.toList()));
            LOG.info("{} rows affected", returnCodes.length);
        }
    }
}
