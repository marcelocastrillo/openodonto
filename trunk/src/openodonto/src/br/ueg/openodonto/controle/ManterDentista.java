package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.ueg.openodonto.controle.servico.ManageTelefone;
import br.ueg.openodonto.controle.validador.AbstractValidator;
import br.ueg.openodonto.controle.validador.ValidadorPadrao;
import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.util.WordFormatter;

public class ManterDentista extends ManageBeanGeral<Dentista> {

    private ManageTelefone manageTelefone;

    public ManterDentista() {
	super(Dentista.class);
	Properties params = new Properties();
	params.put("managebeanName", "manterDentista");
	params.put("formularioSaida", "formDentista");
	params.put("saidaPadrao", "formDentista:output");
	params.put("saidaContato", "formDentista:messageTelefone");
	makeView(params);
    }

    @Override
    public String acaoPesquisar() {
	if (this.getBusca().getParams().get("opcao").equals("cro"))
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
		    "formModalDentista:buscar");
	    return DEFAULT_RULE;
	}

	if (this.getBusca().getParams().get("param") == null
		|| this.getBusca().getParams().get("param").isEmpty()) {
	    getView().addResourceDynamicMenssage(
		    "Informe o parametro para pesquisa.",
		    "formModalDentista:buscar");
	    return DEFAULT_RULE;
	}

	if (this.getBusca().getParams().get("opcao").equals("nome")
		&& this.getBusca().getParams().get("param").length() < 3) {
	    getView().addResourceDynamicMenssage(
		    "Informe pelo menos 3 caracteres.",
		    "formModalDentista:buscar");
	    getBusca().getParams().put("opcao", null);
	    return DEFAULT_RULE;
	}

	if (this.getBusca().getParams().get("opcao").equals("especialidade")
		&& this.getBusca().getParams().get("param").length() < 5) {
	    getView().addResourceDynamicMenssage(
		    "A especialidade deve ter mais que 5 caracteres.",
		    "formModalDentista:buscar");
	    getBusca().getParams().put("opcao", null);
	    return DEFAULT_RULE;
	}

	try {
	    List<Dentista> result = null;
	    Dentista dentista = new Dentista();
	    OrmFormat orm = new OrmFormat(dentista);
	    String[] fields = { "codigo", "nome", "cro", "especialidade" };
	    if (this.getBusca().getParams().get("opcao").equals("codigo")) {
		long cod = 0;
		try {
		    cod = Long.parseLong(this.getBusca().getParams().get(
			    "param"));
		} catch (Exception ex) {
		    getView().addResourceDynamicMenssage(
			    "Digite Apenas numeros para esta opcao.",
			    "formModalDentista:buscar");
		    getBusca().getParams().put("param", null);
		    getBusca().getParams().put("opcao", null);
		    return DEFAULT_RULE;
		}
		dentista.setCodigo(cod);
		IQuery query = CrudQuery.getSelectQuery(Dentista.class, orm
			.formatNotNull(), fields);
		result = dao.getSqlExecutor().executarQuery(query.getQuery(),
			query.getParams(), null);
	    } else if (this.getBusca().getParams().get("opcao").equals("nome")) {
		dentista.setNome(this.getBusca().getParams().get("param"));
		result = dao.getSqlExecutor().executarNamedQuery(
			"Dentista.findByNome", orm.formatNotNull().values(),
			fields);
	    } else if (this.getBusca().getParams().get("opcao").equals("cro")) {
		long cro = 0;
		try {
		    cro = Long.parseLong(this.getBusca().getParams().get(
			    "param"));
		} catch (Exception ex) {
		    getView().addResourceDynamicMenssage(
			    "Digite Apenas numeros para esta opcao.",
			    "formModalDentista:buscar");
		    getBusca().getParams().put("param", null);
		    getBusca().getParams().put("opcao", null);
		    return DEFAULT_RULE;
		}
		dentista.setCro(cro);
		result = dao.getSqlExecutor().executarNamedQuery(
			"Dentista.findByCro", orm.formatNotNull().values(),
			fields);
	    } else if (this.getBusca().getParams().get("opcao").equals(
		    "especialidade")) {
		dentista.setEspecialidade(this.getBusca().getParams().get(
			"param"));
		result = dao.getSqlExecutor().executarNamedQuery(
			"Dentista.findEspecialidade",
			orm.formatNotNull().values(), fields);
	    }
	    if (result != null) {
		this.getBusca().acaoLimpar();
		getBusca().getResultados().addAll(result);
		getView().addResourceDynamicMenssage(
			"Foram encontrados " + result.size() + " resultados.",
			"formModalDentista:buscar");
	    } else
		getView().addResourceDynamicMenssage(
			"Nao foi encontrado nenhum resultado.",
			"formModalDentista:buscar");
	} catch (Exception ex) {
	    ex.printStackTrace();
	    getView().addResourceMessage("ErroSistema.",
		    "formModalDentista:buscar");
	} finally {
	    dao.closeConnection();
	}
	getBusca().getParams().put("param", null);
	getBusca().getParams().put("opcao", null);
	return DEFAULT_RULE;
    }

    @Override
    protected List<String> getCamposFormatados() {
	List<String> formatados = new ArrayList<String>();
	formatados.add("nome");
	formatados.add("especialidade");
	formatados.add("cidade");
	formatados.add("endereco");
	return formatados;
    }

    @Override
    protected List<AbstractValidator> getCamposObrigatorios() {
	List<AbstractValidator> obrigatorios = new ArrayList<AbstractValidator>();
	obrigatorios
		.add(new ValidadorPadrao("nome", "formDentista:entradaNome"));
	obrigatorios.add(new ValidadorPadrao("cro", "formDentista:entradaCro"));
	obrigatorios.add(new ValidadorPadrao("especialidade",
		"formDentista:entradaEspecialidade"));
	return obrigatorios;
    }

    @Override
    protected void initExtra() {
	this.manageTelefone = new ManageTelefone(getDentista().getTelefone(),
		this);
    }

    @Override
    protected void carregarExtra() {
	manageTelefone.setTelefones(getDentista().getTelefone());
    }

    public Dentista getDentista() {
	return getBackBean();
    }

    public void setDentista(Dentista dentista) {
	setBackBean(dentista);
    }

    public ManageTelefone getManageTelefone() {
	return manageTelefone;
    }

    public void setManageTelefone(ManageTelefone manageTelefone) {
	this.manageTelefone = manageTelefone;
    }

}
