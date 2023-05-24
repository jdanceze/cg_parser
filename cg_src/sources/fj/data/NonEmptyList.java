package fj.data;

import fj.F;
import fj.F1Functions;
import fj.data.List;
import fj.function.Effect1;
import java.util.Collection;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/NonEmptyList.class */
public final class NonEmptyList<A> implements Iterable<A> {
    public final A head;
    public final List<A> tail;

    @Override // java.lang.Iterable
    public Iterator<A> iterator() {
        return toCollection().iterator();
    }

    private NonEmptyList(A head, List<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    public NonEmptyList<A> cons(A a) {
        return nel(a, this.tail.cons((List<A>) this.head));
    }

    public NonEmptyList<A> append(NonEmptyList<A> as) {
        List.Buffer<A> b = new List.Buffer<>();
        b.append(this.tail);
        b.snoc(as.head);
        b.append(as.tail);
        List<A> bb = b.toList();
        return nel(this.head, bb);
    }

    public <B> NonEmptyList<B> map(F<A, B> f) {
        return nel(f.f(this.head), this.tail.map(f));
    }

    public <B> NonEmptyList<B> bind(final F<A, NonEmptyList<B>> f) {
        final List.Buffer<B> b = new List.Buffer<>();
        NonEmptyList<B> p = f.f(this.head);
        b.snoc(p.head);
        b.append(p.tail);
        this.tail.foreachDoEffect(new Effect1<A>() { // from class: fj.data.NonEmptyList.1
            @Override // fj.function.Effect1
            public void f(A a) {
                NonEmptyList nonEmptyList = (NonEmptyList) f.f(a);
                b.snoc(nonEmptyList.head);
                b.append(nonEmptyList.tail);
            }
        });
        List<B> bb = b.toList();
        return nel(bb.head(), bb.tail());
    }

    public NonEmptyList<NonEmptyList<A>> sublists() {
        return (NonEmptyList) fromList(Option.somes(toList().toStream().substreams().map(F1Functions.o(new F<List<A>, Option<NonEmptyList<A>>>() { // from class: fj.data.NonEmptyList.2
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Option<NonEmptyList<A>> f(List<A> list) {
                return NonEmptyList.fromList(list);
            }
        }, Conversions.Stream_List())).toList())).some();
    }

    public NonEmptyList<NonEmptyList<A>> tails() {
        return (NonEmptyList) fromList(Option.somes(toList().tails().map((F<List<A>, Option<NonEmptyList<A>>>) new F<List<A>, Option<NonEmptyList<A>>>() { // from class: fj.data.NonEmptyList.3
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((List) ((List) obj));
            }

            public Option<NonEmptyList<A>> f(List<A> list) {
                return NonEmptyList.fromList(list);
            }
        }))).some();
    }

    public <B> NonEmptyList<B> mapTails(F<NonEmptyList<A>, B> f) {
        return tails().map(f);
    }

    public List<A> toList() {
        return this.tail.cons((List<A>) this.head);
    }

    public Collection<A> toCollection() {
        return toList().toCollection();
    }

    public static <A> F<NonEmptyList<A>, List<A>> toList_() {
        return new F<NonEmptyList<A>, List<A>>() { // from class: fj.data.NonEmptyList.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((NonEmptyList) ((NonEmptyList) obj));
            }

            public List<A> f(NonEmptyList<A> as) {
                return as.toList();
            }
        };
    }

    public static <A> NonEmptyList<A> nel(A head, List<A> tail) {
        return new NonEmptyList<>(head, tail);
    }

    public static <A> NonEmptyList<A> nel(A head) {
        return nel(head, List.nil());
    }

    public static <A> F<A, NonEmptyList<A>> nel() {
        return new F<A, NonEmptyList<A>>() { // from class: fj.data.NonEmptyList.5
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((AnonymousClass5) obj);
            }

            @Override // fj.F
            public NonEmptyList<A> f(A a) {
                return NonEmptyList.nel(a);
            }
        };
    }

    public static <A> Option<NonEmptyList<A>> fromList(List<A> as) {
        if (as.isEmpty()) {
            return Option.none();
        }
        return Option.some(nel(as.head(), as.tail()));
    }
}
