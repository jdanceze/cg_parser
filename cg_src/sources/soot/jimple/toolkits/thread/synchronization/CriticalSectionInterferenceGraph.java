package soot.jimple.toolkits.thread.synchronization;

import java.util.ArrayList;
import java.util.List;
import soot.Hierarchy;
import soot.Local;
import soot.PointsToAnalysis;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.jimple.toolkits.callgraph.ReachableMethods;
import soot.jimple.toolkits.pointer.CodeBlockRWSet;
import soot.jimple.toolkits.thread.mhp.MhpTester;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/thread/synchronization/CriticalSectionInterferenceGraph.class */
public class CriticalSectionInterferenceGraph {
    int nextGroup;
    List<CriticalSectionGroup> groups;
    List<CriticalSection> criticalSections;
    MhpTester mhp;
    PointsToAnalysis pta = Scene.v().getPointsToAnalysis();
    boolean optionOneGlobalLock;
    boolean optionLeaveOriginalLocks;
    boolean optionIncludeEmptyPossibleEdges;

    public CriticalSectionInterferenceGraph(List<CriticalSection> criticalSections, MhpTester mhp, boolean optionOneGlobalLock, boolean optionLeaveOriginalLocks, boolean optionIncludeEmptyPossibleEdges) {
        this.optionOneGlobalLock = false;
        this.optionLeaveOriginalLocks = false;
        this.optionIncludeEmptyPossibleEdges = false;
        this.criticalSections = criticalSections;
        this.mhp = mhp;
        this.optionOneGlobalLock = optionOneGlobalLock;
        this.optionLeaveOriginalLocks = optionLeaveOriginalLocks;
        this.optionIncludeEmptyPossibleEdges = optionIncludeEmptyPossibleEdges;
        calculateGroups();
    }

    public int groupCount() {
        return this.nextGroup;
    }

    public List<CriticalSectionGroup> groups() {
        return this.groups;
    }

    public void calculateGroups() {
        CodeBlockRWSet rw;
        int size;
        this.nextGroup = 1;
        this.groups = new ArrayList();
        this.groups.add(new CriticalSectionGroup(0));
        if (this.optionOneGlobalLock) {
            CriticalSectionGroup onlyGroup = new CriticalSectionGroup(this.nextGroup);
            for (CriticalSection tn1 : this.criticalSections) {
                onlyGroup.add(tn1);
            }
            this.nextGroup++;
            this.groups.add(onlyGroup);
            return;
        }
        for (CriticalSection tn12 : this.criticalSections) {
            if (tn12.setNumber != -1) {
                if (tn12.read.size() == 0 && tn12.write.size() == 0 && !this.optionLeaveOriginalLocks) {
                    tn12.setNumber = -1;
                } else {
                    for (CriticalSection tn2 : this.criticalSections) {
                        if (tn2.setNumber != -1 && mayHappenInParallel(tn12, tn2)) {
                            boolean typeCompatible = false;
                            boolean emptyEdge = false;
                            if (tn12.origLock != null && tn2.origLock != null) {
                                if (tn12.origLock == null || tn2.origLock == null) {
                                    emptyEdge = true;
                                } else if (!(tn12.origLock instanceof Local) || !(tn2.origLock instanceof Local)) {
                                    emptyEdge = !tn12.origLock.equals(tn2.origLock);
                                } else {
                                    emptyEdge = !this.pta.reachingObjects((Local) tn12.origLock).hasNonEmptyIntersection(this.pta.reachingObjects((Local) tn2.origLock));
                                }
                                RefLikeType typeOne = (RefLikeType) tn12.origLock.getType();
                                RefLikeType typeTwo = (RefLikeType) tn2.origLock.getType();
                                SootClass classOne = typeOne instanceof RefType ? ((RefType) typeOne).getSootClass() : null;
                                SootClass classTwo = typeTwo instanceof RefType ? ((RefType) typeTwo).getSootClass() : null;
                                if (classOne != null && classTwo != null) {
                                    Hierarchy h = Scene.v().getActiveHierarchy();
                                    if (classOne.isInterface()) {
                                        typeCompatible = classTwo.isInterface() ? h.getSubinterfacesOfIncluding(classOne).contains(classTwo) || h.getSubinterfacesOfIncluding(classTwo).contains(classOne) : h.getImplementersOf(classOne).contains(classTwo);
                                    } else if (classTwo.isInterface()) {
                                        typeCompatible = h.getImplementersOf(classTwo).contains(classOne);
                                    } else {
                                        typeCompatible = (classOne != null && Scene.v().getActiveHierarchy().getSubclassesOfIncluding(classOne).contains(classTwo)) || (classTwo != null && Scene.v().getActiveHierarchy().getSubclassesOfIncluding(classTwo).contains(classOne));
                                    }
                                }
                            }
                            if ((!this.optionLeaveOriginalLocks && (tn12.write.hasNonEmptyIntersection(tn2.write) || tn12.write.hasNonEmptyIntersection(tn2.read) || tn12.read.hasNonEmptyIntersection(tn2.write))) || (this.optionLeaveOriginalLocks && typeCompatible && (this.optionIncludeEmptyPossibleEdges || !emptyEdge))) {
                                if (this.optionLeaveOriginalLocks) {
                                    rw = new CodeBlockRWSet();
                                    size = emptyEdge ? 0 : 1;
                                } else {
                                    rw = tn12.write.intersection(tn2.write);
                                    rw.union(tn12.write.intersection(tn2.read));
                                    rw.union(tn12.read.intersection(tn2.write));
                                    size = rw.size();
                                }
                                tn12.edges.add(new CriticalSectionDataDependency(tn2, size, rw));
                                if (size > 0) {
                                    if (tn12.setNumber > 0) {
                                        if (tn2.setNumber == 0) {
                                            tn12.group.add(tn2);
                                        } else if (tn2.setNumber > 0 && tn12.setNumber != tn2.setNumber) {
                                            tn12.group.mergeGroups(tn2.group);
                                        }
                                    } else if (tn12.setNumber == 0) {
                                        if (tn2.setNumber == 0) {
                                            CriticalSectionGroup newGroup = new CriticalSectionGroup(this.nextGroup);
                                            newGroup.add(tn12);
                                            newGroup.add(tn2);
                                            this.groups.add(newGroup);
                                            this.nextGroup++;
                                        } else if (tn2.setNumber > 0) {
                                            tn2.group.add(tn12);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (tn12.setNumber == 0) {
                        tn12.setNumber = -1;
                    }
                }
            }
        }
    }

    public boolean mayHappenInParallel(CriticalSection tn1, CriticalSection tn2) {
        if (this.mhp == null) {
            if (this.optionLeaveOriginalLocks) {
                return true;
            }
            ReachableMethods rm = Scene.v().getReachableMethods();
            if (!rm.contains(tn1.method) || !rm.contains(tn2.method)) {
                return false;
            }
            return true;
        }
        return this.mhp.mayHappenInParallel(tn1.method, tn2.method);
    }
}
