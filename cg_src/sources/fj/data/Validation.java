package fj.data;

import fj.Bottom;
import fj.F;
import fj.F2;
import fj.F3;
import fj.F4;
import fj.F5;
import fj.F6;
import fj.F7;
import fj.F8;
import fj.Function;
import fj.P;
import fj.P1;
import fj.Semigroup;
import fj.Show;
import fj.Unit;
import fj.function.Effect1;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Validation.class */
public class Validation<E, T> implements Iterable<T> {
    private final Either<E, T> e;
    public static final F<String, Validation<NumberFormatException, Byte>> parseByte = new F<String, Validation<NumberFormatException, Byte>>() { // from class: fj.data.Validation.11
        @Override // fj.F
        public Validation<NumberFormatException, Byte> f(String s) {
            return Validation.parseByte(s);
        }
    };
    public static final F<String, Validation<NumberFormatException, Double>> parseDouble = new F<String, Validation<NumberFormatException, Double>>() { // from class: fj.data.Validation.12
        @Override // fj.F
        public Validation<NumberFormatException, Double> f(String s) {
            return Validation.parseDouble(s);
        }
    };
    public static final F<String, Validation<NumberFormatException, Float>> parseFloat = new F<String, Validation<NumberFormatException, Float>>() { // from class: fj.data.Validation.13
        @Override // fj.F
        public Validation<NumberFormatException, Float> f(String s) {
            return Validation.parseFloat(s);
        }
    };
    public static final F<String, Validation<NumberFormatException, Integer>> parseInt = new F<String, Validation<NumberFormatException, Integer>>() { // from class: fj.data.Validation.14
        @Override // fj.F
        public Validation<NumberFormatException, Integer> f(String s) {
            return Validation.parseInt(s);
        }
    };
    public static final F<String, Validation<NumberFormatException, Long>> parseLong = new F<String, Validation<NumberFormatException, Long>>() { // from class: fj.data.Validation.15
        @Override // fj.F
        public Validation<NumberFormatException, Long> f(String s) {
            return Validation.parseLong(s);
        }
    };
    public static final F<String, Validation<NumberFormatException, Short>> parseShort = new F<String, Validation<NumberFormatException, Short>>() { // from class: fj.data.Validation.16
        @Override // fj.F
        public Validation<NumberFormatException, Short> f(String s) {
            return Validation.parseShort(s);
        }
    };

    protected Validation(Either<E, T> e) {
        this.e = e;
    }

    public boolean isFail() {
        return this.e.isLeft();
    }

    public boolean isSuccess() {
        return this.e.isRight();
    }

    public E fail() {
        if (isFail()) {
            return this.e.left().value();
        }
        throw Bottom.error("Validation: fail on success value");
    }

    public T success() {
        if (isSuccess()) {
            return this.e.right().value();
        }
        throw Bottom.error("Validation: success on fail value");
    }

    public <X> X validation(F<E, X> fail, F<T, X> success) {
        return (X) this.e.either(fail, success);
    }

    public Validation<E, T>.FailProjection<E, T> f() {
        return new FailProjection<>(this);
    }

    public Either<E, T> toEither() {
        return this.e;
    }

    public T successE(P1<String> err) {
        return this.e.right().valueE(err);
    }

    public T successE(String err) {
        return this.e.right().valueE(P.p(err));
    }

    public T orSuccess(P1<T> t) {
        return this.e.right().orValue(t);
    }

    public T orSuccess(T t) {
        return this.e.right().orValue(P.p(t));
    }

    public T on(F<E, T> f) {
        return this.e.right().on(f);
    }

    public Unit foreach(F<T, Unit> f) {
        return this.e.right().foreach(f);
    }

    public void foreachDoEffect(Effect1<T> f) {
        this.e.right().foreachDoEffect(f);
    }

    public <A> Validation<E, A> map(F<T, A> f) {
        if (isFail()) {
            return fail(fail());
        }
        return success(f.f(success()));
    }

    public <A> Validation<E, A> bind(F<T, Validation<E, A>> f) {
        return isSuccess() ? f.f(success()) : fail(fail());
    }

    public <A> Validation<E, A> sequence(Validation<E, A> v) {
        return bind(Function.constant(v));
    }

