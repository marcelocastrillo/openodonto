package br.ueg.openodonto.servico.busca;

import java.util.List;

import br.com.simple.validator.Validator;

public interface InputField<T>{

	InputMask           getMask();
	List<Validator>     getValidators();
	List<T>             getDomain();
	T                   getValue();
	void                setValue(T value);
	
}
