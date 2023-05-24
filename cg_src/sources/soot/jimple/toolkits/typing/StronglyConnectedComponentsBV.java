package soot.jimple.toolkits.typing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.util.BitSetIterator;
import soot.util.BitVector;
/* JADX INFO: Access modifiers changed from: package-private */
@Deprecated
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/StronglyConnectedComponentsBV.class */
public class StronglyConnectedComponentsBV {
    private static final Logger logger = LoggerFactory.getLogger(StronglyConnectedComponentsBV.class);
    BitVector variables;
    Set<TypeVariableBV> black;
    TypeResolverBV resolver;
    LinkedList<TypeVariableBV> current_tree;
    private static final boolean DEBUG = false;
    LinkedList<LinkedList<TypeVariableBV>> forest = new LinkedList<>();
    LinkedList<TypeVariableBV> finished = new LinkedList<>();

    public StronglyConnectedComponentsBV(BitVector typeVariableList, TypeResolverBV resolver) throws TypeException {
        this.resolver = resolver;
        this.variables = typeVariableList;
        this.black = new TreeSet();
        BitSetIterator i = this.variables.iterator();
        while (i.hasNext()) {
            TypeVariableBV var = resolver.typeVariableForId(i.next());
            if (!this.black.contains(var)) {
                this.black.add(var);
                dfsg_visit(var);
            }
        }
        this.black = new TreeSet();
        Iterator<TypeVariableBV> it = this.finished.iterator();
        while (it.hasNext()) {
            TypeVariableBV var2 = it.next();
            if (!this.black.contains(var2)) {
                this.current_tree = new LinkedList<>();
                this.forest.add(this.current_tree);
                this.black.add(var2);
                dfsgt_visit(var2);
            }
        }
        Iterator<LinkedList<TypeVariableBV>> i2 = this.forest.iterator();
        while (i2.hasNext()) {
            LinkedList<TypeVariableBV> list = i2.next();
            TypeVariableBV previous = null;
            Iterator<TypeVariableBV> j = list.iterator();
            while (j.hasNext()) {
                TypeVariableBV current = j.next();
                if (previous == null) {
                    previous = current;
                } else {
                    try {
                        previous = previous.union(current);
                    } catch (TypeException e) {
                        throw e;
                    }
                }
            }
        }
    }

    private void dfsg_visit(TypeVariableBV var) {
        BitVector parents = var.parents();
        BitSetIterator i = parents.iterator();
        while (i.hasNext()) {
            TypeVariableBV parent = this.resolver.typeVariableForId(i.next());
            if (!this.black.contains(parent)) {
                this.black.add(parent);
                dfsg_visit(parent);
            }
        }
        this.finished.add(0, var);
    }

    private void dfsgt_visit(TypeVariableBV var) {
        this.current_tree.add(var);
        BitVector children = var.children();
        BitSetIterator i = children.iterator();
        while (i.hasNext()) {
            TypeVariableBV child = this.resolver.typeVariableForId(i.next());
            if (!this.black.contains(child)) {
                this.black.add(child);
                dfsgt_visit(child);
            }
        }
    }
}
