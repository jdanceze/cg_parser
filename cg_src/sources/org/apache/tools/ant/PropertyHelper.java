package org.apache.tools.ant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import org.apache.tools.ant.property.GetProperty;
import org.apache.tools.ant.property.NullReturn;
import org.apache.tools.ant.property.ParseProperties;
import org.apache.tools.ant.property.PropertyExpander;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/PropertyHelper.class */
public class PropertyHelper implements GetProperty {
    private static final PropertyEvaluator TO_STRING = new PropertyEvaluator() { // from class: org.apache.tools.ant.PropertyHelper.1
        private final String PREFIX = "toString:";
        private final int PREFIX_LEN = "toString:".length();

        @Override // org.apache.tools.ant.PropertyHelper.PropertyEvaluator
        public Object evaluate(String property, PropertyHelper propertyHelper) {
            Object o = null;
            if (property.startsWith("toString:") && propertyHelper.getProject() != null) {
                o = propertyHelper.getProject().getReference(property.substring(this.PREFIX_LEN));
            }
            if (o == null) {
                return null;
            }
            return o.toString();
        }
    };
    private static final PropertyExpander DEFAULT_EXPANDER = s, pos, notUsed -> {
        int index = pos.getIndex();
        if (s.length() - index >= 3 && '$' == s.charAt(index) && '{' == s.charAt(index + 1)) {
            int start = index + 2;
            int end = s.indexOf(125, start);
            if (end < 0) {
                throw new BuildException("Syntax error in property: " + s.substring(index));
            }
            pos.setIndex(end + 1);
            return start == end ? "" : s.substring(start, end);
        }
        return null;
    };
    private static final PropertyExpander SKIP_DOUBLE_DOLLAR = s, pos, notUsed -> {
        int index = pos.getIndex();
        if (s.length() - index < 2 || '$' != s.charAt(index)) {
            return null;
        }
        int index2 = index + 1;
        if ('$' == s.charAt(index2)) {
            pos.setIndex(index2);
            return null;
        }
        return null;
    };
    private static final PropertyEvaluator FROM_REF = new PropertyEvaluator() { // from class: org.apache.tools.ant.PropertyHelper.2
        private final String PREFIX = "ant.refid:";
        private final int PREFIX_LEN = "ant.refid:".length();

        @Override // org.apache.tools.ant.PropertyHelper.PropertyEvaluator
        public Object evaluate(String prop, PropertyHelper helper) {
            if (prop.startsWith("ant.refid:") && helper.getProject() != null) {
                return helper.getProject().getReference(prop.substring(this.PREFIX_LEN));
            }
            return null;
        }
    };
    private Project project;
    private PropertyHelper next;
    private final Hashtable<Class<? extends Delegate>, List<Delegate>> delegates = new Hashtable<>();
    private final Hashtable<String, Object> properties = new Hashtable<>();
    private final Hashtable<String, Object> userProperties = new Hashtable<>();
    private final Hashtable<String, Object> inheritedProperties = new Hashtable<>();

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/PropertyHelper$Delegate.class */
    public interface Delegate {
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/PropertyHelper$PropertyEnumerator.class */
    public interface PropertyEnumerator extends Delegate {
        Set<String> getPropertyNames();
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/PropertyHelper$PropertyEvaluator.class */
    public interface PropertyEvaluator extends Delegate {
        Object evaluate(String str, PropertyHelper propertyHelper);
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/PropertyHelper$PropertySetter.class */
    public interface PropertySetter extends Delegate {
        boolean setNew(String str, Object obj, PropertyHelper propertyHelper);

        boolean set(String str, Object obj, PropertyHelper propertyHelper);
    }

    protected PropertyHelper() {
        add(FROM_REF);
        add(TO_STRING);
        add(SKIP_DOUBLE_DOLLAR);
        add(DEFAULT_EXPANDER);
    }

    public static Object getProperty(Project project, String name) {
        return getPropertyHelper(project).getProperty(name);
    }

    public static void setProperty(Project project, String name, Object value) {
        getPropertyHelper(project).setProperty(name, value, true);
    }

    public static void setNewProperty(Project project, String name, Object value) {
        getPropertyHelper(project).setNewProperty(name, value);
    }

    public void setProject(Project p) {
        this.project = p;
    }

    public Project getProject() {
        return this.project;
    }

    @Deprecated
    public void setNext(PropertyHelper next) {
        this.next = next;
    }

    @Deprecated
    public PropertyHelper getNext() {
        return this.next;
    }

    public static synchronized PropertyHelper getPropertyHelper(Project project) {
        PropertyHelper helper = null;
        if (project != null) {
            helper = (PropertyHelper) project.getReference(MagicNames.REFID_PROPERTY_HELPER);
        }
        if (helper != null) {
            return helper;
        }
        PropertyHelper helper2 = new PropertyHelper();
        helper2.setProject(project);
        if (project != null) {
            project.addReference(MagicNames.REFID_PROPERTY_HELPER, helper2);
        }
        return helper2;
    }

    public Collection<PropertyExpander> getExpanders() {
        return getDelegates(PropertyExpander.class);
    }

