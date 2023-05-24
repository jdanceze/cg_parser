package soot.JastAddJ;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/BytecodeReader.class */
public interface BytecodeReader {
    CompilationUnit read(InputStream inputStream, String str, Program program) throws FileNotFoundException, IOException;
}
