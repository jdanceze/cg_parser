package polyglot.ext.jl.parse;

import android.bluetooth.BluetoothClass;
import android.view.WindowManager;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigInteger;
import java.util.HashMap;
import javassist.compiler.TokenId;
import org.apache.http.HttpStatus;
import polyglot.lex.BooleanLiteral;
import polyglot.lex.CharacterLiteral;
import polyglot.lex.DoubleLiteral;
import polyglot.lex.EOF;
import polyglot.lex.EscapedUnicodeReader;
import polyglot.lex.FloatLiteral;
import polyglot.lex.Identifier;
import polyglot.lex.IntegerLiteral;
import polyglot.lex.Keyword;
import polyglot.lex.Lexer;
import polyglot.lex.LongLiteral;
import polyglot.lex.NullLiteral;
import polyglot.lex.Operator;
import polyglot.lex.StringLiteral;
import polyglot.lex.Token;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/parse/Lexer_c.class */
public class Lexer_c implements Lexer {
    public static final int YYEOF = -1;
    private static final int YY_BUFFERSIZE = 16384;
    public static final int END_OF_LINE_COMMENT = 4;
    public static final int STRING = 1;
    public static final int YYINITIAL = 0;
    public static final int CHARACTER = 2;
    public static final int TRADITIONAL_COMMENT = 3;
    private static final String yy_packed0 = "\u0001\u0006\u0001\u0007\u0001\b\u0001\u0007\u0001\t\u0001\u0006\u0001\n\u0001\u000b\u0002\t\u0001\u000b\u0001\f\u0001\t\u0001\r\u0001\u0006\u0001\u000b\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0003\t\u0001\u0013\u0003\t\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0017\u0001\u0018\u0001\u0019\u0001\u001a\u0001\u001b\u0001\u001c\u0001\u001d\u0001\u001e\u0001\u001f\u0001 \u0001!\u0001\"\u0001#\u0001$\u0001%\u0001&\u0001'\u0001(\u0004\t\u0001)\u0001*\u0001+\u000b)\u0001,\u0004)\u0001-!)\u0001.\u0001/\u00010\u000b.\u00011\u0003.\u00012\".\u0011\u0007\u00013$\u0007\u00014\u000152\u00076��\u0001\u00077��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\t\t\u0014��\u0004\t\u0006��\u00016\u00017\u00018\u0001��\u00016\u00019\u0001:\u0002��\u00016\u0007��\u0001:\u0001;\u0001��\u0001<\u0016��\u0001<\u0001;\u0001=\u0007��\u0002\u000b\u0002��\u0001\u000b\u00019\u0001:\u0002��\u0001\u000b\u0007��\u0001:\u0001;\u0001��\u0001<\u0016��\u0001<\u0001;\u0001=\u0007��\u00029\u0002��\u00019\u0004��\u000192��\u0001>\u0017��\u0001?\u001f��\u0001@\u0001A\u0013��\u0001B4��\u0001C\u0013��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0001\t\u0001D\u0007\t\u0014��\u0004\t\u0004��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0005\t\u0001E\u0003\t\u0014��\u0004\t\u0004��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0002\t\u0001F\u0006\t\u0014��\u0004\t%��\u0001G4��\u0001H\u0001I3��\u0001J\u0001��\u0001K2��\u0001L4��\u0001M\u0006��\u0001N-��\u0001O\u0007��\u0001P,��\u0001Q\b��\u0001R+��\u0001S4��\u0001T\u000f��\u0001)\u0002��\u000b)\u0001��\u0004)\u0001��!)\u0001��\u0001*3��\u0001U\u0001��\u0004U\u0001V\u0003U\u0001W\u0003U\u0001X\u0001V\u0002U\u0001Y\u0001Z\u0001[\u0001\\\u0002U\u0001]\u0003U\u0001^\u0017U\u0001_\u0001.\u0002��\u000b.\u0001��\u0003.\u0001��\".\u0001��\u0001/3��\u0001U\u0001��\u0004U\u0001V\u0003U\u0001W\u0003U\u0001`\u0001V\u0002U\u0001a\u0001b\u0001c\u0001d\u0002U\u0001e\u0003U\u0001f\u0017U\u0001g\u0010��\u00014%��\u000149��\u00016\u00017\u0002��\u00016\u00019\u0001:\u0002��\u00016\u0007��\u0001:\u0002��\u0001h\u0016��\u0001h\t��\u00027\u0002��\u00017\u00019\u0001:\u0002��\u00017\u0007��\u0001:#��\u0002i\u0001��\u0002i\u0001��\u0001i\u0002��\u0001i\u0007��\u0003i\u0018��\u0003i\u0006��\u00029\u0002��\u00019\u0001��\u0001:\u0002��\u00019\u0007��\u0001:\u0001;\u0019��\u0001;\u0001=\u0007��\u0002j\u0002��\u0001j\u0002��\u0001k\u0001��\u0001j\u001e��\u0001k\n��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0002\t\u0001l\u0006\t\u0014��\u0004\t\u0004��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0006\t\u0001m\u0002\t\u0014��\u0004\t\u0004��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0006\t\u0001n\u0002\t\u0014��\u0004\t%��\u0001o\u0001p3��\u0001q\u0015��\u0001W\u0003��\u0001W\u0004��\u0001W+��\u0001r\u0003��\u0001r\u0004��\u0001r+��\u0002i\u0001��\u0002i\u0001��\u0001i\u0002��\u0001i\u0007��\u0003i\u0001s\u0016��\u0001s\u0003i\u0006��\u0002j\u0002��\u0001j\u0004��\u0001j\b��\u0001;\u0019��\u0001;\u0001=\u0007��\u0002j\u0002��\u0001j\u0004��\u0001j)��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0003\t\u0001t\u0005\t\u0014��\u0004\t\u0004��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0007\t\u0001u\u0001\t\u0014��\u0004\t\u0004��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0006\t\u0001v\u0002\t\u0014��\u0004\t%��\u0001w\u0013��\u0007\t\u0001��\u0001\t\u0002��\u0001\t\u0004��\u0003\t\u0001x\u0005\t\u0014��\u0004\t";
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
    StringBuffer sb;
    String file;
    ErrorQueue eq;
    HashMap keywords;
    private static final String yycmap_packed = "\t\u0005\u0001\u0003\u0001\u0001\u0001��\u0001\u0003\u0001\u0002\u000e\u0005\u0004��\u0001\u0003\u0001(\u0001\u0013\u0001��\u0001\u0004\u00010\u0001,\u0001\u0012\u0001\u001d\u0001\u001e\u0001\u0011\u0001.\u0001$\u0001\r\u0001\u000b\u0001\u0010\u0001\u0006\u0003\u000f\u0004\n\u0002\u0007\u0001+\u0001#\u0001'\u0001%\u0001&\u0001*\u0001��\u0003\t\u00013\u0001\f\u00012\u0005\u0004\u00011\u000b\u0004\u0001\b\u0002\u0004\u0001!\u0001\u000e\u0001\"\u0001/\u0001\u0004\u0001��\u0001\u0019\u00014\u0001\t\u00013\u0001\u0017\u0001\u0018\u0005\u0004\u0001\u001a\u0001\u0004\u0001\u001c\u0003\u0004\u0001\u0015\u0001\u001b\u0001\u0014\u0001\u0016\u0002\u0004\u0001\b\u0002\u0004\u0001\u001f\u0001-\u0001 \u0001)!\u0005\u0002��\u0004\u0004\u0004��\u0001\u0004\n��\u0001\u0004\u0004��\u0001\u0004\u0005��\u0017\u0004\u0001��\u001f\u0004\u0001��Ĩ\u0004\u0002��\u0012\u0004\u001c��^\u0004\u0002��\t\u0004\u0002��\u0007\u0004\u000e��\u0002\u0004\u000e��\u0005\u0004\t��\u0001\u0004\u0011��O\u0005\u0011��\u0003\u0005\u0017��\u0001\u0004\u000b��\u0001\u0004\u0001��\u0003\u0004\u0001��\u0001\u0004\u0001��\u0014\u0004\u0001��,\u0004\u0001��\b\u0004\u0002��\u001a\u0004\f��\u0082\u0004\u0001��\u0004\u0005\u0005��9\u0004\u0002��\u0002\u0004\u0002��\u0002\u0004\u0003��&\u0004\u0002��\u0002\u00047��&\u0004\u0002��\u0001\u0004\u0007��'\u0004\t��\u0011\u0005\u0001��\u0017\u0005\u0001��\u0003\u0005\u0001��\u0001\u0005\u0001��\u0002\u0005\u0001��\u0001\u0005\u000b��\u001b\u0004\u0005��\u0003\u0004.��\u001a\u0004\u0005��\u000b\u0004\u000b\u0005\n��\n\u0005\u0006��\u0001\u0005c\u0004\u0001��\u0001\u0004\u0007\u0005\u0002��\u0006\u0005\u0002\u0004\u0002\u0005\u0001��\u0004\u0005\u0002��\n\u0005\u0003\u0004\u0012��\u0001\u0005\u0001\u0004\u0001\u0005\u001b\u0004\u0003��\u001b\u00055��&\u0004\u000b\u0005Ő��\u0003\u0005\u0001��5\u0004\u0002��\u0001\u0005\u0001\u0004\u0010\u0005\u0002��\u0001\u0004\u0004\u0005\u0003��\n\u0004\u0002\u0005\u0002��\n\u0005\u0011��\u0003\u0005\u0001��\b\u0004\u0002��\u0002\u0004\u0002��\u0016\u0004\u0001��\u0007\u0004\u0001��\u0001\u0004\u0003��\u0004\u0004\u0002��\u0001\u0005\u0001��\u0007\u0005\u0002��\u0002\u0005\u0002��\u0003\u0005\t��\u0001\u0005\u0004��\u0002\u0004\u0001��\u0003\u0004\u0002\u0005\u0002��\n\u0005\u0004\u0004\u000e��\u0001\u0005\u0002��\u0006\u0004\u0004��\u0002\u0004\u0002��\u0016\u0004\u0001��\u0007\u0004\u0001��\u0002\u0004\u0001��\u0002\u0004\u0001��\u0002\u0004\u0002��\u0001\u0005\u0001��\u0005\u0005\u0004��\u0002\u0005\u0002��\u0003\u0005\u000b��\u0004\u0004\u0001��\u0001\u0004\u0007��\f\u0005\u0003\u0004\f��\u0003\u0005\u0001��\u0007\u0004\u0001��\u0001\u0004\u0001��\u0003\u0004\u0001��\u0016\u0004\u0001��\u0007\u0004\u0001��\u0002\u0004\u0001��\u0005\u0004\u0002��\u0001\u0005\u0001\u0004\b\u0005\u0001��\u0003\u0005\u0001��\u0003\u0005\u0002��\u0001\u0004\u000f��\u0001\u0004\u0005��\n\u0005\u0011��\u0003\u0005\u0001��\b\u0004\u0002��\u0002\u0004\u0002��\u0016\u0004\u0001��\u0007\u0004\u0001��\u0002\u0004\u0002��\u0004\u0004\u0002��\u0001\u0005\u0001\u0004\u0006\u0005\u0003��\u0002\u0005\u0002��\u0003\u0005\b��\u0002\u0005\u0004��\u0002\u0004\u0001��\u0003\u0004\u0004��\n\u0005\u0012��\u0002\u0005\u0001��\u0006\u0004\u0003��\u0003\u0004\u0001��\u0004\u0004\u0003��\u0002\u0004\u0001��\u0001\u0004\u0001��\u0002\u0004\u0003��\u0002\u0004\u0003��\u0003\u0004\u0003��\b\u0004\u0001��\u0003\u0004\u0004��\u0005\u0005\u0003��\u0003\u0005\u0001��\u0004\u0005\t��\u0001\u0005\u000f��\t\u0005\u0011��\u0003\u0005\u0001��\b\u0004\u0001��\u0003\u0004\u0001��\u0017\u0004\u0001��\n\u0004\u0001��\u0005\u0004\u0004��\u0007\u0005\u0001��\u0003\u0005\u0001��\u0004\u0005\u0007��\u0002\u0005\t��\u0002\u0004\u0004��\n\u0005\u0012��\u0002\u0005\u0001��\b\u0004\u0001��\u0003\u0004\u0001��\u0017\u0004\u0001��\n\u0004\u0001��\u0005\u0004\u0004��\u0007\u0005\u0001��\u0003\u0005\u0001��\u0004\u0005\u0007��\u0002\u0005\u0007��\u0001\u0004\u0001��\u0002\u0004\u0004��\n\u0005\u0012��\u0002\u0005\u0001��\b\u0004\u0001��\u0003\u0004\u0001��\u0017\u0004\u0001��\u0010\u0004\u0004��\u0006\u0005\u0002��\u0003\u0005\u0001��\u0004\u0005\t��\u0001\u0005\b��\u0002\u0004\u0004��\n\u0005\u0012��\u0002\u0005\u0001��\u0012\u0004\u0003��\u0018\u0004\u0001��\t\u0004\u0001��\u0001\u0004\u0002��\u0007\u0004\u0003��\u0001\u0005\u0004��\u0006\u0005\u0001��\u0001\u0005\u0001��\b\u0005\u0012��\u0002\u0005\r��0\u0004\u0001\u0005\u0002\u0004\u0007\u0005\u0004��\b\u0004\b\u0005\u0001��\n\u0005'��\u0002\u0004\u0001��\u0001\u0004\u0002��\u0002\u0004\u0001��\u0001\u0004\u0002��\u0001\u0004\u0006��\u0004\u0004\u0001��\u0007\u0004\u0001��\u0003\u0004\u0001��\u0001\u0004\u0001��\u0001\u0004\u0002��\u0002\u0004\u0001��\u0004\u0004\u0001\u0005\u0002\u0004\u0006\u0005\u0001��\u0002\u0005\u0001\u0004\u0002��\u0005\u0004\u0001��\u0001\u0004\u0001��\u0006\u0005\u0002��\n\u0005\u0002��\u0002\u0004\"��\u0001\u0004\u0017��\u0002\u0005\u0006��\n\u0005\u000b��\u0001\u0005\u0001��\u0001\u0005\u0001��\u0001\u0005\u0004��\u0002\u0005\b\u0004\u0001��\"\u0004\u0006��\u0014\u0005\u0001��\u0002\u0005\u0004\u0004\u0004��\b\u0005\u0001��$\u0005\t��\u0001\u00059��\"\u0004\u0001��\u0005\u0004\u0001��\u0002\u0004\u0001��\u0007\u0005\u0003��\u0004\u0005\u0006��\n\u0005\u0006��\u0006\u0004\u0004\u0005F��&\u0004\n��'\u0004\t��Z\u0004\u0005��D\u0004\u0005��R\u0004\u0006��\u0007\u0004\u0001��?\u0004\u0001��\u0001\u0004\u0001��\u0004\u0004\u0002��\u0007\u0004\u0001��\u0001\u0004\u0001��\u0004\u0004\u0002��'\u0004\u0001��\u0001\u0004\u0001��\u0004\u0004\u0002��\u001f\u0004\u0001��\u0001\u0004\u0001��\u0004\u0004\u0002��\u0007\u0004\u0001��\u0001\u0004\u0001��\u0004\u0004\u0002��\u0007\u0004\u0001��\u0007\u0004\u0001��\u0017\u0004\u0001��\u001f\u0004\u0001��\u0001\u0004\u0001��\u0004\u0004\u0002��\u0007\u0004\u0001��'\u0004\u0001��\u0013\u0004\u000e��\t\u0005.��U\u0004\f��ɬ\u0004\u0002��\b\u0004\n��\u001a\u0004\u0005��K\u0004\u0095��4\u0004 \u0005\u0007��\u0001\u0004\u0004��\n\u0005!��\u0004\u0005\u0001��\n\u0005\u0006��X\u0004\b��)\u0004\u0001\u0005Ֆ��\u009c\u0004\u0004��Z\u0004\u0006��\u0016\u0004\u0002��\u0006\u0004\u0002��&\u0004\u0002��\u0006\u0004\u0002��\b\u0004\u0001��\u0001\u0004\u0001��\u0001\u0004\u0001��\u0001\u0004\u0001��\u001f\u0004\u0002��5\u0004\u0001��\u0007\u0004\u0001��\u0001\u0004\u0003��\u0003\u0004\u0001��\u0007\u0004\u0003��\u0004\u0004\u0002��\u0006\u0004\u0004��\r\u0004\u0005��\u0003\u0004\u0001��\u0007\u0004\u000f��\u0004\u0005\u001a��\u0005\u0005\u0010��\u0002\u0004)��\u0006\u0005\u000f��\u0001\u0004 ��\u0010\u0004 ��\r\u0005\u0004��\u0001\u0005 ��\u0001\u0004\u0004��\u0001\u0004\u0002��\n\u0004\u0001��\u0001\u0004\u0003��\u0005\u0004\u0006��\u0001\u0004\u0001��\u0001\u0004\u0001��\u0001\u0004\u0001��\u0004\u0004\u0001��\u0003\u0004\u0001��\u0007\u0004&��$\u0004ກ��\u0003\u0004\u0019��\t\u0004\u0006\u0005\u0001��\u0005\u0004\u0002��\u0003\u0004\u0006��T\u0004\u0004��\u0002\u0005\u0002��\u0002\u0004\u0002��^\u0004\u0006��(\u0004\u0004��^\u0004\u0011��\u0018\u0004Ɉ��ᦶ\u0004J��冦\u0004Z��ҍ\u0004ݳ��⮤\u0004⅜��Į\u0004Ò��\u0007\u0004\f��\u0005\u0004\u0005��\u0001\u0004\u0001\u0005\n\u0004\u0001��\r\u0004\u0001��\u0005\u0004\u0001��\u0001\u0004\u0001��\u0002\u0004\u0001��\u0002\u0004\u0001��l\u0004!��ū\u0004\u0012��@\u0004\u0002��6\u0004(��\f\u0004$��\u0004\u0005\u000f��\u0002\u0004\u0018��\u0003\u0004\u0019��\u0001\u0004\u0006��\u0003\u0004\u0001��\u0001\u0004\u0001��\u0087\u0004\u0002��\u0001\u0005\u0004��\u0001\u0004\u000b��\n\u0005\u0007��\u001a\u0004\u0004��\u0001\u0004\u0001��\u001a\u0004\n��Z\u0004\u0003��\u0006\u0004\u0002��\u0006\u0004\u0002��\u0006\u0004\u0002��\u0003\u0004\u0003��\u0002\u0004\u0003��\u0002\u0004\u0012��\u0003\u0005\u0004��";
    private static final char[] yycmap = yy_unpack_cmap(yycmap_packed);
    private static final int[] yy_rowMap = {0, 53, 106, 159, 212, 265, 265, 318, TokenId.ARSHIFT_E, HttpStatus.SC_FAILED_DEPENDENCY, 477, 530, 583, 636, 689, 265, 265, 742, 795, 848, 265, 265, 265, 265, 265, 265, 265, 265, 901, 954, 1007, BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX, 265, 265, 265, 1113, 1166, 1219, 1272, 1325, 1378, 265, 1431, 1484, 265, 1537, 265, 1590, 1643, 265, 1696, 265, 1749, 1802, 1855, 1908, 1961, WindowManager.LayoutParams.TYPE_STATUS_BAR_PANEL, 265, 265, 265, 265, 265, 265, 265, 265, 265, 2067, 2120, 2173, 265, 265, 2226, 265, 2279, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, BluetoothClass.Device.HEALTH_DATA_DISPLAY, 2385, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 265, 2438, 2491, 2544, 2597, 2650, 2703, 265, 2756, 265, 265, 265, TokenId.ARSHIFT_E, 2809, TokenId.ARSHIFT_E, 265, TokenId.ARSHIFT_E};
    private static final int[] yytrans = yy_unpack();
    private static final String[] YY_ERROR_MSG = {"Unkown internal scanner error", "Internal error: unknown state", "Error: could not match input", "Error: pushback value was too large"};
    private static final byte[] YY_ATTRIBUTE = {0, 0, 0, 0, 0, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 9, 9, 9, 1, 1, 1, 1, 1, 1, 9, 1, 1, 9, 1, 9, 1, 1, 9, 1, 9, 1, 1, 0, 0, 1, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 9, 9, 1, 9, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 0, 1, 1, 1, 9, 1, 9, 9, 9, 1, 1, 1, 9, 1};

