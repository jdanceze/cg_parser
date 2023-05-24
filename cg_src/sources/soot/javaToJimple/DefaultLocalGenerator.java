package soot.javaToJimple;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.RefLikeType;
import soot.ShortType;
import soot.Type;
import soot.UnknownType;
import soot.VoidType;
import soot.jimple.Jimple;
import soot.jimple.toolkits.typing.fast.Integer127Type;
import soot.jimple.toolkits.typing.fast.Integer1Type;
import soot.jimple.toolkits.typing.fast.Integer32767Type;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/DefaultLocalGenerator.class */
public class DefaultLocalGenerator extends LocalGenerator {
    protected final Chain<Local> locals;
    static final /* synthetic */ boolean $assertionsDisabled;
    private int tempInt = -1;
    private int tempVoid = -1;
    private int tempBoolean = -1;
    private int tempLong = -1;
    private int tempDouble = -1;
    private int tempFloat = -1;
    private int tempRefLikeType = -1;
    private int tempByte = -1;
    private int tempShort = -1;
    private int tempChar = -1;
    private int tempUnknownType = -1;
    protected Set<String> names = null;
    protected long expectedModCount = -1;

    static {
        $assertionsDisabled = !DefaultLocalGenerator.class.desiredAssertionStatus();
    }

    public DefaultLocalGenerator(Body b) {
        this.locals = b.getLocals();
    }

    @Override // soot.LocalGenerator
    public Local generateLocal(Type type) {
        Supplier<String> nameGen;
        String name;
        if ((type instanceof IntType) || (type instanceof Integer1Type) || (type instanceof Integer127Type) || (type instanceof Integer32767Type)) {
            nameGen = this::nextIntName;
        } else if (type instanceof ByteType) {
            nameGen = this::nextByteName;
        } else if (type instanceof ShortType) {
            nameGen = this::nextShortName;
        } else if (type instanceof BooleanType) {
            nameGen = this::nextBooleanName;
        } else if (type instanceof VoidType) {
            nameGen = this::nextVoidName;
        } else if (type instanceof CharType) {
            nameGen = this::nextCharName;
        } else if (type instanceof DoubleType) {
            nameGen = this::nextDoubleName;
        } else if (type instanceof FloatType) {
            nameGen = this::nextFloatName;
        } else if (type instanceof LongType) {
            nameGen = this::nextLongName;
        } else if (type instanceof RefLikeType) {
            nameGen = this::nextRefLikeTypeName;
        } else if (type instanceof UnknownType) {
            nameGen = this::nextUnknownTypeName;
        } else {
            throw new RuntimeException(String.format("Unhandled Type %s of Local variable to Generate - Not Implemented", type.getClass().getName()));
        }
        Set<String> localNames = this.names;
        Chain<Local> locs = this.locals;
        long modCount = locs.getModificationCount();
        if (this.expectedModCount != modCount) {
            this.expectedModCount = modCount;
            Set<String> hashSet = new HashSet<>(locs.size());
            localNames = hashSet;
            this.names = hashSet;
            for (Local l : locs) {
                localNames.add(l.getName());
            }
        }
        if ($assertionsDisabled || localNames != null) {
            do {
                name = nameGen.get();
            } while (localNames.contains(name));
            return createLocal(name, type);
        }
        throw new AssertionError();
    }

    private String nextIntName() {
        StringBuilder sb = new StringBuilder("$i");
        int i = this.tempInt + 1;
        this.tempInt = i;
        return sb.append(i).toString();
    }

    private String nextCharName() {
        StringBuilder sb = new StringBuilder("$c");
        int i = this.tempChar + 1;
        this.tempChar = i;
        return sb.append(i).toString();
    }

    private String nextVoidName() {
        StringBuilder sb = new StringBuilder("$v");
        int i = this.tempVoid + 1;
        this.tempVoid = i;
        return sb.append(i).toString();
    }

    private String nextByteName() {
        StringBuilder sb = new StringBuilder("$b");
        int i = this.tempByte + 1;
        this.tempByte = i;
        return sb.append(i).toString();
    }

    private String nextShortName() {
        StringBuilder sb = new StringBuilder("$s");
        int i = this.tempShort + 1;
        this.tempShort = i;
        return sb.append(i).toString();
    }

    private String nextBooleanName() {
        StringBuilder sb = new StringBuilder("$z");
        int i = this.tempBoolean + 1;
        this.tempBoolean = i;
        return sb.append(i).toString();
    }

    private String nextDoubleName() {
        StringBuilder sb = new StringBuilder("$d");
        int i = this.tempDouble + 1;
        this.tempDouble = i;
        return sb.append(i).toString();
    }

    private String nextFloatName() {
        StringBuilder sb = new StringBuilder("$f");
        int i = this.tempFloat + 1;
        this.tempFloat = i;
        return sb.append(i).toString();
    }

    private String nextLongName() {
        StringBuilder sb = new StringBuilder("$l");
        int i = this.tempLong + 1;
        this.tempLong = i;
        return sb.append(i).toString();
    }

    private String nextRefLikeTypeName() {
        StringBuilder sb = new StringBuilder("$r");
        int i = this.tempRefLikeType + 1;
        this.tempRefLikeType = i;
        return sb.append(i).toString();
    }

    private String nextUnknownTypeName() {
        StringBuilder sb = new StringBuilder("$u");
        int i = this.tempUnknownType + 1;
        this.tempUnknownType = i;
        return sb.append(i).toString();
    }

    protected Local createLocal(String name, Type sootType) {
        if ($assertionsDisabled || this.expectedModCount == this.locals.getModificationCount()) {
            if ($assertionsDisabled || !this.names.contains(name)) {
                Local sootLocal = Jimple.v().newLocal(name, sootType);
                this.locals.add(sootLocal);
                this.expectedModCount++;
                this.names.add(name);
                if ($assertionsDisabled || this.expectedModCount == this.locals.getModificationCount()) {
                    if ($assertionsDisabled || this.names.contains(name)) {
                        return sootLocal;
                    }
                    throw new AssertionError();
                }
                throw new AssertionError();
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }
}
