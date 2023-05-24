package beaver;
/* loaded from: gencallgraphv3.jar:beaver/Action.class */
public abstract class Action {
    public static final Action NONE = new Action() { // from class: beaver.Action.1
        @Override // beaver.Action
        public Symbol reduce(Symbol[] args, int offset) {
            return new Symbol((Object) null);
        }
    };
    public static final Action RETURN = new Action() { // from class: beaver.Action.2
        @Override // beaver.Action
        public Symbol reduce(Symbol[] args, int offset) {
            return args[offset + 1];
        }
    };

    public abstract Symbol reduce(Symbol[] symbolArr, int i);
}
