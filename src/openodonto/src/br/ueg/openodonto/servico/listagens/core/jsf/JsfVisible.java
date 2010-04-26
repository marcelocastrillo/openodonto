package br.ueg.openodonto.servico.listagens.core.jsf;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ueg.openodonto.servico.listagens.core.AbstractLista;
import br.ueg.openodonto.servico.listagens.core.codec.Encoder;
import br.ueg.openodonto.servico.listagens.core.codec.PropriedadeRefletida;
import br.ueg.openodonto.servico.listagens.core.codec.PropriedadeRefletida.Way;

public class JsfVisible<T> extends JsfManageSelect{
	
	private Class<T> classe;

	private Object manageBean;
	
	private String beanPath;

	private AbstractLista<T> contraDominio;	
	
	public <M> JsfVisible(Class<T> classe,M manageBean,String beanPath){
		this.classe = classe;
		this.manageBean = manageBean;
		this.beanPath = beanPath;
	}
	
	public <M> JsfVisible(Class<T> classe,M manageBean,String beanPath,AbstractLista<T> contraDominio){
		this(classe , manageBean , beanPath);
		this.contraDominio = contraDominio;
	}
	
	private Long encodeSing(){		
		return Encoder.encode(classe,getTsimples());
	}
	
	private List<Long> encodeMult(){
		return Encoder.encode(classe,getTmultiplo());
	}
	
	private T decodeSing(Long key){
		return contraDominio == null ? Encoder.decode(classe,key) : Encoder.decode(classe,key,contraDominio);
	}
	
	private List<T> decodeMult(List<Long> keys){
		return contraDominio == null ? Encoder.decode(classe,keys) : Encoder.decode(classe,keys,contraDominio);
	}	

	public List<Long> getSelecaoMultipla() {
		return encodeMult();
	}

	public Long getSelecaoSimples() {
		return encodeSing();
	}

	public void setSelecaoMultipla(List<Long> keys) {
		setTmultiplo(decodeMult(keys));		
	}

	public void setSelecaoSimples(Long key) {
		setTsimples(decodeSing(key));
	}
	
	@SuppressWarnings("unchecked")
	private T getTsimples(){
		T result = null;
		try {
			result = (T)getReflectAcess(Way.GET).invoke(getParent(), new Object[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private List<T> getTmultiplo(){
		List<T> result = null;
		try {
			result = (List<T>)getReflectAcess(Way.GET).invoke(getParent(), new Object[0]);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch(NullPointerException e) {
			return new ArrayList<T>();
		}
		return result;
	}
	
	private void setTsimples(T t){
		try {
			getReflectAcess(Way.SET).invoke(getParent(), t);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	private void setTmultiplo(List<T> t){
		try {
			getReflectAcess(Way.SET).invoke(getParent(), t);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}	
	
	private Method getReflectAcess(Way way){
		String soon = beanPath.substring(beanPath.lastIndexOf(".")+1);
		if(way.equals(Way.GET))
			return PropriedadeRefletida.pojoGetterMaker(getParent().getClass(), soon);
		else if(way.equals(Way.SET))
			return PropriedadeRefletida.pojoSetterMaker(getParent().getClass(), soon);
		else 
			return null;
	}
	
	@SuppressWarnings("unchecked")
	private <X> X getParent() {
		Class<?> classX = this.manageBean.getClass();
		X o = (X)this.manageBean;
		List<String> parents = Arrays.asList(beanPath.split("[.]"));
		for(int i = 0 ;i < parents.size()-1;i++){
			Method m = PropriedadeRefletida.pojoGetterMaker(classX, parents.get(i));
			try {
				o = (X)m.invoke(o, new Object[0]);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if(o == null)
				return null;
			classX = o.getClass();
		}	
		return o;
	}

	
	public AbstractLista<T> getDominio() {
		if(contraDominio == null)
			return null;
		if(contraDominio.isOld())
			contraDominio.refreshDominio(null);
		return contraDominio;
	}
	
	public void setDominio(AbstractLista<T> dominio) {	
		this.contraDominio = dominio;
	}
	
	
	
}
