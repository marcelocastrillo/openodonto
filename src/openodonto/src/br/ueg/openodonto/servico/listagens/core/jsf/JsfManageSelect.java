package br.ueg.openodonto.servico.listagens.core.jsf;

import java.util.ArrayList;
import java.util.List;

public class JsfManageSelect{

	private List<Long> jsfVisible;
	
	public JsfManageSelect(){
		this.jsfVisible =  new ArrayList<Long>();
	}

	protected List<Long> getSelecaoMultipla() {
		return this.jsfVisible;
	}

	protected Long getSelecaoSimples() {
		if(!isSelecaoSimples())
			manageSelecaoSimples();
		return this.jsfVisible.get(0);
	}

	protected void setSelecaoMultipla(List<Long> keys) {
		this.jsfVisible = keys;
	}

	protected void setSelecaoSimples(Long key) {
		if(!isSelecaoSimples())
			manageSelecaoSimples();
		this.jsfVisible.set(0, key);
	}
		
	protected void manageSelecaoSimples(){
		this.jsfVisible.clear();
		this.jsfVisible.add(new Long(0L));
	}
	
	protected boolean isSelecaoSimples(){
		return this.jsfVisible.size() == 1;
	}
		
}
