package org.apache.tools.ant;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.tools.ant.taskdefs.PreSetDef;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.resources.FileProvider;
import org.apache.tools.ant.types.resources.FileResource;
import org.apache.tools.ant.util.StringUtils;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/IntrospectionHelper.class */
public final class IntrospectionHelper {
    private static final Map<String, IntrospectionHelper> HELPERS = new Hashtable();
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP = new HashMap(8);
    private static final int MAX_REPORT_NESTED_TEXT = 20;
    private static final String ELLIPSIS = "...";
    private final Map<String, Class<?>> attributeTypes = new Hashtable();
    private final Map<String, AttributeSetter> attributeSetters = new Hashtable();
    private final Map<String, Class<?>> nestedTypes = new Hashtable();
    private final Map<String, NestedCreator> nestedCreators = new Hashtable();
    private final List<Method> addTypeMethods = new ArrayList();
    private final Method addText;
    private final Class<?> bean;
    protected static final String NOT_SUPPORTED_CHILD_PREFIX = " doesn't support the nested \"";
    protected static final String NOT_SUPPORTED_CHILD_POSTFIX = "\" element.";

    static {
        Class<?>[] primitives = {Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};
        Class<?>[] wrappers = {Boolean.class, Byte.class, Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class};
        for (int i = 0; i < primitives.length; i++) {
            PRIMITIVE_TYPE_MAP.put(primitives[i], wrappers[i]);
        }
    }

    private IntrospectionHelper(Class<?> bean) {
        Method[] methods;
        Constructor<?> constructor;
        Constructor<?> constructor2;
        AttributeSetter as;
        this.bean = bean;
        Method addTextMethod = null;
        for (Method m : bean.getMethods()) {
            String name = m.getName();
            Class<?> returnType = m.getReturnType();
            Class<?>[] args = m.getParameterTypes();
            if (args.length == 1 && Void.TYPE.equals(returnType) && ("add".equals(name) || "addConfigured".equals(name))) {
                insertAddTypeMethod(m);
            } else if ((!ProjectComponent.class.isAssignableFrom(bean) || args.length != 1 || !isHiddenSetMethod(name, args[0])) && (!isContainer() || args.length != 1 || !"addTask".equals(name) || !Task.class.equals(args[0]))) {
                if ("addText".equals(name) && Void.TYPE.equals(returnType) && args.length == 1 && String.class.equals(args[0])) {
                    addTextMethod = m;
                } else if (name.startsWith("set") && Void.TYPE.equals(returnType) && args.length == 1 && !args[0].isArray()) {
                    String propName = getPropertyName(name, "set");
                    AttributeSetter as2 = this.attributeSetters.get(propName);
                    if ((as2 == null || (!String.class.equals(args[0]) && (!File.class.equals(args[0]) || (!Resource.class.equals(as2.type) && !FileProvider.class.equals(as2.type))))) && (as = createAttributeSetter(m, args[0], propName)) != null) {
                        this.attributeTypes.put(propName, args[0]);
                        this.attributeSetters.put(propName, as);
                    }
                } else if (name.startsWith("create") && !returnType.isArray() && !returnType.isPrimitive() && args.length == 0) {
                    String propName2 = getPropertyName(name, "create");
                    if (this.nestedCreators.get(propName2) == null) {
                        this.nestedTypes.put(propName2, returnType);
                        this.nestedCreators.put(propName2, new CreateNestedCreator(m));
                    }
                } else if (name.startsWith("addConfigured") && Void.TYPE.equals(returnType) && args.length == 1 && !String.class.equals(args[0]) && !args[0].isArray() && !args[0].isPrimitive()) {
                    try {
                        try {
                            constructor2 = args[0].getConstructor(new Class[0]);
                        } catch (NoSuchMethodException e) {
                        }
                    } catch (NoSuchMethodException e2) {
                        constructor2 = args[0].getConstructor(Project.class);
                    }
                    String propName3 = getPropertyName(name, "addConfigured");
                    this.nestedTypes.put(propName3, args[0]);
                    this.nestedCreators.put(propName3, new AddNestedCreator(m, constructor2, 2));
                } else if (name.startsWith("add") && Void.TYPE.equals(returnType) && args.length == 1 && !String.class.equals(args[0]) && !args[0].isArray() && !args[0].isPrimitive()) {
                    try {
                        try {
                            constructor = args[0].getConstructor(new Class[0]);
                        } catch (NoSuchMethodException e3) {
                            constructor = args[0].getConstructor(Project.class);
                        }
                        String propName4 = getPropertyName(name, "add");
                        if (this.nestedTypes.get(propName4) == null) {
                            this.nestedTypes.put(propName4, args[0]);
                            this.nestedCreators.put(propName4, new AddNestedCreator(m, constructor, 1));
                        }
                    } catch (NoSuchMethodException e4) {
                    }
                }
            }
        }
        this.addText = addTextMethod;
    }

