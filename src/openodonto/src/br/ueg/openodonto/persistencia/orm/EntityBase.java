package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public class EntityBase{

	public Map<String , Object> format(){
		Map<String , Object> map = new HashMap<String, Object>();
		List<Field> fields = getAllFields(new LinkedList<Field>(), this.getClass());
		for(Field field : fields){
			Column column;
			if((column = field.getAnnotation(Column.class)) != null){
				map.put(column.name(), getBeanValue(field.getName()));
			}else{
				if(!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())){
				    map.put(field.getName(), getBeanValue(field.getName()));
				}
			}
		}
		return map;		
	}
	
	private List<Field> getAllFields(List<Field> fields, Class<?> type) {
	    for (Field field: type.getDeclaredFields()) {
	        fields.add(field);
	    }
	    Class<?> superType = type.getSuperclass();
	    if (superType.getSuperclass() != null) {
		    Table table = superType.getAnnotation(Table.class);
		    if(table != null){
	            fields = getAllFields(fields, type.getSuperclass());
		    }
	    }

	    return fields;
	}

	
	public void parse(Map<String , Object> values){
		List<Field> fields = getAllFields(new LinkedList<Field>(), this.getClass());
		for(String column : values.keySet()){
			setBeanValue(findFieldByAnnotation(column, fields).getName(), values.get(column));
		}
	}
	
	private Field findFieldByAnnotation(String column,List<Field> fields){
		for(Field field : fields){
			Column colAnott;
			if((colAnott = field.getAnnotation(Column.class)) != null){
				if(colAnott.name().equals(column)){
					return field;
				}
			}else if(field.getName().equals(column)){
				return field;
			}
		}
		return null;
	}
	
	private void setBeanValue(String name , Object value){
		try {
			PropertyUtils.setNestedProperty(this, name, value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	private Object getBeanValue(String name){		
	    try {
			return PropertyUtils.getNestedProperty(this, name);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
