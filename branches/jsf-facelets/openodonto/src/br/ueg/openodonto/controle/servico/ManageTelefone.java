package br.ueg.openodonto.controle.servico;

import java.io.Serializable;
import java.util.List;

import br.ueg.openodonto.controle.ManageBeanGeral;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.dominio.constante.TiposTelefone;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.validator.ValidatorFactory;

/**
 * @author Vinicius
 * 
 */
public class ManageTelefone implements Serializable {

	private static final long serialVersionUID = 8556507770756119797L;

	private Telefone telefone;

	private String   editNumber;
	
	private TiposTelefone   editType;
	
	private List<Telefone> telefones;
	
	private Validator      validatorType;
	private Validator      validatorNumber;

	@SuppressWarnings("unchecked")
	private ManageBeanGeral backBean;
	
	private String saidaContato;
	
	private String saidaEditarContato;
	
	private boolean sucessEdit;

	@SuppressWarnings("unchecked")
	public ManageTelefone(List<Telefone> telefones, ManageBeanGeral backBean) {
		this.telefone = new Telefone();
		this.telefones = telefones;
		this.backBean = backBean;
		this.validatorNumber = ValidatorFactory.newStrRangeLen(15, 4, true);
		this.validatorType = ValidatorFactory.newNull();
	}

	private void buildSaidaContato(){
		this.saidaContato =    this.backBean.getView().getProperties().get("formularioSaida") + ":" + "messageTelefone";
	}
	
	private void buildSaidaEditarContato() {
		this.saidaEditarContato = "formAlterarTelefone:messageEditTelefone";
	}

	public void acaoCancelarEdicao(){
		setEditType(null);
		setEditNumber(null);
		setTelefone(new Telefone());
		setSucessEdit(false);
	}
	
	public void acaoCarregarEdicao(){
		setEditType(getTelefone().getTipoTelefone());
		setEditNumber(getTelefone().getNumero());
	}
	
	public void acaoEditar(){
		setSucessEdit(false);
		this.validatorNumber.setValue(getEditNumber());
		this.validatorType.setValue(getEditType());
		if(validatorNumber.isValid() && validatorType.isValid()){
			getTelefone().setNumero(getEditNumber());
			getTelefone().setTipoTelefone(getEditType());
			setTelefone(new Telefone());
			setSucessEdit(true);
		}else{
			this.backBean.getView().addResourceDynamicMenssage("* Tipo e Numero são obrigatórios !",	getSaidaEditarContato());
		}
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

	public TiposTelefone getEditType() {
		return editType;
	}

	public void setEditType(TiposTelefone editType) {
		this.editType = editType;
	}
	
	public String getEditNumber() {
		return editNumber;
	}

	public void setEditNumber(String editNumber) {
		this.editNumber = editNumber;
	}

	public void setSaidaContato(String saidaContato) {
		this.saidaContato = saidaContato;
	}	
	
	public boolean getSucessEdit() {
		return sucessEdit;
	}

	public void setSucessEdit(boolean sucessEdit) {
		this.sucessEdit = sucessEdit;
	}

	private String getSaidaEditarContato(){
		if(saidaEditarContato == null){
			buildSaidaEditarContato();
		}
		return saidaEditarContato;
	}	

	private String getSaidaContato() {
		if(saidaContato == null){
			buildSaidaContato();
		}
		return saidaContato;
	}
	
	
}
