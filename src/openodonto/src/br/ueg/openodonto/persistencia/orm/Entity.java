package br.ueg.openodonto.persistencia.orm;

import java.util.Map;

public interface Entity{

    Map<String , Object> format();
	
	Map<Class<?>,Map<String , Object>> formatDisjoin();
	
	Map<String , Object> format(String... fields);
	
	Map<Class<?>,Map<String , Object>> formatDisjoin(String... fields);

    void parse(Map<String , Object> values);
	

}
