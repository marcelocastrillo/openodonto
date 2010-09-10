package br.ueg.openodonto.validacao;

import java.util.regex.Pattern;

import br.ueg.openodonto.validacao.tipo.NumericValidatorType;

public class NumberValidator extends AbstractValidator implements NumericValidatorType{

	protected static Pattern numberPattern;
	
	static{
		numberPattern = Pattern.compile("^(\\d|-){1}\\d*\\.?\\d*$");
	}	
	
	public NumberValidator(Object value) {
		super(null, value);
	}
	
	public NumberValidator(Validator next) {
		super(next, next.getValue());
	}

	public Number getValue(){
		return Double.valueOf(super.getValue().toString());
	}
	
	protected boolean validate(){
		String sValue = super.getValue().toString();
		if(!numberPattern.matcher(sValue).matches()){
			setErrorMsg("Não é um numero.");
			return false;
		}
		return true;
	}

	
}
