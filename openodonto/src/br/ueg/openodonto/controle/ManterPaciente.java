package br.ueg.openodonto.controle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.controle.validador.ValidadorPadrao;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.servico.busca.InputField;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.servico.busca.Search;
import br.ueg.openodonto.servico.busca.event.AbstractSearchListener;
import br.ueg.openodonto.servico.busca.event.SearchEvent;
import br.ueg.openodonto.validacao.EmptyValidator;
import br.ueg.openodonto.validacao.NullValidator;
import br.ueg.openodonto.validacao.Validator;

public class ManterPaciente extends ManageBeanGeral<Paciente> {
	
	private static final long serialVersionUID = -2146469226044009908L;
	
	private ManageTelefone                manageTelefone;
	private static Map<String, String>    params;
	private Search<Paciente>              search;
	private MessageDisplayer              displayer;
	
	static{
		params = new HashMap<String, String>();
		params.put("managebeanName", "manterPaciente");
		params.put("formularioSaida", "formPaciente");
		params.put("formModalSearch", "formSearch");
		params.put("nameModalSearch", "painelBusca");
		params.put("saidaPadrao", "formPaciente:output");
		params.put("saidaContato", "messageTelefone");
	}

	public ManterPaciente() {
		super(Paciente.class);
	}

	protected void initExtra() {
		this.displayer = new ViewDisplayer();
		this.manageTelefone = new ManageTelefone(getPaciente().getTelefone(), this);
		this.search = new SearchBase<Paciente>(new SearchablePaciente(this.displayer),"Buscar Paciente");
		this.search.addSearchListener(new SearchPacienteHandler());
		this.search.addSearchListener(new SearchSelectedHandler());
		makeView(params);
	}

	protected void carregarExtra() {
		manageTelefone.setTelefones(getPaciente().getTelefone());
	}

	protected List<String> getCamposFormatados() {
		List<String> formatados = new ArrayList<String>();
		formatados.add("nome");
		formatados.add("cpf");
		formatados.add("cidade");
		formatados.add("endereco");
		formatados.add("responsavel");
		formatados.add("referencia");
		return formatados;
	}

	protected List<AbstractValidator> getCamposObrigatorios() {
		List<AbstractValidator> obrigatorios = new ArrayList<AbstractValidator>();
		obrigatorios.add(new ValidadorPadrao("nome", "formPaciente:entradaNome"));
		obrigatorios.add(new ValidadorPadrao("cpf", "formPaciente:entradaCpf"));
		return obrigatorios;
	}
	
	public Search<Paciente> getSearch() {
		return search;
	}

	public Paciente getPaciente() {
		return getBackBean();
	}

	public void setPaciente(Paciente paciente) {
		setBackBean(paciente);
	}

	public ManageTelefone getManageTelefone() {
		return this.manageTelefone;
	}

	public void setManageTelefone(ManageTelefone manageTelefone) {
		this.manageTelefone = manageTelefone;
	}

	private Paciente buildExample(SearchablePaciente searchable){
		Paciente target = new Paciente();
		InputField<String> inputNome = searchable.getCommonInput("nomeFilter");
		InputField<String> inputCpf = searchable.getCommonInput("cpfFilter");
		InputField<String> inputEmail = searchable.getCommonInput("emailFilter");
		InputField<String> inputId = searchable.getCommonInput("idFilter");
		Validator validatorNome = inputNome.getValidators().get(0);
		Validator validatorCpf = inputCpf.getValidators().get(0);
		Validator validatorEmail = inputEmail.getValidators().get(0);
		Validator validatorId = inputId.getValidators().get(0);
		if(validatorNome.isValid()){
			target.setNome(inputNome.getValue());
		}else if(!(validatorNome.getSource() instanceof NullValidator) &&
				!(validatorNome.getSource() instanceof EmptyValidator)){
			displayer.display("* " + searchable.getFiltersMap().get("nomeFilter").getLabel() + " : " + validatorNome.getErrorMessage());
		}
		if(validatorEmail.isValid()){
			target.setEmail(inputEmail.getValue());
		}else if(!(validatorEmail.getSource() instanceof NullValidator) &&
				!(validatorEmail.getSource() instanceof EmptyValidator)){
			displayer.display("* " + searchable.getFiltersMap().get("emailFilter").getLabel() + " : " + validatorEmail.getErrorMessage());
		}
		if(validatorCpf.isValid()){
			target.setCpf(inputCpf.getValue());
		}
		if(validatorId.isValid()){
			target.setCodigo(Long.valueOf(inputId.getValue()));
		}				
		return target;
	}
	
	protected class ViewDisplayer implements MessageDisplayer{
		@Override
		public void display(String message) {
			getView().addResourceDynamicMenssage(message, "searchDefaultOutput");
		}		
	}
	
	protected class SearchPacienteHandler extends AbstractSearchListener{
		@Override
		@SuppressWarnings("unchecked")
		public void searchPerformed(SearchEvent event) {
			long time = System.currentTimeMillis();
			try {				
				Paciente target = buildExample((SearchablePaciente)((Search<Paciente>)event.getSource()).getSearchable());
				OrmFormat format = new OrmFormat(target);
				IQuery query = CrudQuery.getSelectQuery(Paciente.class, format.formatNotNull(),  "codigo", "nome", "email", "cpf");				
				List<Map<String,Object>> result = dao.getSqlExecutor().executarUntypedQuery(query.getQuery(), query.getParams(), 100);
				search.getResults().clear();
				search.getResults().addAll(wrapResult(result));
				time = System.currentTimeMillis() - time;
				showTimeQuery(params.get("formModalSearch"), result.size(), time);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	
}
