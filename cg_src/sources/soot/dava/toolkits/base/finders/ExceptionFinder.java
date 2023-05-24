package soot.dava.toolkits.base.finders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.dava.Dava;
import soot.dava.DavaBody;
import soot.dava.RetriggerAnalysisException;
import soot.dava.internal.SET.SETNode;
import soot.dava.internal.SET.SETTryNode;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.jimple.GotoStmt;
import soot.jimple.Stmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/ExceptionFinder.class */
public class ExceptionFinder implements FactFinder {
    public ExceptionFinder(Singletons.Global g) {
    }

    public static ExceptionFinder v() {
        return G.v().soot_dava_toolkits_base_finders_ExceptionFinder();
    }

    @Override // soot.dava.toolkits.base.finders.FactFinder
    public void find(DavaBody body, AugmentedStmtGraph asg, SETNode SET) throws RetriggerAnalysisException {
        Dava.v().log("ExceptionFinder::find()");
        IterableSet<ExceptionNode> synchronizedBlockFacts = body.get_SynchronizedBlockFacts();
        Iterator<ExceptionNode> it = body.get_ExceptionFacts().iterator();
        while (it.hasNext()) {
            ExceptionNode en = it.next();
            if (!synchronizedBlockFacts.contains(en)) {
                IterableSet<AugmentedStmt> fullBody = new IterableSet<>();
                for (IterableSet<AugmentedStmt> is : en.get_CatchList()) {
                    fullBody.addAll(is);
                }
                fullBody.addAll(en.get_TryBody());
                if (!SET.nest(new SETTryNode(fullBody, en, asg, body))) {
                    throw new RetriggerAnalysisException();
                }
            }
        }
    }

