package soot.defaultInterfaceMethods;
/* compiled from: SimpleDefaultInterface.java */
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/Default.class */
interface Default {
    void printMessage();

    default void target() {
        System.out.println("Hello!");
    }
}
