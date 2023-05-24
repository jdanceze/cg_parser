package org.mockito.plugins;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.mockito.Incubating;
@Incubating
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MemberAccessor.class */
public interface MemberAccessor {

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MemberAccessor$ConstructionDispatcher.class */
    public interface ConstructionDispatcher {
        Object newInstance() throws InstantiationException, InvocationTargetException, IllegalAccessException;
    }

    /* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MemberAccessor$OnConstruction.class */
    public interface OnConstruction {
        Object invoke(ConstructionDispatcher constructionDispatcher) throws InstantiationException, InvocationTargetException, IllegalAccessException;
    }

    Object newInstance(Constructor<?> constructor, Object... objArr) throws InstantiationException, InvocationTargetException, IllegalAccessException;

    Object invoke(Method method, Object obj, Object... objArr) throws InvocationTargetException, IllegalAccessException;

    Object get(Field field, Object obj) throws IllegalAccessException;

    void set(Field field, Object obj, Object obj2) throws IllegalAccessException;

    default Object newInstance(Constructor<?> constructor, OnConstruction onConstruction, Object... arguments) throws InstantiationException, InvocationTargetException, IllegalAccessException {
        return onConstruction.invoke(()
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x000e: RETURN  
              (wrap: java.lang.Object : 0x0009: INVOKE  (r0v1 java.lang.Object A[REMOVE]) = 
              (r7v0 'onConstruction' org.mockito.plugins.MemberAccessor$OnConstruction A[D('onConstruction' org.mockito.plugins.MemberAccessor$OnConstruction)])
              (wrap: org.mockito.plugins.MemberAccessor$ConstructionDispatcher : 0x0004: INVOKE_CUSTOM (r1v1 org.mockito.plugins.MemberAccessor$ConstructionDispatcher A[REMOVE]) = 
              (r5v0 'this' org.mockito.plugins.MemberAccessor A[D('this' org.mockito.plugins.MemberAccessor), DONT_INLINE, IMMUTABLE_TYPE, THIS])
              (r6v0 'constructor' java.lang.reflect.Constructor<?> A[D('constructor' java.lang.reflect.Constructor<?>), DONT_INLINE])
              (r8v0 'arguments' java.lang.Object[] A[D('arguments' java.lang.Object[]), DONT_INLINE])
            
             handle type: INVOKE_DIRECT
             lambda: org.mockito.plugins.MemberAccessor.ConstructionDispatcher.newInstance():java.lang.Object
             call insn: ?: INVOKE  (r1 I:org.mockito.plugins.MemberAccessor), (r2 I:java.lang.reflect.Constructor), (r3 I:java.lang.Object[]) type: DIRECT call: org.mockito.plugins.MemberAccessor.lambda$newInstance$0(java.lang.reflect.Constructor, java.lang.Object[]):java.lang.Object)
             type: INTERFACE call: org.mockito.plugins.MemberAccessor.OnConstruction.invoke(org.mockito.plugins.MemberAccessor$ConstructionDispatcher):java.lang.Object)
             in method: org.mockito.plugins.MemberAccessor.newInstance(java.lang.reflect.Constructor<?>, org.mockito.plugins.MemberAccessor$OnConstruction, java.lang.Object[]):java.lang.Object, file: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/plugins/MemberAccessor.class
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
            Caused by: java.lang.IndexOutOfBoundsException: Index 2 out of bounds for length 2
            	at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:100)
            	at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:106)
            	at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:302)
            	at java.base/java.util.Objects.checkIndex(Objects.java:385)
            	at java.base/java.util.ArrayList.get(ArrayList.java:427)
            	at jadx.core.codegen.InsnGen.makeInlinedLambdaMethod(InsnGen.java:998)
            	at jadx.core.codegen.InsnGen.makeInvokeLambda(InsnGen.java:903)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:794)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:401)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:143)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:119)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:106)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:1075)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:851)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:401)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:143)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:119)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:106)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:347)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:282)
            	... 15 more
            */
        /*
            this = this;
            r0 = r7
            r1 = r5
            r2 = r6
            r3 = r8
            java.lang.Object r1 = () -> { // org.mockito.plugins.MemberAccessor.ConstructionDispatcher.newInstance():java.lang.Object
                return r1.lambda$newInstance$0(r2, r3);
            }
            java.lang.Object r0 = r0.invoke(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mockito.plugins.MemberAccessor.newInstance(java.lang.reflect.Constructor, org.mockito.plugins.MemberAccessor$OnConstruction, java.lang.Object[]):java.lang.Object");
    }
}
