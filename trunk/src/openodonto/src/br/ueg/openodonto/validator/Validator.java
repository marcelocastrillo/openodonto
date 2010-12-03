package br.ueg.openodonto.validator;

public interface Validator {

	boolean isValid();
	String getErrorMessage();
	Validator getSource();
	Object getValue();
	void setValue(Object value);
	Validator concatBegin(Validator next);
	Validator concat(Validator next);
}
