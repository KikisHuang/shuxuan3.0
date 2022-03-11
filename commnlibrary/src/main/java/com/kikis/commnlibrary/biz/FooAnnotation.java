package com.kikis.commnlibrary.biz;

import com.google.gson.ExclusionStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FooAnnotation {
    // some implementation here
}