    @Deprecated
    public boolean setPropertyHook(String ns, String name, Object value, boolean inherited, boolean user, boolean isNew) {
        if (getNext() != null) {
            return getNext().setPropertyHook(ns, name, value, inherited, user, isNew);
        }
        return false;
    }

    @Deprecated
    public Object getPropertyHook(String ns, String name, boolean user) {
        Object o;
        if (getNext() != null && (o = getNext().getPropertyHook(ns, name, user)) != null) {
            return o;
        }
        if (this.project != null && name.startsWith("toString:")) {
            Object v = this.project.getReference(name.substring("toString:".length()));
            if (v == null) {
                return null;
            }
            return v.toString();
        }
        return null;
    }

    @Deprecated
    public void parsePropertyString(String value, Vector<String> fragments, Vector<String> propertyRefs) throws BuildException {
        parsePropertyStringDefault(value, fragments, propertyRefs);
    }

    public String replaceProperties(String ns, String value, Hashtable<String, Object> keys) throws BuildException {
        return replaceProperties(value);
    }

    public String replaceProperties(String value) throws BuildException {
        Object o = parseProperties(value);
        return (o == null || (o instanceof String)) ? (String) o : o.toString();
    }

    public Object parseProperties(String value) throws BuildException {
        return new ParseProperties(getProject(), getExpanders(), this).parseProperties(value);
    }

    public boolean containsProperties(String value) {
        return new ParseProperties(getProject(), getExpanders(), this).containsProperties(value);
    }

    @Deprecated
    public boolean setProperty(String ns, String name, Object value, boolean verbose) {
        return setProperty(name, value, verbose);
    }

    public boolean setProperty(String name, Object value, boolean verbose) {
        for (PropertySetter setter : getDelegates(PropertySetter.class)) {
            if (setter.set(name, value, this)) {
                return true;
            }
        }
        synchronized (this) {
            if (this.userProperties.containsKey(name)) {
                if (this.project != null && verbose) {
                    this.project.log("Override ignored for user property \"" + name + "\"", 3);
                }
                return false;
            }
            if (this.project != null && verbose) {
                if (this.properties.containsKey(name)) {
                    this.project.log("Overriding previous definition of property \"" + name + "\"", 3);
                }
                this.project.log("Setting project property: " + name + " -> " + value, 4);
            }
            if (name != null && value != null) {
                this.properties.put(name, value);
            }
            return true;
        }
    }

    @Deprecated
    public void setNewProperty(String ns, String name, Object value) {
        setNewProperty(name, value);
    }

    public void setNewProperty(String name, Object value) {
        for (PropertySetter setter : getDelegates(PropertySetter.class)) {
            if (setter.setNew(name, value, this)) {
                return;
            }
        }
        synchronized (this) {
            if (this.project != null && this.properties.containsKey(name)) {
                this.project.log("Override ignored for property \"" + name + "\"", 3);
                return;
            }
            if (this.project != null) {
                this.project.log("Setting project property: " + name + " -> " + value, 4);
            }
            if (name != null && value != null) {
                this.properties.put(name, value);
            }
        }
    }

    @Deprecated
    public void setUserProperty(String ns, String name, Object value) {
        setUserProperty(name, value);
    }

    public void setUserProperty(String name, Object value) {
        if (this.project != null) {
            this.project.log("Setting ro project property: " + name + " -> " + value, 4);
        }
        synchronized (this) {
            this.userProperties.put(name, value);
            this.properties.put(name, value);
        }
    }

    @Deprecated
    public void setInheritedProperty(String ns, String name, Object value) {
        setInheritedProperty(name, value);
    }

    public void setInheritedProperty(String name, Object value) {
        if (this.project != null) {
            this.project.log("Setting ro project property: " + name + " -> " + value, 4);
        }
        synchronized (this) {
            this.inheritedProperties.put(name, value);
            this.userProperties.put(name, value);
            this.properties.put(name, value);
        }
    }

    @Deprecated
    public Object getProperty(String ns, String name) {
        return getProperty(name);
    }

    @Override // org.apache.tools.ant.property.GetProperty
    public Object getProperty(String name) {
        if (name == null) {
            return null;
        }
        for (PropertyEvaluator evaluator : getDelegates(PropertyEvaluator.class)) {
            Object o = evaluator.evaluate(name, this);
            if (o != null) {
                if (o instanceof NullReturn) {
                    return null;
                }
                return o;
            }
        }
        return this.properties.get(name);
    }

    public Set<String> getPropertyNames() {
        Set<String> names = new HashSet<>(this.properties.keySet());
        getDelegates(PropertyEnumerator.class).forEach(e -> {
            names.addAll(e.getPropertyNames());
        });
        return Collections.unmodifiableSet(names);
    }

    @Deprecated
    public Object getUserProperty(String ns, String name) {
        return getUserProperty(name);
    }

    public Object getUserProperty(String name) {
        if (name == null) {
            return null;
        }
        return this.userProperties.get(name);
    }

