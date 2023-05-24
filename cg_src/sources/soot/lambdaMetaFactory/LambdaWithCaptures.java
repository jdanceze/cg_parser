package soot.lambdaMetaFactory;

import java.util.function.Supplier;
/* loaded from: gencallgraphv3.jar:soot/lambdaMetaFactory/LambdaWithCaptures.class */
public class LambdaWithCaptures {
    public void main() {
        Supplier<String> s = ()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x0005: INVOKE_CUSTOM (r0v2 's' java.util.function.Supplier<java.lang.String>) = 
              (r3v0 'this' soot.lambdaMetaFactory.LambdaWithCaptures A[D('this' soot.lambdaMetaFactory.LambdaWithCaptures), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              ("Hello")
            
             handle type: INVOKE_DIRECT
             lambda: java.util.function.Supplier.get():java.lang.Object
             call insn: ?: INVOKE  (r0 I:soot.lambdaMetaFactory.LambdaWithCaptures), (r1 I:java.lang.String) type: DIRECT call: soot.lambdaMetaFactory.LambdaWithCaptures.lambda$0(java.lang.String):java.lang.String in method: soot.lambdaMetaFactory.LambdaWithCaptures.main():void, file: gencallgraphv3.jar:soot/lambdaMetaFactory/LambdaWithCaptures.class
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:289)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:252)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:296)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:275)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:377)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:306)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:272)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            Caused by: java.lang.ClassCastException: class jadx.core.dex.instructions.args.InsnWrapArg cannot be cast to class jadx.core.dex.instructions.args.RegisterArg (jadx.core.dex.instructions.args.InsnWrapArg and jadx.core.dex.instructions.args.RegisterArg are in unnamed module of loader 'app')
            	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:997)
            	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:903)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:794)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:401)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:282)
            	... 15 more
            */
        /*
            this = this;
            java.lang.String r0 = "Hello"
            r4 = r0
            r0 = r3
            r1 = r4
            void r0 = () -> { // java.util.function.Supplier.get():java.lang.Object
                return r0.lambda$0(r1);
            }
            r5 = r0
            java.io.PrintStream r0 = java.lang.System.out
            r1 = r5
            java.lang.Object r1 = r1.get()
            java.lang.String r1 = (java.lang.String) r1
            r0.println(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.lambdaMetaFactory.LambdaWithCaptures.main():void");
    }

    private String getString() {
        return "World";
    }
}
