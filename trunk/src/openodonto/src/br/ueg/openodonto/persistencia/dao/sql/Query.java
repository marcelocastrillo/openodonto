package br.ueg.openodonto.persistencia.dao.sql;

import java.util.List;

public class Query {

	private String query;
	private List<Object>  params;
	
	public Query(String query, List<Object> params) {
		this.query = query;
		this.params = params;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	
}
