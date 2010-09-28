package br.ueg.openodonto.validator;

public class ValidatorFactory {

	public static Validator newNull(){
		return new NullValidator("");
	}
	
	public static Validator newStrMaxLen(int max, boolean trimmed){
		return new NullValidator(new EmptyValidator(new StringSizeValidator("", max,0),true));
	}
	
	public static Validator newStrMinLen(int min, boolean trimmed){
		return new NullValidator(new EmptyValidator(new StringSizeValidator("", Integer.MAX_VALUE,min),true));
	}
	
	public static Validator newStrRangeLen(int max,int min, boolean trimmed){
		return new NullValidator(new EmptyValidator(new StringSizeValidator("", max,min),true));
	}
	
	public static Validator newStrMask(String mask){
		return newStrMask(mask,Integer.MAX_VALUE);
	}
	
	public static Validator newStrMask(String mask,int max){
		return new NullValidator(new EmptyValidator(new StringSizeValidator(new MaskValidator("", mask), max,0)));
	}
	
	public static Validator newStrEmpty(){
		return new NullValidator(new EmptyValidator(""));
	}
	
	public static Validator newNumSize(Integer max){
		return new NullValidator(new NumberValidator(new NumberSizeValidator(0, max)));
	}
	
	public static Validator newCpf(){
		return newStrMask("^[0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2}");//999.999.999-99
	}
	
	public static Validator newCnpj(){
		return newStrMask("^[0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[/]?[0-9]{4}[-]?[0-9]{2}");//99.999.999/9999-99
	}
	
	public static Validator newEmail(int max){
		return newStrMask("([\\w\\._-])+@([\\w-]+\\.)+[\\w-]+",max);
	}
	
	public static Validator newEmail(){
		return newEmail(Integer.MAX_VALUE);
	}
}