    public Lexer_c(InputStream in, String file, ErrorQueue eq) {
        this(new BufferedReader(new InputStreamReader(in)), file, eq);
    }

    public Lexer_c(Reader reader, String file, ErrorQueue eq) {
        this(new EscapedUnicodeReader(reader));
        this.file = file;
        this.eq = eq;
        this.keywords = new HashMap();
        init_keywords();
    }

    protected void init_keywords() {
        this.keywords.put(Jimple.ABSTRACT, new Integer(29));
        this.keywords.put("assert", new Integer(105));
        this.keywords.put("boolean", new Integer(2));
        this.keywords.put(Jimple.BREAK, new Integer(51));
        this.keywords.put("byte", new Integer(3));
        this.keywords.put(Jimple.CASE, new Integer(46));
        this.keywords.put(Jimple.CATCH, new Integer(56));
        this.keywords.put("char", new Integer(7));
        this.keywords.put("class", new Integer(35));
        this.keywords.put("const", new Integer(106));
        this.keywords.put("continue", new Integer(52));
        this.keywords.put("default", new Integer(47));
        this.keywords.put("do", new Integer(48));
        this.keywords.put("double", new Integer(9));
        this.keywords.put("else", new Integer(44));
        this.keywords.put(Jimple.EXTENDS, new Integer(36));
        this.keywords.put(Jimple.FINAL, new Integer(30));
        this.keywords.put("finally", new Integer(57));
        this.keywords.put(Jimple.FLOAT, new Integer(8));
        this.keywords.put("for", new Integer(50));
        this.keywords.put(Jimple.GOTO, new Integer(107));
        this.keywords.put(Jimple.IF, new Integer(43));
        this.keywords.put(Jimple.IMPLEMENTS, new Integer(37));
        this.keywords.put("import", new Integer(24));
        this.keywords.put(Jimple.INSTANCEOF, new Integer(74));
        this.keywords.put("int", new Integer(5));
        this.keywords.put("interface", new Integer(42));
        this.keywords.put("long", new Integer(6));
        this.keywords.put(Jimple.NATIVE, new Integer(31));
        this.keywords.put("new", new Integer(58));
        this.keywords.put("package", new Integer(23));
        this.keywords.put(Jimple.PRIVATE, new Integer(27));
        this.keywords.put(Jimple.PROTECTED, new Integer(26));
        this.keywords.put(Jimple.PUBLIC, new Integer(25));
        this.keywords.put("return", new Integer(53));
        this.keywords.put("short", new Integer(4));
        this.keywords.put(Jimple.STATIC, new Integer(28));
        this.keywords.put(Jimple.STRICTFP, new Integer(104));
        this.keywords.put("super", new Integer(41));
        this.keywords.put("switch", new Integer(45));
        this.keywords.put(Jimple.SYNCHRONIZED, new Integer(32));
        this.keywords.put("this", new Integer(40));
        this.keywords.put(Jimple.THROW, new Integer(54));
        this.keywords.put(Jimple.THROWS, new Integer(39));
        this.keywords.put(Jimple.TRANSIENT, new Integer(33));
        this.keywords.put("try", new Integer(55));
        this.keywords.put(Jimple.VOID, new Integer(38));
        this.keywords.put(Jimple.VOLATILE, new Integer(34));
        this.keywords.put("while", new Integer(49));
    }

