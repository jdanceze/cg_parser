package ppg.lex;

import android.bluetooth.BluetoothClass;
import dalvik.bytecode.Opcodes;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.http.HttpStatus;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:ppg/lex/Lexer.class */
public class Lexer {
    public static final int YYEOF = -1;
    private static final int YY_BUFFERSIZE = 16384;
    public static final int CODE = 3;
    public static final int STRING = 2;
    public static final int YYINITIAL = 0;
    public static final int COMMENT = 1;
    private static final String yy_packed0 = "\u0001\u0005\u0001\u0006\u0001\u0005\u0001\u0007\u0001\b\u0001\u0007\u0001\t\u0001\n\u0001\u000b\u0001\u0005\u0001\u0007\u0001\u0005\u0001\u0006\u0001\f\u0001\r\u0003\u0005\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0006\u0001\u0012\u0001\u0013\u0001\u0006\u0001\u0014\u0001\u0015\u0001\u0016\u0001\u0006\u0001\u0017\u0001\u0018\u0004\u0006\u0001\u0019\u0001\u0006\u0001\u0005\u0001\u001a\u0001\u001b\u0001\u001c\u0001\u001d\u0001\u001e\u0001\u001f\u0001 \u0001!\u0005\"\u0001#\u0001$(\"\u0001%\u0002&\u0001%\u0001&\u0001��\u0005&\u0001'\u0006&\u0001(\u001c&\u0005)\u0001*\u0001)\u0001+')0��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\f��\u0001\u0007\u0001��\u0001\u0007\u0004��\u0001\u0007(��\u0001,\u0001��\u0001-/��\u0001.(��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\b\u0006\u0001/\n\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u00010\u0001\u0006\u00011\u00012\n\u0006\n��\u0002\u0006\t��\u0001\u0006\u00013\u0001\u0006\u0001��\u0001\u0006\u0002��\u0010\u0006\u00014\u0002\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\b\u0006\u00015\n\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u00016\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u00017\u000b\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0006\u0006\u00018\f\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u00019\u0012\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\n\u0006\u0001:\b\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001;\u0003\u0006\u0001<\u0007\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001\u0006\u0001=\u0011\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001>\u0001��\u0001\u0006\u0002��\u0001\u0006\u0001?\u0011\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001@\u0012\u0006\u0010��\u0001AC��\u0001B\u0012��\u0006#\u0001C,#\u0001D\u0001#\u0001C(#\u0001��\u0002&\u0001��\u0001&\u0001��\u0005&\u0001'\u0006&\u0001(\u001c&\u0002��\u0001E\u0001F\u0001��\u0001F\u0004��\u0001F\u0004&\u0001G\u0002��\u0001&\u001c��\u0007*\u0001H.*\u0001H\u0001I&*\u0005,\u0001��),&��\u0001J\t��\u0002\u0006\t��\u0001\u0006\u0001K\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001L\u000b\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000b\u0006\u0001M\u0007\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001N\u0001O\u0011\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\t\u0006\u0001P\t\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0004\u0006\u0001Q\u000e\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\r\u0006\u0001R\u0005\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\b\u0006\u0001S\n\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001T\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000f\u0006\u0001U\u0003\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001V\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001W\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001\u0006\u0001X\u0005\u0006\u0001Y\u000b\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001Z\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000b\u0006\u0001[\u0007\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000b\u0006\u0001\\\u0007\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001]\u0001��\u0001\u0006\u0002��\u0013\u0006#��\u0001^\u0016��\u0001_/��\u0001F\u0001��\u0001F\u0004��\u0001F\u0001&$��\u0001&\u0006��\u0001&\u0002��\u0007&\u0001��\u0013&\u0004��\u0004&\u0002��\u0002\u0006\t��\u0002\u0006\u0001`\u0001��\u0001\u0006\u0002��\u000b\u0006\u0001a\u0007\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0010\u0006\u0001b\u0002\u0006\n��\u0002\u0006\t��\u0001\u0006\u0001c\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001d\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0002\u0006\u0001e\u0010\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0002\u0006\u0001f\u0005\u0006\u0001g\n\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001h\r\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001i\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\t\u0006\u0001j\t\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001k\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0012\u0006\u0001l\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001m\u000b\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001\u0006\u0001n\u0011\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000e\u0006\u0001o\u0004\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\f\u0006\u0001p\u0006\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001q\u0012\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001r\u000b\u0006\n��\u0002\u0006\t��\u0001\u0006\u0001s\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0012\u0006\u0001t!��\u0001u\u0018��\u0001&-��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001v\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\f\u0006\u0001w\u0006\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001x\u0012\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\f\u0006\u0001y\u0006\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0003\u0006\u0001z\u000f\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001{\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001|\u000b\u0006\n��\u0002\u0006\t��\u0001\u0006\u0001}\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001~\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001\u007f\u000b\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001\u0080\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000b\u0006\u0001\u0081\u0007\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001\u0082\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\b\u0006\u0001\u0083\n\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001\u0084\u0001��\u0001\u0006\u0002��\u0013\u0006\u001d��\u0001\u0085\u001b��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001\u0086\u000b\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\f\u0006\u0001\u0087\u0006\u0006\n��\u0002\u0006\t��\u0001\u0006\u0001\u0088\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\r\u0006\u0001\u0089\u0005\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0004\u0006\u0001\u008a\u000e\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0010\u0006\u0001\u008b\u0002\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001\u008c\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0004\u0006\u0001\u008d\u000e\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001\u008e\u0012\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0004\u0006\u0001\u008f\u000e\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000f\u0006\u0001\u0090\u0003\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001\u0091\u000b\u0006\n��\u0002\u0006\t��\u0001\u0006\u0001\u0092\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0010\u0006\u0001\u0093\u0002\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\b\u0006\u0001\u0094\n\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000b\u0006\u0001\u0095\u0007\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001\u0096\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001\u0097\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001\u0098\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\f\u0006\u0001\u0099\u0006\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0004\u0006\u0001\u009a\u000e\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001\u009b\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001\u009c\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001\u009d\u0012\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001\u0006\u0001\u009e\u0011\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0002\u0006\u0001\u009f\u0010\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0007\u0006\u0001 \u000b\u0006\n��\u0002\u0006\t��\u0001\u0006\u0001¡\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001¢\r\u0006\n��\u0002\u0006\t��\u0001\u0006\u0001£\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0001\u0006\u0001¤\u0001\u0006\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0002\u0006\u0001¥\u0001��\u0001\u0006\u0002��\u0013\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0001\u0006\u0001¦\u0011\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u000b\u0006\u0001§\u0007\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\f\u0006\u0001¨\u0006\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0005\u0006\u0001©\r\u0006\n��\u0002\u0006\t��\u0003\u0006\u0001��\u0001\u0006\u0002��\u0002\u0006\u0001ª\u0010\u0006\t��";
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
    private int lastId;
    private String filename;
    private String lineSeparator;
    private static final char[] yycmap = {0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 5, 0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '\n', '\t', 18, '\t', '\t', '.', '\t', '\t', '\t', '\t', 6, '\t', ')', '\t', '(', 4, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 7, '\'', '\t', '&', '\t', '\t', 17, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '\f', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, '+', 11, ',', 15, 16, 17, 30, 1, 20, 23, 24, ' ', '\"', '%', 19, 1, '!', 21, '#', '\r', 27, 28, 1, 26, 31, 14, 22, 29, '$', 25, 1, 1, '*', '-', '\b', 17, 0};
    private static final int[] yy_rowMap = {0, 47, 94, 141, 188, 235, 282, 329, 188, 376, 188, HttpStatus.SC_LOCKED, 470, 188, 517, 564, 611, 658, 705, 752, 799, 846, 893, 940, 987, 188, 188, 188, 1034, 188, 188, 188, 1081, 1128, 1128, 1175, 188, 1222, 1269, 188, 1316, 1316, 1363, 1410, 188, 1457, 1504, 1551, 1598, 235, 1645, 1692, 1739, 1786, 1833, 1880, 1927, 1974, 2021, BluetoothClass.Device.TOY_GAME, 2115, 2162, 2209, 2256, 188, Opcodes.OP_IGET_OBJECT_JUMBO, 1175, 188, 2350, 2397, 2444, 1363, 188, 188, 2491, 2538, 2585, 2632, 2679, 2726, 2773, 2820, 2867, 2914, 2961, 3008, 3055, 3102, 3149, 3196, 3243, 3290, 3337, 3384, 3431, 3478, 3525, 3572, 3619, 235, 3666, 3713, 3760, 235, 235, 235, 3807, 3854, 3901, 3948, 3995, 4042, 4089, 4136, 235, 235, 4183, 4230, 4277, 4324, 4371, 4418, 4465, 4512, 4559, 235, 4606, 4653, 4700, 4747, 4794, 235, 188, 4841, 4888, 4935, 4982, 5029, 5076, 235, 5123, 5170, 5217, 5264, 235, 235, 5311, 5358, 5405, 5452, 235, 5499, 235, 5546, 5593, 235, 5640, 235, 235, 235, 5687, 235, 5734, 5781, 5828, 5875, 5922, 235, 235, 235};
    private static final int[] yytrans = yy_unpack();
    private static final String[] YY_ERROR_MSG = {"Unkown internal scanner error", "Internal error: unknown state", "Error: could not match input", "Error: pushback value was too large"};
    private static final byte[] YY_ATTRIBUTE = {0, 0, 1, 0, 9, 1, 1, 1, 9, 1, 9, 1, 1, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 1, 9, 9, 9, 1, 1, 0, 1, 9, 1, 1, 9, 1, 0, 1, 1, 9, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 0, 0, 9, 0, 0, 0, 0, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

    public Lexer(InputStream in, String filename) {
        this(in);
        this.filename = filename;
    }

    private void error(String message) throws LexicalError {
        throw new LexicalError(this.filename, this.yyline + 1, message);
    }

    private Token t(int id, Object value) {
        this.lastId = id;
        return new Token(id, this.filename, this.yyline + 1, this.yychar, this.yychar + yylength(), value);
    }

    private Token t(int id) {
        return t(id, yytext());
    }

    public Lexer(Reader in) {
        this.yy_lexical_state = 0;
        this.yy_buffer = new char[16384];
        this.yy_atBOL = true;
        this.lastId = -1;
        this.filename = "";
        this.lineSeparator = System.getProperty("line.separator", "\n");
        this.yy_reader = in;
    }

    public Lexer(InputStream in) {
        this(new InputStreamReader(in));
    }

    private static int[] yy_unpack() {
        int[] trans = new int[5969];
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

    public Token getToken() throws IOException, LexicalError {
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
            this.yychar += yy_markedPos_l - this.yy_startRead;
            boolean yy_r = false;
            for (int yy_currentPos_l = this.yy_startRead; yy_currentPos_l < yy_markedPos_l; yy_currentPos_l++) {
                switch (yy_buffer_l[yy_currentPos_l]) {
                    case '\n':
                        if (!yy_r) {
                            this.yyline++;
                            break;
                        } else {
                            yy_r = false;
                            break;
                        }
                    case 11:
                    case '\f':
                    case 133:
                    case 8232:
                    case 8233:
                        this.yyline++;
                        yy_r = false;
                        break;
                    case '\r':
                        this.yyline++;
                        yy_r = true;
                        break;
                    default:
                        yy_r = false;
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
                case 37:
                    error("Unclosed string literal");
                    break;
                case 3:
                case 34:
                case 41:
                case 45:
                case 65:
                case 66:
                case 68:
                case 69:
                case 70:
                case 71:
                case 93:
                case 94:
                case 116:
                case 170:
                default:
                    if (yy_input == -1 && this.yy_startRead == this.yy_currentPos) {
                        this.yy_atEOF = true;
                        return t(0, "EOF");
                    }
                    yy_ScanError(2);
                    break;
                    break;
                case 4:
                case 7:
                case 32:
                    error(new StringBuffer().append("Invalid character: ").append(yytext()).toString());
                    break;
                case 5:
                case 11:
                case 12:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 46:
                case 47:
                case 48:
                case 50:
                case 51:
                case 52:
                case 53:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                case 62:
                case 63:
                case 75:
                case 76:
                case 77:
                case 78:
                case 79:
                case 80:
                case 81:
                case 82:
                case 83:
                case 84:
                case 85:
                case 86:
                case 87:
                case 88:
                case 89:
                case 90:
                case 91:
                case 92:
                case 95:
                case 96:
                case 97:
                case 98:
                case 100:
                case 101:
                case 102:
                case 106:
                case 107:
                case 108:
                case 109:
                case 110:
                case 111:
                case 112:
                case 113:
                case 117:
                case 118:
                case 119:
                case 120:
                case 121:
                case 122:
                case 123:
                case 124:
                case 126:
                case 127:
                case 128:
                case 129:
                case 130:
                case 133:
                case 134:
                case 135:
                case 136:
                case 137:
                case 138:
                case 141:
                case 142:
                case 143:
                case 146:
                case 147:
                case 148:
                case 149:
                case 151:
                case 153:
                case 154:
                case 156:
                case 160:
                case 162:
                case 163:
                case 164:
                case 165:
                case 166:
                    return t(37, yytext().intern());
                case 6:
                case 43:
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
                case 212:
                case 213:
                case 214:
                case 215:
                case 216:
                case 217:
                case 218:
                case 219:
                case 220:
                case 221:
                    break;
                case 8:
                    return t(31);
                case 9:
                    return t(12);
                case 10:
                    return t(7);
                case 13:
                    yybegin(2);
                    break;
                case 25:
                    return t(11);
                case 26:
                    return t(18);
                case 27:
                    return t(10);
                case 28:
                    return t(6);
                case 29:
                    return t(19);
                case 30:
                    return t(20);
                case 31:
                    return t(14);
                case 33:
                case 35:
                    error("Illegal comment");
                    break;
                case 36:
                    error(new StringBuffer().append("Illegal character in string literal: ").append(yytext()).toString());
                    break;
                case 38:
                    error("Illegal escape character");
                    break;
                case 39:
                    yybegin(0);
                    String literal = yytext();
                    return t(36, literal.substring(0, literal.length() - 1));
                case 40:
                case 42:
                    error(new StringBuffer().append("Invalid character in code block: '").append(yytext()).append("'").toString());
                    break;
                case 44:
                    yybegin(1);
                    break;
                case 49:
                    return t(3);
                case 64:
                    yybegin(3);
                    break;
                case 67:
                    yybegin(0);
                    break;
                case 72:
                    yybegin(0);
                    String codeStr = yytext();
                    return t(38, codeStr.substring(0, codeStr.length() - 2));
                case 73:
                    return t(13);
                case 74:
                    return t(26);
                case 99:
                    return t(27);
                case 103:
                    return t(23);
                case 104:
                    return t(33);
                case 105:
                    return t(4);
                case 114:
                    return t(28);
                case 115:
                    return t(29);
                case 125:
                    return t(34);
                case 131:
                    return t(30);
                case 132:
                    return t(17);
                case 139:
                    return t(22);
                case 140:
                    return t(39);
                case 144:
                    return t(25);
                case 145:
                    return t(24);
                case 150:
                    return t(2);
                case 152:
                    return t(8);
                case 155:
                    return t(21);
                case 157:
                    return t(35);
                case 158:
                    return t(15);
                case 159:
                    return t(5);
                case 161:
                    return t(40);
                case 167:
                    return t(9);
                case 168:
                    return t(32);
                case 169:
                    return t(16);
            }
        }
    }
}
