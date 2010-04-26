package br.ueg.openodonto.servico.validador;

import br.ueg.openodonto.util.ReflexaoUtil;



/**
 * @author vinicius.rodrigues
 *
 */
public class ValidadorPadrao extends AbstractValidator{

	public boolean isValid(Object root) {
		if(root == null)
			return false;
		Object campoObrigatorio = ReflexaoUtil.getTsimples(root,getELCampo());
		return campoObrigatorio == null || (campoObrigatorio instanceof String && ((String)campoObrigatorio).isEmpty());
	}
	
	public ValidadorPadrao(String ELCampo ,String messageOut){
		super(ELCampo,messageOut,"* Este campo é obrigatório !");
	}
	
	

}
