package com.xp1024.dao;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Postgres {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public long count() {
		Map<String, Object> map = jdbcTemplate.queryForMap("select count(1) as total from htmdata");
		return (long) map.get("total");
	}

	public void save(int fid, String link, String title, String data) {
		String sql = "insert into htmdata(fid, link, title, data, data_length) values (?, ?, ?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { fid, link, title, data, data.length() });
	}

	public long queryByLinkAndTitle(String link, String title) {
		String sql = "select count(1) as total from htmdata where link=? and title=?";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql, link, title);
		return (long) map.get("total");
	}

	public boolean threadExist(int fid, String title) {
		String sql = "select count(1) as total from thread where fid=? or title=?";
		return (long) jdbcTemplate.queryForMap(sql, fid, title).get("total") > 0;
	}

	public void saveThread(int fid, String title, int parentFid) {
		if (threadExist(fid, title))
			return;
		String sql = "insert into thread(fid, title, parent_fid) values (?, ?, ?)";
		jdbcTemplate.update(sql, new Object[] { fid, title, parentFid == -1 ? null : parentFid });
	}

}
