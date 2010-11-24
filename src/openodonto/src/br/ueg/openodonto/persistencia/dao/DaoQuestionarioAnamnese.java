package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.ueg.openodonto.dominio.Odontograma;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.PacienteQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestaoAnamnese;
import br.ueg.openodonto.dominio.QuestaoQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestionarioAnamnese;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=QuestionarioAnamnese.class)
public class DaoQuestionarioAnamnese extends DaoCrud<QuestionarioAnamnese> {

	private static final long serialVersionUID = -2997010647680704668L;
	
	public DaoQuestionarioAnamnese() {
		super(QuestionarioAnamnese.class);
	}	

	@Override
	public QuestionarioAnamnese getNewEntity() {
		return new QuestionarioAnamnese();
	}
	
	public QuestionarioAnamnese findByKey(Long codigo) throws SQLException {
		OrmFormat format = new OrmFormat(new QuestionarioAnamnese(codigo));
		return super.findByKey(format);
	}
	
	@Override
	protected void afterUpdate(QuestionarioAnamnese o) throws Exception {
		updateRelationship(o);
	}

	@Override
	protected void afterInsert(QuestionarioAnamnese o) throws Exception {
		updateRelationship(o);
	}
	
	private void updateRelationship(QuestionarioAnamnese o) throws Exception {
		DaoQuestaoAnamnese daoQA = (DaoQuestaoAnamnese) DaoFactory.getInstance().getDao(QuestaoAnamnese.class);
		daoQA.updateRelationshipQuestionarioAnamnese(o);
	}
	
	@Override
	protected void afterLoad(QuestionarioAnamnese o) throws Exception {
		DaoQuestaoQuestionarioAnamnese daoQQA = (DaoQuestaoQuestionarioAnamnese) DaoFactory.getInstance().getDao(QuestaoQuestionarioAnamnese.class);		
		List<QuestaoQuestionarioAnamnese> questoes = daoQQA.getQQARelationshipQA(o.getCodigo());
		Map<QuestaoQuestionarioAnamnese,QuestaoAnamnese> questoesMap = new TreeMap<QuestaoQuestionarioAnamnese, QuestaoAnamnese>();
		DaoQuestaoAnamnese daoQA = (DaoQuestaoAnamnese)DaoFactory.getInstance().getDao(QuestaoAnamnese.class);
		for(QuestaoQuestionarioAnamnese qqa : questoes){
			questoesMap.put(qqa, daoQA.findByKey(qqa.getQuestaoAnamneseId()));
		}
		o.setQuestoes(questoesMap);
	}
	
	@Override
	protected boolean beforeRemove(QuestionarioAnamnese o,	Map<String, Object> params) throws Exception {
		EntityManager<QuestaoQuestionarioAnamnese> daoQQA = DaoFactory.getInstance().getDao(QuestaoQuestionarioAnamnese.class);
		for(QuestaoQuestionarioAnamnese qqa : o.getQuestoes().keySet()){
			daoQQA.remover(qqa);
		}
		return true;
	}

	public void updateRelationshipPaciente(Paciente paciente) throws Exception {
		Long pacienteId = paciente.getCodigo();
		/*TODO
		List<QuestionarioAnamnese> questionarios = paciente.getAnamneses();
		if(questionarios != null){
			DaoPacienteQuestionarioAnamnese daoPQA = (DaoPacienteQuestionarioAnamnese) DaoFactory.getInstance().getDao(PacienteQuestionarioAnamnese.class);
			List<PacienteQuestionarioAnamnese> pqas =  daoPQA.getPQARelationshipPaciente(pacienteId);
			for(PacienteQuestionarioAnamnese pqa : pqas){
				if(!containsPQARelationship(questionarios,pqa)){
					daoPQA.remover(pqa);
				}
			}			
			for(QuestionarioAnamnese questionario : questionarios){
				if(!containsQARelationship(pqas,questionario)){
					daoPQA.inserir(new PacienteQuestionarioAnamnese(pacienteId, questionario.getCodigo()));
				}
			}			
		}
		*/
	}

	private boolean containsQARelationship(	List<PacienteQuestionarioAnamnese> pqas,QuestionarioAnamnese questionario) {
		PacienteQuestionarioAnamnese key = new PacienteQuestionarioAnamnese(null, questionario.getCodigo());
		int index = Collections.binarySearch(pqas, key,new Comparator<PacienteQuestionarioAnamnese>() {
			@Override
			public int compare(PacienteQuestionarioAnamnese o1,	PacienteQuestionarioAnamnese o2) {
				return o1.getQuestionarioAnamneseId().compareTo(o2.getQuestionarioAnamneseId());
			}
		});
		return index >= 0;
	}

	private boolean containsPQARelationship(List<QuestionarioAnamnese> questionarios,PacienteQuestionarioAnamnese pqa) {
		QuestionarioAnamnese key = new QuestionarioAnamnese(pqa.getQuestionarioAnamneseId());
		int index = Collections.binarySearch(questionarios, key,new Comparator<QuestionarioAnamnese>() {
			@Override
			public int compare(QuestionarioAnamnese o1, QuestionarioAnamnese o2) {
				return o1.getCodigo().compareTo(o2.getCodigo());
			}
		});
		return index >= 0;
	}

	public List<QuestionarioAnamnese> getQuestionariosAnamnesesRelationshipPaciente(Long idPaciente) throws Exception {
		OrmFormat orm = new OrmFormat(new Odontograma(idPaciente));
		IQuery query = CrudQuery.getSelectQuery(QuestionarioAnamnese.class, orm.formatNotNull(), "*");
		List<QuestionarioAnamnese> loads = getSqlExecutor().executarQuery(query);
		for(QuestionarioAnamnese o : loads){
			afterLoad(o);
		}
		return loads;
	}

}
