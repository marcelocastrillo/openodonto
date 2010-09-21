package br.ueg.openodonto.controle.busca;

import java.lang.reflect.Field;
import java.util.ArrayList;

import br.ueg.openodonto.dominio.Paciente;
import br.ueg.openodonto.persistencia.orm.OrmResolver;
import br.ueg.openodonto.persistencia.orm.OrmTranslator;
import br.ueg.openodonto.servico.busca.FieldFacade;
import br.ueg.openodonto.servico.busca.InputMask;
import br.ueg.openodonto.servico.busca.MessageDisplayer;
import br.ueg.openodonto.validator.Validator;
import br.ueg.openodonto.validator.ValidatorFactory;

public class SearchablePaciente extends AbstractSearchable<Paciente>{

	private static final long serialVersionUID = -946348173920879375L;

	public SearchablePaciente(MessageDisplayer displayer) {
		super(displayer);
	}
	
	public void buildFacade(){
		super.buildFacade();
		OrmTranslator translator = new OrmTranslator(OrmResolver.getAllFields(new ArrayList<Field>(), Paciente.class, true));
		getFacade().add(new FieldFacade("Código",translator.getColumn("codigo")));
		getFacade().add(new FieldFacade("Nome",translator.getColumn("nome")));
		getFacade().add(new FieldFacade("CPF",translator.getColumn("cpf")));
		getFacade().add(new FieldFacade("E-mail",translator.getColumn("email")));
	}
	
	public void buildMask(){
		super.buildMask();
		getMasksMap().put("cpf",new JsMask("mask('999.999.999-99',{placeholder:'_'})","maskCpf","maskCpf"));
	}
	
	public void buildFilter(){
		super.buildFilter();
		buildNameFilter();
		buildEmailFilter();
		buildCpfFilter();
		buildCodigoFilter();
	}
	
	private void buildNameFilter(){
		Validator validator = ValidatorFactory.newStrRangeLen(100,3, true);
		getFiltersMap().put("nomeFilter", buildBasicFilter("nomeFilter","Nome",validator));
	}
	
	private void buildCpfFilter(){
		Validator validator = ValidatorFactory.newCpf();
		InputMask mask = getMasksMap().get("cpf");
		getFiltersMap().put("cpfFilter",buildBasicFilter("cpfFilter","CPF",mask,validator));
	}
	
	private void buildEmailFilter(){
		Validator validator = ValidatorFactory.newEmail();
		getFiltersMap().put("emailFilter", buildBasicFilter("emailFilter","E-mail",validator));
	}
	
	private void buildCodigoFilter(){
		Validator validator = ValidatorFactory.newNumSize(Integer.MAX_VALUE);
		getFiltersMap().put("idFilter",buildBasicFilter("idFilter","Código",validator));
	}

}
