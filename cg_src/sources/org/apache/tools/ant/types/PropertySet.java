package org.apache.tools.ant.types;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.PropertyHelper;
import org.apache.tools.ant.types.Mapper;
import org.apache.tools.ant.types.resources.MappedResource;
import org.apache.tools.ant.types.resources.PropertyResource;
import org.apache.tools.ant.types.selectors.FilenameSelector;
import org.apache.tools.ant.util.FileNameMapper;
import org.apache.tools.ant.util.regexp.RegexpMatcher;
import org.apache.tools.ant.util.regexp.RegexpMatcherFactory;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/PropertySet.class */
public class PropertySet extends DataType implements ResourceCollection {
    private Set<String> cachedNames;
    private Mapper mapper;
    private boolean dynamic = true;
    private boolean negate = false;
    private List<PropertyRef> ptyRefs = new ArrayList();
    private List<PropertySet> setRefs = new ArrayList();
    private boolean noAttributeSet = true;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/PropertySet$PropertyRef.class */
    public static class PropertyRef {
        private int count;
        private String name;
        private String regex;
        private String prefix;
        private String builtin;

        public void setName(String name) {
            assertValid("name", name);
            this.name = name;
        }

        public void setRegex(String regex) {
            assertValid(FilenameSelector.REGEX_KEY, regex);
            this.regex = regex;
        }

        public void setPrefix(String prefix) {
            assertValid("prefix", prefix);
            this.prefix = prefix;
        }

        public void setBuiltin(BuiltinPropertySetName b) {
            String pBuiltIn = b.getValue();
            assertValid("builtin", pBuiltIn);
            this.builtin = pBuiltIn;
        }

        private void assertValid(String attr, String value) {
            if (value == null || value.length() < 1) {
                throw new BuildException("Invalid attribute: " + attr);
            }
            int i = this.count + 1;
            this.count = i;
            if (i != 1) {
                throw new BuildException("Attributes name, regex, and prefix are mutually exclusive");
            }
        }

        public String toString() {
            return "name=" + this.name + ", regex=" + this.regex + ", prefix=" + this.prefix + ", builtin=" + this.builtin;
        }
    }

    public void appendName(String name) {
        PropertyRef r = new PropertyRef();
        r.setName(name);
        addPropertyref(r);
    }

    public void appendRegex(String regex) {
        PropertyRef r = new PropertyRef();
        r.setRegex(regex);
        addPropertyref(r);
    }

    public void appendPrefix(String prefix) {
        PropertyRef r = new PropertyRef();
        r.setPrefix(prefix);
        addPropertyref(r);
    }

    public void appendBuiltin(BuiltinPropertySetName b) {
        PropertyRef r = new PropertyRef();
        r.setBuiltin(b);
        addPropertyref(r);
    }

    public void setMapper(String type, String from, String to) {
        Mapper m = createMapper();
        Mapper.MapperType mapperType = new Mapper.MapperType();
        mapperType.setValue(type);
        m.setType(mapperType);
        m.setFrom(from);
        m.setTo(to);
    }

    public void addPropertyref(PropertyRef ref) {
        assertNotReference();
        setChecked(false);
        this.ptyRefs.add(ref);
    }

    public void addPropertyset(PropertySet ref) {
        assertNotReference();
        setChecked(false);
        this.setRefs.add(ref);
    }

    public Mapper createMapper() {
        assertNotReference();
        if (this.mapper != null) {
            throw new BuildException("Too many <mapper>s!");
        }
        this.mapper = new Mapper(getProject());
        setChecked(false);
        return this.mapper;
    }

    public void add(FileNameMapper fileNameMapper) {
        createMapper().add(fileNameMapper);
    }

    public void setDynamic(boolean dynamic) {
        assertNotReference();
        this.dynamic = dynamic;
    }

    public void setNegate(boolean negate) {
        assertNotReference();
        this.negate = negate;
    }

    public boolean getDynamic() {
        if (isReference()) {
            return getRef().dynamic;
        }
        dieOnCircularReference();
        return this.dynamic;
    }

    public Mapper getMapper() {
        if (isReference()) {
            return getRef().mapper;
        }
        dieOnCircularReference();
        return this.mapper;
    }

    private Map<String, Object> getAllSystemProperties() {
        return (Map) System.getProperties().stringPropertyNames().stream().collect(Collectors.toMap(name -> {
            return name;
        }, name2 -> {
            return System.getProperties().getProperty(name2);
        }, a, b -> {
            return b;
        }));
    }

    public Properties getProperties() {
        Properties result = new Properties();
        result.putAll(getPropertyMap());
        return result;
    }

    private Map<String, Object> getPropertyMap() {
        String[] newname;
        if (isReference()) {
            return getRef().getPropertyMap();
        }
        dieOnCircularReference();
        Mapper myMapper = getMapper();
        FileNameMapper m = myMapper == null ? null : myMapper.getImplementation();
        Map<String, Object> effectiveProperties = getEffectiveProperties();
        Set<String> propertyNames = getPropertyNames(effectiveProperties);
        Map<String, Object> result = new HashMap<>();
        for (String name : propertyNames) {
            Object value = effectiveProperties.get(name);
            if (value != null) {
                if (m != null && (newname = m.mapFileName(name)) != null) {
                    name = newname[0];
                }
                result.put(name, value);
            }
        }
        return result;
    }

