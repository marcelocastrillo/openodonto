package br.ueg.openodonto.controle.busca;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.InputMask;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.SearchFilter;
import br.ueg.openodonto.servico.busca.SearchFilterBase;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class SearchablePaciente implements Searchable<Paciente>{

	private static List<FieldFacade>   facade;
	private Map<String,SearchFilter>   filtersMap;
	private Map<String,InputMask>      masksMap;
	private MessageDisplayer           displayer;
	
	private List<SearchFilter>         filtersList;
	private List<InputMask>            masksList;
	
	static{
		buildFacade();
	}
	
	public SearchablePaciente(MessageDisplayer displayer) {
		this.displayer = displayer;
		buildMask();
		buildFilter();
		filtersList = new ArrayList<SearchFilter>(filtersMap.values());
		masksList = new ArrayList<InputMask>(masksMap.values());
	}
	
	private static void buildFacade(){
		OrmTranslator translator = new OrmTranslator(OrmResolver.getAllFields(new ArrayList<Field>(), Paciente.class, true));
		facade = new ArrayList<FieldFacade>();
		facade.add(new FieldFacade("Código",translator.getColumn("codigo")));
		facade.add(new FieldFacade("Nome",translator.getColumn("nome")));
		facade.add(new FieldFacade("CPF",translator.getColumn("cpf")));
		facade.add(new FieldFacade("E-mail",translator.getColumn("email")));
	}
	
	private void buildMask(){
		masksMap = new HashMap<String,InputMask>();
		masksMap.put("cpf",new JsMask("mask('999-999-999-99',{placeholder:' '})","maskCpf","maskCpf"));
	}
	
	private void buildFilter(){
		filtersMap = new HashMap<String,SearchFilter>();
		buildNameFilter();
		buildEmailFilter();
		buildCpfFilter();
		buildCodigoFilter();
	}
	
	private SearchFilterBase buildBasicFilter(String name,String label,InputMask mask,Validator... validator){
		SearchFilterBase filter = new SearchFilterBase(null,name,label,displayer);
		SearchFilterBase.Field field = filter.new Field();
		filter.setField(field);
		SearchFilterBase.Input<String> input = filter.new Input<String>();
		input.getValidators().addAll(Arrays.asList(validator));
		input.setMask(mask);
		field.getInputFields().add(input);
		return filter;
	}
	
	private SearchFilterBase buildBasicFilter(String name,String label,Validator... validator){
		return buildBasicFilter(name,label,null,validator);
	}
	
	private void buildNameFilter(){
		Validator validator = ValidatorFactory.newStrMaxLen(100, true);
		filtersMap.put("nomeFilter", buildBasicFilter("nomeFilter","Nome",validator));
	}
	
	private void buildCpfFilter(){
		Validator validator = ValidatorFactory.newCpf();
		InputMask mask = masksMap.get("cpf");
		filtersMap.put("cpfFilter",buildBasicFilter("cpfFilter","CPF",mask,validator));
	}
	
	private void buildEmailFilter(){
		Validator validator = ValidatorFactory.newEmail();
		filtersMap.put("emailFilter", buildBasicFilter("emailFilter","E-mail",validator));
	}
	
	private void buildCodigoFilter(){
		Validator validator = ValidatorFactory.newNumSize(Integer.MAX_VALUE);
		filtersMap.put("idFilter",buildBasicFilter("idFilter","Código",validator));
	}	

	@Override
	public List<FieldFacade> getFacade() {
		return facade;
	}

	@Override
	public List<SearchFilter> getFilters() {
		return filtersList;
	}

	@Override
	public List<InputMask> getMasks() {
		return masksList;
	}

	public Map<String, SearchFilter> getFiltersMap() {
		return filtersMap;
	}

	public void setFiltersMap(Map<String, SearchFilter> filtersMap) {
		this.filtersMap = filtersMap;
	}

	public Map<String, InputMask> getMasksMap() {
		return masksMap;
	}

	public void setMasksMap(Map<String, InputMask> masksMap) {
		this.masksMap = masksMap;
	}

	public List<SearchFilter> getFiltersList() {
		return filtersList;
	}

	public void setFiltersList(List<SearchFilter> filtersList) {
		this.filtersList = filtersList;
	}

	public List<InputMask> getMasksList() {
		return masksList;
	}

	public void setMasksList(List<InputMask> masksList) {
		this.masksList = masksList;
	}

}
