package com.xp1024.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Mysql {
	
	private static final Logger log = LoggerFactory.getLogger(Mysql.class);

	@Bean(name = "datasource1")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource1")
	public DataSource primaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplate1")
	@Autowired
	JdbcTemplate jdbcTemplate1(@Qualifier("datasource1") DataSource ds) {
		JdbcTemplate jt = new JdbcTemplate(ds);
		return jt;
	}

	@Autowired
	@Qualifier("jdbcTemplate1")
	private JdbcTemplate jdbcTemplate1;

	public int count() {
		String sql = "select count(1) as total from htmdata";
		sql = "select max(id) as total from htmdata";
		Map<String, Object> map = jdbcTemplate1.queryForMap(sql);
		if (null == map || null == map.get("total"))
			return 0;
		return (int) map.get("total");
	}

	public void save(int fid, String link, String title, String data) {
		String sql = "insert into htmdata(fid, link, title, data, data_length) values (?, ?, ?, ?, ?)";
		jdbcTemplate1.update(sql, new Object[] { fid, link, title, data, data.length() });
	}

	public long queryByLink(String link) {
		String sql = "select count(1) as total from htmdata where link=?";
		Map<String, Object> map = jdbcTemplate1.queryForMap(sql, link);
		return (long) map.get("total");
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
	
	public List<Map<String, Object>> query(int begin, int end) {
		String sql = "select fid, link, title, data from htmdata where id >= ? and id < ? and id in "
				+ "(SELECT MAX(b.id) FROM htmdata b group by b.link)";
		//return jdbcTemplate1.queryForList(sql, begin, end).stream().filter(distinctByKey(m -> m.get("link"))).collect(Collectors.toList());
		return jdbcTemplate1.queryForList(sql, begin, end);
	}
	
	public void batchInsert(List<Map<String, Object>> list) throws SQLException {
		long start = System.currentTimeMillis();
		if (null == list || 0 == list.size()) {
			log.error("list is empty");
			return;
		}
		int size = list.size();
		log.info("batchInsert start at {}, size={}", start, size);
		String sql = "insert into htmdata(fid, link, title, data, data_length) values (?, ?, ?, ?, ?)";
		Connection conn = jdbcTemplate1.getDataSource().getConnection();
		conn.setAutoCommit(false);
		PreparedStatement ps = jdbcTemplate1.getDataSource().getConnection().prepareStatement(sql);
		for (int i = 0; i < size; i++) {
			ps.setInt(1, (int) list.get(i).get("fid"));
			ps.setString(2, (String) list.get(i).get("link"));
			ps.setString(3, (String) list.get(i).get("title"));
			ps.setString(4, (String) list.get(i).get("data"));
			ps.setInt(5, ((String) list.get(i).get("data")).length());
			ps.addBatch();
		}
		ps.executeBatch();
		conn.commit();
		conn.setAutoCommit(true);
		ps.close();
		conn.close();
		long end = System.currentTimeMillis();
		log.info("batchInsert end at {}, {} inserted, takes {} ms", end, size, (end - start));
	}
}
