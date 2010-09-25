package br.ueg.openodonto.controle.busca;

import java.util.List;

import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.servico.busca.SelectableResult;
import br.ueg.openodonto.servico.busca.SelectableSearch;

public class SelectableSearchBase<T> extends SearchBase<T> implements SelectableSearch<T> {

	private static final long serialVersionUID = -1781119263710365722L;

	public SelectableSearchBase(Searchable<T> searchable, String title) {
		super(searchable,title,"painelBusca",null);
	}

	@Override
	public void addSelecteds(List<SelectableResult> selecteds) {
		
	}
	
	@Override
	public List<SelectableResult> getSelectableResults() {
		return null;
	}

}