    @Override // polyglot.lex.Lexer
    public String file() {
        return this.file;
    }

    private Position pos() {
        return new Position(this.file, this.yyline + 1, this.yycolumn, this.yyline + 1, this.yycolumn + yytext().length());
    }

    private Position pos(int len) {
        return new Position(this.file, this.yyline + 1, (this.yycolumn - len) - 1, this.yyline + 1, this.yycolumn + 1);
    }

    private Token key(int symbol) {
        return new Keyword(pos(), yytext(), symbol);
    }

    private Token op(int symbol) {
        return new Operator(pos(), yytext(), symbol);
    }

    private Token id() {
        return new Identifier(pos(), yytext(), 12);
    }

    private Token int_lit(String s, int radix) {
        BigInteger x = new BigInteger(s, radix);
        boolean boundary = radix == 10 && s.equals("2147483648");
        int bits = radix == 10 ? 31 : 32;
        if (x.bitLength() > bits && !boundary) {
            this.eq.enqueue(3, new StringBuffer().append("Integer literal \"").append(yytext()).append("\" out of range.").toString(), pos());
        }
        return new IntegerLiteral(pos(), x.intValue(), boundary ? 95 : 94);
    }

    private Token long_lit(String s, int radix) {
        BigInteger x = new BigInteger(s, radix);
        boolean boundary = radix == 10 && s.equals("9223372036854775808");
        int bits = radix == 10 ? 63 : 64;
        if (x.bitLength() > bits && !boundary) {
            this.eq.enqueue(3, new StringBuffer().append("Long literal \"").append(yytext()).append("\" out of range.").toString(), pos());
        }
        return new LongLiteral(pos(), x.longValue(), boundary ? 97 : 96);
    }

