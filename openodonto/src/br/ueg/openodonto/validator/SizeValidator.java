package br.ueg.openodonto.validator;

import br.ueg.openodonto.validator.tipo.StringValidatorType;

public class SizeValidator extends AbstractValidator implements StringValidatorType{

	private int size;
		
	public SizeValidator(Validator validator, int size) {
		super(validator, validator.getValue());
		this.size = size;
	}
	
	public SizeValidator(String value, int size) {
		super(null, value);
		this.size = size;
	}
	
	public String getValue(){
		return super.getValue().toString();
	}
	
	@Override
	protected boolean validate() {
		if(getValue().length() > size){
			setErrorMsg("Valor muito longo : Máximo permitido = " + size);
			return false;
		}
		return true;
	}

}
