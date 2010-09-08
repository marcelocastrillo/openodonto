package br.ueg.openodonto.validacao;

public interface Validator {

	boolean isValid();
	String getErrorMessage();
	Validator getSource();
	Object getValue();
	void setValue(Object value);
}
