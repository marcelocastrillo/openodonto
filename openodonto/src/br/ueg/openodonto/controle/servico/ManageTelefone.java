package br.ueg.openodonto.controle.servico;

import java.io.Serializable;
import java.util.List;

import br.ueg.openodonto.controle.ManageBeanGeral;
import br.ueg.openodonto.dominio.Telefone;

/**
 * @author Vinicius
 * 
 */
public class ManageTelefone implements Serializable {

	private static final long serialVersionUID = 8556507770756119797L;

	private Telefone telefone;

	private List<Telefone> telefones;

	@SuppressWarnings("unchecked")
	private ManageBeanGeral backBean;
	
	private String saidaContato;

	@SuppressWarnings("unchecked")
	public ManageTelefone(List<Telefone> telefones, ManageBeanGeral backBean) {
		this.telefone = new Telefone();
		this.telefones = telefones;
		this.backBean = backBean;
	}

	private void buildSaidaContato(){
		this.saidaContato =    this.backBean.getView().getProperties().get("formularioSaida") + ":" +		
                               this.backBean.getView().getProperties().get("saidaContato");
	}
	
	public void acaoInserirTelefone() {
		if (this.getTelefone() == null
				|| this.getTelefone().getNumero() == null
				|| this.getTelefone().getNumero().isEmpty()
				|| this.getTelefone().getTipoTelefone() == null) {
			this.backBean.getView().addResourceDynamicMenssage("* Tipo e Numero são obrigatórios !",	getSaidaContato());
			return;
		}
		getTelefones().add(this.getTelefone());
		setTelefone(new Telefone());
	}

	public void acaoRemoverTelefone() {
		if (this.telefone != null){
			getTelefones().remove(this.telefone);
			setTelefone(new Telefone());
		}
	}

	public void acaoAlterarTelefone() {
		setTelefone(new Telefone());
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

	private String getSaidaContato() {
		if(saidaContato == null){
			buildSaidaContato();
		}
		return saidaContato;
	}
	
	
}
