package soot.tools;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.JavaBasicTypes;
import soot.Main;
import soot.PackManager;
import soot.PrimType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Transform;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/tools/BadFields.class */
public class BadFields extends SceneTransformer {
    private static final Logger logger = LoggerFactory.getLogger(BadFields.class);
    private SootClass lastClass;
    private SootClass currentClass;

    public static void main(String[] args) {
        PackManager.v().getPack("cg").add(new Transform("cg.badfields", new BadFields()));
        Main.main(args);
    }

    @Override // soot.SceneTransformer
    protected void internalTransform(String phaseName, Map<String, String> options) {
        this.lastClass = null;
        for (SootClass cl : Scene.v().getApplicationClasses()) {
            this.currentClass = cl;
            handleClass(cl);
            Iterator<SootMethod> it = cl.methodIterator();
            while (it.hasNext()) {
                handleMethod(it.next());
            }
        }
        Scene.v().setCallGraph(Scene.v().internalMakeCallGraph());
    }

    private void handleClass(SootClass cl) {
        for (SootField f : cl.getFields()) {
            if (f.isStatic()) {
                String typeName = f.getType().toString();
                if (!typeName.equals("java.lang.Class") && (!f.isFinal() || (!(f.getType() instanceof PrimType) && !typeName.equals("java.io.PrintStream") && !typeName.equals("java.lang.String") && !typeName.equals(Scene.v().getObjectType().toString()) && !typeName.equals(JavaBasicTypes.JAVA_LANG_INTEGER) && !typeName.equals(JavaBasicTypes.JAVA_LANG_BOOLEAN)))) {
                    warn("Bad field " + f);
                }
            }
        }
    }

    private void warn(String warning) {
        if (this.lastClass != this.currentClass) {
            logger.debug("In class " + this.currentClass);
        }
        this.lastClass = this.currentClass;
        logger.debug("  " + warning);
    }

    private void handleMethod(SootMethod m) {
        if (!m.isConcrete()) {
            return;
        }
        for (ValueBox b : m.retrieveActiveBody().getUseAndDefBoxes()) {
            Value v = b.getValue();
            if (v instanceof StaticFieldRef) {
                StaticFieldRef sfr = (StaticFieldRef) v;
                SootField f = sfr.getField();
                if (f.getDeclaringClass().getName().equals("java.lang.System")) {
                    if (f.getName().equals("err")) {
                        logger.debug("Use of System.err in " + m);
                    }
                    if (f.getName().equals("out")) {
                        logger.debug("Use of System.out in " + m);
                    }
                }
            }
        }
        Iterator<Unit> sIt = m.getActiveBody().getUnits().iterator();
        while (sIt.hasNext()) {
            Stmt s = (Stmt) sIt.next();
            if (s.containsInvokeExpr()) {
                InvokeExpr ie = s.getInvokeExpr();
                SootMethod target = ie.getMethod();
                if (target.getDeclaringClass().getName().equals("java.lang.System") && target.getName().equals("exit")) {
                    warn(m + " calls System.exit");
                }
            }
        }
        if (m.getName().equals("<clinit>")) {
            Iterator<Unit> sIt2 = m.getActiveBody().getUnits().iterator();
            while (sIt2.hasNext()) {
                Stmt s2 = (Stmt) sIt2.next();
                for (ValueBox b2 : s2.getUseBoxes()) {
                    Value v2 = b2.getValue();
                    if (v2 instanceof FieldRef) {
                        warn(String.valueOf(m.getName()) + " reads field " + v2);
                    }
                }
                if (s2.containsInvokeExpr()) {
                    InvokeExpr ie2 = s2.getInvokeExpr();
                    calls(ie2.getMethod());
                }
            }
        }
    }

    private void calls(SootMethod target) {
        if (target.getName().equals("<init>") && (target.getDeclaringClass().getName().equals("java.io.PrintStream") || target.getDeclaringClass().getName().equals(JavaBasicTypes.JAVA_LANG_BOOLEAN) || target.getDeclaringClass().getName().equals(JavaBasicTypes.JAVA_LANG_INTEGER) || target.getDeclaringClass().getName().equals("java.lang.String") || target.getDeclaringClass().getName().equals(Scene.v().getObjectType().toString()))) {
            return;
        }
        if (target.getName().equals("getProperty") && target.getDeclaringClass().getName().equals("java.lang.System")) {
            return;
        }
        if (target.getName().equals("charAt") && target.getDeclaringClass().getName().equals("java.lang.String")) {
            return;
        }
        warn("<clinit> invokes " + target);
    }
}
