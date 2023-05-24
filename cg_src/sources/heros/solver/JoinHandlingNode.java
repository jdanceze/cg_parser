package heros.solver;

import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/JoinHandlingNode.class */
public interface JoinHandlingNode<T> {
    boolean handleJoin(T t);

    JoinKey createJoinKey();

    void setCallingContext(T t);

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/JoinHandlingNode$JoinKey.class */
    public static class JoinKey {
        private Object[] elements;

        public JoinKey(Object... elements) {
            this.elements = elements;
        }

        public int hashCode() {
            int result = (31 * 1) + Arrays.hashCode(this.elements);
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            JoinKey other = (JoinKey) obj;
            if (!Arrays.equals(this.elements, other.elements)) {
                return false;
            }
            return true;
        }
    }
}
