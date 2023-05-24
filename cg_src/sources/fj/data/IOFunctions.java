package fj.data;

import fj.Bottom;
import fj.F;
import fj.F1Functions;
import fj.F2;
import fj.Function;
import fj.P;
import fj.P1;
import fj.P2;
import fj.Try;
import fj.Unit;
import fj.data.Iteratee;
import fj.function.Try0;
import fj.function.Try1;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
/* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions.class */
public class IOFunctions {
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    public static final F<java.io.Reader, IO<Unit>> closeReader = new F<java.io.Reader, IO<Unit>>() { // from class: fj.data.IOFunctions.1
        @Override // fj.F
        public IO<Unit> f(java.io.Reader r) {
            return IOFunctions.closeReader(r);
        }
    };
    public static final BufferedReader stdinBufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static <A> Try0<A, IOException> toTry(IO<A> io) {
        return IOFunctions$$Lambda$1.lambdaFactory$(io);
    }

    public static <A> P1<Validation<IOException, A>> p(IO<A> io) {
        return Try.f(toTry(io));
    }

    public static <A> IO<A> io(P1<A> p) {
        return IOFunctions$$Lambda$2.lambdaFactory$(p);
    }

    public static <A> IO<A> io(Try0<A, ? extends IOException> t) {
        return IOFunctions$$Lambda$3.lambdaFactory$(t);
    }

    public static IO<Unit> closeReader(final java.io.Reader r) {
        return new IO<Unit>() { // from class: fj.data.IOFunctions.2
            @Override // fj.data.IO
            public Unit run() throws IOException {
                r.close();
                return Unit.unit();
            }
        };
    }

    public static <A> IO<Iteratee.IterV<String, A>> enumFileLines(File f, Option<Charset> encoding, Iteratee.IterV<String, A> i) {
        return bracket(bufferedReader(f, encoding), Function.vary(closeReader), Function.partialApply2(lineReader(), i));
    }

    public static <A> IO<Iteratee.IterV<char[], A>> enumFileCharChunks(File f, Option<Charset> encoding, Iteratee.IterV<char[], A> i) {
        return bracket(fileReader(f, encoding), Function.vary(closeReader), Function.partialApply2(charChunkReader(), i));
    }

    public static <A> IO<Iteratee.IterV<Character, A>> enumFileChars(File f, Option<Charset> encoding, Iteratee.IterV<Character, A> i) {
        return bracket(fileReader(f, encoding), Function.vary(closeReader), Function.partialApply2(charChunkReader2(), i));
    }

    public static IO<BufferedReader> bufferedReader(File f, Option<Charset> encoding) {
        return map(fileReader(f, encoding), new F<java.io.Reader, BufferedReader>() { // from class: fj.data.IOFunctions.3
            @Override // fj.F
            public BufferedReader f(java.io.Reader a) {
                return new BufferedReader(a);
            }
        });
    }

    public static IO<java.io.Reader> fileReader(final File f, final Option<Charset> encoding) {
        return new IO<java.io.Reader>() { // from class: fj.data.IOFunctions.4
            @Override // fj.data.IO
            public java.io.Reader run() throws IOException {
                FileInputStream fis = new FileInputStream(f);
                return encoding.isNone() ? new InputStreamReader(fis) : new InputStreamReader(fis, (Charset) encoding.some());
            }
        };
    }

    public static final <A, B, C> IO<C> bracket(final IO<A> init, final F<A, IO<B>> fin, final F<A, IO<C>> body) {
        return new IO<C>() { // from class: fj.data.IOFunctions.5
            /* JADX WARN: Type inference failed for: r0v12, types: [C, java.lang.Object] */
            @Override // fj.data.IO
            public C run() throws IOException {
                Object run = init.run();
                try {
                    try {
                        ?? run2 = ((IO) body.f(run)).run();
                        fin.f(run);
                        return run2;
                    } catch (IOException e) {
                        throw e;
                    }
                } catch (Throwable th) {
                    fin.f(run);
                    throw th;
                }
            }
        };
    }

    public static final <A> IO<A> unit(final A a) {
        return new IO<A>() { // from class: fj.data.IOFunctions.6
            /* JADX WARN: Type inference failed for: r0v1, types: [A, java.lang.Object] */
            @Override // fj.data.IO
            public A run() throws IOException {
                return a;
            }
        };
    }

    public static final <A> IO<A> lazy(P1<A> p) {
        return IOFunctions$$Lambda$4.lambdaFactory$(p);
    }