    public <A> Option<Validation<A, T>> filter(F<T, Boolean> f) {
        return this.e.right().filter(f).map(validation());
    }

    public <A> Validation<E, A> apply(Validation<E, F<T, A>> v) {
        return v.bind(new F<F<T, A>, Validation<E, A>>() { // from class: fj.data.Validation.1
            {
                Validation.this = this;
            }

            @Override // fj.F
            public Validation<E, A> f(F<T, A> f) {
                return Validation.this.map(f);
            }
        });
    }

    public boolean forall(F<T, Boolean> f) {
        return this.e.right().forall(f);
    }

    public boolean exists(F<T, Boolean> f) {
        return this.e.right().exists(f);
    }

    public List<T> toList() {
        return this.e.right().toList();
    }

    public Option<T> toOption() {
        return this.e.right().toOption();
    }

    public Array<T> toArray() {
        return this.e.right().toArray();
    }

    public Stream<T> toStream() {
        return this.e.right().toStream();
    }

    public <A> Validation<E, A> accumapply(Semigroup<E> s, Validation<E, F<T, A>> v) {
        E fail;
        if (isFail()) {
            if (v.isFail()) {
                fail = s.sum(v.fail(), fail());
            } else {
                fail = fail();
            }
            return fail(fail);
        } else if (v.isFail()) {
            return fail(v.fail());
        } else {
            return success(v.success().f(success()));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B> Validation<E, B> accumulate(Semigroup<E> s, Validation<E, A> va, F<T, F<A, B>> f) {
        return (Validation<E, A>) va.accumapply(s, map(f));
    }

    public <A, B> Validation<E, B> accumulate(Semigroup<E> s, Validation<E, A> va, F2<T, A, B> f) {
        return (Validation<E, A>) va.accumapply(s, map(Function.curry(f)));
    }

    public <A> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va) {
        return accumulate(s, va, (F2<T, A, Unit>) new F2<T, A, Unit>() { // from class: fj.data.Validation.2
            {
                Validation.this = this;
            }

            @Override // fj.F2
            public /* bridge */ /* synthetic */ Unit f(Object obj, Object obj2) {
                return f((AnonymousClass2) obj, obj2);
            }

            @Override // fj.F2
            public Unit f(T t, A a) {
                return Unit.unit();
            }
        }).f().toOption();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B, C> Validation<E, C> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, F<T, F<A, F<B, C>>> f) {
        return (Validation<E, A>) vb.accumapply(s, accumulate(s, va, f));
    }

    public <A, B, C> Validation<E, C> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, F3<T, A, B, C> f) {
        return (Validation<E, A>) vb.accumapply(s, accumulate(s, va, Function.curry(f)));
    }

