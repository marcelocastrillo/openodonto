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

	public Double getValue(){
		return Double.parseDouble(super.getValue().toString());
	}
	
	protected boolean validate(){
		String strValue = super.getValue().toString();
		if(!numberPattern.matcher(strValue).matches()){
			setErrorMsg("Não é um numero.");
			return false;
		}
		return true;
	}

	
}
