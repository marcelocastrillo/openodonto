package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.OdontogramaDente;
import br.ueg.openodonto.dominio.OdontogramaDenteProcedimento;
import br.ueg.openodonto.dominio.Procedimento;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;

@Dao(classe=Procedimento.class)
public class DaoProcedimento extends DaoCrud<Procedimento>{ //DaoProduto

	private static final long serialVersionUID = 4428579571426531692L;

	public DaoProcedimento() {
		super(Procedimento.class);
	}
	
	@Override
	public Procedimento getNewEntity() {
		return new Procedimento();
	}
	
	public Procedimento findByKey(Long codigo) throws SQLException{
		OrmFormat orm = new OrmFormat(new Procedimento(codigo));
		return super.findByKey(orm);
	}

	public void updateRelationshipOdontogramaDente(OdontogramaDente odontogramaDente) throws Exception{
		Long odontogramaDenteId = odontogramaDente.getCodigo();
		List<Procedimento> procedimentos = new ArrayList<Procedimento>(odontogramaDente.getProcedimentosMap().values());
		if(procedimentos != null){
			DaoOdontogramaDenteProcedimento daoODP = (DaoOdontogramaDenteProcedimento)DaoFactory.getInstance().getDao(OdontogramaDenteProcedimento.class);
			List<OdontogramaDenteProcedimento> odps =  getODPRelationship(new OdontogramaDenteProcedimento(null, odontogramaDenteId));
			for(OdontogramaDenteProcedimento odp : odps){
				if(!containsODPRelationship(procedimentos,odp)){
					daoODP.remover(odp);
				}
			}
			
			
			Iterator<Map.Entry<OdontogramaDenteProcedimento, Procedimento>> iterator = odontogramaDente.getProcedimentosMap().entrySet().iterator();			
			while(iterator.hasNext()){
				Map.Entry<OdontogramaDenteProcedimento, Procedimento> entry = iterator.next();
				Procedimento procedimento = entry.getValue();
				OdontogramaDenteProcedimento odp = entry.getKey(); 
				if(!containsPRelationship(odps,procedimento)){
					odp.setOdontogramaDenteId(odontogramaDenteId);
					odp.setProcedimentoId(procedimento.getCodigo());
					daoODP.inserir(odp);
				}else{
					daoODP.alterar(odp);
				}
			}
		}
	}
	
	private boolean containsPRelationship(List<OdontogramaDenteProcedimento> odps,Procedimento procedimento){
		OdontogramaDenteProcedimento key = new OdontogramaDenteProcedimento(procedimento.getCodigo(),null);
		int index = Collections.binarySearch(odps, key, new PCompatator());
		return index >= 0;
	}
	
	private class PCompatator implements Comparator<OdontogramaDenteProcedimento>{
		@Override
		public int compare(OdontogramaDenteProcedimento o1, OdontogramaDenteProcedimento o2) {
			return o1.getProcedimentoId().compareTo(o2.getProcedimentoId());
		}		
	}
	
	private boolean containsODPRelationship(List<Procedimento> procedimentos,OdontogramaDenteProcedimento odp){
		Procedimento key = new Procedimento(odp.getProcedimentoId());
		int index = Collections.binarySearch(procedimentos, key, new ODPCompatator());
		return index >= 0;
	}
	
	private class ODPCompatator implements Comparator<Procedimento>{
		@Override
		public int compare(Procedimento o1, Procedimento o2) {
			return o1.getCodigo().compareTo(o2.getCodigo());
		}		
	}
	
	public List<OdontogramaDenteProcedimento> getODPRelationship(OdontogramaDenteProcedimento example) throws SQLException{
		OrmFormat format = new OrmFormat(example);
		IQuery sql = CrudQuery.getSelectQuery(OdontogramaDenteProcedimento.class, format.formatNotNull(), "*");
		return DaoFactory.getInstance().getDao(OdontogramaDenteProcedimento.class).getSqlExecutor().executarQuery(sql);
	}
	
	public List<Procedimento> getProcedimentosRelationshipOdontogramaDente(Long odontogramaDenteId)throws SQLException {
		DaoOdontogramaDenteProcedimento dao = (DaoOdontogramaDenteProcedimento) DaoFactory.getInstance().getDao(OdontogramaDenteProcedimento.class);
		return dao.getProcediementos(odontogramaDenteId);
	}	
	
}
