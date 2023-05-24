package fj.data;

import fj.Bottom;
import fj.F;
import fj.Function;
import fj.P;
import fj.P1;
import fj.Show;
import fj.Unit;
import fj.function.Effect1;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Either.class */
public abstract class Either<A, B> {
    public abstract boolean isLeft();

    public abstract boolean isRight();

    private Either() {
    }

    public final Either<A, B>.LeftProjection<A, B> left() {
        return new LeftProjection<>(this);
    }

    public final Either<A, B>.RightProjection<A, B> right() {
        return new RightProjection<>(this);
    }

    public final <X> X either(F<A, X> left, F<B, X> right) {
        if (isLeft()) {
            return left.f(left().value());
        }
        return right.f(right().value());
    }

    public final Either<B, A> swap() {
        if (isLeft()) {
            return new Right(((Left) this).a);
        }
        return new Left(((Right) this).b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Either$Left.class */
    public static final class Left<A, B> extends Either<A, B> {
        private final A a;

        Left(A a) {
            super();
            this.a = a;
        }

        @Override // fj.data.Either
        public boolean isLeft() {
            return true;
        }

        @Override // fj.data.Either
        public boolean isRight() {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Either$Right.class */
    public static final class Right<A, B> extends Either<A, B> {
        private final B b;

        Right(B b) {
            super();
            this.b = b;
        }

        @Override // fj.data.Either
        public boolean isLeft() {
            return false;
        }

        @Override // fj.data.Either
        public boolean isRight() {
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Either$LeftProjection.class */
    public final class LeftProjection<A, B> implements Iterable<A> {
        private final Either<A, B> e;

        private LeftProjection(Either<A, B> e) {
            this.e = e;
        }

        @Override // java.lang.Iterable
        public Iterator<A> iterator() {
            return toCollection().iterator();
        }

        public Either<A, B> either() {
            return this.e;
        }

        public A valueE(P1<String> err) {
            if (this.e.isLeft()) {
                return (A) ((Left) this.e).a;
            }
            throw Bottom.error(err._1());
        }

        public A valueE(String err) {
            return valueE(P.p(err));
        }

        public A value() {
            return valueE(P.p("left.value on Right"));
        }

        public A orValue(P1<A> a) {
            return Either.this.isLeft() ? value() : a._1();
        }

        public A orValue(A a) {
            return Either.this.isLeft() ? value() : a;
        }

        public A on(F<B, A> f) {
            return Either.this.isLeft() ? value() : f.f(this.e.right().value());
        }

        public Unit foreach(F<A, Unit> f) {
            if (Either.this.isLeft()) {
                f.f(value());
            }
            return Unit.unit();
        }

        public void foreachDoEffect(Effect1<A> f) {
            if (Either.this.isLeft()) {
                f.f(value());
            }
        }

        public <X> Either<X, B> map(F<A, X> f) {
            return Either.this.isLeft() ? new Left(f.f(value())) : new Right(this.e.right().value());
        }

        public <X> Either<X, B> bind(F<A, Either<X, B>> f) {
            return Either.this.isLeft() ? f.f(value()) : new Right(this.e.right().value());
        }

        public <X> Either<X, B> sequence(Either<X, B> e) {
            return bind(Function.constant(e));
        }

        public <X> Option<Either<A, X>> filter(F<A, Boolean> f) {
            if (Either.this.isLeft()) {
                if (f.f(value()).booleanValue()) {
                    return Option.some(new Left(value()));
                }
                return Option.none();
            }
            return Option.none();
        }

        public <X> Either<X, B> apply(Either<F<A, X>, B> e) {
            return e.left().bind(new F<F<A, X>, Either<X, B>>() { // from class: fj.data.Either.LeftProjection.1
                @Override // fj.F
                public Either<X, B> f(F<A, X> f) {
                    return LeftProjection.this.map(f);
                }
            });
        }

        public boolean forall(F<A, Boolean> f) {
            return Either.this.isRight() || f.f(value()).booleanValue();
        }

        public boolean exists(F<A, Boolean> f) {
            return Either.this.isLeft() && f.f(value()).booleanValue();
        }

        public List<A> toList() {
            return Either.this.isLeft() ? List.single(value()) : List.nil();
        }

        public Option<A> toOption() {
            return Either.this.isLeft() ? Option.some(value()) : Option.none();
        }

        public Array<A> toArray() {
            if (Either.this.isLeft()) {
                Object[] a = {value()};
                return Array.mkArray(a);
            }
            return Array.mkArray(new Object[0]);
        }

        public Stream<A> toStream() {
            return Either.this.isLeft() ? Stream.single(value()) : Stream.nil();
        }

        public Collection<A> toCollection() {
            return toList().toCollection();
        }
    }

    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Either$RightProjection.class */
    public final class RightProjection<A, B> implements Iterable<B> {
        private final Either<A, B> e;

        private RightProjection(Either<A, B> e) {
            this.e = e;
        }

        @Override // java.lang.Iterable
        public Iterator<B> iterator() {
            return toCollection().iterator();
        }

        public Either<A, B> either() {
            return this.e;
        }

        public B valueE(P1<String> err) {
            if (this.e.isRight()) {
                return (B) ((Right) this.e).b;
            }
            throw Bottom.error(err._1());
        }

        public B value() {
            return valueE(P.p("right.value on Left"));
        }

        public B orValue(P1<B> b) {
            return Either.this.isRight() ? value() : b._1();
        }

        public B on(F<A, B> f) {
            return Either.this.isRight() ? value() : f.f(this.e.left().value());
        }

        public Unit foreach(F<B, Unit> f) {
            if (Either.this.isRight()) {
                f.f(value());
            }
            return Unit.unit();
        }

        public void foreachDoEffect(Effect1<B> f) {
            if (Either.this.isRight()) {
                f.f(value());
            }
        }

        public <X> Either<A, X> map(F<B, X> f) {
            return Either.this.isRight() ? new Right(f.f(value())) : new Left(this.e.left().value());
        }

        public <X> Either<A, X> bind(F<B, Either<A, X>> f) {
            return Either.this.isRight() ? f.f(value()) : new Left(this.e.left().value());
        }

        public <X> Either<A, X> sequence(Either<A, X> e) {
            return bind(Function.constant(e));
        }

        public <X> Option<Either<X, B>> filter(F<B, Boolean> f) {
            if (Either.this.isRight()) {
                if (f.f(value()).booleanValue()) {
                    return Option.some(new Right(value()));
                }
                return Option.none();
            }
            return Option.none();
        }

        public <X> Either<A, X> apply(Either<A, F<B, X>> e) {
            return e.right().bind(new F<F<B, X>, Either<A, X>>() { // from class: fj.data.Either.RightProjection.1
                @Override // fj.F
                public Either<A, X> f(F<B, X> f) {
                    return RightProjection.this.map(f);
                }
            });
        }

        public boolean forall(F<B, Boolean> f) {
            return Either.this.isLeft() || f.f(value()).booleanValue();
        }

        public boolean exists(F<B, Boolean> f) {
            return Either.this.isRight() && f.f(value()).booleanValue();
        }

        public List<B> toList() {
            return Either.this.isRight() ? List.single(value()) : List.nil();
        }

        public Option<B> toOption() {
            return Either.this.isRight() ? Option.some(value()) : Option.none();
        }

        public Array<B> toArray() {
            if (Either.this.isRight()) {
                Object[] a = {value()};
                return Array.mkArray(a);
            }
            return Array.empty();
        }

        public Stream<B> toStream() {
            return Either.this.isRight() ? Stream.single(value()) : Stream.nil();
        }

        public Collection<B> toCollection() {
            return toList().toCollection();
        }
    }

    public static <A, B> Either<A, B> left(A a) {
        return new Left(a);
    }

    public static <A, B> F<A, Either<A, B>> left_() {
        return new F<A, Either<A, B>>() { // from class: fj.data.Either.1
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass1) obj);
            }

            @Override // fj.F
            public Either<A, B> f(A a) {
                return Either.left(a);
            }
        };
    }

    public static <A, B> F<B, Either<A, B>> right_() {
        return new F<B, Either<A, B>>() { // from class: fj.data.Either.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass2) obj);
            }

            @Override // fj.F
            public Either<A, B> f(B b) {
                return Either.right(b);
            }
        };
    }

    public static <A, B> Either<A, B> right(B b) {
        return new Right(b);
    }

    public static <A, B, X> F<F<A, X>, F<Either<A, B>, Either<X, B>>> leftMap_() {
        return new F<F<A, X>, F<Either<A, B>, Either<X, B>>>() { // from class: fj.data.Either.3
            @Override // fj.F
            public F<Either<A, B>, Either<X, B>> f(final F<A, X> axf) {
                return new F<Either<A, B>, Either<X, B>>() { // from class: fj.data.Either.3.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Either) ((Either) obj));
                    }

                    public Either<X, B> f(Either<A, B> e) {
                        return e.left().map(axf);
                    }
                };
            }
        };
    }

    public static <A, B, X> F<F<B, X>, F<Either<A, B>, Either<A, X>>> rightMap_() {
        return new F<F<B, X>, F<Either<A, B>, Either<A, X>>>() { // from class: fj.data.Either.4
            @Override // fj.F
            public F<Either<A, B>, Either<A, X>> f(final F<B, X> axf) {
                return new F<Either<A, B>, Either<A, X>>() { // from class: fj.data.Either.4.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((Either) ((Either) obj));
                    }

                    public Either<A, X> f(Either<A, B> e) {
                        return e.right().map(axf);
                    }
                };
            }
        };
    }

