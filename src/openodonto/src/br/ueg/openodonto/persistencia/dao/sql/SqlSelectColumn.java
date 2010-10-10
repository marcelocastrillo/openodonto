package br.ueg.openodonto.persistencia.dao.sql;

import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Table;

public interface SqlSelectColumn extends SqlSelectable{
	
	Table  getTable();
	Column getColumn();
	
}
