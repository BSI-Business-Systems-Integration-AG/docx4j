package org.docx4j.jaxb;

import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

/**
 * <h3>Custom Strategy</h3> A custom {@link IJaxbClassLoaderFactory} can be provided by a bundle using the
 * <em>docx4j</em> as host bundle. The custom {@link IJaxbClassLoaderFactory} class must use the
 * following fully qualified class name:
 * <p/>
 * <code>org.docx4j.jaxb.CustomJaxbClassLoaderFactory</code>
 */
public final class Docx4jJaxbUtility {

  private static final Logger LOG;
  private static final IJaxbClassLoaderFactory FACTORY;

  static {
    LOG = Logger.getLogger(Docx4jJaxbUtility.class);
    FACTORY = createJaxbClassLoaderFactory();
  }

  private Docx4jJaxbUtility() {
    // nop
  }

  private static IJaxbClassLoaderFactory createJaxbClassLoaderFactory() {
	  IJaxbClassLoaderFactory factory = null;
      // check whether there is a custom object serializer factory available
      try {
        Class<?> customSerializerFactory = Thread.currentThread().getContextClassLoader().loadClass("org.docx4j.jaxb.CustomJaxbClassLoaderFactory");
        LOG.info("loaded custom object serializer factory: [" + customSerializerFactory + "]");
        if (!IJaxbClassLoaderFactory.class.isAssignableFrom(customSerializerFactory)) {
          LOG.warn("custom object serializer factory is not implementing [" + IJaxbClassLoaderFactory.class + "]");
        }
        else if (Modifier.isAbstract(customSerializerFactory.getModifiers())) {
          LOG.warn("custom object serializer factory is an abstract class [" + customSerializerFactory + "]");
        }
        else {
          factory = (IJaxbClassLoaderFactory) customSerializerFactory.newInstance();
        }
      }
      catch (ClassNotFoundException e) {
        // no custom object serializer factory installed
      }
      catch (Exception e) {
        LOG.warn("Unexpected problem while creating a new instance of custom object serializer factory", e);
      }

      // no custom object serializer factory. Use bundle based serializer factory
      if (factory == null) {
        factory = new BasicJaxbClassLoaderFactory();
      }
    return factory;
  }

  /**
   * @return Returns an environment-dependent {@link ClassLoader} that is able to load all classes that are available in
   *         the running environment.
   */
  public static ClassLoader getClassLoader(Class clazz) {
    return FACTORY.getClassLoader(clazz);
  }
}
