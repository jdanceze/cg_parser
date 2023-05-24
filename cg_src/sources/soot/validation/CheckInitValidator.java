package soot.validation;

import java.util.Iterator;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.toolkits.exceptions.ThrowAnalysisFactory;
import soot.toolkits.graph.ExceptionalUnitGraph;
import soot.toolkits.graph.ExceptionalUnitGraphFactory;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.InitAnalysis;
/* loaded from: gencallgraphv3.jar:soot/validation/CheckInitValidator.class */
public enum CheckInitValidator implements BodyValidator {
    INSTANCE;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static CheckInitValidator[] valuesCustom() {
        CheckInitValidator[] valuesCustom = values();
        int length = valuesCustom.length;
        CheckInitValidator[] checkInitValidatorArr = new CheckInitValidator[length];
        System.arraycopy(valuesCustom, 0, checkInitValidatorArr, 0, length);
        return checkInitValidatorArr;
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public /* bridge */ /* synthetic */ void validate(Body body, List list) {
        validate(body, (List<ValidationException>) list);
    }

    public static CheckInitValidator v() {
        return INSTANCE;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.validation.BodyValidator
    public void validate(Body body, List<ValidationException> exception) {
        ExceptionalUnitGraph g = ExceptionalUnitGraphFactory.createExceptionalUnitGraph(body, ThrowAnalysisFactory.checkInitThrowAnalysis(), false);
        InitAnalysis analysis = new InitAnalysis(g);
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit s = it.next();
            FlowSet<Local> init = analysis.getFlowBefore(s);
            for (ValueBox vBox : s.getUseBoxes()) {
                Value v = vBox.getValue();
                if (v instanceof Local) {
                    Local l = (Local) v;
                    if (!init.contains(l)) {
                        exception.add(new ValidationException(s, "Local variable " + l.getName() + " is not definitively defined at this point", "Warning: Local variable " + l + " not definitely defined at " + s + " in " + body.getMethod()));
                    }
                }
            }
        }
    }

    @Override // soot.validation.BodyValidator, soot.validation.Validator
    public boolean isBasicValidator() {
        return false;
    }
}
