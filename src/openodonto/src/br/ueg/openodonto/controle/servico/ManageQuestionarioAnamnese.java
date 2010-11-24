package br.ueg.openodonto.controle.servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.ueg.openodonto.dominio.PacienteQuestionarioAnamnese;
import br.ueg.openodonto.dominio.QuestionarioAnamnese;
import br.ueg.openodonto.util.bean.QuestionarioAnamneseAdapter;
import br.ueg.openodonto.visao.ApplicationView;

public class ManageQuestionarioAnamnese {

	private Map<PacienteQuestionarioAnamnese,QuestionarioAnamnese>		questionariosAnamnese;
	private List<QuestionarioAnamneseAdapter> 							questionariosAdapter;
	private QuestionarioAnamneseAdapter									anamnese;
	private QuestionarioAnamnese        								add;
	private ApplicationView 											view;
	
	public ManageQuestionarioAnamnese(Map<PacienteQuestionarioAnamnese,QuestionarioAnamnese> anamneses, ApplicationView view) {
		this.questionariosAnamnese = anamneses;
		this.questionariosAdapter = new ArrayList<QuestionarioAnamneseAdapter>();
		this.view = view;
		makeAdpter();
		loadFirstAnamnese();
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
		}
	}
	
	private void addAdapter(PacienteQuestionarioAnamnese pqa,QuestionarioAnamnese qa){
		questionariosAdapter.add(new QuestionarioAnamneseAdapter(pqa,qa,qa.getQuestoes()));		
	}
	
	public void acaoAddAnamnese(QuestionarioAnamnese questionario){
		PacienteQuestionarioAnamnese pqa = new PacienteQuestionarioAnamnese(null,questionario.getCodigo());
		questionariosAnamnese.put(pqa , questionario);
		addAdapter(pqa,questionario);
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

	public QuestionarioAnamnese getAdd() {
		return add;
	}

	public void setAdd(QuestionarioAnamnese add) {
		this.add = add;
	}
	
	public QuestionarioAnamneseAdapter getAnamnese() {
		return anamnese;
	}
	
	public void setAnamnese(QuestionarioAnamneseAdapter anamnese) {
		this.anamnese = anamnese;
	}

}
