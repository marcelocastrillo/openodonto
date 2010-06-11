package br.ueg.openodonto.persistencia.dao.sql;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.orm.Entity;

public interface SqlExecutor<T extends Entity> {
	    public List<T> executarQuery(String query,String nomeParametrro, Object valorParametro) throws SQLException;
		public List<T> executarQuery(String query, String nomeParametrro, Object valorParametro, Integer quant)throws SQLException;
		public List<T> executarQuery(String query, Map<String, Object> params) throws SQLException;		
		public List<T> executarQuery(String query,Map<String, Object> params, Integer quant) throws SQLException;	
}
