package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class EntityBase<T> {

	public Map<String , Object> parse(){
		Map<String , Object> map = new HashMap<String, Object>();
		Class<?> current = this.getClass();
		Field[] fields = current.getDeclaredFields();
		for(Field field : fields){
			Column name;
			if((name = field.getAnnotation(Column.class)) != null){
				map.put(field.getName(), getBeanValue(name.name()));
			}else{
				map.put(field.getName(), getBeanValue(field.getName()));
			}
		}
		return map;		
	}
	
	public T format(Map<String , Object> values){
	    return null;	
	}
	
	private Object getBeanValue(String name){
		
	    return null;
	}
	
}
