package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import br.ueg.openodonto.servico.busca.ResultFacade;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.servico.busca.event.SearchEvent;
import br.ueg.openodonto.servico.busca.event.SearchListener;
import br.ueg.openodonto.servico.busca.event.SearchSelectedEvent;

public class SearchBase<T> implements Search<T> {

	private String                    title;
	private List<ResultFacade>        results;
	private Searchable<T>             searchable;
	private List<SearchListener>      listeners;

	
	public SearchBase(Searchable<T> searchable,String title) {
		this.title = title;
		this.results = new ArrayList<ResultFacade>();
		this.searchable = searchable;
		this.listeners = new ArrayList<SearchListener>();
	}
	
	@Override
	public List<ResultFacade> getResults() {
		return results;
	}

	@Override
	public Searchable<T> getSearchable() {
		return searchable;
	}

	public void setResults(List<ResultFacade> results) {
		this.results = results;
	}

	public void setSearchable(Searchable<T> searchable) {
		this.searchable = searchable;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public void addSearchListener(SearchListener listener) {
		if(listener == null){
			return;
		}
		listeners.add(listener);
	}

	private void fireSearchPerfomed(SearchEvent event){
		Iterator<SearchListener> iterator = listeners.iterator();
		while(iterator.hasNext()){
			SearchListener listener = iterator.next();
			listener.searchPerfomed(event);
		}
	}
	
	private void fireSelectPerfomed(SearchSelectedEvent event){
		Iterator<SearchListener> iterator = listeners.iterator();
		while(iterator.hasNext()){
			SearchListener listener = iterator.next();
			listener.resultRequested(event);
		}
	}
	
	@Override
	public void search() {
		fireSearchPerfomed(new SearchEvent() {
			@Override
			public Object getSource() {
				return SearchBase.this;
			}
		});
	}

	@Override
	public void setSelected(final ResultFacade bean) {
		fireSelectPerfomed(new SearchSelectedEvent() {
			@Override
			public Object getSource() {
				return SearchBase.this;
			}			
			@Override
			public ResultFacade getSelected() {
				return bean;
			}
		});
	}	

}
