package br.ueg.openodonto.controle.busca;

import br.com.simple.jdbc.dao.DaoFactory;
import br.ueg.openodonto.dominio.Paciente;

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
