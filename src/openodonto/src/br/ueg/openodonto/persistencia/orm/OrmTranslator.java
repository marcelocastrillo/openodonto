package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.util.List;

public class OrmTranslator {

	private List<Field> fields;
	
	public OrmTranslator(List<Field> fields){
		this.fields = fields;
	}

	public String getColumn(Field field){
		return null;
	}
	
	public String getColumn(String field){
		return null;
	}
	
	public String getFieldName(String column){
		return null;
	}
	
	public Field getField(String column){ //*USADO
		return null;
	}
	
	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	
}
