package br.ueg.openodonto.controle;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.busca.AbstractSearchable;
import br.ueg.openodonto.controle.busca.ResultFacadeBean;
import br.ueg.openodonto.controle.context.ApplicationContext;
import br.ueg.openodonto.controle.context.OpenOdontoWebContext;
import br.ueg.openodonto.controle.servico.ValidationRequest;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.DaoFactory;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Entity;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.ResultFacade;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.Searchable;
import br.ueg.openodonto.servico.busca.event.AbstractSearchListener;
import br.ueg.openodonto.servico.busca.event.SearchEvent;
import br.ueg.openodonto.servico.busca.event.SearchSelectedEvent;
import br.ueg.openodonto.util.PBUtil;
import br.ueg.openodonto.util.WordFormatter;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.visao.ApplicationView;
import br.ueg.openodonto.visao.ApplicationViewFactory;
import br.ueg.openodonto.visao.ApplicationViewFactory.ViewHandler;

/**
 * @author vinicius.rodrigues
 * 
 * @param <T>
 */
public abstract class ManageBeanGeral<T extends Entity> implements Serializable{

	private static final long serialVersionUID = -6270953407778330292L;

	private T                   backBean;
	private Class<T>            classe;
	protected EntityManager<T>  dao;
	private ApplicationContext  context;
	private ApplicationView     view;
	private String              msgBundle;

	public ManageBeanGeral(Class<T> classe) {
		this.classe = classe;
		init();
	}
	
	protected void initExtra(){}

	protected void init() {
		this.backBean = fabricarNovoBean();
		this.dao = DaoFactory.getInstance().getDao(classe);		
		this.context = new OpenOdontoWebContext();
		initExtra();
	}

