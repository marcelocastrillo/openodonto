package br.ueg.openodonto.persistencia.dao.sql;

import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Table;

public interface SqlJoinRestriction {

	Table getTable();
	Table getForeginTable();
	ForwardKey getForwardKey();
	
}
