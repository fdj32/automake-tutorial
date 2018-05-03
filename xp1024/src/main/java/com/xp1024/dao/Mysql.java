package com.xp1024.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.sql.DataSource;

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
		String sql = "insert into htmdata(fid, link, title, data) values (?, ?, ?, ?)";
		jdbcTemplate1.update(sql, new Object[] { fid, link, title, data });
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
}
