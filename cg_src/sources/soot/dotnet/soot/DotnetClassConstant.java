package soot.dotnet.soot;

import soot.jimple.ClassConstant;
/* loaded from: gencallgraphv3.jar:soot/dotnet/soot/DotnetClassConstant.class */
public class DotnetClassConstant extends ClassConstant {
    private DotnetClassConstant(String s) {
        super(convertDotnetClassToJvmDescriptor(s));
    }

    private static String convertDotnetClassToJvmDescriptor(String s) {
        try {
            return "L" + s.replace(".", "/").replace("+", "$") + ";";
        } catch (Exception e) {
            throw new RuntimeException("Cannot convert Dotnet class \"" + s + "\" to JVM Descriptor: " + e);
        }
    }

    public static DotnetClassConstant v(String value) {
        return new DotnetClassConstant(value);
    }

    @Override // soot.jimple.ClassConstant
    public boolean equals(Object c) {
        return (c instanceof ClassConstant) && ((ClassConstant) c).value.equals(this.value);
    }
}
