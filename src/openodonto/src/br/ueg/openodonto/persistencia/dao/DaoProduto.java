package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.orm.Dao;

@Dao(classe=Produto.class)
public class DaoProduto extends DaoCrud<Produto>{

	private static final long serialVersionUID = 4731561150550714997L;
	
	public DaoProduto() {
		super(Produto.class);
	}	

	@Override
	public Produto getNewEntity() {
		return new Produto();
	}

	public void updateRelationshipColaborador(Colaborador colaborador) throws Exception{
		Long idColaborador = colaborador.getCodigo();
		List<Produto> produtos = colaborador.getProdutos();
		if(produtos != null){
			DaoColaboradorProduto daoCP = (DaoColaboradorProduto)DaoFactory.getInstance().getDao(ColaboradorProduto.class);
			List<ColaboradorProduto> cps =  daoCP.getCPRelationshipColaborador(idColaborador);
			for(ColaboradorProduto cp : cps){
				if(!containsCPRelationship(produtos,cp)){
					daoCP.remover(cp);
				}
			}
			for(Produto produto : produtos){
				if(!containsPRelationship(cps,produto)){
					daoCP.inserir(new ColaboradorProduto(idColaborador, produto.getCodigo()));
				}
			}
		}
	}
	
	private boolean containsPRelationship(List<ColaboradorProduto> cps,Produto produto){
		ColaboradorProduto key = new ColaboradorProduto(null,produto.getCodigo());
		int index = Collections.binarySearch(cps, key, new Comparator<ColaboradorProduto>() {
			@Override
			public int compare(ColaboradorProduto o1, ColaboradorProduto o2) {
				return o1.getProdutoIdProduto().compareTo(o2.getProdutoIdProduto());
			}
		});
		return index >= 0;
	}	
	
	private boolean containsCPRelationship(List<Produto> produtos,ColaboradorProduto cp){
		Produto key = new Produto(cp.getProdutoIdProduto());
		int index = Collections.binarySearch(produtos, key, new Comparator<Produto>() {
			@Override
			public int compare(Produto o1, Produto o2) {
				return o1.getCodigo().compareTo(o2.getCodigo());
			}
		});
		return index >= 0;
	}
	
	public List<Produto> getProdutosRelationshipColaborador(Long idColaborador)throws SQLException {
		DaoColaboradorProduto dao = (DaoColaboradorProduto)DaoFactory.getInstance().getDao(ColaboradorProduto.class);
		return dao.getProdutos(idColaborador);
	}	

	
}