    private Token float_lit(String s) {
        try {
            Float x = Float.valueOf(s);
            boolean zero = true;
            int i = 0;
            while (true) {
                if (i < s.length()) {
                    if ('1' <= s.charAt(i) && s.charAt(i) <= '9') {
                        zero = false;
                        break;
                    } else if (s.charAt(i) == 'e' || s.charAt(i) == 'E') {
                        break;
                    } else {
                        i++;
                    }
                } else {
                    break;
                }
            }
            if (x.isInfinite() || x.isNaN() || (x.floatValue() == 0.0f && !zero)) {
                this.eq.enqueue(3, new StringBuffer().append("Illegal float literal \"").append(yytext()).append("\"").toString(), pos());
            }
            return new FloatLiteral(pos(), x.floatValue(), 99);
        } catch (NumberFormatException e) {
            this.eq.enqueue(3, new StringBuffer().append("Illegal float literal \"").append(yytext()).append("\"").toString(), pos());
            return new FloatLiteral(pos(), 0.0f, 99);
        }
    }

    private Token double_lit(String s) {
        try {
            Double x = Double.valueOf(s);
            boolean zero = true;
            int i = 0;
            while (true) {
                if (i < s.length()) {
                    if ('1' <= s.charAt(i) && s.charAt(i) <= '9') {
                        zero = false;
                        break;
                    } else if (s.charAt(i) == 'e' || s.charAt(i) == 'E') {
                        break;
                    } else {
                        i++;
                    }
                } else {
                    break;
                }
            }
            if (x.isInfinite() || x.isNaN() || (x.doubleValue() == Const.default_value_double && !zero)) {
                this.eq.enqueue(3, new StringBuffer().append("Illegal double literal \"").append(yytext()).append("\"").toString(), pos());
            }
            return new DoubleLiteral(pos(), x.doubleValue(), 98);
        } catch (NumberFormatException e) {
            this.eq.enqueue(3, new StringBuffer().append("Illegal double literal \"").append(yytext()).append("\"").toString(), pos());
            return new DoubleLiteral(pos(), Const.default_value_double, 98);
        }
    }

