package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class OrmFormat{
	
	private OrmResolver ormResolver;
	
	public OrmFormat(Object target) {
		this.ormResolver = new OrmResolver(target);
	}

	public Map<String , Object> format(){
		return ormResolver.format();
	}
	
	public Map<Class<?>,Map<String , Object>> formatDisjoin(){
		return ormResolver.formatDisjoin();
	}
	
	public Map<String , Object> format(String... fields){
		ResultMask mask = new MaskResolver(ormResolver.getTarget().getClass(), fields);
		return ormResolver.formatBase(mask.getResultMask());
	}
	
	public Map<String , Object> formatNotNull(){
		Class<?> type = getOrmResolver().getTarget().getClass();
		List<Field> fields = ormResolver.getNotNullFields(new ArrayList<Field>(), type, true);
		return ormResolver.formatBase(fields);
	}
	
	public Map<String , Object> formatKey(){
		Class<?> type = getOrmResolver().getTarget().getClass();
		List<Field> fields = OrmResolver.getKeyFields(new ArrayList<Field>(), type, true);
		return ormResolver.formatBase(fields);
	}
	
	public Map<Class<?>,Map<String , Object>> formatDisjoin(String... fields){
		ResultMask mask = new MaskResolver(this.getClass(), fields);
		Map<Class<?>,Map<String , Object>> map = new LinkedHashMap<Class<?>, Map<String,Object>>();
		for(Class<?> classe : ormResolver.getInheritanceMap().keySet()){
			map.put(classe , ormResolver.formatBase(mask.getResultMask(classe)));
		}
		return map;
	}

    public void parse(Map<String , Object> values){
    	ormResolver.parse(values);
    }
	
    public OrmResolver getOrmResolver() {
		return ormResolver;
	}
	
}
