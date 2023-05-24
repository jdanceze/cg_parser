package com.sun.xml.bind.v2;

import com.sun.istack.FinalArrayList;
import com.sun.xml.bind.Util;
import com.sun.xml.bind.api.JAXBRIContext;
import com.sun.xml.bind.api.TypeReference;
import com.sun.xml.bind.v2.model.annotation.RuntimeAnnotationReader;
import com.sun.xml.bind.v2.runtime.JAXBContextImpl;
import com.sun.xml.bind.v2.util.TypeCast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/ContextFactory.class */
public class ContextFactory {
    public static final String USE_JAXB_PROPERTIES = "_useJAXBProperties";

    /* JADX WARN: Multi-variable type inference failed */
    public static JAXBContext createContext(Class[] classes, Map<String, Object> properties) throws JAXBException {
        Map<String, Object> properties2;
        if (properties == null) {
            properties2 = Collections.emptyMap();
        } else {
            properties2 = new HashMap<>(properties);
        }
        String defaultNsUri = (String) getPropertyValue(properties2, JAXBRIContext.DEFAULT_NAMESPACE_REMAP, String.class);
        Boolean c14nSupport = (Boolean) getPropertyValue(properties2, JAXBRIContext.CANONICALIZATION_SUPPORT, Boolean.class);
        if (c14nSupport == null) {
            c14nSupport = false;
        }
        Boolean disablesecurityProcessing = (Boolean) getPropertyValue(properties2, JAXBRIContext.DISABLE_XML_SECURITY, Boolean.class);
        if (disablesecurityProcessing == null) {
            disablesecurityProcessing = false;
        }
        Boolean allNillable = (Boolean) getPropertyValue(properties2, JAXBRIContext.TREAT_EVERYTHING_NILLABLE, Boolean.class);
        if (allNillable == null) {
            allNillable = false;
        }
        Boolean retainPropertyInfo = (Boolean) getPropertyValue(properties2, JAXBRIContext.RETAIN_REFERENCE_TO_INFO, Boolean.class);
        if (retainPropertyInfo == null) {
            retainPropertyInfo = false;
        }
        Boolean supressAccessorWarnings = (Boolean) getPropertyValue(properties2, JAXBRIContext.SUPRESS_ACCESSOR_WARNINGS, Boolean.class);
        if (supressAccessorWarnings == null) {
            supressAccessorWarnings = false;
        }
        Boolean improvedXsiTypeHandling = (Boolean) getPropertyValue(properties2, JAXBRIContext.IMPROVED_XSI_TYPE_HANDLING, Boolean.class);
        if (improvedXsiTypeHandling == null) {
            String improvedXsiSystemProperty = Util.getSystemProperty(JAXBRIContext.IMPROVED_XSI_TYPE_HANDLING);
            if (improvedXsiSystemProperty == null) {
                improvedXsiTypeHandling = true;
            } else {
                improvedXsiTypeHandling = Boolean.valueOf(improvedXsiSystemProperty);
            }
        }
        Boolean xmlAccessorFactorySupport = (Boolean) getPropertyValue(properties2, JAXBRIContext.XMLACCESSORFACTORY_SUPPORT, Boolean.class);
        if (xmlAccessorFactorySupport == null) {
            xmlAccessorFactorySupport = false;
            Util.getClassLogger().log(Level.FINE, "Property com.sun.xml.bind.XmlAccessorFactoryis not active.  Using JAXB's implementation");
        }
        Boolean backupWithParentNamespace = (Boolean) getPropertyValue(properties2, JAXBRIContext.BACKUP_WITH_PARENT_NAMESPACE, Boolean.class);
        RuntimeAnnotationReader ar = (RuntimeAnnotationReader) getPropertyValue(properties2, JAXBRIContext.ANNOTATION_READER, RuntimeAnnotationReader.class);
        Collection<TypeReference> tr = (Collection) getPropertyValue(properties2, JAXBRIContext.TYPE_REFERENCES, Collection.class);
        Collection<TypeReference> tr2 = tr;
        if (tr == null) {
            tr2 = Collections.emptyList();
        }
        try {
            Map<Class, Class> subclassReplacements = TypeCast.checkedCast((Map) getPropertyValue(properties2, JAXBRIContext.SUBCLASS_REPLACEMENTS, Map.class), Class.class, Class.class);
            if (!properties2.isEmpty()) {
                throw new JAXBException(Messages.UNSUPPORTED_PROPERTY.format(properties2.keySet().iterator().next()));
            }
            JAXBContextImpl.JAXBContextBuilder builder = new JAXBContextImpl.JAXBContextBuilder();
            builder.setClasses(classes);
            builder.setTypeRefs(tr2);
            builder.setSubclassReplacements(subclassReplacements);
            builder.setDefaultNsUri(defaultNsUri);
            builder.setC14NSupport(c14nSupport.booleanValue());
            builder.setAnnotationReader(ar);
            builder.setXmlAccessorFactorySupport(xmlAccessorFactorySupport.booleanValue());
            builder.setAllNillable(allNillable.booleanValue());
            builder.setRetainPropertyInfo(retainPropertyInfo.booleanValue());
            builder.setSupressAccessorWarnings(supressAccessorWarnings.booleanValue());
            builder.setImprovedXsiTypeHandling(improvedXsiTypeHandling.booleanValue());
            builder.setDisableSecurityProcessing(disablesecurityProcessing.booleanValue());
            builder.setBackupWithParentNamespace(backupWithParentNamespace);
            return builder.build();
        } catch (ClassCastException e) {
            throw new JAXBException(Messages.INVALID_TYPE_IN_MAP.format(new Object[0]), e);
        }
    }

