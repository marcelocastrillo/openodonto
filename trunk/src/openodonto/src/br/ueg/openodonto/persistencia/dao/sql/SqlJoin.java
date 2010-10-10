package br.ueg.openodonto.persistencia.dao.sql;

import java.util.List;


public interface SqlJoin {
	SqlJoinType                getType();
	List<SqlJoinRestriction>   getRestrictions();	
}
