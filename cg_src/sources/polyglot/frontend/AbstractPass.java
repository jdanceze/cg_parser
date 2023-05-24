package polyglot.frontend;

import polyglot.frontend.Pass;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/AbstractPass.class */
public abstract class AbstractPass implements Pass {
    protected Pass.ID id;
    protected long exclusive_time = 0;
    protected long inclusive_time = 0;

    @Override // polyglot.frontend.Pass
    public abstract boolean run();

    public AbstractPass(Pass.ID id) {
        this.id = id;
    }

    @Override // polyglot.frontend.Pass
    public Pass.ID id() {
        return this.id;
    }

    @Override // polyglot.frontend.Pass
    public String name() {
        return this.id.toString();
    }

    @Override // polyglot.frontend.Pass
    public void toggleTimers(boolean exclusive_only) {
        if (!exclusive_only) {
            this.inclusive_time = System.currentTimeMillis() - this.inclusive_time;
        }
        this.exclusive_time = System.currentTimeMillis() - this.exclusive_time;
    }

    @Override // polyglot.frontend.Pass
    public void resetTimers() {
        this.inclusive_time = 0L;
        this.exclusive_time = 0L;
    }

    @Override // polyglot.frontend.Pass
    public long exclusiveTime() {
        return this.exclusive_time;
    }

    @Override // polyglot.frontend.Pass
    public long inclusiveTime() {
        return this.inclusive_time;
    }

    public String toString() {
        return new StringBuffer().append(getClass().getName()).append(":").append(this.id).toString();
    }
}
