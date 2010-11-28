package br.ueg.openodonto.validator;

import br.ueg.openodonto.validator.tipo.StringValidatorType;

public class CpfValidator extends AbstractValidator implements StringValidatorType{

	public CpfValidator(Object value) {
		this(null, value);
	}
	
	public CpfValidator(Validator next, Object value) {
		super(next, value);
	}

	@Override
	public String getValue() {
		return super.getValue().toString();
	}
	
	@Override
	protected boolean validate() {
		return false;
	}

}
