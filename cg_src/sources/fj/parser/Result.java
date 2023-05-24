package fj.parser;

import fj.F;
import fj.F2;
import fj.Function;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/parser/Result.class */
public final class Result<I, A> implements Iterable<A> {
    private final I i;
    private final A a;

    private Result(I i, A a) {
        this.i = i;
        this.a = a;
    }

    public I rest() {
        return this.i;
    }

    public A value() {
        return this.a;
    }

    public <J> Result<J, A> mapRest(F<I, J> f) {
        return result(f.f(this.i), this.a);
    }

    public <J> F<F<I, J>, Result<J, A>> mapRest() {
        return new F<F<I, J>, Result<J, A>>() { // from class: fj.parser.Result.1
            @Override // fj.F
            public Result<J, A> f(F<I, J> f) {
                return Result.this.mapRest(f);
            }
        };
    }

    public <B> Result<I, B> mapValue(F<A, B> f) {
        return result(this.i, f.f(this.a));
    }

    public <B> F<F<A, B>, Result<I, B>> mapValue() {
        return new F<F<A, B>, Result<I, B>>() { // from class: fj.parser.Result.2
            @Override // fj.F
            public Result<I, B> f(F<A, B> f) {
                return Result.this.mapValue(f);
            }
        };
    }

    public <B, J> Result<J, B> bimap(F<I, J> f, F<A, B> g) {
        return mapRest(f).mapValue(g);
    }

    public <B, J> F<F<I, J>, F<F<A, B>, Result<J, B>>> bimap() {
        return Function.curry(new F2<F<I, J>, F<A, B>, Result<J, B>>() { // from class: fj.parser.Result.3
            @Override // fj.F2
            public Result<J, B> f(F<I, J> f, F<A, B> g) {
                return Result.this.bimap(f, g);
            }
        });
    }

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return new Iterator<A>() { // from class: fj.parser.Result.4
            private boolean r;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return !this.r;
            }

            @Override // java.util.Iterator
            public A next() {
                if (this.r) {
                    throw new NoSuchElementException();
                }
                this.r = true;
                return (A) Result.this.a;
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static <A, I> Result<I, A> result(I i, A a) {
        return new Result<>(i, a);
    }

    public static <A, I> F<I, F<A, Result<I, A>>> result() {
        return Function.curry(new F2<I, A, Result<I, A>>() { // from class: fj.parser.Result.5
            @Override // fj.F2
            public /* bridge */ /* synthetic */ Object f(Object obj, Object obj2) {
                return f((AnonymousClass5) obj, obj2);
            }

            @Override // fj.F2
            public Result<I, A> f(I i, A a) {
                return Result.result(i, a);
            }
        });
    }
}
