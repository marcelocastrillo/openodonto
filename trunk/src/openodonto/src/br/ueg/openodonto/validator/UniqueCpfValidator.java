package br.ueg.openodonto.validator;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import br.com.simple.jdbc.Entity;
import br.com.simple.jdbc.EntityManager;
import br.com.simple.jdbc.dao.DaoFactory;
import br.com.simple.jdbc.orm.OrmFormat;
import br.com.simple.jdbc.orm.OrmResolver;
import br.com.simple.jdbc.orm.OrmTranslator;
import br.com.simple.jdbc.sql.CrudQuery;
import br.com.simple.jdbc.sql.IQuery;
import br.ueg.openodonto.dominio.constante.PessoaFisica;
import br.ueg.openodonto.util.WordFormatter;
import br.ueg.openodonto.validator.tipo.ObjectValidatorType;

public class UniqueCpfValidator<T extends Entity & PessoaFisica<T>> extends AbstractValidator implements ObjectValidatorType{	
	
	public UniqueCpfValidator(T value) {
		this(null,value);
	}
	
	public UniqueCpfValidator(CpfValidator next,T value) {
		super(next, value);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T getValue() {
		return (T) super.getValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected boolean validate() {
		Class<T> type = (Class<T>) getValue().getClass();
		String cpf = WordFormatter.clear(getValue().getCpf());
		EntityManager<T> dao = (EntityManager<T>) DaoFactory.getInstance().getDao(type);	
		Map<String,Object> params = new HashMap<String, Object>(1);
		params.put("cpf", cpf);
		List<Field> keyFields = OrmResolver.getKeyFields(new LinkedList<Field>(), type, true);
		String[] fields = new String[keyFields.size()];
		for(int i = 0;i < keyFields.size();i++){
			fields[i] = keyFields.get(i).getName();
		}
		IQuery query = CrudQuery.getSelectQuery(getValue().getClass(), params, fields);
		try {
			List<Map<String,Object>> result = dao.getSqlExecutor().executarUntypedQuery(query);
			if(result.size() == 1){
				OrmFormat format = new OrmFormat(getValue());
				Map<String,Object> already = result.get(0);
				Map<String,Object> local = format.format(fields);
				boolean isSamePf = true;
				OrmTranslator translator = new OrmTranslator(keyFields);				
				for(String field : fields){
					isSamePf = isSamePf && already.get(translator.getColumn(field)).equals(local.get(field));
				}
				if(!isSamePf){
					setErrorMsg("CPF já está sendo usado.");
				}
				return isSamePf;
			}else if(result.size() > 1){
				throw new IllegalStateException("Falha de integridade.Permitiso apenas uma pessoa por CPF.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}	

}
