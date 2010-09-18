package br.ueg.openodonto.persistencia.dao.sql;

import java.util.Iterator;

public interface SqlWhereExpression {
	
	Iterator<String> getExpressionIterator();
	SqlWhereOperator getOperator();
	
}
