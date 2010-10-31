package br.ueg.openodonto.persistencia.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.ueg.openodonto.dominio.OdontogramaDente;
import br.ueg.openodonto.dominio.OdontogramaDenteProcedimento;
import br.ueg.openodonto.dominio.Procedimento;
import br.ueg.openodonto.persistencia.dao.sql.CrudQuery;
import br.ueg.openodonto.persistencia.dao.sql.IQuery;
import br.ueg.openodonto.persistencia.orm.Dao;
import br.ueg.openodonto.persistencia.orm.OrmFormat;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;

@Dao(classe=OdontogramaDenteProcedimento.class)
public class DaoOdontogramaDenteProcedimento extends DaoCrud<OdontogramaDenteProcedimento>{//DaoColaboradorProduto
	
	private static final long serialVersionUID = -5700425577933939399L;

	public DaoOdontogramaDenteProcedimento() {
		super(OdontogramaDenteProcedimento.class);
	}
	
	@Override
	public OdontogramaDenteProcedimento getNewEntity() {
		return new OdontogramaDenteProcedimento();
	}
	
	public List<Procedimento> getProcediementos(OdontogramaDente odontogramaDente,Procedimento procedimento) throws SQLException{
		return getRelacionamento(odontogramaDente, procedimento, "odontogramaDenteId","procedimentoId");
	}
	
	public List<OdontogramaDente> getOdontogramaDente(Procedimento procedimento,OdontogramaDente odontogramaDente) throws SQLException{
		return getRelacionamento(procedimento,odontogramaDente,"procedimentoId","odontogramaDenteId");
	}

	public List<Procedimento> getProcediementos(Long odontogramaDenteId) throws SQLException{
		Map<String,Object> whereParams = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		whereParams.put(translator.getColumn("odontogramaDenteId"), odontogramaDenteId);
		return getRelacionamento(Procedimento.class,"procedimentoId",whereParams);
	}
	
	public List<OdontogramaDente> getOdontogramaDente(Long procedimentoId) throws SQLException{
		Map<String,Object> whereParams = new HashMap<String, Object>();
		OrmTranslator translator = new OrmTranslator(fields);
		whereParams.put(translator.getColumn("procedimentoId"), procedimentoId);
		return getRelacionamento(OdontogramaDente.class,"odontogramaDenteId",whereParams);		
	}
	
	public List<OdontogramaDenteProcedimento> getOdontogramaDenteProcedimentosRelationshipOdontogramaDente(Long odontogramaDenteId) throws SQLException{
		OrmFormat orm = new OrmFormat(new OdontogramaDenteProcedimento(null, odontogramaDenteId));
		IQuery query = CrudQuery.getSelectQuery(OdontogramaDenteProcedimento.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);		
	}
	
	public List<OdontogramaDenteProcedimento> getOdontogramaDenteProcedimentosRelationshipProcedimento(Long procedimentoId) throws SQLException{
		OrmFormat orm = new OrmFormat(new OdontogramaDenteProcedimento(procedimentoId, null));
		IQuery query = CrudQuery.getSelectQuery(OdontogramaDenteProcedimento.class, orm.formatNotNull(), "*");
		return getSqlExecutor().executarQuery(query);
	}
	
}
