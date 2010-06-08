package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import br.ueg.openodonto.persistencia.orm.value.EnumValue;

public class OrmResolver {

	private Object                      target;
	private Map<Class<?> , Class<?>>    inheritanceMap;
	
	public OrmResolver(Object target){
		this.target = target;
		inheritanceMap = new LinkedHashMap<Class<?>, Class<?>>();
		mapInheritance();
	}
	
	private void mapInheritance(){
		Class<?> classe = target.getClass();
		while(classe.getSuperclass() != null && classe.getSuperclass().isAnnotationPresent(Table.class)){
			inheritanceMap.put(classe, (classe = classe.getSuperclass()));
		}		
	}
	
	public Map<String , Object> format(){
		return format(target.getClass(), true);
	}
	
    public void parse(Map<String , Object> values){
		parse(values,target.getClass());
	}
	
	public Map<String , Object> format(Class<?> classe, boolean deep){
		List<Field> fields = getAllFields(new LinkedList<Field>(), classe,deep);
		Map<String , Object> map = formatBase(fields);
		return map;		
	}
	
	public Map<String , Object> formatBase(List<Field> fields){
		Map<String , Object> map = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		for(Field field : fields){
			if (field.isAnnotationPresent(Relationship.class)) {
				//TODO tratar relacionamentos
			} else {
				Column column;
				if ((column = field.getAnnotation(Column.class)) != null) {
					map.put(column.name(), getBeanValue(field));
				} else if (!Modifier.isFinal(field.getModifiers())
						&& !Modifier.isStatic(field.getModifiers())) {
					map.put(field.getName(), getBeanValue(field));
				}
			}
		}
		return map;		
	}
	
	public Map<Class<?>,Map<String , Object>> formatDisjoin(){
		if(inheritanceMap.isEmpty()){
			return null;
		}
		Map<Class<?>,Map<String , Object>> disjoinMap = new LinkedHashMap<Class<?>, Map<String,Object>>();
		Iterator<Class<?>> iterator = inheritanceMap.keySet().iterator();
		while(iterator.hasNext()){
			Class<?> key = iterator.next();
			Map<String , Object> value = format(key, false);
			disjoinMap.put(key, value);
		}
		return disjoinMap;
	}
	
	public List<Field> getAllFields(List<Field> fields, Class<?> type, boolean deep) {
	    for (Field field: type.getDeclaredFields()) {
	        fields.add(field);
	    }
	    Class<?> superType = type.getSuperclass();
	    if (superType != null) {
		    Table table = superType.getAnnotation(Table.class);
		    if((table != null && deep) || (table == null && !deep) ){
	            fields = getAllFields(fields, superType ,deep);
		    }
	    }
	    return fields;
	}
	
	public void parse(Map<String , Object> values,Class<?> classe){
		List<Field> fields = getAllFields(new LinkedList<Field>(), classe.getClass(),true);
		for(String column : values.keySet()){
			Field field = findFieldByAnnotation(column, fields);
			if(field.isAnnotationPresent(Relationship.class)){
			    //TODO tratar relacionamentos
			}else{
			    setBeanValue(field, values.get(column));
			}
		}
	}
	
	public Field findFieldByAnnotation(String column,List<Field> fields){
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
	
	private void setBeanValue(Field field , Object value){
		try {
			if(Enum.class.isAssignableFrom(field.getType()) &&
					field.isAnnotationPresent(Enumerator.class)){
				value = reTypeSyncEnum(value, field, field.getAnnotation(Enumerator.class).type());
			}else if(Number.class.isAssignableFrom(field.getType())){
				value = reTypeSyncNumber(value, field.getType());
			}else if(String.class.isAssignableFrom(field.getType())){
				value = reTypeSyncString(value);
			}
			PropertyUtils.setNestedProperty(target, field.getName(), value);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}
	
	private String reTypeSyncString(Object value){
		if(value == null){
			return null;
		}
		return String.valueOf(value);
	}
	
	public Number reTypeSyncNumber(Object value, Class<?> type){
		Number num = (Number)value;
		if(type.isAssignableFrom(Integer.class)){
			return num.intValue();
		}else if(type.isAssignableFrom(Long.class)){
			return num.longValue();
		}else if(type.isAssignableFrom(Short.class)){
			return num.shortValue();
		}else if(type.isAssignableFrom(Byte.class)){
			return num.byteValue();
		}else if(type.isAssignableFrom(Double.class)){
			return num.doubleValue();
		}else if(type.isAssignableFrom(Float.class)){
			return num.floatValue();
		}
		return num;
	}
	
	private Object reTypeSyncEnum(Object value,Field field, EnumValue type){
		if(type == null){
			return null;
		}
		if(((type == EnumValue.ORDINAL && value instanceof Number) ||
				(type == EnumValue.NAME && value instanceof String))
				&& field.getType().isEnum()){
			if(type == EnumValue.ORDINAL){
				Object[] values = field.getType().getEnumConstants();			
				Number index = (Number)value;
				if(index.intValue() >= 0 || index.intValue() < values.length){
					return values[index.intValue()];
				}
			}else if(type == EnumValue.NAME){		
				try {
					return field.getType().getDeclaredField(String.valueOf(value)).get(null);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					e.printStackTrace();
				}
			}
		}
		return null;		
	}
	
	private Object getBeanValue(Field field){
	    try {
	    	Object value = PropertyUtils.getNestedProperty(target, field.getName());
			if(Enum.class.isAssignableFrom(field.getType()) &&
					field.isAnnotationPresent(Enumerator.class)){
				return getEnumValue(value , field.getAnnotation(Enumerator.class).type());
			}
			return value; 
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private Object getEnumValue(Object value , EnumValue type){
		if(type == null){
			return null;
		}
		if(value instanceof Enum<?>){
			Enum<?> enumBean = (Enum<?>)value;
			if(type == EnumValue.ORDINAL){
				return enumBean.ordinal();
			}else if(type == EnumValue.NAME){
				return enumBean.name();
			}else{
				return null;
			}
		}
		return null;
	}

	public Object getTarget() {
		return target;
	}
	
	public Map<Class<?>, Class<?>> getInheritanceMap() {
		return inheritanceMap;
	}
}
