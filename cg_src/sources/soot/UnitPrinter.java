package soot;

import soot.jimple.Constant;
import soot.jimple.IdentityRef;
/* loaded from: gencallgraphv3.jar:soot/UnitPrinter.class */
public interface UnitPrinter {
    void startUnit(Unit unit);

    void endUnit(Unit unit);

    void startUnitBox(UnitBox unitBox);

    void endUnitBox(UnitBox unitBox);

    void startValueBox(ValueBox valueBox);

    void endValueBox(ValueBox valueBox);

    void incIndent();

    void decIndent();

    void noIndent();

    void setIndent(String str);

    String getIndent();

    void literal(String str);

    void newline();

    void local(Local local);

    void type(Type type);

    void methodRef(SootMethodRef sootMethodRef);

    void constant(Constant constant);

    void fieldRef(SootFieldRef sootFieldRef);

    void unitRef(Unit unit, boolean z);

    void identityRef(IdentityRef identityRef);

    void setPositionTagger(AttributesUnitPrinter attributesUnitPrinter);

    AttributesUnitPrinter getPositionTagger();

    StringBuffer output();
}