    private Map<String, Object> getEffectiveProperties() {
        Map<String, Object> result;
        Project prj = getProject();
        if (prj == null) {
            result = getAllSystemProperties();
        } else {
            PropertyHelper ph = PropertyHelper.getPropertyHelper(prj);
            result = (Map) prj.getPropertyNames().stream().map(n -> {
                return new AbstractMap.SimpleImmutableEntry(n, ph.getProperty(n));
            }).filter(kv -> {
                return kv.getValue() != null;
            }).collect(Collectors.toMap((v0) -> {
                return v0.getKey();
            }, (v0) -> {
                return v0.getValue();
            }));
        }
        for (PropertySet set : this.setRefs) {
            result.putAll(set.getPropertyMap());
        }
        return result;
    }

    private Set<String> getPropertyNames(Map<String, Object> props) {
        Set<String> names;
        if (getDynamic() || this.cachedNames == null) {
            names = new HashSet<>();
            addPropertyNames(names, props);
            for (PropertySet set : this.setRefs) {
                names.addAll(set.getPropertyMap().keySet());
            }
            if (this.negate) {
                HashSet<String> complement = new HashSet<>(props.keySet());
                complement.removeAll(names);
                names = complement;
            }
            if (!getDynamic()) {
                this.cachedNames = names;
            }
        } else {
            names = this.cachedNames;
        }
        return names;
    }

    private void addPropertyNames(Set<String> names, Map<String, Object> props) {
        if (isReference()) {
            getRef().addPropertyNames(names, props);
        }
        dieOnCircularReference();
        for (PropertyRef r : this.ptyRefs) {
            if (r.name == null) {
                if (r.prefix == null) {
                    if (r.regex == null) {
                        if (r.builtin != null) {
                            String str = r.builtin;
                            boolean z = true;
                            switch (str.hashCode()) {
                                case -1460538945:
                                    if (str.equals("commandline")) {
                                        z = true;
                                        break;
                                    }
                                    break;
                                case -887328209:
                                    if (str.equals("system")) {
                                        z = true;
                                        break;
                                    }
                                    break;
                                case 96673:
                                    if (str.equals("all")) {
                                        z = false;
                                        break;
                                    }
                                    break;
                            }
                            switch (z) {
                                case false:
                                    names.addAll(props.keySet());
                                    continue;
                                case true:
                                    names.addAll(getAllSystemProperties().keySet());
                                    continue;
                                case true:
                                    names.addAll(getProject().getUserProperties().keySet());
                                    continue;
                                default:
                                    throw new BuildException("Impossible: Invalid builtin attribute!");
                            }
                        } else {
                            throw new BuildException("Impossible: Invalid PropertyRef!");
                        }
                    } else {
                        RegexpMatcherFactory matchMaker = new RegexpMatcherFactory();
                        RegexpMatcher matcher = matchMaker.newRegexpMatcher();
                        matcher.setPattern(r.regex);
                        for (String name : props.keySet()) {
                            if (matcher.matches(name)) {
                                names.add(name);
                            }
                        }
                    }
                } else {
                    for (String name2 : props.keySet()) {
                        if (name2.startsWith(r.prefix)) {
                            names.add(name2);
                        }
                    }
                }
            } else if (props.get(r.name) != null) {
                names.add(r.name);
            }
        }
    }

    protected PropertySet getRef() {
        return (PropertySet) getCheckedRef(PropertySet.class);
    }

    @Override // org.apache.tools.ant.types.DataType
    public final void setRefid(Reference r) {
        if (!this.noAttributeSet) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    protected final void assertNotReference() {
        if (isReference()) {
            throw tooManyAttributes();
        }
        this.noAttributeSet = false;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/PropertySet$BuiltinPropertySetName.class */
    public static class BuiltinPropertySetName extends EnumeratedAttribute {
        static final String ALL = "all";
        static final String SYSTEM = "system";
        static final String COMMANDLINE = "commandline";

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{ALL, SYSTEM, COMMANDLINE};
        }
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        if (isReference()) {
            return getRef().toString();
        }
        dieOnCircularReference();
        return (String) new TreeMap(getPropertyMap()).entrySet().stream().map(e -> {
            return ((String) e.getKey()) + "=" + e.getValue();
        }).collect(Collectors.joining(", "));
    }

    @Override // java.lang.Iterable
    public Iterator<Resource> iterator() {
        if (isReference()) {
            return getRef().iterator();
        }
        dieOnCircularReference();
        Stream map = getPropertyNames(getEffectiveProperties()).stream().map(name -> {
            return new PropertyResource(getProject(), name);
        });
        Optional<FileNameMapper> m = Optional.ofNullable(getMapper()).map((v0) -> {
            return v0.getImplementation();
        });
        if (m.isPresent()) {
            map = map.map(p -> {
                return new MappedResource(p, (FileNameMapper) m.get());
            });
        }
        return map.iterator();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public int size() {
        return isReference() ? getRef().size() : getProperties().size();
    }

    @Override // org.apache.tools.ant.types.ResourceCollection
    public boolean isFilesystemOnly() {
        if (isReference()) {
            return getRef().isFilesystemOnly();
        }
        dieOnCircularReference();
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.tools.ant.types.DataType
    public synchronized void dieOnCircularReference(Stack<Object> stk, Project p) throws BuildException {
        if (isChecked()) {
            return;
        }
        if (isReference()) {
            super.dieOnCircularReference(stk, p);
            return;
        }
        if (this.mapper != null) {
            pushAndInvokeCircularReferenceCheck(this.mapper, stk, p);
        }
        for (PropertySet propertySet : this.setRefs) {
            pushAndInvokeCircularReferenceCheck(propertySet, stk, p);
        }
        setChecked(true);
    }
}
