package br.ueg.openodonto.controle.busca;

import br.ueg.openodonto.servico.busca.event.AbstractSearchListener;
import br.ueg.openodonto.servico.busca.event.SearchAssociateEvent;

public class CommonSearchAssociateHandler extends AbstractSearchListener{

	@Override
	public void associatePerformed(SearchAssociateEvent event) {
		System.out.println(event.getSelecteds());
	}
	
}
