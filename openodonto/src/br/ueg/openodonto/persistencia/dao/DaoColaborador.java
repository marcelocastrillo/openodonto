package br.ueg.openodonto.persistencia.dao;

import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.orm.Dao;

@Dao(classe=Colaborador.class)
public class DaoColaborador extends DaoAbstractPessoa<Colaborador> {

	private static final long serialVersionUID = 5204016786567134806L;
	
	public DaoColaborador() {
		super(Colaborador.class);
	}

	@Override
	public Colaborador getNewEntity() {
		return new Colaborador();
	}

	@Override
	protected void afterUpdate(Colaborador o) throws Exception {
		super.afterUpdate(o);
		updateRelationship(o);
	}

	@Override
	protected void afterInsert(Colaborador o) throws Exception {
		super.afterInsert(o);
		updateRelationship(o);
	}
	
	private void updateRelationship(Colaborador o) throws Exception{
		DaoProduto daoProduto = (DaoProduto)DaoFactory.getInstance().getDao(Produto.class);
		daoProduto.updateRelationshipProduto(o);		
	}	
	
	@Override
	public void afterLoad(Colaborador o) throws Exception {
		super.afterLoad(o);
		DaoProduto daoProduto = (DaoProduto)DaoFactory.getInstance().getDao(Produto.class);
		List<Produto> produtos = daoProduto.getProdutosRelationshipColaborador(o.getCodigo());
		o.setProdutos(produtos);
	}
	
	@Override
	protected boolean beforeRemove(Colaborador o, Map<String, Object> params)throws Exception {
		boolean tolerance = super.beforeRemove(o, params);
		DaoColaboradorProduto daoCP = (DaoColaboradorProduto) DaoFactory.getInstance().getDao(ColaboradorProduto.class);
		for(Produto produto : o.getProdutos()){
			ColaboradorProduto cp = new ColaboradorProduto(o.getCodigo(),produto.getCodigo());
			daoCP.remover(cp);
		}
		return tolerance;
	}
	
}
