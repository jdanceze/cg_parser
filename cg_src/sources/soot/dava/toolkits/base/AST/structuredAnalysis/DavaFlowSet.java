package soot.dava.toolkits.base.AST.structuredAnalysis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.dava.internal.AST.ASTNode;
import soot.dava.internal.SET.SETNodeLabel;
import soot.dava.internal.javaRep.DAbruptStmt;
import soot.dava.toolkits.base.AST.traversals.ClosestAbruptTargetFinder;
import soot.toolkits.scalar.AbstractFlowSet;
import soot.toolkits.scalar.FlowSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/structuredAnalysis/DavaFlowSet.class */
public class DavaFlowSet<T> extends AbstractFlowSet<T> {
    static final int DEFAULT_SIZE = 8;
    int numElements;
    int maxElements;
    protected T[] elements;
    HashMap<Serializable, List<DavaFlowSet<T>>> breakList;
    HashMap<Serializable, List<DavaFlowSet<T>>> continueList;
    HashMap<Serializable, List<DavaFlowSet<T>>> implicitBreaks;
    HashMap<Serializable, List<DavaFlowSet<T>>> implicitContinues;

    public DavaFlowSet() {
        this.maxElements = 8;
        this.elements = (T[]) new Object[8];
        this.numElements = 0;
        this.breakList = new HashMap<>();
        this.continueList = new HashMap<>();
        this.implicitBreaks = new HashMap<>();
        this.implicitContinues = new HashMap<>();
    }

    public DavaFlowSet(DavaFlowSet<T> other) {
        this.numElements = other.numElements;
        this.maxElements = other.maxElements;
        this.elements = (T[]) ((Object[]) other.elements.clone());
        this.breakList = (HashMap) other.breakList.clone();
        this.continueList = (HashMap) other.continueList.clone();
        this.implicitBreaks = (HashMap) other.implicitBreaks.clone();
        this.implicitContinues = (HashMap) other.implicitContinues.clone();
    }

