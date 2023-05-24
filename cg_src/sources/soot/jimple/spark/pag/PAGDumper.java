package soot.jimple.spark.pag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.coffi.Instruction;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;
import soot.jimple.spark.solver.TopoSorter;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/PAGDumper.class */
public class PAGDumper {
    private static final Logger logger = LoggerFactory.getLogger(PAGDumper.class);
    protected PAG pag;
    protected String output_dir;
    protected int fieldNum = 0;
    protected HashMap<SparkField, Integer> fieldMap = new HashMap<>();
    protected ObjectNumberer root = new ObjectNumberer(null, 0);

    public PAGDumper(PAG pag, String output_dir) {
        this.pag = pag;
        this.output_dir = output_dir;
    }

    public void dumpPointsToSets() {
        PointsToSetInternal p2set;
        try {
            final PrintWriter file = new PrintWriter(new FileOutputStream(new File(this.output_dir, "solution")));
            file.println("Solution:");
            Iterator vnIt = this.pag.getVarNodeNumberer().iterator();
            while (vnIt.hasNext()) {
                final VarNode vn = vnIt.next();
                if (vn.getReplacement() == vn && (p2set = vn.getP2Set()) != null) {
                    p2set.forall(new P2SetVisitor() { // from class: soot.jimple.spark.pag.PAGDumper.1
                        @Override // soot.jimple.spark.sets.P2SetVisitor
                        public final void visit(Node n) {
                            try {
                                PAGDumper.this.dumpNode(vn, file);
                                file.print(Instruction.argsep);
                                PAGDumper.this.dumpNode(n, file);
                                file.println("");
                            } catch (IOException e) {
                                throw new RuntimeException("Couldn't dump solution." + e);
                            }
                        }
                    });
                }
            }
            file.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't dump solution." + e);
        }
    }

    public void dump() {
        try {
            PrintWriter file = new PrintWriter(new FileOutputStream(new File(this.output_dir, "pag")));
            if (this.pag.getOpts().topo_sort()) {
                new TopoSorter(this.pag, false).sort();
            }
            file.println("Allocations:");
            for (Object object : this.pag.allocSources()) {
                AllocNode n = (AllocNode) object;
                if (n.getReplacement() == n) {
                    Node[] succs = this.pag.allocLookup(n);
                    for (Node element0 : succs) {
                        dumpNode(n, file);
                        file.print(Instruction.argsep);
                        dumpNode(element0, file);
                        file.println("");
                    }
                }
            }
            file.println("Assignments:");
            for (Object object2 : this.pag.simpleSources()) {
                VarNode n2 = (VarNode) object2;
                if (n2.getReplacement() == n2) {
                    Node[] succs2 = this.pag.simpleLookup(n2);
                    for (Node element02 : succs2) {
                        dumpNode(n2, file);
                        file.print(Instruction.argsep);
                        dumpNode(element02, file);
                        file.println("");
                    }
                }
            }
            file.println("Loads:");
            for (Object object3 : this.pag.loadSources()) {
                FieldRefNode n3 = (FieldRefNode) object3;
                Node[] succs3 = this.pag.loadLookup(n3);
                for (Node element03 : succs3) {
                    dumpNode(n3, file);
                    file.print(Instruction.argsep);
                    dumpNode(element03, file);
                    file.println("");
                }
            }
            file.println("Stores:");
            for (Object object4 : this.pag.storeSources()) {
                VarNode n4 = (VarNode) object4;
                if (n4.getReplacement() == n4) {
                    Node[] succs4 = this.pag.storeLookup(n4);
                    for (Node element04 : succs4) {
                        dumpNode(n4, file);
                        file.print(Instruction.argsep);
                        dumpNode(element04, file);
                        file.println("");
                    }
                }
            }
            if (this.pag.getOpts().dump_types()) {
                dumpTypes(file);
            }
            file.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't dump PAG." + e);
        }
    }

