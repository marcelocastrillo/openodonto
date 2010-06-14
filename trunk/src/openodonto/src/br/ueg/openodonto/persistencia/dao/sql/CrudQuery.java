package br.ueg.openodonto.persistencia.dao.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
import br.ueg.openodonto.persistencia.orm.Inheritance;
import br.ueg.openodonto.persistencia.orm.MaskResolver;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.persistencia.orm.ResultMask;
import br.ueg.openodonto.persistencia.orm.Table;

public class CrudQuery{
	
	private static Map<Class<?> , String> tableNameCache;
	
	static{
		tableNameCache = new HashMap<Class<?>, String>();		
	}
	
    @SuppressWarnings("unchecked")
	public static <T extends Entity>IQuery getListQuery(Class<T> classe,String... fields){
    	return new Query(getSelectRoot(classe,fields) , Collections.EMPTY_LIST , getTableName(classe));
    }
    
	public static <T extends Entity>IQuery getSelectQuery(Class<?> classe, Map<String , Object> whereParams,String... fields){
		StringBuilder query = new StringBuilder(getSelectRoot(classe,fields));
		String table = getTableName(classe);
		List<Object> params = new ArrayList<Object>();
		makeWhereOfQuery(whereParams, params, query);
    	return new Query(query.toString(), params, table);
    }
	
	public static IQuery getInheritanceConstraintQuery(String table,Map.Entry<String, Map<String , String>> entry,Map<String, Object> whereParams){
		StringBuilder query = new StringBuilder();
		query.append("SELECT 1 FROM ");
		query.append(table);
		query.append(" INNER JOIN ").append(entry.getKey());
		query.append(" ON ");
		Map<String , String> joinAttributes = entry.getValue();
		for(Map.Entry<String, String> join : joinAttributes.entrySet()){
			query.append(table).append(".").append(join.getKey()).append(" = ").append(entry.getKey()).append(".").append(join.getValue());	
		}
		List<Object> params = new ArrayList<Object>();
		makeWhereOfQuery(whereParams, params, query);
		return new Query(query.toString(), params, table);
	}
	
	public static IQuery getInsertQuery(Map<String , Object> object,String table){
		StringBuilder query = new StringBuilder();
		StringBuilder values = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		values.append(" VALUES ( ");
		query.append("INSERT INTO ");
		query.append(table);
		Iterator<String> iterator = object.keySet().iterator();
		query.append("(");
		while(iterator.hasNext()){
			String field = iterator.next();
			query.append(field);
			params.add(object.get(field));
			values.append("?");
			if(iterator.hasNext()){
				query.append(", ");
				values.append(", ");
			}
		}
		values.append(")");
		query.append(")");
		query.append(values);
		return new Query(query.toString(), params,table);
	}
	

    
	public static IQuery getUpdateQuery(Map<String , Object> object, Map<String , Object> whereParams,String table){
		StringBuilder query = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		query.append("UPDATE ");
		query.append(table);
		query.append(" SET ");
		Iterator<String> iterator = object.keySet().iterator();
		while(iterator.hasNext()){
			String field = iterator.next();
			query.append(field + " = ?");
			params.add(object.get(field));
			if(iterator.hasNext()){
				query.append(", ");
			}
		}
		makeWhereOfQuery(whereParams, params, query);
		return new Query(query.toString(), params,table);
	}
	
	public static IQuery getRemoveQuery(Map<String , Object> whereParams,String table){
		StringBuilder query = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		query.append("DELETE FROM ");
		query.append(table);
		makeWhereOfQuery(whereParams, params, query);
		return new Query(query.toString(), params,table);
	}
	
	private static void makeWhereOfQuery(Map<String , Object> whereParams,List<Object> params,StringBuilder query){
		if(whereParams != null && whereParams.size() > 0){
		    Iterator<String> iterator = whereParams.keySet().iterator();
		    query.append(" WHERE ");
		    while(iterator.hasNext()){
			    String field = iterator.next();
			    query.append(field + " = ?");
			    params.add(whereParams.get(field));
				if(iterator.hasNext()){
					query.append(" AND ");
				}
	    	}
		}
	}
	
    public static String getSelectRoot(Class<?> classe,String... fields){
    	StringBuilder stb = new StringBuilder();
    	stb.append("SELECT ");
    	OrmTranslator translator = null;
    	if((fields.length == 1) && (fields[0].equals("*"))){
    		List<Field> allFields = OrmResolver.getAllFields(new LinkedList<Field>(), classe, true);
    		translator = new OrmTranslator(allFields);

    	}else{
    		ResultMask mask = new MaskResolver(classe, fields);
    		translator = new OrmTranslator(mask.getResultMask());
    	}		
		Iterator<String> iterator = translator.getColumnsMap().keySet().iterator();
		while(iterator.hasNext()){
			stb.append(iterator.next());
			if(iterator.hasNext()){
				stb.append(",");
			}
		}
		stb.append(" FROM ");
		stb.append(getTableName(classe));
		stb.append(" ");
		if(classe.isAnnotationPresent(Inheritance.class)){
			stb.append(getFromJoin(classe));
		}
    	return stb.toString();
    }
    
    public static String getFromJoin(Class<?> classe){
    	StringBuilder stb = new StringBuilder();
    	Class<?> type = classe;
    	Class<?> superType = type.getSuperclass();
    	String typeColumnName = getTableName(classe);
    	String superTypeColumnName = getTableName(superType);
    	stb.append("LEFT JOIN ");
    	stb.append(superTypeColumnName);
    	stb.append(" ");
    	for(ForwardKey fk : type.getAnnotation(Inheritance.class).joinFields()){
    		stb.append("ON ");
    		stb.append(typeColumnName).append(".").append(fk.tableField());
    		stb.append(" = ");
    		stb.append(superTypeColumnName).append(".").append(fk.foreginField());
			stb.append(" ");
    	}
    	return stb.toString();
    }
    
    public static String getTableName(Class<?> clazz){
    	String tableName;
    	if((tableName = tableNameCache.get(clazz)) == null ){
    		tableName = clazz.getAnnotation(Table.class).name();
    		tableNameCache.put(clazz, tableName);
    	}
    	return tableName;
    }
    
}
