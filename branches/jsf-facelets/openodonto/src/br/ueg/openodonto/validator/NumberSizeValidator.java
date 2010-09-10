package br.ueg.openodonto.validator;

import br.ueg.openodonto.validator.tipo.NumericValidatorType;

public class NumberSizeValidator extends AbstractValidator implements NumericValidatorType{
	
	private int size;
	
	public NumberSizeValidator(Validator validator, int size) {
		super(validator, validator.getValue());
		this.size = size;
	}
	
	public NumberSizeValidator(Object value, int size) {
		super(null, value);
		this.size = size;
	}
	
	@Override
	public Number getValue() {
		return Double.valueOf(super.getValue().toString());
	}
	
	@Override
	protected boolean validate() {
		if(getValue().intValue() > size){
			setErrorMsg("Numero muito grande : Máximo permitido = " + size);
			return false;
		}
		return true;
	}
	
}
