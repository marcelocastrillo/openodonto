package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import br.ueg.openodonto.dominio.OdontogramaDente;
import br.ueg.openodonto.dominio.OdontogramaDenteProcedimento;
import br.ueg.openodonto.dominio.Procedimento;
import br.ueg.openodonto.persistencia.EntityManager;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.util.OdontogramaDenteComparator;

@Dao(classe=OdontogramaDente.class)
public class DaoOdontogramaDente extends DaoCrud<OdontogramaDente> { // DaoColaborador

	private static final long serialVersionUID = 6651075870920006681L;
	
	public DaoOdontogramaDente() {
		super(OdontogramaDente.class);
	}

	@Override
	public OdontogramaDente getNewEntity() {
		return new OdontogramaDente();
	}
	
	public OdontogramaDente findByKey(Long codigo) throws SQLException{
		OrmFormat orm = new OrmFormat(new OdontogramaDente(codigo));
		return super.findByKey(orm);
	}

	@Override
	protected void afterUpdate(OdontogramaDente o) throws Exception {
		updateRelationship(o);
	}
	
	@Override
	protected void afterInsert(OdontogramaDente o) throws Exception {
		updateRelationship(o);
	}
	
	private void updateRelationship(OdontogramaDente o) throws Exception{
		DaoProcedimento daoProcedimento = (DaoProcedimento) DaoFactory.getInstance().getDao(Procedimento.class);
		daoProcedimento.updateRelationshipOdontogramaDente(o);
	}
	
	@Override
	protected void afterLoad(OdontogramaDente o) throws Exception {
		DaoOdontogramaDenteProcedimento daoODP = (DaoOdontogramaDenteProcedimento) DaoFactory.getInstance().getDao(OdontogramaDenteProcedimento.class);
		List<OdontogramaDenteProcedimento> procedimentos = daoODP.getODPRelationshipOD(o.getCodigo());
		Map<OdontogramaDenteProcedimento,Procedimento> procedimentosMap = new HashMap<OdontogramaDenteProcedimento, Procedimento>();
		DaoProcedimento daoP = (DaoProcedimento) DaoFactory.getInstance().getDao(Procedimento.class);
		for(OdontogramaDenteProcedimento odp : procedimentos){
			procedimentosMap.put(odp, daoP.findByKey(odp.getProcedimentoId()));
		}
		o.setProcedimentosMap(procedimentosMap);
	}
	
	@Override
	protected boolean beforeRemove(OdontogramaDente o,	Map<String, Object> params) throws Exception {
		EntityManager<OdontogramaDenteProcedimento> daoODP = DaoFactory.getInstance().getDao(OdontogramaDenteProcedimento.class);
		for(OdontogramaDenteProcedimento odp : o.getProcedimentosMap().keySet()){
			daoODP.remover(odp);
		}
		return true;
	}
	
	public TreeSet<OdontogramaDente> getRelacionamentoOdontograma(Long idOdontograma) throws Exception{
		OrmFormat orm = new OrmFormat(new OdontogramaDente(idOdontograma,null,null));
		IQuery query = CrudQuery.getSelectQuery(OdontogramaDente.class, orm.formatNotNull(), "*");
		TreeSet<OdontogramaDente> tree = new TreeSet<OdontogramaDente>(new OdontogramaDenteComparator());
		List<OdontogramaDente> loads = getSqlExecutor().executarQuery(query); 
		for(OdontogramaDente o : loads){
			afterLoad(o);
		}
		tree.addAll(loads);
		return tree;
	}

	public void updateRelationshipOdontograma(Collection<OdontogramaDente> ods,Long idOdontograma) throws Exception {
		if(ods != null){
			Collection<OdontogramaDente> todos = getRelacionamentoOdontograma(idOdontograma);
			for (OdontogramaDente od : todos) {
				if (!ods.contains(od)) {
					remover(od);
				}
			}
			for (OdontogramaDente od : ods) {
				od.setIdOdontograma(idOdontograma);
				alterar(od);
			}
		}
	}
	
}
