package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.util.List;

public interface ResultMask {

	List<Field> getResultMask();
	
	List<Field> getResultMask(Class<?> classe);
	
}
