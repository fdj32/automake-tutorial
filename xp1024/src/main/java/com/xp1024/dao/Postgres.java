package com.xp1024.dao;

import java.util.List;
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
		String sql = "select count(1) as total from htmdata where link=? or title=?";
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

	public boolean imgExist(String src) {
		String sql = "select count(1) as total from img where src=?";
		return (long) jdbcTemplate.queryForMap(sql, src).get("total") > 0;
	}

	public void saveImg(String src) {
		if (imgExist(src))
			return;
		String sql = "insert into img(src) values (?)";
		jdbcTemplate.update(sql, src);
	}

	public List<Map<String, Object>> loadHtmdata(int minId, int maxId) {
		String sql = "select data from htmdata where id >= ? and id < ?";
		return jdbcTemplate.queryForList(sql, minId, maxId);
	}

	public Map<String, Object> htmdataIdRange() {
		String sql = "select min(id) as minId, max(id) as maxId from htmdata";
		return jdbcTemplate.queryForMap(sql);
	}

}
