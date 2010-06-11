package br.ueg.openodonto.persistencia.dao.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
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
	
	
    @SuppressWarnings("unchecked")
	public static <T extends Entity>Query getListQuery(Class<T> classe,String... fields){
    	return new Query(getSelectRoot(classe,fields) , Collections.EMPTY_LIST);
    }
	
	public static Query getInsertQuery(Map<String , Object> object,String table){
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
		return new Query(query.toString(), params);
	}
	

    
	public static Query getUpdateQuery(Map<String , Object> object, Map<String , Object> whereParams,String table){
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
		return new Query(query.toString(), params);
	}
	
	public static Query getRemoveQuery(Map<String , Object> whereParams,String table){
		StringBuilder query = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		query.append("DELETE FROM ");
		query.append(table);
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
		return new Query(query.toString(), params);
	}	

    public static <T extends Entity>String getSelectRoot(Class<T> classe,String... fields){
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
    
    public static <T extends Entity>String getFromJoin(Class<T> classe){
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
    	return clazz.getAnnotation(Table.class).name();
    }
    
}
