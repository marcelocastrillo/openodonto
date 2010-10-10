package br.ueg.openodonto.persistencia.dao.sql;

import java.util.Iterator;

public interface SqlWhereOperator {

	SqlOperationType           getType();
	Iterator<SqlWhereOperand>  getOperandIterator();  
	SqlWhereExpression         getExpression();
}
