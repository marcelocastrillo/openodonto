package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrmTranslator {

	private List<Field>          fields;
	private Map<String, Field>   columnsMap;
	
	public OrmTranslator(List<Field> fields){
		this.fields = fields;
		this.columnsMap = new HashMap<String, Field>();
		resolveColumns();
	}
	
	private String resolveColumnName(Field field){
		Column column;
		if ((column = field.getAnnotation(Column.class)) != null) {
            return column.name();
		} else if (!Modifier.isFinal(field.getModifiers())
				&& !Modifier.isStatic(field.getModifiers())) {
			return field.getName();
		}else{
		    return null;
		}
	}
	
	private void resolveColumns(){
		for(Field field : fields){
			if (field.isAnnotationPresent(Relationship.class)) {
				//TODO tratar relacionamentos
			}else{
			    String columnName = getColumn(field);
			    if(columnName != null){
			       columnsMap.put(columnName , field);
			    }
			}
		}
	}
	
	private Field findFieldByAnnotation(String column){
		return columnsMap.get(column);
	}

	public String getColumn(Field field){
		if(field == null){
			return null;
		}
		String columnName = resolveColumnName(field);
		return columnName;
	}
	
	public String getColumn(String field){
		for(Field f : fields){
			if(f.getName().equals(field)){
				return getColumn(f);
			}
		}
		return null;
	}
	
	public String getFieldName(String column){
		Field field = findFieldByAnnotation(column);
		if(field != null){
			return field.getName();
		}
		return null;
	}
	
	public Field getField(String column){
		return findFieldByAnnotation(column);
	}
	
	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public Map<String, Field> getColumnsMap() {
		return columnsMap;
	}

	public void setColumnsMap(Map<String, Field> columnsMap) {
		this.columnsMap = columnsMap;
	}
	
}
