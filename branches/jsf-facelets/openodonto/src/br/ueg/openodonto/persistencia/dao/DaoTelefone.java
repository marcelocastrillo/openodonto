package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

public class DaoTelefone extends DaoCrud<Telefone> {

	private static final long serialVersionUID = -8028356632411640718L;

	public DaoTelefone() {
		super(Telefone.class);
	}

	static {
		initQuerymap();
	}

	private static void initQuerymap() {
		DaoBase.getStoredQuerysMap().put("Telefone.findByPessoa","WHERE id_pessoa = ?");
	}

	public void updateRelationshipPessoa(List<Telefone> telefones,Long idPessoa) throws Exception {
		if(telefones != null){
			List<Telefone> todos = getTelefonesRelationshipPessoa(idPessoa);
			for (Telefone telefone : todos) {
				if (telefones.contains(telefone)) {
					remover(telefone);
				}
			}
			for (Telefone telefone : telefones) {
				telefone.setIdPessoa(idPessoa);
				alterar(telefone);
				getConnection().setAutoCommit(false);
			}
		}
	}
	
	public List<Telefone> getTelefonesRelationshipPessoa(Long idPessoa)throws SQLException {
		OrmFormat orm = new OrmFormat(new Telefone(idPessoa));
		IQuery query = CrudQuery.getSelectQuery(Telefone.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);
	}
	
	@Override
	public Telefone pesquisar(Object key) {
		if (key == null) {
			return null;
		}
		List<Telefone> lista;
		try {
			Long id = Long.parseLong(String.valueOf(key));
			Telefone find = new Telefone();
			find.setCodigo(id);
			OrmFormat orm = new OrmFormat(find);
			IQuery query = CrudQuery.getSelectQuery(Telefone.class, orm.formatNotNull(), "*");
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
	public Telefone getNewEntity() {
		return new Telefone();
	}
}