    private Token char_lit(String s) {
        if (s.length() == 1) {
            char x = s.charAt(0);
            return new CharacterLiteral(pos(), x, 101);
        }
        this.eq.enqueue(3, new StringBuffer().append("Illegal character literal '").append(s).append("'").toString(), pos(s.length()));
        return new CharacterLiteral(pos(), (char) 0, 101);
    }

    private Token boolean_lit(boolean x) {
        return new BooleanLiteral(pos(), x, 100);
    }

    private Token null_lit() {
        return new NullLiteral(pos(), 103);
    }

    private Token string_lit() {
        return new StringLiteral(pos(this.sb.length()), this.sb.toString(), 102);
    }

    private String chop(int i, int j) {
        return yytext().substring(i, yylength() - j);
    }

    private String chop(int j) {
        return chop(0, j);
    }

    private String chop() {
        return chop(0, 1);
    }

    public Lexer_c(Reader in) {
        this.yy_lexical_state = 0;
        this.yy_buffer = new char[16384];
        this.yy_atBOL = true;
        this.sb = new StringBuffer();
        this.yy_reader = in;
    }

    public Lexer_c(InputStream in) {
        this(new InputStreamReader(in));
    }

    private static int[] yy_unpack() {
        int[] trans = new int[2862];
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
        while (i < 1642) {
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

    @Override // polyglot.lex.Lexer
    public Token nextToken() throws IOException {
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
                case 5:
                case 43:
                case 48:
                    this.eq.enqueue(3, new StringBuffer().append("Illegal character \"").append(yytext()).append("\"").toString(), pos());
                    break;
                case 6:
                case 7:
                case 50:
                case 121:
                case 122:
                case 123:
                case 124:
                case 125:
                case 126:
                case 127:
                case 128:
                case 129:
                case 130:
                case 131:
                case 132:
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
                case 171:
                case 172:
                case 173:
                case 174:
                case 175:
                case 176:
                case 177:
                case 178:
                case 179:
                case 180:
                case 181:
                case 182:
                case 183:
                case 184:
                case 185:
                case 186:
                case 187:
                case 188:
                case 189:
                case 190:
                case 191:
                case 192:
                case 193:
                case 194:
                case 195:
                case 196:
                case 197:
                case 198:
                case 199:
                case 200:
                case 201:
                case 202:
                case 203:
                case 204:
                case 205:
                case 206:
                case 207:
                case 208:
                case 209:
                case 210:
                case 211:
                    break;
                case 8:
                case 17:
                case 18:
                case 19:
                case 67:
                case 68:
                case 69:
                case 107:
                case 108:
                case 109:
                case 116:
                    Integer i3 = (Integer) this.keywords.get(yytext());
                    return i3 == null ? id() : key(i3.intValue());
                case 9:
                case 10:
                    return int_lit(yytext(), 10);
                case 11:
                    return op(13);
                case 12:
                    return op(62);
                case 13:
                    return op(65);
                case 14:
                    return op(15);
                case 15:
                    yybegin(2);
                    this.sb.setLength(0);
                    break;
                case 16:
                    yybegin(1);
                    this.sb.setLength(0);
                    break;
                case 20:
                    return op(20);
                case 21:
                    return op(21);
                case 22:
                    return op(17);
                case 23:
                    return op(18);
                case 24:
                    return op(10);
                case 25:
                    return op(11);
                case 26:
                    return op(14);
                case 27:
                    return op(16);
                case 28:
                    return op(19);
                case 29:
                    return op(71);
                case 30:
                    return op(70);
                case 31:
                    return op(64);
                case 32:
                    return op(63);
                case 33:
                    return op(82);
                case 34:
                    return op(22);
                case 35:
                    return op(77);
                case 36:
                    return op(79);
                case 37:
                    return op(61);
                case 38:
                    return op(78);
                case 39:
                    return op(66);
                case 40:
                    this.sb.append(yytext());
                    break;
                case 41:
                case 42:
                    yybegin(0);
                    this.eq.enqueue(3, "Unclosed string literal", pos(this.sb.length()));
                    break;
                case 44:
                    yybegin(0);
                    return string_lit();
                case 45:
                    this.sb.append(yytext());
                    break;
                case 46:
                case 47:
                    yybegin(0);
                    this.eq.enqueue(3, "Unclosed character literal", pos(this.sb.length()));
                    break;
                case 49:
                    yybegin(0);
                    return char_lit(this.sb.toString());
                case 51:
                case 52:
                    yybegin(0);
                    break;
                case 53:
                    return int_lit(yytext(), 8);
                case 54:
                case 55:
                case 57:
                case 106:
                case 120:
                default:
                    if (yy_input == -1 && this.yy_startRead == this.yy_currentPos) {
                        this.yy_atEOF = true;
                        return new EOF(pos(), 0);
                    }
                    yy_ScanError(2);
                    break;
                case 56:
                case 105:
                    return double_lit(yytext());
                case 58:
                    return float_lit(chop());
                case 59:
                    return long_lit(chop(), 10);
                case 60:
                    return double_lit(chop());
                case 61:
                    return op(60);
                case 62:
                    return op(87);
                case 63:
                    yybegin(4);
                    break;
                case 64:
                    yybegin(3);
                    break;
                case 65:
                    return op(84);
                case 66:
                    return op(83);
                case 70:
                    return op(75);
                case 71:
                    return op(73);
                case 72:
                    return op(68);
                case 73:
                    return op(72);
                case 74:
                    return op(67);
                case 75:
                    return op(76);
                case 76:
                    return op(91);
                case 77:
                    return op(80);
                case 78:
                    return op(93);
                case 79:
                    return op(81);
                case 80:
                    return op(86);
                case 81:
                    return op(59);
                case 82:
                    return op(92);
                case 83:
                    return op(85);
                case 84:
                    this.eq.enqueue(3, new StringBuffer().append("Illegal escape character \"").append(yytext()).append("\"").toString(), pos());
                    break;
                case 85:
                case 86:
                case 113:
                    try {
                        int x = Integer.parseInt(chop(1, 0), 8);
                        this.sb.append((char) x);
                        break;
                    } catch (NumberFormatException e) {
                        this.eq.enqueue(3, new StringBuffer().append("Illegal octal escape \"").append(yytext()).append("\"").toString(), pos());
                        break;
                    }
                case 87:
                    this.sb.append('\\');
                    break;
                case 88:
                    this.sb.append('\'');
                    break;
                case 89:
                    this.sb.append('\"');
                    break;
                case 90:
                    this.sb.append('\t');
                    break;
                case 91:
                    this.sb.append('\r');
                    break;
                case 92:
                    this.sb.append('\f');
                    break;
                case 93:
                    this.sb.append('\n');
                    break;
                case 94:
                    this.sb.append('\b');
                    break;
                case 95:
                    this.sb.append('\\');
                    break;
                case 96:
                    this.sb.append('\'');
                    break;
                case 97:
                    this.sb.append('\"');
                    break;
                case 98:
                    this.sb.append('\t');
                    break;
                case 99:
                    this.sb.append('\r');
                    break;
                case 100:
                    this.sb.append('\f');
                    break;
                case 101:
                    this.sb.append('\n');
                    break;
                case 102:
                    this.sb.append('\b');
                    break;
                case 103:
                    return long_lit(chop(), 8);
                case 104:
                    return int_lit(chop(2, 0), 16);
                case 110:
                    return op(89);
                case 111:
                    return op(69);
                case 112:
                    return op(88);
                case 114:
                    return long_lit(chop(2, 1), 16);
                case 115:
                    return boolean_lit(true);
                case 117:
                    return null_lit();
                case 118:
                    return op(90);
                case 119:
                    return boolean_lit(false);
            }
        }
    }
}
