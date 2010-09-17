package br.ueg.openodonto.persistencia.dao;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
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
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;

@Dao(classe=ColaboradorProduto.class)
public class DaoColaboradorProduto  extends DaoCrud<ColaboradorProduto> {

	private static final long serialVersionUID = 4322391116355410621L;	
	private static String JOIN_FROM_PESSOA;
	private static String JOIN_FROM_PRODUTO;
	
	static {
		resolveRelationship();
		initQueryMap();
	}

	private static void resolveRelationship(){
		List<Field> fields = OrmResolver.getAllFields(new LinkedList<Field>(), ColaboradorProduto.class, false);
		OrmTranslator translator = new OrmTranslator(fields);
		JOIN_FROM_PESSOA = " "+buildJoin(translator,"colaboradorIdPessoa",Colaborador.class);
		JOIN_FROM_PRODUTO = " "+buildJoin(translator,"colaboradorIdPessoa",Produto.class);
		System.out.println(JOIN_FROM_PESSOA);
		System.out.println(JOIN_FROM_PRODUTO);
	}
	
	private static String buildJoin(OrmTranslator translator,String name,Class<?> relation){
		Field field = translator.getFieldByFieldName(name);
		return CrudQuery.getRelationshipJoin(relation,field);
	}
	
	public static void initQueryMap() {
		DaoBase.getStoredQuerysMap().put("DaoColaboradorProduto.BuscaProdutosByIdColaborador",JOIN_FROM_PESSOA+" WHERE colaborador_id_pessoa = ?");
		DaoBase.getStoredQuerysMap().put("DaoColaboradorProduto.BuscaColaboradoresByIdProduto",JOIN_FROM_PRODUTO+" WHERE produto_id_produto = ?");
	}
	
	public DaoColaboradorProduto() {
		super(ColaboradorProduto.class);
	}

	@Override
	public ColaboradorProduto getNewEntity() {
		return new ColaboradorProduto();
	}	
	
	public <T extends Entity> List<T> getRelacionamento(Long id,Class<T> relacao,String name) throws SQLException{
		List<Object> params = Arrays.asList(new Object[]{id});
		String select = CrudQuery.getSelectRoot(relacao, "*");
		String where = getStoredQuerysMap().get(name);
		String sql = select + where;
		IQuery query = new Query(sql, params,CrudQuery.getTableName(getClasse()));
		EntityManager<T> dao = DaoFactory.getInstance().getDao(relacao);		
		return dao.getSqlExecutor().executarQuery(query);		
	}
	
	public List<Colaborador> getColaboradores(Long idProduto) throws SQLException{
		return getRelacionamento(idProduto,Colaborador.class,"DaoColaboradorProduto.BuscaColaboradoresByIdProduto");
	}
	
	public List<Produto> getProdutos(Long idColaborador) throws SQLException{
		return getRelacionamento(idColaborador,Produto.class,"DaoColaboradorProduto.BuscaProdutosByIdColaborador");
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

