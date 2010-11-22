package br.ueg.openodonto.visao.converter;

import java.util.List;

import javax.el.ELContext;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import br.ueg.openodonto.controle.ManterQuestionarioAnamnese;
import br.ueg.openodonto.util.bean.QuestionarioQuestaoAdapter;

public class QuestaoQuestionarioConverter implements Converter{

	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		Long id = Long.parseLong(value);		
		ManterQuestionarioAnamnese mBean = getMbean(context);		
		List<QuestionarioQuestaoAdapter> questoes =  mBean.getManageQuestao().getQuestoesAdapter();
		QuestionarioQuestaoAdapter questao = questoes.get(id.intValue());
		return questao;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		QuestionarioQuestaoAdapter adapter = (QuestionarioQuestaoAdapter) value;
		return adapter.getQqa().getIndex().toString();
	}
	
	private ManterQuestionarioAnamnese getMbean(FacesContext context){
		ELContext elctx = context.getELContext();
		ManterQuestionarioAnamnese mBean = (ManterQuestionarioAnamnese)elctx.getELResolver().getValue(elctx, null, "manterQuestionarioAnamnese");
		return mBean;
	}

}
