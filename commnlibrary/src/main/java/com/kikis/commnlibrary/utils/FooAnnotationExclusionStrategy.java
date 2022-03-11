package com.kikis.commnlibrary.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.kikis.commnlibrary.biz.FooAnnotation;

// Excludes any field (or class) that is tagged with an "@FooAnnotation"
public  class FooAnnotationExclusionStrategy implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> clazz) {
        return clazz.getAnnotation(FooAnnotation.class) != null;
    }

    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(FooAnnotation.class) != null;
    }
}