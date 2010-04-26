package br.ueg.openodonto.servico;

import java.util.List;

import javax.faces.event.ActionEvent;

import br.ueg.openodonto.controle.ManageBeanGeral;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.dominio.constante.TiposTelefone;
import br.ueg.openodonto.servico.listagens.core.jsf.JsfVisible;
import br.ueg.openodonto.util.MessageBundle;
import br.ueg.openodonto.util.MessageBundle.MSG_TIPO;


/**
 * @author Vinicius
 *
 */
public class ManageTelefone {

	private JsfVisible<TiposTelefone> jsfTiposTelefone;
	
	private Telefone telefone;
	
	private List<Telefone> telefones;
	
	private String formularioSaida;
	
	@SuppressWarnings("unchecked")
	private ManageBeanGeral backBean;
	
	@SuppressWarnings("unchecked")
	public ManageTelefone(List<Telefone> telefones, ManageBeanGeral backBean){
		this.telefone = new Telefone();
		this.telefones = telefones;
		this.backBean = backBean;
		this.formularioSaida = backBean.getFormularioSaida();
		this.jsfTiposTelefone = new JsfVisible<TiposTelefone>(TiposTelefone.class , this, "telefone.tipoTelefone");
	}
	
	@SuppressWarnings("unchecked")
	public void acaoInserirTelefone(ActionEvent evt){
		if(this.getTelefone() == null ||
				this.getTelefone().getNumero() == null ||
				this.getTelefone().getNumero().isEmpty() ||
				this.getTelefone().getTipoTelefone() == null){
			this.backBean.getMsgBundleEstatica().add(new MessageBundle(MSG_TIPO.dinamica,"Preencha o campo para adicionar.", getFormularioSaida()+":messageTelefone"));
			return;
		}
		getTelefones().add(this.getTelefone());
		setTelefone(new Telefone());
	}

	public void acaoRemoverTelefone(ActionEvent evt){
		if (getTelefoneParametro() != null) 
			this.telefone = getTelefoneParametro();
		else
			return;
		getTelefones().remove(this.telefone);
		setTelefone(new Telefone());
	}
	
	public void acaoCarregarTelefone(ActionEvent evt){
		if (getTelefoneParametro() != null) 
			this.setTelefone(getTelefoneParametro());
	}
	
	@SuppressWarnings("unchecked")
	private Telefone getTelefoneParametro(){
		int index = 0;
		try {
			String parametro = this.backBean.getRequest().getParameter("index");
			index = Integer.parseInt(parametro);
		} catch (Exception e) {
			this.backBean.exibirPopUp("Nao foi possivel carregar o registro.");
			this.backBean.getMsgBundleEstatica().add(new MessageBundle(MSG_TIPO.dinamica,"Nao foi possivel carregar a conta.", getFormularioSaida()+":output"));
			return null;
		}
		if (this.getTelefones().get(index) != null) 
			return this.getTelefones().get(index);
		else{
			this.backBean.exibirPopUp("Registro selecionado invalido.");
			this.backBean.getMsgBundleEstatica().add(new MessageBundle(MSG_TIPO.dinamica,"Registro selecionado invalido.", getFormularioSaida()+":output"));
			return null;
		}
	}
	
	public void acaoAlterarTelefone(ActionEvent evt){
		setTelefone(new Telefone());
	}	
	
	public Telefone getTelefone() {	
		return this.telefone;
	}
	
	public void setTelefone(Telefone telefone) {	
		this.telefone = telefone;
	}
	
	public String getFormularioSaida() {	
		return this.formularioSaida;
	}
	
	public void setFormularioSaida(String formularioSaida) {	
		this.formularioSaida = formularioSaida;
	}	
	
	public List<Telefone> getTelefones() {
		return this.telefones;
	}
	
	public void setTelefones(List<Telefone> telefones) {	
		this.telefones = telefones;
	}

	public JsfVisible<TiposTelefone> getJsfTiposTelefone() {	
		return this.jsfTiposTelefone;
	}
	
	public void setJsfTiposTelefone(JsfVisible<TiposTelefone> jsfTiposTelefone) {	
		this.jsfTiposTelefone = jsfTiposTelefone;
	}		
	
}
