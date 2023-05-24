package soot.validation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.G;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.toolkits.exceptions.PedanticThrowAnalysis;
import soot.toolkits.exceptions.ThrowAnalysis;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
/* loaded from: gencallgraphv3.jar:soot/validation/UsesValidator.class */
public enum UsesValidator implements BodyValidator {
    INSTANCE;
    
    static final /* synthetic */ boolean $assertionsDisabled;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static UsesValidator[] valuesCustom() {
        UsesValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        UsesValidator[] usesValidatorArr = new UsesValidator[length];
        System.arraycopy(valuesCustom, 0, usesValidatorArr, 0, length);
        return usesValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    static {
        $assertionsDisabled = !UsesValidator.class.desiredAssertionStatus();
    }

    public static UsesValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        ThrowAnalysis throwAnalysis = PedanticThrowAnalysis.v();
        UnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, throwAnalysis, false);
        LocalDefs ld = G.v().soot_toolkits_scalar_LocalDefsFactory().newLocalDefs(g, true);
        Collection<Local> locals = body.getLocals();
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            for (ValueBox box : u.getUseBoxes()) {
                Value v = box.getValue();
                if (v instanceof Local) {
                    Local l = (Local) v;
                    if (!locals.contains(l)) {
                        String msg = "Local " + v + " is referenced here but not in body's local-chain. (" + body.getMethod() + ")";
                        exception.add(new ValidationException(u, msg, msg));
                    }
                    if (!ld.getDefsOfAt(l, u).isEmpty()) {
                        continue;
                    } else if (!$assertionsDisabled && !graphEdgesAreValid(g, u)) {
                        throw new AssertionError("broken graph found: " + u);
                    } else {
                        exception.add(new ValidationException(u, "There is no path from a definition of " + v + " to this statement.", "(" + body.getMethod() + ") no defs for value: " + l + "!"));
                    }
                }
            }
        }
    }

    private boolean graphEdgesAreValid(UnitGraph g, Unit u) {
        for (Unit p : g.getPredsOf(u)) {
            if (!g.getSuccsOf(p).contains(u)) {
                return false;
            }
        }
        return true;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return false;
    }
}
