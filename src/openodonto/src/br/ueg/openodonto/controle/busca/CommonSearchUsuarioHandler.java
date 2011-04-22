package br.ueg.openodonto.controle.busca;

import br.com.simple.jdbc.dao.DaoFactory;
import br.ueg.openodonto.dominio.Usuario;

public class CommonSearchUsuarioHandler extends CommonSearchBeanHandler<Usuario>{
	private String[] showColumns = {"codigo", "nome", "user",};	
	public CommonSearchUsuarioHandler() {
		super(Usuario.class, DaoFactory.getInstance().getDao(Usuario.class));
	}
	@Override
	public String[] getShowColumns() {
		return showColumns;
	}

}
