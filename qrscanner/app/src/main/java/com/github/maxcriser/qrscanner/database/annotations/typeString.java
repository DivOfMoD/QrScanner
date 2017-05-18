package com.github.maxcriser.qrscanner.database.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface typeString {

<<<<<<< HEAD
    String value() default "TEXT";
=======
    String value() default "DATA";
>>>>>>> FEATURE-merge-maxcriser-account

}