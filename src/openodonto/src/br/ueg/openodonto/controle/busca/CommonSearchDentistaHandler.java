package br.ueg.openodonto.controle.busca;

import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.persistencia.dao.DaoFactory;

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
