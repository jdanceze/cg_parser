package fj.data;

import fj.F;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.function.Effect1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/Java.class */
public final class Java {
    public static final F<List<Boolean>, BitSet> List_BitSet = new F<List<Boolean>, BitSet>() { // from class: fj.data.Java.2
        @Override // fj.F
        public BitSet f(List<Boolean> bs) {
            final BitSet s = new BitSet(bs.length());
            bs.zipIndex().foreachDoEffect(new Effect1<P2<Boolean, Integer>>() { // from class: fj.data.Java.2.1
                {
                    AnonymousClass2.this = this;
                }

                @Override // fj.function.Effect1
                public void f(P2<Boolean, Integer> bi) {
                    s.set(bi._2().intValue(), bi._1().booleanValue());
                }
            });
            return s;
        }
    };
    public static final F<Array<Boolean>, BitSet> Array_BitSet = new F<Array<Boolean>, BitSet>() { // from class: fj.data.Java.20
        @Override // fj.F
        public BitSet f(Array<Boolean> bs) {
            final BitSet s = new BitSet(bs.length());
            bs.zipIndex().foreachDoEffect(new Effect1<P2<Boolean, Integer>>() { // from class: fj.data.Java.20.1
                {
                    AnonymousClass20.this = this;
                }

                @Override // fj.function.Effect1
                public void f(P2<Boolean, Integer> bi) {
                    s.set(bi._2().intValue(), bi._1().booleanValue());
                }
            });
            return s;
        }
    };
    public static final F<Stream<Boolean>, BitSet> Stream_BitSet = new F<Stream<Boolean>, BitSet>() { // from class: fj.data.Java.39
        @Override // fj.F
        public BitSet f(Stream<Boolean> bs) {
            final BitSet s = new BitSet(bs.length());
            bs.zipIndex().foreachDoEffect(new Effect1<P2<Boolean, Integer>>() { // from class: fj.data.Java.39.1
                {
                    AnonymousClass39.this = this;
                }

                @Override // fj.function.Effect1
                public void f(P2<Boolean, Integer> bi) {
                    s.set(bi._2().intValue(), bi._1().booleanValue());
                }
            });
            return s;
        }
    };
    public static final F<Option<Boolean>, BitSet> Option_BitSet = new F<Option<Boolean>, BitSet>() { // from class: fj.data.Java.57
        @Override // fj.F
        public BitSet f(Option<Boolean> bs) {
            final BitSet s = new BitSet(bs.length());
            bs.foreachDoEffect(new Effect1<Boolean>() { // from class: fj.data.Java.57.1
                {
                    AnonymousClass57.this = this;
                }

                @Override // fj.function.Effect1
                public void f(Boolean b) {
                    if (b.booleanValue()) {
                        s.set(0);
                    }
                }
            });
            return s;
        }
    };
    public static final F<String, ArrayList<Character>> String_ArrayList = Function.compose(List_ArrayList(), Conversions.String_List);
    public static final F<String, java.util.HashSet<Character>> String_HashSet = Function.compose(List_HashSet(), Conversions.String_List);
    public static final F<String, LinkedHashSet<Character>> String_LinkedHashSet = Function.compose(List_LinkedHashSet(), Conversions.String_List);
    public static final F<String, LinkedList<Character>> String_LinkedList = Function.compose(List_LinkedList(), Conversions.String_List);
    public static final F<String, PriorityQueue<Character>> String_PriorityQueue = Function.compose(List_PriorityQueue(), Conversions.String_List);
    public static final F<String, Stack<Character>> String_Stack = Function.compose(List_Stack(), Conversions.String_List);
    public static final F<String, TreeSet<Character>> String_TreeSet = Function.compose(List_TreeSet(), Conversions.String_List);
    public static final F<String, Vector<Character>> String_Vector = Function.compose(List_Vector(), Conversions.String_List);
    public static final F<String, ConcurrentLinkedQueue<Character>> String_ConcurrentLinkedQueue = Function.compose(List_ConcurrentLinkedQueue(), Conversions.String_List);
    public static final F<String, CopyOnWriteArrayList<Character>> String_CopyOnWriteArrayList = Function.compose(List_CopyOnWriteArrayList(), Conversions.String_List);
    public static final F<String, CopyOnWriteArraySet<Character>> String_CopyOnWriteArraySet = Function.compose(List_CopyOnWriteArraySet(), Conversions.String_List);
    public static final F<String, LinkedBlockingQueue<Character>> String_LinkedBlockingQueue = Function.compose(List_LinkedBlockingQueue(), Conversions.String_List);
    public static final F<String, PriorityBlockingQueue<Character>> String_PriorityBlockingQueue = Function.compose(List_PriorityBlockingQueue(), Conversions.String_List);
    public static final F<StringBuffer, ArrayList<Character>> StringBuffer_ArrayList = Function.compose(List_ArrayList(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, java.util.HashSet<Character>> StringBuffer_HashSet = Function.compose(List_HashSet(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, LinkedHashSet<Character>> StringBuffer_LinkedHashSet = Function.compose(List_LinkedHashSet(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, LinkedList<Character>> StringBuffer_LinkedList = Function.compose(List_LinkedList(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, PriorityQueue<Character>> StringBuffer_PriorityQueue = Function.compose(List_PriorityQueue(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, Stack<Character>> StringBuffer_Stack = Function.compose(List_Stack(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, TreeSet<Character>> StringBuffer_TreeSet = Function.compose(List_TreeSet(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, Vector<Character>> StringBuffer_Vector = Function.compose(List_Vector(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, ConcurrentLinkedQueue<Character>> StringBuffer_ConcurrentLinkedQueue = Function.compose(List_ConcurrentLinkedQueue(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, CopyOnWriteArrayList<Character>> StringBuffer_CopyOnWriteArrayList = Function.compose(List_CopyOnWriteArrayList(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, CopyOnWriteArraySet<Character>> StringBuffer_CopyOnWriteArraySet = Function.compose(List_CopyOnWriteArraySet(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, LinkedBlockingQueue<Character>> StringBuffer_LinkedBlockingQueue = Function.compose(List_LinkedBlockingQueue(), Conversions.StringBuffer_List);
    public static final F<StringBuffer, PriorityBlockingQueue<Character>> StringBuffer_PriorityBlockingQueue = Function.compose(List_PriorityBlockingQueue(), Conversions.StringBuffer_List);
    public static final F<StringBuilder, ArrayList<Character>> StringBuilder_ArrayList = Function.compose(List_ArrayList(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, java.util.HashSet<Character>> StringBuilder_HashSet = Function.compose(List_HashSet(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, LinkedHashSet<Character>> StringBuilder_LinkedHashSet = Function.compose(List_LinkedHashSet(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, LinkedList<Character>> StringBuilder_LinkedList = Function.compose(List_LinkedList(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, PriorityQueue<Character>> StringBuilder_PriorityQueue = Function.compose(List_PriorityQueue(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, Stack<Character>> StringBuilder_Stack = Function.compose(List_Stack(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, TreeSet<Character>> StringBuilder_TreeSet = Function.compose(List_TreeSet(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, Vector<Character>> StringBuilder_Vector = Function.compose(List_Vector(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, ConcurrentLinkedQueue<Character>> StringBuilder_ConcurrentLinkedQueue = Function.compose(List_ConcurrentLinkedQueue(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, CopyOnWriteArrayList<Character>> StringBuilder_CopyOnWriteArrayList = Function.compose(List_CopyOnWriteArrayList(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, CopyOnWriteArraySet<Character>> StringBuilder_CopyOnWriteArraySet = Function.compose(List_CopyOnWriteArraySet(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, LinkedBlockingQueue<Character>> StringBuilder_LinkedBlockingQueue = Function.compose(List_LinkedBlockingQueue(), Conversions.StringBuilder_List);
    public static final F<StringBuilder, PriorityBlockingQueue<Character>> StringBuilder_PriorityBlockingQueue = Function.compose(List_PriorityBlockingQueue(), Conversions.StringBuilder_List);
    public static final F<BitSet, List<Boolean>> BitSet_List = new F<BitSet, List<Boolean>>() { // from class: fj.data.Java.76
        @Override // fj.F
        public List<Boolean> f(final BitSet s) {
            return List.unfold(new F<Integer, Option<P2<Boolean, Integer>>>() { // from class: fj.data.Java.76.1
                {
                    AnonymousClass76.this = this;
                }

                @Override // fj.F
                public Option<P2<Boolean, Integer>> f(Integer i) {
                    if (i.intValue() == s.length()) {
                        return Option.none();
                    }
                    return Option.some(P.p(Boolean.valueOf(s.get(i.intValue())), Integer.valueOf(i.intValue() + 1)));
                }
            }, 0);
        }
    };

    private Java() {
        throw new UnsupportedOperationException();
    }

    public static <A> F<List<A>, ArrayList<A>> List_ArrayList() {
        return new F<List<A>, ArrayList<A>>() { // from class: fj.data.Java.1
            @Override // fj.F
            public ArrayList<A> f(List<A> as) {
                return new ArrayList<>(as.toCollection());
            }
        };
    }

    public static <A extends Enum<A>> F<List<A>, EnumSet<A>> List_EnumSet() {
        return (F<List<A>, EnumSet<A>>) new F<List<A>, EnumSet<A>>() { // from class: fj.data.Java.3
            @Override // fj.F
            public EnumSet<A> f(List<A> as) {
                return EnumSet.copyOf(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, java.util.HashSet<A>> List_HashSet() {
        return new F<List<A>, java.util.HashSet<A>>() { // from class: fj.data.Java.4
            @Override // fj.F
            public java.util.HashSet<A> f(List<A> as) {
                return new java.util.HashSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, LinkedHashSet<A>> List_LinkedHashSet() {
        return new F<List<A>, LinkedHashSet<A>>() { // from class: fj.data.Java.5
            @Override // fj.F
            public LinkedHashSet<A> f(List<A> as) {
                return new LinkedHashSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, LinkedList<A>> List_LinkedList() {
        return new F<List<A>, LinkedList<A>>() { // from class: fj.data.Java.6
            @Override // fj.F
            public LinkedList<A> f(List<A> as) {
                return new LinkedList<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, PriorityQueue<A>> List_PriorityQueue() {
        return new F<List<A>, PriorityQueue<A>>() { // from class: fj.data.Java.7
            @Override // fj.F
            public PriorityQueue<A> f(List<A> as) {
                return new PriorityQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, Stack<A>> List_Stack() {
        return new F<List<A>, Stack<A>>() { // from class: fj.data.Java.8
            @Override // fj.F
            public Stack<A> f(List<A> as) {
                Stack<A> s = new Stack<>();
                s.addAll(as.toCollection());
                return s;
            }
        };
    }

    public static <A> F<List<A>, TreeSet<A>> List_TreeSet() {
        return new F<List<A>, TreeSet<A>>() { // from class: fj.data.Java.9
            @Override // fj.F
            public TreeSet<A> f(List<A> as) {
                return new TreeSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, Vector<A>> List_Vector() {
        return new F<List<A>, Vector<A>>() { // from class: fj.data.Java.10
            @Override // fj.F
            public Vector<A> f(List<A> as) {
                return new Vector<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, ArrayBlockingQueue<A>> List_ArrayBlockingQueue(final boolean fair) {
        return new F<List<A>, ArrayBlockingQueue<A>>() { // from class: fj.data.Java.11
            @Override // fj.F
            public ArrayBlockingQueue<A> f(List<A> as) {
                return new ArrayBlockingQueue<>(as.length(), fair, as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, ConcurrentLinkedQueue<A>> List_ConcurrentLinkedQueue() {
        return new F<List<A>, ConcurrentLinkedQueue<A>>() { // from class: fj.data.Java.12
            @Override // fj.F
            public ConcurrentLinkedQueue<A> f(List<A> as) {
                return new ConcurrentLinkedQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, CopyOnWriteArrayList<A>> List_CopyOnWriteArrayList() {
        return new F<List<A>, CopyOnWriteArrayList<A>>() { // from class: fj.data.Java.13
            @Override // fj.F
            public CopyOnWriteArrayList<A> f(List<A> as) {
                return new CopyOnWriteArrayList<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, CopyOnWriteArraySet<A>> List_CopyOnWriteArraySet() {
        return new F<List<A>, CopyOnWriteArraySet<A>>() { // from class: fj.data.Java.14
            @Override // fj.F
            public CopyOnWriteArraySet<A> f(List<A> as) {
                return new CopyOnWriteArraySet<>(as.toCollection());
            }
        };
    }

    public static <A extends Delayed> F<List<A>, DelayQueue<A>> List_DelayQueue() {
        return (F<List<A>, DelayQueue<A>>) new F<List<A>, DelayQueue<A>>() { // from class: fj.data.Java.15
            @Override // fj.F
            public DelayQueue<A> f(List<A> as) {
                return new DelayQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, LinkedBlockingQueue<A>> List_LinkedBlockingQueue() {
        return new F<List<A>, LinkedBlockingQueue<A>>() { // from class: fj.data.Java.16
            @Override // fj.F
            public LinkedBlockingQueue<A> f(List<A> as) {
                return new LinkedBlockingQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, PriorityBlockingQueue<A>> List_PriorityBlockingQueue() {
        return new F<List<A>, PriorityBlockingQueue<A>>() { // from class: fj.data.Java.17
            @Override // fj.F
            public PriorityBlockingQueue<A> f(List<A> as) {
                return new PriorityBlockingQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<List<A>, SynchronousQueue<A>> List_SynchronousQueue(final boolean fair) {
        return new F<List<A>, SynchronousQueue<A>>() { // from class: fj.data.Java.18
            @Override // fj.F
            public SynchronousQueue<A> f(List<A> as) {
                SynchronousQueue<A> q = new SynchronousQueue<>(fair);
                q.addAll(as.toCollection());
                return q;
            }
        };
    }

    public static <A> F<Array<A>, ArrayList<A>> Array_ArrayList() {
        return new F<Array<A>, ArrayList<A>>() { // from class: fj.data.Java.19
            @Override // fj.F
            public ArrayList<A> f(Array<A> as) {
                return new ArrayList<>(as.toCollection());
            }
        };
    }

    public static <A extends Enum<A>> F<Array<A>, EnumSet<A>> Array_EnumSet() {
        return (F<Array<A>, EnumSet<A>>) new F<Array<A>, EnumSet<A>>() { // from class: fj.data.Java.21
            @Override // fj.F
            public EnumSet<A> f(Array<A> as) {
                return EnumSet.copyOf(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, java.util.HashSet<A>> Array_HashSet() {
        return new F<Array<A>, java.util.HashSet<A>>() { // from class: fj.data.Java.22
            @Override // fj.F
            public java.util.HashSet<A> f(Array<A> as) {
                return new java.util.HashSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, LinkedHashSet<A>> Array_LinkedHashSet() {
        return new F<Array<A>, LinkedHashSet<A>>() { // from class: fj.data.Java.23
            @Override // fj.F
            public LinkedHashSet<A> f(Array<A> as) {
                return new LinkedHashSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, LinkedList<A>> Array_LinkedList() {
        return new F<Array<A>, LinkedList<A>>() { // from class: fj.data.Java.24
            @Override // fj.F
            public LinkedList<A> f(Array<A> as) {
                return new LinkedList<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, PriorityQueue<A>> Array_PriorityQueue() {
        return new F<Array<A>, PriorityQueue<A>>() { // from class: fj.data.Java.25
            @Override // fj.F
            public PriorityQueue<A> f(Array<A> as) {
                return new PriorityQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, Stack<A>> Array_Stack() {
        return new F<Array<A>, Stack<A>>() { // from class: fj.data.Java.26
            @Override // fj.F
            public Stack<A> f(Array<A> as) {
                Stack<A> s = new Stack<>();
                s.addAll(as.toCollection());
                return s;
            }
        };
    }

    public static <A> F<Array<A>, TreeSet<A>> Array_TreeSet() {
        return new F<Array<A>, TreeSet<A>>() { // from class: fj.data.Java.27
            @Override // fj.F
            public TreeSet<A> f(Array<A> as) {
                return new TreeSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, Vector<A>> Array_Vector() {
        return new F<Array<A>, Vector<A>>() { // from class: fj.data.Java.28
            @Override // fj.F
            public Vector<A> f(Array<A> as) {
                return new Vector<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, ArrayBlockingQueue<A>> Array_ArrayBlockingQueue(final boolean fair) {
        return new F<Array<A>, ArrayBlockingQueue<A>>() { // from class: fj.data.Java.29
            @Override // fj.F
            public ArrayBlockingQueue<A> f(Array<A> as) {
                return new ArrayBlockingQueue<>(as.length(), fair, as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, ConcurrentLinkedQueue<A>> Array_ConcurrentLinkedQueue() {
        return new F<Array<A>, ConcurrentLinkedQueue<A>>() { // from class: fj.data.Java.30
            @Override // fj.F
            public ConcurrentLinkedQueue<A> f(Array<A> as) {
                return new ConcurrentLinkedQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, CopyOnWriteArrayList<A>> Array_CopyOnWriteArrayList() {
        return new F<Array<A>, CopyOnWriteArrayList<A>>() { // from class: fj.data.Java.31
            @Override // fj.F
            public CopyOnWriteArrayList<A> f(Array<A> as) {
                return new CopyOnWriteArrayList<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, CopyOnWriteArraySet<A>> Array_CopyOnWriteArraySet() {
        return new F<Array<A>, CopyOnWriteArraySet<A>>() { // from class: fj.data.Java.32
            @Override // fj.F
            public CopyOnWriteArraySet<A> f(Array<A> as) {
                return new CopyOnWriteArraySet<>(as.toCollection());
            }
        };
    }

    public static <A extends Delayed> F<Array<A>, DelayQueue<A>> Array_DelayQueue() {
        return (F<Array<A>, DelayQueue<A>>) new F<Array<A>, DelayQueue<A>>() { // from class: fj.data.Java.33
            @Override // fj.F
            public DelayQueue<A> f(Array<A> as) {
                return new DelayQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, LinkedBlockingQueue<A>> Array_LinkedBlockingQueue() {
        return new F<Array<A>, LinkedBlockingQueue<A>>() { // from class: fj.data.Java.34
            @Override // fj.F
            public LinkedBlockingQueue<A> f(Array<A> as) {
                return new LinkedBlockingQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, PriorityBlockingQueue<A>> Array_PriorityBlockingQueue() {
        return new F<Array<A>, PriorityBlockingQueue<A>>() { // from class: fj.data.Java.35
            @Override // fj.F
            public PriorityBlockingQueue<A> f(Array<A> as) {
                return new PriorityBlockingQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Array<A>, SynchronousQueue<A>> Array_SynchronousQueue(final boolean fair) {
        return new F<Array<A>, SynchronousQueue<A>>() { // from class: fj.data.Java.36
            @Override // fj.F
            public SynchronousQueue<A> f(Array<A> as) {
                SynchronousQueue<A> q = new SynchronousQueue<>(fair);
                q.addAll(as.toCollection());
                return q;
            }
        };
    }

    public static <A> F<Stream<A>, Iterable<A>> Stream_Iterable() {
        return new F<Stream<A>, Iterable<A>>() { // from class: fj.data.Java.37
            @Override // fj.F
            public Iterable<A> f(final Stream<A> as) {
                return new Iterable<A>() { // from class: fj.data.Java.37.1
                    {
                        AnonymousClass37.this = this;
                    }

                    @Override // java.lang.Iterable
                    public Iterator<A> iterator() {
                        return new Iterator<A>() { // from class: fj.data.Java.37.1.1
                            private Stream<A> x;

                            {
                                AnonymousClass1.this = this;
                                this.x = as;
                            }

                            @Override // java.util.Iterator
                            public boolean hasNext() {
                                return this.x.isNotEmpty();
                            }

                            /* JADX WARN: Type inference failed for: r0v5, types: [A, java.lang.Object] */
                            @Override // java.util.Iterator
                            public A next() {
                                if (this.x.isEmpty()) {
                                    throw new NoSuchElementException("Empty iterator");
                                }
                                ?? head = this.x.head();
                                this.x = (Stream) this.x.tail()._1();
                                return head;
                            }

                            @Override // java.util.Iterator
                            public void remove() {
                                throw new UnsupportedOperationException();
                            }
                        };
                    }
                };
            }
        };
    }

    public static <A> F<Stream<A>, ArrayList<A>> Stream_ArrayList() {
        return new F<Stream<A>, ArrayList<A>>() { // from class: fj.data.Java.38
            @Override // fj.F
            public ArrayList<A> f(Stream<A> as) {
                return new ArrayList<>(as.toCollection());
            }
        };
    }

    public static <A extends Enum<A>> F<Stream<A>, EnumSet<A>> Stream_EnumSet() {
        return (F<Stream<A>, EnumSet<A>>) new F<Stream<A>, EnumSet<A>>() { // from class: fj.data.Java.40
            @Override // fj.F
            public EnumSet<A> f(Stream<A> as) {
                return EnumSet.copyOf(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, java.util.HashSet<A>> Stream_HashSet() {
        return new F<Stream<A>, java.util.HashSet<A>>() { // from class: fj.data.Java.41
            @Override // fj.F
            public java.util.HashSet<A> f(Stream<A> as) {
                return new java.util.HashSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, LinkedHashSet<A>> Stream_LinkedHashSet() {
        return new F<Stream<A>, LinkedHashSet<A>>() { // from class: fj.data.Java.42
            @Override // fj.F
            public LinkedHashSet<A> f(Stream<A> as) {
                return new LinkedHashSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, LinkedList<A>> Stream_LinkedList() {
        return new F<Stream<A>, LinkedList<A>>() { // from class: fj.data.Java.43
            @Override // fj.F
            public LinkedList<A> f(Stream<A> as) {
                return new LinkedList<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, PriorityQueue<A>> Stream_PriorityQueue() {
        return new F<Stream<A>, PriorityQueue<A>>() { // from class: fj.data.Java.44
            @Override // fj.F
            public PriorityQueue<A> f(Stream<A> as) {
                return new PriorityQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, Stack<A>> Stream_Stack() {
        return new F<Stream<A>, Stack<A>>() { // from class: fj.data.Java.45
            @Override // fj.F
            public Stack<A> f(Stream<A> as) {
                Stack<A> s = new Stack<>();
                s.addAll(as.toCollection());
                return s;
            }
        };
    }

    public static <A> F<Stream<A>, TreeSet<A>> Stream_TreeSet() {
        return new F<Stream<A>, TreeSet<A>>() { // from class: fj.data.Java.46
            @Override // fj.F
            public TreeSet<A> f(Stream<A> as) {
                return new TreeSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, Vector<A>> Stream_Vector() {
        return new F<Stream<A>, Vector<A>>() { // from class: fj.data.Java.47
            @Override // fj.F
            public Vector<A> f(Stream<A> as) {
                return new Vector<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, ArrayBlockingQueue<A>> Stream_ArrayBlockingQueue(final boolean fair) {
        return new F<Stream<A>, ArrayBlockingQueue<A>>() { // from class: fj.data.Java.48
            @Override // fj.F
            public ArrayBlockingQueue<A> f(Stream<A> as) {
                return new ArrayBlockingQueue<>(as.length(), fair, as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, ConcurrentLinkedQueue<A>> Stream_ConcurrentLinkedQueue() {
        return new F<Stream<A>, ConcurrentLinkedQueue<A>>() { // from class: fj.data.Java.49
            @Override // fj.F
            public ConcurrentLinkedQueue<A> f(Stream<A> as) {
                return new ConcurrentLinkedQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, CopyOnWriteArrayList<A>> Stream_CopyOnWriteArrayList() {
        return new F<Stream<A>, CopyOnWriteArrayList<A>>() { // from class: fj.data.Java.50
            @Override // fj.F
            public CopyOnWriteArrayList<A> f(Stream<A> as) {
                return new CopyOnWriteArrayList<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, CopyOnWriteArraySet<A>> Stream_CopyOnWriteArraySet() {
        return new F<Stream<A>, CopyOnWriteArraySet<A>>() { // from class: fj.data.Java.51
            @Override // fj.F
            public CopyOnWriteArraySet<A> f(Stream<A> as) {
                return new CopyOnWriteArraySet<>(as.toCollection());
            }
        };
    }

    public static <A extends Delayed> F<Stream<A>, DelayQueue<A>> Stream_DelayQueue() {
        return (F<Stream<A>, DelayQueue<A>>) new F<Stream<A>, DelayQueue<A>>() { // from class: fj.data.Java.52
            @Override // fj.F
            public DelayQueue<A> f(Stream<A> as) {
                return new DelayQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, LinkedBlockingQueue<A>> Stream_LinkedBlockingQueue() {
        return new F<Stream<A>, LinkedBlockingQueue<A>>() { // from class: fj.data.Java.53
            @Override // fj.F
            public LinkedBlockingQueue<A> f(Stream<A> as) {
                return new LinkedBlockingQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, PriorityBlockingQueue<A>> Stream_PriorityBlockingQueue() {
        return new F<Stream<A>, PriorityBlockingQueue<A>>() { // from class: fj.data.Java.54
            @Override // fj.F
            public PriorityBlockingQueue<A> f(Stream<A> as) {
                return new PriorityBlockingQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Stream<A>, SynchronousQueue<A>> Stream_SynchronousQueue(final boolean fair) {
        return new F<Stream<A>, SynchronousQueue<A>>() { // from class: fj.data.Java.55
            @Override // fj.F
            public SynchronousQueue<A> f(Stream<A> as) {
                SynchronousQueue<A> q = new SynchronousQueue<>(fair);
                q.addAll(as.toCollection());
                return q;
            }
        };
    }

    public static <A> F<Option<A>, ArrayList<A>> Option_ArrayList() {
        return new F<Option<A>, ArrayList<A>>() { // from class: fj.data.Java.56
            @Override // fj.F
            public ArrayList<A> f(Option<A> as) {
                return new ArrayList<>(as.toCollection());
            }
        };
    }

    public static <A extends Enum<A>> F<Option<A>, EnumSet<A>> Option_EnumSet() {
        return (F<Option<A>, EnumSet<A>>) new F<Option<A>, EnumSet<A>>() { // from class: fj.data.Java.58
            @Override // fj.F
            public EnumSet<A> f(Option<A> as) {
                return EnumSet.copyOf(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, java.util.HashSet<A>> Option_HashSet() {
        return new F<Option<A>, java.util.HashSet<A>>() { // from class: fj.data.Java.59
            @Override // fj.F
            public java.util.HashSet<A> f(Option<A> as) {
                return new java.util.HashSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, LinkedHashSet<A>> Option_LinkedHashSet() {
        return new F<Option<A>, LinkedHashSet<A>>() { // from class: fj.data.Java.60
            @Override // fj.F
            public LinkedHashSet<A> f(Option<A> as) {
                return new LinkedHashSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, LinkedList<A>> Option_LinkedList() {
        return new F<Option<A>, LinkedList<A>>() { // from class: fj.data.Java.61
            @Override // fj.F
            public LinkedList<A> f(Option<A> as) {
                return new LinkedList<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, PriorityQueue<A>> Option_PriorityQueue() {
        return new F<Option<A>, PriorityQueue<A>>() { // from class: fj.data.Java.62
            @Override // fj.F
            public PriorityQueue<A> f(Option<A> as) {
                return new PriorityQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, Stack<A>> Option_Stack() {
        return new F<Option<A>, Stack<A>>() { // from class: fj.data.Java.63
            @Override // fj.F
            public Stack<A> f(Option<A> as) {
                Stack<A> s = new Stack<>();
                s.addAll(as.toCollection());
                return s;
            }
        };
    }

    public static <A> F<Option<A>, TreeSet<A>> Option_TreeSet() {
        return new F<Option<A>, TreeSet<A>>() { // from class: fj.data.Java.64
            @Override // fj.F
            public TreeSet<A> f(Option<A> as) {
                return new TreeSet<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, Vector<A>> Option_Vector() {
        return new F<Option<A>, Vector<A>>() { // from class: fj.data.Java.65
            @Override // fj.F
            public Vector<A> f(Option<A> as) {
                return new Vector<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, ArrayBlockingQueue<A>> Option_ArrayBlockingQueue(final boolean fair) {
        return new F<Option<A>, ArrayBlockingQueue<A>>() { // from class: fj.data.Java.66
            @Override // fj.F
            public ArrayBlockingQueue<A> f(Option<A> as) {
                return new ArrayBlockingQueue<>(as.length(), fair, as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, ConcurrentLinkedQueue<A>> Option_ConcurrentLinkedQueue() {
        return new F<Option<A>, ConcurrentLinkedQueue<A>>() { // from class: fj.data.Java.67
            @Override // fj.F
            public ConcurrentLinkedQueue<A> f(Option<A> as) {
                return new ConcurrentLinkedQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, CopyOnWriteArrayList<A>> Option_CopyOnWriteArrayList() {
        return new F<Option<A>, CopyOnWriteArrayList<A>>() { // from class: fj.data.Java.68
            @Override // fj.F
            public CopyOnWriteArrayList<A> f(Option<A> as) {
                return new CopyOnWriteArrayList<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, CopyOnWriteArraySet<A>> Option_CopyOnWriteArraySet() {
        return new F<Option<A>, CopyOnWriteArraySet<A>>() { // from class: fj.data.Java.69
            @Override // fj.F
            public CopyOnWriteArraySet<A> f(Option<A> as) {
                return new CopyOnWriteArraySet<>(as.toCollection());
            }
        };
    }

    public static <A extends Delayed> F<Option<A>, DelayQueue<A>> Option_DelayQueue() {
        return (F<Option<A>, DelayQueue<A>>) new F<Option<A>, DelayQueue<A>>() { // from class: fj.data.Java.70
            @Override // fj.F
            public DelayQueue<A> f(Option<A> as) {
                return new DelayQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, LinkedBlockingQueue<A>> Option_LinkedBlockingQueue() {
        return new F<Option<A>, LinkedBlockingQueue<A>>() { // from class: fj.data.Java.71
            @Override // fj.F
            public LinkedBlockingQueue<A> f(Option<A> as) {
                return new LinkedBlockingQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, PriorityBlockingQueue<A>> Option_PriorityBlockingQueue() {
        return new F<Option<A>, PriorityBlockingQueue<A>>() { // from class: fj.data.Java.72
            @Override // fj.F
            public PriorityBlockingQueue<A> f(Option<A> as) {
                return new PriorityBlockingQueue<>(as.toCollection());
            }
        };
    }

    public static <A> F<Option<A>, SynchronousQueue<A>> Option_SynchronousQueue(final boolean fair) {
        return new F<Option<A>, SynchronousQueue<A>>() { // from class: fj.data.Java.73
            @Override // fj.F
            public SynchronousQueue<A> f(Option<A> as) {
                SynchronousQueue<A> q = new SynchronousQueue<>(fair);
                q.addAll(as.toCollection());
                return q;
            }
        };
    }

    public static <A, B> F<Either<A, B>, ArrayList<A>> Either_ArrayListA() {
        return Function.compose(Option_ArrayList(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, ArrayList<B>> Either_ArrayListB() {
        return Function.compose(Option_ArrayList(), Conversions.Either_OptionB());
    }

    public static <B> F<Either<Boolean, B>, BitSet> Either_BitSetA() {
        return Function.compose(Option_BitSet, Conversions.Either_OptionA());
    }

    public static <A> F<Either<A, Boolean>, BitSet> Either_BitSetB() {
        return Function.compose(Option_BitSet, Conversions.Either_OptionB());
    }

    public static <A extends Enum<A>, B> F<Either<A, B>, EnumSet<A>> Either_EnumSetA() {
        return Function.compose(Option_EnumSet(), Conversions.Either_OptionA());
    }

    public static <A, B extends Enum<B>> F<Either<A, B>, EnumSet<B>> Either_EnumSetB() {
        return Function.compose(Option_EnumSet(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, java.util.HashSet<A>> Either_HashSetA() {
        return Function.compose(Option_HashSet(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, java.util.HashSet<B>> Either_HashSetB() {
        return Function.compose(Option_HashSet(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, LinkedHashSet<A>> Either_LinkedHashSetA() {
        return Function.compose(Option_LinkedHashSet(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, LinkedHashSet<B>> Either_LinkedHashSetB() {
        return Function.compose(Option_LinkedHashSet(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, LinkedList<A>> Either_LinkedListA() {
        return Function.compose(Option_LinkedList(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, PriorityQueue<A>> Option_PriorityQueueA() {
        return Function.compose(Option_PriorityQueue(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, PriorityQueue<B>> Option_PriorityQueueB() {
        return Function.compose(Option_PriorityQueue(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, LinkedList<B>> Either_LinkedListB() {
        return Function.compose(Option_LinkedList(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, Stack<A>> Either_StackA() {
        return Function.compose(Option_Stack(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, Stack<B>> Either_StackB() {
        return Function.compose(Option_Stack(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, TreeSet<A>> Either_TreeSetA() {
        return Function.compose(Option_TreeSet(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, TreeSet<B>> Either_TreeSetB() {
        return Function.compose(Option_TreeSet(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, Vector<A>> Either_VectorA() {
        return Function.compose(Option_Vector(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, Vector<B>> Either_VectorB() {
        return Function.compose(Option_Vector(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, ArrayBlockingQueue<A>> Either_ArrayBlockingQueueA(boolean fair) {
        return Function.compose(Option_ArrayBlockingQueue(fair), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, ArrayBlockingQueue<B>> Either_ArrayBlockingQueueB(boolean fair) {
        return Function.compose(Option_ArrayBlockingQueue(fair), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, ConcurrentLinkedQueue<A>> Either_ConcurrentLinkedQueueA() {
        return Function.compose(Option_ConcurrentLinkedQueue(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, ConcurrentLinkedQueue<B>> Either_ConcurrentLinkedQueueB() {
        return Function.compose(Option_ConcurrentLinkedQueue(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, CopyOnWriteArrayList<A>> Either_CopyOnWriteArrayListA() {
        return Function.compose(Option_CopyOnWriteArrayList(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, CopyOnWriteArrayList<B>> Either_CopyOnWriteArrayListB() {
        return Function.compose(Option_CopyOnWriteArrayList(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, CopyOnWriteArraySet<A>> Either_CopyOnWriteArraySetA() {
        return Function.compose(Option_CopyOnWriteArraySet(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, CopyOnWriteArraySet<B>> Either_CopyOnWriteArraySetB() {
        return Function.compose(Option_CopyOnWriteArraySet(), Conversions.Either_OptionB());
    }

    public static <A extends Delayed, B> F<Either<A, B>, DelayQueue<A>> Either_DelayQueueA() {
        return Function.compose(Option_DelayQueue(), Conversions.Either_OptionA());
    }

    public static <A, B extends Delayed> F<Either<A, B>, DelayQueue<B>> Either_DelayQueueB() {
        return Function.compose(Option_DelayQueue(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, LinkedBlockingQueue<A>> Either_LinkedBlockingQueueA() {
        return Function.compose(Option_LinkedBlockingQueue(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, LinkedBlockingQueue<B>> Either_LinkedBlockingQueueB() {
        return Function.compose(Option_LinkedBlockingQueue(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, PriorityBlockingQueue<A>> Either_PriorityBlockingQueueA() {
        return Function.compose(Option_PriorityBlockingQueue(), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, PriorityBlockingQueue<B>> Either_PriorityBlockingQueueB() {
        return Function.compose(Option_PriorityBlockingQueue(), Conversions.Either_OptionB());
    }

    public static <A, B> F<Either<A, B>, SynchronousQueue<A>> Either_SynchronousQueueA(boolean fair) {
        return Function.compose(Option_SynchronousQueue(fair), Conversions.Either_OptionA());
    }

    public static <A, B> F<Either<A, B>, SynchronousQueue<B>> Either_SynchronousQueueB(boolean fair) {
        return Function.compose(Option_SynchronousQueue(fair), Conversions.Either_OptionB());
    }

    public static F<String, ArrayBlockingQueue<Character>> String_ArrayBlockingQueue(boolean fair) {
        return Function.compose(List_ArrayBlockingQueue(fair), Conversions.String_List);
    }

    public static F<String, SynchronousQueue<Character>> String_SynchronousQueue(boolean fair) {
        return Function.compose(List_SynchronousQueue(fair), Conversions.String_List);
    }

    public static F<StringBuffer, ArrayBlockingQueue<Character>> StringBuffer_ArrayBlockingQueue(boolean fair) {
        return Function.compose(List_ArrayBlockingQueue(fair), Conversions.StringBuffer_List);
    }

    public static F<StringBuffer, SynchronousQueue<Character>> StringBuffer_SynchronousQueue(boolean fair) {
        return Function.compose(List_SynchronousQueue(fair), Conversions.StringBuffer_List);
    }

    public static F<StringBuilder, ArrayBlockingQueue<Character>> StringBuilder_ArrayBlockingQueue(boolean fair) {
        return Function.compose(List_ArrayBlockingQueue(fair), Conversions.StringBuilder_List);
    }

    public static F<StringBuilder, SynchronousQueue<Character>> StringBuilder_SynchronousQueue(boolean fair) {
        return Function.compose(List_SynchronousQueue(fair), Conversions.StringBuilder_List);
    }

    public static <A> F<ArrayList<A>, List<A>> ArrayList_List() {
        return new F<ArrayList<A>, List<A>>() { // from class: fj.data.Java.74
            @Override // fj.F
            public List<A> f(ArrayList<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<java.util.List<A>, List<A>> JUList_List() {
        return new F<java.util.List<A>, List<A>>() { // from class: fj.data.Java.75
            @Override // fj.F
            public List<A> f(java.util.List<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A extends Enum<A>> F<EnumSet<A>, List<A>> EnumSet_List() {
        return (F<EnumSet<A>, List<A>>) new F<EnumSet<A>, List<A>>() { // from class: fj.data.Java.77
            @Override // fj.F
            public List<A> f(EnumSet<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> List<A> Collection_List(Collection<A> c) {
        return (List) Collection_List().f(c);
    }

    public static <A> F<Collection<A>, List<A>> Collection_List() {
        F<Collection<A>, List<A>> f;
        f = Java$$Lambda$1.instance;
        return f;
    }

    public static /* synthetic */ List lambda$Collection_List$153(Collection c) {
        return List.list(c.toArray(array(c.size(), new Object[0])));
    }

    @SafeVarargs
    private static <E> E[] array(int length, E... array) {
        return (E[]) Arrays.copyOf(array, length);
    }

    public static <A> F<java.util.HashSet<A>, List<A>> HashSet_List() {
        return new F<java.util.HashSet<A>, List<A>>() { // from class: fj.data.Java.78
            @Override // fj.F
            public List<A> f(java.util.HashSet<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<LinkedHashSet<A>, List<A>> LinkedHashSet_List() {
        return new F<LinkedHashSet<A>, List<A>>() { // from class: fj.data.Java.79
            @Override // fj.F
            public List<A> f(LinkedHashSet<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<LinkedList<A>, List<A>> LinkedList_List() {
        return new F<LinkedList<A>, List<A>>() { // from class: fj.data.Java.80
            @Override // fj.F
            public List<A> f(LinkedList<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<PriorityQueue<A>, List<A>> PriorityQueue_List() {
        return new F<PriorityQueue<A>, List<A>>() { // from class: fj.data.Java.81
            @Override // fj.F
            public List<A> f(PriorityQueue<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<Stack<A>, List<A>> Stack_List() {
        return new F<Stack<A>, List<A>>() { // from class: fj.data.Java.82
            @Override // fj.F
            public List<A> f(Stack<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<TreeSet<A>, List<A>> TreeSet_List() {
        return new F<TreeSet<A>, List<A>>() { // from class: fj.data.Java.83
            @Override // fj.F
            public List<A> f(TreeSet<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<Vector<A>, List<A>> Vector_List() {
        return new F<Vector<A>, List<A>>() { // from class: fj.data.Java.84
            @Override // fj.F
            public List<A> f(Vector<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<ArrayBlockingQueue<A>, List<A>> ArrayBlockingQueue_List() {
        return new F<ArrayBlockingQueue<A>, List<A>>() { // from class: fj.data.Java.85
            @Override // fj.F
            public List<A> f(ArrayBlockingQueue<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<ConcurrentLinkedQueue<A>, List<A>> ConcurrentLinkedQueue_List() {
        return new F<ConcurrentLinkedQueue<A>, List<A>>() { // from class: fj.data.Java.86
            @Override // fj.F
            public List<A> f(ConcurrentLinkedQueue<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<CopyOnWriteArrayList<A>, List<A>> CopyOnWriteArrayList_List() {
        return new F<CopyOnWriteArrayList<A>, List<A>>() { // from class: fj.data.Java.87
            @Override // fj.F
            public List<A> f(CopyOnWriteArrayList<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<CopyOnWriteArraySet<A>, List<A>> CopyOnWriteArraySet_List() {
        return new F<CopyOnWriteArraySet<A>, List<A>>() { // from class: fj.data.Java.88
            @Override // fj.F
            public List<A> f(CopyOnWriteArraySet<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A extends Delayed> F<DelayQueue<A>, List<A>> DelayQueue_List() {
        return (F<DelayQueue<A>, List<A>>) new F<DelayQueue<A>, List<A>>() { // from class: fj.data.Java.89
            @Override // fj.F
            public List<A> f(DelayQueue<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<LinkedBlockingQueue<A>, List<A>> LinkedBlockingQueue_List() {
        return new F<LinkedBlockingQueue<A>, List<A>>() { // from class: fj.data.Java.90
            @Override // fj.F
            public List<A> f(LinkedBlockingQueue<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<PriorityBlockingQueue<A>, List<A>> PriorityBlockingQueue_List() {
        return new F<PriorityBlockingQueue<A>, List<A>>() { // from class: fj.data.Java.91
            @Override // fj.F
            public List<A> f(PriorityBlockingQueue<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<SynchronousQueue<A>, List<A>> SynchronousQueue_List() {
        return new F<SynchronousQueue<A>, List<A>>() { // from class: fj.data.Java.92
            @Override // fj.F
            public List<A> f(SynchronousQueue<A> as) {
                return Java.Collection_List(as);
            }
        };
    }

    public static <A> F<P1<A>, Callable<A>> P1_Callable() {
        return new F<P1<A>, Callable<A>>() { // from class: fj.data.Java.93
            @Override // fj.F
            public Callable<A> f(final P1<A> a) {
                return new Callable<A>() { // from class: fj.data.Java.93.1
                    {
                        AnonymousClass93.this = this;
                    }

                    /* JADX WARN: Type inference failed for: r0v2, types: [A, java.lang.Object] */
                    @Override // java.util.concurrent.Callable
                    public A call() {
                        return a._1();
                    }
                };
            }
        };
    }

    public static <A> F<Future<A>, P1<Either<Exception, A>>> Future_P1() {
        return new F<Future<A>, P1<Either<Exception, A>>>() { // from class: fj.data.Java.94
            @Override // fj.F
            public P1<Either<Exception, A>> f(final Future<A> a) {
                return new P1<Either<Exception, A>>() { // from class: fj.data.Java.94.1
                    {
                        AnonymousClass94.this = this;
                    }

                    @Override // fj.P1
                    public Either<Exception, A> _1() {
                        Either left;
                        try {
                            left = Either.right(a.get());
                        } catch (Exception e) {
                            left = Either.left(e);
                        }
                        return left;
                    }
                };
            }
        };
    }
}
