package br.ueg.openodonto.servico.validador;


/**
 * @author vinicius.rodrigues
 *
 */
public abstract class AbstractValidator {

	private String ELCampo;
	
	private String messageOut;
	
	private String message;
	
	public abstract boolean isValid(Object root);

	
	public AbstractValidator(String message) {
		setMessage(message);
	}	
	
	public AbstractValidator(String ELCampo ,String messageOut,String message) {
		this(message);
		setMessageOut(messageOut);
		setELCampo(ELCampo);
	}
	
	public String getELCampo() {	
		return this.ELCampo;
	}
	
	public void setELCampo(String campo) {	
		this.ELCampo = campo;
	}
	
	public String getMessageOut() {	
		return this.messageOut;
	}
	
	public void setMessageOut(String messageOut) {	
		this.messageOut = messageOut;
	}

	
	public String getMessage() {	
		return this.message;
	}


	
	public void setMessage(String message) {	
		this.message = message;
	}
	
	
	
	
}
