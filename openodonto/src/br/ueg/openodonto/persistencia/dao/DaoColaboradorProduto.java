package br.ueg.openodonto.persistencia.dao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
import br.ueg.openodonto.persistencia.orm.OrmResolver;
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
	
	
	/**
	 * Faz uma select na classe recebida em 'relacao' com join para classe atual do DAO .
	 * Para filtrar o join ( clausula 'ON') usa a declaracao de foregin key presente no field 'name'.
	 * @param <T> O Tipo de retorno
	 * @param relacao
	 * @param name
	 * @param whereParams
	 * @return
	 * @throws SQLException
	 */
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
	
	/**
	 * Inicialmente faz uma consulta filtrando pelo exemplo de <E>;
	 * Em seguida pega os resultados da consulta anterior e tenta relacionar cada resultado com <T>.
	 * Usa o nome do field 'fname' para recuperar a foregin key da classe atual com a classe <E>
	 * fornecendo assim a juncao inicial para a filtragem pelo exemplo de 'relacao'.
	 * @param <T>
	 * @param <E>
	 * @param example
	 * @param relacao
	 * @param fname
	 * @param rname
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity,E extends Entity>List<T> getRelacionamento(E example,T relacao,String fname,String rname) throws SQLException{
		List<T> relacoes = new ArrayList<T>();
		OrmTranslator translator = new OrmTranslator(fields);
		OrmTranslator exampleTranslator = new OrmTranslator(OrmResolver.getAllFields(new LinkedList<Field>(), example.getClass(), true));

		//Recupera o nome da coluna referenciada pela FK de 'fname' e traduz para o nome do field associado.
		ForwardKey[] fks = translator.getFieldByFieldName(fname).getAnnotation(Column.class).joinFields();
		String[] crossFkField = new String[fks.length];
		for(int i = 0 ; i < fks.length;i++){
			crossFkField[i] = exampleTranslator.getFieldName(fks[i].tableField());
		}
		
		OrmFormat format = new OrmFormat(example);
		OrmFormat formatRelacao = new OrmFormat(relacao);
		IQuery query = CrudQuery.getSelectQuery(example.getClass(), format.formatNotNull(), crossFkField);		
		EntityManager<E> dao = DaoFactory.getInstance().getDao((Class<E>)example.getClass());
		List<Map<String, Object>> results = dao.getSqlExecutor().executarUntypedQuery(query.getQuery(), query.getParams(), 1000);		

		for(Map<String, Object> result : results){
			Map<String, Object> whereParams = new HashMap<String, Object>();
			whereParams.put(translator.getColumn(fname), result.get(exampleTranslator.getColumn(crossFkField[0])));
			whereParams.putAll(formatRelacao.formatNotNull());
			relacoes.addAll(getRelacionamento((Class<T>)relacao.getClass(),rname,whereParams));
		}
		return relacoes;
	}
	
	public List<Colaborador> getColaboradores(Produto produto,Colaborador colaborador) throws SQLException{
		return getRelacionamento(produto,colaborador,"produtoIdProduto","colaboradorIdPessoa");
	}	
	
	public List<Produto> getProdutos(Colaborador colaborador,Produto produto) throws SQLException{
		return getRelacionamento(colaborador,produto,"colaboradorIdPessoa","produtoIdProduto");
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

