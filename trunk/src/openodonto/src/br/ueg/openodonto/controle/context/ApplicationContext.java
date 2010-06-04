package br.ueg.openodonto.controle.context;

import br.ueg.openodonto.dominio.Usuario;

public interface ApplicationContext {

	Usuario getUsuarioSessao();
	String  getParameter(String name);
	<T> T  getAttribute(String name,Class<T> classe);
	
}
