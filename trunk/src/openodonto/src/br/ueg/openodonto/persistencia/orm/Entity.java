package br.ueg.openodonto.persistencia.orm;

import java.util.Map;

public interface Entity{

	Map<String , Object> format();

	void parse(Map<String , Object> values);
}
