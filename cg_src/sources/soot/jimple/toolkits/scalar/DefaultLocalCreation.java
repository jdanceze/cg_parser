package soot.jimple.toolkits.scalar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import soot.Local;
import soot.Type;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/DefaultLocalCreation.class */
public class DefaultLocalCreation extends LocalCreation {
    public static final String DEFAULT_PREFIX = "soot";
    private final Set<String> locals;
    private int counter;

    public DefaultLocalCreation(Collection<Local> locals) {
        this(locals, DEFAULT_PREFIX);
    }

    public DefaultLocalCreation(Collection<Local> locals, String prefix) {
        super(locals, prefix);
        this.locals = new HashSet(locals.size());
        for (Local l : locals) {
            this.locals.add(l.getName());
        }
        this.counter = 0;
    }

    @Override // soot.jimple.toolkits.scalar.LocalCreation
    public Local newLocal(Type type) {
        return newLocal(this.prefix, type);
    }

    @Override // soot.jimple.toolkits.scalar.LocalCreation
    public Local newLocal(String prefix, Type type) {
        int suffix = prefix.equals(this.prefix) ? this.counter : 0;
        while (this.locals.contains(String.valueOf(prefix) + suffix)) {
            suffix++;
        }
        if (prefix.equals(this.prefix)) {
            this.counter = suffix + 1;
        }
        String newName = String.valueOf(prefix) + suffix;
        Local newLocal = Jimple.v().newLocal(newName, type);
        this.localChain.add(newLocal);
        this.locals.add(newName);
        return newLocal;
    }
}
