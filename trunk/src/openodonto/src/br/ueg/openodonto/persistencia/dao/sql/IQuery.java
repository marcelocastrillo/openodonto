package br.ueg.openodonto.persistencia.dao.sql;

import java.util.List;

public interface IQuery {

	public abstract String getQuery();

	public abstract List<Object> getParams();

	public abstract String getTable();

}