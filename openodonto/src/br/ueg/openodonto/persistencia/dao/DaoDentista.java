package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

public class DaoDentista extends DaoCrud<Dentista> {

	private static final long serialVersionUID = 7872660758710684668L;

	public DaoDentista() {
		super(Dentista.class);
	}

	static {
		initQuerymap();
	}

	private static void initQuerymap() {
		DaoBase.getStoredQuerysMap().put("Dentista.findByNome",	"WHERE nome LIKE ?");
		DaoBase.getStoredQuerysMap().put("Dentista.findByCro", "WHERE cro = ?");
		DaoBase.getStoredQuerysMap().put("Dentista.findEspecialidade", "WHERE especialidade like ?");
	}

	@Override
	public Dentista getNewEntity() {
		return new Dentista();
	}

	private void updateRelationshipTelefone(Dentista o) throws Exception {
		if (o.getTelefone() != null) {
			EntityManager<Telefone> entityManagerTelefone = DaoFactory
					.getInstance().getDao(Telefone.class);
			List<Telefone> todos = getTelefonesFromDentista(o.getCodigo());
			for (Telefone telefone : todos) {
				if (!o.getTelefone().contains(telefone)) {
					entityManagerTelefone.remover(telefone);
				}
			}
			for (Telefone telefone : o.getTelefone()) {
				telefone.setIdPessoa(o.getCodigo());
				entityManagerTelefone.alterar(telefone);
				getConnection().setAutoCommit(false);
			}
		}
	}

	private List<Telefone> getTelefonesFromDentista(Long id)
			throws SQLException {
		EntityManager<Telefone> emTelefone = DaoFactory.getInstance().getDao(
				Telefone.class);
		OrmFormat orm = new OrmFormat(new Telefone(id));
		IQuery query = CrudQuery.getSelectQuery(Telefone.class, orm
				.formatNotNull(), "*");
		return emTelefone.getSqlExecutor().executarQuery(query);
	}

	@Override
	protected void afterUpdate(Dentista o) throws Exception {
		updateRelationshipTelefone(o);
	}

	@Override
	protected void aferInsert(Dentista o) throws Exception {
		updateRelationshipTelefone(o);
	}

	@Override
	public List<Dentista> listar() {
		List<Dentista> lista = super.listar();
		if (lista != null) {
			for (Dentista dentista : lista) {
				try {
					dentista.setTelefone(getTelefonesFromDentista(dentista
							.getCodigo()));
				} catch (Exception ex) {
				}
			}
		}
		return lista;
	}

	@Override
	public void afterLoad(Dentista o) throws Exception {
		List<Telefone> telefones = getTelefonesFromDentista(o.getCodigo());
		o.setTelefone(telefones);
	}

	@Override
	public Dentista pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<Dentista> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			Dentista find = new Dentista();
			find.setCodigo(id);
			OrmFormat orm = new OrmFormat(find);
			IQuery query = CrudQuery.getSelectQuery(Dentista.class, orm
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

	@Override
	protected boolean beforeRemove(Dentista o, Map<String, Object> params)
			throws Exception {
		List<String> referencias = referencedConstraint(Pessoa.class, params);
		if (referencias.contains(CrudQuery.getTableName(Dentista.class))
				&& referencias.contains(CrudQuery.getTableName(Telefone.class))
				&& referencias.size() == 2) {
			EntityManager<Telefone> entityManagerTelefone = DaoFactory
					.getInstance().getDao(Telefone.class);
			for (Telefone telefone : o.getTelefone()) {
				entityManagerTelefone.remover(telefone);
			}
			return false;
		} else {
			return true;
		}
	}

}
