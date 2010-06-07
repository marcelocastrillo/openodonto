package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.orm.value.EnumValue;

public class OrmResolver {

	private Object target;
	private Map<Class<?> , Class<?>> inheritanceMap;
	
	public OrmResolver(Object target){
		this.target = target;
		inheritanceMap = new LinkedHashMap<Class<?>, Class<?>>();
		configureInheritance();
	}
	
	private void configureInheritance(){
		Class<?> classe = target.getClass();
		while(classe.getSuperclass() != null){
			inheritanceMap.put(classe, classe.getSuperclass());
			classe = classe.getSuperclass();
		}		
	}
	
	public static void main(String[] args) {
		OrmResolver orm = new OrmResolver(new Paciente());
		System.out.println(orm.inheritanceMap);
	}
	
	public Map<String , Object> format(){
		Map<String , Object> map = new HashMap<String, Object>();
		List<Field> fields = getAllFields(new LinkedList<Field>(), this.getClass());
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
	
	public Map<Class<? extends Entity>,Map<String , Object>> formatWithInheritance(){
		return null;		
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
			Field field = findFieldByAnnotation(column, fields);
			if(field.isAnnotationPresent(Relationship.class)){
			//TODO tratar relacionamentos
			}else{
			    setBeanValue(field, values.get(column));
			}
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
			PropertyUtils.setNestedProperty(this, field.getName(), value);
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
	    	Object value = PropertyUtils.getNestedProperty(this, field.getName());
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
	
}
