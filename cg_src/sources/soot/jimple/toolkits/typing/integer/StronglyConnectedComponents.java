package soot.jimple.toolkits.typing.integer;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/integer/StronglyConnectedComponents.class */
class StronglyConnectedComponents {
    private static final Logger logger = LoggerFactory.getLogger(StronglyConnectedComponents.class);
    private static final boolean DEBUG = false;
    private final Set<TypeVariable> black = new TreeSet();
    private final List<TypeVariable> finished = new LinkedList();
    private List<TypeVariable> current_tree;

    public static void merge(List<TypeVariable> typeVariableList) throws TypeException {
        new StronglyConnectedComponents(typeVariableList);
    }

    private StronglyConnectedComponents(List<TypeVariable> typeVariableList) throws TypeException {
        this.current_tree = new LinkedList();
        for (TypeVariable var : typeVariableList) {
            if (!this.black.contains(var)) {
                this.black.add(var);
                dfsg_visit(var);
            }
        }
        this.black.clear();
        List<List<TypeVariable>> forest = new LinkedList<>();
        for (TypeVariable var2 : this.finished) {
            if (!this.black.contains(var2)) {
                this.current_tree = new LinkedList();
                forest.add(this.current_tree);
                this.black.add(var2);
                dfsgt_visit(var2);
            }
        }
        for (List<TypeVariable> list : forest) {
            TypeVariable previous = null;
            for (TypeVariable current : list) {
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

    private void dfsg_visit(TypeVariable var) {
        for (TypeVariable parent : var.parents()) {
            if (!this.black.contains(parent)) {
                this.black.add(parent);
                dfsg_visit(parent);
            }
        }
        this.finished.add(0, var);
    }

    private void dfsgt_visit(TypeVariable var) {
        this.current_tree.add(var);
        for (TypeVariable child : var.children()) {
            if (!this.black.contains(child)) {
                this.black.add(child);
                dfsgt_visit(child);
            }
        }
    }
}
