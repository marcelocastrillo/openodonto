package br.ueg.openodonto.controle.fuck;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.ResultFacadeBean;
import br.ueg.openodonto.controle.SearchBase;
import br.ueg.openodonto.servico.busca.ResultFacade;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.event.AbstractSearchListener;
import br.ueg.openodonto.servico.busca.event.SearchEvent;
import br.ueg.openodonto.servico.busca.event.SearchSelectedEvent;

public class ManterBean {

	private SearchBase<Bean>     search;  
	
	public ManterBean() {		
		search = new SearchBase<Bean>(new SearchableBean(""),"Busca Bean");		
		search.addSearchListener(new SearchHandler());
		search.addSearchListener(new SearchSelectedHandler());
		System.out.println("===== CONTRUTOR =====");
	}

	public SearchBase<Bean> getSearch() {
		System.out.println("===== GET "+search+" =====");
		return search;
	}
	
	public void setSearch(SearchBase<Bean> search) {
		System.out.println("===== SET "+search+" =====");
		this.search = search;
	}

	protected class SearchHandler extends AbstractSearchListener{
		@SuppressWarnings("unchecked")
		public void searchPerfomed(SearchEvent event) {
			Search<Bean> search = (Search<Bean>)event.getSource(); 
			Map<String , Object> c1 = new HashMap<String, Object>();
			c1.put("nome", "Vincius G G R");c1.put("cpf", "02549287142");c1.put("email", "viiniiciius@gmail.com");
			
			Map<String , Object> c2 = new HashMap<String, Object>();
			c2.put("nome", "Amanda B S");c2.put("cpf", "11111111111");c2.put("email", "amanda.bs@hotmail.com");
			
			Map<String , Object> c3 = new HashMap<String, Object>();
			c3.put("nome", "Meire L G R");c3.put("cpf", "22222222222");c3.put("email", "nao@tem.com");
			
			Map<String , Object> c4 = new HashMap<String, Object>();
			c4.put("nome", "Derlan R V");c4.put("cpf", "33333333333");c4.put("email", "nao@tem.com");
			
			List<ResultFacade> resultFacade = new ArrayList<ResultFacade>();
			resultFacade.add(new ResultFacadeBean(c1));
			resultFacade.add(new ResultFacadeBean(c2));
			resultFacade.add(new ResultFacadeBean(c3));
			resultFacade.add(new ResultFacadeBean(c4));
			
			search.getResults().clear();			
			search.getResults().addAll(resultFacade);			
			
		}		
	}
	
	protected class SearchSelectedHandler extends AbstractSearchListener{
		@Override
		public void resultRequested(SearchSelectedEvent event) {
			System.out.println(event.getSelected());
		}
	}
	
}
