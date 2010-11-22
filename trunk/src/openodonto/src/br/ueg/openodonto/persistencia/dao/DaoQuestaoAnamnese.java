package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import br.ueg.openodonto.dominio.QuestaoAnamnese;
import br.ueg.openodonto.dominio.QuestaoQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestionarioAnamnese;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=QuestaoAnamnese.class)
public class DaoQuestaoAnamnese extends DaoCrud<QuestaoAnamnese> { //DaoProduto

	private static final long serialVersionUID = 3460742959166812919L;
	
	public DaoQuestaoAnamnese() {
		super(QuestaoAnamnese.class);
	}

	@Override
	public QuestaoAnamnese getNewEntity() {
		return new QuestaoAnamnese();
	}
	
	public QuestaoAnamnese findByKey(Long codigo) throws SQLException{
		OrmFormat orm = new OrmFormat(new QuestaoAnamnese(codigo));
		return super.findByKey(orm);
	}

	public void updateRelationshipQuestionarioAnamnese(QuestionarioAnamnese questionarioAnamnese) throws Exception {
		Long questionarioAnamneseId = questionarioAnamnese.getCodigo();
		List<QuestaoAnamnese> questoes = new ArrayList<QuestaoAnamnese>(questionarioAnamnese.getQuestoes().values());
		if(questoes != null){
			DaoQuestaoQuestionarioAnamnese daoQQA = (DaoQuestaoQuestionarioAnamnese) DaoFactory.getInstance().getDao(QuestaoQuestionarioAnamnese.class);
			List<QuestaoQuestionarioAnamnese> qqas =   daoQQA.getQQARelationshipQA(questionarioAnamneseId);
			for(QuestaoQuestionarioAnamnese qqa : qqas){
				if(!containsQQARelationship(questoes,qqa)){
					daoQQA.remover(qqa);
				}
			}			
			Iterator<Entry<QuestaoQuestionarioAnamnese, QuestaoAnamnese>> iterator = questionarioAnamnese.getQuestoes().entrySet().iterator();
			while(iterator.hasNext()){
				Entry<QuestaoQuestionarioAnamnese, QuestaoAnamnese> entry = iterator.next();
				QuestaoAnamnese questao = entry.getValue();
				QuestaoQuestionarioAnamnese qqa = entry.getKey(); 
				if(!containsQARelationship(qqas,questao)){
					qqa.setQuestionarioAnamneseId(questionarioAnamneseId);
					qqa.setQuestaoAnamneseId(questao.getCodigo());
					daoQQA.inserir(qqa);
				}else{
					daoQQA.alterar(qqa);
				}
			}
		}
	}

	private boolean containsQARelationship(	List<QuestaoQuestionarioAnamnese> qqas, QuestaoAnamnese questao) {
		QuestaoQuestionarioAnamnese key = new QuestaoQuestionarioAnamnese(questao.getCodigo(), null);
		int index = Collections.binarySearch(qqas, key, new Comparator<QuestaoQuestionarioAnamnese>() {
			@Override
			public int compare(QuestaoQuestionarioAnamnese o1,QuestaoQuestionarioAnamnese o2) {
				return o1.getQuestaoAnamneseId().compareTo(o2.getQuestaoAnamneseId());
			}
		});
		return index >= 0;
	}

	private boolean containsQQARelationship(List<QuestaoAnamnese> questoes,	QuestaoQuestionarioAnamnese qqa) {
		QuestaoAnamnese key = new QuestaoAnamnese(qqa.getQuestaoAnamneseId());
		int index = Collections.binarySearch(questoes, key, new Comparator<QuestaoAnamnese>() {
			@Override
			public int compare(QuestaoAnamnese o1, QuestaoAnamnese o2) {
				return o1.getCodigo().compareTo(o2.getCodigo());
			}
		});
		return index >= 0;
	}
}
