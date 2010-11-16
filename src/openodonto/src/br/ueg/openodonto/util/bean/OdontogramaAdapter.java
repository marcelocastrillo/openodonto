package br.ueg.openodonto.util.bean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import br.ueg.openodonto.dominio.Odontograma;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.util.WordFormatter;
import br.ueg.openodonto.validator.EmptyValidator;
import br.ueg.openodonto.validator.NullValidator;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class OdontogramaAdapter {

	private static final DateFormat  df = new SimpleDateFormat("dd/MM/yyyy");
	
	private Odontograma   		odontograma;	
	private String        		abstractDescricao;
	private Validator     		validatorDesc;
	private MessageDisplayer 	displayer;
	
	public OdontogramaAdapter(Odontograma odontograma,MessageDisplayer displayer) {
		validatorDesc = ValidatorFactory.newStrMaxLen(500, false);
		this.odontograma = odontograma;
		this.displayer = displayer;
		formatDescricao();
	}
	
	public void formatDescricao(){
		this.abstractDescricao = WordFormatter.abstractStr(getDescricao(), 36);
	}
	
	public String getDescricao() {
		return getOdontograma().getDescricao();
	}
	
	public void setDescricao(String descricao){
		validatorDesc.setValue(descricao);
		if(isValidDec()){
			getOdontograma().setDescricao(descricao);
		}
	}
	
	private boolean isValidDec(){
		boolean valid = true;
		String formName = getDisplayer().getView().getProperties().get("formularioSaida");
		Class<?>[] allowed = {NullValidator.class,EmptyValidator.class};
		if(!validatorDesc.isValid() && ValidatorFactory.checkInvalidPermiteds(validatorDesc, allowed)){
			getDisplayer().getView().addResourceDynamicMenssage("* Descrição : " +
					validatorDesc.getErrorMessage(),
					formName+":"+getDisplayer().getOutput());
			valid = false;
		}
		return valid;
	}
	
	public String getData(){		
		if(getOdontograma().getData() != null ){
			try{
				return df.format(getOdontograma().getData());
			}catch (Exception e) {
				return "";
			}
		}else{
			return "";
		}
	}

	public Odontograma getOdontograma() {
		return odontograma;
	}

	public void setOdontograma(Odontograma odontograma) {
		this.odontograma = odontograma;
	}

	public String getAbstractDescricao() {
		return abstractDescricao;
	}

	public void setAbstractDescricao(String abstractDescricao) {
	}

	public MessageDisplayer getDisplayer() {
		return displayer;
	}

	public void setDisplayer(MessageDisplayer displayer) {
		this.displayer = displayer;
	}	
	
}
