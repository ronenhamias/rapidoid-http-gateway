package io.scalecube.serialization;

import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.concurrent.ConcurrentHashMap;

public class SchemaCache {

  static final ConcurrentHashMap<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public static <T> Schema<T> getOrCreate(Class<T> clazz) {
    return (Schema<T>) schemaCache.computeIfAbsent(clazz, RuntimeSchema::createFrom);
  }
}
