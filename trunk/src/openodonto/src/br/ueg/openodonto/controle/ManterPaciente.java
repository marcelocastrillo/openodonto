/**
 * 
 */
package br.ueg.openodonto.controle;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import br.ueg.openodonto.dominio.constante.TiposUF;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.servico.ManageTelefone;
import br.ueg.openodonto.servico.listagens.core.jsf.JsfVisible;
import br.ueg.openodonto.servico.validador.AbstractValidator;
import br.ueg.openodonto.servico.validador.ValidadorPadrao;
import br.ueg.openodonto.util.PalavrasFormatadas;

public class ManterPaciente extends ManageBeanGeral<Paciente> {

	private ManageTelefone								manageTelefone;
	private JsfVisible<TiposUF>                         jsfEstado;

	public ManterPaciente() {
		super(Paciente.class, "formPaciente", "manterPaciente");
	}

	protected void initExtra() {
		this.jsfEstado = new JsfVisible<TiposUF>(TiposUF.class , this , "paciente.estado");
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

	public void acaoAlterar(ActionEvent evt){
		super.acaoAlterar(evt);
	}
	
	protected List<AbstractValidator> getCamposObrigatorios() {
		List<AbstractValidator> obrigatorios = new ArrayList<AbstractValidator>();
		obrigatorios.add(new ValidadorPadrao("nome", "entradaNome"));	
		obrigatorios.add(new ValidadorPadrao("cpf", "entradaCpf"));
		return obrigatorios;
	}

	public void acaoPesquisar(ActionEvent evt) {
		if(this.getBusca().getParams().get("opcao").equals("cpf"))
			this.getBusca().getParams().put("opcao" , PalavrasFormatadas.clear(PalavrasFormatadas.remover(this.getBusca().getParams().get("opcao"))).trim());
		
		if(this.getBusca().getParams().get("opcao") == null || this.getBusca().getParams().get("opcao").isEmpty() || this.getBusca().getParams().get("opcao").length() < 3){
			this.adicionarMensagemDinamicaJSF("Selecine um filtro de pesquisa.", "formModalPaciente:buscar");
			return;
		}
		
		if(this.getBusca().getParams().get("param") == null || this.getBusca().getParams().get("param").isEmpty()){
			this.adicionarMensagemDinamicaJSF("Informe o parametro para pesquisa.", "formModalPaciente:buscar");
			return;
		}
		
		if(this.getBusca().getParams().get("opcao").equals("nome") && this.getBusca().getParams().get("param").length() < 3){
			this.adicionarMensagemDinamicaJSF("Informe pelo menos 3 caracteres.", "formModalPaciente:buscar");
			getBusca().getParams().put("opcao", null);
			return;
		}
		
		if(this.getBusca().getParams().get("opcao").equals("cpf") && this.getBusca().getParams().get("param").length() != 11){
			this.adicionarMensagemDinamicaJSF("Numero de cpf invalido.", "formModalPaciente:buscar");
			getBusca().getParams().put("opcao", null);
			return;
		}
		
		if(this.getBusca().getParams().get("opcao").equals("email") && this.getBusca().getParams().get("param").length() < 5){
			this.adicionarMensagemDinamicaJSF("O email deve ter mais que 5 caracteres.", "formModalPaciente:buscar");
			getBusca().getParams().put("opcao", null);
			return;
		}
		
		try{
			List<Paciente> result = null;
			if(this.getBusca().getParams().get("opcao").equals("codigo")){
				long cod = 0;
				try{
					cod = Long.parseLong(this.getBusca().getParams().get("param"));
				}catch(Exception ex){
					this.adicionarMensagemDinamicaJSF("Digite Apenas numeros para esta opcao", "formModalPaciente:buscar");
					getBusca().getParams().put("param", null);
					getBusca().getParams().put("opcao", null);
					return;
				}
				result = dao.executarQuery("Paciente.BuscaByCodigo", "codigo", cod);
			}
			else if(this.getBusca().getParams().get("opcao").equals("nome")){
				result = dao.executarQuery("Paciente.BuscaByNome", "nome", "%"+this.getBusca().getParams().get("param")+"%");
			}else if(this.getBusca().getParams().get("opcao").equals("cpf")){
				result = dao.executarQuery("Paciente.BuscaByCPF", "cpf", this.getBusca().getParams().get("param"));
			}else if(this.getBusca().getParams().get("opcao").equals("email")){
			   result = dao.executarQuery("Paciente.BuscaByEmail", "email", this.getBusca().getParams().get("param"));
		    }
			if(result != null){
				this.getBusca().acaoLimpar(null);
				getBusca().getResultados().addAll(result);
				this.adicionarMensagemDinamicaJSF("Foram encontrados "+result.size()+" resultados.", "formModalPaciente:buscar");
			}
			else
				this.adicionarMensagemDinamicaJSF("Nao foi encontrado nenhum resultado.", "formModalPaciente:buscar");
		}catch(Exception ex){
			ex.printStackTrace();
			this.adicionarMensagemJSF("ErroSistema", "formModalPaciente:buscar");
		}finally{
			dao.closeConnection();
		}
		getBusca().getParams().put("param", null);
		getBusca().getParams().put("opcao", null);
	}

	public Paciente getPaciente() {
		return getBackBean();
	}

	public void setPaciente(Paciente aluno) {
		setBackBean(aluno);
	}
	
	public ManageTelefone getManageTelefone() {	
		return this.manageTelefone;
	}
	
	public void setManageTelefone(ManageTelefone manageTelefone) {	
		this.manageTelefone = manageTelefone;
	}

	public JsfVisible<TiposUF> getJsfEstado() {
		return jsfEstado;
	}

	public void setJsfEstado(JsfVisible<TiposUF> jsfEstado) {
		this.jsfEstado = jsfEstado;
	}
	
	

}
