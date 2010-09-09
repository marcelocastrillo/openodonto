package br.ueg.openodonto.controle.fuck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.InputField;
import br.ueg.openodonto.servico.busca.InputMask;
import br.ueg.openodonto.servico.busca.SearchFilter;
import br.ueg.openodonto.servico.busca.SearchFilterBase;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.validacao.EmptyValidator;
import br.ueg.openodonto.validacao.NullValidator;
import br.ueg.openodonto.validacao.SizeValidator;

public class SearchableBean implements Searchable<Bean>{

	private List<FieldFacade>          facade;
	private List<SearchFilter>         filters;
	private String                     outMessage;
	
	public SearchableBean(String outMessage) {
		facade = new ArrayList<FieldFacade>();
		facade.add(new FieldFacade("Nome","nome"));
		facade.add(new FieldFacade("CPF","cpf"));
		facade.add(new FieldFacade("E-mail","email"));
		this.outMessage = outMessage;
		buildFilter();
	}
	
	private void buildFilter(){
		filters = new ArrayList<SearchFilter>();
		filters.add(buildNameFilter());
		//filters.add(buildEmailFilter());
		//filters.add(buildCpfFilter());
	}
	
	private SearchFilter buildNameFilter(){
		SearchFilterBase filter = new SearchFilterBase();
		filter.setLabel("Nome:");
		filter.setOutMessage(outMessage);
		SearchFilterBase.Field nameField = filter.new Field();
		filter.setField(nameField);
		List<InputField<?>> inputs = new ArrayList<InputField<?>>();
		nameField.setInputFields(inputs);
		SearchFilterBase.Input<String> nameInput = filter.new Input<String>();
		nameInput.getValidators().add(new NullValidator(new EmptyValidator(new SizeValidator("", 10), true)));
		inputs.add(nameInput);
		return filter;
	}
	
	private SearchFilter buildCpfFilter(){
		return null;
	}
	
	private SearchFilter buildEmailFilter(){
		return null;
	}
	
	@Override
	public List<FieldFacade> getFacade() {
		return facade;
	}

	@Override
	public List<SearchFilter> getFilters() {
		return filters;
	}

	public int getFilerlength(){
		return filters == null ? 0 : filters.size();
	}

	@Override
	public Collection<InputMask> getMasks() {
		return null;
	}
	
	
	
}
