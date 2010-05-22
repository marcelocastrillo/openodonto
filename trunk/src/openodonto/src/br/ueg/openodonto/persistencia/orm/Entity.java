package br.ueg.openodonto.persistencia.orm;

import java.util.Map;

public interface Entity<T> {

	Map<String , Object> parse();

	T format(Map<String , Object> values);
}
