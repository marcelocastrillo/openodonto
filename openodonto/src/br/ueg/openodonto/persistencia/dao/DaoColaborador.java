package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Colaborador;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Colaborador.class)
public class DaoColaborador extends DaoCrud<Colaborador> {

	private static final long serialVersionUID = 5204016786567134806L;
	
	public DaoColaborador() {
		super(Colaborador.class);
	}

	@Override
	public Colaborador getNewEntity() {
		return new Colaborador();
	}

	@Override
	public Colaborador pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<Colaborador> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			Colaborador find = new Colaborador();
			find.setCodigo(id);
			OrmFormat orm = new OrmFormat(find);
			IQuery query = CrudQuery.getSelectQuery(Colaborador.class, orm.formatNotNull(), "*");
			lista = getSqlExecutor().executarQuery(query.getQuery(),query.getParams(), 1);
			if (lista.size() == 1) {
				return lista.get(0);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void afterUpdate(Colaborador o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}

	@Override
	protected void aferInsert(Colaborador o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}
	
	@Override
	public List<Colaborador> listar() {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Colaborador> lista = super.listar();
		if (lista != null) {
			for (Colaborador colaborador : lista) {
				try {
					colaborador.setTelefone(daoTelefone.getTelefonesRelationshipPessoa(colaborador.getCodigo()));
				} catch (Exception ex) {
				}
			}
		}
		return lista;
	}
	
	@Override
	public void afterLoad(Colaborador o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Telefone> telefones = daoTelefone.getTelefonesRelationshipPessoa(o.getCodigo());
		o.setTelefone(telefones);
	}
	
	@Override
	protected boolean beforeRemove(Colaborador o, Map<String, Object> params)throws Exception {
		List<String> referencias = referencedConstraint(Pessoa.class, params);
		if (referencias.contains(CrudQuery.getTableName(Colaborador.class))
				&& referencias.contains(CrudQuery.getTableName(Telefone.class))
				&& referencias.size() == 2) {
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
