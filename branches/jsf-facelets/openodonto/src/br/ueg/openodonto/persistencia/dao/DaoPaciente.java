package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Paciente.class)
public class DaoPaciente extends DaoCrud<Paciente> {

    private static final long serialVersionUID = -4278752127118870714L;


	public DaoPaciente() {
		super(Paciente.class);
	}

	@Override
	public Paciente getNewEntity() {
		return new Paciente();
	}

	@Override
	protected void afterUpdate(Paciente o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}
	
	@Override
	protected void aferInsert(Paciente o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}

	@Override
	public List<Paciente> listar(boolean lazy, String... fields) {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Paciente> lista = super.listar(lazy, fields);
		if (lista != null && !lazy) {
			for (Paciente paciente : lista) {
				try {
					paciente.setTelefone(daoTelefone.getTelefonesRelationshipPessoa(paciente.getCodigo()));
				} catch (Exception ex) {
				}
			}
		}
		return lista;
	}

	@Override
	public void afterLoad(Paciente o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Telefone> telefones = daoTelefone.getTelefonesRelationshipPessoa(o.getCodigo());
		o.setTelefone(telefones);
	}
	
	@Override
	protected boolean beforeRemove(Paciente o, Map<String, Object> params)throws Exception {
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
	public Paciente pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<Paciente> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			OrmFormat orm = new OrmFormat(new Paciente(id));
			IQuery query = CrudQuery.getSelectQuery(Paciente.class, orm	.formatNotNull(), "*");
			lista = getSqlExecutor().executarQuery(query.getQuery(),query.getParams(), 1);
			if (lista.size() == 1) {
				return lista.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
