package br.ueg.openodonto.persistencia.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.orm.Entity;

public class DaoFactory {

	private static DaoFactory instance;
	private Map<Class<? extends Entity>,Class<EntityManager<? extends Entity>>> daoMap;

	private DaoFactory() {
		daoMap = new HashMap<Class<? extends Entity>, Class<EntityManager<? extends Entity>>>();
	}

	public static DaoFactory getInstance() {
		if (instance == null) {
			instance = new DaoFactory();
		}
		return instance;
	}

	public <T extends Entity>void registerDao(Class<T> modelo,Class<EntityManager<? extends Entity>> entityManage){
		daoMap.put(modelo, entityManage);
		System.out.println(new Date() + " [DAO] Registrado - " + modelo.getName());
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity> EntityManager<T> getDao(Class<T> modelo) {
		Class<EntityManager<? extends Entity>> entityManager;
		if((entityManager = daoMap.get(modelo)) != null){
			try {
				return (EntityManager<T>)entityManager.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return null;
		}
	}

}
