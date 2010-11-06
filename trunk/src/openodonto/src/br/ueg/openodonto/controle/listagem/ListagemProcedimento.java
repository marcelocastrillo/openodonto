package br.ueg.openodonto.controle.listagem;

import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.dominio.Procedimento;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.servico.listagens.core.ListaDominio;

public class ListagemProcedimento extends ListaDominio<Procedimento> {

	public ListagemProcedimento() {
		super(Procedimento.class);
	}
	
	@Override
	public List<Procedimento> getRefreshDominio() {
		EntityManager<Procedimento> daoDominio = DaoFactory.getInstance().getDao(getClasse());
		List<Procedimento> lista = new ArrayList<Procedimento>();
		try {
			lista = daoDominio.listar(true, "codigo","nome","valor");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}	
	
	@Override
	public boolean isChangeable() {
		return false;
	}

}