	protected T fabricarNovoBean() {
		try {
			return classe.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void acaoShowed() {
		getView().showAction();
	}

	public void exibirSaida() {
		getView().showOut();
	}

	public void exibirPopUp(String message) {
		getView().showPopUp(message);
	}

	@SuppressWarnings("unchecked")
	protected List<ValidationRequest> getCamposObrigatorios(){
		return Collections.EMPTY_LIST;
	}

	@SuppressWarnings("unchecked")
	protected List<String> getCamposFormatados(){
		return Collections.EMPTY_LIST;
	}

	protected void ValidarCamposExtras(){};

	protected void acaoValidarCampos() throws Exception {
		ValidarCamposExtras();
		List<String> camposFormatados = getCamposFormatados();
		for (String path : camposFormatados) {
			Object o = PBUtil.instance().getNestedProperty(getBackBean(), path);
			String campoParaFormatar = null;
			if (o != null){
				campoParaFormatar = String.valueOf(o);
			}else{
				continue;
			}
			if (!campoParaFormatar.isEmpty()) {
				String atributoFormatado = WordFormatter.clear(WordFormatter.remover(campoParaFormatar)).toUpperCase();
				PBUtil.instance().setNestedProperty(getBackBean(), path, atributoFormatado);
			}
		}
	}

	protected boolean checarCamposObrigatoriosExtras() {
		return true;
	}

	protected boolean checarCamposObrigatorios() throws Exception {
		boolean returned = true;
		List<ValidationRequest> camposObrigatorios = getCamposObrigatorios();
		for (ValidationRequest validation : camposObrigatorios) {
			Validator validador = validation.getValidator();
			validador.setValue(PBUtil.instance().getNestedProperty(getBackBean(), validation.getPath()));
			if (!validador.isValid()) {
				getView().addLocalDynamicMenssage("* " + validador.getErrorMessage(), validation.getOut(), false);
				returned = false;
			}
		}
		return checarCamposObrigatoriosExtras() && returned;
	}

	public void acaoSalvar() {
		boolean alredy = false;
		try {
			acaoValidarCampos();
			if (!checarCamposObrigatorios()) {
				exibirPopUp(getView().getMessageFromResource("camposObrigatorios"));
				getView().addLocalDynamicMenssage("Campos obrigatorios invalidos.", "saidaPadrao", true);
				return;
			}
			if (dao.exists(getBackBean())){
				alredy = true;
			}
			acaoSalvarExtra();
			this.dao.alterar(this.backBean);
		} catch (Exception ex) {
			exibirPopUp(getView().getMessageFromResource("ErroSistema"));
			getView().addLocalMessage("ErroSistema", "saidaPadrao", true);
			ex.printStackTrace();
			return;
		} finally {
			dao.closeConnection();
		}
		init();
		exibirPopUp(getView().getMessageFromResource(alredy ? "Atualizado" : "Cadastro"));
		getView().addLocalMessage(alredy ? "Atualizado" : "Cadastro", "saidaPadrao", true);
	}
	
	protected void showTimeQuery(String formName,int size , long time){
		StringBuilder fetchedMsg = new StringBuilder();
		fetchedMsg.append(String.format("Foram encontrados %d resultados.",size));
		getView().addResourceDynamicMenssage(fetchedMsg.toString(),formName + ":buscar");
		if(time != -1){
			StringBuilder FetchedTimeMsg = new StringBuilder();
			double fTime = time / 1000.0;
			FetchedTimeMsg.append(String.format("( %.3f segundos )",fTime));
			getView().addResourceDynamicMenssage(FetchedTimeMsg.toString(), formName + ":queryTime");
		}
	}

	public void acaoSalvarExtra() {}

	public void acaoRemoverSim() {
		try {
			this.dao.remover(this.backBean);
		} catch (Exception e) {
			exibirPopUp("Nao foi possivel remover o registro.");
			getView().addLocalDynamicMenssage("Nao foi possivel remover o registro.","saidaPadrao", true);
			return;
		} finally {
			dao.closeConnection();
		}
		init();
		exibirPopUp(getView().getMessageFromResource("removido"));
		getView().addLocalMessage("removido", "saidaPadrao", true);
	}

	public void acaoAtualizar() {
		getView().refresh();
	}

	protected Usuario getUsuarioSessao() {
		return getContext().getUsuarioSessao();
	}


	protected void carregarExtra(){};

	public T getBackBean() {
		return this.backBean;
	}

	public void setBackBean(T backBean) {
		this.backBean = backBean;
	}
	
	public String getMsgBundle() {
		return this.msgBundle;
	}

	public void setMsgBundle(String msgBundle) {
		this.msgBundle = msgBundle;
	}
	
	public ApplicationContext getContext() {
		return context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public ApplicationView getView() {
		return view;
	}

	public EntityManager<T> getDao() {
		return dao;
	}

	public void setView(ApplicationView view) {
		this.view = view;
	}

	public void makeView(Map<String, String> params) {
		this.view = ApplicationViewFactory.getViewInstance(ViewHandler.JSF,	params);
	}
	
	protected class ViewDisplayer implements MessageDisplayer,Serializable{
		private static final long serialVersionUID = -745463611021419165L;
		private String output;		
		public ViewDisplayer(String output) {
			this.output = output;
		}		
		@Override
		public void display(String message) {
			getView().addResourceDynamicMenssage(message, output);
		}		
	}
	
	protected List<ResultFacade> wrapResult(List<Map<String, Object>> result){
		List<ResultFacade> resultWrap = new ArrayList<ResultFacade>(result.size());
		Iterator<Map<String, Object>> iterator = result.iterator();
		while(iterator.hasNext()){
			resultWrap.add(new ResultFacadeBean(iterator.next()));
		}
		return resultWrap;
	}
	
	protected abstract class SearchBeanHandler<E> extends AbstractSearchListener{
		@Override
		@SuppressWarnings("unchecked")
		public void searchPerformed(SearchEvent event) {
			long time = System.currentTimeMillis();
			try {				
				Search<E> search = (Search<E>)event.getSource();
				List<Map<String,Object>> result = evaluteResult(search);
				search.getResults().clear();
				search.getResults().addAll(wrapResult(result));
				time = System.currentTimeMillis() - time;
				showTimeQuery(getView().getProperties().get("formModalSearch"), result.size(), time);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		
		public List<Map<String,Object>> evaluteResult(Search<E> search) throws SQLException{
			E target = buildExample(search.getSearchable());
			IQuery query = getQuery(target);				
			List<Map<String,Object>> result = dao.getSqlExecutor().executarUntypedQuery(query);
			return result;
		}
		
		public IQuery getQuery(E example){
			OrmFormat format = new OrmFormat(example);
			return CrudQuery.getSelectQuery(classe, format.formatNotNull(),  getShowColumns());
		}
		public abstract String[] getShowColumns();
		
		public E buildExample(Searchable<E> searchable){
			return ((AbstractSearchable<E>)searchable).buildExample();
		}
	}
	
	public class SearchSelectedHandler extends AbstractSearchListener implements Serializable{
		private static final long serialVersionUID = -8455981783765205027L;
		@Override
		@SuppressWarnings("unchecked")
		public void resultRequested(SearchSelectedEvent event) {
			try{
				T entity = (T)getBackBean().getClass().newInstance();
				setBackBean(entity);
				OrmFormat format = new OrmFormat(entity);
				format.parse(event.getSelected().getValue());
				dao.load(getBackBean());
			}catch (Exception e) {
				e.printStackTrace();
			}
			carregarExtra();			
		}
	}

}
