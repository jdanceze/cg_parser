package polyglot.util.typedump;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.TypeEncoder;
import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/typedump/TypeDumper.class */
class TypeDumper {
    static Set dontExpand;
    Type theType;
    String rawName;
    String compilerVersion;
    Date timestamp;
    static Class class$java$lang$Void;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Short;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$java$lang$Class;
    static Class class$java$lang$String;
    static Class class$java$lang$Object;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: polyglot.util.typedump.TypeDumper.dumpObject(polyglot.util.CodeWriter, java.lang.Object, java.util.Map):void, file: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/typedump/TypeDumper.class
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:158)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        Caused by: jadx.plugins.input.java.utils.JavaClassParseException: Unknown opcode: 0xa8
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:71)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	... 5 more
        */
    protected void dumpObject(polyglot.util.CodeWriter r1, java.lang.Object r2, java.util.Map r3) {
        /*
        // Can't load method instructions: Load method exception: JavaClassParseException: Unknown opcode: 0xa8 in method: polyglot.util.typedump.TypeDumper.dumpObject(polyglot.util.CodeWriter, java.lang.Object, java.util.Map):void, file: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/typedump/TypeDumper.class
        */
        throw new UnsupportedOperationException("Method not decompiled: polyglot.util.typedump.TypeDumper.dumpObject(polyglot.util.CodeWriter, java.lang.Object, java.util.Map):void");
    }

    static {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class cls6;
        Class cls7;
        Class cls8;
        Class cls9;
        Object[] primitiveLike = new Object[9];
        if (class$java$lang$Void == null) {
            cls = class$("java.lang.Void");
            class$java$lang$Void = cls;
        } else {
            cls = class$java$lang$Void;
        }
        primitiveLike[0] = cls;
        if (class$java$lang$Boolean == null) {
            cls2 = class$(JavaBasicTypes.JAVA_LANG_BOOLEAN);
            class$java$lang$Boolean = cls2;
        } else {
            cls2 = class$java$lang$Boolean;
        }
        primitiveLike[1] = cls2;
        if (class$java$lang$Short == null) {
            cls3 = class$(JavaBasicTypes.JAVA_LANG_SHORT);
            class$java$lang$Short = cls3;
        } else {
            cls3 = class$java$lang$Short;
        }
        primitiveLike[2] = cls3;
        if (class$java$lang$Integer == null) {
            cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
            class$java$lang$Integer = cls4;
        } else {
            cls4 = class$java$lang$Integer;
        }
        primitiveLike[3] = cls4;
        if (class$java$lang$Long == null) {
            cls5 = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls5;
        } else {
            cls5 = class$java$lang$Long;
        }
        primitiveLike[4] = cls5;
        if (class$java$lang$Float == null) {
            cls6 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
            class$java$lang$Float = cls6;
        } else {
            cls6 = class$java$lang$Float;
        }
        primitiveLike[5] = cls6;
        if (class$java$lang$Double == null) {
            cls7 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls7;
        } else {
            cls7 = class$java$lang$Double;
        }
        primitiveLike[6] = cls7;
        if (class$java$lang$Class == null) {
            cls8 = class$("java.lang.Class");
            class$java$lang$Class = cls8;
        } else {
            cls8 = class$java$lang$Class;
        }
        primitiveLike[7] = cls8;
        if (class$java$lang$String == null) {
            cls9 = class$("java.lang.String");
            class$java$lang$String = cls9;
        } else {
            cls9 = class$java$lang$String;
        }
        primitiveLike[8] = cls9;
        dontExpand = new HashSet(Arrays.asList(primitiveLike));
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    TypeDumper(String rawName, Type t, String compilerVersion, Long timestamp) {
        this.theType = t;
        this.rawName = rawName;
        this.compilerVersion = compilerVersion;
        this.timestamp = new Date(timestamp.longValue());
    }

    public static TypeDumper load(String name, TypeSystem ts) throws ClassNotFoundException, NoSuchFieldException, IOException, SecurityException {
        Class c = Class.forName(name);
        try {
            Field jlcVersion = c.getDeclaredField("jlc$CompilerVersion");
            Field jlcTimestamp = c.getDeclaredField("jlc$SourceLastModified");
            Field jlcType = c.getDeclaredField("jlc$ClassType");
            String t = (String) jlcType.get(null);
            TypeEncoder te = new TypeEncoder(ts);
            return new TypeDumper(name, te.decode(t), (String) jlcVersion.get(null), (Long) jlcTimestamp.get(null));
        } catch (IllegalAccessException exn) {
            throw new SecurityException(new StringBuffer().append("illegal access: ").append(exn.getMessage()).toString());
        }
    }

    public void dump(CodeWriter w) {
        Map cache = new HashMap();
        cache.put(this.theType, this.theType);
        w.write(new StringBuffer().append("Type ").append(this.rawName).append(" {").toString());
        w.allowBreak(2);
        w.begin(0);
        w.write(new StringBuffer().append("Compiled with polyglot version ").append(this.compilerVersion).append(".  ").toString());
        w.allowBreak(0);
        w.write(new StringBuffer().append("Last modified: ").append(this.timestamp.toString()).append(".  ").toString());
        w.allowBreak(0);
        w.write(this.theType.toString());
        w.allowBreak(4);
        w.write(new StringBuffer().append("<").append(this.theType.getClass().toString()).append(">").toString());
        w.allowBreak(0);
        dumpObject(w, this.theType, cache);
        w.allowBreak(0);
        w.end();
        w.allowBreak(0);
        w.write("}");
        w.newline(0);
    }

    static boolean dontDump(Class c) {
        return dontExpand.contains(c);
    }
}