    public static <A, B> Either<A, B> joinLeft(Either<Either<A, B>, B> e) {
        return (Either<A, B>) e.left().bind(Function.identity());
    }

    public static <A, B> Either<A, B> joinRight(Either<A, Either<A, B>> e) {
        return (Either<A, B>) e.right().bind(Function.identity());
    }

    public static <A, X> Either<List<A>, X> sequenceLeft(final List<Either<A, X>> a) {
        if (a.isEmpty()) {
            return left(List.nil());
        }
        return (Either<X, X>) a.head().left().bind((F<A, Either<List<A>, X>>) new F<A, Either<List<A>, X>>() { // from class: fj.data.Either.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass5) obj);
            }

            @Override // fj.F
            public Either<List<A>, X> f(A aa) {
                return Either.sequenceLeft(List.this.tail()).left().map(List.cons_(aa));
            }
        });
    }

    public static <B, X> Either<X, List<B>> sequenceRight(final List<Either<X, B>> a) {
        if (a.isEmpty()) {
            return right(List.nil());
        }
        return (Either<X, X>) a.head().right().bind((F<B, Either<X, List<B>>>) new F<B, Either<X, List<B>>>() { // from class: fj.data.Either.6
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass6) obj);
            }

            @Override // fj.F
            public Either<X, List<B>> f(B bb) {
                return Either.sequenceRight(List.this.tail()).right().map(List.cons_(bb));
            }
        });
    }

    public static <A> A reduce(Either<A, A> e) {
        return e.isLeft() ? e.left().value() : e.right().value();
    }

    public static <A, B> Either<A, B> iif(boolean c, P1<B> right, P1<A> left) {
        return c ? new Right(right._1()) : new Left(left._1());
    }

    public static <A, B> List<A> lefts(List<Either<A, B>> es) {
        return (List) es.foldRight((F<Either<A, B>, F<F<Either<A, B>, F<List<A>, List<A>>>, F<Either<A, B>, F<List<A>, List<A>>>>>) new F<Either<A, B>, F<List<A>, List<A>>>() { // from class: fj.data.Either.7
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Either) ((Either) obj));
            }

            public F<List<A>, List<A>> f(final Either<A, B> e) {
                return new F<List<A>, List<A>>() { // from class: fj.data.Either.7.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((List) ((List) obj));
                    }

                    public List<A> f(List<A> as) {
                        return e.isLeft() ? as.cons((List<A>) e.left().value()) : as;
                    }
                };
            }
        }, (F<Either<A, B>, F<List<A>, List<A>>>) List.nil());
    }

    public static <A, B> List<B> rights(List<Either<A, B>> es) {
        return (List) es.foldRight((F<Either<A, B>, F<F<Either<A, B>, F<List<B>, List<B>>>, F<Either<A, B>, F<List<B>, List<B>>>>>) new F<Either<A, B>, F<List<B>, List<B>>>() { // from class: fj.data.Either.8
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((Either) ((Either) obj));
            }

            public F<List<B>, List<B>> f(final Either<A, B> e) {
                return new F<List<B>, List<B>>() { // from class: fj.data.Either.8.1
                    @Override // fj.F
                    public /* bridge */ /* synthetic */ Object f(Object obj) {
                        return f((List) ((List) obj));
                    }

                    public List<B> f(List<B> bs) {
                        return e.isRight() ? bs.cons((List<B>) e.right().value()) : bs;
                    }
                };
            }
        }, (F<Either<A, B>, F<List<B>, List<B>>>) List.nil());
    }

    public String toString() {
        return Show.eitherShow(Show.anyShow(), Show.anyShow()).showS((Show) this);
    }
}
