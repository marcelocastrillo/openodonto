package br.ueg.openodonto.persistencia.dao.sql;

import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.orm.Entity;

public interface SqlExecutor<T extends Entity> {
	    public List<T> executarQuery(String query,String nomeParametrro, Object valorParametro) throws Exception;
		public List<T> executarQuery(String query, String nomeParametrro, Object valorParametro, Integer quant)throws Exception;
		public List<T> executarQuery(String query, Map<String, Object> params);		
	    public List<T> executarPrefixQuery(String name,String nomeParametrro, Object valorParametro) throws Exception;
		public List<T> executarPrefixQuery(String name, String nomeParametrro, Object valorParametro, Integer quant)throws Exception;
		public List<T> executarPrefixQuery(String name, Map<String, Object> params);
		public List<T> executarPrefixQuery(String name,Map<String, Object> params, Integer quant);		
		public List<T> executarQuery(String query,Map<String, Object> params, Integer quant);	
}
