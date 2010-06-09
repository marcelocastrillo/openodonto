package br.ueg.openodonto.persistencia.orm;

import java.util.LinkedHashMap;
import java.util.Map;


public class EntityBase{
	
	private OrmResolver ormResolver;
	
	public EntityBase() {
		this.ormResolver = new OrmResolver(this);
	}

	public Map<String , Object> format(){
		return ormResolver.format();
	}
	
	public Map<Class<?>,Map<String , Object>> formatDisjoin(){
		return ormResolver.formatDisjoin();
	}
	
	public Map<String , Object> format(String... fields){
		ResultMask mask = new MaskResolver(this.getClass(), fields);
		return ormResolver.formatBase(mask.getResultMask());
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
	
	
}
