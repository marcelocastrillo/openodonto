package br.ueg.openodonto.servico.busca.event;

import java.util.EventListener;

public interface SearchListener extends EventListener{
	
	void searchPerfomed(SearchEvent event);
	void resultRequested(SearchEvent event);

}
