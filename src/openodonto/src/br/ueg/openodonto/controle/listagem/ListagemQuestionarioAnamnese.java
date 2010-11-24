package br.ueg.openodonto.controle.listagem;

import java.util.ArrayList;
import java.util.List;

import br.ueg.openodonto.dominio.QuestionarioAnamnese;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.servico.listagens.core.ListaDominio;

public class ListagemQuestionarioAnamnese extends ListaDominio<QuestionarioAnamnese>{

	public ListagemQuestionarioAnamnese() {
		super(QuestionarioAnamnese.class);
	}
	
	@Override
	public List<QuestionarioAnamnese> getRefreshDominio() {
		EntityManager<QuestionarioAnamnese> daoDominio = DaoFactory.getInstance().getDao(getClasse());
		List<QuestionarioAnamnese> lista = new ArrayList<QuestionarioAnamnese>();		
		try {
			lista = daoDominio.listar(true, "codigo","nome");
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