    private boolean isHiddenSetMethod(String name, Class<?> type) {
        return ("setLocation".equals(name) && Location.class.equals(type)) || ("setTaskType".equals(name) && String.class.equals(type));
    }

    public static synchronized IntrospectionHelper getHelper(Class<?> c) {
        return getHelper(null, c);
    }

    public static synchronized IntrospectionHelper getHelper(Project p, Class<?> c) {
        IntrospectionHelper ih = HELPERS.get(c.getName());
        if (ih == null || ih.bean != c) {
            ih = new IntrospectionHelper(c);
            if (p != null) {
                HELPERS.put(c.getName(), ih);
            }
        }
        return ih;
    }

    public void setAttribute(Project p, Object element, String attributeName, Object value) throws BuildException {
        AttributeSetter as = this.attributeSetters.get(attributeName.toLowerCase(Locale.ENGLISH));
        if (as == null && value != null) {
            if (element instanceof DynamicAttributeNS) {
                DynamicAttributeNS dc = (DynamicAttributeNS) element;
                String uriPlusPrefix = ProjectHelper.extractUriFromComponentName(attributeName);
                String uri = ProjectHelper.extractUriFromComponentName(uriPlusPrefix);
                String localName = ProjectHelper.extractNameFromComponentName(attributeName);
                String qName = uri.isEmpty() ? localName : uri + ":" + localName;
                dc.setDynamicAttribute(uri, localName, qName, value.toString());
            } else if (element instanceof DynamicObjectAttribute) {
                DynamicObjectAttribute dc2 = (DynamicObjectAttribute) element;
                dc2.setDynamicAttribute(attributeName.toLowerCase(Locale.ENGLISH), value);
            } else if (element instanceof DynamicAttribute) {
                DynamicAttribute dc3 = (DynamicAttribute) element;
                dc3.setDynamicAttribute(attributeName.toLowerCase(Locale.ENGLISH), value.toString());
            } else if (attributeName.contains(":")) {
            } else {
                String msg = getElementName(p, element) + " doesn't support the \"" + attributeName + "\" attribute.";
                throw new UnsupportedAttributeException(msg, attributeName);
            }
        } else if (as != null) {
            try {
                as.setObject(p, element, value);
            } catch (IllegalAccessException ie) {
                throw new BuildException(ie);
            } catch (InvocationTargetException ite) {
                throw extractBuildException(ite);
            }
        }
    }

    public void setAttribute(Project p, Object element, String attributeName, String value) throws BuildException {
        setAttribute(p, element, attributeName, (Object) value);
    }

    public void addText(Project project, Object element, String text) throws BuildException {
        if (this.addText == null) {
            String text2 = text.trim();
            if (text2.isEmpty()) {
                return;
            }
            throw new BuildException(project.getElementName(element) + " doesn't support nested text data (\"" + condenseText(text2) + "\").");
        }
        try {
            this.addText.invoke(element, text);
        } catch (IllegalAccessException ie) {
            throw new BuildException(ie);
        } catch (InvocationTargetException ite) {
            throw extractBuildException(ite);
        }
    }

