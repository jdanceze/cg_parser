package org.jf.dexlib2.dexbacked.util;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Iterator;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.AccessFlags;
import org.jf.dexlib2.dexbacked.DexBackedDexFile;
import org.jf.dexlib2.dexbacked.DexBackedMethod;
import org.jf.dexlib2.dexbacked.DexBackedMethodImplementation;
import org.jf.dexlib2.dexbacked.DexReader;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.debug.DebugItem;
import org.jf.dexlib2.iface.debug.EndLocal;
import org.jf.dexlib2.iface.debug.LocalInfo;
import org.jf.dexlib2.immutable.debug.ImmutableEndLocal;
import org.jf.dexlib2.immutable.debug.ImmutableEpilogueBegin;
import org.jf.dexlib2.immutable.debug.ImmutableLineNumber;
import org.jf.dexlib2.immutable.debug.ImmutablePrologueEnd;
import org.jf.dexlib2.immutable.debug.ImmutableRestartLocal;
import org.jf.dexlib2.immutable.debug.ImmutableSetSourceFile;
import org.jf.dexlib2.immutable.debug.ImmutableStartLocal;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/DebugInfo.class */
public abstract class DebugInfo implements Iterable<DebugItem> {
    @Nonnull
    public abstract Iterator<String> getParameterNames(@Nullable DexReader dexReader);

    public abstract int getSize();

