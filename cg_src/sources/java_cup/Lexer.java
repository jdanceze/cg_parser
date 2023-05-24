package java_cup;

import android.app.DownloadManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Scanner;
import java_cup.runtime.Symbol;
import org.hamcrest.generator.qdox.parser.impl.Parser;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/Lexer.class */
public class Lexer implements sym, Scanner {
    public static final int YYEOF = -1;
    private static final int YY_BUFFERSIZE = 16384;
    public static final int CODESEG = 1;
    public static final int YYINITIAL = 0;
    private static final String yy_packed0 = "\u0001\u0003\u0001\u0004\u0002\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\u0003\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\r\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0003\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0017\u0001\u0018\u0001\u0019\u0002\b\u0001\u001a\u0002\b\u0001\u001b\u0001\u001c\u0001\u001d\u0001\b\u0001\u001e\u0001\u001f\u0004\b\u0001\u0003\u000f \u0001!\u001a ,��\u0001\u0005+��\u0001\"\u0001#*��\u0002\b\n��\u0004\b\u0003��\u0010\b\u0010��\u0001$,��\u0001%\u001d��\u0002\b\n��\u0001\b\u0001&\u0002\b\u0003��\u0001'\u000f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0003\b\u0001(\f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u000e\b\u0001)\u0001\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0005\b\u0001*\u0001\b\u0001+\b\b\u0010��\u0001, ��\u0002\b\n��\u0003\b\u0001-\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0004\b\u0001.\u0005\b\u0001/\u0005\b\u0007��\u0002\b\n��\u0002\b\u00010\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0002\b\u00011\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0003\b\u00012\u0003��\u0006\b\u00013\b\b\u00014\u0007��\u0002\b\n��\u0004\b\u0003��\u0005\b\u00015\n\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0003\b\u00016\f\b*��\u00017\u0001\"\u0001\u0004\u0001\u0005'\"\u0005#\u00018$#\u0010��\u00019,��\u0001:\u001c��\u0002\b\n��\u0002\b\u0001;\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0001\b\u0001<\u0001\b\u0001=\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0002\b\u0001>\r\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0006\b\u0001?\t\b\u0007��\u0002\b\n��\u0004\b\u0003��\t\b\u0001@\u0006\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0001A\u000f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0006\b\u0001B\t\b\u0007��\u0002\b\n��\u0001C\u0003\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0003\b\u0001D\f\b\u0007��\u0002\b\n��\u0001\b\u0001E\u0002\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\r\b\u0001F\u0002\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0001G\u000f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0001H\u000f\b\u0007��\u0002\b\n��\u0001I\u0003\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\n\b\u0001J\u0005\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0006\b\u0001K\t\b\u0001��\u0004#\u0001\u0005\u00018$#\u0014��\u0001L\u001b��\u0002\b\n��\u0003\b\u0001M\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\b\b\u0001N\u0007\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0001\b\u0001O\u000e\b\u0007��\u0002\b\n��\u0004\b\u0003��\f\b\u0001P\u0003\b\u0007��\u0002\b\n��\u0002\b\u0001Q\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0002\b\u0001R\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\b\b\u0001S\u0007\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0003\b\u0001T\f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0005\b\u0001U\n\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0006\b\u0001V\t\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0004\b\u0001W\u000b\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0006\b\u0001X\t\b\u0007��\u0002\b\n��\u0004\b\u0003��\n\b\u0001Y\u0005\b\u0007��\u0002\b\n��\u0001\b\u0001Z\u0002\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0002\b\u0001[\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0001\\\u0005\b\u0001]\t\b\u0007��\u0002\b\n��\u0004\b\u0003��\f\b\u0001^\u0003\b\u0016��\u0001_\u001a��\u0002\b\n��\u0002\b\u0001`\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0002\b\u0001a\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0001b\u000f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0006\b\u0001c\t\b\u0007��\u0002\b\n��\u0004\b\u0003��\n\b\u0001d\u0005\b\u0007��\u0002\b\n��\u0004\b\u0003��\b\b\u0001e\u0007\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0005\b\u0001f\n\b\u0007��\u0002\b\n��\u0001\b\u0001g\u0002\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0003\b\u0001h\f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0006\b\u0001i\t\b\u0007��\u0002\b\n��\u0001\b\u0001j\u0002\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\b\b\u0001k\u0007\b\u0007��\u0002\b\n��\u0002\b\u0001l\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\t\b\u0001m\u0006\b\u0007��\u0002\b\n��\u0001\b\u0001n\u0002\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0002\b\u0001o\r\b\u0007��\u0002\b\n��\u0004\b\u0003��\t\b\u0001p\u0006\b\u0007��\u0002\b\n��\u0004\b\u0003��\n\b\u0001q\u0005\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0006\b\u0001r\t\b\u0007��\u0002\b\n��\u0004\b\u0003��\n\b\u0001s\u0005\b\u0007��\u0002\b\n��\u0004\b\u0003��\b\b\u0001t\u0007\b\u0007��\u0002\b\n��\u0001\b\u0001u\u0002\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0002\b\u0001v\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0002\b\u0001w\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\b\b\u0001x\u0007\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0001y\u000f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0005\b\u0001z\n\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0004\b\u0001{\u000b\b\u0007��\u0002\b\n��\u0004\b\u0003��\n\b\u0001|\u0005\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0007\b\u0001}\b\b\u0007��\u0002\b\n��\u0003\b\u0001~\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0003\b\u0001\u007f\f\b\u0007��\u0002\b\n��\u0003\b\u0001\u0080\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\n\b\u0001\u0081\u0005\b\u0007��\u0002\b\n��\u0002\b\u0001\u0082\u0001\b\u0003��\u0010\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0001\u0083\u000f\b\u0007��\u0002\b\n��\u0004\b\u0003��\u0007\b\u0001\u0084\b\b\u0001��";
    private static final int YY_UNKNOWN_ERROR = 0;
    private static final int YY_ILLEGAL_STATE = 1;
    private static final int YY_NO_MATCH = 2;
    private static final int YY_PUSHBACK_2BIG = 3;
    private Reader yy_reader;
    private int yy_state;
    private int yy_lexical_state;
    private char[] yy_buffer;
    private int yy_markedPos;
    private int yy_pushbackPos;
    private int yy_currentPos;
    private int yy_startRead;
    private int yy_endRead;
    private int yyline;
    private int yychar;
    private int yycolumn;
    private boolean yy_atBOL;
    private boolean yy_atEOF;
    private boolean yy_eof_done;
    private StringBuffer sb;
    private ComplexSymbolFactory symbolFactory;
    private int csline;
    private int cscolumn;
    private static final String yycmap_packed = "\t\u0007\u0001\u0003\u0001\u0002\u0001��\u0001\u0003\u0001\u0001\u000e\u0007\u0004��\u0001\u0003\u0003��\u0001\u0006\u0001\u0011\u0004��\u0001\u0005\u0001��\u0001\n\u0001��\u0001\u000b\u0001\u0004\n\u0007\u0001\u000f\u0001\t\u0001\u0017\u0001\u0010\u0001\u0016\u0001\b\u0001��\u001a\u0006\u0001\r\u0001��\u0001\u000e\u0001��\u0001\u0006\u0001��\u0001\u0019\u0001\u0006\u0001\u0015\u0001\"\u0001\u0014\u0001&\u0001\u001b\u0001%\u0001\u001c\u0001\u0006\u0001\u001a\u0001 \u0001\u001d\u0001#\u0001\u001e\u0001\u0012\u0001\u0006\u0001\u0013\u0001!\u0001\u001f\u0001(\u0001\u0006\u0001$\u0001'\u0002\u0006\u0001\u0018\u0001\f\u0001)\u0001��!\u0007\u0002��\u0004\u0006\u0004��\u0001\u0006\u0002��\u0001\u0007\u0007��\u0001\u0006\u0004��\u0001\u0006\u0005��\u0017\u0006\u0001��\u001f\u0006\u0001��Ǌ\u0006\u0004��\f\u0006\u000e��\u0005\u0006\u0007��\u0001\u0006\u0001��\u0001\u0006\u0011��p\u0007\u0005\u0006\u0001��\u0002\u0006\u0002��\u0004\u0006\b��\u0001\u0006\u0001��\u0003\u0006\u0001��\u0001\u0006\u0001��\u0014\u0006\u0001��S\u0006\u0001��\u008b\u0006\u0001��\u0005\u0007\u0002��\u009e\u0006\t��&\u0006\u0002��\u0001\u0006\u0007��'\u0006\t��-\u0007\u0001��\u0001\u0007\u0001��\u0002\u0007\u0001��\u0002\u0007\u0001��\u0001\u0007\b��\u001b\u0006\u0005��\u0003\u0006\r��\u0004\u0007\u0007��\u0001\u0006\u0004��\u000b\u0007\u0005��+\u0006\u001f\u0007\u0004��\u0002\u0006\u0001\u0007c\u0006\u0001��\u0001\u0006\b\u0007\u0001��\u0006\u0007\u0002\u0006\u0002\u0007\u0001��\u0004\u0007\u0002\u0006\n\u0007\u0003\u0006\u0002��\u0001\u0006\u000f��\u0001\u0007\u0001\u0006\u0001\u0007\u001e\u0006\u001b\u0007\u0002��Y\u0006\u000b\u0007\u0001\u0006\u000e��\n\u0007!\u0006\t\u0007\u0002\u0006\u0004��\u0001\u0006\u0005��\u0016\u0006\u0004\u0007\u0001\u0006\t\u0007\u0001\u0006\u0003\u0007\u0001\u0006\u0005\u0007\u0012��\u0019\u0006\u0003\u0007¤��\u0004\u00076\u0006\u0003\u0007\u0001\u0006\u0012\u0007\u0001\u0006\u0007\u0007\n\u0006\u0002\u0007\u0002��\n\u0007\u0001��\u0007\u0006\u0001��\u0007\u0006\u0001��\u0003\u0007\u0001��\b\u0006\u0002��\u0002\u0006\u0002��\u0016\u0006\u0001��\u0007\u0006\u0001��\u0001\u0006\u0003��\u0004\u0006\u0002��\u0001\u0007\u0001\u0006\u0007\u0007\u0002��\u0002\u0007\u0002��\u0003\u0007\u0001\u0006\b��\u0001\u0007\u0004��\u0002\u0006\u0001��\u0003\u0006\u0002\u0007\u0002��\n\u0007\u0004\u0006\u0007��\u0001\u0006\u0005��\u0003\u0007\u0001��\u0006\u0006\u0004��\u0002\u0006\u0002��\u0016\u0006\u0001��\u0007\u0006\u0001��\u0002\u0006\u0001��\u0002\u0006\u0001��\u0002\u0006\u0002��\u0001\u0007\u0001��\u0005\u0007\u0004��\u0002\u0007\u0002��\u0003\u0007\u0003��\u0001\u0007\u0007��\u0004\u0006\u0001��\u0001\u0006\u0007��\f\u0007\u0003\u0006\u0001\u0007\u000b��\u0003\u0007\u0001��\t\u0006\u0001��\u0003\u0006\u0001��\u0016\u0006\u0001��\u0007\u0006\u0001��\u0002\u0006\u0001��\u0005\u0006\u0002��\u0001\u0007\u0001\u0006\b\u0007\u0001��\u0003\u0007\u0001��\u0003\u0007\u0002��\u0001\u0006\u000f��\u0002\u0006\u0002\u0007\u0002��\n\u0007\u0001��\u0001\u0006\u000f��\u0003\u0007\u0001��\b\u0006\u0002��\u0002\u0006\u0002��\u0016\u0006\u0001��\u0007\u0006\u0001��\u0002\u0006\u0001��\u0005\u0006\u0002��\u0001\u0007\u0001\u0006\u0007\u0007\u0002��\u0002\u0007\u0002��\u0003\u0007\b��\u0002\u0007\u0004��\u0002\u0006\u0001��\u0003\u0006\u0002\u0007\u0002��\n\u0007\u0001��\u0001\u0006\u0010��\u0001\u0007\u0001\u0006\u0001��\u0006\u0006\u0003��\u0003\u0006\u0001��\u0004\u0006\u0003��\u0002\u0006\u0001��\u0001\u0006\u0001��\u0002\u0006\u0003��\u0002\u0006\u0003��\u0003\u0006\u0003��\f\u0006\u0004��\u0005\u0007\u0003��\u0003\u0007\u0001��\u0004\u0007\u0002��\u0001\u0006\u0006��\u0001\u0007\u000e��\n\u0007\t��\u0001\u0006\u0007��\u0003\u0007\u0001��\b\u0006\u0001��\u0003\u0006\u0001��\u0017\u0006\u0001��\n\u0006\u0001��\u0005\u0006\u0003��\u0001\u0006\u0007\u0007\u0001��\u0003\u0007\u0001��\u0004\u0007\u0007��\u0002\u0007\u0001��\u0002\u0006\u0006��\u0002\u0006\u0002\u0007\u0002��\n\u0007\u0012��\u0002\u0007\u0001��\b\u0006\u0001��\u0003\u0006\u0001��\u0017\u0006\u0001��\n\u0006\u0001��\u0005\u0006\u0002��\u0001\u0007\u0001\u0006\u0007\u0007\u0001��\u0003\u0007\u0001��\u0004\u0007\u0007��\u0002\u0007\u0007��\u0001\u0006\u0001��\u0002\u0006\u0002\u0007\u0002��\n\u0007\u0001��\u0002\u0006\u000f��\u0002\u0007\u0001��\b\u0006\u0001��\u0003\u0006\u0001��)\u0006\u0002��\u0001\u0006\u0007\u0007\u0001��\u0003\u0007\u0001��\u0004\u0007\u0001\u0006\b��\u0001\u0007\b��\u0002\u0006\u0002\u0007\u0002��\n\u0007\n��\u0006\u0006\u0002��\u0002\u0007\u0001��\u0012\u0006\u0003��\u0018\u0006\u0001��\t\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0003��\u0001\u0007\u0004��\u0006\u0007\u0001��\u0001\u0007\u0001��\b\u0007\u0012��\u0002\u0007\r��0\u0006\u0001\u0007\u0002\u0006\u0007\u0007\u0004��\b\u0006\b\u0007\u0001��\n\u0007'��\u0002\u0006\u0001��\u0001\u0006\u0002��\u0002\u0006\u0001��\u0001\u0006\u0002��\u0001\u0006\u0006��\u0004\u0006\u0001��\u0007\u0006\u0001��\u0003\u0006\u0001��\u0001\u0006\u0001��\u0001\u0006\u0002��\u0002\u0006\u0001��\u0004\u0006\u0001\u0007\u0002\u0006\u0006\u0007\u0001��\u0002\u0007\u0001\u0006\u0002��\u0005\u0006\u0001��\u0001\u0006\u0001��\u0006\u0007\u0002��\n\u0007\u0002��\u0002\u0006\"��\u0001\u0006\u0017��\u0002\u0007\u0006��\n\u0007\u000b��\u0001\u0007\u0001��\u0001\u0007\u0001��\u0001\u0007\u0004��\u0002\u0007\b\u0006\u0001��$\u0006\u0004��\u0014\u0007\u0001��\u0002\u0007\u0005\u0006\u000b\u0007\u0001��$\u0007\t��\u0001\u00079��+\u0006\u0014\u0007\u0001\u0006\n\u0007\u0006��\u0006\u0006\u0004\u0007\u0004\u0006\u0003\u0007\u0001\u0006\u0003\u0007\u0002\u0006\u0007\u0007\u0003\u0006\u0004\u0007\r\u0006\f\u0007\u0001\u0006\u000f\u0007\u0002��&\u0006\n��+\u0006\u0001��\u0001\u0006\u0003��ŉ\u0006\u0001��\u0004\u0006\u0002��\u0007\u0006\u0001��\u0001\u0006\u0001��\u0004\u0006\u0002��)\u0006\u0001��\u0004\u0006\u0002��!\u0006\u0001��\u0004\u0006\u0002��\u0007\u0006\u0001��\u0001\u0006\u0001��\u0004\u0006\u0002��\u000f\u0006\u0001��9\u0006\u0001��\u0004\u0006\u0002��C\u0006\u0002��\u0003\u0007 ��\u0010\u0006\u0010��U\u0006\f��ɬ\u0006\u0002��\u0011\u0006\u0001��\u001a\u0006\u0005��K\u0006\u0003��\u0003\u0006\u000f��\r\u0006\u0001��\u0004\u0006\u0003\u0007\u000b��\u0012\u0006\u0003\u0007\u000b��\u0012\u0006\u0002\u0007\f��\r\u0006\u0001��\u0003\u0006\u0001��\u0002\u0007\f��4\u0006 \u0007\u0003��\u0001\u0006\u0003��\u0002\u0006\u0001\u0007\u0002��\n\u0007!��\u0003\u0007\u0002��\n\u0007\u0006��X\u0006\b��)\u0006\u0001\u0007\u0001\u0006\u0005��F\u0006\n��\u001d\u0006\u0003��\f\u0007\u0004��\f\u0007\n��\n\u0007\u001e\u0006\u0002��\u0005\u0006\u000b��,\u0006\u0004��\u0011\u0007\u0007\u0006\u0002\u0007\u0006��\n\u0007&��\u0017\u0006\u0005\u0007\u0004��5\u0006\n\u0007\u0001��\u001d\u0007\u0002��\u000b\u0007\u0006��\n\u0007\r��\u0001\u0006X��\u0005\u0007/\u0006\u0011\u0007\u0007\u0006\u0004��\n\u0007\u0011��\t\u0007\f��\u0003\u0007\u001e\u0006\n\u0007\u0003��\u0002\u0006\n\u0007\u0006��&\u0006\u000e\u0007\f��$\u0006\u0014\u0007\b��\n\u0007\u0003��\u0003\u0006\n\u0007$\u0006R��\u0003\u0007\u0001��\u0015\u0007\u0004\u0006\u0001\u0007\u0004\u0006\u0001\u0007\r��À\u0006'\u0007\u0015��\u0004\u0007Ė\u0006\u0002��\u0006\u0006\u0002��&\u0006\u0002��\u0006\u0006\u0002��\b\u0006\u0001��\u0001\u0006\u0001��\u0001\u0006\u0001��\u0001\u0006\u0001��\u001f\u0006\u0002��5\u0006\u0001��\u0007\u0006\u0001��\u0001\u0006\u0003��\u0003\u0006\u0001��\u0007\u0006\u0003��\u0004\u0006\u0002��\u0006\u0006\u0004��\r\u0006\u0005��\u0003\u0006\u0001��\u0007\u0006\u000e��\u0005\u0007\u001a��\u0005\u0007\u0010��\u0002\u0006\u0013��\u0001\u0006\u000b��\u0005\u0007\u0005��\u0006\u0007\u0001��\u0001\u0006\r��\u0001\u0006\u0010��\r\u0006\u0003��\u001a\u0006\u0016��\r\u0007\u0004��\u0001\u0007\u0003��\f\u0007\u0011��\u0001\u0006\u0004��\u0001\u0006\u0002��\n\u0006\u0001��\u0001\u0006\u0003��\u0005\u0006\u0006��\u0001\u0006\u0001��\u0001\u0006\u0001��\u0001\u0006\u0001��\u0004\u0006\u0001��\u000b\u0006\u0002��\u0004\u0006\u0005��\u0005\u0006\u0004��\u0001\u0006\u0011��)\u0006\u0a77��/\u0006\u0001��/\u0006\u0001��\u0085\u0006\u0006��\u0004\u0006\u0003\u0007\u000e��&\u0006\n��6\u0006\t��\u0001\u0006\u000f��\u0001\u0007\u0017\u0006\t��\u0007\u0006\u0001��\u0007\u0006\u0001��\u0007\u0006\u0001��\u0007\u0006\u0001��\u0007\u0006\u0001��\u0007\u0006\u0001��\u0007\u0006\u0001��\u0007\u0006\u0001�� \u0007/��\u0001\u0006Ǖ��\u0003\u0006\u0019��\t\u0006\u0006\u0007\u0001��\u0005\u0006\u0002��\u0005\u0006\u0004��V\u0006\u0002��\u0002\u0007\u0002��\u0003\u0006\u0001��Z\u0006\u0001��\u0004\u0006\u0005��)\u0006\u0003��^\u0006\u0011��\u001b\u00065��\u0010\u0006Ȁ��ᦶ\u0006J��凌\u00064��ҍ\u0006C��.\u0006\u0002��č\u0006\u0003��\u0010\u0006\n\u0007\u0002\u0006\u0014��/\u0006\u0001\u0007\f��\u0002\u0007\u0001��\u0019\u0006\b��P\u0006\u0002\u0007%��\t\u0006\u0002��g\u0006\u0002��\u0004\u0006\u0001��\u0002\u0006\u000e��\n\u0006P��\b\u0006\u0001\u0007\u0003\u0006\u0001\u0007\u0004\u0006\u0001\u0007\u0017\u0006\u0005\u0007\u0010��\u0001\u0006\u0007��4\u0006\f��\u0002\u00072\u0006\u0011\u0007\u000b��\n\u0007\u0006��\u0012\u0007\u0006\u0006\u0003��\u0001\u0006\u0004��\n\u0007\u001c\u0006\b\u0007\u0002��\u0017\u0006\r\u0007\f��\u001d\u0006\u0003��\u0004\u0007/\u0006\u000e\u0007\u000e��\u0001\u0006\n\u0007&��)\u0006\u000e\u0007\t��\u0003\u0006\u0001\u0007\b\u0006\u0002\u0007\u0002��\n\u0007\u0006��\u0017\u0006\u0003��\u0001\u0006\u0001\u0007\u0004��0\u0006\u0001\u0007\u0001\u0006\u0003\u0007\u0002\u0006\u0002\u0007\u0005\u0006\u0002\u0007\u0001\u0006\u0001\u0007\u0001\u0006\u0018��\u0003\u0006#��\u0006\u0006\u0002��\u0006\u0006\u0002��\u0006\u0006\t��\u0007\u0006\u0001��\u0007\u0006\u0091��#\u0006\b\u0007\u0001��\u0002\u0007\u0002��\n\u0007\u0006��⮤\u0006\f��\u0017\u0006\u0004��1\u0006℄��Į\u0006\u0002��>\u0006\u0002��j\u0006&��\u0007\u0006\f��\u0005\u0006\u0005��\u0001\u0006\u0001\u0007\n\u0006\u0001��\r\u0006\u0001��\u0005\u0006\u0001��\u0001\u0006\u0001��\u0002\u0006\u0001��\u0002\u0006\u0001��l\u0006!��ū\u0006\u0012��@\u0006\u0002��6\u0006(��\r\u0006\u0003��\u0010\u0007\u0010��\u0007\u0007\f��\u0002\u0006\u0018��\u0003\u0006\u0019��\u0001\u0006\u0006��\u0005\u0006\u0001��\u0087\u0006\u0002��\u0001\u0007\u0004��\u0001\u0006\u000b��\n\u0007\u0007��\u001a\u0006\u0004��\u0001\u0006\u0001��\u001a\u0006\u000b��Y\u0006\u0003��\u0006\u0006\u0002��\u0006\u0006\u0002��\u0006\u0006\u0002��\u0003\u0006\u0003��\u0002\u0006\u0003��\u0002\u0006\u0012��\u0003\u0007\u0004��";
    private static final char[] yycmap = yy_unpack_cmap(yycmap_packed);
    private static final int[] yy_rowMap = {0, 42, 84, 126, 84, 168, 84, 210, 84, 84, 84, 84, 84, 84, 84, 252, Parser.SQUARECLOSE, 336, 378, 420, 462, 84, 84, 504, 546, 588, 630, 672, 714, 756, 798, 84, 840, 882, 924, 966, DownloadManager.ERROR_CANNOT_RESUME, 1050, 1092, 1134, 1176, 1218, 1260, 84, 1302, 1344, 1386, 1428, 1470, 1512, 1554, 1596, 1638, 1680, 84, 1722, 84, 1764, 1806, 1848, 1890, 1932, 1974, 2016, 2058, 2100, 2142, 2184, 2226, 2268, 2310, 2352, 2394, 2436, 2478, 2520, 2562, 2604, 2646, 2688, 2730, 210, 2772, 2814, 2856, 210, 2898, 210, 210, 2940, 2982, 3024, 3066, 210, 84, 3108, 3150, 3192, 210, 3234, 210, 3276, 3318, 3360, 210, 210, 3402, 3444, 3486, 210, 3528, 3570, 210, 210, 3612, 3654, 3696, 3738, 210, 210, 3780, 3822, 3864, 3906, 210, 210, 3948, 3990, 4032, 210, 4074, 210};
    private static final int[] yytrans = yy_unpack();
    private static final String[] YY_ERROR_MSG = {"Unkown internal scanner error", "Internal error: unknown state", "Error: could not match input", "Error: pushback value was too large"};
    private static final byte[] YY_ATTRIBUTE = {0, 0, 9, 1, 9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 1, 1, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 0, 9, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public Lexer(ComplexSymbolFactory sf) {
        this(new InputStreamReader(System.in));
        this.symbolFactory = sf;
    }

    public Symbol symbol(String name, int code) {
        return this.symbolFactory.newSymbol(name, code, new ComplexSymbolFactory.Location(this.yyline + 1, (this.yycolumn + 1) - yylength()), new ComplexSymbolFactory.Location(this.yyline + 1, this.yycolumn + 1));
    }

    public Symbol symbol(String name, int code, String lexem) {
        return this.symbolFactory.newSymbol(name, code, new ComplexSymbolFactory.Location(this.yyline + 1, this.yycolumn + 1), new ComplexSymbolFactory.Location(this.yyline + 1, this.yycolumn + yylength()), lexem);
    }

    protected void emit_warning(String message) {
        ErrorManager.getManager().emit_warning("Scanner at " + (this.yyline + 1) + "(" + (this.yycolumn + 1) + "): " + message);
    }

    protected void emit_error(String message) {
        ErrorManager.getManager().emit_error("Scanner at " + (this.yyline + 1) + "(" + (this.yycolumn + 1) + "): " + message);
    }

    public Lexer(Reader in) {
        this.yy_lexical_state = 0;
        this.yy_buffer = new char[16384];
        this.yy_atBOL = true;
        this.yy_reader = in;
    }

    public Lexer(InputStream in) {
        this(new InputStreamReader(in));
    }

    private static int[] yy_unpack() {
        int[] trans = new int[4116];
        yy_unpack(yy_packed0, 0, trans);
        return trans;
    }

    private static int yy_unpack(String packed, int offset, int[] trans) {
        int i = 0;
        int j = offset;
        int l = packed.length();
        while (i < l) {
            int i2 = i;
            int i3 = i + 1;
            int count = packed.charAt(i2);
            i = i3 + 1;
            int value = packed.charAt(i3);
            int value2 = value - 1;
            do {
                int i4 = j;
                j++;
                trans[i4] = value2;
                count--;
            } while (count > 0);
        }
        return j;
    }

    private static char[] yy_unpack_cmap(String packed) {
        char[] map = new char[65536];
        int i = 0;
        int j = 0;
        while (i < 2200) {
            int i2 = i;
            int i3 = i + 1;
            int count = packed.charAt(i2);
            i = i3 + 1;
            char value = packed.charAt(i3);
            do {
                int i4 = j;
                j++;
                map[i4] = value;
                count--;
            } while (count > 0);
        }
        return map;
    }

    private boolean yy_refill() throws IOException {
        if (this.yy_startRead > 0) {
            System.arraycopy(this.yy_buffer, this.yy_startRead, this.yy_buffer, 0, this.yy_endRead - this.yy_startRead);
            this.yy_endRead -= this.yy_startRead;
            this.yy_currentPos -= this.yy_startRead;
            this.yy_markedPos -= this.yy_startRead;
            this.yy_pushbackPos -= this.yy_startRead;
            this.yy_startRead = 0;
        }
        if (this.yy_currentPos >= this.yy_buffer.length) {
            char[] newBuffer = new char[this.yy_currentPos * 2];
            System.arraycopy(this.yy_buffer, 0, newBuffer, 0, this.yy_buffer.length);
            this.yy_buffer = newBuffer;
        }
        int numRead = this.yy_reader.read(this.yy_buffer, this.yy_endRead, this.yy_buffer.length - this.yy_endRead);
        if (numRead < 0) {
            return true;
        }
        this.yy_endRead += numRead;
        return false;
    }

    public final void yyclose() throws IOException {
        this.yy_atEOF = true;
        this.yy_endRead = this.yy_startRead;
        if (this.yy_reader != null) {
            this.yy_reader.close();
        }
    }

    public final void yyreset(Reader reader) throws IOException {
        yyclose();
        this.yy_reader = reader;
        this.yy_atBOL = true;
        this.yy_atEOF = false;
        this.yy_startRead = 0;
        this.yy_endRead = 0;
        this.yy_pushbackPos = 0;
        this.yy_markedPos = 0;
        this.yy_currentPos = 0;
        this.yycolumn = 0;
        this.yychar = 0;
        this.yyline = 0;
        this.yy_lexical_state = 0;
    }

    public final int yystate() {
        return this.yy_lexical_state;
    }

    public final void yybegin(int newState) {
        this.yy_lexical_state = newState;
    }

    public final String yytext() {
        return new String(this.yy_buffer, this.yy_startRead, this.yy_markedPos - this.yy_startRead);
    }

    public final char yycharat(int pos) {
        return this.yy_buffer[this.yy_startRead + pos];
    }

    public final int yylength() {
        return this.yy_markedPos - this.yy_startRead;
    }

    private void yy_ScanError(int errorCode) {
        String message;
        try {
            message = YY_ERROR_MSG[errorCode];
        } catch (ArrayIndexOutOfBoundsException e) {
            message = YY_ERROR_MSG[0];
        }
        throw new Error(message);
    }

    private void yypushback(int number) {
        if (number > yylength()) {
            yy_ScanError(3);
        }
        this.yy_markedPos -= number;
    }

    private void yy_do_eof() throws IOException {
        if (!this.yy_eof_done) {
            this.yy_eof_done = true;
            yyclose();
        }
    }

    @Override // java_cup.runtime.Scanner
    public Symbol next_token() throws IOException {
        int yy_input;
        boolean yy_peek;
        int yy_endRead_l = this.yy_endRead;
        char[] yy_buffer_l = this.yy_buffer;
        char[] yycmap_l = yycmap;
        int[] yytrans_l = yytrans;
        int[] yy_rowMap_l = yy_rowMap;
        byte[] yy_attr_l = YY_ATTRIBUTE;
        while (true) {
            int yy_markedPos_l = this.yy_markedPos;
            boolean yy_r = false;
            for (int yy_currentPos_l = this.yy_startRead; yy_currentPos_l < yy_markedPos_l; yy_currentPos_l++) {
                switch (yy_buffer_l[yy_currentPos_l]) {
                    case '\n':
                        if (yy_r) {
                            yy_r = false;
                            break;
                        } else {
                            this.yyline++;
                            this.yycolumn = 0;
                            break;
                        }
                    case 11:
                    case '\f':
                    case 133:
                    case 8232:
                    case 8233:
                        this.yyline++;
                        this.yycolumn = 0;
                        yy_r = false;
                        break;
                    case '\r':
                        this.yyline++;
                        this.yycolumn = 0;
                        yy_r = true;
                        break;
                    default:
                        yy_r = false;
                        this.yycolumn++;
                        break;
                }
            }
            if (yy_r) {
                if (yy_markedPos_l < yy_endRead_l) {
                    yy_peek = yy_buffer_l[yy_markedPos_l] == '\n';
                } else if (this.yy_atEOF) {
                    yy_peek = false;
                } else {
                    boolean eof = yy_refill();
                    yy_markedPos_l = this.yy_markedPos;
                    yy_buffer_l = this.yy_buffer;
                    if (eof) {
                        yy_peek = false;
                    } else {
                        yy_peek = yy_buffer_l[yy_markedPos_l] == '\n';
                    }
                }
                if (yy_peek) {
                    this.yyline--;
                }
            }
            int yy_action = -1;
            int i = yy_markedPos_l;
            this.yy_startRead = i;
            this.yy_currentPos = i;
            int yy_currentPos_l2 = i;
            this.yy_state = this.yy_lexical_state;
            while (true) {
                if (yy_currentPos_l2 < yy_endRead_l) {
                    int i2 = yy_currentPos_l2;
                    yy_currentPos_l2++;
                    yy_input = yy_buffer_l[i2];
                } else if (this.yy_atEOF) {
                    yy_input = 65535;
                } else {
                    this.yy_currentPos = yy_currentPos_l2;
                    this.yy_markedPos = yy_markedPos_l;
                    boolean eof2 = yy_refill();
                    int yy_currentPos_l3 = this.yy_currentPos;
                    yy_markedPos_l = this.yy_markedPos;
                    yy_buffer_l = this.yy_buffer;
                    yy_endRead_l = this.yy_endRead;
                    if (eof2) {
                        yy_input = -1;
                    } else {
                        yy_currentPos_l2 = yy_currentPos_l3 + 1;
                        yy_input = yy_buffer_l[yy_currentPos_l3];
                    }
                }
                int yy_next = yytrans_l[yy_rowMap_l[this.yy_state] + yycmap_l[yy_input]];
                if (yy_next != -1) {
                    this.yy_state = yy_next;
                    byte b = yy_attr_l[this.yy_state];
                    if ((b & 1) == 1) {
                        yy_action = this.yy_state;
                        yy_markedPos_l = yy_currentPos_l2;
                        if ((b & 8) == 8) {
                        }
                    }
                }
            }
            this.yy_markedPos = yy_markedPos_l;
            switch (yy_action) {
                case 2:
                case 5:
                case 16:
                case 23:
                    emit_warning("Unrecognized character '" + yytext() + "' -- ignored");
                    break;
                case 3:
                case 4:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 139:
                case 140:
                case 141:
                case 142:
                case 143:
                case 144:
                case 145:
                case 146:
                case 147:
                case 148:
                case 149:
                case 150:
                case 151:
                case 152:
                case 153:
                case 154:
                case 155:
                case 156:
                case 157:
                case 158:
                case 159:
                case 160:
                case 161:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                case 167:
                case 168:
                case 169:
                case 170:
                    break;
                case 6:
                    return symbol("STAR", 15);
                case 7:
                case 17:
                case 18:
                case 19:
                case 20:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 50:
                case 51:
                case 52:
                case 53:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 74:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 82:
                case 83:
                case 84:
                case 86:
                case 89:
                case 90:
                case 91:
                case 92:
                case 95:
                case 96:
                case 97:
                case 99:
                case 101:
                case 102:
                case 103:
                case 106:
                case 107:
                case 108:
                case 110:
                case 111:
                case 114:
                case 115:
                case 116:
                case 117:
                case 120:
                case 121:
                case 122:
                case 123:
                case 126:
                case 127:
                case 128:
                case 130:
                    return symbol("ID", 34, yytext());
                case 8:
                    return symbol("QESTION", 30);
                case 9:
                    return symbol("SEMI", 13);
                case 10:
                    return symbol("COMMA", 14);
                case 11:
                    return symbol("DOT", 16);
                case 12:
                    return symbol("BAR", 19);
                case 13:
                    return symbol("LBRACK", 25);
                case 14:
                    return symbol("RBRACK", 26);
                case 15:
                    return symbol("COLON", 17);
                case 21:
                    return symbol("GT", 28);
                case 22:
                    return symbol("LT", 29);
                case 31:
                case 32:
                    this.sb.append(yytext());
                    break;
                case 33:
                case 34:
                case 35:
                case 36:
                case 55:
                case 57:
                case 75:
                case 132:
                default:
                    if (yy_input == -1 && this.yy_startRead == this.yy_currentPos) {
                        this.yy_atEOF = true;
                        yy_do_eof();
                        return this.symbolFactory.newSymbol("EOF", 0);
                    }
                    yy_ScanError(2);
                    break;
                case 43:
                    this.sb = new StringBuffer();
                    this.csline = this.yyline + 1;
                    this.cscolumn = this.yycolumn + 1;
                    yybegin(1);
                    break;
                case 54:
                    yybegin(0);
                    return this.symbolFactory.newSymbol("CODE_STRING", 35, new ComplexSymbolFactory.Location(this.csline, this.cscolumn), new ComplexSymbolFactory.Location(this.yyline + 1, this.yycolumn + 1 + yylength()), this.sb.toString());
                case 56:
                    return symbol("COLON_COLON_EQUALS", 18);
                case 73:
                    return symbol("NON", 8);
                case 81:
                    return symbol("CODE", 4);
                case 85:
                    return symbol("INIT", 9);
                case 87:
                    return symbol("LEFT", 21);
                case 88:
                    return symbol("SCAN", 10);
                case 93:
                    return symbol("WITH", 11);
                case 94:
                    return symbol("PERCENT_PREC", 24);
                case 98:
                    return symbol("RIGHT", 22);
                case 100:
                    return symbol("CLASS", 33);
                case 104:
                    return symbol("START", 12);
                case 105:
                    return symbol("SUPER", 31);
                case 109:
                    return symbol("PARSER", 6);
                case 112:
                    return symbol("ACTION", 5);
                case 113:
                    return symbol("IMPORT", 3);
                case 118:
                    return symbol("PACKAGE", 2);
                case 119:
                    return symbol("EXTENDS", 32);
                case 124:
                    return symbol("PARSER", 7);
                case 125:
                    return symbol("NONASSOC", 23);
                case 129:
                    return symbol("PRECEDENCE", 20);
                case 131:
                    return symbol("NONTERMINAL", 27);
            }
        }
    }
}