    public void throwNotSupported(Project project, Object parent, String elementName) {
        String msg = project.getElementName(parent) + NOT_SUPPORTED_CHILD_PREFIX + elementName + NOT_SUPPORTED_CHILD_POSTFIX;
        throw new UnsupportedElementException(msg, elementName);
    }

    private NestedCreator getNestedCreator(Project project, String parentUri, Object parent, String elementName, UnknownElement child) throws BuildException {
        String uri = ProjectHelper.extractUriFromComponentName(elementName);
        String name = ProjectHelper.extractNameFromComponentName(elementName);
        if (uri.equals(ProjectHelper.ANT_CORE_URI)) {
            uri = "";
        }
        if (parentUri.equals(ProjectHelper.ANT_CORE_URI)) {
            parentUri = "";
        }
        NestedCreator nc = null;
        if (uri.equals(parentUri) || uri.isEmpty()) {
            nc = this.nestedCreators.get(name.toLowerCase(Locale.ENGLISH));
        }
        if (nc == null) {
            nc = createAddTypeCreator(project, parent, elementName);
        }
        if (nc == null && ((parent instanceof DynamicElementNS) || (parent instanceof DynamicElement))) {
            String qName = child == null ? name : child.getQName();
            final Object nestedElement = createDynamicElement(parent, child == null ? "" : child.getNamespace(), name, qName);
            if (nestedElement != null) {
                nc = new NestedCreator(null) { // from class: org.apache.tools.ant.IntrospectionHelper.1
                    @Override // org.apache.tools.ant.IntrospectionHelper.NestedCreator
                    Object create(Project project2, Object parent2, Object ignore) {
                        return nestedElement;
                    }
                };
            }
        }
        if (nc == null) {
            throwNotSupported(project, parent, elementName);
        }
        return nc;
    }

    private Object createDynamicElement(Object parent, String ns, String localName, String qName) {
        Object nestedElement = null;
        if (parent instanceof DynamicElementNS) {
            DynamicElementNS dc = (DynamicElementNS) parent;
            nestedElement = dc.createDynamicElement(ns, localName, qName);
        }
        if (nestedElement == null && (parent instanceof DynamicElement)) {
            DynamicElement dc2 = (DynamicElement) parent;
            nestedElement = dc2.createDynamicElement(localName.toLowerCase(Locale.ENGLISH));
        }
        return nestedElement;
    }

    @Deprecated
    public Object createElement(Project project, Object parent, String elementName) throws BuildException {
        NestedCreator nc = getNestedCreator(project, "", parent, elementName, null);
        try {
            Object nestedElement = nc.create(project, parent, null);
            if (project != null) {
                project.setProjectReference(nestedElement);
            }
            return nestedElement;
        } catch (IllegalAccessException | InstantiationException ie) {
            throw new BuildException(ie);
        } catch (InvocationTargetException ite) {
            throw extractBuildException(ite);
        }
    }

    public Creator getElementCreator(Project project, String parentUri, Object parent, String elementName, UnknownElement ue) {
        NestedCreator nc = getNestedCreator(project, parentUri, parent, elementName, ue);
        return new Creator(project, parent, nc);
    }

    public boolean isDynamic() {
        return DynamicElement.class.isAssignableFrom(this.bean) || DynamicElementNS.class.isAssignableFrom(this.bean);
    }

    public boolean isContainer() {
        return TaskContainer.class.isAssignableFrom(this.bean);
    }

    public boolean supportsNestedElement(String elementName) {
        return supportsNestedElement("", elementName);
    }

    public boolean supportsNestedElement(String parentUri, String elementName) {
        return isDynamic() || !this.addTypeMethods.isEmpty() || supportsReflectElement(parentUri, elementName);
    }

    public boolean supportsNestedElement(String parentUri, String elementName, Project project, Object parent) {
        return !(this.addTypeMethods.isEmpty() || createAddTypeCreator(project, parent, elementName) == null) || isDynamic() || supportsReflectElement(parentUri, elementName);
    }

