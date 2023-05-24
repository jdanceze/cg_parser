package soot.dava.toolkits.base.finders;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.dava.internal.asg.AugmentedStmt;
import soot.dava.internal.asg.AugmentedStmtGraph;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/finders/ExceptionNode.class */
public class ExceptionNode {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionNode.class);
    private final IterableSet<AugmentedStmt> body;
    private IterableSet<AugmentedStmt> tryBody;
    private SootClass exception;
    private AugmentedStmt handlerAugmentedStmt;
    private IterableSet<AugmentedStmt> catchBody = null;
    private boolean dirty = true;
    private List<AugmentedStmt> exitList = null;
    private LinkedList<IterableSet<AugmentedStmt>> catchList = null;
    private HashMap<IterableSet<AugmentedStmt>, SootClass> catch2except = null;

    public ExceptionNode(IterableSet<AugmentedStmt> tryBody, SootClass exception, AugmentedStmt handlerAugmentedStmt) {
        this.tryBody = tryBody;
        this.exception = exception;
        this.handlerAugmentedStmt = handlerAugmentedStmt;
        this.body = new IterableSet<>(tryBody);
    }

    public boolean add_TryStmts(Collection<AugmentedStmt> c) {
        for (AugmentedStmt as : c) {
            if (!add_TryStmt(as)) {
                return false;
            }
        }
        return true;
    }

    public boolean add_TryStmt(AugmentedStmt as) {
        if (this.body.contains(as) || this.tryBody.contains(as)) {
            return false;
        }
        this.body.add(as);
        this.tryBody.add(as);
        return true;
    }

    public void refresh_CatchBody(ExceptionFinder ef) {
        if (this.catchBody != null) {
            this.body.removeAll(this.catchBody);
        }
        this.catchBody = ef.get_CatchBody(this.handlerAugmentedStmt);
        this.body.addAll(this.catchBody);
    }

    public IterableSet<AugmentedStmt> get_Body() {
        return this.body;
    }

    public IterableSet<AugmentedStmt> get_TryBody() {
        return this.tryBody;
    }

    public IterableSet<AugmentedStmt> get_CatchBody() {
        return this.catchBody;
    }

    public boolean remove(AugmentedStmt as) {
        if (!this.body.contains(as)) {
            return false;
        }
        if (this.tryBody.contains(as)) {
            this.tryBody.remove(as);
        } else if (this.catchBody != null && this.catchBody.contains(as)) {
            this.catchBody.remove(as);
            this.dirty = true;
        } else {
            return false;
        }
        this.body.remove(as);
        return true;
    }

    public List<AugmentedStmt> get_CatchExits() {
        if (this.catchBody == null) {
            return null;
        }
        if (this.dirty) {
            this.exitList = new LinkedList();
            this.dirty = false;
            Iterator<AugmentedStmt> it = this.catchBody.iterator();
            while (it.hasNext()) {
                AugmentedStmt as = it.next();
                Iterator<AugmentedStmt> it2 = as.bsuccs.iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    AugmentedStmt succ = it2.next();
                    if (!this.catchBody.contains(succ)) {
                        this.exitList.add(as);
                        break;
                    }
                }
            }
        }
        return this.exitList;
    }

    public void splitOff_ExceptionNode(IterableSet<AugmentedStmt> newTryBody, AugmentedStmtGraph asg, IterableSet<ExceptionNode> enlist) {
        IterableSet<AugmentedStmt> oldTryBody = new IterableSet<>();
        oldTryBody.addAll(this.tryBody);
        IterableSet<AugmentedStmt> oldBody = new IterableSet<>();
        oldBody.addAll(this.body);
        Iterator<AugmentedStmt> it = newTryBody.iterator();
        while (it.hasNext()) {
            AugmentedStmt as = it.next();
            if (!remove(as)) {
                StringBuffer b = new StringBuffer();
                Iterator<AugmentedStmt> it2 = newTryBody.iterator();
                while (it2.hasNext()) {
                    AugmentedStmt auBody = it2.next();
                    b.append("\n" + auBody.toString());
                }
                b.append("\n-");
                Iterator<T> it3 = oldTryBody.iterator();
                while (it3.hasNext()) {
                    AugmentedStmt auBody2 = (AugmentedStmt) it3.next();
                    b.append("\n" + auBody2.toString());
                }
                b.append("\n-");
                Iterator<T> it4 = oldBody.iterator();
                while (it4.hasNext()) {
                    AugmentedStmt auBody3 = (AugmentedStmt) it4.next();
                    b.append("\n" + auBody3.toString());
                }
                b.append("\n-");
                throw new RuntimeException("Tried to split off a new try body that isn't in the old one.\n" + as + "\n - " + b.toString());
            }
        }
        asg.clone_Body(this.catchBody);
        AugmentedStmt oldCatchTarget = this.handlerAugmentedStmt;
        AugmentedStmt newCatchTarget = asg.get_CloneOf(this.handlerAugmentedStmt);
        Iterator<AugmentedStmt> it5 = newTryBody.iterator();
        while (it5.hasNext()) {
            AugmentedStmt as2 = it5.next();
            as2.remove_CSucc(oldCatchTarget);
            oldCatchTarget.remove_CPred(as2);
        }
        Iterator<AugmentedStmt> it6 = this.tryBody.iterator();
        while (it6.hasNext()) {
            AugmentedStmt as3 = it6.next();
            as3.remove_CSucc(newCatchTarget);
            newCatchTarget.remove_CPred(as3);
        }
        Iterator<ExceptionNode> it7 = enlist.iterator();
        while (it7.hasNext()) {
            ExceptionNode en = it7.next();
            if (this != en && this.catchBody.isSupersetOf(en.get_Body())) {
                IterableSet<AugmentedStmt> clonedTryBody = new IterableSet<>();
                Iterator<AugmentedStmt> it8 = en.get_TryBody().iterator();
                while (it8.hasNext()) {
                    AugmentedStmt au = it8.next();
                    clonedTryBody.add(asg.get_CloneOf(au));
                }
                enlist.addLast(new ExceptionNode(clonedTryBody, en.exception, asg.get_CloneOf(en.handlerAugmentedStmt)));
            }
        }
        enlist.addLast(new ExceptionNode(newTryBody, this.exception, asg.get_CloneOf(this.handlerAugmentedStmt)));
        Iterator<ExceptionNode> it9 = enlist.iterator();
        while (it9.hasNext()) {
            it9.next().refresh_CatchBody(ExceptionFinder.v());
        }
        asg.find_Dominators();
    }

    public void add_CatchBody(ExceptionNode other) {
        if (other.get_CatchList() == null) {
            add_CatchBody(other.get_CatchBody(), other.get_Exception());
            return;
        }
        for (IterableSet<AugmentedStmt> c : other.get_CatchList()) {
            add_CatchBody(c, other.get_Exception(c));
        }
    }

    public void add_CatchBody(IterableSet<AugmentedStmt> newCatchBody, SootClass except) {
        if (this.catchList == null) {
            this.catchList = new LinkedList<>();
            this.catchList.addLast(this.catchBody);
            this.catch2except = new HashMap<>();
            this.catch2except.put(this.catchBody, this.exception);
        }
        this.body.addAll(newCatchBody);
        this.catchList.addLast(newCatchBody);
        this.catch2except.put(newCatchBody, except);
    }

    public List<IterableSet<AugmentedStmt>> get_CatchList() {
        List<IterableSet<AugmentedStmt>> l = this.catchList;
        if (l == null) {
            l = new LinkedList<>();
            l.add(this.catchBody);
        }
        return l;
    }

    public Map<IterableSet<AugmentedStmt>, SootClass> get_ExceptionMap() {
        Map<IterableSet<AugmentedStmt>, SootClass> m = this.catch2except;
        if (m == null) {
            m = new HashMap<>();
            m.put(this.catchBody, this.exception);
        }
        return m;
    }

    public SootClass get_Exception() {
        return this.exception;
    }

    public SootClass get_Exception(IterableSet<AugmentedStmt> catchBody) {
        if (this.catch2except == null) {
            return this.exception;
        }
        return this.catch2except.get(catchBody);
    }

    public void dump() {
        logger.debug("try {");
        Iterator<AugmentedStmt> it = get_TryBody().iterator();
        while (it.hasNext()) {
            AugmentedStmt au = it.next();
            logger.debug("\t" + au);
        }
        logger.debug("}");
        for (IterableSet<AugmentedStmt> catchBody : get_CatchList()) {
            logger.debug("catch " + get_ExceptionMap().get(catchBody) + " {");
            Iterator<AugmentedStmt> it2 = catchBody.iterator();
            while (it2.hasNext()) {
                AugmentedStmt au2 = it2.next();
                logger.debug("\t" + au2);
            }
            logger.debug("}");
        }
    }
}
