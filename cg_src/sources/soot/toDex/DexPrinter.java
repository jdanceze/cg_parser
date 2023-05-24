package soot.toDex;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.commons.cli.HelpFormatter;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.Opcodes;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.BuilderOffsetInstruction;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.builder.MethodImplementationBuilder;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.AnnotationElement;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.ExceptionHandler;
import org.jf.dexlib2.iface.Field;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.MethodParameter;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.StringReference;
import org.jf.dexlib2.iface.reference.TypeReference;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.immutable.ImmutableAnnotation;
import org.jf.dexlib2.immutable.ImmutableAnnotationElement;
import org.jf.dexlib2.immutable.ImmutableClassDef;
import org.jf.dexlib2.immutable.ImmutableExceptionHandler;
import org.jf.dexlib2.immutable.ImmutableField;
import org.jf.dexlib2.immutable.ImmutableMethod;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;
import org.jf.dexlib2.immutable.reference.ImmutableFieldReference;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.immutable.reference.ImmutableStringReference;
import org.jf.dexlib2.immutable.reference.ImmutableTypeReference;
import org.jf.dexlib2.immutable.value.ImmutableAnnotationEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableArrayEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableBooleanEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableByteEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableCharEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableDoubleEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableEnumEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableFieldEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableFloatEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableIntEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableLongEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableMethodEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableNullEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableShortEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableStringEncodedValue;
import org.jf.dexlib2.immutable.value.ImmutableTypeEncodedValue;
import org.jf.dexlib2.writer.builder.BuilderEncodedValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.CompilationDeathException;
import soot.IntType;
import soot.Local;
import soot.PackManager;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.SourceLocator;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.coffi.Instruction;
import soot.dexpler.DexInnerClassParser;
import soot.dexpler.DexType;
import soot.dexpler.Util;
import soot.jimple.ClassConstant;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.MonitorStmt;
import soot.jimple.NopStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.scalar.EmptySwitchEliminator;
import soot.options.Options;
import soot.tagkit.AbstractHost;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationBooleanElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationDefaultTag;
import soot.tagkit.AnnotationDoubleElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationEnumElem;
import soot.tagkit.AnnotationFloatElem;
import soot.tagkit.AnnotationIntElem;
import soot.tagkit.AnnotationLongElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationTag;
import soot.tagkit.ConstantValueTag;
import soot.tagkit.DeprecatedTag;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.EnclosingMethodTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.InnerClassAttribute;
import soot.tagkit.InnerClassTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LineNumberTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SignatureTag;
import soot.tagkit.SourceFileTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;
import soot.toDex.instructions.Insn;
import soot.toDex.instructions.Insn10t;
import soot.toDex.instructions.Insn30t;
import soot.toDex.instructions.InsnWithOffset;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/toDex/DexPrinter.class */
public class DexPrinter {
    private static final Logger LOGGER;
    public static final Pattern SIGNATURE_FILE_PATTERN;
    protected MultiDexBuilder dexBuilder = createDexBuilder();
    protected File originalApk;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DexPrinter.class.desiredAssertionStatus();
        LOGGER = LoggerFactory.getLogger(DexPrinter.class);
        SIGNATURE_FILE_PATTERN = Pattern.compile("META-INF/[^/]+(\\.SF|\\.DSA|\\.RSA|\\.EC)$");
    }

    protected MultiDexBuilder createDexBuilder() {
        int apiLevel;
        Scene.AndroidVersionInfo androidSDKVersionInfo = Scene.v().getAndroidSDKVersionInfo();
        if (androidSDKVersionInfo == null) {
            apiLevel = Scene.v().getAndroidAPIVersion();
        } else {
            apiLevel = Math.min(androidSDKVersionInfo.minSdkVersion, androidSDKVersionInfo.sdkTargetVersion);
        }
        return new MultiDexBuilder(Opcodes.forApi(apiLevel));
    }

    private static boolean isSignatureFile(String fileName) {
        return SIGNATURE_FILE_PATTERN.matcher(fileName).matches();
    }

    private static int getVisibility(int visibility) {
        if (visibility == 0) {
            return 1;
        }
        if (visibility == 1) {
            return 2;
        }
        if (visibility == 2) {
            return 0;
        }
        throw new DexPrinterException("Unknown annotation visibility: '" + visibility + "'");
    }

    protected static FieldReference toFieldReference(SootField f) {
        FieldReference fieldRef = new ImmutableFieldReference(SootToDexUtils.getDexClassName(f.getDeclaringClass().getName()), f.getName(), SootToDexUtils.getDexTypeDescriptor(f.getType()));
        return fieldRef;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static FieldReference toFieldReference(SootFieldRef ref) {
        FieldReference fieldRef = new ImmutableFieldReference(SootToDexUtils.getDexClassName(ref.declaringClass().getName()), ref.name(), SootToDexUtils.getDexTypeDescriptor(ref.type()));
        return fieldRef;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static MethodReference toMethodReference(SootMethodRef m) {
        List<String> parameters = new ArrayList<>();
        for (Type t : m.getParameterTypes()) {
            parameters.add(SootToDexUtils.getDexTypeDescriptor(t));
        }
        return new ImmutableMethodReference(SootToDexUtils.getDexClassName(m.getDeclaringClass().getName()), m.getName(), parameters, SootToDexUtils.getDexTypeDescriptor(m.getReturnType()));
    }

    public static TypeReference toTypeReference(Type t) {
        return new ImmutableTypeReference(SootToDexUtils.getDexTypeDescriptor(t));
    }

    /* JADX WARN: Finally extract failed */
    private void printZip() throws IOException {
        Throwable th = null;
        try {
            ZipOutputStream outputZip = getZipOutputStream();
            LOGGER.info("Do not forget to sign the .apk file with jarsigner and to align it with zipalign");
            if (this.originalApk != null) {
                Throwable th2 = null;
                try {
                    ZipFile original = new ZipFile(this.originalApk);
                    try {
                        copyAllButClassesDexAndSigFiles(original, outputZip);
                        if (original != null) {
                            original.close();
                        }
                    } catch (Throwable th3) {
                        if (original != null) {
                            original.close();
                        }
                        throw th3;
                    }
                } catch (Throwable th4) {
                    if (0 == 0) {
                        th2 = th4;
                    } else if (null != th4) {
                        th2.addSuppressed(th4);
                    }
                    throw th2;
                }
            }
            Path tempPath = Files.createTempDirectory(Long.toString(System.nanoTime()), new FileAttribute[0]);
            List<File> files = this.dexBuilder.writeTo(tempPath.toString());
            if (!files.isEmpty()) {
                byte[] buffer = new byte[16384];
                for (File file : files) {
                    Throwable th5 = null;
                    try {
                        InputStream is = Files.newInputStream(file.toPath(), new OpenOption[0]);
                        try {
                            outputZip.putNextEntry(new ZipEntry(file.getName()));
                            while (true) {
                                int read = is.read(buffer);
                                if (read <= 0) {
                                    break;
                                }
                                outputZip.write(buffer, 0, read);
                            }
                            outputZip.closeEntry();
                            if (is != null) {
                                is.close();
                            }
                        } catch (Throwable th6) {
                            th5 = th6;
                            if (is != null) {
                                is.close();
                            }
                            throw th5;
                        }
                    } catch (Throwable th7) {
                        if (th5 == null) {
                            th5 = th7;
                        } else if (th5 != th7) {
                            th5.addSuppressed(th7);
                        }
                        throw th5;
                    }
                }
            }
            if (Options.v().output_jar()) {
                addManifest(outputZip, files);
            }
            Files.walkFileTree(tempPath, new SimpleFileVisitor<Path>() { // from class: soot.toDex.DexPrinter.1
                @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                public FileVisitResult visitFile(Path file2, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file2);
                    return FileVisitResult.CONTINUE;
                }

                @Override // java.nio.file.SimpleFileVisitor, java.nio.file.FileVisitor
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            if (outputZip != null) {
                outputZip.close();
            }
        } catch (Throwable th8) {
            if (0 == 0) {
                th = th8;
            } else if (null != th8) {
                th.addSuppressed(th8);
            }
            throw th;
        }
    }

    private ZipOutputStream getZipOutputStream() throws IOException {
        if (Options.v().output_jar()) {
            LOGGER.info("Writing JAR to \"{}\"", Options.v().output_dir());
            return PackManager.v().getJarFile();
        }
        String name = this.originalApk == null ? "out.apk" : this.originalApk.getName();
        if (this.originalApk == null) {
            LOGGER.warn("Setting output file name to \"{}\" as original APK has not been found.", name);
        }
        Path outputFile = Paths.get(SourceLocator.v().getOutputDir(), name);
        if (Files.exists(outputFile, LinkOption.NOFOLLOW_LINKS)) {
            if (!Options.v().force_overwrite()) {
                throw new CompilationDeathException("Output file \"" + outputFile + "\" exists. Not overwriting.");
            }
            try {
                Files.delete(outputFile);
            } catch (IOException exception) {
                throw new IllegalStateException("Removing \"" + outputFile + "\" failed. Not writing out anything.", exception);
            }
        }
        LOGGER.info("Writing APK to \"{}\".", outputFile);
        return new ZipOutputStream(Files.newOutputStream(outputFile, StandardOpenOption.CREATE_NEW));
    }

    private void copyAllButClassesDexAndSigFiles(ZipFile source, ZipOutputStream destination) throws IOException {
        Enumeration<? extends ZipEntry> sourceEntries = source.entries();
        while (sourceEntries.hasMoreElements()) {
            ZipEntry sourceEntry = sourceEntries.nextElement();
            String sourceEntryName = sourceEntry.getName();
            if (!sourceEntryName.endsWith(".dex") && !isSignatureFile(sourceEntryName)) {
                ZipEntry destinationEntry = new ZipEntry(sourceEntryName);
                destinationEntry.setMethod(sourceEntry.getMethod());
                destinationEntry.setSize(sourceEntry.getSize());
                destinationEntry.setCrc(sourceEntry.getCrc());
                destination.putNextEntry(destinationEntry);
                Throwable th = null;
                try {
                    InputStream zipEntryInput = source.getInputStream(sourceEntry);
                    byte[] buffer = new byte[2048];
                    while (true) {
                        int bytesRead = zipEntryInput.read(buffer);
                        if (bytesRead <= 0) {
                            break;
                        }
                        destination.write(buffer, 0, bytesRead);
                    }
                    if (zipEntryInput != null) {
                        zipEntryInput.close();
                    }
                } catch (Throwable th2) {
                    if (0 == 0) {
                        th = th2;
                    } else if (null != th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            }
        }
    }

    private void addManifest(ZipOutputStream destination, Collection<File> dexFiles) throws IOException {
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        manifest.getMainAttributes().put(new Attributes.Name("Created-By"), "Soot Dex Printer");
        if (dexFiles != null && !dexFiles.isEmpty()) {
            manifest.getMainAttributes().put(new Attributes.Name("Dex-Location"), dexFiles.stream().map((v0) -> {
                return v0.getName();
            }).collect(Collectors.joining(Instruction.argsep)));
        }
        ZipEntry manifestEntry = new ZipEntry("META-INF/MANIFEST.MF");
        destination.putNextEntry(manifestEntry);
        Throwable th = null;
        try {
            BufferedOutputStream bufOut = new BufferedOutputStream(destination);
            manifest.write(bufOut);
            bufOut.flush();
            if (bufOut != null) {
                bufOut.close();
            }
            destination.closeEntry();
        } catch (Throwable th2) {
            if (0 == 0) {
                th = th2;
            } else if (null != th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private EncodedValue buildEncodedValueForAnnotation(AnnotationElem elem) {
        switch (elem.getKind()) {
            case '@':
                AnnotationAnnotationElem e = (AnnotationAnnotationElem) elem;
                List<AnnotationElement> elements = null;
                Collection<AnnotationElem> elems = e.getValue().getElems();
                if (!elems.isEmpty()) {
                    elements = new ArrayList<>();
                    Set<String> alreadyWritten = new HashSet<>();
                    for (AnnotationElem ae : elems) {
                        if (!alreadyWritten.add(ae.getName())) {
                            throw new DexPrinterException("Duplicate annotation attribute: " + ae.getName());
                        }
                        elements.add(new ImmutableAnnotationElement(ae.getName(), buildEncodedValueForAnnotation(ae)));
                    }
                }
                return new ImmutableAnnotationEncodedValue(SootToDexUtils.getDexClassName(e.getValue().getType()), elements);
            case 'B':
                AnnotationIntElem e2 = (AnnotationIntElem) elem;
                return new ImmutableByteEncodedValue((byte) e2.getValue());
            case 'C':
                AnnotationIntElem e3 = (AnnotationIntElem) elem;
                return new ImmutableCharEncodedValue((char) e3.getValue());
            case 'D':
                AnnotationDoubleElem e4 = (AnnotationDoubleElem) elem;
                return new ImmutableDoubleEncodedValue(e4.getValue());
            case 'F':
                AnnotationFloatElem e5 = (AnnotationFloatElem) elem;
                return new ImmutableFloatEncodedValue(e5.getValue());
            case 'I':
                AnnotationIntElem e6 = (AnnotationIntElem) elem;
                return new ImmutableIntEncodedValue(e6.getValue());
            case 'J':
                AnnotationLongElem e7 = (AnnotationLongElem) elem;
                return new ImmutableLongEncodedValue(e7.getValue());
            case 'M':
                AnnotationStringElem e8 = (AnnotationStringElem) elem;
                String[] sp = e8.getValue().split(Instruction.argsep);
                String classString = SootToDexUtils.getDexClassName(sp[0].split(":")[0]);
                if (classString.isEmpty()) {
                    throw new DexPrinterException("Empty class name in annotation");
                }
                String returnType = sp[1];
                String[] sp2 = sp[2].split("\\(");
                String methodNameString = sp2[0];
                String parameters = sp2[1].replaceAll("\\)", "");
                List<String> paramTypeList = parameters.isEmpty() ? null : Arrays.asList(parameters.split(","));
                return new ImmutableMethodEncodedValue(new ImmutableMethodReference(classString, methodNameString, paramTypeList, returnType));
            case 'N':
                return ImmutableNullEncodedValue.INSTANCE;
            case 'S':
                AnnotationIntElem e9 = (AnnotationIntElem) elem;
                return new ImmutableShortEncodedValue((short) e9.getValue());
            case 'Z':
                if (elem instanceof AnnotationIntElem) {
                    AnnotationIntElem e10 = (AnnotationIntElem) elem;
                    switch (e10.getValue()) {
                        case 0:
                            return ImmutableBooleanEncodedValue.FALSE_VALUE;
                        case 1:
                            return ImmutableBooleanEncodedValue.TRUE_VALUE;
                        default:
                            throw new DexPrinterException("error: boolean value from int with value != 0 or 1.");
                    }
                } else if (elem instanceof AnnotationBooleanElem) {
                    AnnotationBooleanElem e11 = (AnnotationBooleanElem) elem;
                    if (e11.getValue()) {
                        return ImmutableBooleanEncodedValue.TRUE_VALUE;
                    }
                    return ImmutableBooleanEncodedValue.FALSE_VALUE;
                } else {
                    throw new DexPrinterException("Annotation type incompatible with target type boolean");
                }
            case '[':
                AnnotationArrayElem e12 = (AnnotationArrayElem) elem;
                List<EncodedValue> values = new ArrayList<>();
                for (int i = 0; i < e12.getNumValues(); i++) {
                    values.add(buildEncodedValueForAnnotation(e12.getValueAt(i)));
                }
                return new ImmutableArrayEncodedValue(values);
            case 'c':
                AnnotationClassElem e13 = (AnnotationClassElem) elem;
                return new ImmutableTypeEncodedValue(e13.getDesc());
            case 'e':
                AnnotationEnumElem e14 = (AnnotationEnumElem) elem;
                String classT = SootToDexUtils.getDexClassName(e14.getTypeName());
                return new ImmutableEnumEncodedValue(new ImmutableFieldReference(classT, e14.getConstantName(), classT));
            case 'f':
                AnnotationStringElem e15 = (AnnotationStringElem) elem;
                String fSig = e15.getValue();
                String[] sp3 = fSig.split(Instruction.argsep);
                String classString2 = SootToDexUtils.getDexClassName(sp3[0].split(":")[0]);
                if (classString2.isEmpty()) {
                    throw new DexPrinterException("Empty class name in annotation");
                }
                String typeString = sp3[1];
                if (typeString.isEmpty()) {
                    throw new DexPrinterException("Empty type string in annotation");
                }
                String fieldName = sp3[2];
                return new ImmutableFieldEncodedValue(new ImmutableFieldReference(classString2, fieldName, typeString));
            case 's':
                AnnotationStringElem e16 = (AnnotationStringElem) elem;
                return new ImmutableStringEncodedValue(e16.getValue());
            default:
                throw new DexPrinterException("Unknown Elem Attr Kind: " + elem.getKind());
        }
    }

    protected EncodedValue makeConstantItem(SootField sf, Tag t) {
        if (!(t instanceof ConstantValueTag)) {
            throw new DexPrinterException("error: t not ConstantValueTag.");
        }
        if (t instanceof IntegerConstantValueTag) {
            IntegerConstantValueTag i = (IntegerConstantValueTag) t;
            Type sft = sf.getType();
            if (sft instanceof BooleanType) {
                int v = i.getIntValue();
                switch (v) {
                    case 0:
                        return ImmutableBooleanEncodedValue.FALSE_VALUE;
                    case 1:
                        return ImmutableBooleanEncodedValue.TRUE_VALUE;
                    default:
                        throw new DexPrinterException("error: boolean value from int with value != 0 or 1.");
                }
            } else if (sft instanceof CharType) {
                return new ImmutableCharEncodedValue((char) i.getIntValue());
            } else {
                if (sft instanceof ByteType) {
                    return new ImmutableByteEncodedValue((byte) i.getIntValue());
                }
                if (sft instanceof IntType) {
                    return new ImmutableIntEncodedValue(i.getIntValue());
                }
                if (sft instanceof ShortType) {
                    return new ImmutableShortEncodedValue((short) i.getIntValue());
                }
                throw new DexPrinterException("error: unexpected constant tag type: " + t + " for field " + sf);
            }
        } else if (t instanceof LongConstantValueTag) {
            LongConstantValueTag l = (LongConstantValueTag) t;
            return new ImmutableLongEncodedValue(l.getLongValue());
        } else if (t instanceof DoubleConstantValueTag) {
            DoubleConstantValueTag d = (DoubleConstantValueTag) t;
            return new ImmutableDoubleEncodedValue(d.getDoubleValue());
        } else if (t instanceof FloatConstantValueTag) {
            FloatConstantValueTag f = (FloatConstantValueTag) t;
            return new ImmutableFloatEncodedValue(f.getFloatValue());
        } else if (t instanceof StringConstantValueTag) {
            StringConstantValueTag s = (StringConstantValueTag) t;
            if (sf.getType().equals(RefType.v("java.lang.String"))) {
                return new ImmutableStringEncodedValue(s.getStringValue());
            }
            return null;
        } else {
            throw new DexPrinterException("Unexpected constant type");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v28, types: [soot.toDex.MultiDexBuilder] */
    /* JADX WARN: Type inference failed for: r0v29, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v32 */
    private void addAsClassDefItem(SootClass c) {
        SourceFileTag sft = (SourceFileTag) c.getTag(SourceFileTag.NAME);
        String sourceFile = sft == null ? null : sft.getSourceFile();
        String classType = SootToDexUtils.getDexTypeDescriptor(c.getType());
        int accessFlags = c.getModifiers();
        String superClass = c.hasSuperclass() ? SootToDexUtils.getDexTypeDescriptor(c.getSuperclass().getType()) : null;
        List<String> interfaces = null;
        if (!c.getInterfaces().isEmpty()) {
            interfaces = new ArrayList<>();
            for (SootClass ifc : c.getInterfaces()) {
                interfaces.add(SootToDexUtils.getDexTypeDescriptor(ifc.getType()));
            }
        }
        List<Field> fields = null;
        if (!c.getFields().isEmpty()) {
            fields = new ArrayList<>();
            for (SootField f : c.getFields()) {
                if (!isIgnored(f)) {
                    EncodedValue staticInit = null;
                    for (Tag t : f.getTags()) {
                        if (t instanceof ConstantValueTag) {
                            if (staticInit != null) {
                                LOGGER.warn("More than one constant tag for field \"{}\": \"{}\"", f, t);
                            } else {
                                staticInit = makeConstantItem(f, t);
                            }
                        }
                    }
                    if (staticInit == null) {
                        staticInit = BuilderEncodedValues.defaultValueForType(SootToDexUtils.getDexTypeDescriptor(f.getType()));
                    }
                    Set<Annotation> fieldAnnotations = buildFieldAnnotations(f);
                    ImmutableField field = new ImmutableField(classType, f.getName(), SootToDexUtils.getDexTypeDescriptor(f.getType()), f.getModifiers(), staticInit, fieldAnnotations, (Set<HiddenApiRestriction>) null);
                    fields.add(field);
                }
            }
        }
        Collection<Method> methods = toMethods(c);
        ClassDef classDef = new ImmutableClassDef(classType, accessFlags, superClass, interfaces, sourceFile, buildClassAnnotations(c), fields, methods);
        ?? r0 = this.dexBuilder;
        synchronized (r0) {
            this.dexBuilder.internClass(classDef);
            r0 = r0;
        }
    }

    private Set<Annotation> buildClassAnnotations(SootClass c) {
        InnerClassAttribute icTag;
        List<Annotation> innerClassItem;
        Set<String> skipList = new HashSet<>();
        Set<Annotation> annotations = buildCommonAnnotations(c, skipList);
        EnclosingMethodTag eMethTag = (EnclosingMethodTag) c.getTag(EnclosingMethodTag.NAME);
        if (eMethTag != null) {
            Annotation enclosingMethodItem = buildEnclosingMethodTag(eMethTag, skipList);
            if (enclosingMethodItem != null) {
                annotations.add(enclosingMethodItem);
            }
        } else if (c.hasOuterClass() && skipList.add("Ldalvik/annotation/EnclosingClass;")) {
            ImmutableAnnotationElement enclosingElement = new ImmutableAnnotationElement("value", (ImmutableEncodedValue) new ImmutableTypeEncodedValue(SootToDexUtils.getDexClassName(c.getOuterClass().getName())));
            annotations.add(new ImmutableAnnotation(2, "Ldalvik/annotation/EnclosingClass;", Collections.singleton(enclosingElement)));
        }
        if (c.hasOuterClass() && (icTag = (InnerClassAttribute) c.getOuterClass().getTag(InnerClassAttribute.NAME)) != null && (innerClassItem = buildInnerClassAttribute(c, icTag, skipList)) != null) {
            annotations.addAll(innerClassItem);
        }
        writeMemberClasses(c, skipList, annotations);
        for (Tag t : c.getTags()) {
            if (VisibilityAnnotationTag.NAME.equals(t.getName())) {
                annotations.addAll(buildVisibilityAnnotationTag((VisibilityAnnotationTag) t, skipList));
            }
        }
        List<AnnotationElem> defaults = new ArrayList<>();
        for (SootMethod method : c.getMethods()) {
            AnnotationDefaultTag tag = (AnnotationDefaultTag) method.getTag(AnnotationDefaultTag.NAME);
            if (tag != null) {
                tag.getDefaultVal().setName(method.getName());
                defaults.add(tag.getDefaultVal());
            }
        }
        if (!defaults.isEmpty()) {
            VisibilityAnnotationTag defaultAnnotationTag = new VisibilityAnnotationTag(1);
            AnnotationTag a = new AnnotationTag("Ldalvik/annotation/AnnotationDefault;");
            defaultAnnotationTag.addAnnotation(a);
            AnnotationTag at = new AnnotationTag(SootToDexUtils.getDexClassName(c.getName()));
            AnnotationAnnotationElem ae = new AnnotationAnnotationElem(at, '@', "value");
            a.addElem(ae);
            for (AnnotationElem aelem : defaults) {
                at.addElem(aelem);
            }
            annotations.addAll(buildVisibilityAnnotationTag(defaultAnnotationTag, skipList));
        }
        return annotations;
    }

    protected void writeMemberClasses(SootClass c, Set<String> skipList, Set<Annotation> annotations) {
        List<Annotation> memberClassesItem;
        InnerClassAttribute icTag = (InnerClassAttribute) c.getTag(InnerClassAttribute.NAME);
        if (icTag != null && (memberClassesItem = buildMemberClassesAttribute(c, icTag, skipList)) != null) {
            annotations.addAll(memberClassesItem);
        }
    }

    private Set<Annotation> buildFieldAnnotations(SootField f) {
        Set<String> skipList = new HashSet<>();
        Set<Annotation> annotations = buildCommonAnnotations(f, skipList);
        for (Tag t : f.getTags()) {
            if (VisibilityAnnotationTag.NAME.equals(t.getName())) {
                annotations.addAll(buildVisibilityAnnotationTag((VisibilityAnnotationTag) t, skipList));
            }
        }
        return annotations;
    }

    private Set<Annotation> buildMethodAnnotations(SootMethod m) {
        Set<String> skipList = new HashSet<>();
        Set<Annotation> annotations = buildCommonAnnotations(m, skipList);
        for (Tag t : m.getTags()) {
            if (VisibilityAnnotationTag.NAME.equals(t.getName())) {
                annotations.addAll(buildVisibilityAnnotationTag((VisibilityAnnotationTag) t, skipList));
            }
        }
        List<SootClass> exceptionList = m.getExceptionsUnsafe();
        if (exceptionList != null && !exceptionList.isEmpty()) {
            List<ImmutableEncodedValue> valueList = new ArrayList<>(exceptionList.size());
            for (SootClass exceptionClass : exceptionList) {
                valueList.add(new ImmutableTypeEncodedValue(DexType.toDalvikICAT(exceptionClass.getName()).replace(".", "/")));
            }
            ImmutableArrayEncodedValue valueValue = new ImmutableArrayEncodedValue(valueList);
            ImmutableAnnotationElement valueElement = new ImmutableAnnotationElement("value", (ImmutableEncodedValue) valueValue);
            Set<ImmutableAnnotationElement> elements = Collections.singleton(valueElement);
            ImmutableAnnotation ann = new ImmutableAnnotation(2, "Ldalvik/annotation/Throws;", elements);
            annotations.add(ann);
        }
        return annotations;
    }

    private Set<Annotation> buildMethodParameterAnnotations(SootMethod m, int paramIdx) {
        Set<String> skipList = null;
        Set<Annotation> annotations = null;
        for (Tag t : m.getTags()) {
            if (VisibilityParameterAnnotationTag.NAME.equals(t.getName())) {
                VisibilityParameterAnnotationTag vat = (VisibilityParameterAnnotationTag) t;
                if (skipList == null) {
                    skipList = new HashSet<>();
                    annotations = new HashSet<>();
                }
                annotations.addAll(buildVisibilityParameterAnnotationTag(vat, skipList, paramIdx));
            }
        }
        return annotations;
    }

    private Set<Annotation> buildCommonAnnotations(AbstractHost host, Set<String> skipList) {
        SignatureTag tag;
        Set<Annotation> annotations = new HashSet<>();
        if (host.hasTag(DeprecatedTag.NAME) && !skipList.contains("Ljava/lang/Deprecated;")) {
            ImmutableAnnotation ann = new ImmutableAnnotation(1, "Ljava/lang/Deprecated;", Collections.emptySet());
            annotations.add(ann);
            skipList.add("Ljava/lang/Deprecated;");
        }
        if (!skipList.contains("Ldalvik/annotation/Signature;") && (tag = (SignatureTag) host.getTag(SignatureTag.NAME)) != null) {
            List<String> splitSignature = SootToDexUtils.splitSignature(tag.getSignature());
            Set<ImmutableAnnotationElement> elements = null;
            if (splitSignature != null && splitSignature.size() > 0) {
                List<ImmutableEncodedValue> valueList = new ArrayList<>();
                for (String s : splitSignature) {
                    valueList.add(new ImmutableStringEncodedValue(s));
                }
                ImmutableArrayEncodedValue valueValue = new ImmutableArrayEncodedValue(valueList);
                ImmutableAnnotationElement valueElement = new ImmutableAnnotationElement("value", (ImmutableEncodedValue) valueValue);
                elements = Collections.singleton(valueElement);
            } else {
                LOGGER.info("Signature annotation without value detected");
            }
            annotations.add(new ImmutableAnnotation(2, "Ldalvik/annotation/Signature;", elements));
            skipList.add("Ldalvik/annotation/Signature;");
        }
        return annotations;
    }

    private List<ImmutableAnnotation> buildVisibilityAnnotationTag(VisibilityAnnotationTag t, Set<String> skipList) {
        if (t.getAnnotations() == null) {
            return Collections.emptyList();
        }
        List<ImmutableAnnotation> annotations = new ArrayList<>();
        Iterator<AnnotationTag> it = t.getAnnotations().iterator();
        while (it.hasNext()) {
            AnnotationTag at = it.next();
            String type = at.getType();
            if (skipList.add(type)) {
                List<AnnotationElement> elements = null;
                Collection<AnnotationElem> elems = at.getElems();
                if (!elems.isEmpty()) {
                    elements = new ArrayList<>();
                    Set<String> alreadyWritten = new HashSet<>();
                    for (AnnotationElem ae : elems) {
                        if (ae.getName() == null || ae.getName().isEmpty()) {
                            throw new DexPrinterException("Null or empty annotation name encountered");
                        }
                        if (!alreadyWritten.add(ae.getName())) {
                            throw new DexPrinterException("Duplicate annotation attribute: " + ae.getName());
                        }
                        EncodedValue value = buildEncodedValueForAnnotation(ae);
                        ImmutableAnnotationElement element = new ImmutableAnnotationElement(ae.getName(), value);
                        elements.add(element);
                    }
                }
                String typeName = SootToDexUtils.getDexClassName(at.getType());
                annotations.add(new ImmutableAnnotation(getVisibility(t.getVisibility()), typeName, elements));
            }
        }
        return annotations;
    }

    private List<ImmutableAnnotation> buildVisibilityParameterAnnotationTag(VisibilityParameterAnnotationTag t, Set<String> skipList, int paramIdx) {
        if (t.getVisibilityAnnotations() == null) {
            return Collections.emptyList();
        }
        int paramTagIdx = 0;
        List<ImmutableAnnotation> annotations = new ArrayList<>();
        Iterator<VisibilityAnnotationTag> it = t.getVisibilityAnnotations().iterator();
        while (it.hasNext()) {
            VisibilityAnnotationTag vat = it.next();
            if (paramTagIdx == paramIdx && vat != null && vat.getAnnotations() != null) {
                Iterator<AnnotationTag> it2 = vat.getAnnotations().iterator();
                while (it2.hasNext()) {
                    AnnotationTag at = it2.next();
                    String type = at.getType();
                    if (skipList.add(type)) {
                        List<AnnotationElement> elements = null;
                        Collection<AnnotationElem> elems = at.getElems();
                        if (!elems.isEmpty()) {
                            elements = new ArrayList<>();
                            Set<String> alreadyWritten = new HashSet<>();
                            for (AnnotationElem ae : elems) {
                                if (ae.getName() == null || ae.getName().isEmpty()) {
                                    throw new DexPrinterException("Null or empty annotation name encountered");
                                }
                                if (!alreadyWritten.add(ae.getName())) {
                                    throw new DexPrinterException("Duplicate annotation attribute: " + ae.getName());
                                }
                                EncodedValue value = buildEncodedValueForAnnotation(ae);
                                elements.add(new ImmutableAnnotationElement(ae.getName(), value));
                            }
                        }
                        ImmutableAnnotation ann = new ImmutableAnnotation(getVisibility(vat.getVisibility()), SootToDexUtils.getDexClassName(at.getType()), elements);
                        annotations.add(ann);
                    }
                }
                continue;
            }
            paramTagIdx++;
        }
        return annotations;
    }

    private Annotation buildEnclosingMethodTag(EnclosingMethodTag t, Set<String> skipList) {
        if (!skipList.add("Ldalvik/annotation/EnclosingMethod;") || t.getEnclosingMethod() == null) {
            return null;
        }
        String[] split1 = t.getEnclosingMethodSig().split("\\)");
        String parametersS = split1[0].replaceAll("\\(", "");
        String returnTypeS = split1[1];
        List<String> typeList = new ArrayList<>();
        if (!parametersS.isEmpty()) {
            for (String p : Util.splitParameters(parametersS)) {
                if (!p.isEmpty()) {
                    typeList.add(p);
                }
            }
        }
        ImmutableMethodReference mRef = new ImmutableMethodReference(SootToDexUtils.getDexClassName(t.getEnclosingClass()), t.getEnclosingMethod(), typeList, returnTypeS);
        ImmutableMethodEncodedValue methodRef = new ImmutableMethodEncodedValue(mRef);
        AnnotationElement methodElement = new ImmutableAnnotationElement("value", (ImmutableEncodedValue) methodRef);
        return new ImmutableAnnotation(2, "Ldalvik/annotation/EnclosingMethod;", Collections.singleton(methodElement));
    }

    private List<Annotation> buildInnerClassAttribute(SootClass parentClass, InnerClassAttribute t, Set<String> skipList) {
        ImmutableEncodedValue nameValue;
        if (t.getSpecs() == null) {
            return null;
        }
        List<Annotation> anns = null;
        for (Tag t2 : t.getSpecs()) {
            InnerClassTag icTag = (InnerClassTag) t2;
            String outerClass = DexInnerClassParser.getOuterClassNameFromTag(icTag);
            String innerClass = icTag.getInnerClass().replace('/', '.');
            if (parentClass.hasOuterClass() && innerClass.equals(parentClass.getName())) {
                if (parentClass.getName().equals(outerClass) && icTag.getOuterClass() == null) {
                    outerClass = null;
                }
                if (!parentClass.getName().equals(outerClass) && skipList.add("Ldalvik/annotation/InnerClass;")) {
                    List<AnnotationElement> elements = new ArrayList<>();
                    ImmutableAnnotationElement flagsElement = new ImmutableAnnotationElement("accessFlags", (ImmutableEncodedValue) new ImmutableIntEncodedValue(icTag.getAccessFlags()));
                    elements.add(flagsElement);
                    if (icTag.getShortName() != null && !icTag.getShortName().isEmpty()) {
                        nameValue = new ImmutableStringEncodedValue(icTag.getShortName());
                    } else {
                        nameValue = ImmutableNullEncodedValue.INSTANCE;
                    }
                    elements.add(new ImmutableAnnotationElement("name", nameValue));
                    if (anns == null) {
                        anns = new ArrayList<>();
                    }
                    anns.add(new ImmutableAnnotation(2, "Ldalvik/annotation/InnerClass;", elements));
                }
            }
        }
        return anns;
    }

    private List<Annotation> buildMemberClassesAttribute(SootClass parentClass, InnerClassAttribute t, Set<String> skipList) {
        List<Annotation> anns = null;
        Set<String> memberClasses = null;
        for (Tag t2 : t.getSpecs()) {
            InnerClassTag icTag = (InnerClassTag) t2;
            String outerClass = DexInnerClassParser.getOuterClassNameFromTag(icTag);
            if (icTag.getOuterClass() != null && parentClass.getName().equals(outerClass)) {
                if (memberClasses == null) {
                    memberClasses = new HashSet<>();
                }
                memberClasses.add(SootToDexUtils.getDexClassName(icTag.getInnerClass()));
            }
        }
        if (memberClasses != null && !memberClasses.isEmpty() && skipList.add("Ldalvik/annotation/MemberClasses;")) {
            List<EncodedValue> classes = new ArrayList<>();
            for (String memberClass : memberClasses) {
                classes.add(new ImmutableTypeEncodedValue(memberClass));
            }
            ImmutableArrayEncodedValue classesValue = new ImmutableArrayEncodedValue(classes);
            ImmutableAnnotationElement element = new ImmutableAnnotationElement("value", (ImmutableEncodedValue) classesValue);
            ImmutableAnnotation memberAnnotation = new ImmutableAnnotation(2, "Ldalvik/annotation/MemberClasses;", Collections.singletonList(element));
            if (0 == 0) {
                anns = new ArrayList<>();
            }
            anns.add(memberAnnotation);
        }
        return anns;
    }

    protected Collection<Method> toMethods(SootClass clazz) {
        if (clazz.getMethods().isEmpty()) {
            return null;
        }
        String classType = SootToDexUtils.getDexTypeDescriptor(clazz.getType());
        List<Method> methods = new ArrayList<>();
        for (SootMethod sm : clazz.getMethods()) {
            if (!isIgnored(sm)) {
                try {
                    MethodImplementation impl = toMethodImplementation(sm);
                    ParamNamesTag pnt = (ParamNamesTag) sm.getTag(ParamNamesTag.NAME);
                    List<String> parameterNames = pnt == null ? null : pnt.getNames();
                    int paramIdx = 0;
                    List<MethodParameter> parameters = null;
                    if (sm.getParameterCount() > 0) {
                        parameters = new ArrayList<>();
                        for (Type tp : sm.getParameterTypes()) {
                            String paramType = SootToDexUtils.getDexTypeDescriptor(tp);
                            parameters.add(new ImmutableMethodParameter(paramType, buildMethodParameterAnnotations(sm, paramIdx), (!sm.isConcrete() || parameterNames == null) ? null : parameterNames.get(paramIdx)));
                            paramIdx++;
                        }
                    }
                    String returnType = SootToDexUtils.getDexTypeDescriptor(sm.getReturnType());
                    ImmutableMethod meth = new ImmutableMethod(classType, sm.getName(), parameters, returnType, SootToDexUtils.getDexAccessFlags(sm), buildMethodAnnotations(sm), (Set<HiddenApiRestriction>) null, impl);
                    methods.add(meth);
                } catch (Exception e) {
                    throw new DexPrinterException("Error while processing method " + sm, e);
                }
            }
        }
        return methods;
    }

    protected boolean isIgnored(SootMethod sm) {
        return sm.isPhantom();
    }

    protected boolean isIgnored(SootField sf) {
        return sf.isPhantom();
    }

    protected MethodImplementation toMethodImplementation(SootMethod m) {
        if (m.isAbstract() || m.isNative()) {
            return null;
        }
        Body activeBody = m.retrieveActiveBody();
        String mName = m.getName();
        if (mName.isEmpty()) {
            throw new DexPrinterException("Invalid empty method name: " + m.getSignature());
        }
        if ((mName.indexOf(60) >= 0 || mName.indexOf(62) >= 0) && !"<init>".equals(mName) && !"<clinit>".equals(mName)) {
            throw new DexPrinterException("Invalid method name: " + m.getSignature());
        }
        EmptySwitchEliminator.v().transform(activeBody);
        SynchronizedMethodTransformer.v().transform(activeBody);
        FastDexTrapTightener.v().transform(activeBody);
        DexArrayInitDetector initDetector = new DexArrayInitDetector();
        initDetector.constructArrayInitializations(activeBody);
        initDetector.fixTraps(activeBody);
        TrapSplitter.v().transform(activeBody);
        int inWords = SootToDexUtils.getDexWords(m.getParameterTypes());
        if (!m.isStatic()) {
            inWords++;
        }
        Collection<Unit> units = activeBody.getUnits();
        StmtVisitor stmtV = buildStmtVisitor(m, initDetector);
        Chain<Trap> traps = activeBody.getTraps();
        Set<Unit> trapReferences = new HashSet<>(traps.size() * 3);
        for (Trap t : traps) {
            trapReferences.add(t.getBeginUnit());
            trapReferences.add(t.getEndUnit());
            trapReferences.add(t.getHandlerUnit());
        }
        toInstructions(units, stmtV, trapReferences);
        int registerCount = stmtV.getRegisterCount();
        if (inWords > registerCount) {
            registerCount = inWords;
        }
        MethodImplementationBuilder builder = new MethodImplementationBuilder(registerCount);
        LabelAssigner labelAssigner = new LabelAssigner(builder);
        List<BuilderInstruction> instructions = stmtV.getRealInsns(labelAssigner);
        Map<Local, Integer> seenRegisters = new HashMap<>();
        Map<org.jf.dexlib2.iface.instruction.Instruction, LocalRegisterAssignmentInformation> instructionRegisterMap = stmtV.getInstructionRegisterMap();
        if (Options.v().write_local_annotations()) {
            for (LocalRegisterAssignmentInformation assignment : stmtV.getParameterInstructionsList()) {
                if (!"this".equals(assignment.getLocal().getName())) {
                    addRegisterAssignmentDebugInfo(assignment, seenRegisters, builder);
                }
            }
        }
        fixLongJumps(instructions, labelAssigner, stmtV);
        for (BuilderInstruction ins : instructions) {
            Stmt origStmt = stmtV.getStmtForInstruction(ins);
            if (stmtV.getInstructionPayloadMap().containsKey(ins)) {
                builder.addLabel(labelAssigner.getLabelName(stmtV.getInstructionPayloadMap().get(ins)));
            }
            if (origStmt != null) {
                if (trapReferences.contains(origStmt)) {
                    labelAssigner.getOrCreateLabel(origStmt);
                }
                String labelName = labelAssigner.getLabelName(origStmt);
                if (labelName != null && !builder.getLabel(labelName).isPlaced()) {
                    builder.addLabel(labelName);
                }
                if (stmtV.getStmtForInstruction(ins) != null) {
                    writeTagsForStatement(builder, origStmt);
                }
            }
            builder.addInstruction(ins);
            LocalRegisterAssignmentInformation registerAssignmentTag = instructionRegisterMap.get(ins);
            if (registerAssignmentTag != null) {
                addRegisterAssignmentDebugInfo(registerAssignmentTag, seenRegisters, builder);
            }
        }
        for (Integer num : seenRegisters.values()) {
            int registersLeft = num.intValue();
            builder.addEndLocal(registersLeft);
        }
        toTries(activeBody.getTraps(), builder, labelAssigner);
        for (Label lbl : labelAssigner.getAllLabels()) {
            if (!lbl.isPlaced()) {
                throw new DexPrinterException("Label not placed: " + lbl);
            }
        }
        return builder.getMethodImplementation();
    }

    protected StmtVisitor buildStmtVisitor(SootMethod belongingMethod, DexArrayInitDetector arrayInitDetector) {
        return new StmtVisitor(belongingMethod, arrayInitDetector);
    }

    protected void writeTagsForStatement(MethodImplementationBuilder builder, Stmt stmt) {
        for (Tag t : stmt.getTags()) {
            if (t instanceof LineNumberTag) {
                LineNumberTag lnt = (LineNumberTag) t;
                builder.addLineNumber(lnt.getLineNumber());
            } else if (t instanceof SourceFileTag) {
                SourceFileTag sft = (SourceFileTag) t;
                builder.addSetSourceFile(new ImmutableStringReference(sft.getSourceFile()));
            }
        }
    }

    private void fixLongJumps(List<BuilderInstruction> instructions, LabelAssigner labelAssigner, StmtVisitor stmtV) {
        boolean hasChanged;
        Integer targetIndex;
        Label lbl;
        Map<org.jf.dexlib2.iface.instruction.Instruction, Integer> instructionsToIndex = new HashMap<>();
        List<Integer> instructionsToOffsets = new ArrayList<>();
        Map<Label, Integer> labelsToOffsets = new HashMap<>();
        Map<Label, Integer> labelsToIndex = new HashMap<>();
        do {
            hasChanged = false;
            instructionsToOffsets.clear();
            int offset = 0;
            int idx = 0;
            for (BuilderInstruction bi : instructions) {
                instructionsToIndex.put(bi, Integer.valueOf(idx));
                instructionsToOffsets.add(Integer.valueOf(offset));
                Stmt origStmt = stmtV.getStmtForInstruction(bi);
                if (origStmt != null && (lbl = labelAssigner.getLabelUnsafe(origStmt)) != null) {
                    labelsToOffsets.put(lbl, Integer.valueOf(offset));
                    labelsToIndex.put(lbl, Integer.valueOf(idx));
                }
                offset += bi.getFormat().size / 2;
                idx++;
            }
            int j = 0;
            while (true) {
                if (j >= instructions.size()) {
                    break;
                }
                BuilderInstruction bj = instructions.get(j);
                if (bj instanceof BuilderOffsetInstruction) {
                    BuilderOffsetInstruction boj = (BuilderOffsetInstruction) bj;
                    Insn jumpInsn = stmtV.getInsnForInstruction(boj);
                    if (jumpInsn instanceof InsnWithOffset) {
                        InsnWithOffset offsetInsn = (InsnWithOffset) jumpInsn;
                        Integer targetOffset = labelsToOffsets.get(boj.getTarget());
                        if (targetOffset != null) {
                            int distance = Math.abs(targetOffset.intValue() - instructionsToOffsets.get(j).intValue());
                            if (distance <= offsetInsn.getMaxJumpOffset() && (targetIndex = labelsToIndex.get(boj.getTarget())) != null) {
                                int start = Math.min(targetIndex.intValue(), j);
                                int end = Math.max(targetIndex.intValue(), j);
                                int theoreticalMaximumIncrease = (end - start) * 2;
                                if (distance + theoreticalMaximumIncrease > offsetInsn.getMaxJumpOffset()) {
                                    int countConstString = 0;
                                    for (int z = start; z <= end; z++) {
                                        if (instructions.get(z).getOpcode() == Opcode.CONST_STRING) {
                                            countConstString++;
                                        }
                                    }
                                    int maxOffsetChange = countConstString * 2;
                                    distance += maxOffsetChange;
                                }
                            }
                            if (distance > offsetInsn.getMaxJumpOffset()) {
                                insertIntermediateJump(labelsToIndex.get(boj.getTarget()).intValue(), j, stmtV, instructions, labelAssigner);
                                hasChanged = true;
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                j++;
            }
        } while (hasChanged);
    }

    private void insertIntermediateJump(int targetInsPos, int jumpInsPos, StmtVisitor stmtV, List<BuilderInstruction> instructions, LabelAssigner labelAssigner) {
        BuilderInstruction originalJumpInstruction = instructions.get(jumpInsPos);
        Insn originalJumpInsn = stmtV.getInsnForInstruction(originalJumpInstruction);
        if (originalJumpInsn == null) {
            return;
        }
        if (!(originalJumpInsn instanceof InsnWithOffset)) {
            throw new DexPrinterException("Unexpected jump instruction target");
        }
        InsnWithOffset offsetInsn = (InsnWithOffset) originalJumpInsn;
        if ((originalJumpInsn instanceof Insn10t) && originalJumpInsn.getOpcode() == Opcode.GOTO) {
            Insn30t newJump = new Insn30t(Opcode.GOTO_32);
            newJump.setTarget(((Insn10t) originalJumpInsn).getTarget());
            BuilderInstruction newJumpInstruction = newJump.getRealInsn(labelAssigner);
            instructions.remove(jumpInsPos);
            instructions.add(jumpInsPos, newJumpInstruction);
            stmtV.fakeNewInsn(stmtV.getStmtForInstruction(originalJumpInstruction), newJump, newJumpInstruction);
            return;
        }
        int distance = Math.max(targetInsPos, jumpInsPos) - Math.min(targetInsPos, jumpInsPos);
        if (distance == 0) {
            return;
        }
        int newJumpIdx = Math.min(targetInsPos, jumpInsPos) + (distance / 2);
        int sign = (int) Math.signum(targetInsPos - jumpInsPos);
        do {
            Stmt newStmt = stmtV.getStmtForInstruction(instructions.get(newJumpIdx));
            Stmt prevStmt = newJumpIdx > 0 ? stmtV.getStmtForInstruction(instructions.get(newJumpIdx - 1)) : null;
            if (newStmt == null || newStmt == prevStmt) {
                newJumpIdx -= sign;
                if (newJumpIdx < 0) {
                    break;
                }
            } else {
                NopStmt nop = Jimple.v().newNopStmt();
                Insn30t newJump2 = new Insn30t(Opcode.GOTO_32);
                newJump2.setTarget(stmtV.getStmtForInstruction(instructions.get(targetInsPos)));
                BuilderInstruction newJumpInstruction2 = newJump2.getRealInsn(labelAssigner);
                instructions.add(newJumpIdx, newJumpInstruction2);
                stmtV.fakeNewInsn(nop, newJump2, newJumpInstruction2);
                if (newJumpIdx <= jumpInsPos) {
                    jumpInsPos++;
                }
                if (newJumpIdx <= targetInsPos) {
                    int i = targetInsPos + 1;
                }
                offsetInsn.setTarget(nop);
                BuilderInstruction replacementJumpInstruction = offsetInsn.getRealInsn(labelAssigner);
                if (!$assertionsDisabled && instructions.get(jumpInsPos) != originalJumpInstruction) {
                    throw new AssertionError();
                }
                instructions.remove(jumpInsPos);
                instructions.add(jumpInsPos, replacementJumpInstruction);
                stmtV.fakeNewInsn(stmtV.getStmtForInstruction(originalJumpInstruction), originalJumpInsn, replacementJumpInstruction);
                Stmt afterNewJump = stmtV.getStmtForInstruction(instructions.get(newJumpIdx + 1));
                Insn10t jumpAround = new Insn10t(Opcode.GOTO);
                jumpAround.setTarget(afterNewJump);
                BuilderInstruction jumpAroundInstruction = jumpAround.getRealInsn(labelAssigner);
                instructions.add(newJumpIdx, jumpAroundInstruction);
                stmtV.fakeNewInsn(Jimple.v().newNopStmt(), jumpAround, jumpAroundInstruction);
                return;
            }
        } while (newJumpIdx < instructions.size());
        throw new DexPrinterException("No position for inserting intermediate jump instruction found");
    }

    private void addRegisterAssignmentDebugInfo(LocalRegisterAssignmentInformation registerAssignment, Map<Local, Integer> seenRegisters, MethodImplementationBuilder builder) {
        Local local = registerAssignment.getLocal();
        String dexLocalType = SootToDexUtils.getDexTypeDescriptor(local.getType());
        StringReference localName = new ImmutableStringReference(local.getName());
        Register reg = registerAssignment.getRegister();
        int register = reg.getNumber();
        Integer beforeRegister = seenRegisters.get(local);
        if (beforeRegister != null) {
            if (beforeRegister.intValue() == register) {
                return;
            }
            builder.addEndLocal(beforeRegister.intValue());
        }
        builder.addStartLocal(register, localName, new ImmutableTypeReference(dexLocalType), new ImmutableStringReference(""));
        seenRegisters.put(local, Integer.valueOf(register));
    }

    protected void toInstructions(Collection<Unit> units, StmtVisitor stmtV, Set<Unit> trapReferences) {
        Set<ClassConstant> monitorConsts = new HashSet<>();
        for (Unit u : units) {
            if (u instanceof MonitorStmt) {
                MonitorStmt monitorStmt = (MonitorStmt) u;
                if (monitorStmt.getOp() instanceof ClassConstant) {
                    monitorConsts.add((ClassConstant) monitorStmt.getOp());
                }
            }
        }
        boolean monitorAllocsMade = false;
        for (Unit u2 : units) {
            if (!monitorAllocsMade && !monitorConsts.isEmpty() && !(u2 instanceof IdentityStmt)) {
                stmtV.preAllocateMonitorConsts(monitorConsts);
                monitorAllocsMade = true;
            }
            stmtV.beginNewStmt((Stmt) u2);
            u2.apply(stmtV);
        }
        stmtV.finalizeInstructions(trapReferences);
    }

    protected void toTries(Collection<Trap> traps, MethodImplementationBuilder builder, LabelAssigner labelAssigner) {
        Map<CodeRange, List<ExceptionHandler>> codeRangesToTryItem = new LinkedHashMap<>();
        for (Trap t : traps) {
            Stmt beginStmt = (Stmt) t.getBeginUnit();
            Stmt endStmt = (Stmt) t.getEndUnit();
            int startCodeAddress = labelAssigner.getLabel(beginStmt).getCodeAddress();
            int endCodeAddress = labelAssigner.getLabel(endStmt).getCodeAddress();
            CodeRange range = new CodeRange(startCodeAddress, endCodeAddress);
            String exceptionType = SootToDexUtils.getDexTypeDescriptor(t.getException().getType());
            int codeAddress = labelAssigner.getLabel((Stmt) t.getHandlerUnit()).getCodeAddress();
            ImmutableExceptionHandler exceptionHandler = new ImmutableExceptionHandler(exceptionType, codeAddress);
            List<ExceptionHandler> newHandlers = new ArrayList<>();
            Iterator<CodeRange> it = codeRangesToTryItem.keySet().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                CodeRange r = it.next();
                if (r.containsRange(range)) {
                    range.startAddress = r.startAddress;
                    range.endAddress = r.endAddress;
                    List<ExceptionHandler> oldHandlers = codeRangesToTryItem.get(r);
                    if (oldHandlers != null) {
                        newHandlers.addAll(oldHandlers);
                    }
                } else if (range.containsRange(r)) {
                    range.startAddress = r.startAddress;
                    range.endAddress = r.endAddress;
                    List<ExceptionHandler> oldHandlers2 = codeRangesToTryItem.get(range);
                    if (oldHandlers2 != null) {
                        newHandlers.addAll(oldHandlers2);
                    }
                    codeRangesToTryItem.remove(r);
                }
            }
            if (!newHandlers.contains(exceptionHandler)) {
                newHandlers.add(exceptionHandler);
            }
            codeRangesToTryItem.put(range, newHandlers);
        }
        for (CodeRange r1 : codeRangesToTryItem.keySet()) {
            for (CodeRange r2 : codeRangesToTryItem.keySet()) {
                if (r1 != r2 && r1.overlaps(r2)) {
                    LOGGER.warn("Trap region overlaps detected");
                }
            }
        }
        for (CodeRange range2 : codeRangesToTryItem.keySet()) {
            boolean allCaughtForRange = false;
            for (ExceptionHandler handler : codeRangesToTryItem.get(range2)) {
                if (!allCaughtForRange) {
                    if ("Ljava/lang/Throwable;".equals(handler.getExceptionType())) {
                        allCaughtForRange = true;
                    }
                    builder.addCatch(new ImmutableTypeReference(handler.getExceptionType()), labelAssigner.getLabelAtAddress(range2.startAddress), labelAssigner.getLabelAtAddress(range2.endAddress), labelAssigner.getLabelAtAddress(handler.getHandlerCodeAddress()));
                }
            }
        }
    }

    public void add(SootClass c) {
        File sourceForClass;
        if (c.isPhantom()) {
            return;
        }
        addAsClassDefItem(c);
        Map<String, File> dexClassIndex = SourceLocator.v().dexClassIndex();
        if (dexClassIndex == null || (sourceForClass = dexClassIndex.get(c.getName())) == null || sourceForClass.getName().endsWith(".dex")) {
            return;
        }
        if (this.originalApk != null && !this.originalApk.equals(sourceForClass)) {
            throw new CompilationDeathException("multiple APKs as source of an application are not supported");
        }
        this.originalApk = sourceForClass;
    }

    public void print() {
        try {
            if (Options.v().output_jar() || (this.originalApk != null && Options.v().output_format() != 11)) {
                printZip();
                return;
            }
            String outputDir = SourceLocator.v().getOutputDir();
            LOGGER.info("Writing dex files to \"{}\" folder.", outputDir);
            this.dexBuilder.writeTo(outputDir);
        } catch (IOException e) {
            throw new CompilationDeathException("I/O exception while printing dex", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/toDex/DexPrinter$CodeRange.class */
    public static class CodeRange {
        int startAddress;
        int endAddress;

        public CodeRange(int startAddress, int endAddress) {
            this.startAddress = startAddress;
            this.endAddress = endAddress;
        }

        public boolean containsRange(CodeRange r) {
            return r.startAddress >= this.startAddress && r.endAddress <= this.endAddress;
        }

        public boolean overlaps(CodeRange r) {
            if (r.startAddress < this.startAddress || r.startAddress >= this.endAddress) {
                return r.startAddress <= this.startAddress && r.endAddress > this.startAddress;
            }
            return true;
        }

        public String toString() {
            return String.valueOf(this.startAddress) + HelpFormatter.DEFAULT_OPT_PREFIX + this.endAddress;
        }

        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (other == null || !(other instanceof CodeRange)) {
                return false;
            }
            CodeRange cr = (CodeRange) other;
            return this.startAddress == cr.startAddress && this.endAddress == cr.endAddress;
        }

        public int hashCode() {
            return (17 * this.startAddress) + (13 * this.endAddress);
        }
    }
}
