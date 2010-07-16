/**
 * 
 */
package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.controle.validador.ValidadorPadrao;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.util.WordFormatter;

public class ManterPaciente extends ManageBeanGeral<Paciente> {
	
	private static final long serialVersionUID = -2146469226044009908L;
	private ManageTelefone manageTelefone;

	public ManterPaciente() {
		super(Paciente.class);
		Properties params = new Properties();
		params.put("managebeanName", "manterPaciente");
		params.put("formularioSaida", "formPaciente");
		params.put("saidaPadrao", "formPaciente:output");
		params.put("saidaContato", "formPaciente:messageTelefone");
		makeView(params);
	}

	protected void initExtra() {
		this.manageTelefone = new ManageTelefone(getPaciente().getTelefone(), this);
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

	public String acaoAlterar() {
		return super.acaoAlterar();
	}

	protected List<AbstractValidator> getCamposObrigatorios() {
		List<AbstractValidator> obrigatorios = new ArrayList<AbstractValidator>();
		obrigatorios
				.add(new ValidadorPadrao("nome", "formPaciente:entradaNome"));
		obrigatorios.add(new ValidadorPadrao("cpf", "formPaciente:entradaCpf"));
		return obrigatorios;
	}

	public String acaoListar(){
		List<Paciente> result = null;
		long tempoConsulta = -1;
		try{
			long begin = System.currentTimeMillis();
			result = dao.listar(true,"codigo", "nome", "email", "cpf");
			tempoConsulta = System.currentTimeMillis() - begin;
			if (result != null) {
				this.getBusca().acaoLimpar();
				getBusca().getResultados().addAll(result);
				hintResultMessage(result.size(),tempoConsulta);
			} else
				getView().addResourceDynamicMenssage(
						"Nao foi encontrado nenhum resultado.",
						"formModalPaciente:buscar");			
		}catch (Exception e) {
		}
		return DEFAULT_RULE;
	}
	
	public String acaoPesquisar() {
		if (this.getBusca().getParams().get("opcao").equals("cpf"))
			this.getBusca().getParams().put(
					"opcao",
					WordFormatter.clear(
							WordFormatter.remover(this.getBusca().getParams()
									.get("opcao"))).trim());

		if (this.getBusca().getParams().get("opcao") == null
				|| this.getBusca().getParams().get("opcao").isEmpty()
				|| this.getBusca().getParams().get("opcao").length() < 3) {
			getView().addResourceDynamicMenssage(
					"Selecine um filtro de pesquisa.",
					"formModalPaciente:buscar");
			return DEFAULT_RULE;
		}

		if (this.getBusca().getParams().get("param") == null
				|| this.getBusca().getParams().get("param").isEmpty()) {
			getView().addResourceDynamicMenssage(
					"Informe o parametro para pesquisa.",
					"formModalPaciente:buscar");
			return DEFAULT_RULE;
		}

		if (this.getBusca().getParams().get("opcao").equals("nome")
				&& this.getBusca().getParams().get("param").length() < 3) {
			getView().addResourceDynamicMenssage(
					"Informe pelo menos 3 caracteres.",
					"formModalPaciente:buscar");
			getBusca().getParams().put("opcao", null);
			return DEFAULT_RULE;
		}

		if (this.getBusca().getParams().get("opcao").equals("cpf")
				&& this.getBusca().getParams().get("param").length() != 11) {
			getView().addResourceDynamicMenssage("Numero de cpf invalido.",
					"formModalPaciente:buscar");
			getBusca().getParams().put("opcao", null);
			return DEFAULT_RULE;
		}

		if (this.getBusca().getParams().get("opcao").equals("email")
				&& this.getBusca().getParams().get("param").length() < 5) {
			getView().addResourceDynamicMenssage(
					"O email deve ter mais que 5 caracteres.",
					"formModalPaciente:buscar");
			getBusca().getParams().put("opcao", null);
			return DEFAULT_RULE;
		}

		long tempoConsulta=-1;
		try {
			List<Paciente> result = null;
			Paciente paciente = new Paciente();
			OrmFormat orm = new OrmFormat(paciente);
			String[] fields = {"codigo", "nome", "email", "cpf"};
			if (this.getBusca().getParams().get("opcao").equals("codigo")) {
				long cod = 0;
				try {
					cod = Long.parseLong(this.getBusca().getParams().get(
							"param"));
				} catch (Exception ex) {
					getView().addResourceDynamicMenssage(
							"Digite Apenas numeros para esta opcao.",
							"formModalPaciente:buscar");
					getBusca().getParams().put("param", null);
					getBusca().getParams().put("opcao", null);
					return DEFAULT_RULE;
				}
				paciente.setCodigo(cod);
				IQuery query = CrudQuery.getSelectQuery(Paciente.class, orm
						.formatNotNull(), fields);
				long begin = System.currentTimeMillis();
				result = dao.getSqlExecutor().executarQuery(query.getQuery(),
						query.getParams(), null);
				tempoConsulta = System.currentTimeMillis() - begin;
			} else if (this.getBusca().getParams().get("opcao").equals("nome")) {
				paciente.setNome("%"+this.getBusca().getParams().get("param")+"%");
				long begin = System.currentTimeMillis();
				result = dao.getSqlExecutor().executarNamedQuery("Paciente.BuscaByNome", orm.formatNotNull().values(),fields);
				tempoConsulta = System.currentTimeMillis() - begin;
			} else if (this.getBusca().getParams().get("opcao").equals("cpf")) {
				paciente.setCpf(this.getBusca().getParams().get("param"));
				long begin = System.currentTimeMillis();
				result = dao.getSqlExecutor().executarNamedQuery("Paciente.BuscaByCPF", orm.formatNotNull().values(),fields);
				tempoConsulta = System.currentTimeMillis() - begin;
			} else if (this.getBusca().getParams().get("opcao").equals("email")) {
				paciente.setEmail(this.getBusca().getParams().get("param"));
				long begin = System.currentTimeMillis();
				result = dao.getSqlExecutor().executarNamedQuery(
						"Paciente.BuscaByEmail", orm.formatNotNull().values(),fields);
				tempoConsulta = System.currentTimeMillis() - begin;
			}
			if (result != null) {
				this.getBusca().acaoLimpar();
				getBusca().getResultados().addAll(result);
				hintResultMessage(result.size(),tempoConsulta);
			} else
				getView().addResourceDynamicMenssage(
						"Nao foi encontrado nenhum resultado.",
						"formModalPaciente:buscar");
		} catch (Exception ex) {
			ex.printStackTrace();
			getView().addResourceMessage("ErroSistema.",
					"formModalPaciente:buscar");
		} finally {
			dao.closeConnection();
		}
		getBusca().getParams().put("param", null);
		getBusca().getParams().put("opcao", null);
		return DEFAULT_RULE;
	}

	private void hintResultMessage(int size , long time){
		StringBuilder fetchedMsg = new StringBuilder();
		fetchedMsg.append(String.format("Foram encontrados %d resultados.",size));
		getView().addResourceDynamicMenssage(fetchedMsg.toString(),"formModalPaciente:buscar");
		if(time != -1){
			StringBuilder FetchedTimeMsg = new StringBuilder();
			double fTime = time / 1000.0;
			FetchedTimeMsg.append(String.format("( %.3f segundos )",fTime));
			getView().addResourceDynamicMenssage(FetchedTimeMsg.toString(),"formModalPaciente:queryTime");
		}
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

}
