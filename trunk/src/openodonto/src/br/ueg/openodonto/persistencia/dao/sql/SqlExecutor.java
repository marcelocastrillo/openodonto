package br.ueg.openodonto.persistencia.dao.sql;

import java.sql.SQLException;
import java.util.List;

public interface SqlExecutor<T> {
	    public List<T> executarQuery(IQuery query) throws SQLException;
	    public List<T> executarQuery(String query, Object valorParametro) throws SQLException;
		public List<T> executarQuery(String query, Object valorParametro, Integer quant)throws SQLException;
		public List<T> executarQuery(String query, List<Object> params) throws SQLException;		
		public List<T> executarQuery(String query,List<Object> params, Integer quant) throws SQLException;	
}
