package soot.defaultInterfaceMethods;
/* loaded from: gencallgraphv3.jar:soot/defaultInterfaceMethods/ClassInterfaceSameSignature.class */
public class ClassInterfaceSameSignature implements HelloWorld {
    @Override // soot.defaultInterfaceMethods.HelloWorld
    public void print() {
        System.out.println("Welcome to Java 8");
    }

    public void main() {
        ClassInterfaceSameSignature testClass = new ClassInterfaceSameSignature();
        testClass.print();
    }
}
