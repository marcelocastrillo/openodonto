package br.ueg.openodonto.controle.busca;

import br.com.simple.jdbc.dao.DaoFactory;
import br.ueg.openodonto.dominio.Dentista;

public class CommonSearchDentistaHandler extends CommonSearchBeanHandler<Dentista>{

	private String[] showColumns = {"codigo", "nome", "cro", "especialidade"};
	
	public CommonSearchDentistaHandler() {
		super(Dentista.class, DaoFactory.getInstance().getDao(Dentista.class));
	}

	@Override
	public String[] getShowColumns() {
		return showColumns;
	}

}
