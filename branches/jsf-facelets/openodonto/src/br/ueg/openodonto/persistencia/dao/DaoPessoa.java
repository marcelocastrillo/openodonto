package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Pessoa.class)
public class DaoPessoa extends DaoCrud<Pessoa>{

	private static final long serialVersionUID = -4663199413291562756L;
	
	public DaoPessoa() {
		super(Pessoa.class);
	}
	
	@Override
	public Pessoa getNewEntity() {
		return new Pessoa();
	}

	@Override
	protected void afterUpdate(Pessoa o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}
	
	@Override
	protected void aferInsert(Pessoa o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		daoTelefone.updateRelationshipPessoa(o.getTelefone(), o.getCodigo());
	}

	@Override
	public List<Pessoa> listar(boolean lazy, String... fields) {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Pessoa> lista = super.listar(lazy, fields);
		if (lista != null && !lazy) {
			for (Pessoa pessoa : lista) {
				try {
					pessoa.setTelefone(daoTelefone.getTelefonesRelationshipPessoa(pessoa.getCodigo()));
				} catch (Exception ex) {
				}
			}
		}
		return lista;
	}
	
	@Override
	public void afterLoad(Pessoa o) throws Exception {
		EntityManager<Telefone> entityManagerTelefone = DaoFactory.getInstance().getDao(Telefone.class);
		DaoTelefone daoTelefone = (DaoTelefone) entityManagerTelefone;
		List<Telefone> telefones = daoTelefone.getTelefonesRelationshipPessoa(o.getCodigo());
		o.setTelefone(telefones);
	}
	
	@Override
	public Pessoa pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<Pessoa> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			Pessoa pessoa = new Pessoa();
			pessoa.setCodigo(id);
			OrmFormat orm = new OrmFormat(pessoa);
			IQuery query = CrudQuery.getSelectQuery(Pessoa.class, orm.formatNotNull(), "*");
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
