package br.ueg.openodonto.controle.busca;

import java.lang.reflect.Field;
import java.util.ArrayList;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class SearchableColaborador extends AbstractSearchable<Colaborador> {

	private static final long serialVersionUID = -2595474489456158101L;

	public SearchableColaborador(MessageDisplayer displayer) {
		super(displayer,Colaborador.class);
	}

	public void buildFacade(){
		super.buildFacade();
		OrmTranslator translator = new OrmTranslator(OrmResolver.getAllFields(new ArrayList<Field>(), Paciente.class, true));
		getFacade().add(new FieldFacade("Código",translator.getColumn("codigo")));
		getFacade().add(new FieldFacade("Nome",translator.getColumn("nome")));
		getFacade().add(new FieldFacade("Categoria",translator.getColumn("categoria")));
		getFacade().add(new FieldFacade("Descrição",translator.getColumn("descricao")));
	}
	
	public void buildFilter(){
		super.buildFilter();
		buildNameFilter();
		buildCategoriaFilter();
		buildDescricaoFilter();
	}

	private void buildDescricaoFilter() {
		Validator validator = ValidatorFactory.newStrRangeLen(300,5, true);
		getFiltersMap().put("nomeFilter", buildBasicFilter("descricaoFilter","Descrição",validator));
	}

	private void buildCategoriaFilter() {
	
	}

	private void buildNameFilter() {
		Validator validator = ValidatorFactory.newStrRangeLen(150,3, true);
		getFiltersMap().put("nomeFilter", buildBasicFilter("nomeFilter","Nome",validator));
	}

	@Override
	public Colaborador buildExample() {
		return null;
	}

}
