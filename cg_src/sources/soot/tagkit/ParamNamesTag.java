package soot.tagkit;

import java.util.Arrays;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/tagkit/ParamNamesTag.class */
public class ParamNamesTag implements Tag {
    public static final String NAME = "ParamNamesTag";
    private final String[] names;

    public ParamNamesTag(List<String> parameterNames) {
        this((String[]) parameterNames.toArray(new String[parameterNames.size()]));
    }

    public ParamNamesTag(String[] parameterNames) {
        this.names = parameterNames;
    }

    public String toString() {
        return Arrays.toString(this.names);
    }

    public List<String> getNames() {
        return Arrays.asList(this.names);
    }

    public String[] getNameArray() {
        return this.names;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public List<String> getInfo() {
        return getNames();
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("ParamNamesTag has no value for bytecode");
    }
}
