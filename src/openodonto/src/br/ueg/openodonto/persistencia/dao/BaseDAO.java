
package br.ueg.openodonto.persistencia.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.ConnectionFactory;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.Query;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Inheritance;
import br.ueg.openodonto.persistencia.orm.OrmFormat;


/**
 *
 * @author Vinicius
 */

public abstract class BaseDAO<T extends Entity> implements Serializable {

    private static final long                serialVersionUID = 186038189036166890L;
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

	public ResultSet executeQuery(String sql, List<Object> params, Integer limit) throws SQLException {
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

	public ResultSet executeQuery(String sql, Object... params) throws SQLException {
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

	public List<Object> execute(String sql, Object[] params) throws SQLException {
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
		return getGeneratedValues(preparedStatement);
	}
	
	public Map<String, Object> formatResultSet(ResultSet rs) throws SQLException{
		if(rs != null){
			Map<String,Object> objects = new HashMap<String ,Object>();
			int count = rs.getMetaData().getColumnCount();
			for(int i = 1; i <= count ; i++){
				objects.put(rs.getMetaData().getColumnName(i) , rs.getObject(i));
			}
			return objects;
		}
		return null;		
	}

	private List<Object> getGeneratedValues(PreparedStatement preparedStatement) throws SQLException{
		ResultSet generatedValues = preparedStatement.getGeneratedKeys();
		List<Object> generatedKeys = new LinkedList<Object>();
		if (generatedValues != null) {
			while (generatedValues.next()) {
				int count = generatedValues.getMetaData().getColumnCount();
				for (int i = 1; i <= count; i++) {
					generatedKeys.add(generatedValues.getObject(i));
				}
			}
		}
		return generatedKeys;
	}
	
	public List<T> listar(String... fields) throws SQLException{
		List<T> lista = new ArrayList<T>();
		Query query = CrudQuery.getListQuery(classe,fields);
		ResultSet rs = executeQuery(query.getQuery(), query.getParams());
		while(rs.next()){
			lista.add(parseEntity(rs));
		}
		return lista;
	}
	
	public void update(T entity,Map<String , Object> whereParams) throws SQLException{
		OrmFormat format = new OrmFormat(entity);
		Map<Class<?> , Map<String , Object>> object = format.formatDisjoin();
		LinkedList<Class<?>> sortedSet = new LinkedList<Class<?>>(object.keySet());
		Iterator<Class<?>> iterator = sortedSet.iterator();
		while(iterator.hasNext()){
			Class<?> type = iterator.next();
			Map<String , Object> localParams = disjoinWhereParams(object , whereParams, type);
			Query query = CrudQuery.getUpdateQuery(object.get(type), localParams, CrudQuery.getTableName(type));
			execute(query.getQuery(), query.getParams().toArray());
		}
	}
	
	public void insert(T entity) throws SQLException{		
		OrmFormat format = new OrmFormat(entity);
		Map<Class<?> , Map<String , Object>> object = format.formatDisjoin();
		LinkedList<Class<?>> sortedSet = new LinkedList<Class<?>>(object.keySet());
		Iterator<Class<?>> iterator = sortedSet.descendingIterator();
		while(iterator.hasNext()){
			Class<?> type = iterator.next();
			Query query = CrudQuery.getInsertQuery(object.get(type), CrudQuery.getTableName(type));
			System.out.println(query.getQuery() + " " + query.getParams());
		}
	}
	
	public void remove(T entity) throws SQLException{
		
	}
	
	private Map<String , Object> disjoinWhereParams(Map<Class<?> , Map<String , Object>> object,Map<String,Object> whereParams, Class<?> type){
		Map<String , Object> localParams = new LinkedHashMap<String, Object>();
		Inheritance inherite = type.getAnnotation(Inheritance.class);
		for(String key : whereParams.keySet()){
			if(object.get(type).containsKey(key)){
				localParams.put(key, whereParams.get(key));
			}
			if(inherite != null){
				for(ForwardKey fk : inherite.joinFields()){
					if(fk.foreginField().equals(key)){
						localParams.put(fk.tableField(), whereParams.get(key));
					}
				}
			}
		}
		return localParams;
	}
	
	public static Map<String, String> getStoredQuerysMap() {
		return storedQuerysMap;
	}
	
	public T parseEntity(ResultSet rs) throws SQLException{
		T entity = getNewEntity();
		OrmFormat format = new OrmFormat(entity);
		format.parse(formatResultSet(rs));
		return entity;
	}
	
	public abstract T getNewEntity();

}
