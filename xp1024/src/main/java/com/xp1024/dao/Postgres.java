package com.xp1024.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Postgres {

	private static final Logger log = LoggerFactory.getLogger(Postgres.class);

	@Bean(name = "datasource2")
	@ConfigurationProperties(prefix = "spring.datasource2")
	public DataSource secondaryDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplate2")
	@Autowired
	JdbcTemplate jdbcTemplate2(@Qualifier("datasource2") DataSource ds) {
		JdbcTemplate jt = new JdbcTemplate(ds);
		return jt;
	}

	@Autowired
	@Qualifier("jdbcTemplate2")
	private JdbcTemplate jdbcTemplate2;

	public long count() {
		Map<String, Object> map = jdbcTemplate2.queryForMap("select count(1) as total from htmdata");
		return (long) map.get("total");
	}
	
	public void save(int fid, String link, String title, String data) {
		String sql = "insert into htmdata(fid, link, title, data) values (?, ?, ?, ?)";
		jdbcTemplate2.update(sql, new Object[] { fid, link, title, data });
	}

	public long queryByLink(String link) {
		String sql = "select count(1) as total from htmdata where link=?";
		Map<String, Object> map = jdbcTemplate2.queryForMap(sql, link);
		return (long) map.get("total");
	}

	public void batchInsert(List<Map<String, Object>> list) throws SQLException {
		long start = System.currentTimeMillis();
		if (null == list || 0 == list.size()) {
			log.error("list is empty");
			return;
		}
		int size = list.size();
		log.info("batchInsert start at {}, size={}", start, size);
		String sql = "insert into htmdata(fid, link, title, data) values (?, ?, ?, ?)";
		Connection conn = jdbcTemplate2.getDataSource().getConnection();
//		while(null != conn)
//			conn = jdbcTemplate2.getDataSource().getConnection();
		conn.setAutoCommit(false);
		PreparedStatement ps = jdbcTemplate2.getDataSource().getConnection().prepareStatement(sql);
		for (int i = 0; i < size; i++) {
			ps.setInt(1, (int) list.get(i).get("fid"));
			ps.setString(2, (String) list.get(i).get("link"));
			ps.setString(3, (String) list.get(i).get("title"));
			ps.setString(4, (String) list.get(i).get("data"));
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
