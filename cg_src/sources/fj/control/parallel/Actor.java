package fj.control.parallel;

import fj.Effect;
import fj.F;
import fj.P1;
import fj.Unit;
import fj.function.Effect1;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/parallel/Actor.class */
public final class Actor<A> {
    private final Strategy<Unit> s;
    private final F<A, P1<Unit>> f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: fj.control.parallel.Actor$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/parallel/Actor$1.class */
    public static class AnonymousClass1 implements Effect1<T> {
        AtomicBoolean suspended = new AtomicBoolean(true);
        ConcurrentLinkedQueue<T> mbox = new ConcurrentLinkedQueue<>();
        P1<Unit> processor = new P1<Unit>() { // from class: fj.control.parallel.Actor.1.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.P1
            public Unit _1() {
                Object poll = AnonymousClass1.this.mbox.poll();
                if (poll != null) {
                    AnonymousClass1.this.val$ea.f(poll);
                    AnonymousClass1.this.val$s.par(this);
                } else {
                    AnonymousClass1.this.suspended.set(true);
                    AnonymousClass1.this.work();
                }
                return Unit.unit();
            }
        };
        final /* synthetic */ Effect1 val$ea;
        final /* synthetic */ Strategy val$s;

        AnonymousClass1(Effect1 effect1, Strategy strategy) {
            this.val$ea = effect1;
            this.val$s = strategy;
        }

        @Override // fj.function.Effect1
        public void f(T a) {
            this.mbox.offer(a);
            work();
        }

        protected void work() {
            if (!this.mbox.isEmpty() && this.suspended.compareAndSet(true, false)) {
                this.val$s.par(this.processor);
            }
        }
    }

    public static <T> Actor<T> queueActor(Strategy<Unit> s, Effect1<T> ea) {
        return actor(Strategy.seqStrategy(), new AnonymousClass1(ea, s));
    }

    private Actor(final Strategy<Unit> s, final F<A, P1<Unit>> e) {
        this.s = s;
        this.f = new F<A, P1<Unit>>() { // from class: fj.control.parallel.Actor.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ P1<Unit> f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.F
            public P1<Unit> f(A a) {
                return s.par((P1) e.f(a));
            }
        };
    }

    public static <A> Actor<A> actor(Strategy<Unit> s, Effect1<A> e) {
        return new Actor<>(s, P1.curry(Effect.f(e)));
    }

    public static <A> Actor<A> actor(Strategy<Unit> s, F<A, P1<Unit>> e) {
        return new Actor<>(s, e);
    }

    public P1<Unit> act(A a) {
        return this.f.f(a);
    }

    public <B> Actor<B> comap(final F<B, A> f) {
        return actor(this.s, new F<B, P1<Unit>>() { // from class: fj.control.parallel.Actor.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ P1<Unit> f(Object obj) {
                return f((AnonymousClass3) obj);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // fj.F
            public P1<Unit> f(B b) {
                return Actor.this.act(f.f(b));
            }
        });
    }

    public Actor<Promise<A>> promise() {
        return actor(this.s, new Effect1<Promise<A>>() { // from class: fj.control.parallel.Actor.4
            @Override // fj.function.Effect1
            public /* bridge */ /* synthetic */ void f(Object obj) {
                f((Promise) ((Promise) obj));
            }

            public void f(Promise<A> b) {
                b.to(Actor.this);
            }
        });
    }
}