    public static final <A> IO<A> lazy(F<Unit, A> f) {
        return IOFunctions$$Lambda$5.lambdaFactory$(f);
    }

    public static /* synthetic */ Object lambda$lazy$109(F f) throws IOException {
        return f.f(Unit.unit());
    }

    public static final <A> SafeIO<A> lazySafe(F<Unit, A> f) {
        return IOFunctions$$Lambda$6.lambdaFactory$(f);
    }

    public static /* synthetic */ Object lambda$lazySafe$110(F f) {
        return f.f(Unit.unit());
    }

    public static final <A> SafeIO<A> lazySafe(P1<A> f) {
        return IOFunctions$$Lambda$7.lambdaFactory$(f);
    }

    public static <A> F<BufferedReader, F<Iteratee.IterV<String, A>, IO<Iteratee.IterV<String, A>>>> lineReader() {
        F<Iteratee.IterV<String, A>, Boolean> isDone = new F<Iteratee.IterV<String, A>, Boolean>() { // from class: fj.data.IOFunctions.7
            final F<P2<A, Iteratee.Input<String>>, P1<Boolean>> done = Function.constant(P.p(true));
            final F<F<Iteratee.Input<String>, Iteratee.IterV<String, A>>, P1<Boolean>> cont = Function.constant(P.p(false));

            @Override // fj.F
            public Boolean f(Iteratee.IterV<String, A> i) {
                return (Boolean) ((P1) i.fold(this.done, this.cont))._1();
            }
        };
        return new AnonymousClass8(isDone);
    }

    /* renamed from: fj.data.IOFunctions$8 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$8.class */
    public static class AnonymousClass8 implements F<BufferedReader, F<Iteratee.IterV<String, A>, IO<Iteratee.IterV<String, A>>>> {
        final /* synthetic */ F val$isDone;

        AnonymousClass8(F f) {
            this.val$isDone = f;
        }

        /* renamed from: fj.data.IOFunctions$8$1 */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$8$1.class */
        public class AnonymousClass1 implements F<Iteratee.IterV<String, A>, IO<Iteratee.IterV<String, A>>> {
            final F<P2<A, Iteratee.Input<String>>, P1<Iteratee.IterV<String, A>>> done = Bottom.errorF("iteratee is done");
            final /* synthetic */ BufferedReader val$r;

            AnonymousClass1(BufferedReader bufferedReader) {
                AnonymousClass8.this = this$0;
                this.val$r = bufferedReader;
            }

            @Override // fj.F
            public IO<Iteratee.IterV<String, A>> f(final Iteratee.IterV<String, A> it) {
                return new IO<Iteratee.IterV<String, A>>() { // from class: fj.data.IOFunctions.8.1.1
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // fj.data.IO
                    public Iteratee.IterV<String, A> run() throws IOException {
                        Iteratee.IterV iterV = it;
                        while (true) {
                            Iteratee.IterV iterV2 = iterV;
                            if (!((Boolean) AnonymousClass8.this.val$isDone.f(iterV2)).booleanValue()) {
                                String s = AnonymousClass1.this.val$r.readLine();
                                if (s == null) {
                                    return iterV2;
                                }
                                Iteratee.Input<String> input = Iteratee.Input.el(s);
                                iterV = (Iteratee.IterV) ((P1) iterV2.fold(AnonymousClass1.this.done, F1Functions.lazy(Function.apply(input))))._1();
                            } else {
                                return iterV2;
                            }
                        }
                    }
                };
            }
        }

        @Override // fj.F
        public F<Iteratee.IterV<String, A>, IO<Iteratee.IterV<String, A>>> f(BufferedReader r) {
            return new AnonymousClass1(r);
        }
    }

    public static <A> F<java.io.Reader, F<Iteratee.IterV<char[], A>, IO<Iteratee.IterV<char[], A>>>> charChunkReader() {
        F<Iteratee.IterV<char[], A>, Boolean> isDone = new F<Iteratee.IterV<char[], A>, Boolean>() { // from class: fj.data.IOFunctions.9
            final F<P2<A, Iteratee.Input<char[]>>, P1<Boolean>> done = Function.constant(P.p(true));
            final F<F<Iteratee.Input<char[]>, Iteratee.IterV<char[], A>>, P1<Boolean>> cont = Function.constant(P.p(false));

            @Override // fj.F
            public Boolean f(Iteratee.IterV<char[], A> i) {
                return (Boolean) ((P1) i.fold(this.done, this.cont))._1();
            }
        };
        return new AnonymousClass10(isDone);
    }

