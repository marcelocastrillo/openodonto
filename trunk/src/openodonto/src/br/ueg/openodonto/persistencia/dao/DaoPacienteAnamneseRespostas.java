package br.ueg.openodonto.persistencia.dao;

import br.ueg.openodonto.dominio.PacienteAnamneseRespostas;
import br.ueg.openodonto.persistencia.orm.Dao;

@Dao(classe=PacienteAnamneseRespostas.class)
public class DaoPacienteAnamneseRespostas extends DaoCrud<PacienteAnamneseRespostas>{

	private static final long serialVersionUID = 3114100911322875992L;
	
	public DaoPacienteAnamneseRespostas() {
		super(PacienteAnamneseRespostas.class);
	}	

	@Override
	public PacienteAnamneseRespostas getNewEntity() {
		return new PacienteAnamneseRespostas();
	}

}
