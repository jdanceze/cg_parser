package soot.lambdaMetaFactory;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/MarkerInterfaces.class */
public class MarkerInterfaces {

    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/MarkerInterfaces$Marker1.class */
    public interface Marker1 {
    }

    /* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/MarkerInterfaces$Marker2.class */
    public interface Marker2 {
    }

    /*  JADX ERROR: Dependency scan failed at insn: 0x0001: INVOKE_CUSTOM
        jadx.plugins.input.java.utils.JavaClassParseException: Can't encode constant CLASS as encoded value
        	at jadx.plugins.input.java.data.ConstPoolReader.readAsEncodedValue(ConstPoolReader.java:230)
        	at jadx.plugins.input.java.data.ConstPoolReader.resolveMethodCallSite(ConstPoolReader.java:117)
        	at jadx.plugins.input.java.data.ConstPoolReader.getCallSite(ConstPoolReader.java:97)
        	at jadx.plugins.input.java.data.code.JavaInsnData.getIndexAsCallSite(JavaInsnData.java:168)
        	at jadx.plugins.input.java.data.code.decoders.InvokeDecoder.decode(InvokeDecoder.java:32)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processInsn(UsageInfoVisitor.java:127)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.lambda$processInstructions$0(UsageInfoVisitor.java:84)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:82)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processInstructions(UsageInfoVisitor.java:82)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processMethod(UsageInfoVisitor.java:67)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.processClass(UsageInfoVisitor.java:56)
        	at jadx.core.dex.visitors.usage.UsageInfoVisitor.init(UsageInfoVisitor.java:41)
        	at jadx.core.dex.nodes.RootNode.runPreDecompileStage(RootNode.java:275)
        */
    /*  JADX ERROR: Failed to decode insn: 0x0001: INVOKE_CUSTOM, method: soot.lambdaMetaFactory.MarkerInterfaces.main():void
        jadx.plugins.input.java.utils.JavaClassParseException: Can't encode constant CLASS as encoded value
        	at jadx.plugins.input.java.data.ConstPoolReader.readAsEncodedValue(ConstPoolReader.java:230)
        	at jadx.plugins.input.java.data.ConstPoolReader.resolveMethodCallSite(ConstPoolReader.java:117)
        	at jadx.plugins.input.java.data.ConstPoolReader.getCallSite(ConstPoolReader.java:97)
        	at jadx.plugins.input.java.data.code.JavaInsnData.getIndexAsCallSite(JavaInsnData.java:168)
        	at jadx.plugins.input.java.data.code.decoders.InvokeDecoder.decode(InvokeDecoder.java:32)
        	at jadx.plugins.input.java.data.code.JavaInsnData.decode(JavaInsnData.java:46)
        	at jadx.core.dex.instructions.InsnDecoder.lambda$process$0(InsnDecoder.java:52)
        	at jadx.plugins.input.java.data.code.JavaCodeReader.visitInstructions(JavaCodeReader.java:82)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:48)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:148)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:413)
        	at jadx.core.ProcessClass.process(ProcessClass.java:67)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:115)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:387)
        	at jadx.core.dex.nodes.ClassNode.getCode(ClassNode.java:335)
        */
    public void main() {
        /*
            r3 = this;
            r0 = r3
            // decode failed: Can't encode constant CLASS as encoded value
            r1 = r4
            java.lang.Object r1 = r1.get()
            java.lang.String r1 = (java.lang.String) r1
            r0.println(r1)
            return
            r0 = 0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.lambdaMetaFactory.MarkerInterfaces.main():void");
    }

    /* renamed from: getString */
    public String lambda$0() {
        return "Hello";
    }
}
