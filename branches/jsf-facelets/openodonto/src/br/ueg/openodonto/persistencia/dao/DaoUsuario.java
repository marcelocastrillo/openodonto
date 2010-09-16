package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;

import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Usuario.class)
public class DaoUsuario extends DaoCrud<Usuario> {

	public DaoUsuario() {
		super(Usuario.class);
	}

	static {
		initQuerymap();
	}

	private static void initQuerymap() {
		DaoBase.getStoredQuerysMap().put("Usuario.autenticar",	"WHERE user = ? AND senha = ?");
		DaoBase.getStoredQuerysMap().put("Usuario.findByNome",  "WHERE nome LIKE ?");
		DaoBase.getStoredQuerysMap().put("Usuario.findUser",  "WHERE user = ? ");
	}

	private static final long serialVersionUID = 4857838625916905656L;

	@Override
	public Usuario getNewEntity() {
		return new Usuario();
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
