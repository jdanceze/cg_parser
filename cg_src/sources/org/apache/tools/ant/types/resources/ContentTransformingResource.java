package org.apache.tools.ant.types.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.ResourceCollection;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/ContentTransformingResource.class */
public abstract class ContentTransformingResource extends ResourceDecorator {
    private static final int BUFFER_SIZE = 8192;

    protected abstract InputStream wrapStream(InputStream inputStream) throws IOException;

    protected abstract OutputStream wrapStream(OutputStream outputStream) throws IOException;

    /* JADX INFO: Access modifiers changed from: protected */
    public ContentTransformingResource() {
    }

    protected ContentTransformingResource(ResourceCollection other) {
        super(other);
    }

    @Override // org.apache.tools.ant.types.resources.ResourceDecorator, org.apache.tools.ant.types.Resource
    public long getSize() {
        if (isExists()) {
            try {
                InputStream in = getInputStream();
                byte[] buf = new byte[8192];
                int size = 0;
                while (true) {
                    int readNow = in.read(buf, 0, buf.length);
                    if (readNow <= 0) {
                        break;
                    }
                    size += readNow;
                }
                long j = size;
                if (in != null) {
                    in.close();
                }
                return j;
            } catch (IOException ex) {
                throw new BuildException("caught exception while reading " + getName(), ex);
            }
        }
        return 0L;
    }

    @Override // org.apache.tools.ant.types.resources.ResourceDecorator, org.apache.tools.ant.types.Resource
    public InputStream getInputStream() throws IOException {
        InputStream in = getResource().getInputStream();
        if (in != null) {
            in = wrapStream(in);
        }
        return in;
    }

    @Override // org.apache.tools.ant.types.resources.ResourceDecorator, org.apache.tools.ant.types.Resource
    public OutputStream getOutputStream() throws IOException {
        OutputStream out = getResource().getOutputStream();
        if (out != null) {
            out = wrapStream(out);
        }
        return out;
    }

    @Override // org.apache.tools.ant.types.resources.ResourceDecorator, org.apache.tools.ant.types.Resource
    public <T> T as(Class<T> clazz) {
        Appendable a;
        if (Appendable.class.isAssignableFrom(clazz)) {
            if (isAppendSupported() && (a = (Appendable) getResource().as(Appendable.class)) != null) {
                return clazz.cast(()
                /*  JADX ERROR: Method code generation error
                    jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x002c: RETURN  
                      (wrap: T : 0x0029: INVOKE  (r0v18 T A[REMOVE]) = 
                      (r5v0 'clazz' java.lang.Class<T> A[D('clazz' java.lang.Class<T>)])
                      (wrap: org.apache.tools.ant.types.resources.Appendable : 0x0024: INVOKE_CUSTOM (r1v5 org.apache.tools.ant.types.resources.Appendable A[REMOVE]) = 
                      (r4v0 'this' org.apache.tools.ant.types.resources.ContentTransformingResource A[D('this' org.apache.tools.ant.types.resources.ContentTransformingResource), DONT_INLINE, IMMUTABLE_TYPE, THIS])
                      (r0v14 'a' org.apache.tools.ant.types.resources.Appendable A[D('a' org.apache.tools.ant.types.resources.Appendable), DONT_INLINE])
                    
                     handle type: INVOKE_DIRECT
                     lambda: org.apache.tools.ant.types.resources.Appendable.getAppendOutputStream():java.io.OutputStream
                     call insn: ?: INVOKE  
                      (r1 I:org.apache.tools.ant.types.resources.ContentTransformingResource)
                      (r2 I:org.apache.tools.ant.types.resources.Appendable)
                     type: DIRECT call: org.apache.tools.ant.types.resources.ContentTransformingResource.lambda$as$0(org.apache.tools.ant.types.resources.Appendable):java.io.OutputStream)
                     type: VIRTUAL call: java.lang.Class.cast(java.lang.Object):java.lang.Object)
                     in method: org.apache.tools.ant.types.resources.ContentTransformingResource.as(java.lang.Class<T>):T, file: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/resources/ContentTransformingResource.class
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:289)
                    	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:252)
                    	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:91)
                    	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.dex.regions.Region.generate(Region.java:35)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:80)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:123)
                    	at jadx.core.dex.regions.conditions.IfRegion.generate(IfRegion.java:90)
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
                    Caused by: java.lang.IndexOutOfBoundsException: Index 1 out of bounds for length 1
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
                    	... 29 more
                    */
                /*
                    this = this;
                    java.lang.Class<org.apache.tools.ant.types.resources.Appendable> r0 = org.apache.tools.ant.types.resources.Appendable.class
                    r1 = r5
                    boolean r0 = r0.isAssignableFrom(r1)
                    if (r0 == 0) goto L2f
                    r0 = r4
                    boolean r0 = r0.isAppendSupported()
                    if (r0 == 0) goto L2d
                    r0 = r4
                    org.apache.tools.ant.types.Resource r0 = r0.getResource()
                    java.lang.Class<org.apache.tools.ant.types.resources.Appendable> r1 = org.apache.tools.ant.types.resources.Appendable.class
                    java.lang.Object r0 = r0.as(r1)
                    org.apache.tools.ant.types.resources.Appendable r0 = (org.apache.tools.ant.types.resources.Appendable) r0
                    r6 = r0
                    r0 = r6
                    if (r0 == 0) goto L2d
                    r0 = r5
                    r1 = r4
                    r2 = r6
                    T r1 = () -> { // org.apache.tools.ant.types.resources.Appendable.getAppendOutputStream():java.io.OutputStream
                        return r1.lambda$as$0(r2);
                    }
                    java.lang.Object r0 = r0.cast(r1)
                    return r0
                L2d:
                    r0 = 0
                    return r0
                L2f:
                    java.lang.Class<org.apache.tools.ant.types.resources.FileProvider> r0 = org.apache.tools.ant.types.resources.FileProvider.class
                    r1 = r5
                    boolean r0 = r0.isAssignableFrom(r1)
                    if (r0 == 0) goto L3c
                    r0 = 0
                    goto L44
                L3c:
                    r0 = r4
                    org.apache.tools.ant.types.Resource r0 = r0.getResource()
                    r1 = r5
                    java.lang.Object r0 = r0.as(r1)
                L44:
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: org.apache.tools.ant.types.resources.ContentTransformingResource.as(java.lang.Class):java.lang.Object");
            }

            protected boolean isAppendSupported() {
                return false;
            }
        }
