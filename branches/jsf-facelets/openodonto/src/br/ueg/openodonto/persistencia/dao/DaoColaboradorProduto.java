package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.Query;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;

@Dao(classe=ColaboradorProduto.class)
public class DaoColaboradorProduto  extends DaoCrud<ColaboradorProduto> {

	private static final long serialVersionUID = 4322391116355410621L;
	
	public DaoColaboradorProduto() {
		super(ColaboradorProduto.class);
	}

	@Override
	public ColaboradorProduto getNewEntity() {
		return new ColaboradorProduto();
	}	
	
	public String getWhere(String to){
		if(to.equals("colaboradorIdPessoa")){
			return " WHERE produto_id_produto = ?";
		}else if(to.equals("produtoIdProduto")){
			return  "WHERE colaborador_id_pessoa = ?";
		}
		return null;
	}
	
	public <T extends Entity> List<T> getRelacionamento(Long id,Class<T> relacao,String name) throws SQLException{
		String sql = CrudQuery.getSelectRoot(relacao, "*");
		OrmTranslator translator = new OrmTranslator(fields);
		sql += CrudQuery.buildJoin(translator,name,relacao);
		sql += getWhere(name);
		EntityManager<T> dao = DaoFactory.getInstance().getDao(relacao);
		IQuery query = new Query(sql, CrudQuery.getTableName(relacao),id);
		return dao.getSqlExecutor().executarQuery(query);		
	}
	
	public List<Colaborador> getColaboradores(Long idProduto) throws SQLException{
		return getRelacionamento(idProduto,Colaborador.class,"colaboradorIdPessoa");
	}	
	
	public List<Produto> getProdutos(Long idColaborador) throws SQLException{
		return getRelacionamento(idColaborador,Produto.class,"produtoIdProduto");
	}
	
	@Override
	public ColaboradorProduto pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<ColaboradorProduto> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			ColaboradorProduto find = new ColaboradorProduto();
			find.setCodigo(id);
			OrmFormat orm = new OrmFormat(find);
			IQuery query = CrudQuery.getSelectQuery(ColaboradorProduto.class, orm.formatNotNull(), "*");
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

