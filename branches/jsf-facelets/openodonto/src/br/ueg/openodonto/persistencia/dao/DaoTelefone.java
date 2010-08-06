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
	DaoBase.getStoredQuerysMap().put("Telefone.findByPessoa",
		"WHERE id_pessoa = ?");
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
	    IQuery query = CrudQuery.getSelectQuery(Telefone.class, orm
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
    public Telefone getNewEntity() {
	return new Telefone();
    }
}
