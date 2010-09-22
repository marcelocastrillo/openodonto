package br.ueg.openodonto.controle.busca;

import java.lang.reflect.Field;
import java.util.ArrayList;

import br.ueg.openodonto.controle.servico.ExampleRequest;
import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.persistencia.dao.sql.SqlWhereOperatorType;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.validator.EmptyValidator;
import br.ueg.openodonto.validator.NullValidator;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class SearchableDentista extends AbstractSearchable<Dentista> {

	private static final long serialVersionUID = -5557269675090544299L;

	public SearchableDentista(MessageDisplayer displayer) {
		super(displayer,Dentista.class);
	}

	public void buildFacade(){
		super.buildFacade();
		OrmTranslator translator = new OrmTranslator(OrmResolver.getAllFields(new ArrayList<Field>(), Dentista.class, true));
		getFacade().add(new FieldFacade("Código",translator.getColumn("codigo")));
		getFacade().add(new FieldFacade("Nome",translator.getColumn("nome")));
		getFacade().add(new FieldFacade("CRO",translator.getColumn("cro")));
		getFacade().add(new FieldFacade("Especialidade",translator.getColumn("especialidade")));
	}
	
	public void buildFilter(){
		super.buildFilter();
		buildNameFilter();
		buildCroFilter();
		buildEspecialidadeFilter();
		buildCodigoFilter();
	}
	
	private void buildNameFilter(){
		Validator validator = ValidatorFactory.newStrRangeLen(100,3, true);
		getFiltersMap().put("nomeFilter", buildBasicFilter("nomeFilter","Nome",validator));
	}
	
	private void buildCroFilter(){
		Validator validator = ValidatorFactory.newNumSize(Integer.MAX_VALUE);
		getFiltersMap().put("croFilter",buildBasicFilter("croFilter","CRO",validator));
	}
	
	private void buildEspecialidadeFilter(){
		Validator validator = ValidatorFactory.newStrRangeLen(150,3, true);
		getFiltersMap().put("especialidadeFilter", buildBasicFilter("especialidadeFilter","Especialidade",validator));
	}
	
	private void buildCodigoFilter(){
		Validator validator = ValidatorFactory.newNumSize(Integer.MAX_VALUE);
		getFiltersMap().put("idFilter",buildBasicFilter("idFilter","Código",validator));
	}

	@Override
	public Dentista buildExample() {
		ExampleRequest<Dentista> request  = new ExampleRequest<Dentista>(this);		
		request.getFilterRelation().add(request.new TypedFilter("nomeFilter", "nome",SqlWhereOperatorType.LIKE));
		request.getFilterRelation().add(request.new TypedFilter("idFilter","codigo",SqlWhereOperatorType.EQUAL));
		request.getFilterRelation().add(request.new TypedFilter("croFilter", "cro",SqlWhereOperatorType.EQUAL));
		request.getFilterRelation().add(request.new TypedFilter("especialidadeFilter", "especialidade",SqlWhereOperatorType.LIKE));
		request.getInvalidPermiteds().add(NullValidator.class);
		request.getInvalidPermiteds().add(EmptyValidator.class);
		Dentista target = getManageExample().processExampleRequest(request);
		return target;
	}		
	
}
