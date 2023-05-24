package soot;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.tagkit.CodeAttribute;
import soot.tagkit.Tag;
import soot.util.Chain;
import soot.util.EscapedWriter;
import soot.util.HashChain;
import soot.validation.BodyValidator;
import soot.validation.CheckEscapingValidator;
import soot.validation.CheckInitValidator;
import soot.validation.CheckTypesValidator;
import soot.validation.CheckVoidLocalesValidator;
import soot.validation.LocalsValidator;
import soot.validation.TrapsValidator;
import soot.validation.UnitBoxesValidator;
import soot.validation.UsesValidator;
import soot.validation.ValidationException;
import soot.validation.ValueBoxesValidator;
/* loaded from: gencallgraphv3.jar:soot/Body.class */
public abstract class Body extends AbstractHost implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Body.class);
    protected transient SootMethod method;
    protected Chain<Local> localChain;
    protected Chain<Trap> trapChain;
    protected UnitPatchingChain unitChain;

    public abstract Object clone(boolean z);

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/Body$LazyValidatorsSingleton.class */
    public static class LazyValidatorsSingleton {
        static final BodyValidator[] V = {LocalsValidator.v(), TrapsValidator.v(), UnitBoxesValidator.v(), UsesValidator.v(), ValueBoxesValidator.v(), CheckTypesValidator.v(), CheckVoidLocalesValidator.v(), CheckEscapingValidator.v()};

        private LazyValidatorsSingleton() {
        }
    }

    public Object clone() {
        return clone(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Body(SootMethod m) {
        this.method = null;
        this.localChain = new HashChain();
        this.trapChain = new HashChain();
        this.unitChain = new UnitPatchingChain(new HashChain());
        this.method = m;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Body() {
        this.method = null;
        this.localChain = new HashChain();
        this.trapChain = new HashChain();
        this.unitChain = new UnitPatchingChain(new HashChain());
    }

    public SootMethod getMethod() {
        if (this.method == null) {
            throw new RuntimeException("no method associated w/ body");
        }
        return this.method;
    }

    public SootMethod getMethodUnsafe() {
        return this.method;
    }

    public void setMethod(SootMethod method) {
        this.method = method;
    }

    public int getLocalCount() {
        return this.localChain.size();
    }

    public Map<Object, Object> importBodyContentsFrom(Body b) {
        return importBodyContentsFrom(b, false);
    }

    public Map<Object, Object> importBodyContentsFrom(Body b, boolean noLocalsClone) {
        HashMap<Object, Object> bindings = new HashMap<>();
        Iterator<Unit> it = b.getUnits().iterator();
        while (it.hasNext()) {
            Unit original = it.next();
            Unit copy = (Unit) original.clone();
            copy.addAllTagsOf(original);
            this.unitChain.addLast((UnitPatchingChain) copy);
            bindings.put(original, copy);
        }
        for (Trap original2 : b.getTraps()) {
            Trap copy2 = (Trap) original2.clone();
            this.trapChain.addLast(copy2);
            bindings.put(original2, copy2);
        }
        if (!noLocalsClone) {
            for (Local original3 : b.getLocals()) {
                Local copy3 = (Local) original3.clone();
                this.localChain.addLast(copy3);
                bindings.put(original3, copy3);
            }
        } else {
            this.localChain.addAll(b.getLocals());
        }
        for (UnitBox box : getAllUnitBoxes()) {
            Unit newObject = (Unit) bindings.get(box.getUnit());
            if (newObject != null) {
                box.setUnit(newObject);
            }
        }
        if (!noLocalsClone) {
            for (ValueBox vb : getUseBoxes()) {
                Value val = vb.getValue();
                if (val instanceof Local) {
                    vb.setValue((Value) bindings.get(val));
                }
            }
            for (ValueBox vb2 : getDefBoxes()) {
                Value val2 = vb2.getValue();
                if (val2 instanceof Local) {
                    vb2.setValue((Value) bindings.get(val2));
                }
            }
        }
        return bindings;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void runValidation(BodyValidator validator) {
        List<ValidationException> exceptionList = new ArrayList<>();
        validator.validate(this, exceptionList);
        if (!exceptionList.isEmpty()) {
            throw exceptionList.get(0);
        }
    }

    public void validate() {
        List<ValidationException> exceptionList = new ArrayList<>();
        validate(exceptionList);
        if (!exceptionList.isEmpty()) {
            throw exceptionList.get(0);
        }
    }

    public void validate(List<ValidationException> exceptionList) {
        BodyValidator[] bodyValidatorArr;
        boolean runAllValidators = Options.v().debug() || Options.v().validate();
        for (BodyValidator validator : LazyValidatorsSingleton.V) {
            if (validator.isBasicValidator() || runAllValidators) {
                validator.validate(this, exceptionList);
            }
        }
    }

    public void validateValueBoxes() {
        runValidation(ValueBoxesValidator.v());
    }

    public void validateLocals() {
        runValidation(LocalsValidator.v());
    }

    public void validateTraps() {
        runValidation(TrapsValidator.v());
    }

    public void validateUnitBoxes() {
        runValidation(UnitBoxesValidator.v());
    }

    public void validateUses() {
        runValidation(UsesValidator.v());
    }

    public void checkInit() {
        runValidation(CheckInitValidator.v());
    }

    public Chain<Local> getLocals() {
        return this.localChain;
    }

    public Chain<Trap> getTraps() {
        return this.trapChain;
    }

    public Unit getThisUnit() {
        Iterator<Unit> it = getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if ((u instanceof IdentityStmt) && (((IdentityStmt) u).getRightOp() instanceof ThisRef)) {
                return u;
            }
        }
        throw new RuntimeException("couldn't find this-assignment! in " + getMethod());
    }

    public Local getThisLocal() {
        return (Local) ((IdentityStmt) getThisUnit()).getLeftOp();
    }

    public Local getParameterLocal(int i) {
        Iterator<Unit> it = getUnits().iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            if (s instanceof IdentityStmt) {
                IdentityStmt is = (IdentityStmt) s;
                Value rightOp = is.getRightOp();
                if (rightOp instanceof ParameterRef) {
                    ParameterRef pr = (ParameterRef) rightOp;
                    if (pr.getIndex() == i) {
                        return (Local) is.getLeftOp();
                    }
                } else {
                    continue;
                }
            }
        }
        throw new RuntimeException("couldn't find parameterref" + i + " in " + getMethod());
    }

    public List<Local> getParameterLocals() {
        int numParams = getMethod().getParameterCount();
        Local[] res = new Local[numParams];
        int numFound = 0;
        Iterator<Unit> it = getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof IdentityStmt) {
                IdentityStmt is = (IdentityStmt) u;
                Value rightOp = is.getRightOp();
                if (rightOp instanceof ParameterRef) {
                    int idx = ((ParameterRef) rightOp).getIndex();
                    if (res[idx] != null) {
                        throw new RuntimeException("duplicate parameterref" + idx + " in " + getMethod());
                    }
                    res[idx] = (Local) is.getLeftOp();
                    numFound++;
                    if (numFound >= numParams) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        if (numFound != numParams) {
            for (int i = 0; i < numParams; i++) {
                if (res[i] == null) {
                    throw new RuntimeException("couldn't find parameterref" + i + " in " + getMethod());
                }
            }
            throw new RuntimeException("couldn't find parameterref? in " + getMethod());
        }
        return Arrays.asList(res);
    }

    public List<Value> getParameterRefs() {
        int numParams = getMethod().getParameterCount();
        Value[] res = new Value[numParams];
        int numFound = 0;
        Iterator<Unit> it = getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof IdentityStmt) {
                Value rightOp = ((IdentityStmt) u).getRightOp();
                if (rightOp instanceof ParameterRef) {
                    ParameterRef pr = (ParameterRef) rightOp;
                    int idx = pr.getIndex();
                    if (res[idx] != null) {
                        throw new RuntimeException("duplicate parameterref" + idx + " in " + getMethod());
                    }
                    res[idx] = pr;
                    numFound++;
                    if (numFound >= numParams) {
                        break;
                    }
                } else {
                    continue;
                }
            }
        }
        return Arrays.asList(res);
    }

    public UnitPatchingChain getUnits() {
        return this.unitChain;
    }

    public List<UnitBox> getAllUnitBoxes() {
        ArrayList<UnitBox> unitBoxList = new ArrayList<>();
        Iterator<Unit> it = this.unitChain.iterator();
        while (it.hasNext()) {
            Unit item = it.next();
            unitBoxList.addAll(item.getUnitBoxes());
        }
        for (Trap item2 : this.trapChain) {
            unitBoxList.addAll(item2.getUnitBoxes());
        }
        for (Tag t : getTags()) {
            if (t instanceof CodeAttribute) {
                unitBoxList.addAll(((CodeAttribute) t).getUnitBoxes());
            }
        }
        return unitBoxList;
    }

    public List<UnitBox> getUnitBoxes(boolean branchTarget) {
        ArrayList<UnitBox> unitBoxList = new ArrayList<>();
        Iterator<Unit> it = this.unitChain.iterator();
        while (it.hasNext()) {
            Unit item = it.next();
            if (item.branches() == branchTarget) {
                unitBoxList.addAll(item.getUnitBoxes());
            }
        }
        for (Trap item2 : this.trapChain) {
            unitBoxList.addAll(item2.getUnitBoxes());
        }
        for (Tag t : getTags()) {
            if (t instanceof CodeAttribute) {
                unitBoxList.addAll(((CodeAttribute) t).getUnitBoxes());
            }
        }
        return unitBoxList;
    }

    public List<ValueBox> getUseBoxes() {
        ArrayList<ValueBox> useBoxList = new ArrayList<>();
        Iterator<Unit> it = this.unitChain.iterator();
        while (it.hasNext()) {
            Unit item = it.next();
            useBoxList.addAll(item.getUseBoxes());
        }
        return useBoxList;
    }

    public List<ValueBox> getDefBoxes() {
        ArrayList<ValueBox> defBoxList = new ArrayList<>();
        Iterator<Unit> it = this.unitChain.iterator();
        while (it.hasNext()) {
            Unit item = it.next();
            defBoxList.addAll(item.getDefBoxes());
        }
        return defBoxList;
    }

    public List<ValueBox> getUseAndDefBoxes() {
        ArrayList<ValueBox> useAndDefBoxList = new ArrayList<>();
        Iterator<Unit> it = this.unitChain.iterator();
        while (it.hasNext()) {
            Unit item = it.next();
            useAndDefBoxList.addAll(item.getUseBoxes());
            useAndDefBoxList.addAll(item.getDefBoxes());
        }
        return useAndDefBoxList;
    }

    public String toString() {
        ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
        try {
            PrintWriter writerOut = new PrintWriter(new EscapedWriter(new OutputStreamWriter(streamOut)));
            try {
                Printer.v().printTo(this, writerOut);
                writerOut.flush();
                if (writerOut != null) {
                    writerOut.close();
                }
            } catch (Throwable th) {
                if (writerOut != null) {
                    writerOut.close();
                }
                throw th;
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), (Throwable) e);
        }
        return streamOut.toString();
    }

    public long getModificationCount() {
        return this.localChain.getModificationCount() + this.unitChain.getModificationCount() + this.trapChain.getModificationCount();
    }
}
