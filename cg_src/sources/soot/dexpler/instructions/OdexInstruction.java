package soot.dexpler.instructions;

import org.jf.dexlib2.analysis.ClassPath;
import org.jf.dexlib2.iface.DexFile;
import org.jf.dexlib2.iface.Method;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/OdexInstruction.class */
public interface OdexInstruction {
    void deOdex(DexFile dexFile, Method method, ClassPath classPath);
}
