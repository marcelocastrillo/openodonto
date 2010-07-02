package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.Dentista;
import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.dominio.Pessoa;
import br.ueg.openodonto.dominio.Telefone;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@SuppressWarnings("serial")
public class DaoPaciente extends DaoCrud<Paciente> {

    static {
	initQueryMap();
    }

    public static void initQueryMap() {
	DaoBase.getStoredQuerysMap().put("Paciente.BuscaByNome",
		"WHERE nome LIKE ?");
	DaoBase.getStoredQuerysMap()
		.put("Paciente.BuscaByCPF", "WHERE cpf = ?");
	DaoBase.getStoredQuerysMap().put("Paciente.BuscaByEmail",
		"WHERE email = ?");
    }

    public DaoPaciente() {
	super(Paciente.class);
    }

    @Override
    public Paciente getNewEntity() {
	return new Paciente();
    }

    private void updateRelationshipTelefone(Paciente o) throws Exception {
	if (o.getTelefone() != null) {
	    EntityManager<Telefone> entityManagerTelefone = DaoFactory
		    .getInstance().getDao(Telefone.class);
	    List<Telefone> todos = getTelefonesFromPaciente(o.getCodigo());
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

    private List<Telefone> getTelefonesFromPaciente(Long id)
	    throws SQLException {
	EntityManager<Telefone> emTelefone = DaoFactory.getInstance().getDao(
		Telefone.class);
	OrmFormat orm = new OrmFormat(new Telefone(id));
	IQuery query = CrudQuery.getSelectQuery(Telefone.class, orm
		.formatNotNull(), "*");
	return emTelefone.getSqlExecutor().executarQuery(query);
    }

    @Override
    protected void afterUpdate(Paciente o) throws Exception {
	updateRelationshipTelefone(o);
    }

    @Override
    protected void aferInsert(Paciente o) throws Exception {
	updateRelationshipTelefone(o);
    }

    @Override
    public List<Paciente> listar() {
	List<Paciente> lista = super.listar();
	if (lista != null) {
	    for (Paciente paciente : lista) {
		try {
		    paciente.setTelefone(getTelefonesFromPaciente(paciente
			    .getCodigo()));
		} catch (Exception ex) {
		}
	    }
	}
	return lista;
    }
    
    @Override
    public void afterLoad(Paciente o) throws Exception {
	List<Telefone> telefones = getTelefonesFromPaciente(o.getCodigo());
	o.setTelefone(telefones);
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
	    IQuery query = CrudQuery.getSelectQuery(Paciente.class, orm
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
    protected boolean beforeRemove(Paciente o, Map<String, Object> params)
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
