package br.ueg.openodonto.persistencia.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MaskResolver implements ResultMask{

	private OrmResolver    ormResolver;
	private List<String>   fields;
	
	public MaskResolver(OrmResolver ormResolver,String... fields){
		this.ormResolver = ormResolver;
		this.fields = Arrays.asList(fields);
	}
	
	@Override
	public List<Field> getResultMask() {
		List<Field> list = ormResolver.getAllFields(new LinkedList<Field>(),ormResolver.getTarget().getClass() ,true);
		return filterFields(list);
	}

	@Override
	public List<Field> getResultMask(Class<?> classe) {
		List<Field> list = ormResolver.getAllFields(new LinkedList<Field>(),classe ,false);
		return filterFields(list);
	}
	
	public List<Field> filterFields(List<Field> list) {
		List<Integer> remove = new ArrayList<Integer>();
		Iterator<Field> iterator = list.iterator();
		while(iterator.hasNext()){
			Field field = iterator.next();
			if(!contains(field)){
				remove.add(list.indexOf(field));
			}
		}
		for(Integer index : remove){
			list.remove(index);
		}
		return list;
	}
	
	private boolean contains(Field field){
		for(String name : fields){
			if(field.getName().equals(name)){
				return true;
			}
		}
		return false;
	}

	public List<String> getFields() {
		return fields;
	}
	
}
