package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Usuario;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.dao.sql.QueryExecutor;
import br.ueg.openodonto.persistencia.dao.sql.SqlExecutor;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

public class DaoUsuario extends DaoBase<Usuario> {

    private SqlExecutor<Usuario> sqlExecutor;

    public DaoUsuario() {
	super(Usuario.class);
	this.sqlExecutor = new QueryExecutor<Usuario>(this);
    }

    static {
	initQuerymap();
    }

    private static void initQuerymap() {
	DaoBase.getStoredQuerysMap().put("Usuario.autenticar",
		"WHERE user = ? AND senha = ?");
    }

    private static final long serialVersionUID = 4857838625916905656L;

    @Override
    public Usuario getNewEntity() {
	return new Usuario();
    }

    @Override
    public void alterar(Usuario o) throws Exception {
	if (o != null && o.getCodigo() != null
		&& pesquisar(o.getCodigo()) != null) {
	    Savepoint save = null;
	    try {
		if (o == null) {
		    return;
		}
		getConnection().setAutoCommit(false);
		save = getConnection().setSavepoint(
			"Before Update Usuario - Savepoint");
		OrmFormat orm = new OrmFormat(o);
		update(o, orm.formatKey());
	    } catch (Exception ex) {
		ex.printStackTrace();
		if (save != null) {
		    getConnection().rollback(save);
		}
		throw ex;
	    }
	    getConnection().setAutoCommit(true);
	} else if (o != null) {
	    inserir(o);
	}
    }

    @Override
    public SqlExecutor<Usuario> getSqlExecutor() {
	return sqlExecutor;
    }

    @Override
    public void inserir(Usuario o) throws Exception {
	if (o != null && o.getCodigo() != null
		&& pesquisar(o.getCodigo()) != null) {
	    alterar(o);
	} else if (o != null) {
	    Savepoint save = null;
	    try {
		if (o == null) {
		    return;
		}
		getConnection().setAutoCommit(false);
		save = getConnection().setSavepoint(
			"Before Insert Usuario - Savepoint");
		insert(o);
	    } catch (Exception ex) {
		ex.printStackTrace();
		if (save != null) {
		    getConnection().rollback(save);
		}
		throw ex;
	    }
	    getConnection().setAutoCommit(true);
	}
    }

    @Override
    public List<Usuario> listar() {
	List<Usuario> usuarios = null;
	try {
	    usuarios = listar("*");
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return usuarios;
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

    @Override
    public void remover(Usuario o) throws Exception {
	Savepoint save = null;
	try {
	    save = getConnection().setSavepoint(
		    "Before Remove Usuarios - Savepoint");
	    OrmFormat orm = new OrmFormat(o);
	    Map<String, Object> params = orm.formatKey();
	    remove(params, false);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    if (save != null) {
		getConnection().rollback(save);
	    }
	    throw ex;
	}
	getConnection().setAutoCommit(true);
    }

    @Override
    public void load(Usuario o) {
	if (o == null || o.getCodigo() == null) {
	    throw new RuntimeException("Usuario Inválido");
	}
	OrmFormat orm = new OrmFormat(o);
	Usuario loaded = pesquisar(o.getCodigo());
	OrmFormat ormLoaded = new OrmFormat(loaded);
	orm.parse(ormLoaded.format());
    }

}
