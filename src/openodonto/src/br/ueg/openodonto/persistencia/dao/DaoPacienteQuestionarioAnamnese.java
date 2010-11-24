package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.PacienteAnamneseResposta;
import br.ueg.openodonto.dominio.PacienteQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestaoAnamnese;
import br.ueg.openodonto.dominio.QuestaoQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestionarioAnamnese;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;

@Dao(classe=PacienteQuestionarioAnamnese.class)
public class DaoPacienteQuestionarioAnamnese extends DaoCrud<PacienteQuestionarioAnamnese> {

	public DaoPacienteQuestionarioAnamnese() {
		super(PacienteQuestionarioAnamnese.class);
	}

	private static final long serialVersionUID = 2819939273529100056L;

	@Override
	public PacienteQuestionarioAnamnese getNewEntity() {
		return new PacienteQuestionarioAnamnese();
	}
	
	@Override
	protected void afterUpdate(PacienteQuestionarioAnamnese o) throws Exception {
	    updateRelationship(o);
	}
	
	@Override
	protected void afterInsert(PacienteQuestionarioAnamnese o) throws Exception {
	    updateRelationship(o);
	}

	private void updateRelationship(PacienteQuestionarioAnamnese o){
	    DaoPacienteAnamneseRespostas daoPAR = (DaoPacienteAnamneseRespostas) DaoFactory.getInstance().getDao(PacienteAnamneseResposta.class);
	    daoPAR.updateRelationshipPQA(o);
	}
	
	@Override
	protected void afterLoad(PacienteQuestionarioAnamnese o) throws Exception {
	    //TODO LOAD PACIENTE RESPOSTAS
	    DaoQuestaoQuestionarioAnamnese daoQQA = (DaoQuestaoQuestionarioAnamnese)DaoFactory.getInstance().getDao(QuestaoQuestionarioAnamnese.class);
	    DaoPacienteAnamneseRespostas daoPAR = (DaoPacienteAnamneseRespostas) DaoFactory.getInstance().getDao(PacienteAnamneseResposta.class);
	    Map<QuestaoAnamnese,PacienteAnamneseResposta> QuestaoRespostasMap = new HashMap<QuestaoAnamnese, PacienteAnamneseResposta>();
	    List<QuestaoAnamnese> questoes = null;
	    for(QuestaoAnamnese questao : questoes){
	        daoPAR.getRespostasRelationshipPQA(o.getPacienteId(),o.getQuestionarioAnamneseId(),questao.getCodigo());
	    }
	    
	}
	
	@Override
	protected boolean beforeRemove(PacienteQuestionarioAnamnese o,Map<String, Object> params) throws Exception {
	    DaoPacienteAnamneseRespostas daoPAR = (DaoPacienteAnamneseRespostas) DaoFactory.getInstance().getDao(PacienteAnamneseResposta.class);
	    for(PacienteAnamneseResposta par : o.getRespotas().values()){
	        daoPAR.remover(par);
	    }
	    return true;
	}
	
	public List<Paciente> getPacientes(QuestionarioAnamnese questionario,Paciente paciente) throws SQLException{
		return getRelacionamento(questionario,paciente,"questionarioAnamneseId","pacienteId");
	}
	
	public List<QuestionarioAnamnese> getQuestionarios(Paciente paciente,QuestionarioAnamnese questionario) throws SQLException{
		return getRelacionamento(paciente,questionario,"pacienteId","questionarioAnamneseId");
	}

	public List<Paciente> getPacientes(Long questionarioAnamneseId) throws SQLException{
		Map<String,Object> whereParams = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		whereParams.put(translator.getColumn("questionarioAnamneseId"), questionarioAnamneseId);
		return getRelacionamento(Paciente.class,"pacienteId",whereParams);
	}
	
	public List<QuestionarioAnamnese> getQuestionarios(Long pacienteId) throws SQLException{
		Map<String,Object> whereParams = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		whereParams.put(translator.getColumn("pacienteId"), pacienteId);
		return getRelacionamento(QuestionarioAnamnese.class,"questionarioAnamneseId",whereParams);
	}
	
	public List<PacienteQuestionarioAnamnese> getPQARelationshipPaciente(Long pacienteId) throws SQLException {
		OrmFormat orm = new OrmFormat(new PacienteQuestionarioAnamnese(pacienteId, null));
		IQuery query = CrudQuery.getSelectQuery(PacienteQuestionarioAnamnese.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);
	}
	
	public List<PacienteQuestionarioAnamnese> getPQARelationshipQA(Long questionarioAnamneseId) throws SQLException {
		OrmFormat orm = new OrmFormat(new PacienteQuestionarioAnamnese(null, questionarioAnamneseId));
		IQuery query = CrudQuery.getSelectQuery(PacienteQuestionarioAnamnese.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);
	}

}