    /* renamed from: fj.data.IOFunctions$10 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$10.class */
    public static class AnonymousClass10 implements F<java.io.Reader, F<Iteratee.IterV<char[], A>, IO<Iteratee.IterV<char[], A>>>> {
        final /* synthetic */ F val$isDone;

        AnonymousClass10(F f) {
            this.val$isDone = f;
        }

        /* renamed from: fj.data.IOFunctions$10$1 */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$10$1.class */
        public class AnonymousClass1 implements F<Iteratee.IterV<char[], A>, IO<Iteratee.IterV<char[], A>>> {
            final F<P2<A, Iteratee.Input<char[]>>, P1<Iteratee.IterV<char[], A>>> done = Bottom.errorF("iteratee is done");
            final /* synthetic */ java.io.Reader val$r;

            AnonymousClass1(java.io.Reader reader) {
                AnonymousClass10.this = this$0;
                this.val$r = reader;
            }

            @Override // fj.F
            public IO<Iteratee.IterV<char[], A>> f(final Iteratee.IterV<char[], A> it) {
                return new IO<Iteratee.IterV<char[], A>>() { // from class: fj.data.IOFunctions.10.1.1
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // fj.data.IO
                    public Iteratee.IterV<char[], A> run() throws IOException {
                        Iteratee.IterV iterV = it;
                        while (true) {
                            Iteratee.IterV iterV2 = iterV;
                            if (!((Boolean) AnonymousClass10.this.val$isDone.f(iterV2)).booleanValue()) {
                                char[] buffer = new char[4096];
                                int numRead = AnonymousClass1.this.val$r.read(buffer);
                                if (numRead == -1) {
                                    return iterV2;
                                }
                                if (numRead < buffer.length) {
                                    buffer = Arrays.copyOfRange(buffer, 0, numRead);
                                }
                                Iteratee.Input<char[]> input = Iteratee.Input.el(buffer);
                                iterV = (Iteratee.IterV) ((P1) iterV2.fold(AnonymousClass1.this.done, F1Functions.lazy(Function.apply(input))))._1();
                            } else {
                                return iterV2;
                            }
                        }
                    }
                };
            }
        }

        @Override // fj.F
        public F<Iteratee.IterV<char[], A>, IO<Iteratee.IterV<char[], A>>> f(java.io.Reader r) {
            return new AnonymousClass1(r);
        }
    }

    public static <A> F<java.io.Reader, F<Iteratee.IterV<Character, A>, IO<Iteratee.IterV<Character, A>>>> charChunkReader2() {
        F<Iteratee.IterV<Character, A>, Boolean> isDone = new F<Iteratee.IterV<Character, A>, Boolean>() { // from class: fj.data.IOFunctions.11
            final F<P2<A, Iteratee.Input<Character>>, P1<Boolean>> done = Function.constant(P.p(true));
            final F<F<Iteratee.Input<Character>, Iteratee.IterV<Character, A>>, P1<Boolean>> cont = Function.constant(P.p(false));

            @Override // fj.F
            public Boolean f(Iteratee.IterV<Character, A> i) {
                return (Boolean) ((P1) i.fold(this.done, this.cont))._1();
            }
        };
        return new AnonymousClass12(isDone);
    }

    /* renamed from: fj.data.IOFunctions$12 */
    /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$12.class */
    public static class AnonymousClass12 implements F<java.io.Reader, F<Iteratee.IterV<Character, A>, IO<Iteratee.IterV<Character, A>>>> {
        final /* synthetic */ F val$isDone;

        AnonymousClass12(F f) {
            this.val$isDone = f;
        }

        /* renamed from: fj.data.IOFunctions$12$1 */
        /* loaded from: gencallgraphv3.jar:functionaljava-4.2.jar:fj/data/IOFunctions$12$1.class */
        public class AnonymousClass1 implements F<Iteratee.IterV<Character, A>, IO<Iteratee.IterV<Character, A>>> {
            final F<P2<A, Iteratee.Input<Character>>, Iteratee.IterV<Character, A>> done = Bottom.errorF("iteratee is done");
            final /* synthetic */ java.io.Reader val$r;

            AnonymousClass1(java.io.Reader reader) {
                AnonymousClass12.this = this$0;
                this.val$r = reader;
            }

