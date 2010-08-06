package br.ueg.openodonto.controle.validador;

import java.lang.reflect.InvocationTargetException;

import br.ueg.openodonto.util.PBUtil;

/**
 * @author vinicius.rodrigues
 * 
 */
public class ValidadorPadrao extends AbstractValidator {

    public boolean isValid(Object root) {
	if (root == null)
	    return false;
	Object campoObrigatorio = null;
	try {
	    campoObrigatorio = PBUtil.instance().getNestedProperty(root,
		    getELCampo());
	} catch (IllegalAccessException e) {
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	}
	return campoObrigatorio == null
		|| (campoObrigatorio instanceof String && ((String) campoObrigatorio)
			.isEmpty());
    }

    public ValidadorPadrao(String ELCampo, String messageOut) {
	super(ELCampo, messageOut, "* Este campo é obrigatório !");
    }

}
