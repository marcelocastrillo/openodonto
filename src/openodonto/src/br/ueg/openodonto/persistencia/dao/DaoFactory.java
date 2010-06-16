package br.ueg.openodonto.persistencia.dao;

import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.orm.Entity;

public class DaoFactory {

	private static DaoFactory instance;
	
	private DaoFactory(){
		
	}
	
	public static DaoFactory getInstance(){
		if(instance == null){
			instance = new DaoFactory();
		}
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Entity>EntityManager<T> getDao(Class<T> modelo){
		if(modelo.equals(Paciente.class)){
			return (EntityManager<T>)new DaoCrudPaciente();
		}else if(modelo.equals(Telefone.class)){
			return (EntityManager<T>)new DaoCrudTelefone();
		}else if(modelo.equals(Usuario.class)){
			return (EntityManager<T>)new DaoCrudUsuario();
		}else if(modelo.equals(Dentista.class)){
			return (EntityManager<T>)new DaoCrudDentista();
		}else{
			return null;
		}
	}
	
}
