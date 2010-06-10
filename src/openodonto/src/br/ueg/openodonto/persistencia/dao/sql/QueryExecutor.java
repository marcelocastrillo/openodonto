package br.ueg.openodonto.persistencia.dao.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.dao.BaseDAO;
import br.ueg.openodonto.persistencia.orm.Entity;

public class QueryExecutor<T extends Entity> implements SqlExecutor<T>{

	private BaseDAO<T> dao;

	public QueryExecutor(BaseDAO<T> dao){
		this.dao = dao;
	}
	
	
    public List<T> executarQuery(String query,String nomeParametrro, Object valorParametro) throws Exception {
		return executarQuery(query , nomeParametrro , valorParametro , null);
	}

	public List<T> executarQuery(String query, String nomeParametrro, Object valorParametro, Integer quant)throws Exception {
		Map<String , Object> params = new LinkedHashMap<String, Object>();
		params.put(nomeParametrro, valorParametro);
		return executarQuery(query, params, quant);
	}

	public List<T> executarQuery(String query, Map<String, Object> params) {
		return executarQuery(query, params, null);
	}
	
    public List<T> executarPrefixQuery(String name,String nomeParametrro, Object valorParametro) throws Exception {
		return executarQuery(BaseDAO.getStoredQuerysMap().get(name) , nomeParametrro , valorParametro , null);
	}

	public List<T> executarPrefixQuery(String name, String nomeParametrro, Object valorParametro, Integer quant)throws Exception {
		Map<String , Object> params = new LinkedHashMap<String, Object>();
		params.put(nomeParametrro, valorParametro);
		return executarQuery(BaseDAO.getStoredQuerysMap().get(name), params, quant);
	}

	public List<T> executarPrefixQuery(String name, Map<String, Object> params) {
		return executarQuery(BaseDAO.getStoredQuerysMap().get(name), params, null);
	}
	
	public List<T> executarPrefixQuery(String name,Map<String, Object> params, Integer quant) {
		return executarQuery(BaseDAO.getStoredQuerysMap().get(name), params , quant);
	}
	
	public List<T> executarQuery(String query,Map<String, Object> params, Integer quant) {
		List<T> pList = new ArrayList<T>();
		if(params == null){
			return pList;
		}
		try{
			dao.getConnection().setReadOnly(true);
			ResultSet rs = dao.executeQuery(
					query,
					new ArrayList<Object>(params.values()) , quant);
			dao.getConnection().setReadOnly(false);
			while(rs.next()) {
				T entity = dao.parseEntity(rs);
				pList.add(entity);
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return pList;
	}
	
}
