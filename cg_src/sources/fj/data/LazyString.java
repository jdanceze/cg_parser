package fj.data;

import fj.Equal;
import fj.F;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.function.Booleans;
import fj.function.Characters;
import java.util.regex.Pattern;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/LazyString.class */
public final class LazyString implements CharSequence {
    private final Stream<Character> s;
    public static final LazyString empty = str("");
    public static final F<LazyString, Stream<Character>> toStream = new F<LazyString, Stream<Character>>() { // from class: fj.data.LazyString.4
        @Override // fj.F
        public Stream<Character> f(LazyString string) {
            return string.toStream();
        }
    };
    public static final F<LazyString, String> toString = new F<LazyString, String>() { // from class: fj.data.LazyString.5
        @Override // fj.F
        public String f(LazyString string) {
            return string.toString();
        }
    };
    public static final F<Stream<Character>, LazyString> fromStream = new F<Stream<Character>, LazyString>() { // from class: fj.data.LazyString.6
        @Override // fj.F
        public LazyString f(Stream<Character> s) {
            return LazyString.fromStream(s);
        }
    };
    private static final Equal<Stream<Character>> eqS = Equal.streamEqual(Equal.charEqual);

    private LazyString(Stream<Character> s) {
        this.s = s;
    }

    public static LazyString str(String s) {
        return new LazyString(Stream.unfold(new F<P2<String, Integer>, Option<P2<Character, P2<String, Integer>>>>() { // from class: fj.data.LazyString.1
            @Override // fj.F
            public Option<P2<Character, P2<String, Integer>>> f(P2<String, Integer> o) {
                String s2 = o._1();
                int n = o._2().intValue();
                Option<P2<Character, P2<String, Integer>>> none = Option.none();
                return s2.length() <= n ? none : Option.some(P.p(Character.valueOf(s2.charAt(n)), P.p(s2, Integer.valueOf(n + 1))));
            }
        }, P.p(s, 0)));
    }

    public static LazyString fromStream(Stream<Character> s) {
        return new LazyString(s);
    }

    public Stream<Character> toStream() {
        return this.s;
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.s.length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return this.s.index(index).charValue();
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return fromStream(this.s.drop(start).take(end - start));
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return new StringBuilder(this).toString();
    }

    public LazyString append(LazyString cs) {
        return fromStream(this.s.append(cs.s));
    }

    public LazyString append(String s) {
        return append(str(s));
    }

    public boolean contains(LazyString cs) {
        return Booleans.or((Stream<Boolean>) this.s.tails().map(Function.compose(startsWith().f(cs), fromStream)));
    }

    public boolean endsWith(LazyString cs) {
        return reverse().startsWith(cs.reverse());
    }

    public boolean startsWith(LazyString cs) {
        return cs.isEmpty() || (!isEmpty() && Equal.charEqual.eq(Character.valueOf(head()), Character.valueOf(cs.head())) && tail().startsWith(cs.tail()));
    }

    public static F<LazyString, F<LazyString, Boolean>> startsWith() {
        return Function.curry(new F2<LazyString, LazyString, Boolean>() { // from class: fj.data.LazyString.2
            @Override // fj.F2
            public Boolean f(LazyString needle, LazyString haystack) {
                return Boolean.valueOf(haystack.startsWith(needle));
            }
        });
    }

    public char head() {
        return this.s.head().charValue();
    }

    public LazyString tail() {
        return fromStream(this.s.tail()._1());
    }

    public boolean isEmpty() {
        return this.s.isEmpty();
    }

    public LazyString reverse() {
        return fromStream(this.s.reverse());
    }

    public Option<Integer> indexOf(char c) {
        return this.s.indexOf(Equal.charEqual.eq(Character.valueOf(c)));
    }

    public Option<Integer> indexOf(LazyString cs) {
        return this.s.substreams().indexOf(eqS.eq(cs.s));
    }

    public boolean matches(String regex) {
        return Pattern.matches(regex, this);
    }

    public Stream<LazyString> split(final F<Character, Boolean> p) {
        Stream<Character> findIt = this.s.dropWhile(p);
        final P2<Stream<Character>, Stream<Character>> ws = findIt.split(p);
        return findIt.isEmpty() ? Stream.nil() : Stream.cons(fromStream(ws._1()), new P1<Stream<LazyString>>() { // from class: fj.data.LazyString.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // fj.P1
            public Stream<LazyString> _1() {
                return LazyString.fromStream((Stream) ws._2()).split(p);
            }
        });
    }

    public Stream<LazyString> split(char c) {
        return split(Equal.charEqual.eq(Character.valueOf(c)));
    }

    public Stream<LazyString> words() {
        return split(Characters.isSpaceChar);
    }

    public Stream<LazyString> lines() {
        return split('\n');
    }

    public static LazyString unlines(Stream<LazyString> str) {
        return fromStream(Stream.join(str.intersperse(str("\n")).map(toStream)));
    }

    public static LazyString unwords(Stream<LazyString> str) {
        return fromStream(Stream.join(str.intersperse(str(Instruction.argsep)).map(toStream)));
    }
}
