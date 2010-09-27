package br.ueg.openodonto.controle.busca;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.EntityManager;

public abstract class CommonSearchPacienteHandler extends CommoSearchBeanHandler<Paciente>{

	private String[] showColumns = {"codigo", "nome", "email", "cpf"};
	
	public CommonSearchPacienteHandler(EntityManager<Paciente> dao) {
		super(Paciente.class, dao);
	}

	@Override
	public String[] getShowColumns() {
		return showColumns;
	}

}
