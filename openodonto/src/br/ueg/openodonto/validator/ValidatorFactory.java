package br.ueg.openodonto.validator;

public class ValidatorFactory {

	public static Validator newStrMaxLen(int size, boolean trimmed){
		return new NullValidator(new EmptyValidator(new SizeValidator("", size),true));
	}
	
	public static Validator newNumSize(Integer max){
		return new NullValidator(new NumberValidator(new NumberSizeValidator(0, max)));
	}
	
	public static Validator newStrMask(String mask){
		return new NullValidator(new EmptyValidator(new MaskValidator("", mask)));
	}
	
	public static Validator newCpf(){
		return newStrMask("^[0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2}");
	}
	
	public static Validator newEmail(){
		return newStrMask("([\\w\\._-])+@([\\w-]+\\.)+[\\w-]+");
	}

}
