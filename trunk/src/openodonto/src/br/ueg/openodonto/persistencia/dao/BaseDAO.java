/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ueg.openodonto.persistencia.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.ConnectionFactory;


/**
 *
 * @author Vinicius
 */

public abstract class BaseDAO<T> implements Serializable {

    private static final long serialVersionUID = 186038189036166890L;
    
    private String listQuery;
    
    public BaseDAO() {
    	init();
	}

    private void init(){
    	listQuery = "SELECT * FROM " + getTableName();
    }
    
	public ConnectionFactory getConnectionFactory() {
		return ConnectionFactory.getInstance();
	}

	public Connection getConnection(){
		try {
			return this.getConnectionFactory().getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
	public void closeConnection() {
		this.getConnectionFactory().closeConnection();
	}

	public ResultSet executeQuery(String sql, List<Object> params) throws Exception {
		return executeQuery(sql , params, null);
	}
	
	public ResultSet executeQuery(String sql, List<Object> params, Integer limit) throws Exception {
		PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
		if(limit != null){
			preparedStatement.setMaxRows(limit);
		}
		if(params != null){
			int i=1;
			for(Object object : params){
                if(object instanceof java.sql.Date){
                    preparedStatement.setDate(i++, (java.sql.Date)object);
                }else{
				    preparedStatement.setObject(i++, object);
                }
			}
		}
		return preparedStatement.executeQuery();
	}

	public ResultSet executeQuery(String sql, Object... params) throws Exception {
		PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql);
		if(params != null){
			for(int i=1 ; i < params.length+1; i++){
                if(params[i-1] instanceof java.sql.Date){
                    preparedStatement.setDate(i, (java.sql.Date)params[i-1]);
                }else{
				    preparedStatement.setObject(i, params[i-1]);
                }
			}
		}
		return preparedStatement.executeQuery();
	}

	public Map<String,Object> execute(String sql, Object[] params) throws Exception {
		PreparedStatement preparedStatement = this.getConnection().prepareStatement(sql , Statement.RETURN_GENERATED_KEYS);
		if(params != null){
			for(int i=1 ; i < params.length+1; i++){
               if(params[i-1] instanceof Date){
                    preparedStatement.setDate(i, new java.sql.Date(((Date)params[i-1]).getTime()));
                }else if(params[i-1] instanceof String){
                	preparedStatement.setString(i, String.valueOf(params[i-1]));
                }else{
				    preparedStatement.setObject(i, params[i-1]);
                }
			}
		}
		preparedStatement.execute();
		ResultSet generatedValues = preparedStatement.getGeneratedKeys();
		Map<String,Object> objects = new LinkedHashMap<String ,Object>();
		if (generatedValues != null) {
			if (generatedValues.next()) {
				int count = generatedValues.getMetaData().getColumnCount();
				for (int i = 1; i <= count; i++) {
					objects.put(generatedValues.getMetaData().getColumnName(i),	generatedValues.getObject(i));
				}
			}
		}
		return objects;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> listar() throws Exception{
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM ");
		query.append(getTableName());
		List<T> lista = new ArrayList<T>();
		ResultSet rs = executeQuery(query.toString() , Collections.EMPTY_LIST);
		while(rs.next()){
			lista.add(parseEntry(rs));
		}
		return lista;
	}
	
	public Map<String,Object> executeInsert(T entry) throws Exception{
		StringBuilder query = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		values.append(" VALUES ( ");
		query.append("INSERT INTO ");
		query.append(getTableName());
		Map<String , Object> fields = format(entry);
		Iterator<String> iterator = fields.keySet().iterator();
		query.append("(");
		while(iterator.hasNext()){
			String field = iterator.next();
			query.append(field);
			params.add(fields.get(field));
			values.append("?");
			if(iterator.hasNext()){
				query.append(", ");
				values.append(", ");
			}
		}
		values.append(")");
		query.append(")");
		query.append(values);
		return execute(query.toString(), params.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public ResultSet list() throws Exception{
		return executeQuery(this.listQuery, Collections.EMPTY_LIST);
	}
	
	public Map<String, Object> formatResultSet(ResultSet rs) throws SQLException{
		if(rs != null){
			Map<String,Object> objects = new LinkedHashMap<String ,Object>();
			int count = rs.getMetaData().getColumnCount();
			for(int i = 1; i <= count ; i++){
				objects.put(rs.getMetaData().getColumnName(i) , rs.getObject(i));
			}
			return objects;
		}
		return null;
		
	}
	
	public Map<String,Object> executeUpdate(T entry, Map<String , Object> whereParams) throws Exception{
		StringBuilder query = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		query.append("UPDATE ");
		query.append(getTableName());
		query.append(" SET ");
		Map<String , Object> fields = format(entry);
		Iterator<String> iterator = fields.keySet().iterator();
		while(iterator.hasNext()){
			String field = iterator.next();
			query.append(field + " = ?");
			params.add(fields.get(field));
			if(iterator.hasNext()){
				query.append(", ");
			}
		}
		if(whereParams != null && whereParams.size() > 0){
		    iterator = whereParams.keySet().iterator();
		    query.append(" WHERE ");
		    while(iterator.hasNext()){
			    String field = iterator.next();
			    query.append(field + " = ?");
			    params.add(whereParams.get(field));
				if(iterator.hasNext()){
					query.append(", ");
				}
	    	}
		}		
		return execute(query.toString(), params.toArray());
	}
	
	public void executeRemove(Map<String , Object> whereParams) throws Exception{
		StringBuilder query = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		query.append("DELETE FROM ");
		query.append(getTableName());
		if(whereParams != null && whereParams.size() > 0){
		    Iterator<String> iterator = whereParams.keySet().iterator();
		    query.append(" WHERE ");
		    while(iterator.hasNext()){
			    String field = iterator.next();
			    query.append(field + " = ?");
			    params.add(whereParams.get(field));
				if(iterator.hasNext()){
					query.append(", ");
				}
	    	}
		}	
		execute(query.toString(), params.toArray());
	}
	
	protected abstract String getTableName();
	
	protected abstract Map<String , Object> format(T entry);
	
	protected abstract T parseEntry(ResultSet rs) throws SQLException;

}
