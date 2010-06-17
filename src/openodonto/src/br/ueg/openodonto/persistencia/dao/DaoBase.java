
package br.ueg.openodonto.persistencia.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLNonTransientException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ueg.openodonto.persistencia.ConnectionFactory;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Inheritance;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.persistencia.orm.OrmResolver;


/**
 *
 * @author Vinicius
 */

public abstract class DaoBase<T extends Entity> implements Serializable,EntityManager<T> {

    private static final long                    serialVersionUID = 186038189036166890L;
    private static Map<String , String>          storedQuerysMap;
    private static Map<String , List<String>>    metaTableAICache;
    
    static{
    	storedQuerysMap = new HashMap<String, String>();
    	metaTableAICache = new HashMap<String, List<String>>();
    }
    
    private Class<T> classe;
    
    public DaoBase(Class<T> classe) {
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

	public ResultSet executeQuery(String sql, Collection<Object> params, Integer limit) throws SQLException {
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

	public Map<String,Object> execute(IQuery query) throws SQLException {
		String sql = query.getQuery();
		Object[] params =  query.getParams().toArray();
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
		String table = query.getTable();
		return getGeneratedAutoIncremetValues(preparedStatement, table);
	}
	
	public Map<String, Object> formatResultSet(ResultSet rs) throws SQLException{
		if(rs != null){
			Map<String,Object> objects = new HashMap<String ,Object>();
			ResultSetMetaData meta = rs.getMetaData();
			int count = meta.getColumnCount();
			for(int i = 1; i <= count ; i++){
				String columnName = meta.getColumnName(i);
				objects.put(columnName, getTypeSyncValue(rs, i));
			}
			return objects;
		}
		return null;		
	}
	
	private List<String> getGeneratedNames(ResultSet rs) throws SQLException{
		List<String> generatedNames = new ArrayList<String>();
		while(rs.next()){
			boolean isAutoIncrement = rs.getString("IS_AUTOINCREMENT").equals("YES");
			if(isAutoIncrement){
				String name = rs.getString("COLUMN_NAME");
				generatedNames.add(name);
			}
		}
		return generatedNames;
	}
	
	private List<Object> getGeneratedValues(PreparedStatement preparedStatement) throws SQLException{
		ResultSet generatedValues = preparedStatement.getGeneratedKeys();
		List<Object> generatedKeys = new LinkedList<Object>();
		if (generatedValues != null) {
			while (generatedValues.next()) {
				int count = generatedValues.getMetaData().getColumnCount();
				for (int i = 1; i <= count; i++) {
					generatedKeys.add(getTypeSyncValue(generatedValues,i));
				}
			}
		}
		return generatedKeys;
	}
	
	private Map<String , Object> getGeneratedResult(List<String> names, List<Object> values){
		if((names.size() != values.size()) && values.size() > 0){
			throw new RuntimeException("Erro recuperar valores gerados");
		}
		Map<String , Object> generateMap = new HashMap<String, Object>();
		Iterator<String> iteratorNames = names.iterator();
		Iterator<Object> iteratorValues = values.iterator();
		while(iteratorNames.hasNext() && iteratorValues.hasNext()){
			generateMap.put(iteratorNames.next(), iteratorValues.next());
		}
		return generateMap;
	}
	
	private Map<String, Object> getGeneratedAutoIncremetValues(PreparedStatement ps, String table) throws SQLException{
		List<String> names;
		if(DaoBase.metaTableAICache.containsKey(table)){
			names = DaoBase.metaTableAICache.get(table); 
		}else{
			ResultSet rs = getConnection().getMetaData().getColumns(null, null, table, null);
			names = getGeneratedNames(rs);
			DaoBase.metaTableAICache.put(table, names);
		}		
		List<Object> values = getGeneratedValues(ps);
		return getGeneratedResult(names, values);
	}
	
	private Object getTypeSyncValue(ResultSet rs,int i) throws SQLException{
		ResultSetMetaData meta = rs.getMetaData();
		switch (meta.getColumnType(i)) {
		case Types.BOOLEAN:
			return rs.getBoolean(i);
		case Types.CHAR:
			return  rs.getString(i);
		case Types.DATE:
			return rs.getDate(i);
		case Types.DOUBLE:
			return rs.getDouble(i);
		case Types.FLOAT:
			return rs.getFloat(i);
		case Types.INTEGER:
			return rs.getInt(i);
		case Types.TIME:
			return rs.getDate(i);
		case Types.TIMESTAMP:
			return rs.getDate(i);
		case Types.VARCHAR:
			return rs.getString(i);
		default:
			return rs.getObject(i);
		}
	}
	
	protected List<String> referencedConstraint(Class<?> type,Map<String , Object> keyParams) throws SQLException{
		List<String> constraintList = new ArrayList<String>();
		ResultSet rs = getConnection().getMetaData().getExportedKeys(null, null, CrudQuery.getTableName(type));
		Map<String, Map<String , String>> joinMap = new HashMap<String, Map<String , String>>();
		Map<String , Object> whereParams = new HashMap<String, Object>();
		String tableName = CrudQuery.getTableName(type);
		while (rs.next()) {
			Map<String, Object> formatedRs = formatResultSet(rs);
			String fkTableName = formatedRs.get("FKTABLE_NAME").toString();
			Map<String , String> joinAttributteMap = new HashMap<String, String>();
			if((joinAttributteMap = joinMap.get(fkTableName)) == null){
				joinAttributteMap = new HashMap<String, String>();
				joinMap.put(fkTableName, joinAttributteMap);
			}
			String pk = formatedRs.get("PKCOLUMN_NAME").toString();
			String fk = formatedRs.get("FKCOLUMN_NAME").toString();
			if(keyParams.containsKey(pk)){
			    whereParams.put(tableName + "." + pk, keyParams.get(pk));
			}else{
				throw new RuntimeException("O valor da coluna " + pk + " é obrigatório");
			}
			joinAttributteMap.put(pk,fk);			
		}
		for(Map.Entry<String, Map<String , String>> entry : joinMap.entrySet()){
			IQuery query = CrudQuery.getInheritanceConstraintQuery(tableName, entry, whereParams);
			ResultSet rsi = executeQuery(query.getQuery(), query.getParams(), 1);
			if(rsi.next()){
				constraintList.add(entry.getKey());
			}
		}		
		return constraintList;
	}
	
	private void applyForwardKey(OrmFormat format,Class<?> type,Map<Class<?> , Map<String , Object>> object){
		if(type.isAnnotationPresent(Inheritance.class)){
			Class<?> inheriter = format.getOrmResolver().getInheritanceMap().get(type);
			ForwardKey[] fks = type.getAnnotation(Inheritance.class).joinFields();
			for(ForwardKey fk : fks){
				Object value = object.get(inheriter).get(fk.foreginField());
				object.get(type).put(fk.tableField(), value);
			}
		}
	}
	
	private Map<String, Object> restoreInheritance(Class<?> type,OrmFormat format) throws SQLException{
		Map<String, Object> whereParams = format.formatKey();
		IQuery query = CrudQuery.getSelectQuery(type, whereParams, "*");
		ResultSet rs = executeQuery(query.getQuery(), query.getParams(),1);
		if(rs.next()){
			return formatResultSet(rs);
		}else{
			return null;
		}
	}

	public List<T> listar(String... fields) throws SQLException{
		List<T> lista = new ArrayList<T>();
		IQuery query = CrudQuery.getListQuery(classe,fields);
		ResultSet rs = executeQuery(query.getQuery(), query.getParams(), null);
		while(rs.next()){
			lista.add(parseEntity(rs));
		}
		return lista;
	}
	
	public void update(T entity,Map<String , Object> whereParams) throws SQLException{
		if(whereParams == null || whereParams.isEmpty()){
			throw new RuntimeException("Parametro WHERE obrigatorio. Caso contrario causaria alteração de todos os dados da tabela");
		}
		OrmFormat format = new OrmFormat(entity);
		Map<Class<?> , Map<String , Object>> object = format.formatDisjoin();
		LinkedList<Class<?>> sortedSet = new LinkedList<Class<?>>(object.keySet());
		Iterator<Class<?>> iterator = sortedSet.iterator();
		while(iterator.hasNext()){
			Class<?> type = iterator.next();
			Map<String , Object> localParams = disjoinAttributes(object.get(type).keySet() , whereParams, type);
			IQuery query = CrudQuery.getUpdateQuery(object.get(type), localParams, CrudQuery.getTableName(type));
			execute(query);
		}
	}
	
	public void insert(T entity) throws SQLException{		
		OrmFormat format = new OrmFormat(entity);
		Map<Class<?> , Map<String , Object>> object = format.formatDisjoin();
		LinkedList<Class<?>> sortedSet = new LinkedList<Class<?>>(object.keySet());
		Iterator<Class<?>> iterator = sortedSet.descendingIterator();
		while(iterator.hasNext()){
			Class<?> type = iterator.next();
			applyForwardKey(format, type, object);
			Map<String, Object> readyInheritance;
			if((readyInheritance = restoreInheritance(type, format)) != null){
				object.put(type, readyInheritance);
				continue;
			}
			IQuery query = CrudQuery.getInsertQuery(object.get(type), CrudQuery.getTableName(type));
			Map<String,Object> generated = execute(query);
			format.parse(generated);
			for(String name : generated.keySet()){
				object.get(type).put(name, generated.get(name));
			}
		}
	}
	
	public void remove(Map<String , Object> whereParams,boolean permissive) throws SQLException{
		if(whereParams == null || whereParams.isEmpty()){
			throw new RuntimeException("Parametro WHERE obrigatorio. Caso contrario causaria remoção de todos os dados da tabela");
		}
		OrmResolver orm = new OrmResolver(classe);
		LinkedList<Class<?>> sortedSet = new LinkedList<Class<?>>(orm.getInheritanceMap().keySet());
		Iterator<Class<?>> iterator = sortedSet.iterator();
		while(iterator.hasNext()){
			Class<?> type = iterator.next();
			Set<String> keysColumns = orm.format(type, false).keySet();
			Map<String , Object> localParams = disjoinAttributes(keysColumns, whereParams, type);
			IQuery query = CrudQuery.getRemoveQuery(localParams, CrudQuery.getTableName(type));
			try{
		        execute(query);
			}catch (SQLNonTransientException e) {
				if(sortedSet.size() > 1 && permissive){
					break;
				}else{
					throw e;
				}
			}
		}
	}
	
	private Map<String , Object> disjoinAttributes(Set<String> keys,Map<String,Object> attributes, Class<?> type){
		Map<String , Object> localParams = new LinkedHashMap<String, Object>();
		Inheritance inherite = type.getAnnotation(Inheritance.class);
		for(String key : attributes.keySet()){
			if(keys.contains(key)){
				localParams.put(key, attributes.get(key));
			}
			if(inherite != null){
				for(ForwardKey fk : inherite.joinFields()){
					if(fk.foreginField().equals(key)){
						localParams.put(fk.tableField(), attributes.get(key));
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
	
	public Class<T> getClasse() {
		return classe;
	}
	
	public abstract T getNewEntity();
	
	

}
