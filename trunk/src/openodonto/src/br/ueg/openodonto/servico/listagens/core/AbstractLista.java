package br.ueg.openodonto.servico.listagens.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import br.ueg.openodonto.util.PBUtil;

/**
 * @author vinicius.rodrigues
 * 
 * @param <T>
 */
public abstract class AbstractLista<T> {

    private Map<Long, T> dominioMap;

    protected Class<T> classe;

    private long lastUpdate;

    public AbstractLista(Class<T> classe) {
	this.classe = classe;
	lastUpdate = 0L;
    }

    public abstract List<T> getDominio();

    public Map<String, Long> getLabelMap() {
	if (this.dominioMap == null || isOld())
	    mapDominio();
	Map<String, Long> labels = Collections
		.synchronizedMap(new LinkedHashMap<String, Long>());
	for (Long key : dominioMap.keySet()) {
	    if (getDominioMap().get(key) != null)
		labels.put(getDominioMap().get(key).toString(), key);
	}
	return labels;
    }

    protected synchronized void mapDominio() {
	lastUpdate = System.currentTimeMillis();
	List<T> values = getDominio();
	Map<Long, T> novoDominio = Collections
		.synchronizedMap(new LinkedHashMap<Long, T>());
	for (T value : values)
	    try {
		novoDominio.put((Long) PBUtil.instance().getNestedProperty(value,
			"codigo"), value);
	    } catch (IllegalAccessException e) {
		e.printStackTrace();
	    } catch (InvocationTargetException e) {
		e.printStackTrace();
	    } catch (NoSuchMethodException e) {
		e.printStackTrace();
	    }
	this.dominioMap = novoDominio;
    }

    public synchronized Map<Long, T> getDominioMap() {
	if (this.dominioMap == null)
	    this.dominioMap = Collections
		    .synchronizedMap(new LinkedHashMap<Long, T>());
	return this.dominioMap;
    }

    public boolean isOld() {
	return lastUpdate + 10000L < System.currentTimeMillis();
    }

    public void refreshDominio(ActionEvent evt) {
	mapDominio();
    }

    public Class<T> getClasse() {
	return classe;
    }

}
