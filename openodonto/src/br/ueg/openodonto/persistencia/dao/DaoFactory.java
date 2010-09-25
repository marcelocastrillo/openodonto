package br.ueg.openodonto.persistencia.dao;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.ColaboradorProduto;
import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Produto;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.orm.Entity;

public class DaoFactory {

	private static DaoFactory instance;

	private DaoFactory() {

	}

	public static DaoFactory getInstance() {
		if (instance == null) {
			instance = new DaoFactory();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T extends Entity> EntityManager<T> getDao(Class<T> modelo) {
		if (modelo.equals(Paciente.class)) {
			return (EntityManager<T>) new DaoPaciente();
		} else if (modelo.equals(Telefone.class)) {
			return (EntityManager<T>) new DaoTelefone();
		} else if (modelo.equals(Usuario.class)) {
			return (EntityManager<T>) new DaoUsuario();
		} else if (modelo.equals(Dentista.class)) {
			return (EntityManager<T>) new DaoDentista();
		} else if (modelo.equals(Colaborador.class)) {
			return (EntityManager<T>) new DaoColaborador();
		} else if (modelo.equals(ColaboradorProduto.class)) {
			return (EntityManager<T>) new DaoColaboradorProduto();
		} else if (modelo.equals(Produto.class)) {
			return (EntityManager<T>) new DaoProduto();
		} else if (modelo.equals(Pessoa.class)){
			return (EntityManager<T>)new DaoPessoa();
		} else {
			return null;
		}
	}

}
