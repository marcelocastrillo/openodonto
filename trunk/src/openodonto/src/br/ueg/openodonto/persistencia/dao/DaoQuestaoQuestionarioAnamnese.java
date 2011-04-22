package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.simple.jdbc.annotation.Dao;
import br.com.simple.jdbc.dao.DaoCrud;
import br.com.simple.jdbc.orm.OrmFormat;
import br.com.simple.jdbc.orm.OrmTranslator;
import br.com.simple.jdbc.sql.CrudQuery;
import br.com.simple.jdbc.sql.IQuery;
import br.ueg.openodonto.dominio.QuestaoAnamnese;
import br.ueg.openodonto.dominio.QuestaoQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestionarioAnamnese;

@Dao(classe=QuestaoQuestionarioAnamnese.class)
public class DaoQuestaoQuestionarioAnamnese extends	DaoCrud<QuestaoQuestionarioAnamnese> {

	private static final long serialVersionUID = -2139673497915771578L;
	
	public DaoQuestaoQuestionarioAnamnese() {
		super(QuestaoQuestionarioAnamnese.class);
	}	

	@Override
	public QuestaoQuestionarioAnamnese getNewEntity() {
		return new QuestaoQuestionarioAnamnese();
	}

	public List<QuestionarioAnamnese> getQuestionarios(QuestaoAnamnese questao,QuestionarioAnamnese questionario) throws SQLException{
		return getRelacionamento(questao, questionario, "questaoAnamneseId", "questionarioAnamneseId");
	}
	
	public List<QuestaoAnamnese> getQuestoes(QuestaoAnamnese questionario,QuestaoAnamnese questao) throws SQLException{
		return getRelacionamento(questionario, questao,"questionarioAnamneseId","questaoAnamneseId");
	}
	
	public List<QuestionarioAnamnese> getQuestionarios(Long questaoAnamneseId) throws SQLException{
		Map<String,Object> whereParams = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		whereParams.put(translator.getColumn("questaoAnamneseId"), questaoAnamneseId);
		return getRelacionamento(QuestionarioAnamnese.class,"questionarioAnamneseId",whereParams);
	}
	
	public List<QuestaoAnamnese> getQuestoes(Long questionarioAnamneseId) throws SQLException{
		Map<String,Object> whereParams = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		whereParams.put(translator.getColumn("questionarioAnamneseId"), questionarioAnamneseId);
		return getRelacionamento(QuestaoAnamnese.class,"questaoAnamneseId",whereParams);
	}
	
	public List<QuestaoQuestionarioAnamnese> getQQARelationshipQA(Long questionarioAnamneseId) throws SQLException {
		OrmFormat orm = new OrmFormat(new QuestaoQuestionarioAnamnese(null, questionarioAnamneseId));
		IQuery query = CrudQuery.getSelectQuery(QuestaoQuestionarioAnamnese.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);		
	}
	
	public List<QuestaoQuestionarioAnamnese> getQQARelationshipQuestaoA(Long questaoAnamneseId) throws SQLException {
		OrmFormat orm = new OrmFormat(new QuestaoQuestionarioAnamnese(questaoAnamneseId, null));
		IQuery query = CrudQuery.getSelectQuery(QuestaoQuestionarioAnamnese.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);		
	}

}
