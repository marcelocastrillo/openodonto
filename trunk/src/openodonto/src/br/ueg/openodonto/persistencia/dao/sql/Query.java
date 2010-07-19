package br.ueg.openodonto.persistencia.dao.sql;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Query implements IQuery {

    private String                  query;
    private Map<String , Field>     columnsMap; // Map Nome da coluna Field
    private Map<String, Object>     whereParams; // Map Nome da coluna valor
    private List<Object>            params;
    private String                  table;

    public Query(String query, List<Object> params, String table) {
	this.query = query;
	this.params = params;
	this.table = table;
    }

    @Override
    public String getQuery() {
	return query;
    }

    public void setQuery(String query) {
	this.query = query;
    }

    @Override
    public List<Object> getParams() {
	return params;
    }

    public void setParams(List<Object> params) {
	this.params = params;
    }

    @Override
    public String getTable() {
	return table;
    }

    public void setTable(String table) {
	this.table = table;
    }

}