    public static DebugInfo newOrEmpty(@Nonnull DexBackedDexFile dexFile, int debugInfoOffset, @Nonnull DexBackedMethodImplementation methodImpl) {
        if (debugInfoOffset == 0) {
            return EmptyDebugInfo.INSTANCE;
        }
        return new DebugInfoImpl(dexFile, debugInfoOffset, methodImpl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/DebugInfo$EmptyDebugInfo.class */
    public static class EmptyDebugInfo extends DebugInfo {
        public static final EmptyDebugInfo INSTANCE = new EmptyDebugInfo();

        private EmptyDebugInfo() {
        }

        @Override // java.lang.Iterable
        @Nonnull
        public Iterator<DebugItem> iterator() {
            return ImmutableSet.of().iterator();
        }

        @Override // org.jf.dexlib2.dexbacked.util.DebugInfo
        @Nonnull
        public Iterator<String> getParameterNames(@Nullable DexReader reader) {
            return ImmutableSet.of().iterator();
        }

        @Override // org.jf.dexlib2.dexbacked.util.DebugInfo
        public int getSize() {
            return 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/dexbacked/util/DebugInfo$DebugInfoImpl.class */
    public static class DebugInfoImpl extends DebugInfo {
        @Nonnull
        public final DexBackedDexFile dexFile;
        private final int debugInfoOffset;
        @Nonnull
        private final DexBackedMethodImplementation methodImpl;
        private static final LocalInfo EMPTY_LOCAL_INFO = new LocalInfo() { // from class: org.jf.dexlib2.dexbacked.util.DebugInfo.DebugInfoImpl.1
            @Override // org.jf.dexlib2.iface.debug.LocalInfo
            @Nullable
            public String getName() {
                return null;
            }

            @Override // org.jf.dexlib2.iface.debug.LocalInfo
            @Nullable
            public String getType() {
                return null;
            }

            @Override // org.jf.dexlib2.iface.debug.LocalInfo
            @Nullable
            public String getSignature() {
                return null;
            }
        };

        public DebugInfoImpl(@Nonnull DexBackedDexFile dexFile, int debugInfoOffset, @Nonnull DexBackedMethodImplementation methodImpl) {
            this.dexFile = dexFile;
            this.debugInfoOffset = debugInfoOffset;
            this.methodImpl = methodImpl;
        }

        @Override // java.lang.Iterable
        @Nonnull
        public Iterator<DebugItem> iterator() {
            DexReader reader = this.dexFile.getDataBuffer().readerAt(this.debugInfoOffset);
            final int lineNumberStart = reader.readBigUleb128();
            int registerCount = this.methodImpl.getRegisterCount();
            final LocalInfo[] locals = new LocalInfo[registerCount];
            Arrays.fill(locals, EMPTY_LOCAL_INFO);
            DexBackedMethod method = this.methodImpl.method;
            Iterator<? extends MethodParameter> parameterIterator = new ParameterIterator(method.getParameterTypes(), method.getParameterAnnotations(), getParameterNames(reader));
            int parameterIndex = 0;
            if (!AccessFlags.STATIC.isSet(this.methodImpl.method.getAccessFlags())) {
                parameterIndex = 0 + 1;
                locals[0] = new LocalInfo() { // from class: org.jf.dexlib2.dexbacked.util.DebugInfo.DebugInfoImpl.2
                    @Override // org.jf.dexlib2.iface.debug.LocalInfo
                    public String getName() {
                        return "this";
                    }

                    @Override // org.jf.dexlib2.iface.debug.LocalInfo
                    public String getType() {
                        return DebugInfoImpl.this.methodImpl.method.getDefiningClass();
                    }

                    @Override // org.jf.dexlib2.iface.debug.LocalInfo
                    public String getSignature() {
                        return null;
                    }
                };
            }
            while (parameterIterator.hasNext()) {
                int i = parameterIndex;
                parameterIndex++;
                locals[i] = parameterIterator.next();
            }
            if (parameterIndex < registerCount) {
                int localIndex = registerCount - 1;
                while (true) {
                    parameterIndex--;
                    if (parameterIndex <= -1) {
                        break;
                    }
                    LocalInfo currentLocal = locals[parameterIndex];
                    String type = currentLocal.getType();
                    if (type != null && (type.equals("J") || type.equals("D"))) {
                        localIndex--;
                        if (localIndex == parameterIndex) {
                            break;
                        }
                    }
                    locals[localIndex] = currentLocal;
                    locals[parameterIndex] = EMPTY_LOCAL_INFO;
                    localIndex--;
                }
            }
            return new VariableSizeLookaheadIterator<DebugItem>(this.dexFile.getDataBuffer(), reader.getOffset()) { // from class: org.jf.dexlib2.dexbacked.util.DebugInfo.DebugInfoImpl.3
                private int codeAddress = 0;
                private int lineNumber;

                {
                    this.lineNumber = lineNumberStart;
                }

                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // org.jf.dexlib2.dexbacked.util.VariableSizeLookaheadIterator
                @Nullable
                public DebugItem readNextItem(@Nonnull DexReader reader2) {
                    LocalInfo localInfo;
                    LocalInfo localInfo2;
                    while (true) {
                        int next = reader2.readUbyte();
                        switch (next) {
                            case 0:
                                return endOfData();
                            case 1:
                                int addressDiff = reader2.readSmallUleb128();
                                this.codeAddress += addressDiff;
                                break;
                            case 2:
                                int lineDiff = reader2.readSleb128();
                                this.lineNumber += lineDiff;
                                break;
                            case 3:
                                int register = reader2.readSmallUleb128();
                                String name = DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                                String type2 = DebugInfoImpl.this.dexFile.getTypeSection().getOptional(reader2.readSmallUleb128() - 1);
                                ImmutableStartLocal startLocal = new ImmutableStartLocal(this.codeAddress, register, name, type2, null);
                                if (register >= 0 && register < locals.length) {
                                    locals[register] = startLocal;
                                }
                                return startLocal;
                            case 4:
                                int register2 = reader2.readSmallUleb128();
                                String name2 = DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                                String type3 = DebugInfoImpl.this.dexFile.getTypeSection().getOptional(reader2.readSmallUleb128() - 1);
                                String signature = DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                                ImmutableStartLocal startLocal2 = new ImmutableStartLocal(this.codeAddress, register2, name2, type3, signature);
                                if (register2 >= 0 && register2 < locals.length) {
                                    locals[register2] = startLocal2;
                                }
                                return startLocal2;
                            case 5:
                                int register3 = reader2.readSmallUleb128();
                                boolean replaceLocalInTable = true;
                                if (register3 < 0 || register3 >= locals.length) {
                                    localInfo2 = DebugInfoImpl.EMPTY_LOCAL_INFO;
                                    replaceLocalInTable = false;
                                } else {
                                    localInfo2 = locals[register3];
                                }
                                if (localInfo2 instanceof EndLocal) {
                                    localInfo2 = DebugInfoImpl.EMPTY_LOCAL_INFO;
                                    replaceLocalInTable = false;
                                }
                                ImmutableEndLocal endLocal = new ImmutableEndLocal(this.codeAddress, register3, localInfo2.getName(), localInfo2.getType(), localInfo2.getSignature());
                                if (replaceLocalInTable) {
                                    locals[register3] = endLocal;
                                }
                                return endLocal;
                            case 6:
                                int register4 = reader2.readSmallUleb128();
                                if (register4 < 0 || register4 >= locals.length) {
                                    localInfo = DebugInfoImpl.EMPTY_LOCAL_INFO;
                                } else {
                                    localInfo = locals[register4];
                                }
                                ImmutableRestartLocal restartLocal = new ImmutableRestartLocal(this.codeAddress, register4, localInfo.getName(), localInfo.getType(), localInfo.getSignature());
                                if (register4 >= 0 && register4 < locals.length) {
                                    locals[register4] = restartLocal;
                                }
                                return restartLocal;
                            case 7:
                                return new ImmutablePrologueEnd(this.codeAddress);
                            case 8:
                                return new ImmutableEpilogueBegin(this.codeAddress);
                            case 9:
                                String sourceFile = DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                                return new ImmutableSetSourceFile(this.codeAddress, sourceFile);
                            default:
                                int adjusted = next - 10;
                                this.codeAddress += adjusted / 15;
                                this.lineNumber += (adjusted % 15) - 4;
                                return new ImmutableLineNumber(this.codeAddress, this.lineNumber);
                        }
                    }
                }
            };
        }

        @Override // org.jf.dexlib2.dexbacked.util.DebugInfo
        @Nonnull
        public VariableSizeIterator<String> getParameterNames(@Nullable DexReader reader) {
            if (reader == null) {
                reader = this.dexFile.getDataBuffer().readerAt(this.debugInfoOffset);
                reader.skipUleb128();
            }
            int parameterNameCount = reader.readSmallUleb128();
            return new VariableSizeIterator<String>(reader, parameterNameCount) { // from class: org.jf.dexlib2.dexbacked.util.DebugInfo.DebugInfoImpl.4
                /* JADX INFO: Access modifiers changed from: protected */
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // org.jf.dexlib2.dexbacked.util.VariableSizeIterator
                public String readNextItem(@Nonnull DexReader reader2, int index) {
                    return DebugInfoImpl.this.dexFile.getStringSection().getOptional(reader2.readSmallUleb128() - 1);
                }
            };
        }

        @Override // org.jf.dexlib2.dexbacked.util.DebugInfo
        public int getSize() {
            Iterator<DebugItem> iter = iterator();
            while (iter.hasNext()) {
                iter.next();
            }
            return ((VariableSizeLookaheadIterator) iter).getReaderOffset() - this.debugInfoOffset;
        }
    }
}
