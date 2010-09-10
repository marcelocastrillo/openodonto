package br.ueg.openodonto.servico.busca.event;

public abstract class AbstractSearchListener implements SearchListener{

	public void resultRequested(SearchSelectedEvent event){		
	}
	
	@Override
	public void resultRequested(SearchEvent event) {
		resultRequested((SearchSelectedEvent)event);
	}

	@Override
	public void searchPerformed(SearchEvent event) {
	}
	
	@Override
	public void cleanPerformed(SearchEvent event) {	
	}

}