    public void preprocess(DavaBody body, AugmentedStmtGraph asg) {
        Dava.v().log("ExceptionFinder::preprocess()");
        IterableSet<ExceptionNode> enlist = new IterableSet<>();
        for (Trap trap : body.getTraps()) {
            IterableSet<AugmentedStmt> tryBody = new IterableSet<>();
            Iterator<Unit> it = body.getUnits().iterator((UnitPatchingChain) trap.getBeginUnit());
            Unit e = trap.getEndUnit();
            for (Unit u = it.next(); u != e; u = it.next()) {
                tryBody.add(asg.get_AugStmt((Stmt) u));
            }
            enlist.add(new ExceptionNode(tryBody, trap.getException(), asg.get_AugStmt((Stmt) trap.getHandlerUnit())));
        }
        Iterator<ExceptionNode> it2 = enlist.iterator();
        while (it2.hasNext()) {
            ExceptionNode en = it2.next();
            IterableSet<AugmentedStmt> try_body = en.get_TryBody();
            ArrayList<AugmentedStmt> toAdd = new ArrayList<>();
            Iterator<AugmentedStmt> it3 = try_body.iterator();
            while (it3.hasNext()) {
                AugmentedStmt tras = it3.next();
                for (AugmentedStmt pas : tras.cpreds) {
                    if (!try_body.contains(pas) && (pas.get_Stmt() instanceof GotoStmt)) {
                        boolean add_it = true;
                        for (AugmentedStmt pred : pas.cpreds) {
                            add_it = try_body.contains(pred);
                            if (!add_it) {
                                break;
                            }
                        }
                        if (add_it) {
                            toAdd.add(pas);
                        }
                    }
                }
            }
            en.add_TryStmts(toAdd);
        }
        loop6: while (true) {
            Iterator<ExceptionNode> it4 = enlist.iterator();
            while (it4.hasNext()) {
                ExceptionNode enode = it4.next();
                enode.refresh_CatchBody(this);
            }
            int size = enlist.size();
            ExceptionNode[] ena = new ExceptionNode[size];
            int i = 0;
            Iterator<ExceptionNode> it5 = enlist.iterator();
            while (it5.hasNext()) {
                ExceptionNode next = it5.next();
                ena[i] = next;
                i++;
            }
            int i2 = 0;
            while (true) {
                if (i2 < size - 1) {
                    ExceptionNode eni = ena[i2];
                    for (int j = i2 + 1; j < size; j++) {
                        ExceptionNode enj = ena[j];
                        IterableSet<AugmentedStmt> eniTryBody = eni.get_TryBody();
                        IterableSet<AugmentedStmt> enjTryBody = enj.get_TryBody();
                        if (!eniTryBody.equals(enjTryBody) && eniTryBody.intersects(enjTryBody) && !eniTryBody.isSupersetOf(enj.get_Body()) && !enjTryBody.isSupersetOf(eni.get_Body())) {
                            IterableSet<AugmentedStmt> newTryBody = eniTryBody.intersection(enjTryBody);
                            if (newTryBody.equals(enjTryBody)) {
                                eni.splitOff_ExceptionNode(newTryBody, asg, enlist);
                            } else {
                                enj.splitOff_ExceptionNode(newTryBody, asg, enlist);
                            }
                        }
                    }
                    i2++;
                } else {
                    Iterator<ExceptionNode> it6 = enlist.iterator();
                    while (it6.hasNext()) {
                        ExceptionNode en2 = it6.next();
                        IterableSet<AugmentedStmt> tryBody2 = en2.get_TryBody();
                        LinkedList<AugmentedStmt> heads = new LinkedList<>();
                        Iterator<AugmentedStmt> it7 = tryBody2.iterator();
                        while (it7.hasNext()) {
                            AugmentedStmt as = it7.next();
                            if (as.cpreds.isEmpty()) {
                                heads.add(as);
                            } else {
                                Iterator<AugmentedStmt> it8 = as.cpreds.iterator();
                                while (true) {
                                    if (!it8.hasNext()) {
                                        break;
                                    }
                                    AugmentedStmt pred2 = it8.next();
                                    if (!tryBody2.contains(pred2)) {
                                        heads.add(as);
                                        break;
                                    }
                                }
                            }
                        }
                        HashSet<AugmentedStmt> touchSet = new HashSet<>(heads);
                        IterableSet<AugmentedStmt> subTryBlock = new IterableSet<>();
                        LinkedList<AugmentedStmt> worklist = new LinkedList<>();
                        AugmentedStmt head = heads.removeFirst();
                        worklist.add(head);
                        while (!worklist.isEmpty()) {
                            AugmentedStmt as2 = worklist.removeFirst();
                            subTryBlock.add(as2);
                            for (AugmentedStmt sas : as2.csuccs) {
                                if (tryBody2.contains(sas) && !touchSet.contains(sas)) {
                                    touchSet.add(sas);
                                    if (sas.get_Dominators().contains(head)) {
                                        worklist.add(sas);
                                    } else {
                                        heads.addLast(sas);
                                    }
                                }
                            }
                        }
                        if (!heads.isEmpty()) {
                            en2.splitOff_ExceptionNode(subTryBlock, asg, enlist);
                        }
                    }
                    break loop6;
                }
            }
        }
        LinkedList<ExceptionNode> reps = new LinkedList<>();
        HashMap<Serializable, LinkedList<IterableSet<AugmentedStmt>>> hCode2bucket = new HashMap<>();
        HashMap<Serializable, ExceptionNode> tryBody2exceptionNode = new HashMap<>();
        Iterator<ExceptionNode> it9 = enlist.iterator();
        while (it9.hasNext()) {
            ExceptionNode en3 = it9.next();
            int hashCode = 0;
            IterableSet<AugmentedStmt> curTryBody = en3.get_TryBody();
            Iterator<AugmentedStmt> it10 = curTryBody.iterator();
            while (it10.hasNext()) {
                AugmentedStmt au = it10.next();
                hashCode ^= au.hashCode();
            }
            Integer I = new Integer(hashCode);
            LinkedList<IterableSet<AugmentedStmt>> bucket = hCode2bucket.get(I);
            if (bucket == null) {
                bucket = new LinkedList<>();
                hCode2bucket.put(I, bucket);
            }
            ExceptionNode repExceptionNode = null;
            Iterator<IterableSet<AugmentedStmt>> it11 = bucket.iterator();
            while (true) {
                if (!it11.hasNext()) {
                    break;
                }
                IterableSet<AugmentedStmt> bucketTryBody = it11.next();
                if (curTryBody.equals(bucketTryBody)) {
                    repExceptionNode = tryBody2exceptionNode.get(bucketTryBody);
                    break;
                }
            }
            if (repExceptionNode == null) {
                tryBody2exceptionNode.put(curTryBody, en3);
                bucket.add(curTryBody);
                reps.add(en3);
            } else {
                repExceptionNode.add_CatchBody(en3);
            }
        }
        enlist.clear();
        enlist.addAll(reps);
        IterableSet<ExceptionNode> exceptionFacts = body.get_ExceptionFacts();
        exceptionFacts.clear();
        exceptionFacts.addAll(enlist);
    }

    public IterableSet<AugmentedStmt> get_CatchBody(AugmentedStmt handlerAugmentedStmt) {
        IterableSet<AugmentedStmt> catchBody = new IterableSet<>();
        catchBody.add(handlerAugmentedStmt);
        LinkedList<AugmentedStmt> catchQueue = new LinkedList<>(handlerAugmentedStmt.csuccs);
        while (!catchQueue.isEmpty()) {
            AugmentedStmt as = catchQueue.removeFirst();
            if (!catchBody.contains(as) && as.get_Dominators().contains(handlerAugmentedStmt)) {
                catchBody.add(as);
                catchQueue.addAll(as.csuccs);
            }
        }
        return catchBody;
    }
}
