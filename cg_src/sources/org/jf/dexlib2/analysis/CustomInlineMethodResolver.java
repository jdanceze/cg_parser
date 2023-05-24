package org.jf.dexlib2.analysis;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.jf.dexlib2.HiddenApiRestriction;
import org.jf.dexlib2.iface.Annotation;
import org.jf.dexlib2.iface.ClassDef;
import org.jf.dexlib2.iface.Method;
import org.jf.dexlib2.iface.MethodImplementation;
import org.jf.dexlib2.iface.instruction.InlineIndexInstruction;
import org.jf.dexlib2.immutable.ImmutableMethod;
import org.jf.dexlib2.immutable.ImmutableMethodParameter;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
import org.jf.dexlib2.immutable.util.ParamUtil;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/CustomInlineMethodResolver.class */
public class CustomInlineMethodResolver extends InlineMethodResolver {
    @Nonnull
    private final ClassPath classPath;
    @Nonnull
    private final Method[] inlineMethods;
    private static final Pattern longMethodPattern;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !CustomInlineMethodResolver.class.desiredAssertionStatus();
        longMethodPattern = Pattern.compile("(L[^;]+;)->([^(]+)\\(([^)]*)\\)(.+)");
    }

    public CustomInlineMethodResolver(@Nonnull ClassPath classPath, @Nonnull String inlineTable) {
        this.classPath = classPath;
        StringReader reader = new StringReader(inlineTable);
        List<String> lines = new ArrayList<>();
        BufferedReader br = new BufferedReader(reader);
        try {
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                if (line.length() > 0) {
                    lines.add(line);
                }
            }
            this.inlineMethods = new Method[lines.size()];
            for (int i = 0; i < this.inlineMethods.length; i++) {
                this.inlineMethods[i] = parseAndResolveInlineMethod(lines.get(i));
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error while parsing inline table", ex);
        }
    }

    public CustomInlineMethodResolver(@Nonnull ClassPath classPath, @Nonnull File inlineTable) throws IOException {
        this(classPath, Files.toString(inlineTable, Charset.forName("UTF-8")));
    }

    @Override // org.jf.dexlib2.analysis.InlineMethodResolver
    @Nonnull
    public Method resolveExecuteInline(@Nonnull AnalyzedInstruction analyzedInstruction) {
        InlineIndexInstruction instruction = (InlineIndexInstruction) analyzedInstruction.instruction;
        int methodIndex = instruction.getInlineIndex();
        if (methodIndex < 0 || methodIndex >= this.inlineMethods.length) {
            throw new RuntimeException("Invalid method index: " + methodIndex);
        }
        return this.inlineMethods[methodIndex];
    }

    @Nonnull
    private Method parseAndResolveInlineMethod(@Nonnull String inlineMethod) {
        Matcher m = longMethodPattern.matcher(inlineMethod);
        if (!m.matches()) {
            if ($assertionsDisabled) {
                throw new RuntimeException("Invalid method descriptor: " + inlineMethod);
            }
            throw new AssertionError();
        }
        String className = m.group(1);
        String methodName = m.group(2);
        Iterable<ImmutableMethodParameter> methodParams = ParamUtil.parseParamString(m.group(3));
        String methodRet = m.group(4);
        ImmutableMethodReference methodRef = new ImmutableMethodReference(className, methodName, methodParams, methodRet);
        int accessFlags = 0;
        boolean resolved = false;
        TypeProto typeProto = this.classPath.getClass(className);
        if (typeProto instanceof ClassProto) {
            ClassDef classDef = ((ClassProto) typeProto).getClassDef();
            Iterator<? extends Method> it = classDef.getMethods().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Method method = it.next();
                if (method.equals(methodRef)) {
                    resolved = true;
                    accessFlags = method.getAccessFlags();
                    break;
                }
            }
        }
        if (!resolved) {
            throw new RuntimeException("Cannot resolve inline method: " + inlineMethod);
        }
        return new ImmutableMethod(className, methodName, methodParams, methodRet, accessFlags, (Set<? extends Annotation>) null, (Set<HiddenApiRestriction>) null, (MethodImplementation) null);
    }
}