            @Override // fj.F
            public IO<Iteratee.IterV<Character, A>> f(final Iteratee.IterV<Character, A> it) {
                return new IO<Iteratee.IterV<Character, A>>() { // from class: fj.data.IOFunctions.12.1.1
                    {
                        AnonymousClass1.this = this;
                    }

                    @Override // fj.data.IO
                    public Iteratee.IterV<Character, A> run() throws IOException {
                        Iteratee.IterV iterV = it;
                        while (!((Boolean) AnonymousClass12.this.val$isDone.f(iterV)).booleanValue()) {
                            char[] buffer = new char[4096];
                            int numRead = AnonymousClass1.this.val$r.read(buffer);
                            if (numRead == -1) {
                                return iterV;
                            }
                            if (numRead < buffer.length) {
                                buffer = Arrays.copyOfRange(buffer, 0, numRead);
                            }
                            for (char c : buffer) {
                                Iteratee.Input<Character> input = Iteratee.Input.el(Character.valueOf(c));
                                iterV = (Iteratee.IterV) iterV.fold(AnonymousClass1.this.done, Function.apply(input));
                            }
                        }
                        return iterV;
                    }
                };
            }
        }

        @Override // fj.F
        public F<Iteratee.IterV<Character, A>, IO<Iteratee.IterV<Character, A>>> f(java.io.Reader r) {
            return new AnonymousClass1(r);
        }
    }

    public static final <A, B> IO<B> map(final IO<A> io, final F<A, B> f) {
        return new IO<B>() { // from class: fj.data.IOFunctions.13
            /* JADX WARN: Type inference failed for: r0v2, types: [B, java.lang.Object] */
            @Override // fj.data.IO
            public B run() throws IOException {
                return f.f(io.run());
            }
        };
    }

    public static final <A, B> IO<B> bind(final IO<A> io, final F<A, IO<B>> f) {
        return new IO<B>() { // from class: fj.data.IOFunctions.14
            /* JADX WARN: Type inference failed for: r0v4, types: [B, java.lang.Object] */
            @Override // fj.data.IO
            public B run() throws IOException {
                return ((IO) f.f(io.run())).run();
            }
        };
    }

    public static <A> IO<List<A>> sequence(List<IO<A>> list) {
        F2<IO<A>, B, B> f2;
        f2 = IOFunctions$$Lambda$8.instance;
        return (IO) list.foldRight((F2<IO<A>, F2<IO<A>, B, B>, F2<IO<A>, B, B>>) f2, (F2<IO<A>, B, B>) unit(List.nil()));
    }

    public static /* synthetic */ IO lambda$sequence$114(IO io, IO ioList) {
        return bind(ioList, IOFunctions$$Lambda$30.lambdaFactory$(io));
    }

    public static /* synthetic */ IO lambda$null$113(IO io, List xs) {
        return map(io, IOFunctions$$Lambda$31.lambdaFactory$(xs));
    }

    public static <A> IO<Stream<A>> sequence(Stream<IO<A>> stream) {
        F2<B, IO<A>, B> f2;
        f2 = IOFunctions$$Lambda$9.instance;
        return (IO) stream.foldLeft((F2<F2<B, IO<A>, B>, IO<A>, F2<B, IO<A>, B>>) f2, (F2<B, IO<A>, B>) unit(Stream.nil()));
    }

    public static /* synthetic */ IO lambda$sequence$118(IO ioList, IO io) {
        return bind(ioList, IOFunctions$$Lambda$27.lambdaFactory$(io));
    }

    public static /* synthetic */ IO lambda$null$117(IO io, Stream xs) {
        return map(io, IOFunctions$$Lambda$28.lambdaFactory$(xs));
    }

    public static /* synthetic */ Stream lambda$null$116(Stream stream, Object x) {
        return Stream.cons(x, P.lazy(IOFunctions$$Lambda$29.lambdaFactory$(stream)));
    }

    public static /* synthetic */ Stream lambda$null$115(Stream stream, Unit u) {
        return stream;
    }

    public static <A, B> IO<List<B>> traverse(List<A> list, F<A, IO<B>> f) {
        return (IO) list.foldRight((F2<A, F2<A, B, B>, F2<A, B, B>>) IOFunctions$$Lambda$10.lambdaFactory$(f), (F2<A, B, B>) unit(List.nil()));
    }

    public static /* synthetic */ IO lambda$traverse$121(F f, Object a, IO acc) {
        return bind(acc, IOFunctions$$Lambda$25.lambdaFactory$(f, a));
    }

    public static /* synthetic */ IO lambda$null$120(F f, Object obj, List bs) {
        return map((IO) f.f(obj), IOFunctions$$Lambda$26.lambdaFactory$(bs));
    }

