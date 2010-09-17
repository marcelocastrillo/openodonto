package br.ueg.openodonto.controle.busca;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.servico.ManageListagem;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.dominio.constante.CategoriaProduto;
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

public class SearchableProduto implements Searchable<Produto>{

	private static List<FieldFacade>   facade;
	private Map<String,SearchFilter>   filtersMap;
	private MessageDisplayer           displayer;
	
	private List<SearchFilter>         filtersList;
	
	static{
		buildFacade();
	}
	
	public SearchableProduto(MessageDisplayer displayer){
		this.displayer = displayer;
		buildFilter();
		filtersList = new ArrayList<SearchFilter>(filtersMap.values());
	}
	
	private static void buildFacade(){
		OrmTranslator translator = new OrmTranslator(OrmResolver.getAllFields(new ArrayList<Field>(), Produto.class, true));
		facade = new ArrayList<FieldFacade>();
		facade.add(new FieldFacade("Código",translator.getColumn("codigo")));
		facade.add(new FieldFacade("Nome",translator.getColumn("nome")));
		facade.add(new FieldFacade("Categoria","categoriaDesc"));
		facade.add(new FieldFacade("Descrição","shortDescription"));
	}
	
	private void buildFilter(){
		filtersMap = new HashMap<String,SearchFilter>();
		buildNameFilter();
		buildCategoriaFilter();
		buildDescricaoFilter();
	}

	private SearchFilterBase buildBasicFilter(String name,String label,Validator... validator){
		SearchFilterBase filter = new SearchFilterBase(null,name,label,displayer);
		SearchFilterBase.Field field = filter.new Field();
		filter.setField(field);
		SearchFilterBase.Input<String> input = filter.new Input<String>();
		input.getValidators().addAll(Arrays.asList(validator));
		field.getInputFields().add(input);
		return filter;
	}
	
	private void buildDescricaoFilter() {
		Validator validator = ValidatorFactory.newStrRangeLen(300,3, true);
		filtersMap.put("descricaoFilter", buildBasicFilter("descricaoFilter","Descrição",validator));
	}

	private void buildCategoriaFilter() {
		Validator validator = ValidatorFactory.newNull();
		SearchFilterBase filter = new SearchFilterBase(null,"categoriaFilter","Categoria",displayer);
		SearchFilterBase.Field field = filter.new Field();
		filter.setField(field);
		SearchFilterBase.Input<CategoriaProduto> input = filter.new Input<CategoriaProduto>();
		input.getValidators().add(validator);
		input.setDomain(ManageListagem.getLista(CategoriaProduto.class).getDominio());
		field.getInputFields().add(input);
		filtersMap.put("categoriaFilter", filter);
	}

	private void buildNameFilter() {
		Validator validator = ValidatorFactory.newStrRangeLen(150,3, true);
		filtersMap.put("nomeFilter", buildBasicFilter("nomeFilter","Nome",validator));
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
		return null;
	}

	public Map<String, SearchFilter> getFiltersMap() {
		return filtersMap;
	}

	public void setFiltersMap(Map<String, SearchFilter> filtersMap) {
		this.filtersMap = filtersMap;
	}

	public List<SearchFilter> getFiltersList() {
		return filtersList;
	}

	public void setFiltersList(List<SearchFilter> filtersList) {
		this.filtersList = filtersList;
	}

}
