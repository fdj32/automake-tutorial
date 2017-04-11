package com.active.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

public abstract class ListRowMapper<T> implements RowMapper {

	private List<T> result = new ArrayList<T>();

	public List<T> getResults() {
		return result;
	}
	
	public T getResult() {
		return result.size() > 0 ? result.get(0) : null;
	}

	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T t = mapRow(rs);
		result.add(t);
		return t;
	}

	public abstract T mapRow(ResultSet rs) throws SQLException;
}
