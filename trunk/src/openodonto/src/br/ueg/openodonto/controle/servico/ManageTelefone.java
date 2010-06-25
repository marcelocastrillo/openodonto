package br.ueg.openodonto.controle.servico;

import static br.ueg.openodonto.controle.ManageBeanGeral.DEFAULT_RULE;

import java.util.List;

import br.ueg.openodonto.controle.ManageBeanGeral;
import br.ueg.openodonto.dominio.Telefone;

/**
 * @author Vinicius
 * 
 */
public class ManageTelefone {

    private Telefone telefone;

    private List<Telefone> telefones;

    @SuppressWarnings("unchecked")
    private ManageBeanGeral backBean;

    @SuppressWarnings("unchecked")
    public ManageTelefone(List<Telefone> telefones, ManageBeanGeral backBean) {
	this.telefone = new Telefone();
	this.telefones = telefones;
	this.backBean = backBean;
    }

    public String acaoInserirTelefone() {
	if (this.getTelefone() == null
		|| this.getTelefone().getNumero() == null
		|| this.getTelefone().getNumero().isEmpty()
		|| this.getTelefone().getTipoTelefone() == null) {
	    this.backBean.getView().addResourceDynamicMenssage(
		    "Preencha o campo para adicionar.",
		    this.backBean.getView().getParams().getProperty(
			    "saidaContato"));
	    return DEFAULT_RULE;
	}
	getTelefones().add(this.getTelefone());
	setTelefone(new Telefone());
	return DEFAULT_RULE;
    }

    public String acaoRemoverTelefone() {
	if (getTelefoneParametro() != null)
	    this.telefone = getTelefoneParametro();
	else
	    return DEFAULT_RULE;
	getTelefones().remove(this.telefone);
	setTelefone(new Telefone());
	return DEFAULT_RULE;
    }

    public String acaoCarregarTelefone() {
	if (getTelefoneParametro() != null)
	    this.setTelefone(getTelefoneParametro());
	return DEFAULT_RULE;
    }

    private Telefone getTelefoneParametro() {
	int index = 0;
	try {
	    String parametro = this.backBean.getContext().getParameter("index");
	    index = Integer.parseInt(parametro);
	} catch (Exception e) {
	    this.backBean.exibirPopUp("Nao foi possivel carregar o registro.");
	    this.backBean.getView().addLocalDynamicMenssage(
		    "Nao foi possivel carregar o registro.", "saidaPadrao",
		    true);
	    return null;
	}
	if (this.getTelefones().get(index) != null)
	    return this.getTelefones().get(index);
	else {
	    this.backBean.exibirPopUp("Registro selecionado invalido.");
	    this.backBean.getView().addLocalDynamicMenssage(
		    "Registro selecionado invalido.", "saidaPadrao", true);
	    return null;
	}
    }

    public String acaoAlterarTelefone() {
	setTelefone(new Telefone());
	return DEFAULT_RULE;
    }

    public Telefone getTelefone() {
	return this.telefone;
    }

    public void setTelefone(Telefone telefone) {
	this.telefone = telefone;
    }

    public List<Telefone> getTelefones() {
	return this.telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
	this.telefones = telefones;
    }

}
