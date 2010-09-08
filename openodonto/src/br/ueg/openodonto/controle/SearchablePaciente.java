package br.ueg.openodonto.controle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.InputMask;
import br.ueg.openodonto.servico.busca.SearchFilter;
import br.ueg.openodonto.servico.busca.SearchFilterBase;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.validacao.Validator;
import br.ueg.openodonto.validacao.ValidatorFactory;

public class SearchablePaciente implements Searchable<Paciente>{

	private List<FieldFacade>          facade;
	private List<SearchFilter>         filters;
	private Map<String,InputMask>      masks;
	private String                     outMessage;
	
	public SearchablePaciente(String outMessage) {
		OrmTranslator translator = new OrmTranslator(OrmResolver.getAllFields(new ArrayList<Field>(), Paciente.class, true));
		facade = new ArrayList<FieldFacade>();
		facade.add(new FieldFacade("Código",translator.getColumn("codigo")));
		facade.add(new FieldFacade("Nome",translator.getColumn("nome")));
		facade.add(new FieldFacade("CPF",translator.getColumn("cpf")));
		facade.add(new FieldFacade("E-mail",translator.getColumn("email")));
		this.outMessage = outMessage;
		buildMask();
		buildFilter();
	}
	
	private void buildMask(){
		masks = new HashMap<String,InputMask>();
		masks.put("cpf",new JsMask("mask('999-999-999-99',{placeholder:' '})","maskCpf","maskCpf"));
	}
	
	private void buildFilter(){
		filters = new ArrayList<SearchFilter>();
		filters.add(buildNameFilter());
		filters.add(buildEmailFilter());
		filters.add(buildCpfFilter());
		filters.add(buildCodigoFilter());
	}
	
	private SearchFilterBase buildBasicFilter(String label,InputMask mask,Validator... validator){
		SearchFilterBase filter = new SearchFilterBase(null,label,outMessage);
		SearchFilterBase.Field field = filter.new Field();
		filter.setField(field);
		SearchFilterBase.Input<String> input = filter.new Input<String>();
		input.getValidators().addAll(Arrays.asList(validator));
		input.setMask(mask);
		field.getInputFields().add(input);
		return filter;
	}
	
	private SearchFilterBase buildBasicFilter(String label,Validator... validator){
		return buildBasicFilter(label,null,validator);
	}
	
	private SearchFilter buildNameFilter(){
		Validator validator = ValidatorFactory.newSrtLen(10, true);
		return buildBasicFilter("Nome:",validator);
	}
	
	private SearchFilter buildCpfFilter(){
		Validator validator = ValidatorFactory.newCpf();
		InputMask mask = masks.get("cpf");
		SearchFilterBase filter = buildBasicFilter("CPF:",mask,validator);
		return filter;
	}
	
	private SearchFilter buildEmailFilter(){
		Validator validator = ValidatorFactory.newEmail();
		SearchFilterBase filter = buildBasicFilter("E-mail:",validator);
		return filter;
	}
	
	private SearchFilter buildCodigoFilter(){
		Validator validator = ValidatorFactory.newNumSize(Integer.MAX_VALUE);
		return buildBasicFilter("Código:",validator);
	}
	
	@Override
	public List<FieldFacade> getFacade() {
		return facade;
	}

	@Override
	public List<SearchFilter> getFilters() {
		return filters;
	}

	public int getFilterlength(){
		return filters == null ? 0 : filters.size();
	}

	@Override
	public Collection<InputMask> getMasks() {
		return new ArrayList<InputMask>(masks.values());
	}	
}
