package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.orm.Dao;
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
	
	public List<Colaborador> getColaboradores(Produto produto,Colaborador colaborador) throws SQLException{
		return getRelacionamento(produto,colaborador,"produtoIdProduto","colaboradorIdPessoa");
	}	
	
	public List<Produto> getProdutos(Colaborador colaborador,Produto produto) throws SQLException{
		return getRelacionamento(colaborador,produto,"colaboradorIdPessoa","produtoIdProduto");
	}
	
	public List<Map<String,Object>> getUntypeColaboradores(Produto produto,Colaborador colaborador) throws SQLException{
		return getAtypeRelacionamento(produto,colaborador,"produtoIdProduto","colaboradorIdPessoa");
	}	
	
	public List<Map<String,Object>> getUntypeProdutos(Colaborador colaborador,Produto produto) throws SQLException{
		return getAtypeRelacionamento(colaborador,produto,"colaboradorIdPessoa","produtoIdProduto");
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

}

