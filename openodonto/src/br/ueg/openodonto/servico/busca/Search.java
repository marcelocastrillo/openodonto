package br.ueg.openodonto.servico.busca;

import java.util.List;

import br.ueg.openodonto.servico.busca.event.SearchListener;

public interface Search<T>{	
	
	String                         getTitle();
	List<ResultFacade>             getResults();
	Searchable<T>                  getSearchable();
	void                           addSearchListener(SearchListener listener);
	void                           search();
	void                           setSelected(ResultFacade bean);
	
}
