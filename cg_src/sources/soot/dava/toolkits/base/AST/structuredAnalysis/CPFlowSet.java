package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.util.HashMap;
import soot.dava.DecompilationException;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/CPFlowSet.class */
public class CPFlowSet extends DavaFlowSet<CPTuple> {
    public CPFlowSet() {
    }

    /* JADX WARN: Type inference failed for: r1v6, types: [T[], soot.dava.toolkits.base.AST.structuredAnalysis.CPTuple[]] */
    public CPFlowSet(CPFlowSet other) {
        this.numElements = other.numElements;
        this.maxElements = other.maxElements;
        this.elements = new CPTuple[other.getElementCount()];
        for (int i = 0; i < other.getElementCount(); i++) {
            if (other.getElementAt(i) != null) {
                ((CPTuple[]) this.elements)[i] = other.getElementAt(i).m2535clone();
            } else {
                ((CPTuple[]) this.elements)[i] = null;
            }
        }
        this.breakList = (HashMap) other.breakList.clone();
        this.continueList = (HashMap) other.continueList.clone();
        this.implicitBreaks = (HashMap) other.implicitBreaks.clone();
        this.implicitContinues = (HashMap) other.implicitContinues.clone();
    }

    public Object contains(String className, String localOrField) {
        for (int i = 0; i < this.numElements; i++) {
            CPTuple current = getElementAt(i);
            if (current.getSootClassName().equals(className)) {
                if (current.containsField()) {
                    if (current.getVariable().getSootField().getName().equals(localOrField)) {
                        return current.getValue();
                    }
                } else if (current.containsLocal() && current.getVariable().getLocal().getName().equals(localOrField)) {
                    return current.getValue();
                }
            }
        }
        return null;
    }

    public void addIfNotPresent(CPTuple newTuple) {
        for (int i = 0; i < this.numElements; i++) {
            CPTuple current = ((CPTuple[]) this.elements)[i];
            if (current.getSootClassName().equals(newTuple.getSootClassName())) {
                CPVariable curVar = current.getVariable();
                CPVariable newTupleVar = newTuple.getVariable();
                if (curVar.equals(newTupleVar)) {
                    current.setValue(newTuple.getValue());
                    return;
                }
            }
        }
        add(newTuple);
    }

    public void addIfNotPresentButDontUpdate(CPTuple newTuple) {
        for (int i = 0; i < this.numElements; i++) {
            CPTuple current = ((CPTuple[]) this.elements)[i];
            if (current.getSootClassName().equals(newTuple.getSootClassName())) {
                CPVariable curVar = current.getVariable();
                CPVariable newTupleVar = newTuple.getVariable();
                if (curVar.equals(newTupleVar)) {
                    if (current.isTop()) {
                        current.setValue(newTuple.getValue());
                        return;
                    }
                    return;
                }
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet, soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void intersection(FlowSet otherFlow, FlowSet destFlow) {
        CPFlowSet workingSet;
        if (!(otherFlow instanceof CPFlowSet) || !(destFlow instanceof CPFlowSet)) {
            super.intersection(otherFlow, destFlow);
            return;
        }
        CPFlowSet other = (CPFlowSet) otherFlow;
        CPFlowSet dest = (CPFlowSet) destFlow;
        if (dest == other || dest == this) {
            workingSet = new CPFlowSet();
        } else {
            workingSet = dest;
            workingSet.clear();
        }
        for (int i = 0; i < this.numElements; i++) {
            CPTuple thisTuple = getElementAt(i);
            String className = thisTuple.getSootClassName();
            CPVariable thisVar = thisTuple.getVariable();
            CPTuple matchFound = null;
            int j = 0;
            while (true) {
                if (j >= other.numElements) {
                    break;
                }
                CPTuple otherTuple = other.getElementAt(j);
                String tempClass = otherTuple.getSootClassName();
                if (!tempClass.equals(className) || !otherTuple.getVariable().equals(thisVar)) {
                    j++;
                } else {
                    matchFound = otherTuple;
                    break;
                }
            }
            if (matchFound != null) {
                if (thisTuple.isTop()) {
                    workingSet.add(thisTuple.m2535clone());
                } else if (matchFound.isTop()) {
                    workingSet.add(matchFound.m2535clone());
                } else if (!matchFound.isTop() && !thisTuple.isTop()) {
                    Object matchedValue = matchFound.getValue();
                    Object thisValue = thisTuple.getValue();
                    if (matchedValue.equals(thisValue)) {
                        workingSet.add(thisTuple.m2535clone());
                    } else {
                        workingSet.add(new CPTuple(className, thisVar, true));
                    }
                } else {
                    throw new DecompilationException("Ran out of cases in CPVariable values...report bug to developer");
                }
            } else {
                workingSet.add(thisTuple.m2535clone());
            }
        }
        for (int i2 = 0; i2 < other.numElements; i2++) {
            CPTuple otherTuple2 = other.getElementAt(i2);
            String otherClassName = otherTuple2.getSootClassName();
            CPVariable otherVar = otherTuple2.getVariable();
            boolean inBoth = false;
            int j2 = 0;
            while (true) {
                if (j2 >= this.numElements) {
                    break;
                }
                CPTuple thisTuple2 = getElementAt(j2);
                String thisClassName = thisTuple2.getSootClassName();
                CPVariable thisVar2 = thisTuple2.getVariable();
                if (!otherClassName.equals(thisClassName) || !thisVar2.equals(otherVar)) {
                    j2++;
                } else {
                    inBoth = true;
                    break;
                }
            }
            if (!inBoth) {
                workingSet.add(otherTuple2.m2535clone());
            }
        }
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet, soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public CPFlowSet clone() {
        return new CPFlowSet(this);
    }

    @Override // soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet, soot.toolkits.scalar.AbstractFlowSet
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append("Printing CPFlowSet: ");
        for (int i = 0; i < this.numElements; i++) {
            b.append("\n" + ((CPTuple[]) this.elements)[i].toString());
        }
        b.append("\n");
        return b.toString();
    }
}