    public Hashtable<String, Object> getProperties() {
        Hashtable<String, Object> hashtable;
        synchronized (this.properties) {
            hashtable = new Hashtable<>(this.properties);
        }
        return hashtable;
    }

    public Hashtable<String, Object> getUserProperties() {
        Hashtable<String, Object> hashtable;
        synchronized (this.userProperties) {
            hashtable = new Hashtable<>(this.userProperties);
        }
        return hashtable;
    }

    public Hashtable<String, Object> getInheritedProperties() {
        Hashtable<String, Object> hashtable;
        synchronized (this.inheritedProperties) {
            hashtable = new Hashtable<>(this.inheritedProperties);
        }
        return hashtable;
    }

    protected Hashtable<String, Object> getInternalProperties() {
        return this.properties;
    }

    protected Hashtable<String, Object> getInternalUserProperties() {
        return this.userProperties;
    }

    protected Hashtable<String, Object> getInternalInheritedProperties() {
        return this.inheritedProperties;
    }

    public void copyInheritedProperties(Project other) {
        synchronized (this.inheritedProperties) {
            for (Map.Entry<String, Object> entry : this.inheritedProperties.entrySet()) {
                String arg = entry.getKey();
                if (other.getUserProperty(arg) == null) {
                    other.setInheritedProperty(arg, entry.getValue().toString());
                }
            }
        }
    }

    public void copyUserProperties(Project other) {
        synchronized (this.userProperties) {
            for (Map.Entry<String, Object> entry : this.userProperties.entrySet()) {
                String arg = entry.getKey();
                if (!this.inheritedProperties.containsKey(arg)) {
                    other.setUserProperty(arg, entry.getValue().toString());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void parsePropertyStringDefault(String value, Vector<String> fragments, Vector<String> propertyRefs) throws BuildException {
        int i = 0;
        while (true) {
            int prev = i;
            int pos = value.indexOf(36, prev);
            if (pos >= 0) {
                if (pos > 0) {
                    fragments.addElement(value.substring(prev, pos));
                }
                if (pos == value.length() - 1) {
                    fragments.addElement("$");
                    i = pos + 1;
                } else if (value.charAt(pos + 1) != '{') {
                    if (value.charAt(pos + 1) == '$') {
                        fragments.addElement("$");
                    } else {
                        fragments.addElement(value.substring(pos, pos + 2));
                    }
                    i = pos + 2;
                } else {
                    int endName = value.indexOf(125, pos);
                    if (endName < 0) {
                        throw new BuildException("Syntax error in property: " + value);
                    }
                    String propertyName = value.substring(pos + 2, endName);
                    fragments.addElement(null);
                    propertyRefs.addElement(propertyName);
                    i = endName + 1;
                }
            } else if (prev < value.length()) {
                fragments.addElement(value.substring(prev));
                return;
            } else {
                return;
            }
        }
    }

    public void add(Delegate delegate) {
        List<Delegate> list;
        synchronized (this.delegates) {
            for (Class<? extends Delegate> key : getDelegateInterfaces(delegate)) {
                List<Delegate> list2 = this.delegates.get(key);
                if (list2 == null) {
                    list = new ArrayList<>();
                } else {
                    list = new ArrayList<>(list2);
                    list.remove(delegate);
                }
                list.add(0, delegate);
                this.delegates.put(key, Collections.unmodifiableList(list));
            }
        }
    }

    protected <D extends Delegate> List<D> getDelegates(Class<D> type) {
        List<D> result = (List<D>) this.delegates.get(type);
        return result == null ? Collections.emptyList() : result;
    }

    protected static Set<Class<? extends Delegate>> getDelegateInterfaces(Delegate d) {
        Class<?>[] interfaces;
        HashSet hashSet = new HashSet();
        Class<?> cls = d.getClass();
        while (true) {
            Class<?> c = cls;
            if (c != null) {
                for (Class<?> ifc : c.getInterfaces()) {
                    if (Delegate.class.isAssignableFrom(ifc)) {
                        hashSet.add(ifc);
                    }
                }
                cls = c.getSuperclass();
            } else {
                hashSet.remove(Delegate.class);
                return hashSet;
            }
        }
    }

    public static Boolean toBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof String) {
            String s = (String) value;
            if (Project.toBoolean(s)) {
                return Boolean.TRUE;
            }
            if ("off".equalsIgnoreCase(s) || "false".equalsIgnoreCase(s) || "no".equalsIgnoreCase(s)) {
                return Boolean.FALSE;
            }
            return null;
        }
        return null;
    }

    private static boolean nullOrEmpty(Object value) {
        return value == null || "".equals(value);
    }

    private boolean evalAsBooleanOrPropertyName(Object value) {
        Boolean b = toBoolean(value);
        if (b != null) {
            return b.booleanValue();
        }
        return getProperty(String.valueOf(value)) != null;
    }

    public boolean testIfCondition(Object value) {
        return nullOrEmpty(value) || evalAsBooleanOrPropertyName(value);
    }

    public boolean testUnlessCondition(Object value) {
        return nullOrEmpty(value) || !evalAsBooleanOrPropertyName(value);
    }
}
