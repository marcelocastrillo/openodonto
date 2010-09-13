package br.ueg.openodonto.controle.busca;

import java.util.List;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.InputMask;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.SearchFilter;
import br.ueg.openodonto.servico.busca.Searchable;

public class SearchableColaborador implements Searchable<Colaborador> {

	public SearchableColaborador(MessageDisplayer displayer) {
	}

	@Override
	public List<FieldFacade> getFacade() {
		return null ;
	}

	@Override
	public List<SearchFilter> getFilters() {
		return null;
	}

	@Override
	public List<InputMask> getMasks() {
		return null;
	}

}
