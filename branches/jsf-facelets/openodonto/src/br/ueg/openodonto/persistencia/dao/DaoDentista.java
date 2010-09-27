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
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Dentista.class)
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

	@Override
	protected void afterUpdate(Dentista o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}

	@Override
	protected void aferInsert(Dentista o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}

	@Override
	public List<Dentista> listar() {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Dentista> lista = super.listar();
		if (lista != null) {
			for (Dentista dentista : lista) {
				try {
					dentista.setTelefone(daoTelefone.getTelefonesRelationshipPessoa(dentista.getCodigo()));
				} catch (Exception ex) {
				}
			}
		}
		return lista;
	}

	@Override
	public void afterLoad(Dentista o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Telefone> telefones = daoTelefone.getTelefonesRelationshipPessoa(o.getCodigo());
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
	protected boolean beforeRemove(Dentista o, Map<String, Object> params)throws Exception {
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
