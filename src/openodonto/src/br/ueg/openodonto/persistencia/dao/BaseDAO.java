
package br.ueg.openodonto.persistencia.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.ConnectionFactory;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Inheritance;
import br.ueg.openodonto.persistencia.orm.MaskResolver;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.persistencia.orm.ResultMask;
import br.ueg.openodonto.persistencia.orm.Table;


/**
 *
 * @author Vinicius
 */

public abstract class BaseDAO<T extends Entity> implements Serializable {

    private static final long serialVersionUID = 186038189036166890L;
    private static Map<String , String>      storedQuerysMap;
    
    static{
    	storedQuerysMap = new HashMap<String, String>();
    }
    
    private Class<T> classe;
    
    public BaseDAO(Class<T> classe) {
    	this.classe = classe;
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

	public List<Object> execute(String sql, Object[] params) throws Exception {
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
		List<Object> generatedKeys = new LinkedList<Object>();
		if(generatedValues != null){
			while(generatedValues.next()){
			int count = generatedValues.getMetaData().getColumnCount();
			for(int i = 1; i <= count ; i++){
				generatedKeys.add(generatedValues.getObject(i));
			}
			}
		}
		return generatedKeys;
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
	
	public ResultSet list() throws Exception{
		return null;//return executeQuery(getSelectRoot(null, "*"), Collections.EMPTY_LIST);
	}	
	
	public String getTableName(){
		return classe.getAnnotation(Table.class).name();
	}	
	
	public static Map<String, String> getStoredQuerysMap() {
		return storedQuerysMap;
	}
	
	public abstract Map<String , Object> format(T entry);
	
	public abstract T parseEntry(ResultSet rs) throws SQLException;

}