    private static <T> T getPropertyValue(Map<String, Object> properties, String keyName, Class<T> type) throws JAXBException {
        Object o = properties.get(keyName);
        if (o == null) {
            return null;
        }
        properties.remove(keyName);
        if (!type.isInstance(o)) {
            throw new JAXBException(Messages.INVALID_PROPERTY_VALUE.format(keyName, o));
        }
        return type.cast(o);
    }

    @Deprecated
    public static JAXBRIContext createContext(Class[] classes, Collection<TypeReference> typeRefs, Map<Class, Class> subclassReplacements, String defaultNsUri, boolean c14nSupport, RuntimeAnnotationReader ar, boolean xmlAccessorFactorySupport, boolean allNillable, boolean retainPropertyInfo) throws JAXBException {
        return createContext(classes, typeRefs, subclassReplacements, defaultNsUri, c14nSupport, ar, xmlAccessorFactorySupport, allNillable, retainPropertyInfo, false);
    }

    @Deprecated
    public static JAXBRIContext createContext(Class[] classes, Collection<TypeReference> typeRefs, Map<Class, Class> subclassReplacements, String defaultNsUri, boolean c14nSupport, RuntimeAnnotationReader ar, boolean xmlAccessorFactorySupport, boolean allNillable, boolean retainPropertyInfo, boolean improvedXsiTypeHandling) throws JAXBException {
        JAXBContextImpl.JAXBContextBuilder builder = new JAXBContextImpl.JAXBContextBuilder();
        builder.setClasses(classes);
        builder.setTypeRefs(typeRefs);
        builder.setSubclassReplacements(subclassReplacements);
        builder.setDefaultNsUri(defaultNsUri);
        builder.setC14NSupport(c14nSupport);
        builder.setAnnotationReader(ar);
        builder.setXmlAccessorFactorySupport(xmlAccessorFactorySupport);
        builder.setAllNillable(allNillable);
        builder.setRetainPropertyInfo(retainPropertyInfo);
        builder.setImprovedXsiTypeHandling(improvedXsiTypeHandling);
        return builder.build();
    }

    public static JAXBContext createContext(String contextPath, ClassLoader classLoader, Map<String, Object> properties) throws JAXBException {
        FinalArrayList<Class> classes = new FinalArrayList<>();
        StringTokenizer tokens = new StringTokenizer(contextPath, ":");
        while (tokens.hasMoreTokens()) {
            boolean foundJaxbIndex = false;
            boolean foundObjectFactory = false;
            String pkg = tokens.nextToken();
            try {
                Class<?> o = classLoader.loadClass(pkg + ".ObjectFactory");
                classes.add(o);
                foundObjectFactory = true;
            } catch (ClassNotFoundException e) {
            }
            try {
                List<Class> indexedClasses = loadIndexedClasses(pkg, classLoader);
                if (indexedClasses != null) {
                    classes.addAll(indexedClasses);
                    foundJaxbIndex = true;
                }
                if (!foundObjectFactory && !foundJaxbIndex) {
                    throw new JAXBException(Messages.BROKEN_CONTEXTPATH.format(pkg));
                }
            } catch (IOException e2) {
                throw new JAXBException(e2);
            }
        }
        return createContext((Class[]) classes.toArray(new Class[classes.size()]), properties);
    }

    private static List<Class> loadIndexedClasses(String pkg, ClassLoader classLoader) throws IOException, JAXBException {
        String resource = pkg.replace('.', '/') + "/jaxb.index";
        InputStream resourceAsStream = classLoader.getResourceAsStream(resource);
        if (resourceAsStream == null) {
            return null;
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
        try {
            FinalArrayList<Class> classes = new FinalArrayList<>();
            String className = in.readLine();
            while (className != null) {
                String className2 = className.trim();
                if (className2.startsWith("#") || className2.length() == 0) {
                    className = in.readLine();
                } else if (className2.endsWith(".class")) {
                    throw new JAXBException(Messages.ILLEGAL_ENTRY.format(className2));
                } else {
                    try {
                        classes.add(classLoader.loadClass(pkg + '.' + className2));
                        className = in.readLine();
                    } catch (ClassNotFoundException e) {
                        throw new JAXBException(Messages.ERROR_LOADING_CLASS.format(className2, resource), e);
                    }
                }
            }
            return classes;
        } finally {
            in.close();
        }
    }
}
