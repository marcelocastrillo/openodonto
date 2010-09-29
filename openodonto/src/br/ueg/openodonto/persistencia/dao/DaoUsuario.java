package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Usuario.class)
public class DaoUsuario extends DaoCrud<Usuario> {

	private static final long serialVersionUID = 4857838625916905656L;
	
	public DaoUsuario() {
		super(Usuario.class);
	}

	@Override
	public Usuario getNewEntity() {
		return new Usuario();
	}

	@Override
	protected void afterUpdate(Usuario o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}

	@Override
	protected void aferInsert(Usuario o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}

	@Override
	public List<Usuario> listar() {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Usuario> lista = super.listar();
		if (lista != null) {
			for (Usuario dentista : lista) {
				try {
					dentista.setTelefone(daoTelefone.getTelefonesRelationshipPessoa(dentista.getCodigo()));
				} catch (Exception ex) {
				}
			}
		}
		return lista;
	}
	
	@Override
	protected boolean beforeRemove(Usuario o, Map<String, Object> params)throws Exception {
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

	@Override
	public void afterLoad(Usuario o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Telefone> telefones = daoTelefone.getTelefonesRelationshipPessoa(o.getCodigo());
		o.setTelefone(telefones);
	}
	
	@Override
	public Usuario pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<Usuario> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			Usuario find = new Usuario();
			find.setCodigo(id);
			OrmFormat orm = new OrmFormat(find);
			IQuery query = CrudQuery.getSelectQuery(Usuario.class, orm
					.formatNotNull(), "*");
			lista = getSqlExecutor().executarQuery(query.getQuery(),
					query.getParams(), 1);
			if (lista.size() == 1) {
				return lista.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