    private boolean sameType(Object flowSet) {
        return flowSet instanceof DavaFlowSet;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    /* renamed from: clone */
    public DavaFlowSet<T> mo2534clone() {
        return new DavaFlowSet<>(this);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public FlowSet<T> emptySet() {
        return new DavaFlowSet();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void clear() {
        this.numElements = 0;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public int size() {
        return this.numElements;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public boolean isEmpty() {
        return this.numElements == 0;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public List<T> toList() {
        Object[] copiedElements = new Object[this.numElements];
        System.arraycopy(this.elements, 0, copiedElements, 0, this.numElements);
        return Arrays.asList(copiedElements);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void add(T e) {
        if (!contains(e)) {
            if (this.numElements == this.maxElements) {
                doubleCapacity();
            }
            T[] tArr = this.elements;
            int i = this.numElements;
            this.numElements = i + 1;
            tArr[i] = e;
        }
    }

    private void doubleCapacity() {
        int newSize = this.maxElements * 2;
        T[] tArr = (T[]) new Object[newSize];
        System.arraycopy(this.elements, 0, tArr, 0, this.numElements);
        this.elements = tArr;
        this.maxElements = newSize;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void remove(Object obj) {
        for (int i = 0; i < this.numElements; i++) {
            if (this.elements[i].equals(obj)) {
                remove(i);
                return;
            }
        }
    }

    public void remove(int idx) {
        T[] tArr = this.elements;
        T[] tArr2 = this.elements;
        int i = this.numElements - 1;
        this.numElements = i;
        tArr[idx] = tArr2[i];
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void union(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        if (sameType(otherFlow) && sameType(destFlow)) {
            DavaFlowSet<T> other = (DavaFlowSet) otherFlow;
            DavaFlowSet<T> dest = (DavaFlowSet) destFlow;
            if (dest == other) {
                for (int i = 0; i < this.numElements; i++) {
                    dest.add(this.elements[i]);
                }
                return;
            }
            if (this != dest) {
                copy(dest);
            }
            for (int i2 = 0; i2 < other.numElements; i2++) {
                dest.add(other.elements[i2]);
            }
            return;
        }
        super.union(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void intersection(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        DavaFlowSet<T> workingSet;
        if (sameType(otherFlow) && sameType(destFlow)) {
            DavaFlowSet<T> other = (DavaFlowSet) otherFlow;
            DavaFlowSet<T> dest = (DavaFlowSet) destFlow;
            if (dest == other || dest == this) {
                workingSet = new DavaFlowSet<>();
            } else {
                workingSet = dest;
                workingSet.clear();
            }
            for (int i = 0; i < this.numElements; i++) {
                if (other.contains(this.elements[i])) {
                    workingSet.add(this.elements[i]);
                }
            }
            if (workingSet != dest) {
                workingSet.copy(dest);
                return;
            }
            return;
        }
        super.intersection(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void difference(FlowSet<T> otherFlow, FlowSet<T> destFlow) {
        DavaFlowSet<T> workingSet;
        if (sameType(otherFlow) && sameType(destFlow)) {
            DavaFlowSet<T> other = (DavaFlowSet) otherFlow;
            DavaFlowSet<T> dest = (DavaFlowSet) destFlow;
            if (dest == other || dest == this) {
                workingSet = new DavaFlowSet<>();
            } else {
                workingSet = dest;
                workingSet.clear();
            }
            for (int i = 0; i < this.numElements; i++) {
                if (!other.contains(this.elements[i])) {
                    workingSet.add(this.elements[i]);
                }
            }
            if (workingSet != dest) {
                workingSet.copy(dest);
                return;
            }
            return;
        }
        super.difference(otherFlow, destFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public boolean contains(Object obj) {
        for (int i = 0; i < this.numElements; i++) {
            if (this.elements[i].equals(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet
    public boolean equals(Object otherFlow) {
        if (sameType(otherFlow)) {
            DavaFlowSet<T> other = (DavaFlowSet) otherFlow;
            if (other.numElements != this.numElements) {
                return false;
            }
            int size = this.numElements;
            for (int i = 0; i < size; i++) {
                if (!other.contains(this.elements[i])) {
                    return false;
                }
            }
            return true;
        }
        return super.equals(otherFlow);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet
    public void copy(FlowSet<T> destFlow) {
        if (this == destFlow) {
            return;
        }
        if (sameType(destFlow)) {
            DavaFlowSet<T> dest = (DavaFlowSet) destFlow;
            while (dest.maxElements < this.maxElements) {
                dest.doubleCapacity();
            }
            dest.numElements = this.numElements;
            System.arraycopy(this.elements, 0, dest.elements, 0, this.numElements);
            return;
        }
        super.copy(destFlow);
    }

    private List<DavaFlowSet<T>> addIfNotDuplicate(List<DavaFlowSet<T>> into, DavaFlowSet<T> addThis) {
        Iterator<DavaFlowSet<T>> it = into.iterator();
        boolean found = false;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            DavaFlowSet<T> temp = it.next();
            if (temp.equals(addThis) && temp.internalDataMatchesTo(addThis)) {
                found = true;
                break;
            }
        }
        if (!found) {
            into.add(addThis);
        }
        return into;
    }

    public void addToBreakList(String labelBroken, DavaFlowSet<T> set) {
        List<DavaFlowSet<T>> labelsBreakList = this.breakList.get(labelBroken);
        if (labelsBreakList == null) {
            List<DavaFlowSet<T>> labelsBreakList2 = new ArrayList<>();
            labelsBreakList2.add(set);
            this.breakList.put(labelBroken, labelsBreakList2);
            return;
        }
        this.breakList.put(labelBroken, addIfNotDuplicate(labelsBreakList, set));
    }

    public void addToContinueList(String labelContinued, DavaFlowSet<T> set) {
        List<DavaFlowSet<T>> labelsContinueList = this.continueList.get(labelContinued);
        if (labelsContinueList == null) {
            List<DavaFlowSet<T>> labelsContinueList2 = new ArrayList<>();
            labelsContinueList2.add(set);
            this.continueList.put(labelContinued, labelsContinueList2);
            return;
        }
        this.continueList.put(labelContinued, addIfNotDuplicate(labelsContinueList, set));
    }

    private boolean checkImplicit(DAbruptStmt ab) {
        SETNodeLabel label = ab.getLabel();
        if (label == null || label.toString() == null) {
            return true;
        }
        return false;
    }

    public void addToImplicitBreaks(DAbruptStmt ab, DavaFlowSet<T> set) {
        if (!checkImplicit(ab)) {
            throw new RuntimeException("Tried to add explicit break statement in the implicit list in");
        }
        if (!ab.is_Break()) {
            throw new RuntimeException("Tried to add continue statement in the break list in DavaFlowSet.addToImplicitBreaks");
        }
        ASTNode node = ClosestAbruptTargetFinder.v().getTarget(ab);
        List<DavaFlowSet<T>> listSets = this.implicitBreaks.get(node);
        if (listSets == null) {
            listSets = new ArrayList<>();
        }
        this.implicitBreaks.put(node, addIfNotDuplicate(listSets, set));
    }

    public void addToImplicitContinues(DAbruptStmt ab, DavaFlowSet<T> set) {
        if (!checkImplicit(ab)) {
            throw new RuntimeException("Tried to add explicit continue statement in the implicit list ");
        }
        if (!ab.is_Continue()) {
            throw new RuntimeException("Tried to add break statement in the continue list");
        }
        ASTNode node = ClosestAbruptTargetFinder.v().getTarget(ab);
        List<DavaFlowSet<T>> listSets = this.implicitContinues.get(node);
        if (listSets == null) {
            listSets = new ArrayList<>();
        }
        this.implicitContinues.put(node, addIfNotDuplicate(listSets, set));
    }

    private HashMap<Serializable, List<DavaFlowSet<T>>> getBreakList() {
        return this.breakList;
    }

    private HashMap<Serializable, List<DavaFlowSet<T>>> getContinueList() {
        return this.continueList;
    }

    public HashMap<Serializable, List<DavaFlowSet<T>>> getImplicitBreaks() {
        return this.implicitBreaks;
    }

    public HashMap<Serializable, List<DavaFlowSet<T>>> getImplicitContinues() {
        return this.implicitContinues;
    }

    public List<DavaFlowSet<T>> getImplicitlyBrokenSets(ASTNode node) {
        List<DavaFlowSet<T>> toReturn = this.implicitBreaks.get(node);
        if (toReturn != null) {
            return toReturn;
        }
        return null;
    }

    public List<DavaFlowSet<T>> getImplicitlyContinuedSets(ASTNode node) {
        List<DavaFlowSet<T>> toReturn = this.implicitContinues.get(node);
        if (toReturn != null) {
            return toReturn;
        }
        return null;
    }

    private List<DavaFlowSet<T>> copyDavaFlowSetList(List<DavaFlowSet<T>> currentList, List<DavaFlowSet<T>> temp) {
        for (DavaFlowSet<T> check : temp) {
            Iterator<DavaFlowSet<T>> currentListIt = currentList.iterator();
            boolean found = false;
            while (true) {
                if (!currentListIt.hasNext()) {
                    break;
                }
                DavaFlowSet<T> currentSet = currentListIt.next();
                if (check.equals(currentSet) && check.internalDataMatchesTo(currentSet)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                currentList.add(check);
            }
        }
        return currentList;
    }

    public void copyInternalDataFrom(DavaFlowSet<T> fromThis) {
        if (!sameType(fromThis)) {
            return;
        }
        Map<Serializable, List<DavaFlowSet<T>>> fromThisBreakList = fromThis.getBreakList();
        Iterator<Serializable> keys = fromThisBreakList.keySet().iterator();
        while (keys.hasNext()) {
            String labelBroken = (String) keys.next();
            List<DavaFlowSet<T>> temp = fromThisBreakList.get(labelBroken);
            List<DavaFlowSet<T>> currentList = this.breakList.get(labelBroken);
            if (currentList == null) {
                this.breakList.put(labelBroken, temp);
            } else {
                List<DavaFlowSet<T>> complete = copyDavaFlowSetList(currentList, temp);
                this.breakList.put(labelBroken, complete);
            }
        }
        HashMap<Serializable, List<DavaFlowSet<T>>> fromThisContinueList = fromThis.getContinueList();
        Iterator<Serializable> keys2 = fromThisContinueList.keySet().iterator();
        while (keys2.hasNext()) {
            String labelContinued = (String) keys2.next();
            List<DavaFlowSet<T>> temp2 = fromThisContinueList.get(labelContinued);
            List<DavaFlowSet<T>> currentList2 = this.continueList.get(labelContinued);
            if (currentList2 == null) {
                this.continueList.put(labelContinued, temp2);
            } else {
                List<DavaFlowSet<T>> complete2 = copyDavaFlowSetList(currentList2, temp2);
                this.continueList.put(labelContinued, complete2);
            }
        }
        HashMap<Serializable, List<DavaFlowSet<T>>> copyThis = fromThis.getImplicitBreaks();
        Iterator<Serializable> it = copyThis.keySet().iterator();
        while (it.hasNext()) {
            ASTNode node = (ASTNode) it.next();
            List<DavaFlowSet<T>> fromDavaFlowSets = copyThis.get(node);
            List<DavaFlowSet<T>> toDavaFlowSets = this.implicitBreaks.get(node);
            if (toDavaFlowSets == null) {
                this.implicitBreaks.put(node, fromDavaFlowSets);
            } else {
                List<DavaFlowSet<T>> complete3 = copyDavaFlowSetList(toDavaFlowSets, fromDavaFlowSets);
                this.implicitBreaks.put(node, complete3);
            }
        }
        HashMap<Serializable, List<DavaFlowSet<T>>> copyThis2 = fromThis.getImplicitContinues();
        Iterator<Serializable> it2 = copyThis2.keySet().iterator();
        while (it2.hasNext()) {
            ASTNode node2 = (ASTNode) it2.next();
            List<DavaFlowSet<T>> fromDavaFlowSets2 = copyThis2.get(node2);
            List<DavaFlowSet<T>> toDavaFlowSets2 = this.implicitContinues.get(node2);
            if (toDavaFlowSets2 == null) {
                this.implicitContinues.put(node2, fromDavaFlowSets2);
            } else {
                List<DavaFlowSet<T>> complete4 = copyDavaFlowSetList(toDavaFlowSets2, fromDavaFlowSets2);
                this.implicitContinues.put(node2, complete4);
            }
        }
    }

    private <X> boolean compareLists(List<X> listOne, List<X> listTwo) {
        if (listOne == null && listTwo == null) {
            return true;
        }
        if (listOne == null || listTwo == null || listOne.size() != listTwo.size()) {
            return false;
        }
        Iterator<X> listOneIt = listOne.iterator();
        while (true) {
            boolean found = false;
            if (listOneIt.hasNext()) {
                Object listOneObj = listOneIt.next();
                Iterator<X> listTwoIt = listTwo.iterator();
                while (true) {
                    if (!listTwoIt.hasNext()) {
                        break;
                    }
                    Object listTwoObj = listTwoIt.next();
                    if (listOneObj.equals(listTwoObj)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    public boolean internalDataMatchesTo(Object otherObj) {
        if (!(otherObj instanceof DavaFlowSet)) {
            return false;
        }
        DavaFlowSet<T> other = (DavaFlowSet) otherObj;
        HashMap<Serializable, List<DavaFlowSet<T>>> otherMap = other.getBreakList();
        if (!compareHashMaps(this.breakList, otherMap)) {
            return false;
        }
        HashMap<Serializable, List<DavaFlowSet<T>>> otherMap2 = other.getContinueList();
        if (!compareHashMaps(this.continueList, otherMap2)) {
            return false;
        }
        HashMap<Serializable, List<DavaFlowSet<T>>> otherMap3 = other.getImplicitBreaks();
        if (!compareHashMaps(this.implicitBreaks, otherMap3)) {
            return false;
        }
        HashMap<Serializable, List<DavaFlowSet<T>>> otherMap4 = other.getImplicitContinues();
        if (!compareHashMaps(this.implicitContinues, otherMap4)) {
            return false;
        }
        return true;
    }

    private boolean compareHashMaps(HashMap<Serializable, List<DavaFlowSet<T>>> thisMap, HashMap<Serializable, List<DavaFlowSet<T>>> otherMap) {
        List<String> otherKeyList = new ArrayList<>();
        Iterator<Serializable> keys = otherMap.keySet().iterator();
        while (keys.hasNext()) {
            String otherKey = (String) keys.next();
            otherKeyList.add(otherKey);
            if (!compareLists((List<DavaFlowSet<T>>) otherMap.get(otherKey), (List<DavaFlowSet<T>>) thisMap.get(otherKey))) {
                return false;
            }
        }
        Iterator<Serializable> keys2 = thisMap.keySet().iterator();
        while (keys2.hasNext()) {
            String key = (String) keys2.next();
            Iterator<String> keyListIt = otherKeyList.iterator();
            boolean alreadyDone = false;
            while (true) {
                if (!keyListIt.hasNext()) {
                    break;
                }
                String doneKey = keyListIt.next();
                if (key.equals(doneKey)) {
                    alreadyDone = true;
                    break;
                }
            }
            if (!alreadyDone) {
                return false;
            }
        }
        return true;
    }

    public List<DavaFlowSet<T>> getContinueSet(String label) {
        return this.continueList.remove(label);
    }

    public List<DavaFlowSet<T>> getBreakSet(String label) {
        return this.breakList.remove(label);
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append(" SET={");
        for (int i = 0; i < this.numElements; i++) {
            if (i != 0) {
                b.append(" , ");
            }
            b.append(this.elements[i].toString());
        }
        b.append(" }");
        return b.toString();
    }

    @Override // soot.toolkits.scalar.AbstractFlowSet, soot.toolkits.scalar.FlowSet, java.lang.Iterable
    public Iterator<T> iterator() {
        return new Iterator<T>() { // from class: soot.dava.toolkits.base.AST.structuredAnalysis.DavaFlowSet.1
            int lastIdx = 0;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.lastIdx < DavaFlowSet.this.numElements;
            }

            @Override // java.util.Iterator
            public T next() {
                T[] tArr = DavaFlowSet.this.elements;
                int i = this.lastIdx;
                this.lastIdx = i + 1;
                return tArr[i];
            }

            @Override // java.util.Iterator
            public void remove() {
                DavaFlowSet davaFlowSet = DavaFlowSet.this;
                int i = this.lastIdx - 1;
                this.lastIdx = i;
                davaFlowSet.remove(i);
            }
        };
    }

    public int getElementCount() {
        return this.elements.length;
    }

    public T getElementAt(int idx) {
        return this.elements[idx];
    }
}
