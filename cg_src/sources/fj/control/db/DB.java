package fj.control.db;

import fj.F;
import fj.Function;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Callable;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/control/db/DB.class */
public abstract class DB<A> {
    public abstract A run(Connection connection) throws SQLException;

    public static <A> DB<A> db(final F<Connection, A> f) {
        return new DB<A>() { // from class: fj.control.db.DB.1
            @Override // fj.control.db.DB
            public A run(Connection c) {
                return (A) F.this.f(c);
            }
        };
    }

    public final F<Connection, Callable<A>> asFunction() {
        return new F<Connection, Callable<A>>() { // from class: fj.control.db.DB.2
            @Override // fj.F
            public Callable<A> f(final Connection c) {
                return new Callable<A>() { // from class: fj.control.db.DB.2.1
                    @Override // java.util.concurrent.Callable
                    public A call() throws Exception {
                        return (A) DB.this.run(c);
                    }
                };
            }
        };
    }

    public final <B> DB<B> map(final F<A, B> f) {
        return new DB<B>() { // from class: fj.control.db.DB.3
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.control.db.DB
            public B run(Connection c) throws SQLException {
                return f.f(DB.this.run(c));
            }
        };
    }

    public static <A, B> F<DB<A>, DB<B>> liftM(final F<A, B> f) {
        return new F<DB<A>, DB<B>>() { // from class: fj.control.db.DB.4
            @Override // fj.F
            public /* bridge */ /* synthetic */ Object f(Object obj) {
                return f((DB) ((DB) obj));
            }

            public DB<B> f(DB<A> a) {
                return a.map(F.this);
            }
        };
    }

    public static <A> DB<A> unit(final A a) {
        return new DB<A>() { // from class: fj.control.db.DB.5
            @Override // fj.control.db.DB
            public A run(Connection c) {
                return (A) a;
            }
        };
    }

    public final <B> DB<B> bind(final F<A, DB<B>> f) {
        return new DB<B>() { // from class: fj.control.db.DB.6
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r0v4, types: [B, java.lang.Object] */
            @Override // fj.control.db.DB
            public B run(Connection c) throws SQLException {
                return ((DB) f.f(DB.this.run(c))).run(c);
            }
        };
    }

    public static <A> DB<A> join(DB<DB<A>> a) {
        return (DB<A>) a.bind(Function.identity());
    }
}
