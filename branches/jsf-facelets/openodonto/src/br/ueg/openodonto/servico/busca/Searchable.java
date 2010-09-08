package br.ueg.openodonto.servico.busca;

import java.util.Collection;
import java.util.List;

public interface Searchable<T>{
	List<FieldFacade>              getFacade();
	List<SearchFilter>             getFilters();
	Collection<InputMask>          getMasks();
}
