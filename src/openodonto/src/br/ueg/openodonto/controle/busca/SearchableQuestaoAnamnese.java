package br.ueg.openodonto.controle.busca;

import java.lang.reflect.Field;
import java.util.ArrayList;

import br.ueg.openodonto.controle.servico.ExampleRequest;
import br.ueg.openodonto.dominio.QuestaoAnamnese;
import br.ueg.openodonto.persistencia.dao.sql.SqlWhereOperatorType;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.validator.EmptyValidator;
import br.ueg.openodonto.validator.NullValidator;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class SearchableQuestaoAnamnese extends AbstractSearchable<QuestaoAnamnese>{

	private static final long serialVersionUID = 3186361107120833775L;

	public SearchableQuestaoAnamnese(MessageDisplayer displayer) {
		super(QuestaoAnamnese.class, displayer);
	}
	
	@Override
	protected void buildFacade() {
		super.buildFacade();
		OrmTranslator translator = new OrmTranslator(OrmResolver.getAllFields(new ArrayList<Field>(), QuestaoAnamnese.class, true));
		getFacade().add(new FieldFacade("Código",translator.getColumn("codigo")));
		getFacade().add(new FieldFacade("Questão","shortQuestion"));
	}
	
	@Override
	protected void buildFilter() {
		super.buildFilter();
		buildCodigoFilter();
		buildPerguntaFilter();
	}	

	private void buildPerguntaFilter() {
		Validator validator = ValidatorFactory.newStrRangeLen(300,3, true);
		getFiltersMap().put("questionFilter", buildBasicFilter("questionFilter","Questão",validator));
	}

	private void buildCodigoFilter() {
		Validator validator = ValidatorFactory.newNumMax(Integer.MAX_VALUE);
		getFiltersMap().put("idFilter",buildBasicFilter("idFilter","Código",validator));
	}

	@Override
	public QuestaoAnamnese buildExample() {
		ExampleRequest<QuestaoAnamnese> request  = new ExampleRequest<QuestaoAnamnese>(this);		
		request.getFilterRelation().add(request.new TypedFilter("questionFilter", "pergunta",SqlWhereOperatorType.LIKE));
		request.getFilterRelation().add(request.new TypedFilter("idFilter","codigo",SqlWhereOperatorType.EQUAL));
		request.getInvalidPermiteds().add(NullValidator.class);
		request.getInvalidPermiteds().add(EmptyValidator.class);
		QuestaoAnamnese target = getManageExample().processExampleRequest(request);
		return target;
	}

}
