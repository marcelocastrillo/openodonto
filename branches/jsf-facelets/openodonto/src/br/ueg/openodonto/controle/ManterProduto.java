package br.ueg.openodonto.controle;

import java.util.Map;

import br.ueg.openodonto.controle.servico.ExampleRequest;
import br.ueg.openodonto.controle.servico.ManageExample;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.Query;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.validator.EmptyValidator;
import br.ueg.openodonto.validator.NullValidator;

public class ManterProduto extends ManageBeanGeral<Produto>{

	private static final long serialVersionUID = -7320449089337162282L;
	
	private ManageExample<Produto>       manageExample;
	private static Map<String, String>   params;
	private Search<Produto>              search;
	private MessageDisplayer             displayer;	
	
	public ManterProduto() {
		super(Produto.class);
	}	

	private Produto buildProdutoExample(Searchable<Produto> searchable){
		ExampleRequest<Produto> request  = new ExampleRequest<Produto>(searchable);		
		request.getFilterRelation().add(request.new TypedFilter("nomeFilter", "nome"));
		request.getFilterRelation().add(request.new TypedFilter("idFilter","codigo"));
		request.getFilterRelation().add(request.new TypedFilter("descricaoFilter","descricao"));
		request.getInvalidPermiteds().add(NullValidator.class);
		request.getInvalidPermiteds().add(EmptyValidator.class);
		Produto target = manageExample.processExampleRequest(request);
		return target;
	}
	
	protected class SearchProdutoHandler extends SearchBeanHandler<Produto>{
		private String[] showColumns = {"codigo", "nome", "shortDescription"};
		@Override
		public Produto buildExample(Searchable<Produto> searchable) {
			return buildProdutoExample(searchable);
		}
		@Override
		public String[] getShowColumns() {
			return showColumns;
		}		
		@Override/*TODO Este método é temporário e será refatorado/extinto*/
		public IQuery getQuery(Produto example){
			OrmFormat format = new OrmFormat(example);
			Map<String, Object> params = format.formatNotNull();
			Object nome;
			if((nome = params.get("nome")) != null){
				params.put("nome", "%"+nome+"%");
			}
			Object descricao;
			if((descricao = params.get("descricao")) != null){
				params.put("descricao", "%"+descricao+"%");
			}			
			Query query = (Query)CrudQuery.getSelectQuery(Paciente.class,params, getShowColumns());
			if(nome != null){
			    query.setQuery(query.getQuery().replace("nome = ?", "nome like ?"));
			}
			if(descricao != null){
				query.setQuery(query.getQuery().replace("descricao = ?", "descricao like ?"));
			}
			return query;
		}
	}	
	

}
