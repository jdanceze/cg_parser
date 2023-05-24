package heros.fieldsens;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/AccessPath.class */
public class AccessPath<T> {
    private final T[] accesses;
    private final Set<T> exclusions;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AccessPath.class.desiredAssertionStatus();
    }

    public static <T> AccessPath<T> empty() {
        return new AccessPath<>();
    }

    public AccessPath() {
        this.accesses = (T[]) new Object[0];
        this.exclusions = Sets.newHashSet();
    }

    AccessPath(T[] accesses, Set<T> exclusions) {
        this.accesses = accesses;
        this.exclusions = exclusions;
    }

    public boolean isAccessInExclusions(T fieldReference) {
        return this.exclusions.contains(fieldReference);
    }

    public boolean hasAllExclusionsOf(AccessPath<T> accPath) {
        return this.exclusions.containsAll(accPath.exclusions);
    }

    public AccessPath<T> append(T... fieldReferences) {
        if (fieldReferences.length == 0) {
            return this;
        }
        if (isAccessInExclusions(fieldReferences[0])) {
            throw new IllegalArgumentException("FieldRef " + Arrays.toString(fieldReferences) + " cannot be added to " + toString());
        }
        Object[] copyOf = Arrays.copyOf(this.accesses, this.accesses.length + fieldReferences.length);
        System.arraycopy(fieldReferences, 0, copyOf, this.accesses.length, fieldReferences.length);
        return new AccessPath<>(copyOf, Sets.newHashSet());
    }

    public AccessPath<T> prepend(T fieldRef) {
        Object[] objArr = new Object[this.accesses.length + 1];
        objArr[0] = fieldRef;
        System.arraycopy(this.accesses, 0, objArr, 1, this.accesses.length);
        return new AccessPath<>(objArr, this.exclusions);
    }

    public AccessPath<T> removeFirst() {
        Object[] objArr = new Object[this.accesses.length - 1];
        System.arraycopy(this.accesses, 1, objArr, 0, this.accesses.length - 1);
        return new AccessPath<>(objArr, this.exclusions);
    }

    public AccessPath<T> appendExcludedFieldReference(Collection<T> fieldReferences) {
        HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
        newExclusions.addAll(this.exclusions);
        return new AccessPath<>(this.accesses, newExclusions);
    }

    public AccessPath<T> appendExcludedFieldReference(T... fieldReferences) {
        HashSet<T> newExclusions = Sets.newHashSet(fieldReferences);
        newExclusions.addAll(this.exclusions);
        return new AccessPath<>(this.accesses, newExclusions);
    }

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/AccessPath$PrefixTestResult.class */
    public enum PrefixTestResult {
        GUARANTEED_PREFIX(2),
        POTENTIAL_PREFIX(1),
        NO_PREFIX(0);
        
        private int value;

        PrefixTestResult(int value) {
            this.value = value;
        }

        public boolean atLeast(PrefixTestResult minimum) {
            return this.value >= minimum.value;
        }
    }

    public PrefixTestResult isPrefixOf(AccessPath<T> accessPath) {
        if (this.accesses.length > accessPath.accesses.length) {
            return PrefixTestResult.NO_PREFIX;
        }
        for (int i = 0; i < this.accesses.length; i++) {
            if (!this.accesses[i].equals(accessPath.accesses[i])) {
                return PrefixTestResult.NO_PREFIX;
            }
        }
        if (this.accesses.length < accessPath.accesses.length) {
            if (this.exclusions.contains(accessPath.accesses[this.accesses.length])) {
                return PrefixTestResult.NO_PREFIX;
            }
            return PrefixTestResult.GUARANTEED_PREFIX;
        } else if (this.exclusions.isEmpty()) {
            return PrefixTestResult.GUARANTEED_PREFIX;
        } else {
            if (accessPath.exclusions.isEmpty()) {
                return PrefixTestResult.NO_PREFIX;
            }
            boolean intersection = !Sets.intersection(this.exclusions, accessPath.exclusions).isEmpty();
            boolean containsAll = this.exclusions.containsAll(accessPath.exclusions);
            boolean oppositeContainsAll = accessPath.exclusions.containsAll(this.exclusions);
            boolean potentialMatch = (!oppositeContainsAll && intersection && (containsAll || oppositeContainsAll)) ? false : true;
            if (potentialMatch) {
                if (oppositeContainsAll) {
                    return PrefixTestResult.GUARANTEED_PREFIX;
                }
                return PrefixTestResult.POTENTIAL_PREFIX;
            }
            return PrefixTestResult.NO_PREFIX;
        }
    }

    public Delta<T> getDeltaTo(AccessPath<T> accPath) {
        if ($assertionsDisabled || isPrefixOf(accPath).atLeast(PrefixTestResult.POTENTIAL_PREFIX)) {
            HashSet<T> mergedExclusions = Sets.newHashSet(accPath.exclusions);
            if (this.accesses.length == accPath.accesses.length) {
                mergedExclusions.addAll(this.exclusions);
            }
            Delta<T> delta = new Delta<>(Arrays.copyOfRange(accPath.accesses, this.accesses.length, accPath.accesses.length), mergedExclusions);
            if (!$assertionsDisabled && ((!isPrefixOf(accPath).atLeast(PrefixTestResult.POTENTIAL_PREFIX) || accPath.isPrefixOf(delta.applyTo(this)) != PrefixTestResult.GUARANTEED_PREFIX) && (isPrefixOf(accPath) != PrefixTestResult.GUARANTEED_PREFIX || !accPath.equals(delta.applyTo(this))))) {
                throw new AssertionError();
            }
            return delta;
        }
        throw new AssertionError();
    }

    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/AccessPath$Delta.class */
    public static class Delta<T> {
        final T[] accesses;
        final Set<T> exclusions;

        protected Delta(T[] accesses, Set<T> exclusions) {
            this.accesses = accesses;
            this.exclusions = exclusions;
        }

        public boolean canBeAppliedTo(AccessPath<T> accPath) {
            return this.accesses.length <= 0 || !accPath.isAccessInExclusions(this.accesses[0]);
        }

        public AccessPath<T> applyTo(AccessPath<T> accPath) {
            return accPath.append(this.accesses).appendExcludedFieldReference(this.exclusions);
        }

        public String toString() {
            String result = this.accesses.length > 0 ? "." + Joiner.on(".").join(this.accesses) : "";
            if (!this.exclusions.isEmpty()) {
                result = result + "^" + Joiner.on(",").join(this.exclusions);
            }
            return result;
        }

        public int hashCode() {
            int result = (31 * 1) + Arrays.hashCode(this.accesses);
            return (31 * result) + (this.exclusions == null ? 0 : this.exclusions.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Delta other = (Delta) obj;
            if (!Arrays.equals(this.accesses, other.accesses)) {
                return false;
            }
            if (this.exclusions == null) {
                if (other.exclusions != null) {
                    return false;
                }
                return true;
            } else if (!this.exclusions.equals(other.exclusions)) {
                return false;
            } else {
                return true;
            }
        }

        public static <T> Delta<T> empty() {
            return new Delta<>(new Object[0], Sets.newHashSet());
        }
    }

    public AccessPath<T> mergeExcludedFieldReferences(AccessPath<T> accPath) {
        HashSet<T> newExclusions = Sets.newHashSet(this.exclusions);
        newExclusions.addAll(accPath.exclusions);
        return new AccessPath<>(this.accesses, newExclusions);
    }

    public boolean canRead(T field) {
        return this.accesses.length > 0 && this.accesses[0].equals(field);
    }

    public boolean isEmpty() {
        return this.exclusions.isEmpty() && this.accesses.length == 0;
    }

    public int hashCode() {
        int result = (31 * 1) + Arrays.hashCode(this.accesses);
        return (31 * result) + (this.exclusions == null ? 0 : this.exclusions.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccessPath other = (AccessPath) obj;
        if (!Arrays.equals(this.accesses, other.accesses)) {
            return false;
        }
        if (this.exclusions == null) {
            if (other.exclusions != null) {
                return false;
            }
            return true;
        } else if (!this.exclusions.equals(other.exclusions)) {
            return false;
        } else {
            return true;
        }
    }

    public String toString() {
        String result = this.accesses.length > 0 ? "." + Joiner.on(".").join(this.accesses) : "";
        if (!this.exclusions.isEmpty()) {
            result = result + "^" + Joiner.on(",").join(this.exclusions);
        }
        return result;
    }

    public AccessPath<T> removeAnyAccess() {
        if (this.accesses.length > 0) {
            return new AccessPath<>(new Object[0], this.exclusions);
        }
        return this;
    }

    public boolean hasEmptyAccessPath() {
        return this.accesses.length == 0;
    }

    public T getFirstAccess() {
        return this.accesses[0];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Set<T> getExclusions() {
        return this.exclusions;
    }
}
