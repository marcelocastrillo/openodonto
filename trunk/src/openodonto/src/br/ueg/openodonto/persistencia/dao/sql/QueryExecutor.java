package br.ueg.openodonto.persistencia.dao.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.persistencia.dao.BaseDAO;
import br.ueg.openodonto.persistencia.orm.Entity;

public class QueryExecutor<T extends Entity> implements SqlExecutor<T>{

	private BaseDAO<T> dao;

	public QueryExecutor(BaseDAO<T> dao){
		this.dao = dao;
	}
	
	@Override
	public List<T> executarQuery(IQuery query) throws SQLException {
		return executarQuery(query.getQuery() , query.getParams() , null);
	}
	
    public List<T> executarQuery(String query, Object valorParametro) throws SQLException {
		return executarQuery(query  , valorParametro , null);
	}

	public List<T> executarQuery(String query, Object valorParametro, Integer quant)throws SQLException {
		List<Object> params = new ArrayList<Object>();
		params.add(valorParametro);
		return executarQuery(query, params, quant);
	}

	public List<T> executarQuery(String query, List<Object> params) throws SQLException{
		return executarQuery(query, params, null);
	}	

	public List<T> executarQuery(String query,List<Object> params, Integer quant) throws SQLException{
		List<T> pList = new ArrayList<T>();
		if(params == null){
			return pList;
		}
		try{
			dao.getConnection().setReadOnly(true);
			ResultSet rs = dao.executeQuery(
					query,
					params,
					quant);
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
