package ru.mastercvv.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class Pgsql {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void insertBin(String bin, String bank, String network, String country, String type, String level) {
		String sql = "insert into bin(bin, bank_name, issuing_network, country, card_type, card_level) values(?,?,?,?,?,?)";
		jdbcTemplate.update(sql, bin, bank, network, country, type, level);
	}

	public boolean exist(String bin) {
		String sql = "select id from bin where bin=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, bin);
		return list != null && list.size() > 0;
	}
}
