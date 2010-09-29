package br.ueg.openodonto.persistencia.dao;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.orm.Dao;

@Dao(classe=Paciente.class)
public class DaoPaciente extends DaoAbstractPessoa<Paciente>{

    private static final long serialVersionUID = -4278752127118870714L;

	public DaoPaciente() {
		super(Paciente.class);
	}

	@Override
	public Paciente getNewEntity() {
		return new Paciente(); // Melhor que reflexão
	}

}
