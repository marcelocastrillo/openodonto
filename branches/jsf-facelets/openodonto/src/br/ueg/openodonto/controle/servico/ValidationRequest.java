package br.ueg.openodonto.controle.servico;

import br.ueg.openodonto.validator.Validator;

public class ValidationRequest {

	private String     path;
	private Validator  validator;
	private String     out;
	
	public ValidationRequest(String path, Validator validator, String out) {
		super();
		this.path = path;
		this.validator = validator;
		this.out = out;
	}
	
	public ValidationRequest() {
	}
	
	public String getPath() {
		return path;
	}	
	public void setPath(String path) {
		this.path = path;
	}
	public Validator getValidator() {
		return validator;
	}
	public void setValidator(Validator validator) {
		this.validator = validator;
	}
	public String getOut() {
		return out;
	}
	public void setOut(String out) {
		this.out = out;
	}
	
}
