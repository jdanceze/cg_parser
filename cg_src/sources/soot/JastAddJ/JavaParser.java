package soot.JastAddJ;

import beaver.Parser;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/JavaParser.class */
public interface JavaParser {
    CompilationUnit parse(InputStream inputStream, String str) throws IOException, Parser.Exception;
}
