package br.ueg.openodonto.controle.servico;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.controle.ManageBeanGeral;
import br.ueg.openodonto.util.PBUtil;

/**
 * @author vinicius.rodrigues
 * 
 * @param <T>
 */
public class ManageSearch<T> implements Serializable {

	private static final long serialVersionUID = -1563000243980108012L;

	private Map<String, String> params;

	private List<T> resultados;

	private String objetoPath;

	@SuppressWarnings("unchecked")
	private ManageBeanGeral backBean;

	@SuppressWarnings("unchecked")
	public ManageSearch(ManageBeanGeral backBean, String objetoPath) {
		this.params = new HashMap<String, String>();
		this.backBean = backBean;
		this.resultados = new ArrayList<T>();
		this.objetoPath = objetoPath;
	}

	public String acaoLimpar() {
		if (params != null)
			for (String key : params.keySet())
				params.put(key, null);
		if (resultados != null)
			resultados.clear();
		return "";
	}

	private T getRegistroParametro() {
		int index = 0;
		try {
			String parametro = this.backBean.getContext().getParameter("index");
			index = Integer.parseInt(parametro);
		} catch (Exception e) {
			this.backBean.exibirPopUp("Nao foi possivel carregar o registro.");
			this.backBean.getView().addLocalDynamicMenssage("Nao foi possivel carregar o registro.", "saidaPadrao",	true);
			return null;
		}
		if (this.getResultados().get(index) != null)
			return this.getResultados().get(index);
		else {
			this.backBean.exibirPopUp("Registro selecionado invalido.");
			this.backBean.getView().addLocalDynamicMenssage(
					"Registro selecionado invalido.", "saidaPadrao", true);
			return null;
		}
	}

	public String acaoCarregarRegistro() {
		T objeto = getRegistroParametro();
		try {
			PBUtil.instance().setNestedProperty(this.getBackBean(), objetoPath,	objeto);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return "";
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
