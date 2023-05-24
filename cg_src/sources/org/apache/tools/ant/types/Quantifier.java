package org.apache.tools.ant.types;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Quantifier.class */
public class Quantifier extends EnumeratedAttribute {
    private static final String[] VALUES = (String[]) Stream.of((Object[]) Predicate.values()).map((v0) -> {
        return v0.getNames();
    }).flatMap((v0) -> {
        return v0.stream();
    }).toArray(x$0 -> {
        return new String[x$0];
    });
    public static final Quantifier ALL = new Quantifier(Predicate.ALL);
    public static final Quantifier ANY = new Quantifier(Predicate.ANY);
    public static final Quantifier ONE = new Quantifier(Predicate.ONE);
    public static final Quantifier MAJORITY = new Quantifier(Predicate.MAJORITY);
    public static final Quantifier NONE = new Quantifier(Predicate.NONE);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Quantifier$Predicate.class */
    public enum Predicate {
        ALL("all", "each", "every") { // from class: org.apache.tools.ant.types.Quantifier.Predicate.1
            @Override // org.apache.tools.ant.types.Quantifier.Predicate
            boolean eval(int t, int f) {
                return f == 0;
            }
        },
        ANY("any", "some") { // from class: org.apache.tools.ant.types.Quantifier.Predicate.2
            @Override // org.apache.tools.ant.types.Quantifier.Predicate
            boolean eval(int t, int f) {
                return t > 0;
            }
        },
        ONE("one", new String[0]) { // from class: org.apache.tools.ant.types.Quantifier.Predicate.3
            @Override // org.apache.tools.ant.types.Quantifier.Predicate
            boolean eval(int t, int f) {
                return t == 1;
            }
        },
        MAJORITY("majority", "most") { // from class: org.apache.tools.ant.types.Quantifier.Predicate.4
            @Override // org.apache.tools.ant.types.Quantifier.Predicate
            boolean eval(int t, int f) {
                return t > f;
            }
        },
        NONE("none", new String[0]) { // from class: org.apache.tools.ant.types.Quantifier.Predicate.5
            @Override // org.apache.tools.ant.types.Quantifier.Predicate
            boolean eval(int t, int f) {
                return t == 0;
            }
        };
        
        final Set<String> names;

        abstract boolean eval(int i, int i2);

        static Predicate get(String name) {
            return (Predicate) Stream.of((Object[]) values()).filter(p -> {
                return p.names.contains(name);
            }).findFirst().orElseThrow(() -> {
                return new IllegalArgumentException(name);
            });
        }

        Predicate(String primaryName, String... additionalNames) {
            Set<String> names = new LinkedHashSet<>();
            names.add(primaryName);
            Collections.addAll(names, additionalNames);
            this.names = Collections.unmodifiableSet(names);
        }

        Set<String> getNames() {
            return this.names;
        }
    }

    public Quantifier() {
    }

    public Quantifier(String value) {
        setValue(value);
    }

    private Quantifier(Predicate impl) {
        setValue(impl.getNames().iterator().next());
    }

    @Override // org.apache.tools.ant.types.EnumeratedAttribute
    public String[] getValues() {
        return VALUES;
    }

    public boolean evaluate(boolean[] b) {
        int t = 0;
        for (boolean bn : b) {
            if (bn) {
                t++;
            }
        }
        return evaluate(t, b.length - t);
    }

    public boolean evaluate(int t, int f) {
        int index = getIndex();
        if (index == -1) {
            throw new BuildException("Quantifier value not set.");
        }
        return Predicate.get(VALUES[index]).eval(t, f);
    }
}
