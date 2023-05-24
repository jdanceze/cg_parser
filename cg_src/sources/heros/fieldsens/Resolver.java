package heros.fieldsens;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import heros.fieldsens.FlowFunction;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/Resolver.class */
public abstract class Resolver<Field, Fact, Stmt, Method> {
    protected PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer;
    private Set<Resolver<Field, Fact, Stmt, Method>> interest = Sets.newHashSet();
    private List<InterestCallback<Field, Fact, Stmt, Method>> interestCallbacks = Lists.newLinkedList();
    private boolean canBeResolvedEmpty = false;

    public abstract void resolve(FlowFunction.Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> interestCallback);

    /* JADX INFO: Access modifiers changed from: protected */
    public abstract void log(String str);

    public Resolver(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer) {
        this.analyzer = analyzer;
    }

    public void interest(Resolver<Field, Fact, Stmt, Method> resolver) {
        if (!this.interest.add(resolver)) {
            return;
        }
        log("Interest given by: " + resolver);
        Iterator it = Lists.newLinkedList(this.interestCallbacks).iterator();
        while (it.hasNext()) {
            InterestCallback<Field, Fact, Stmt, Method> callback = (InterestCallback) it.next();
            callback.interest(this.analyzer, resolver);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void canBeResolvedEmpty() {
        if (this.canBeResolvedEmpty) {
            return;
        }
        this.canBeResolvedEmpty = true;
        Iterator it = Lists.newLinkedList(this.interestCallbacks).iterator();
        while (it.hasNext()) {
            InterestCallback<Field, Fact, Stmt, Method> callback = (InterestCallback) it.next();
            callback.canBeResolvedEmpty();
        }
    }

    public boolean isInterestGiven() {
        return !this.interest.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void registerCallback(InterestCallback<Field, Fact, Stmt, Method> callback) {
        if (!this.interest.isEmpty()) {
            Iterator it = Lists.newLinkedList(this.interest).iterator();
            while (it.hasNext()) {
                Resolver<Field, Fact, Stmt, Method> resolver = (Resolver) it.next();
                callback.interest(this.analyzer, resolver);
            }
        }
        log("Callback registered");
        this.interestCallbacks.add(callback);
        if (this.canBeResolvedEmpty) {
            callback.canBeResolvedEmpty();
        }
    }
}
