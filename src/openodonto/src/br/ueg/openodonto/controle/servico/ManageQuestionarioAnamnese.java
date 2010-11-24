package br.ueg.openodonto.controle.servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.ueg.openodonto.dominio.PacienteQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestionarioAnamnese;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.persistencia.dao.DaoQuestionarioAnamnese;
import br.ueg.openodonto.util.bean.QuestionarioAnamneseAdapter;
import br.ueg.openodonto.visao.ApplicationView;

public class ManageQuestionarioAnamnese {

	private Map<PacienteQuestionarioAnamnese,QuestionarioAnamnese>		questionariosAnamnese;
	private List<QuestionarioAnamneseAdapter> 							questionariosAdapter;
	private QuestionarioAnamneseAdapter									anamnese;
	private Long                           								add;
	private ApplicationView 											view;
	
	public ManageQuestionarioAnamnese(Map<PacienteQuestionarioAnamnese,QuestionarioAnamnese> anamneses, ApplicationView view) {
		this.questionariosAnamnese = anamneses;
		this.questionariosAdapter = new ArrayList<QuestionarioAnamneseAdapter>();
		this.view = view;
		makeAdpter();		
	}
	
	private void loadFirstAnamnese(){
		if(questionariosAnamnese != null && questionariosAnamnese.size() != 0){
			anamnese = questionariosAdapter.get(0);
		}
	}	
	
	public void makeAdpter(){
		if(questionariosAnamnese != null){
			questionariosAdapter.clear();
			for(Entry<PacienteQuestionarioAnamnese,QuestionarioAnamnese> entry : questionariosAnamnese.entrySet()){
				addAdapter(entry.getKey(),entry.getValue());
			}
			loadFirstAnamnese();
		}
	}
	
	private QuestionarioAnamneseAdapter addAdapter(PacienteQuestionarioAnamnese pqa,QuestionarioAnamnese qa){
	    QuestionarioAnamneseAdapter adapter;
		questionariosAdapter.add(adapter = new QuestionarioAnamneseAdapter(pqa,qa,qa.getQuestoes()));
		return adapter;
	}
	
	public void acaoAddAnamnese(Long codigo){
		PacienteQuestionarioAnamnese pqa = new PacienteQuestionarioAnamnese(null,codigo);
		DaoQuestionarioAnamnese daoQA = (DaoQuestionarioAnamnese) DaoFactory.getInstance().getDao(QuestionarioAnamnese.class);
		QuestionarioAnamnese novo = new QuestionarioAnamnese(codigo);
		try {
            daoQA.load(novo);
        } catch (Exception e) {
            e.printStackTrace();
        }
		this.questionariosAnamnese.put(pqa , novo);
		this.anamnese = addAdapter(pqa,novo);
	}
	
	public ApplicationView getView() {
		return view;
	}

	public Map<PacienteQuestionarioAnamnese, QuestionarioAnamnese> getQuestionariosAnamnese() {
		return questionariosAnamnese;
	}

	public void setQuestionariosAnamnese(Map<PacienteQuestionarioAnamnese, QuestionarioAnamnese> questionariosAnamnese) {
		this.questionariosAnamnese = questionariosAnamnese;
		makeAdpter();
	}

	public List<QuestionarioAnamneseAdapter> getQuestionariosAdapter() {
		return questionariosAdapter;
	}
	
	public Long getAdd() {
        return add;
    }

    public void setAdd(Long add) {
        this.add = add;
    }

    public QuestionarioAnamneseAdapter getAnamnese() {
		return anamnese;
	}
	
	public void setAnamnese(QuestionarioAnamneseAdapter anamnese) {
		this.anamnese = anamnese;
	}

}
