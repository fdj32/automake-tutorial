package com.xp1024.dao;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Postgres {

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
		String sql = "insert into htmdata(fid, link, title, data, data_length) values (?, ?, ?, ?, ?)";
		jdbcTemplate2.update(sql, new Object[] { fid, link, title, data, data.length() });
	}

	public long queryByLinkAndTitle(String link, String title) {
		String sql = "select count(1) as total from htmdata where link=? and title=?";
		Map<String, Object> map = jdbcTemplate2.queryForMap(sql, link, title);
		return (long) map.get("total");
	}

}