    public boolean supportsReflectElement(String parentUri, String elementName) {
        String name = ProjectHelper.extractNameFromComponentName(elementName);
        if (!this.nestedCreators.containsKey(name.toLowerCase(Locale.ENGLISH))) {
            return false;
        }
        String uri = ProjectHelper.extractUriFromComponentName(elementName);
        if (uri.equals(ProjectHelper.ANT_CORE_URI) || uri.isEmpty()) {
            return true;
        }
        if (parentUri.equals(ProjectHelper.ANT_CORE_URI)) {
            parentUri = "";
        }
        return uri.equals(parentUri);
    }

    public void storeElement(Project project, Object parent, Object child, String elementName) throws BuildException {
        NestedCreator ns;
        if (elementName == null || (ns = this.nestedCreators.get(elementName.toLowerCase(Locale.ENGLISH))) == null) {
            return;
        }
        try {
            ns.store(parent, child);
        } catch (IllegalAccessException | InstantiationException ie) {
            throw new BuildException(ie);
        } catch (InvocationTargetException ite) {
            throw extractBuildException(ite);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BuildException extractBuildException(InvocationTargetException ite) {
        Throwable t = ite.getTargetException();
        if (t instanceof BuildException) {
            return (BuildException) t;
        }
        return new BuildException(t);
    }

    public Class<?> getElementType(String elementName) throws BuildException {
        Class<?> nt = this.nestedTypes.get(elementName);
        if (nt == null) {
            throw new UnsupportedElementException("Class " + this.bean.getName() + NOT_SUPPORTED_CHILD_PREFIX + elementName + NOT_SUPPORTED_CHILD_POSTFIX, elementName);
        }
        return nt;
    }

    public Class<?> getAttributeType(String attributeName) throws BuildException {
        Class<?> at = this.attributeTypes.get(attributeName);
        if (at == null) {
            throw new UnsupportedAttributeException("Class " + this.bean.getName() + " doesn't support the \"" + attributeName + "\" attribute.", attributeName);
        }
        return at;
    }

    public Method getAddTextMethod() throws BuildException {
        if (!supportsCharacters()) {
            throw new BuildException("Class " + this.bean.getName() + " doesn't support nested text data.");
        }
        return this.addText;
    }

    public Method getElementMethod(String elementName) throws BuildException {
        NestedCreator creator = this.nestedCreators.get(elementName);
        if (creator == null) {
            throw new UnsupportedElementException("Class " + this.bean.getName() + NOT_SUPPORTED_CHILD_PREFIX + elementName + NOT_SUPPORTED_CHILD_POSTFIX, elementName);
        }
        return creator.method;
    }

    public Method getAttributeMethod(String attributeName) throws BuildException {
        AttributeSetter setter = this.attributeSetters.get(attributeName);
        if (setter == null) {
            throw new UnsupportedAttributeException("Class " + this.bean.getName() + " doesn't support the \"" + attributeName + "\" attribute.", attributeName);
        }
        return setter.method;
    }

    public boolean supportsCharacters() {
        return this.addText != null;
    }

    public Enumeration<String> getAttributes() {
        return Collections.enumeration(this.attributeSetters.keySet());
    }

    public Map<String, Class<?>> getAttributeMap() {
        return this.attributeTypes.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.attributeTypes);
    }

    public Enumeration<String> getNestedElements() {
        return Collections.enumeration(this.nestedTypes.keySet());
    }

    public Map<String, Class<?>> getNestedElementMap() {
        return this.nestedTypes.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.nestedTypes);
    }

    public List<Method> getExtensionPoints() {
        return this.addTypeMethods.isEmpty() ? Collections.emptyList() : Collections.unmodifiableList(this.addTypeMethods);
    }

