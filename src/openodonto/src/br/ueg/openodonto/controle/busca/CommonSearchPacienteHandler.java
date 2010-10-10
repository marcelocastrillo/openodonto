package br.ueg.openodonto.controle.busca;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.dao.DaoFactory;

public class CommonSearchPacienteHandler extends CommonSearchBeanHandler<Paciente>{

	private String[] showColumns = {"codigo", "nome", "email", "cpf"};
	
	public CommonSearchPacienteHandler() {
		super(Paciente.class, DaoFactory.getInstance().getDao(Paciente.class));
	}

	@Override
	public String[] getShowColumns() {
		return showColumns;
	}

}
