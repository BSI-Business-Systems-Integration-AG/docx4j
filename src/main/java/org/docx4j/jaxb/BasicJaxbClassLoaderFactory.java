package org.docx4j.jaxb;

/**
 * Factory for creating {@link ClassLoader} instances.
 */
public class BasicJaxbClassLoaderFactory implements IJaxbClassLoaderFactory {

  @Override
  public ClassLoader getClassLoader(Class clazz) {
    return clazz.getClassLoader();
  }
}
