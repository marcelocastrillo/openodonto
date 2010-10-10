package br.ueg.openodonto.persistencia.dao.sql;

import java.util.List;

import br.ueg.openodonto.persistencia.orm.Table;

public interface SqlFrom {

	List<Table> getTables();
	List<SqlJoinType> getJoins();
	
}