    private AttributeSetter createAttributeSetter(final Method m, Class<?> arg, final String attrName) {
        Constructor<?> c;
        boolean includeProject;
        final Class<?> reflectedArg = PRIMITIVE_TYPE_MAP.getOrDefault(arg, arg);
        if (Object.class == reflectedArg) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.2
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
                    throw new BuildException("Internal ant problem - this should not get called");
                }
            };
        }
        if (String.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.3
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
                    m.invoke(parent, value);
                }
            };
        }
        if (Character.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.4
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
                    if (value.isEmpty()) {
                        throw new BuildException("The value \"\" is not a legal value for attribute \"" + attrName + "\"");
                    }
                    m.invoke(parent, Character.valueOf(value.charAt(0)));
                }
            };
        }
        if (Boolean.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.5
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
                    Method method = m;
                    Boolean[] boolArr = new Boolean[1];
                    boolArr[0] = Project.toBoolean(value) ? Boolean.TRUE : Boolean.FALSE;
                    method.invoke(parent, boolArr);
                }
            };
        }
        if (Class.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.6
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    try {
                        m.invoke(parent, Class.forName(value));
                    } catch (ClassNotFoundException ce) {
                        throw new BuildException(ce);
                    }
                }
            };
        }
        if (File.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.7
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
                    m.invoke(parent, p.resolveFile(value));
                }
            };
        }
        if (Path.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.8
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException {
                    m.invoke(parent, p.resolveFile(value).toPath());
                }
            };
        }
        if (Resource.class.equals(reflectedArg) || FileProvider.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.9
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    m.invoke(parent, new FileResource(p, p.resolveFile(value)));
                }
            };
        }
        if (EnumeratedAttribute.class.isAssignableFrom(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.10
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    try {
                        EnumeratedAttribute ea = (EnumeratedAttribute) reflectedArg.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                        ea.setValue(value);
                        m.invoke(parent, ea);
                    } catch (InstantiationException | NoSuchMethodException ie) {
                        throw new BuildException(ie);
                    }
                }
            };
        }
        AttributeSetter setter = getEnumSetter(reflectedArg, m, arg);
        if (setter != null) {
            return setter;
        }
        if (Long.class.equals(reflectedArg)) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.11
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    try {
                        m.invoke(parent, Long.valueOf(StringUtils.parseHumanSizes(value)));
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw e;
                    } catch (NumberFormatException e2) {
                        throw new BuildException("Can't assign non-numeric value '" + value + "' to attribute " + attrName);
                    } catch (Exception e3) {
                        throw new BuildException(e3);
                    }
                }
            };
        }
        try {
            c = reflectedArg.getConstructor(Project.class, String.class);
            includeProject = true;
        } catch (NoSuchMethodException e) {
            try {
                c = reflectedArg.getConstructor(String.class);
                includeProject = false;
            } catch (NoSuchMethodException e2) {
                return null;
            }
        }
        final boolean finalIncludeProject = includeProject;
        final Constructor<?> finalConstructor = c;
        return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.12
            @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
            public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                try {
                    Object[] args = finalIncludeProject ? new Object[]{p, value} : new Object[]{value};
                    Object attribute = finalConstructor.newInstance(args);
                    if (p != null) {
                        p.setProjectReference(attribute);
                    }
                    m.invoke(parent, attribute);
                } catch (InstantiationException ie) {
                    throw new BuildException(ie);
                } catch (InvocationTargetException e3) {
                    Throwable cause = e3.getCause();
                    if (cause instanceof IllegalArgumentException) {
                        throw new BuildException("Can't assign value '" + value + "' to attribute " + attrName + ", reason: " + cause.getClass() + " with message '" + cause.getMessage() + "'");
                    }
                    throw e3;
                }
            }
        };
    }

    private AttributeSetter getEnumSetter(final Class<?> reflectedArg, final Method m, Class<?> arg) {
        if (reflectedArg.isEnum()) {
            return new AttributeSetter(m, arg) { // from class: org.apache.tools.ant.IntrospectionHelper.13
                @Override // org.apache.tools.ant.IntrospectionHelper.AttributeSetter
                public void set(Project p, Object parent, String value) throws InvocationTargetException, IllegalAccessException, BuildException {
                    try {
                        Enum<?> enumValue = Enum.valueOf(reflectedArg, value);
                        m.invoke(parent, enumValue);
                    } catch (IllegalArgumentException e) {
                        throw new BuildException("'" + value + "' is not a permitted value for " + reflectedArg.getName());
                    }
                }
            };
        }
        return null;
    }

    private String getElementName(Project project, Object element) {
        return project.getElementName(element);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getPropertyName(String methodName, String prefix) {
        return methodName.substring(prefix.length()).toLowerCase(Locale.ENGLISH);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/IntrospectionHelper$Creator.class */
    public static final class Creator {
        private final NestedCreator nestedCreator;
        private final Object parent;
        private final Project project;
        private Object nestedObject;
        private String polyType;

        private Creator(Project project, Object parent, NestedCreator nestedCreator) {
            this.project = project;
            this.parent = parent;
            this.nestedCreator = nestedCreator;
        }

        public void setPolyType(String polyType) {
            this.polyType = polyType;
        }

        public Object create() {
            if (this.polyType != null) {
                if (!this.nestedCreator.isPolyMorphic()) {
                    throw new BuildException("Not allowed to use the polymorphic form for this element");
                }
                ComponentHelper helper = ComponentHelper.getComponentHelper(this.project);
                this.nestedObject = helper.createComponent(this.polyType);
                if (this.nestedObject == null) {
                    throw new BuildException("Unable to create object of type " + this.polyType);
                }
            }
            try {
                this.nestedObject = this.nestedCreator.create(this.project, this.parent, this.nestedObject);
                if (this.project != null) {
                    this.project.setProjectReference(this.nestedObject);
                }
                return this.nestedObject;
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new BuildException(ex);
            } catch (IllegalArgumentException ex2) {
                if (this.polyType == null) {
                    throw ex2;
                }
                throw new BuildException("Invalid type used " + this.polyType);
            } catch (InvocationTargetException ex3) {
                throw IntrospectionHelper.extractBuildException(ex3);
            }
        }

        public Object getRealObject() {
            return this.nestedCreator.getRealObject();
        }

        public void store() {
            try {
                this.nestedCreator.store(this.parent, this.nestedObject);
            } catch (IllegalAccessException | InstantiationException ex) {
                throw new BuildException(ex);
            } catch (IllegalArgumentException ex2) {
                if (this.polyType == null) {
                    throw ex2;
                }
                throw new BuildException("Invalid type used " + this.polyType);
            } catch (InvocationTargetException ex3) {
                throw IntrospectionHelper.extractBuildException(ex3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/IntrospectionHelper$NestedCreator.class */
    public static abstract class NestedCreator {
        private final Method method;

        abstract Object create(Project project, Object obj, Object obj2) throws InvocationTargetException, IllegalAccessException, InstantiationException;

        protected NestedCreator(Method m) {
            this.method = m;
        }

        Method getMethod() {
            return this.method;
        }

        boolean isPolyMorphic() {
            return false;
        }

        Object getRealObject() {
            return null;
        }

        void store(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/IntrospectionHelper$CreateNestedCreator.class */
    private static class CreateNestedCreator extends NestedCreator {
        CreateNestedCreator(Method m) {
            super(m);
        }

        @Override // org.apache.tools.ant.IntrospectionHelper.NestedCreator
        Object create(Project project, Object parent, Object ignore) throws InvocationTargetException, IllegalAccessException {
            return getMethod().invoke(parent, new Object[0]);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/IntrospectionHelper$AddNestedCreator.class */
    private static class AddNestedCreator extends NestedCreator {
        static final int ADD = 1;
        static final int ADD_CONFIGURED = 2;
        private final Constructor<?> constructor;
        private final int behavior;

        AddNestedCreator(Method m, Constructor<?> c, int behavior) {
            super(m);
            this.constructor = c;
            this.behavior = behavior;
        }

        @Override // org.apache.tools.ant.IntrospectionHelper.NestedCreator
        boolean isPolyMorphic() {
            return true;
        }

        @Override // org.apache.tools.ant.IntrospectionHelper.NestedCreator
        Object create(Project project, Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            if (child == null) {
                child = this.constructor.newInstance(this.constructor.getParameterTypes().length == 0 ? new Object[0] : new Object[]{project});
            }
            if (child instanceof PreSetDef.PreSetDefinition) {
                child = ((PreSetDef.PreSetDefinition) child).createObject(project);
            }
            if (this.behavior == 1) {
                istore(parent, child);
            }
            return child;
        }

        @Override // org.apache.tools.ant.IntrospectionHelper.NestedCreator
        void store(Object parent, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
            if (this.behavior == 2) {
                istore(parent, child);
            }
        }

        private void istore(Object parent, Object child) throws InvocationTargetException, IllegalAccessException {
            getMethod().invoke(parent, child);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/IntrospectionHelper$AttributeSetter.class */
    public static abstract class AttributeSetter {
        private final Method method;
        private final Class<?> type;

        abstract void set(Project project, Object obj, String str) throws InvocationTargetException, IllegalAccessException, BuildException;

        protected AttributeSetter(Method m, Class<?> type) {
            this.method = m;
            this.type = type;
        }

        void setObject(Project p, Object parent, Object value) throws InvocationTargetException, IllegalAccessException, BuildException {
            if (this.type != null) {
                Class<?> useType = this.type;
                if (this.type.isPrimitive()) {
                    if (value != null) {
                        useType = (Class) IntrospectionHelper.PRIMITIVE_TYPE_MAP.get(this.type);
                    } else {
                        throw new BuildException("Attempt to set primitive " + IntrospectionHelper.getPropertyName(this.method.getName(), "set") + " to null on " + parent);
                    }
                }
                if (value == null || useType.isInstance(value)) {
                    this.method.invoke(parent, value);
                    return;
                }
            }
            set(p, parent, value.toString());
        }
    }

    public static synchronized void clearCache() {
        HELPERS.clear();
    }

    private NestedCreator createAddTypeCreator(Project project, Object parent, String elementName) throws BuildException {
        if (this.addTypeMethods.isEmpty()) {
            return null;
        }
        ComponentHelper helper = ComponentHelper.getComponentHelper(project);
        MethodAndObject restricted = createRestricted(helper, elementName, this.addTypeMethods);
        MethodAndObject topLevel = createTopLevel(helper, elementName, this.addTypeMethods);
        if (restricted == null && topLevel == null) {
            return null;
        }
        if (restricted != null && topLevel != null) {
            throw new BuildException("ambiguous: type and component definitions for " + elementName);
        }
        MethodAndObject methodAndObject = restricted == null ? topLevel : restricted;
        Object rObject = methodAndObject.object;
        if (methodAndObject.object instanceof PreSetDef.PreSetDefinition) {
            rObject = ((PreSetDef.PreSetDefinition) methodAndObject.object).createObject(project);
        }
        final Object nestedObject = methodAndObject.object;
        final Object realObject = rObject;
        return new NestedCreator(methodAndObject.method) { // from class: org.apache.tools.ant.IntrospectionHelper.14
            @Override // org.apache.tools.ant.IntrospectionHelper.NestedCreator
            Object create(Project project2, Object parent2, Object ignore) throws InvocationTargetException, IllegalAccessException {
                if (!getMethod().getName().endsWith("Configured")) {
                    getMethod().invoke(parent2, realObject);
                }
                return nestedObject;
            }

            @Override // org.apache.tools.ant.IntrospectionHelper.NestedCreator
            Object getRealObject() {
                return realObject;
            }

            @Override // org.apache.tools.ant.IntrospectionHelper.NestedCreator
            void store(Object parent2, Object child) throws InvocationTargetException, IllegalAccessException, InstantiationException {
                if (getMethod().getName().endsWith("Configured")) {
                    getMethod().invoke(parent2, realObject);
                }
            }
        };
    }

    private void insertAddTypeMethod(Method method) {
        Class<?> argClass = method.getParameterTypes()[0];
        int size = this.addTypeMethods.size();
        for (int c = 0; c < size; c++) {
            Method current = this.addTypeMethods.get(c);
            if (current.getParameterTypes()[0].equals(argClass)) {
                if ("addConfigured".equals(method.getName())) {
                    this.addTypeMethods.set(c, method);
                    return;
                }
                return;
            } else if (current.getParameterTypes()[0].isAssignableFrom(argClass)) {
                this.addTypeMethods.add(c, method);
                return;
            }
        }
        this.addTypeMethods.add(method);
    }

    private Method findMatchingMethod(Class<?> paramClass, List<Method> methods) {
        if (paramClass == null) {
            return null;
        }
        Class<?> matchedClass = null;
        Method matchedMethod = null;
        for (Method method : methods) {
            Class<?> methodClass = method.getParameterTypes()[0];
            if (methodClass.isAssignableFrom(paramClass)) {
                if (matchedClass == null) {
                    matchedClass = methodClass;
                    matchedMethod = method;
                } else if (!methodClass.isAssignableFrom(matchedClass)) {
                    throw new BuildException("ambiguous: types " + matchedClass.getName() + " and " + methodClass.getName() + " match " + paramClass.getName());
                }
            }
        }
        return matchedMethod;
    }

    private String condenseText(String text) {
        if (text.length() <= 20) {
            return text;
        }
        int ends = (20 - ELLIPSIS.length()) / 2;
        return new StringBuffer(text).replace(ends, text.length() - ends, ELLIPSIS).toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/IntrospectionHelper$MethodAndObject.class */
    public static class MethodAndObject {
        private final Method method;
        private final Object object;

        public MethodAndObject(Method method, Object object) {
            this.method = method;
            this.object = object;
        }
    }

    private AntTypeDefinition findRestrictedDefinition(ComponentHelper helper, String componentName, List<Method> methods) {
        AntTypeDefinition definition = null;
        Class<?> matchedDefinitionClass = null;
        List<AntTypeDefinition> definitions = helper.getRestrictedDefinitions(componentName);
        if (definitions == null) {
            return null;
        }
        synchronized (definitions) {
            for (AntTypeDefinition d : definitions) {
                Class<?> exposedClass = d.getExposedClass(helper.getProject());
                if (exposedClass != null) {
                    Method method = findMatchingMethod(exposedClass, methods);
                    if (method != null) {
                        if (matchedDefinitionClass != null) {
                            throw new BuildException("ambiguous: restricted definitions for " + componentName + Instruction.argsep + matchedDefinitionClass + " and " + exposedClass);
                        }
                        matchedDefinitionClass = exposedClass;
                        definition = d;
                    }
                }
            }
        }
        return definition;
    }

    private MethodAndObject createRestricted(ComponentHelper helper, String elementName, List<Method> addTypeMethods) {
        Project project = helper.getProject();
        AntTypeDefinition restrictedDefinition = findRestrictedDefinition(helper, elementName, addTypeMethods);
        if (restrictedDefinition == null) {
            return null;
        }
        Method addMethod = findMatchingMethod(restrictedDefinition.getExposedClass(project), addTypeMethods);
        if (addMethod == null) {
            throw new BuildException("Ant Internal Error - contract mismatch for " + elementName);
        }
        Object addedObject = restrictedDefinition.create(project);
        if (addedObject == null) {
            throw new BuildException("Failed to create object " + elementName + " of type " + restrictedDefinition.getTypeClass(project));
        }
        return new MethodAndObject(addMethod, addedObject);
    }

    private MethodAndObject createTopLevel(ComponentHelper helper, String elementName, List<Method> methods) {
        Method addMethod;
        Class<?> clazz = helper.getComponentClass(elementName);
        if (clazz == null || (addMethod = findMatchingMethod(clazz, this.addTypeMethods)) == null) {
            return null;
        }
        Object addedObject = helper.createComponent(elementName);
        return new MethodAndObject(addMethod, addedObject);
    }
}
