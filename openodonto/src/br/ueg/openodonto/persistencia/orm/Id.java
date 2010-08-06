package br.ueg.openodonto.persistencia.orm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.ueg.openodonto.persistencia.orm.value.IdIncrementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Id {

    IdIncrementType autoIncrement() default IdIncrementType.NONE;

}
