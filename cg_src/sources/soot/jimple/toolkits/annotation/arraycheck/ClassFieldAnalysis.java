package soot.jimple.toolkits.annotation.arraycheck;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.ArrayType;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IntConstant;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/ClassFieldAnalysis.class */
public class ClassFieldAnalysis {
    private static final Logger logger = LoggerFactory.getLogger(ClassFieldAnalysis.class);
    private final boolean final_in = true;
    private final boolean private_in = true;
    private final Map<SootClass, Hashtable<SootField, IntValueContainer>> classToFieldInfoMap = new HashMap();

    public ClassFieldAnalysis(Singletons.Global g) {
    }

    public static ClassFieldAnalysis v() {
        return G.v().soot_jimple_toolkits_annotation_arraycheck_ClassFieldAnalysis();
    }

    protected void internalTransform(SootClass c) {
        if (this.classToFieldInfoMap.containsKey(c)) {
            return;
        }
        Date start = new Date();
        if (Options.v().verbose()) {
            logger.debug("[] ClassFieldAnalysis started on : " + start + " for " + c.getPackageName() + c.getName());
        }
        Hashtable<SootField, IntValueContainer> fieldInfoTable = new Hashtable<>();
        this.classToFieldInfoMap.put(c, fieldInfoTable);
        HashSet<SootField> candidSet = new HashSet<>();
        int arrayTypeFieldNum = 0;
        for (SootField field : c.getFields()) {
            int modifiers = field.getModifiers();
            Type type = field.getType();
            if ((type instanceof ArrayType) && ((modifiers & 16) != 0 || (modifiers & 2) != 0)) {
                candidSet.add(field);
                arrayTypeFieldNum++;
            }
        }
        if (arrayTypeFieldNum == 0) {
            if (Options.v().verbose()) {
                logger.debug("[] ClassFieldAnalysis finished with nothing");
                return;
            }
            return;
        }
        Iterator<SootMethod> methodIt = c.methodIterator();
        while (methodIt.hasNext()) {
            ScanMethod(methodIt.next(), candidSet, fieldInfoTable);
        }
        Date finish = new Date();
        if (Options.v().verbose()) {
            long runtime = finish.getTime() - start.getTime();
            long mins = runtime / 60000;
            long secs = (runtime % 60000) / 1000;
            logger.debug("[] ClassFieldAnalysis finished normally. It took " + mins + " mins and " + secs + " secs.");
        }
    }

    public Object getFieldInfo(SootField field) {
        SootClass c = field.getDeclaringClass();
        Map<SootField, IntValueContainer> fieldInfoTable = this.classToFieldInfoMap.get(c);
        if (fieldInfoTable == null) {
            internalTransform(c);
            fieldInfoTable = this.classToFieldInfoMap.get(c);
        }
        return fieldInfoTable.get(field);
    }

    public void ScanMethod(SootMethod method, Set<SootField> candidates, Hashtable<SootField, IntValueContainer> fieldinfo) {
        Body body;
        Value size;
        if (!method.isConcrete() || (body = method.retrieveActiveBody()) == null) {
            return;
        }
        boolean hasArrayLocal = false;
        Collection<Local> locals = body.getLocals();
        Iterator<Local> localIt = locals.iterator();
        while (true) {
            if (!localIt.hasNext()) {
                break;
            }
            Local local = localIt.next();
            Type type = local.getType();
            if (type instanceof ArrayType) {
                hasArrayLocal = true;
                break;
            }
        }
        if (!hasArrayLocal) {
            return;
        }
        HashMap<Stmt, SootField> stmtfield = new HashMap<>();
        Iterator<Unit> unitIt = body.getUnits().iterator();
        while (unitIt.hasNext()) {
            Stmt stmt = (Stmt) unitIt.next();
            if (stmt.containsFieldRef()) {
                Value leftOp = ((AssignStmt) stmt).getLeftOp();
                if (leftOp instanceof FieldRef) {
                    FieldRef fref = (FieldRef) leftOp;
                    SootField field = fref.getField();
                    if (candidates.contains(field)) {
                        stmtfield.put(stmt, field);
                    }
                }
            }
        }
        if (stmtfield.size() == 0) {
            return;
        }
        if (Options.v().verbose()) {
            logger.debug("[] ScanMethod for field started.");
        }
        LocalDefs localDefs = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(body);
        Set<Map.Entry<Stmt, SootField>> entries = stmtfield.entrySet();
        for (Map.Entry<Stmt, SootField> entry : entries) {
            Stmt where = entry.getKey();
            SootField which = entry.getValue();
            IntValueContainer length = new IntValueContainer();
            Value rightOp = ((AssignStmt) where).getRightOp();
            if (rightOp instanceof Local) {
                Local local2 = (Local) rightOp;
                DefinitionStmt usestmt = (DefinitionStmt) where;
                while (length.isBottom()) {
                    List<Unit> defs = localDefs.getDefsOfAt(local2, usestmt);
                    if (defs.size() == 1) {
                        usestmt = (DefinitionStmt) defs.get(0);
                        if (Options.v().debug()) {
                            logger.debug("        " + usestmt);
                        }
                        Value tmp_rhs = usestmt.getRightOp();
                        if ((tmp_rhs instanceof NewArrayExpr) || (tmp_rhs instanceof NewMultiArrayExpr)) {
                            if (tmp_rhs instanceof NewArrayExpr) {
                                size = ((NewArrayExpr) tmp_rhs).getSize();
                            } else {
                                size = ((NewMultiArrayExpr) tmp_rhs).getSize(0);
                            }
                            if (size instanceof IntConstant) {
                                length.setValue(((IntConstant) size).value);
                            } else if (size instanceof Local) {
                                local2 = (Local) size;
                            } else {
                                length.setTop();
                            }
                        } else if (tmp_rhs instanceof IntConstant) {
                            length.setValue(((IntConstant) tmp_rhs).value);
                        } else if (tmp_rhs instanceof Local) {
                            local2 = (Local) tmp_rhs;
                        } else {
                            length.setTop();
                        }
                    } else {
                        length.setTop();
                    }
                }
                IntValueContainer oldv = fieldinfo.get(which);
                if (length.isTop()) {
                    if (oldv == null) {
                        fieldinfo.put(which, length.dup());
                    } else {
                        oldv.setTop();
                    }
                    candidates.remove(which);
                } else if (length.isInteger()) {
                    if (oldv == null) {
                        fieldinfo.put(which, length.dup());
                    } else if (oldv.isInteger() && oldv.getValue() != length.getValue()) {
                        oldv.setTop();
                        candidates.remove(which);
                    }
                }
            }
        }
        if (Options.v().verbose()) {
            logger.debug("[] ScanMethod finished.");
        }
    }
}
