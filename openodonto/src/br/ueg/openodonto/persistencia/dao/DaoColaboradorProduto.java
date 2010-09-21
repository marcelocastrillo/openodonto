package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.Query;
import br.ueg.openodonto.persistencia.orm.Column;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.ForwardKey;
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
	
	public <T extends Entity> List<T> getRelacionamento(Class<T> relacao,String name,Map<String,Object> whereParams) throws SQLException{
		StringBuilder sql = new StringBuilder(CrudQuery.getSelectRoot(relacao, "*"));
		OrmTranslator translator = new OrmTranslator(fields);
		sql.append(CrudQuery.buildJoin(translator,name,relacao));
		List<Object> params = new ArrayList<Object>();
		CrudQuery.makeWhereOfQuery(whereParams, params, sql);
		EntityManager<T> dao = DaoFactory.getInstance().getDao(relacao);
		IQuery query = new Query(sql.toString(),params,CrudQuery.getTableName(relacao));
		return dao.getSqlExecutor().executarQuery(query);		
	}	
	
	@SuppressWarnings("unchecked")
	public <T extends Entity,E extends Entity>List<T> getRelacionamento(E example,Class<T> relacao,String fname,String rname) throws SQLException{
		List<T> colaboradores = new ArrayList<T>();
		OrmTranslator translator = new OrmTranslator(fields);
		EntityManager<E> dao = DaoFactory.getInstance().getDao((Class<E>)example.getClass());
		OrmFormat format = new OrmFormat(example);
		ForwardKey[] fks = translator.getFieldByFieldName(fname).getAnnotation(Column.class).joinFields();
		String[] strFks = new String[fks.length];
		for(int i = 0 ; i < fks.length;i++){
			strFks[i] = fks[i].tableField();
		}
		IQuery query = CrudQuery.getSelectQuery(example.getClass(), format.formatNotNull(), strFks);
		List<Map<String, Object>> results = dao.getSqlExecutor().executarUntypedQuery(query.getQuery(), query.getParams(), 1000);
		for(Map<String, Object> result : results){
			Map<String, Object> whereParams = new HashMap<String, Object>();
			whereParams.put(translator.getColumn(fname), result.get(strFks[0]));
			colaboradores.addAll(getRelacionamento(relacao,rname,whereParams));
		}
		return colaboradores;
	}
	
	public List<Colaborador> getColaboradores(Produto produto) throws SQLException{
		return getRelacionamento(produto,Colaborador.class,"produtoIdProduto","colaboradorIdPessoa");
	}	
	
	public List<Produto> getProdutos(Colaborador colaborador) throws SQLException{
		return getRelacionamento(colaborador,Produto.class,"colaboradorIdPessoa","produtoIdProduto");
	}
	
	public List<Colaborador> getColaboradores(Long idProduto) throws SQLException{
		Map<String,Object> whereParams = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		whereParams.put(translator.getColumn("produtoIdProduto"), idProduto);
		return getRelacionamento(Colaborador.class,"colaboradorIdPessoa",whereParams);
	}	
	
	public List<Produto> getProdutos(Long idColaborador) throws SQLException{
		Map<String,Object> whereParams = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		whereParams.put(translator.getColumn("colaboradorIdPessoa"), idColaborador);
		return getRelacionamento(Produto.class,"produtoIdProduto",whereParams);
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