    public static /* synthetic */ List lambda$null$119(List list, Object b) {
        return list.append(List.list(b));
    }

    public static <A> IO<A> join(IO<IO<A>> io1) {
        F f;
        f = IOFunctions$$Lambda$11.instance;
        return bind(io1, f);
    }

    public static /* synthetic */ IO lambda$join$122(IO io2) {
        return io2;
    }

    public static <A> SafeIO<Validation<IOException, A>> toSafeIO(IO<A> io) {
        return IOFunctions$$Lambda$12.lambdaFactory$(io);
    }

    public static /* synthetic */ Validation lambda$toSafeIO$124(IO io) {
        return (Validation) Try.f(IOFunctions$$Lambda$24.lambdaFactory$(io))._1();
    }

    public static <A, B> IO<B> append(IO<A> io1, IO<B> io2) {
        return IOFunctions$$Lambda$13.lambdaFactory$(io1, io2);
    }

    public static /* synthetic */ Object lambda$append$125(IO io, IO io2) throws IOException {
        io.run();
        return io2.run();
    }

    public static <A, B> IO<A> left(IO<A> io1, IO<B> io2) {
        return IOFunctions$$Lambda$14.lambdaFactory$(io1, io2);
    }

    public static /* synthetic */ Object lambda$left$126(IO io, IO io2) throws IOException {
        Object run = io.run();
        io2.run();
        return run;
    }

    public static <A, B> IO<B> flatMap(IO<A> io, F<A, IO<B>> f) {
        return bind(io, f);
    }

    static <A> IO<Stream<A>> sequenceWhile(final Stream<IO<A>> stream, final F<A, Boolean> f) {
        return new IO<Stream<A>>() { // from class: fj.data.IOFunctions.15
            @Override // fj.data.IO
            public Stream<A> run() throws IOException {
                boolean loop = true;
                Stream stream2 = stream;
                Stream nil = Stream.nil();
                while (loop) {
                    if (stream2.isEmpty()) {
                        loop = false;
                    } else {
                        Object run = ((IO) stream2.head()).run();
                        if (!((Boolean) f.f(run)).booleanValue()) {
                            loop = false;
                        } else {
                            stream2 = (Stream) stream2.tail()._1();
                            nil = nil.cons(run);
                        }
                    }
                }
                return nil.reverse();
            }
        };
    }

    public static <A, B> IO<B> apply(IO<A> io, IO<F<A, B>> iof) {
        return bind(iof, IOFunctions$$Lambda$15.lambdaFactory$(io));
    }

    public static /* synthetic */ IO lambda$apply$128(IO io, F f) {
        return map(io, IOFunctions$$Lambda$23.lambdaFactory$(f));
    }

    public static <A, B, C> IO<C> liftM2(IO<A> ioa, IO<B> iob, F2<A, B, C> f) {
        return bind(ioa, IOFunctions$$Lambda$16.lambdaFactory$(iob, f));
    }

    public static /* synthetic */ IO lambda$liftM2$130(IO io, F2 f2, Object a) {
        return map(io, IOFunctions$$Lambda$22.lambdaFactory$(f2, a));
    }

    public static <A> IO<List<A>> replicateM(IO<A> ioa, int n) {
        return sequence(List.replicate(n, ioa));
    }

    public static <A> IO<State<BufferedReader, Validation<IOException, String>>> readerState() {
        IO<State<BufferedReader, Validation<IOException, String>>> io;
        io = IOFunctions$$Lambda$17.instance;
        return io;
    }

    public static /* synthetic */ State lambda$readerState$133() throws IOException {
        F f;
        f = IOFunctions$$Lambda$20.instance;
        return State.unit(f);
    }

    public static /* synthetic */ P2 lambda$null$132(BufferedReader r) {
        Try1 try1;
        try1 = IOFunctions$$Lambda$21.instance;
        return P.p(r, Try.f(try1).f(r));
    }

    public static IO<String> stdinReadLine() {
        IO<String> io;
        io = IOFunctions$$Lambda$18.instance;
        return io;
    }

    public static /* synthetic */ String lambda$stdinReadLine$134() throws IOException {
        return stdinBufferedReader.readLine();
    }

    public static IO<Unit> stdoutPrintln(String s) {
        return IOFunctions$$Lambda$19.lambdaFactory$(s);
    }

    public static /* synthetic */ Unit lambda$stdoutPrintln$135(String str) throws IOException {
        System.out.println(str);
        return Unit.unit();
    }
}
