package br.ueg.openodonto.servico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import br.ueg.openodonto.controle.ManageBeanGeral;
import br.ueg.openodonto.util.MessageBundle;
import br.ueg.openodonto.util.ReflexaoUtil;
import br.ueg.openodonto.util.MessageBundle.MSG_TIPO;


/**
 * @author vinicius.rodrigues
 *
 * @param <T>
 */
public class SubBusca<T>{

	private Map<String , String> params;
	
	private List<T> resultados;
	
	private String objetoPath;
	
	@SuppressWarnings("unchecked")
	private ManageBeanGeral backBean;
	
	@SuppressWarnings("unchecked")
	public SubBusca(ManageBeanGeral backBean,String objetoPath){
		this.params = new HashMap<String, String>();
		this.backBean = backBean;
		this.resultados = new ArrayList<T>();
		this.objetoPath = objetoPath;
	}	
	
	public void acaoLimpar(ActionEvent evt){
		if(params != null)
			for(String key : params.keySet())
				params.put(key, null);
		if(resultados != null)
			resultados.clear();
	}
	
	@SuppressWarnings("unchecked")
	private T getRegistroParametro(){
		int index = 0;
		try {
			String parametro = this.backBean.getRequest().getParameter("index");
			index = Integer.parseInt(parametro);
		} catch (Exception e) {
			this.backBean.exibirPopUp("Nao foi possivel carregar o registro.");
			this.backBean.getMsgBundleEstatica().add(new MessageBundle(MSG_TIPO.dinamica,"Nao foi possivel carregar o registro.", this.backBean.getFormularioSaida()+":output"));
			return null;
		}
		if (this.getResultados().get(index) != null)
			return this.getResultados().get(index);
		else{
			this.backBean.exibirPopUp("Registro selecionado invalido.");
			this.backBean.getMsgBundleEstatica().add(new MessageBundle(MSG_TIPO.dinamica,"Registro selecionado invalido.", this.backBean.getFormularioSaida()+":output"));
			return null;
		}
	}

	public void acaoCarregarRegistro(ActionEvent evt){
		T objeto = getRegistroParametro();
		ReflexaoUtil.setTsimples(this.getBackBean(), objetoPath , objeto);
	}
		
	public Map<String, String> getParams() {	
		return this.params;
	}
	
	public void setParams(Map<String, String> params) {	
		this.params = params;
	}

	public List<T> getResultados() {	
		return this.resultados;
	}
	
	public void setResultados(List<T> resultados) {	
		this.resultados = resultados;
	}
	
	@SuppressWarnings("unchecked")
	private ManageBeanGeral getBackBean() {	
		return this.backBean;
	}		
	
}
