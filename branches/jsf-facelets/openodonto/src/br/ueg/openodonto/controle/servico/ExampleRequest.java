package br.ueg.openodonto.controle.servico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.servico.busca.Searchable;

public class ExampleRequest<T> {

	private Searchable<T>          searchable;
	private Map<String,String>     filterNameBeanPathMap;
	private List<Class<?>>         invalidPermiteds;
	
	public ExampleRequest(Searchable<T> searchable) {
		this.searchable = searchable;
		this.filterNameBeanPathMap = new HashMap<String, String>();
		this.invalidPermiteds = new ArrayList<Class<?>>();
	}
	
	public Searchable<T> getSearchable() {
		return searchable;
	}
	public void setSearchable(Searchable<T> searchable) {
		this.searchable = searchable;
	}
	public Map<String, String> getFilterNameBeanPathMap() {
		return filterNameBeanPathMap;
	}
	public void setFilterNameBeanPathMap(Map<String, String> filterNameBeanPathMap) {
		this.filterNameBeanPathMap = filterNameBeanPathMap;
	}
	public List<Class<?>> getInvalidPermiteds() {
		return invalidPermiteds;
	}
	public void setInvalidPermiteds(List<Class<?>> invalidPermiteds) {
		this.invalidPermiteds = invalidPermiteds;
	}	
	
}
