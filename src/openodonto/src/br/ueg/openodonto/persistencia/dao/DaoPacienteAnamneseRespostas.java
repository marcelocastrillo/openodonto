package br.ueg.openodonto.persistencia.dao;

import java.util.List;

import br.ueg.openodonto.dominio.PacienteAnamneseResposta;
import br.ueg.openodonto.dominio.PacienteQuestionarioAnamnese;
import br.ueg.openodonto.persistencia.orm.Dao;

@Dao(classe=PacienteAnamneseResposta.class)
public class DaoPacienteAnamneseRespostas extends DaoCrud<PacienteAnamneseResposta>{

	private static final long serialVersionUID = 3114100911322875992L;
	
	public DaoPacienteAnamneseRespostas() {
		super(PacienteAnamneseResposta.class);
	}	

	@Override
	public PacienteAnamneseResposta getNewEntity() {
		return new PacienteAnamneseResposta();
	}
	
	public void updateRelationshipPQA(PacienteQuestionarioAnamnese pqa){
	    //TODO CRUD Respostas
	}

	public List<PacienteAnamneseResposta> getRespostasRelationshipPQA(Long idPaciente,Long idQuestionarioAnamnese,Long idQuestao){
	    //TODO GET Relationship Respostas
	    return null;
	}
	
}
