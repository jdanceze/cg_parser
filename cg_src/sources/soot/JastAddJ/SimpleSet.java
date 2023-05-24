package soot.JastAddJ;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/SimpleSet.class */
public interface SimpleSet {
    public static final SimpleSet emptySet = new SimpleSet() { // from class: soot.JastAddJ.SimpleSet.1
        @Override // soot.JastAddJ.SimpleSet
        public int size() {
            return 0;
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isEmpty() {
            return true;
        }

        @Override // soot.JastAddJ.SimpleSet
        public SimpleSet add(Object o) {
            if (o instanceof SimpleSet) {
                return (SimpleSet) o;
            }
            return new SimpleSetImpl().add(o);
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean contains(Object o) {
            return false;
        }

        @Override // soot.JastAddJ.SimpleSet
        public Iterator iterator() {
            return Collections.EMPTY_LIST.iterator();
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isSingleton() {
            return false;
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isSingleton(Object o) {
            return false;
        }
    };
    public static final SimpleSet fullSet = new SimpleSet() { // from class: soot.JastAddJ.SimpleSet.2
        @Override // soot.JastAddJ.SimpleSet
        public int size() {
            throw new Error("Operation size not supported on the full set");
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isEmpty() {
            return false;
        }

        @Override // soot.JastAddJ.SimpleSet
        public SimpleSet add(Object o) {
            return this;
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean contains(Object o) {
            return true;
        }

        @Override // soot.JastAddJ.SimpleSet
        public Iterator iterator() {
            throw new Error("Operation iterator not support on the full set");
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isSingleton() {
            return false;
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isSingleton(Object o) {
            return false;
        }
    };

    int size();

    boolean isEmpty();

    SimpleSet add(Object obj);

    Iterator iterator();

    boolean contains(Object obj);

    boolean isSingleton();

    boolean isSingleton(Object obj);

    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/SimpleSet$SimpleSetImpl.class */
    public static class SimpleSetImpl implements SimpleSet {
        private HashSet internalSet;

        public SimpleSetImpl() {
            this.internalSet = new HashSet(4);
        }

        public SimpleSetImpl(Collection c) {
            this.internalSet = new HashSet(c.size());
            this.internalSet.addAll(c);
        }

        private SimpleSetImpl(SimpleSetImpl set) {
            this.internalSet = new HashSet(set.internalSet);
        }

        @Override // soot.JastAddJ.SimpleSet
        public int size() {
            return this.internalSet.size();
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isEmpty() {
            return this.internalSet.isEmpty();
        }

        @Override // soot.JastAddJ.SimpleSet
        public SimpleSet add(Object o) {
            if (this.internalSet.contains(o)) {
                return this;
            }
            SimpleSetImpl set = new SimpleSetImpl(this);
            set.internalSet.add(o);
            return set;
        }

        @Override // soot.JastAddJ.SimpleSet
        public Iterator iterator() {
            return this.internalSet.iterator();
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean contains(Object o) {
            return this.internalSet.contains(o);
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isSingleton() {
            return this.internalSet.size() == 1;
        }

        @Override // soot.JastAddJ.SimpleSet
        public boolean isSingleton(Object o) {
            return isSingleton() && contains(o);
        }
    }
}
