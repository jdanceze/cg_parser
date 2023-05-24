package heros.fieldsens;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/InterestCallback.class */
public interface InterestCallback<Field, Fact, Stmt, Method> {
    void interest(PerAccessPathMethodAnalyzer<Field, Fact, Stmt, Method> perAccessPathMethodAnalyzer, Resolver<Field, Fact, Stmt, Method> resolver);

    void canBeResolvedEmpty();
}
