package heros.fieldsens;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import heros.fieldsens.AccessPath;
import heros.fieldsens.FlowFunction;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/ResolverTemplate.class */
public abstract class ResolverTemplate<Field, Fact, Stmt, Method, Incoming> extends Resolver<Field, Fact, Stmt, Method> {
    private boolean recursionLock;
    protected Set<Incoming> incomingEdges;
    private ResolverTemplate<Field, Fact, Stmt, Method, Incoming> parent;
    private Map<AccessPath<Field>, ResolverTemplate<Field, Fact, Stmt, Method, Incoming>> nestedResolvers;
    private Map<AccessPath<Field>, ResolverTemplate<Field, Fact, Stmt, Method, Incoming>> allResolversInExclHierarchy;
    protected AccessPath<Field> resolvedAccessPath;
    protected Debugger<Field, Fact, Stmt, Method> debugger;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract AccessPath<Field> getAccessPathOf(Incoming incoming);

    protected abstract void processIncomingPotentialPrefix(Incoming incoming);

    protected abstract void processIncomingGuaranteedPrefix(Incoming incoming);

    protected abstract ResolverTemplate<Field, Fact, Stmt, Method, Incoming> createNestedResolver(AccessPath<Field> accessPath);

    static {
        $assertionsDisabled = !ResolverTemplate.class.desiredAssertionStatus();
    }

    public ResolverTemplate(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> analyzer, AccessPath<Field> resolvedAccessPath, ResolverTemplate<Field, Fact, Stmt, Method, Incoming> parent, Debugger<Field, Fact, Stmt, Method> debugger) {
        super(analyzer);
        this.recursionLock = false;
        this.incomingEdges = Sets.newHashSet();
        this.nestedResolvers = Maps.newHashMap();
        this.resolvedAccessPath = resolvedAccessPath;
        this.parent = parent;
        this.debugger = debugger;
        if (parent == null || resolvedAccessPath.getExclusions().isEmpty()) {
            this.allResolversInExclHierarchy = Maps.newHashMap();
        } else {
            this.allResolversInExclHierarchy = parent.allResolversInExclHierarchy;
        }
        debugger.newResolver(analyzer, this);
    }

    protected boolean isLocked() {
        if (this.recursionLock) {
            return true;
        }
        if (this.parent == null) {
            return false;
        }
        return this.parent.isLocked();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void lock() {
        this.recursionLock = true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void unlock() {
        this.recursionLock = false;
    }

    public void addIncoming(Incoming inc) {
        if (this.resolvedAccessPath.isPrefixOf(getAccessPathOf(inc)) == AccessPath.PrefixTestResult.GUARANTEED_PREFIX) {
            log("Incoming Edge: " + inc);
            if (!this.incomingEdges.add(inc)) {
                return;
            }
            interest(this);
            Iterator it = Lists.newLinkedList(this.nestedResolvers.values()).iterator();
            while (it.hasNext()) {
                ResolverTemplate<Field, Fact, Stmt, Method, Incoming> nestedResolver = (ResolverTemplate) it.next();
                nestedResolver.addIncoming(inc);
            }
            processIncomingGuaranteedPrefix(inc);
        } else if (getAccessPathOf(inc).isPrefixOf(this.resolvedAccessPath).atLeast(AccessPath.PrefixTestResult.POTENTIAL_PREFIX)) {
            processIncomingPotentialPrefix(inc);
        }
    }

    @Override // heros.fieldsens.Resolver
    public void resolve(FlowFunction.Constraint<Field> constraint, InterestCallback<Field, Fact, Stmt, Method> callback) {
        log("Resolve: " + constraint);
        this.debugger.askedToResolve(this, constraint);
        if (constraint.canBeAppliedTo(this.resolvedAccessPath) && !isLocked()) {
            AccessPath<Field> newAccPath = constraint.applyToAccessPath(this.resolvedAccessPath);
            ResolverTemplate<Field, Fact, Stmt, Method, Incoming> nestedResolver = getOrCreateNestedResolver(newAccPath);
            if (!$assertionsDisabled && !nestedResolver.resolvedAccessPath.equals(constraint.applyToAccessPath(this.resolvedAccessPath))) {
                throw new AssertionError();
            }
            nestedResolver.registerCallback(callback);
        }
    }

    protected ResolverTemplate<Field, Fact, Stmt, Method, Incoming> getOrCreateNestedResolver(AccessPath<Field> newAccPath) {
        if (this.resolvedAccessPath.equals(newAccPath)) {
            return this;
        }
        if (!this.nestedResolvers.containsKey(newAccPath)) {
            if ($assertionsDisabled || this.resolvedAccessPath.getDeltaTo(newAccPath).accesses.length <= 1) {
                if (this.allResolversInExclHierarchy.containsKey(newAccPath)) {
                    return this.allResolversInExclHierarchy.get(newAccPath);
                }
                ResolverTemplate<Field, Fact, Stmt, Method, Incoming> nestedResolver = createNestedResolver(newAccPath);
                if (!this.resolvedAccessPath.getExclusions().isEmpty() || !newAccPath.getExclusions().isEmpty()) {
                    this.allResolversInExclHierarchy.put(newAccPath, nestedResolver);
                }
                this.nestedResolvers.put(newAccPath, nestedResolver);
                Iterator it = Lists.newLinkedList(this.incomingEdges).iterator();
                while (it.hasNext()) {
                    Incoming inc = (Incoming) it.next();
                    nestedResolver.addIncoming(inc);
                }
                return nestedResolver;
            }
            throw new AssertionError();
        }
        return this.nestedResolvers.get(newAccPath);
    }
}
