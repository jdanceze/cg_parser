package org.apache.tools.ant.property;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/property/ResolvePropertyMap.class */
public class ResolvePropertyMap implements GetProperty {
    private final ParseProperties parseProperties;
    private final GetProperty master;
    private Map<String, Object> map;
    private String prefix;
    private final Set<String> seen = new HashSet();
    private boolean prefixValues = false;
    private boolean expandingLHS = true;

    public ResolvePropertyMap(Project project, GetProperty master, Collection<PropertyExpander> expanders) {
        this.master = master;
        this.parseProperties = new ParseProperties(project, expanders, this);
    }

    @Override // org.apache.tools.ant.property.GetProperty
    public Object getProperty(String name) {
        if (this.seen.contains(name)) {
            throw new BuildException("Property %s was circularly defined.", name);
        }
        try {
            String fullKey = name;
            if (this.prefix != null && (this.expandingLHS || this.prefixValues)) {
                fullKey = this.prefix + name;
            }
            Object masterValue = this.master.getProperty(fullKey);
            if (masterValue != null) {
                return masterValue;
            }
            this.seen.add(name);
            String recursiveCallKey = name;
            if (this.prefix != null && !this.expandingLHS && !this.prefixValues) {
                recursiveCallKey = this.prefix + name;
            }
            this.expandingLHS = false;
            Object parseProperties = this.parseProperties.parseProperties((String) this.map.get(recursiveCallKey));
            this.seen.remove(name);
            return parseProperties;
        } finally {
            this.seen.remove(name);
        }
    }

    @Deprecated
    public void resolveAllProperties(Map<String, Object> map) {
        resolveAllProperties(map, null, false);
    }

    @Deprecated
    public void resolveAllProperties(Map<String, Object> map, String prefix) {
        resolveAllProperties(map, null, false);
    }

    public void resolveAllProperties(Map<String, Object> map, String prefix, boolean prefixValues) {
        this.map = map;
        this.prefix = prefix;
        this.prefixValues = prefixValues;
        for (String key : map.keySet()) {
            this.expandingLHS = true;
            Object result = getProperty(key);
            String value = result == null ? "" : result.toString();
            map.put(key, value);
        }
    }
}
