package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Produto.class)
public class DaoProduto extends DaoCrud<Produto>{

	public DaoProduto() {
		super(Produto.class);
	}

	private static final long serialVersionUID = 4731561150550714997L;

	@Override
	public Produto getNewEntity() {
		return new Produto();
	}

	public void updateRelationshipProduto(Colaborador colaborador) throws Exception{
		Long idColaborador = colaborador.getCodigo();
		List<Produto> produtos = colaborador.getProdutos();
		if(produtos != null){
			DaoColaboradorProduto daoCP = (DaoColaboradorProduto)DaoFactory.getInstance().getDao(ColaboradorProduto.class);
			List<ColaboradorProduto> cps =  getCPRelationship(new ColaboradorProduto(idColaborador, null));
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
		int index = Collections.binarySearch(cps, key, new CCompatator());
		return index >= 0;
	}
	
	private class CCompatator implements Comparator<ColaboradorProduto>{
		@Override
		public int compare(ColaboradorProduto o1, ColaboradorProduto o2) {
			return o1.getProdutoIdProduto().compareTo(o2.getProdutoIdProduto());
		}		
	}
	
	private boolean containsCPRelationship(List<Produto> produtos,ColaboradorProduto cp){
		Produto key = new Produto(cp.getProdutoIdProduto());
		int index = Collections.binarySearch(produtos, key, new CPCompatator());
		return index >= 0;
	}
	
	private class CPCompatator implements Comparator<Produto>{
		@Override
		public int compare(Produto o1, Produto o2) {
			return o1.getCodigo().compareTo(o2.getCodigo());
		}
		
	}
	
	private List<ColaboradorProduto> getCPRelationship(ColaboradorProduto example) throws SQLException{
		OrmFormat format = new OrmFormat(example);
		IQuery sql = CrudQuery.getSelectQuery(ColaboradorProduto.class, format.formatNotNull(), "*");
		return DaoFactory.getInstance().getDao(ColaboradorProduto.class).getSqlExecutor().executarQuery(sql);
	}
	
	public List<Produto> getProdutosRelationshipColaborador(Long idColaborador)throws SQLException {
		DaoColaboradorProduto dao = (DaoColaboradorProduto)DaoFactory.getInstance().getDao(ColaboradorProduto.class);
		return dao.getProdutos(idColaborador);
	}
	
	@Override
	public Produto pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<Produto> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			OrmFormat orm = new OrmFormat(new Produto(id));
			IQuery query = CrudQuery.getSelectQuery(Produto.class, orm.formatNotNull(), "*");
			lista = getSqlExecutor().executarQuery(query.getQuery(),query.getParams(), 1);
			if (lista.size() == 1) {
				return lista.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
