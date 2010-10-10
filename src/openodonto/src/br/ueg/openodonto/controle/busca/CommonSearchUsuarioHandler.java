package br.ueg.openodonto.controle.busca;

import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.dao.DaoFactory;

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
