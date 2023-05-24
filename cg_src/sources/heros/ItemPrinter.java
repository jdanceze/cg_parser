package heros;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/ItemPrinter.class */
public interface ItemPrinter<N, D, M> {
    public static final ItemPrinter<Object, Object, Object> DEFAULT_PRINTER = new ItemPrinter<Object, Object, Object>() { // from class: heros.ItemPrinter.1
        @Override // heros.ItemPrinter
        public String printNode(Object node, Object parentMethod) {
            return node.toString();
        }

        @Override // heros.ItemPrinter
        public String printFact(Object fact) {
            return fact.toString();
        }

        @Override // heros.ItemPrinter
        public String printMethod(Object method) {
            return method.toString();
        }
    };

    String printNode(N n, M m);

    String printFact(D d);

    String printMethod(M m);
}