    public <A, B> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb) {
        return accumulate(s, va, vb, (F3<T, A, B, Unit>) new F3<T, A, B, Unit>() { // from class: fj.data.Validation.3
            {
                Validation.this = this;
            }

            @Override // fj.F3
            public /* bridge */ /* synthetic */ Unit f(Object obj, Object obj2, Object obj3) {
                return f((AnonymousClass3) obj, obj2, obj3);
            }

            @Override // fj.F3
            public Unit f(T t, A a, B b) {
                return Unit.unit();
            }
        }).f().toOption();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B, C, D> Validation<E, D> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, F<T, F<A, F<B, F<C, D>>>> f) {
        return (Validation<E, A>) vc.accumapply(s, accumulate(s, va, vb, f));
    }

    public <A, B, C, D> Validation<E, D> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, F4<T, A, B, C, D> f) {
        return (Validation<E, A>) vc.accumapply(s, accumulate(s, va, vb, Function.curry(f)));
    }

    public <A, B, C> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc) {
        return accumulate(s, va, vb, vc, (F4<T, A, B, C, Unit>) new F4<T, A, B, C, Unit>() { // from class: fj.data.Validation.4
            {
                Validation.this = this;
            }

            @Override // fj.F4
            public /* bridge */ /* synthetic */ Unit f(Object obj, Object obj2, Object obj3, Object obj4) {
                return f((AnonymousClass4) obj, obj2, obj3, obj4);
            }

            @Override // fj.F4
            public Unit f(T t, A a, B b, C c) {
                return Unit.unit();
            }
        }).f().toOption();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B, C, D, E$> Validation<E, E$> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, F<T, F<A, F<B, F<C, F<D, E$>>>>> f) {
        return (Validation<E, A>) vd.accumapply(s, accumulate(s, va, vb, vc, f));
    }

    public <A, B, C, D, E$> Validation<E, E$> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, F5<T, A, B, C, D, E$> f) {
        return (Validation<E, A>) vd.accumapply(s, accumulate(s, va, vb, vc, Function.curry(f)));
    }

    public <A, B, C, D> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd) {
        return accumulate(s, va, vb, vc, vd, (F5<T, A, B, C, D, Unit>) new F5<T, A, B, C, D, Unit>() { // from class: fj.data.Validation.5
            {
                Validation.this = this;
            }

            @Override // fj.F5
            public /* bridge */ /* synthetic */ Unit f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
                return f((AnonymousClass5) obj, obj2, obj3, obj4, obj5);
            }

            @Override // fj.F5
            public Unit f(T t, A a, B b, C c, D d) {
                return Unit.unit();
            }
        }).f().toOption();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B, C, D, E$, F$> Validation<E, F$> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, F<T, F<A, F<B, F<C, F<D, F<E$, F$>>>>>> f) {
        return (Validation<E, A>) ve.accumapply(s, accumulate(s, va, vb, vc, vd, f));
    }

    public <A, B, C, D, E$, F$> Validation<E, F$> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, F6<T, A, B, C, D, E$, F$> f) {
        return (Validation<E, A>) ve.accumapply(s, accumulate(s, va, vb, vc, vd, Function.curry(f)));
    }

    public <A, B, C, D, E$> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve) {
        return accumulate(s, va, vb, vc, vd, ve, (F6<T, A, B, C, D, E$, Unit>) new F6<T, A, B, C, D, E$, Unit>() { // from class: fj.data.Validation.6
            {
                Validation.this = this;
            }

            @Override // fj.F6
            public /* bridge */ /* synthetic */ Unit f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6) {
                return f((AnonymousClass6) obj, obj2, obj3, obj4, obj5, obj6);
            }

            @Override // fj.F6
            public Unit f(T t, A a, B b, C c, D d, E$ e) {
                return Unit.unit();
            }
        }).f().toOption();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B, C, D, E$, F$, G> Validation<E, G> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, F<T, F<A, F<B, F<C, F<D, F<E$, F<F$, G>>>>>>> f) {
        return (Validation<E, A>) vf.accumapply(s, accumulate(s, va, vb, vc, vd, ve, f));
    }

    public <A, B, C, D, E$, F$, G> Validation<E, G> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, F7<T, A, B, C, D, E$, F$, G> f) {
        return (Validation<E, A>) vf.accumapply(s, accumulate(s, va, vb, vc, vd, ve, Function.curry(f)));
    }

    public <A, B, C, D, E$, F$> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf) {
        return accumulate(s, va, vb, vc, vd, ve, vf, (F7<T, A, B, C, D, E$, F$, Unit>) new F7<T, A, B, C, D, E$, F$, Unit>() { // from class: fj.data.Validation.7
            {
                Validation.this = this;
            }

            @Override // fj.F7
            public /* bridge */ /* synthetic */ Unit f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7) {
                return f((AnonymousClass7) obj, obj2, obj3, obj4, obj5, obj6, obj7);
            }

            @Override // fj.F7
            public Unit f(T t, A a, B b, C c, D d, E$ e, F$ f) {
                return Unit.unit();
            }
        }).f().toOption();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <A, B, C, D, E$, F$, G, H> Validation<E, H> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, Validation<E, G> vg, F<T, F<A, F<B, F<C, F<D, F<E$, F<F$, F<G, H>>>>>>>> f) {
        return (Validation<E, A>) vg.accumapply(s, accumulate(s, va, vb, vc, vd, ve, vf, f));
    }

    public <A, B, C, D, E$, F$, G, H> Validation<E, H> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, Validation<E, G> vg, F8<T, A, B, C, D, E$, F$, G, H> f) {
        return (Validation<E, A>) vg.accumapply(s, accumulate(s, va, vb, vc, vd, ve, vf, Function.curry(f)));
    }

    public <A, B, C, D, E$, F$, G> Option<E> accumulate(Semigroup<E> s, Validation<E, A> va, Validation<E, B> vb, Validation<E, C> vc, Validation<E, D> vd, Validation<E, E$> ve, Validation<E, F$> vf, Validation<E, G> vg) {
        return accumulate(s, va, vb, vc, vd, ve, vf, vg, (F8<T, A, B, C, D, E$, F$, G, Unit>) new F8<T, A, B, C, D, E$, F$, G, Unit>() { // from class: fj.data.Validation.8
            {
                Validation.this = this;
            }

            @Override // fj.F8
            public /* bridge */ /* synthetic */ Unit f(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8) {
                return f((AnonymousClass8) obj, obj2, obj3, obj4, obj5, obj6, obj7, obj8);
            }

            @Override // fj.F8
            public Unit f(T t, A a, B b, C c, D d, E$ e, F$ f, G g) {
                return Unit.unit();
            }
        }).f().toOption();
    }

    @Override // java.lang.Iterable
    public Iterator<T> iterator() {
        return toEither().right().iterator();
    }

    public Validation<List<E>, T> accumulate() {
        if (isFail()) {
            return fail(List.single(fail()));
        }
        return success(success());
    }

    public <B> Validation<List<E>, B> accumulate(F<T, B> f) {
        if (isFail()) {
            return fail(List.single(fail()));
        }
        return success(f.f(success()));
    }

    public <B, C> Validation<List<E>, C> accumulate(Validation<E, B> v2, F2<T, B, C> f) {
        List<E> list = List.nil();
        if (isFail()) {
            list = list.cons((List<E>) fail());
        }
        if (v2.isFail()) {
            list = list.cons((List<E>) v2.fail());
        }
        if (!list.isEmpty()) {
            return fail(list);
        }
        return success(f.f(success(), v2.success()));
    }

    public <B, C, D> Validation<List<E>, D> accumulate(Validation<E, B> v2, Validation<E, C> v3, F3<T, B, C, D> f) {
        List<E> list = fails(List.list(this, v2, v3));
        if (!list.isEmpty()) {
            return fail(list);
        }
        return success(f.f(success(), v2.success(), v3.success()));
    }

    public <B, C, D, $E> Validation<List<E>, E> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, F4<T, B, C, D, E> f) {
        List<E> list = fails(List.list(this, v2, v3, v4));
        if (!list.isEmpty()) {
            return fail(list);
        }
        return success(f.f(success(), v2.success(), v3.success(), v4.success()));
    }

    public <B, C, D, $E, $F> Validation<List<E>, $F> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, Validation<E, $E> v5, F5<T, B, C, D, $E, $F> f) {
        List<E> list = fails(List.list(this, v2, v3, v4, v5));
        if (!list.isEmpty()) {
            return fail(list);
        }
        return success(f.f(success(), v2.success(), v3.success(), v4.success(), v5.success()));
    }

    public <B, C, D, $E, $F, G> Validation<List<E>, G> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, Validation<E, $E> v5, Validation<E, $F> v6, F6<T, B, C, D, $E, $F, G> f) {
        List<E> list = fails(List.list(this, v2, v3, v4, v5));
        if (!list.isEmpty()) {
            return fail(list);
        }
        return success(f.f(success(), v2.success(), v3.success(), v4.success(), v5.success(), v6.success()));
    }

    public <B, C, D, $E, $F, G, H> Validation<List<E>, H> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, Validation<E, $E> v5, Validation<E, $F> v6, Validation<E, G> v7, F7<T, B, C, D, $E, $F, G, H> f) {
        List<E> list = fails(List.list(this, v2, v3, v4, v5));
        if (!list.isEmpty()) {
            return fail(list);
        }
        return success(f.f(success(), v2.success(), v3.success(), v4.success(), v5.success(), v6.success(), v7.success()));
    }

    public <B, C, D, $E, $F, G, H, I> Validation<List<E>, I> accumulate(Validation<E, B> v2, Validation<E, C> v3, Validation<E, D> v4, Validation<E, $E> v5, Validation<E, $F> v6, Validation<E, G> v7, Validation<E, H> v8, F8<T, B, C, D, $E, $F, G, H, I> f) {
        List<E> list = fails(List.list(this, v2, v3, v4, v5));
        if (!list.isEmpty()) {
            return fail(list);
        }
        return success(f.f(success(), v2.success(), v3.success(), v4.success(), v5.success(), v6.success(), v7.success(), v8.success()));
    }

    public static <A, E> Validation<List<E>, List<A>> sequence(List<Validation<E, A>> list) {
        F2<Validation<E, A>, B, B> f2;
        f2 = Validation$$Lambda$1.instance;
        return (Validation) list.foldRight((F2<Validation<E, A>, F2<Validation<E, A>, B, B>, F2<Validation<E, A>, B, B>>) f2, (F2<Validation<E, A>, B, B>) success(List.nil()));
    }

    public static /* synthetic */ Validation lambda$sequence$41(Validation v, Validation acc) {
        if (acc.isFail() && v.isFail()) {
            return validation(acc.toEither().left().map(Validation$$Lambda$6.lambdaFactory$(v)));
        }
        if (acc.isSuccess() && v.isSuccess()) {
            return acc.map(Validation$$Lambda$7.lambdaFactory$(v));
        }
        return acc;
    }

    public static /* synthetic */ List lambda$null$39(Validation validation, List l) {
        return l.cons((List) validation.fail());
    }

    public static /* synthetic */ List lambda$null$40(Validation validation, List l) {
        return l.cons((List) validation.success());
    }

    public static <A, E> List<E> fails(List<Validation<E, ?>> list) {
        F<Validation<E, ?>, Boolean> f;
        F<Validation<E, ?>, B> f2;
        f = Validation$$Lambda$2.instance;
        List<Validation<E, ?>> filter = list.filter(f);
        f2 = Validation$$Lambda$3.instance;
        return (List<E>) filter.map(f2);
    }

    public static /* synthetic */ Boolean lambda$fails$42(Validation v) {
        return Boolean.valueOf(v.isFail());
    }

    public static <A, E> List<A> successes(List<Validation<?, A>> list) {
        F<Validation<?, A>, Boolean> f;
        F<Validation<?, A>, B> f2;
        f = Validation$$Lambda$4.instance;
        List<Validation<?, A>> filter = list.filter(f);
        f2 = Validation$$Lambda$5.instance;
        return (List<A>) filter.map(f2);
    }

    public static /* synthetic */ Boolean lambda$successes$44(Validation v) {
        return Boolean.valueOf(v.isSuccess());
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Validation$FailProjection.class */
    public final class FailProjection<E, T> implements Iterable<E> {
        private final Validation<E, T> v;

        private FailProjection(Validation<E, T> v) {
            Validation.this = this$0;
            this.v = v;
        }

        public Validation<E, T> validation() {
            return this.v;
        }

        public E failE(P1<String> err) {
            return this.v.toEither().left().valueE(err);
        }

        public E failE(String err) {
            return failE(P.p(err));
        }

        public E orFail(P1<E> e) {
            return this.v.toEither().left().orValue(e);
        }

        public E orFail(E e) {
            return orFail((P1) P.p(e));
        }

        public E on(F<T, E> f) {
            return this.v.toEither().left().on(f);
        }

        public Unit foreach(F<E, Unit> f) {
            return this.v.toEither().left().foreach(f);
        }

        public void foreachDoEffect(Effect1<E> f) {
            this.v.toEither().left().foreachDoEffect(f);
        }

        public <A> Validation<A, T> map(F<E, A> f) {
            return Validation.validation(this.v.toEither().left().map(f));
        }

        public <A> Validation<A, T> bind(F<E, Validation<A, T>> f) {
            return this.v.isFail() ? f.f(this.v.fail()) : Validation.success(this.v.success());
        }

        public <A> Validation<A, T> sequence(final Validation<A, T> v) {
            return bind(new F<E, Validation<A, T>>() { // from class: fj.data.Validation.FailProjection.1
                {
                    FailProjection.this = this;
                }

                @Override // fj.F
                public /* bridge */ /* synthetic */ Object f(Object obj) {
                    return f((AnonymousClass1) obj);
                }

                @Override // fj.F
                public Validation<A, T> f(E e) {
                    return v;
                }
            });
        }

        public <A> Option<Validation<E, A>> filter(F<E, Boolean> f) {
            return this.v.toEither().left().filter(f).map(Validation.validation());
        }

        public <A> Validation<A, T> apply(Validation<F<E, A>, T> v) {
            return v.f().bind(new F<F<E, A>, Validation<A, T>>() { // from class: fj.data.Validation.FailProjection.2
                {
                    FailProjection.this = this;
                }

                @Override // fj.F
                public Validation<A, T> f(F<E, A> f) {
                    return FailProjection.this.map(f);
                }
            });
        }

        public boolean forall(F<E, Boolean> f) {
            return this.v.toEither().left().forall(f);
        }

        public boolean exists(F<E, Boolean> f) {
            return this.v.toEither().left().exists(f);
        }

        public List<E> toList() {
            return this.v.toEither().left().toList();
        }

        public Option<E> toOption() {
            return this.v.toEither().left().toOption();
        }

        public Array<E> toArray() {
            return this.v.toEither().left().toArray();
        }

        public Stream<E> toStream() {
            return this.v.toEither().left().toStream();
        }

        @Override // java.lang.Iterable
        public Iterator<E> iterator() {
            return this.v.toEither().left().iterator();
        }
    }

    public Validation<NonEmptyList<E>, T> nel() {
        if (isSuccess()) {
            return success(success());
        }
        return fail(NonEmptyList.nel(fail()));
    }

    public static <E, T> Validation<E, T> validation(Either<E, T> e) {
        return new Validation<>(e);
    }

    public static <E, T> F<Either<E, T>, Validation<E, T>> validation() {
        return new F<Either<E, T>, Validation<E, T>>() { // from class: fj.data.Validation.9
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Either) ((Either) obj));
            }

            public Validation<E, T> f(Either<E, T> e) {
                return Validation.validation(e);
            }
        };
    }

    public static <E, T> F<Validation<E, T>, Either<E, T>> either() {
        return new F<Validation<E, T>, Either<E, T>>() { // from class: fj.data.Validation.10
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Validation) ((Validation) obj));
            }

            public Either<E, T> f(Validation<E, T> v) {
                return v.toEither();
            }
        };
    }

    public static <E, T> Validation<E, T> success(T t) {
        return validation(Either.right(t));
    }

    public static <E, T> Validation<E, T> fail(E e) {
        return validation(Either.left(e));
    }

    public static <E, T> Validation<NonEmptyList<E>, T> failNEL(E e) {
        return fail(NonEmptyList.nel(e));
    }

    public static <E, T> Validation<E, T> condition(boolean c, E e, T t) {
        return c ? success(t) : fail(e);
    }

    public static Validation<NumberFormatException, Byte> parseByte(String s) {
        try {
            return success(Byte.valueOf(Byte.parseByte(s)));
        } catch (NumberFormatException e) {
            return fail(e);
        }
    }

    public static Validation<NumberFormatException, Double> parseDouble(String s) {
        try {
            return success(Double.valueOf(Double.parseDouble(s)));
        } catch (NumberFormatException e) {
            return fail(e);
        }
    }

    public static Validation<NumberFormatException, Float> parseFloat(String s) {
        try {
            return success(Float.valueOf(Float.parseFloat(s)));
        } catch (NumberFormatException e) {
            return fail(e);
        }
    }

    public static Validation<NumberFormatException, Integer> parseInt(String s) {
        try {
            return success(Integer.valueOf(Integer.parseInt(s)));
        } catch (NumberFormatException e) {
            return fail(e);
        }
    }

    public static Validation<NumberFormatException, Long> parseLong(String s) {
        try {
            return success(Long.valueOf(Long.parseLong(s)));
        } catch (NumberFormatException e) {
            return fail(e);
        }
    }

    public static Validation<NumberFormatException, Short> parseShort(String s) {
        try {
            return success(Short.valueOf(Short.parseShort(s)));
        } catch (NumberFormatException e) {
            return fail(e);
        }
    }

    public String toString() {
        return Show.validationShow(Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