    protected void dumpTypes(PrintWriter file) throws IOException {
        Type t;
        HashSet<Type> declaredTypes = new HashSet<>();
        HashSet<Type> actualTypes = new HashSet<>();
        HashSet<SparkField> allFields = new HashSet<>();
        Iterator nIt = this.pag.getVarNodeNumberer().iterator();
        while (nIt.hasNext()) {
            Type t2 = nIt.next().getType();
            if (t2 != null) {
                declaredTypes.add(t2);
            }
        }
        for (Node object : this.pag.loadSources()) {
            Node n = object;
            if (n.getReplacement() == n) {
                Type t3 = n.getType();
                if (t3 != null) {
                    declaredTypes.add(t3);
                }
                allFields.add(((FieldRefNode) n).getField());
            }
        }
        for (Node object2 : this.pag.storeInvSources()) {
            Node n2 = object2;
            if (n2.getReplacement() == n2) {
                Type t4 = n2.getType();
                if (t4 != null) {
                    declaredTypes.add(t4);
                }
                allFields.add(((FieldRefNode) n2).getField());
            }
        }
        for (Object object3 : this.pag.allocSources()) {
            Node n3 = (Node) object3;
            if (n3.getReplacement() == n3 && (t = n3.getType()) != null) {
                actualTypes.add(t);
            }
        }
        HashMap<Type, Integer> typeToInt = new HashMap<>();
        int nextint = 1;
        Iterator<Type> it = declaredTypes.iterator();
        while (it.hasNext()) {
            Type type = it.next();
            int i = nextint;
            nextint++;
            typeToInt.put(type, new Integer(i));
        }
        Iterator<Type> it2 = actualTypes.iterator();
        while (it2.hasNext()) {
            Type t5 = it2.next();
            if (!typeToInt.containsKey(t5)) {
                int i2 = nextint;
                nextint++;
                typeToInt.put(t5, new Integer(i2));
            }
        }
        file.println("Declared Types:");
        Iterator<Type> it3 = declaredTypes.iterator();
        while (it3.hasNext()) {
            Type declType = it3.next();
            Iterator<Type> it4 = actualTypes.iterator();
            while (it4.hasNext()) {
                Type actType = it4.next();
                if (this.pag.getTypeManager().castNeverFails(actType, declType)) {
                    file.println(typeToInt.get(declType) + Instruction.argsep + typeToInt.get(actType));
                }
            }
        }
        file.println("Allocation Types:");
        for (Node object4 : this.pag.allocSources()) {
            Node n4 = object4;
            if (n4.getReplacement() == n4) {
                Type t6 = n4.getType();
                dumpNode(n4, file);
                if (t6 == null) {
                    throw new RuntimeException("allocnode with null type");
                }
                file.println(Instruction.argsep + typeToInt.get(t6));
            }
        }
        file.println("Variable Types:");
        Iterator nIt2 = this.pag.getVarNodeNumberer().iterator();
        while (nIt2.hasNext()) {
            VarNode n5 = nIt2.next();
            if (n5.getReplacement() == n5) {
                Type t7 = n5.getType();
                dumpNode(n5, file);
                if (t7 == null) {
                    file.println(" 0");
                } else {
                    file.println(Instruction.argsep + typeToInt.get(t7));
                }
            }
        }
    }

    protected int fieldToNum(SparkField f) {
        Integer ret = this.fieldMap.get(f);
        if (ret == null) {
            int i = this.fieldNum + 1;
            this.fieldNum = i;
            ret = new Integer(i);
            this.fieldMap.put(f, ret);
        }
        return ret.intValue();
    }

    protected void dumpNode(Node n, PrintWriter out) throws IOException {
        if (n.getReplacement() != n) {
            throw new RuntimeException("Attempt to dump collapsed node.");
        }
        if (n instanceof FieldRefNode) {
            FieldRefNode fn = (FieldRefNode) n;
            dumpNode(fn.getBase(), out);
            out.print(Instruction.argsep + fieldToNum(fn.getField()));
        } else if (!this.pag.getOpts().class_method_var() || !(n instanceof VarNode)) {
            if (this.pag.getOpts().topo_sort() && (n instanceof VarNode)) {
                out.print(new StringBuilder().append(((VarNode) n).finishingNumber).toString());
            } else {
                out.print(new StringBuilder().append(n.getNumber()).toString());
            }
        } else {
            VarNode vn = (VarNode) n;
            SootMethod m = null;
            if (vn instanceof LocalVarNode) {
                m = ((LocalVarNode) vn).getMethod();
            }
            SootClass c = null;
            if (m != null) {
                c = m.getDeclaringClass();
            }
            ObjectNumberer cl = this.root.findOrAdd(c);
            ObjectNumberer me = cl.findOrAdd(m);
            ObjectNumberer vr = me.findOrAdd(vn);
            out.print(cl.num + Instruction.argsep + me.num + Instruction.argsep + vr.num);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/spark/pag/PAGDumper$ObjectNumberer.class */
    public class ObjectNumberer {
        Object o;
        int num;
        int nextChildNum = 1;
        HashMap<Object, ObjectNumberer> children = null;

        ObjectNumberer(Object o, int num) {
            this.o = null;
            this.num = 0;
            this.o = o;
            this.num = num;
        }

        ObjectNumberer findOrAdd(Object child) {
            if (this.children == null) {
                this.children = new HashMap<>();
            }
            ObjectNumberer ret = this.children.get(child);
            if (ret == null) {
                PAGDumper pAGDumper = PAGDumper.this;
                int i = this.nextChildNum;
                this.nextChildNum = i + 1;
                ret = new ObjectNumberer(child, i);
                this.children.put(child, ret);
            }
            return ret;
        }
    }
}
