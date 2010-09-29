package br.ueg.openodonto.persistencia.dao;

import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;

public abstract class DaoAbstractPessoa<T extends Pessoa> extends DaoCrud<T>{

	private static final long serialVersionUID = -2530942287674194792L;

	public DaoAbstractPessoa(Class<T> classe) {
		super(classe);
	}
	
	@Override
	protected void afterUpdate(T o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}
	
	@Override
	protected void afterInsert(T o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}

	@Override
	public List<T> listar(boolean lazy, String... fields) {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<T> lista = super.listar(lazy, fields);
		if (lista != null && !lazy) {
			for (T pessoa : lista) {
				try {
					pessoa.setTelefone(daoTelefone.getTelefonesRelationshipPessoa(pessoa.getCodigo()));
				} catch (Exception ex) {
				}
			}
		}
		return lista;
	}
	
	@Override
	public void afterLoad(T o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Telefone> telefones = daoTelefone.getTelefonesRelationshipPessoa(o.getCodigo());
		o.setTelefone(telefones);
	}
	
	@Override
	protected boolean beforeRemove(T o, Map<String, Object> params)throws Exception {
		List<String> referencias = referencedConstraint(Pessoa.class, params);
		if (isLastConstraintWithTelefone(referencias)) {
			EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
			for (Telefone telefone : o.getTelefone()) {
				entityManagerTelefone.remover(telefone);
			}
			return false;
		} else {
			return true;
		}
	}

}
