package org.hamcrest.generator.qdox.parser.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import javax.resource.spi.work.WorkException;
import org.hamcrest.generator.qdox.model.Annotation;
import org.hamcrest.generator.qdox.model.annotation.AnnotationValue;
import org.hamcrest.generator.qdox.parser.Builder;
import org.hamcrest.generator.qdox.parser.Lexer;
import org.hamcrest.generator.qdox.parser.ParseException;
import org.hamcrest.generator.qdox.parser.structs.ClassDef;
import org.hamcrest.generator.qdox.parser.structs.FieldDef;
import org.hamcrest.generator.qdox.parser.structs.MethodDef;
import org.hamcrest.generator.qdox.parser.structs.TypeDef;
import org.hamcrest.generator.qdox.parser.structs.TypeVariableDef;
import soot.coffi.Instruction;
import soot.dava.internal.AST.ASTNode;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/impl/Parser.class */
public class Parser {
    boolean yydebug;
    int yynerrs;
    int yyerrflag;
    int yychar;
    static final int YYSTACKSIZE = 500;
    int[] statestk;
    int stateptr;
    int stateptrmax;
    int statemax;
    String yytext;
    Value yyval;
    Value yylval;
    Value[] valstk;
    int valptr;
    public static final short SEMI = 257;
    public static final short DOT = 258;
    public static final short DOTDOTDOT = 259;
    public static final short COMMA = 260;
    public static final short STAR = 261;
    public static final short PERCENT = 262;
    public static final short EQUALS = 263;
    public static final short ANNOSTRING = 264;
    public static final short ANNOCHAR = 265;
    public static final short SLASH = 266;
    public static final short PLUS = 267;
    public static final short MINUS = 268;
    public static final short PACKAGE = 269;
    public static final short IMPORT = 270;
    public static final short PUBLIC = 271;
    public static final short PROTECTED = 272;
    public static final short PRIVATE = 273;
    public static final short STATIC = 274;
    public static final short FINAL = 275;
    public static final short ABSTRACT = 276;
    public static final short NATIVE = 277;
    public static final short STRICTFP = 278;
    public static final short SYNCHRONIZED = 279;
    public static final short TRANSIENT = 280;
    public static final short VOLATILE = 281;
    public static final short CLASS = 282;
    public static final short INTERFACE = 283;
    public static final short ENUM = 284;
    public static final short ANNOINTERFACE = 285;
    public static final short THROWS = 286;
    public static final short EXTENDS = 287;
    public static final short IMPLEMENTS = 288;
    public static final short SUPER = 289;
    public static final short DEFAULT = 290;
    public static final short BRACEOPEN = 291;
    public static final short BRACECLOSE = 292;
    public static final short SQUAREOPEN = 293;
    public static final short SQUARECLOSE = 294;
    public static final short PARENOPEN = 295;
    public static final short PARENCLOSE = 296;
    public static final short LESSTHAN = 297;
    public static final short GREATERTHAN = 298;
    public static final short LESSEQUALS = 299;
    public static final short GREATEREQUALS = 300;
    public static final short LESSTHAN2 = 301;
    public static final short GREATERTHAN2 = 302;
    public static final short GREATERTHAN3 = 303;
    public static final short EXCLAMATION = 304;
    public static final short AMPERSAND2 = 305;
    public static final short VERTLINE2 = 306;
    public static final short EQUALS2 = 307;
    public static final short NOTEQUALS = 308;
    public static final short TILDE = 309;
    public static final short AMPERSAND = 310;
    public static final short VERTLINE = 311;
    public static final short CIRCUMFLEX = 312;
    public static final short VOID = 313;
    public static final short QUERY = 314;
    public static final short COLON = 315;
    public static final short AT = 316;
    public static final short JAVADOCSTART = 317;
    public static final short JAVADOCEND = 318;
    public static final short JAVADOCEOL = 319;
    public static final short CODEBLOCK = 320;
    public static final short PARENBLOCK = 321;
    public static final short BYTE = 322;
    public static final short SHORT = 323;
    public static final short INT = 324;
    public static final short LONG = 325;
    public static final short CHAR = 326;
    public static final short FLOAT = 327;
    public static final short DOUBLE = 328;
    public static final short BOOLEAN = 329;
    public static final short IDENTIFIER = 330;
    public static final short JAVADOCTAG = 331;
    public static final short JAVADOCLINE = 332;
    public static final short BOOLEAN_LITERAL = 333;
    public static final short INTEGER_LITERAL = 334;
    public static final short LONG_LITERAL = 335;
    public static final short FLOAT_LITERAL = 336;
    public static final short DOUBLE_LITERAL = 337;
    public static final short CHAR_LITERAL = 338;
    public static final short STRING_LITERAL = 339;
    public static final short YYERRCODE = 256;
    static final int YYTABLESIZE = 1189;
    static final short YYFINAL = 1;
    static final short YYMAXTOKEN = 339;
    private Lexer lexer;
    private Builder builder;
    private TypeDef fieldType;
    private TypeVariableDef typeVariable;
    private int line;
    private int column;
    private boolean debugLexer;
    int yyn;
    int yym;
    int yystate;
    String yys;
    static final short[] yylhs = {-1, 0, 35, 0, 34, 34, 34, 34, 34, 34, 34, 36, 37, 37, 41, 41, 38, 42, 44, 44, 45, 43, 43, 47, 46, 23, 23, 23, 31, 28, 28, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 24, 48, 48, 48, 48, 50, 6, 49, 49, 49, 49, 51, 51, 52, 53, 7, 54, 54, 55, 55, 3, 3, 3, 4, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 14, 15, 15, 15, 15, 15, 16, 16, 16, 16, 17, 17, 17, 18, 18, 18, 18, 19, 19, 19, 20, 20, 20, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 22, 22, 1, 1, 5, 5, 5, 5, 5, 5, 5, 2, 2, 2, 2, 2, 2, 2, 2, 30, 57, 58, 32, 32, 25, 25, 26, 26, 56, 56, 33, 33, 33, 33, 59, 59, 62, 60, 61, 61, 63, 65, 63, 64, 64, 40, 66, 67, 67, 69, 69, 69, 71, 71, 73, 73, 73, 73, 39, 74, 75, 75, 75, 76, 76, 77, 77, 68, 68, 78, 78, 70, 80, 70, 79, 79, 79, 79, 79, 79, 79, 79, 27, 27, 84, 86, 81, 85, 87, 85, 88, 85, 90, 82, 92, 82, 93, 83, 94, 83, 89, 91, 91, 96, 96, 95, 95, 97, 97, 98, 29, 29, 72, 72, 99, 99, 99};
    static final short[] yylen = {2, 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 3, 3, 4, 1, 2, 4, 1, 0, 2, 1, 0, 2, 0, 3, 1, 3, 3, 2, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 0, 0, 4, 0, 3, 3, 2, 1, 3, 3, 0, 4, 0, 1, 1, 3, 1, 1, 1, 1, 1, 5, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 3, 1, 3, 3, 3, 3, 1, 3, 3, 3, 1, 3, 3, 1, 3, 3, 3, 2, 2, 1, 2, 2, 1, 4, 5, 5, 4, 3, 1, 4, 3, 3, 4, 1, 2, 3, 1, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 0, 6, 1, 1, 3, 1, 3, 1, 3, 1, 1, 3, 3, 0, 1, 0, 4, 1, 3, 1, 0, 4, 1, 3, 4, 4, 1, 3, 0, 1, 3, 3, 2, 1, 2, 2, 3, 4, 6, 1, 1, 1, 0, 2, 1, 3, 0, 2, 1, 3, 0, 0, 3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 6, 0, 0, 4, 0, 5, 0, 9, 0, 8, 0, 6, 0, 7, 3, 0, 2, 1, 3, 0, 1, 1, 3, 4, 0, 1, 0, 2, 0, 2, 2};
    static final short[] yydefred = {1, 2, 0, 10, 0, 0, 0, 18, 4, 3, 5, 6, 7, 8, 9, 0, 0, 0, 25, 0, 0, 0, 115, 0, 21, 0, 31, 32, 33, 34, 35, 36, 37, 41, 38, 40, 39, 173, 174, 0, 175, 43, 42, 44, 0, 0, 184, 11, 0, 0, 12, 0, 0, 0, 20, 19, 0, 0, 14, 0, 0, 0, 0, 0, 0, 27, 26, 13, 116, 0, 47, 16, 23, 22, 0, 159, 149, 0, 148, 15, 0, 158, 184, 0, 0, 226, 166, 171, 0, 0, 0, 0, 51, 0, 0, 125, 126, 127, 128, 129, 130, 131, 124, 0, 121, 120, 119, 118, 117, 122, 123, 0, 0, 0, 61, 107, 62, 63, 64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 92, 98, 101, 0, 52, 0, 18, 139, 0, 0, 0, 0, 0, 0, 0, 165, 0, 164, 168, 0, 194, 187, 192, 193, 0, 186, 188, 189, 190, 191, 96, 97, 0, 0, 0, 100, 99, 0, 0, 0, 0, 0, 0, 49, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 50, 0, 0, 133, 0, 0, 0, 0, 0, 151, 0, 0, 172, 170, 197, 0, 0, 0, 0, 0, 0, 0, 0, 106, 54, 110, 113, 0, 0, 109, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 93, 95, 94, 0, 53, 59, 0, 0, 0, 140, 138, 0, 154, 0, 150, 0, 0, 0, 198, 0, 0, 0, 105, 0, 102, 0, 111, 114, 108, 0, 56, 0, 0, 143, 141, 0, 0, 152, 0, 0, 0, 0, 0, 200, 0, 0, 205, 104, 103, 66, 60, 0, 0, 0, 0, 156, 0, 0, 0, 220, 0, 0, 0, 29, 0, 30, 0, 0, 145, 146, 142, 135, 0, 213, 227, 229, 228, 0, 0, 0, 195, 196, 210, 0, 0, 199, 0, 29, 157, 221, 224, 0, 0, 0, 203, 0, 212, 0, 29, 222, 0, 208, 0, 202, 0, 204, 206};
    static final short[] yydgoto = {1, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 169, 19, 42, 137, 138, 324, 283, 333, 275, 260, 211, 276, 9, 2, 10, 11, 43, 13, 14, 59, 24, 53, 25, 55, 73, 135, 15, 70, 52, 132, 133, 134, 248, 249, 277, 250, 296, 77, 78, 202, 141, 203, 298, 278, 16, 60, 75, 61, 64, 62, 63, 86, 17, 44, 143, 205, 140, 154, 88, 155, 156, 157, 158, 306, 285, 337, 344, 282, 309, 304, 284, 258, 287, 299, 321, 300, 301, 302};
    static final short[] yysindex = {0, 0, -153, 0, -297, -257, -278, 0, 0, 0, 0, 0, 0, 0, 0, 825, -181, -162, 0, 98, -297, 123, 0, -78, 0, -161, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -111, 0, 0, 0, 0, -36, -50, 0, 0, -248, 126, 0, 19, 33, -283, 0, 0, 42, 55, 0, -50, 104, 146, 140, 3, 118, 0, 0, 0, 0, -86, 0, 0, 0, 0, 92, 0, 0, 145, 0, 0, 3, 0, 0, -50, 66, 0, 0, 0, -238, -184, -184, -184, 0, -184, -184, 0, 0, 0, 0, 0, 0, 0, 0, 174, 0, 0, 0, 0, 0, 0, 0, -224, -124, 175, 0, 0, 0, 0, 0, 39, 192, 189, 191, 194, 82, 144, -75, 179, 143, 0, 0, 0, -228, 0, 210, 0, 0, 208, 249, 250, 251, 180, 92, 42, 0, 0, 0, 0, 204, 0, 0, 0, 0, 182, 0, 0, 0, 0, 0, 0, 0, -166, -136, 213, 0, 0, 90, -268, 232, -80, 245, -47, 0, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, -184, 198, 0, 90, -161, 0, 200, 201, 92, 246, -239, 0, 250, 272, 0, 0, 0, 0, 205, 250, 206, 835, -92, -184, -79, 0, 0, 0, 0, 252, 243, 0, 258, 192, 228, 189, 191, 194, 82, 144, 144, -75, -75, -75, -75, 179, 179, 179, 143, 143, 0, 0, 0, 174, 0, 0, 253, 284, -270, 0, 0, 250, 0, 180, 0, 92, 254, 0, 0, 257, 0, 221, 0, 835, 0, -184, 0, 0, 0, -184, 0, 90, 36, 0, 0, 293, 92, 0, 250, 0, 266, 257, 254, 0, 260, 254, 0, 0, 0, 0, 0, 92, 92, -270, 259, 0, 255, 263, 295, 0, 242, -297, -231, 0, -215, 0, 266, 254, 0, 0, 0, 0, 92, 0, 0, 0, 0, 297, 302, 304, 0, 0, 0, -174, -50, 0, -231, 0, 0, 0, 0, 236, -297, -231, 0, 236, 0, -174, 0, 0, 302, 0, 236, 0, -231, 0, 0};
    static final short[] yyrindex = {0, 0, 904, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, -63, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -245, 0, 0, 0, 0, 0, 0, 30, 0, 0, 0, 279, -25, 0, 61, 0, 282, -237, 0, 91, 0, 0, 0, 0, 305, 0, 0, 0, 0, 0, 0, 0, -273, 0, 0, 0, 0, 0, -245, -3, 0, 0, 0, 209, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 280, 0, 0, 0, 0, 0, 0, 0, 301, 0, 0, 0, 0, 0, 0, 0, -27, 769, 754, 727, 329, -90, 630, 518, 399, 350, 0, 0, 0, 0, 0, 0, 0, 0, -235, 142, -57, 314, 0, 0, 279, 0, -85, 0, 0, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 826, 0, 0, 0, 0, 305, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 178, 64, 0, 0, 0, 0, -172, 0, 0, -83, -129, 0, 0, 0, -217, 0, 138, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 771, 0, 766, 738, 710, 689, 651, 668, 546, 567, 588, 609, 448, 469, 497, 371, 420, 0, 0, 0, 0, 0, 0, 0, 283, 0, 0, 0, -31, 0, 0, 0, 0, 0, -221, 0, -195, -197, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 305, -137, 0, 0, 273, 0, 0, 4, 63, -230, -229, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -123, 0, 288, 0, 0, 0, 0, 0, 0, 0, -230, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 265, -211, -219, 0, 0, 0, -230, 180, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -230, 0, 0, -206, 0, 0, 0, 0, 0, 0};
    static final short[] yygindex = {0, 2, 523, -155, -87, 0, 14, 0, 0, 0, 446, 445, 447, 449, 444, 298, 292, 264, 307, -84, -163, 0, 12, 17, 322, 0, 0, -170, -208, 0, -146, 101, -72, 332, 0, 0, 0, 0, -2, 540, 541, 0, 0, 0, 495, 0, 0, 0, 544, 0, 0, 0, 440, 0, 0, 0, 0, 0, 0, 0, 483, 0, 0, 382, 0, 0, 0, 0, 502, 571, 584, 0, 616, 604, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 45, 0, 53, 0, 0, 0, 0, 0, 0, 373, 0};
    static final short[] yytable = {12, 46, 139, 261, 163, 159, 160, 210, 23, 164, 165, 218, 162, 65, 219, 176, 8, 20, 176, 149, 163, 255, 21, 136, 136, 136, 322, 214, 28, 41, 48, 28, 193, 18, 167, 71, 29, 49, 215, 29, 247, 139, 322, 58, 274, 326, 216, 162, 72, 216, 264, 217, 22, 136, 217, 163, 136, 79, 136, 256, 136, 139, 68, 136, 132, 132, 263, 28, 194, 168, 204, 225, 29, 18, 207, 136, 139, 85, 209, 7, 139, 58, 66, 89, 90, 225, 150, 226, 153, 323, 214, 28, 167, 161, 85, 136, 139, 325, 211, 29, 139, 215, 289, 132, 3, 323, 242, 243, 244, 216, 45, 91, 303, 139, 217, 132, 4, 5, 292, 286, 93, 339, 170, 144, 171, 94, 153, 168, 253, 46, 213, 266, 297, 139, 170, 132, 327, 155, 95, 96, 97, 98, 99, 100, 101, 102, 22, 310, 311, 104, 105, 106, 107, 108, 109, 110, 319, 168, 338, 177, 215, 144, 177, 6, 7, 343, 221, 41, 330, 168, 75, 54, 185, 214, 216, 155, 348, 178, 221, 224, 51, 89, 90, 290, 291, 280, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 222, 75, 182, 265, 178, 75, 161, 178, 91, 92, 224, 185, 222, 222, 75, 75, 267, 93, 56, 75, 75, 75, 94, 75, 75, 185, 186, 187, 183, 6, 185, 185, 65, 182, 185, 95, 96, 97, 98, 99, 100, 101, 102, 103, 185, 222, 104, 105, 106, 107, 108, 109, 110, 167, 17, 169, 167, 46, 169, 183, 46, 147, 147, 179, 65, 147, 7, 17, 65, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 46, 48, 65, 167, 48, 169, 179, 46, 57, 179, 46, 46, 46, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 317, 46, 46, 6, 320, 46, 48, 293, 336, 294, 48, 48, 69, 305, 74, 46, 308, 84, 227, 227, 227, 227, 227, 227, 227, 227, 227, 227, 227, 173, 48, 48, 185, 68, 48, 342, 76, 174, 329, 47, 48, 89, 90, 218, 48, 328, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 185, 225, 335, 227, 50, 48, 24, 67, 48, 91, 147, 148, 185, 179, 180, 225, 346, 227, 93, 24, 81, 29, 29, 94, 83, 137, 137, 82, 190, 191, 6, 185, 185, 192, 87, 185, 95, 96, 97, 98, 99, 100, 101, 102, 22, 185, 136, 104, 105, 106, 107, 108, 109, 110, 137, 29, 142, 137, 341, 137, 29, 166, 345, 137, 137, 181, 182, 183, 184, 347, 188, 189, 29, 237, 238, 239, 137, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 29, 55, 57, 172, 137, 233, 234, 235, 236, 231, 232, 76, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 240, 241, 175, 6, 7, 176, 195, 208, 177, 178, 197, 45, 198, 199, 217, 201, 200, 209, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 207, 45, 220, 223, 245, 45, 251, 252, 257, 254, 268, 259, 262, 269, 115, 45, 270, 115, 115, 271, 273, 272, 115, 115, 115, 281, 286, 288, 303, 295, 307, 316, 332, 313, 6, 315, 48, 112, 112, 112, 334, 314, 340, 112, 112, 112, 180, 134, 136, 115, 160, 58, 115, 115, 115, 115, 115, 115, 115, 115, 219, 115, 115, 115, 115, 73, 115, 115, 115, 112, 115, 223, 55, 112, 112, 112, 112, 112, 112, 112, 112, 181, 112, 112, 112, 112, 89, 112, 112, 112, 162, 112, 112, 89, 89, 225, 227, 73, 230, 228, 318, 73, 229, 312, 151, 152, 196, 90, 153, 246, 73, 73, 212, 279, 90, 90, 73, 73, 89, 73, 73, 206, 89, 89, 89, 89, 89, 89, 89, 89, 146, 89, 89, 89, 89, 85, 89, 89, 89, 90, 89, 89, 145, 90, 90, 90, 90, 90, 90, 90, 90, 80, 90, 90, 90, 90, 91, 90, 90, 90, 144, 90, 90, 91, 91, 331, 0, 85, 0, 0, 0, 85, 85, 85, 85, 85, 85, 85, 85, 0, 85, 85, 85, 85, 86, 85, 85, 85, 91, 85, 85, 0, 91, 91, 91, 91, 91, 91, 91, 91, 0, 91, 91, 91, 91, 88, 91, 91, 91, 0, 91, 91, 0, 0, 0, 0, 86, 0, 0, 0, 86, 86, 86, 86, 86, 86, 86, 86, 0, 86, 86, 86, 86, 87, 86, 86, 86, 88, 86, 86, 0, 88, 88, 88, 88, 88, 88, 88, 88, 0, 88, 88, 88, 88, 80, 88, 88, 88, 0, 88, 88, 0, 0, 0, 0, 87, 0, 0, 0, 87, 87, 87, 87, 87, 87, 87, 87, 0, 87, 87, 87, 87, 83, 87, 87, 87, 80, 87, 87, 0, 80, 80, 80, 80, 80, 0, 0, 0, 0, 80, 80, 80, 80, 84, 80, 80, 80, 0, 80, 80, 0, 0, 0, 0, 83, 0, 0, 0, 83, 83, 83, 83, 83, 0, 81, 0, 0, 83, 83, 83, 83, 0, 83, 83, 83, 84, 83, 83, 0, 84, 84, 84, 84, 84, 0, 82, 0, 0, 84, 84, 84, 84, 0, 84, 84, 84, 81, 84, 84, 0, 81, 81, 81, 81, 81, 0, 77, 0, 0, 81, 81, 81, 81, 0, 81, 81, 81, 82, 81, 81, 0, 82, 82, 82, 82, 82, 0, 78, 0, 0, 82, 82, 82, 82, 0, 82, 82, 82, 77, 82, 82, 0, 77, 0, 79, 0, 0, 0, 0, 0, 0, 77, 77, 77, 77, 0, 77, 77, 77, 78, 77, 77, 0, 78, 0, 76, 0, 0, 0, 0, 0, 0, 78, 78, 78, 78, 79, 78, 78, 78, 79, 78, 78, 0, 0, 0, 74, 0, 0, 79, 79, 79, 79, 0, 79, 79, 79, 76, 79, 79, 0, 76, 0, 71, 0, 0, 0, 0, 0, 0, 76, 76, 0, 0, 72, 76, 
    76, 76, 74, 76, 76, 0, 74, 0, 0, 0, 0, 0, 0, 0, 69, 74, 74, 0, 0, 71, 0, 74, 74, 71, 74, 74, 70, 0, 0, 67, 72, 68, 71, 71, 72, 0, 0, 0, 71, 0, 0, 71, 71, 72, 72, 0, 69, 0, 0, 72, 69, 0, 72, 72, 0, 0, 0, 0, 70, 69, 69, 67, 70, 68, 0, 67, 0, 68, 69, 69, 0, 70, 70, 0, 0, 67, 0, 68, 0, 0, 70, 70, 0, 67, 67, 68, 68, 112, 112, 0, 0, 0, 112, 112, 112, 0, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 112, 112, 112, 112, 112, 112, 91, 112, 112, 112, 112, 0, 112, 112, 112, 93, 112, 6, 7, 0, 94, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 95, 96, 97, 98, 99, 100, 101, 102, 22, 0, 0, 104, 105, 106, 107, 108, 109, 110, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45, 45};
    static final short[] yycheck = {2, 0, 74, 211, 91, 89, 90, 153, 6, 93, 94, 166, 257, 261, 282, 288, 2, 274, 291, 257, 257, 260, 5, 258, 259, 260, 257, 257, 257, 15, 0, 260, 260, 330, 258, 318, 257, 20, 257, 260, 195, 258, 257, 45, 314, 260, 257, 292, 331, 260, 213, 257, 330, 288, 260, 292, 291, 59, 293, 298, 330, 258, 330, 298, 259, 260, 212, 296, 296, 293, 142, 316, 293, 330, 295, 310, 293, 63, 295, 317, 297, 83, 330, 267, 268, 330, 88, 174, 260, 320, 320, 320, 258, 91, 80, 330, 293, 305, 295, 320, 297, 320, 265, 298, 257, 320, 190, 191, 192, 320, 291, 295, 286, 330, 320, 310, 269, 270, 273, 293, 304, 329, 258, 260, 112, 309, 298, 293, 200, 291, 296, 215, 278, 330, 258, 330, 306, 260, 322, 323, 324, 325, 326, 327, 328, 329, 330, 293, 294, 333, 334, 335, 336, 337, 338, 339, 302, 293, 328, 288, 296, 298, 291, 316, 317, 335, 258, 153, 314, 293, 260, 332, 257, 161, 162, 298, 346, 260, 258, 258, 258, 267, 268, 267, 271, 257, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 293, 292, 260, 296, 288, 296, 292, 291, 295, 296, 258, 297, 293, 293, 305, 306, 296, 304, 330, 310, 311, 312, 309, 314, 315, 301, 302, 303, 260, 316, 316, 317, 260, 291, 320, 322, 323, 324, 325, 326, 327, 328, 329, 330, 330, 293, 333, 334, 335, 336, 337, 338, 339, 257, 318, 257, 260, 257, 260, 291, 260, 287, 288, 260, 292, 291, 317, 331, 296, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 257, 315, 292, 260, 292, 288, 292, 330, 291, 295, 296, 297, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 302, 316, 317, 316, 303, 320, 292, 287, 326, 289, 296, 297, 295, 284, 288, 330, 287, 330, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 306, 316, 317, 257, 330, 320, 334, 297, 314, 309, 257, 258, 267, 268, 296, 330, 308, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 316, 325, 316, 257, 258, 318, 257, 258, 295, 320, 321, 297, 307, 308, 330, 339, 330, 304, 331, 292, 259, 260, 309, 260, 259, 260, 257, 261, 262, 316, 316, 317, 266, 292, 320, 322, 323, 324, 325, 326, 327, 328, 329, 330, 330, 330, 333, 334, 335, 336, 337, 338, 339, 288, 293, 287, 291, 333, 293, 298, 263, 337, 297, 298, 297, 298, 299, 300, 344, 267, 268, 310, 185, 186, 187, 310, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 330, 291, 292, 296, 330, 181, 182, 183, 184, 179, 180, 297, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 188, 189, 305, 316, 317, 311, 291, 320, 312, 310, 297, 297, 258, 258, 296, 330, 260, 330, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 320, 316, 294, 282, 330, 320, 330, 330, 260, 287, 282, 330, 330, 294, 258, 330, 282, 261, 262, 315, 260, 292, 266, 267, 268, 295, 293, 330, 286, 260, 294, 260, 259, 298, 316, 296, 258, 260, 261, 262, 260, 310, 330, 266, 267, 268, 291, 298, 330, 293, 292, 292, 296, 297, 298, 299, 300, 301, 302, 303, 296, 305, 306, 307, 308, 260, 310, 311, 312, 292, 314, 330, 291, 296, 297, 298, 299, 300, 301, 302, 303, 291, 305, 306, 307, 308, 260, 310, 311, 312, 91, 314, 315, 267, 268, 173, 175, 292, 178, 176, 302, 296, 177, 295, 88, 88, 135, 260, 88, 193, 305, 306, 153, 255, 267, 268, 311, 312, 292, 314, 315, 143, 296, 297, 298, 299, 300, 301, 302, 303, 83, 305, 306, 307, 308, 260, 310, 311, 312, 292, 314, 315, 82, 296, 297, 298, 299, 300, 301, 302, 303, 59, 305, 306, 307, 308, 260, 310, 311, 312, 80, 314, 315, 267, 268, 316, -1, 292, -1, -1, -1, 296, 297, 298, 299, 300, 301, 302, 303, -1, 305, 306, 307, 308, 260, 310, 311, 312, 292, 314, 315, -1, 296, 297, 298, 299, 300, 301, 302, 303, -1, 305, 306, 307, 308, 260, 310, 311, 312, -1, 314, 315, -1, -1, -1, -1, 292, -1, -1, -1, 296, 297, 298, 299, 300, 301, 302, 303, -1, 305, 306, 307, 308, 260, 310, 311, 312, 292, 314, 315, -1, 296, 297, 298, 299, 300, 301, 302, 303, -1, 305, 306, 307, 308, 260, 310, 311, 312, -1, 314, 315, -1, -1, -1, -1, 292, -1, -1, -1, 296, 297, 298, 299, 300, 301, 302, 303, -1, 305, 306, 307, 308, 260, 310, 311, 312, 292, 314, 315, -1, 296, 297, 298, 299, 300, -1, -1, -1, -1, 305, 306, 307, 308, 260, 310, 311, 312, -1, 314, 315, -1, -1, -1, -1, 292, -1, -1, -1, 296, 297, 298, 299, 300, -1, 260, -1, -1, 305, 306, 307, 308, -1, 310, 311, 312, 292, 314, 315, -1, 296, 297, 298, 299, 300, -1, 260, -1, -1, 305, 306, 307, 308, -1, 310, 311, 312, 292, 314, 315, -1, 296, 297, 298, 299, 300, -1, 260, -1, -1, 305, 306, 307, 308, -1, 310, 311, 312, 292, 314, 315, -1, 296, 297, 298, 299, 300, -1, 260, -1, -1, 305, 306, 307, 308, -1, 310, 311, 312, 292, 314, 315, -1, 296, -1, 260, -1, -1, -1, -1, -1, -1, 305, 306, 307, 308, -1, 310, 311, 312, 292, 314, 315, -1, 296, -1, 260, -1, -1, -1, -1, -1, -1, 305, 306, 307, 308, 292, 310, 311, 312, 296, 314, 315, -1, -1, -1, 260, -1, -1, 305, 306, 307, 308, -1, 310, 311, 312, 292, 314, 315, -1, 296, -1, 260, -1, -1, -1, -1, -1, -1, 305, 306, -1, -1, 260, 310, 
    311, 312, 292, 314, 315, -1, 296, -1, -1, -1, -1, -1, -1, -1, 260, 305, 306, -1, -1, 292, -1, 311, 312, 296, 314, 315, 260, -1, -1, 260, 292, 260, 305, 306, 296, -1, -1, -1, 311, -1, -1, 314, 315, 305, 306, -1, 292, -1, -1, 311, 296, -1, 314, 315, -1, -1, -1, -1, 292, 305, 306, 292, 296, 292, -1, 296, -1, 296, 314, 315, -1, 305, 306, -1, -1, 306, -1, 306, -1, -1, 314, 315, -1, 314, 315, 314, 315, 261, 262, -1, -1, -1, 266, 267, 268, -1, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 297, 298, 299, 300, 301, 302, 303, 295, 305, 306, 307, 308, -1, 310, 311, 312, 304, 314, 316, 317, -1, 309, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 322, 323, 324, 325, 326, 327, 328, 329, 330, -1, -1, 333, 334, 335, 336, 337, 338, 339, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285};
    static final String[] yyname = {"end-of-file", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "SEMI", "DOT", "DOTDOTDOT", "COMMA", "STAR", "PERCENT", "EQUALS", "ANNOSTRING", "ANNOCHAR", "SLASH", "PLUS", "MINUS", "PACKAGE", "IMPORT", "PUBLIC", "PROTECTED", "PRIVATE", "STATIC", "FINAL", "ABSTRACT", "NATIVE", "STRICTFP", "SYNCHRONIZED", "TRANSIENT", "VOLATILE", "CLASS", "INTERFACE", "ENUM", "ANNOINTERFACE", "THROWS", "EXTENDS", "IMPLEMENTS", "SUPER", "DEFAULT", "BRACEOPEN", "BRACECLOSE", "SQUAREOPEN", "SQUARECLOSE", "PARENOPEN", "PARENCLOSE", "LESSTHAN", "GREATERTHAN", "LESSEQUALS", "GREATEREQUALS", "LESSTHAN2", "GREATERTHAN2", "GREATERTHAN3", "EXCLAMATION", "AMPERSAND2", "VERTLINE2", "EQUALS2", "NOTEQUALS", "TILDE", "AMPERSAND", "VERTLINE", "CIRCUMFLEX", "VOID", "QUERY", "COLON", "AT", "JAVADOCSTART", "JAVADOCEND", "JAVADOCEOL", "CODEBLOCK", "PARENBLOCK", "BYTE", "SHORT", "INT", "LONG", "CHAR", "FLOAT", "DOUBLE", "BOOLEAN", "IDENTIFIER", "JAVADOCTAG", "JAVADOCLINE", "BOOLEAN_LITERAL", "INTEGER_LITERAL", "LONG_LITERAL", "FLOAT_LITERAL", "DOUBLE_LITERAL", "CHAR_LITERAL", "STRING_LITERAL"};
    static final String[] yyrule = {"$accept : file", "file :", "$$1 :", "file : file $$1 filepart", "filepart : annotation", "filepart : package", "filepart : import", "filepart : javadoc", "filepart : class", "filepart : enum", "filepart : SEMI", "package : PACKAGE fullidentifier SEMI", "import : IMPORT fullidentifier SEMI", "import : IMPORT STATIC fullidentifier SEMI", "javadoclist : javadoc", "javadoclist : javadoclist javadoc", "javadoc : JAVADOCSTART javadocdescription javadoctags JAVADOCEND", "javadocdescription : javadoctokens", "javadoctokens :", "javadoctokens : javadoctokens javadoctoken", "javadoctoken : JAVADOCLINE", "javadoctags :", "javadoctags : javadoctags javadoctag", "$$2 :", "javadoctag : JAVADOCTAG $$2 javadoctokens", "fullidentifier : IDENTIFIER", "fullidentifier : fullidentifier DOT IDENTIFIER", "fullidentifier : fullidentifier DOT STAR", "arrayidentifier : IDENTIFIER dimensions", "dimensions :", "dimensions : dimensions SQUAREOPEN SQUARECLOSE", "modifier : PUBLIC", "modifier : PROTECTED", "modifier : PRIVATE", "modifier : STATIC", "modifier : FINAL", "modifier : ABSTRACT", "modifier : NATIVE", "modifier : SYNCHRONIZED", "modifier : VOLATILE", "modifier : TRANSIENT", "modifier : STRICTFP", "modifiers : modifiers modifier", "modifiers : modifiers annotation", "modifiers : modifiers javadoc", "modifiers :", "$$3 :", "annotation : AT name $$3 annotationParensOpt", "annotationParensOpt :", "annotationParensOpt : PARENOPEN value PARENCLOSE", "annotationParensOpt : PARENOPEN valuePairs PARENCLOSE", "annotationParensOpt : PARENOPEN PARENCLOSE", "valuePairs : valuePair", "valuePairs : valuePairs COMMA valuePair", "valuePair : IDENTIFIER EQUALS value", "$$4 :", "arrayInitializer : $$4 BRACEOPEN valuesOpt BRACECLOSE", "valuesOpt :", "valuesOpt : values", "values : value", "values : values COMMA value", "value : expression", "value : annotation", "value : arrayInitializer", "expression : conditionalExpression", "conditionalExpression : conditionalOrExpression", "conditionalExpression : conditionalOrExpression QUERY expression COLON expression", "conditionalOrExpression : conditionalAndExpression", "conditionalOrExpression : conditionalOrExpression VERTLINE2 conditionalAndExpression", "conditionalAndExpression : inclusiveOrExpression", "conditionalAndExpression : conditionalAndExpression AMPERSAND2 inclusiveOrExpression", "inclusiveOrExpression : exclusiveOrExpression", "inclusiveOrExpression : inclusiveOrExpression VERTLINE exclusiveOrExpression", "exclusiveOrExpression : andExpression", "exclusiveOrExpression : exclusiveOrExpression CIRCUMFLEX andExpression", "andExpression : equalityExpression", "andExpression : andExpression AMPERSAND equalityExpression", "equalityExpression : relationalExpression", "equalityExpression : equalityExpression EQUALS2 relationalExpression", "equalityExpression : equalityExpression NOTEQUALS relationalExpression", "relationalExpression : shiftExpression", "relationalExpression : relationalExpression LESSEQUALS shiftExpression", "relationalExpression : relationalExpression GREATEREQUALS shiftExpression", "relationalExpression : relationalExpression LESSTHAN shiftExpression", "relationalExpression : relationalExpression GREATERTHAN shiftExpression", "shiftExpression : additiveExpression", "shiftExpression : shiftExpression LESSTHAN2 additiveExpression", "shiftExpression : shiftExpression GREATERTHAN3 additiveExpression", "shiftExpression : shiftExpression GREATERTHAN2 additiveExpression", "additiveExpression : multiplicativeExpression", "additiveExpression : additiveExpression PLUS multiplicativeExpression", "additiveExpression : additiveExpression MINUS multiplicativeExpression", "multiplicativeExpression : unaryExpression", "multiplicativeExpression : multiplicativeExpression STAR unaryExpression", "multiplicativeExpression : multiplicativeExpression SLASH unaryExpression", "multiplicativeExpression : multiplicativeExpression PERCENT unaryExpression", "unaryExpression : PLUS unaryExpression", "unaryExpression : MINUS unaryExpression", "unaryExpression : unaryExpressionNotPlusMinus", "unaryExpressionNotPlusMinus : TILDE unaryExpression", "unaryExpressionNotPlusMinus : EXCLAMATION unaryExpression", "unaryExpressionNotPlusMinus : primary", "primary : PARENOPEN primitiveType PARENCLOSE unaryExpression", "primary : PARENOPEN primitiveType dims PARENCLOSE unaryExpression", "primary : PARENOPEN name dims PARENCLOSE unaryExpressionNotPlusMinus", "primary : PARENOPEN name PARENCLOSE unaryExpressionNotPlusMinus", "primary : PARENOPEN expression PARENCLOSE", "primary : literal", "primary : primitiveType dims DOT CLASS", "primary : primitiveType DOT CLASS", "primary : name DOT CLASS", "primary : name dims DOT CLASS", "primary : name", "dims : SQUAREOPEN SQUARECLOSE", "dims : dims SQUAREOPEN SQUARECLOSE", "name : IDENTIFIER", "name : name DOT IDENTIFIER", "literal : DOUBLE_LITERAL", "literal : FLOAT_LITERAL", "literal : LONG_LITERAL", "literal : INTEGER_LITERAL", "literal : BOOLEAN_LITERAL", "literal : CHAR_LITERAL", "literal : STRING_LITERAL", "primitiveType : BOOLEAN", "primitiveType : BYTE", "primitiveType : SHORT", "primitiveType : INT", "primitiveType : LONG", "primitiveType : CHAR", "primitiveType : FLOAT", "primitiveType : DOUBLE", "type : classtype dimensions", "$$5 :", "$$6 :", "classtype : typedeclspecifier LESSTHAN $$5 typearglist $$6 GREATERTHAN", "classtype : typedeclspecifier", "typedeclspecifier : typename", "typedeclspecifier : classtype DOT IDENTIFIER", "typename : IDENTIFIER", "typename : typename DOT IDENTIFIER", "typearglist : typearg", "typearglist : typearglist COMMA typearg", "typearg : type", "typearg : QUERY", "typearg : QUERY EXTENDS type", "typearg : QUERY SUPER type", "opt_typeparams :", "opt_typeparams : typeparams", "$$7 :", "typeparams : LESSTHAN $$7 typeparamlist GREATERTHAN", "typeparamlist : typeparam", "typeparamlist : typeparamlist COMMA typeparam", "typeparam : IDENTIFIER", "$$8 :", "typeparam : IDENTIFIER EXTENDS $$8 typeboundlist", "typeboundlist : type", "typeboundlist : typeboundlist AMPERSAND type", "enum : enum_definition BRACEOPEN enum_body BRACECLOSE", "enum_definition : modifiers ENUM IDENTIFIER opt_implements", "enum_body : enum_values", "enum_body : enum_values SEMI members", "enum_values :", "enum_values : enum_value", "enum_values : enum_value COMMA enum_values", "enum_value : javadoclist opt_annotations enum_constructor", "enum_value : opt_annotations enum_constructor", "enum_constructor : IDENTIFIER", "enum_constructor : IDENTIFIER CODEBLOCK", "enum_constructor : IDENTIFIER PARENBLOCK", "enum_constructor : IDENTIFIER PARENBLOCK CODEBLOCK", "class : classdefinition BRACEOPEN members BRACECLOSE", "classdefinition : modifiers classorinterface IDENTIFIER opt_typeparams opt_extends opt_implements", "classorinterface : CLASS", "classorinterface : INTERFACE", "classorinterface : ANNOINTERFACE", "opt_extends :", "opt_extends : EXTENDS extendslist", "extendslist : classtype", "extendslist : extendslist COMMA classtype", "opt_implements :", "opt_implements : IMPLEMENTS implementslist", "implementslist : classtype", "implementslist : implementslist COMMA classtype", "members :", "$$9 :", "members : members $$9 member", "member : javadoc", "member : fields", "member : method", "member : constructor", "member : static_block", "member : class", "member : enum", "member : SEMI", "memberend : SEMI", "memberend : CODEBLOCK", "static_block : modifiers CODEBLOCK", "$$10 :", "fields : modifiers type arrayidentifier $$10 extrafields memberend", "extrafields :", "$$11 :", "extrafields : extrafields COMMA $$11 arrayidentifier", "$$12 :", "extrafields : extrafields COMMA javadoc $$12 arrayidentifier", "$$13 :", "method : modifiers typeparams type IDENTIFIER $$13 methoddef dimensions opt_exceptions memberend", "$$14 :", "method : modifiers type IDENTIFIER $$14 methoddef dimensions opt_exceptions memberend", "$$15 :", "constructor : modifiers IDENTIFIER $$15 methoddef opt_exceptions memberend", "$$16 :", "constructor : modifiers typeparams IDENTIFIER $$16 methoddef opt_exceptions memberend", "methoddef : PARENOPEN opt_params PARENCLOSE", "opt_exceptions :", "opt_exceptions : THROWS exceptionlist", "exceptionlist : fullidentifier", "exceptionlist : exceptionlist COMMA fullidentifier", "opt_params :", "opt_params : paramlist", "paramlist : param", "paramlist : paramlist COMMA param", "param : opt_parammodifiers type varargs arrayidentifier", "varargs :", "varargs : DOTDOTDOT", "opt_annotations :", "opt_annotations : opt_annotations annotation", "opt_parammodifiers :", "opt_parammodifiers : opt_parammodifiers modifier", "opt_parammodifiers : opt_parammodifiers annotation"};
    private StringBuffer textBuffer = new StringBuffer();
    private ClassDef cls = new ClassDef();
    private MethodDef mth = new MethodDef();
    private List typeParams = new ArrayList();
    private List annotationStack = new ArrayList();
    private Annotation annotation = null;
    private List annoValueListStack = new ArrayList();
    private List annoValueList = null;
    private FieldDef param = new FieldDef();
    private Set modifiers = new HashSet();
    private Stack typeStack = new Stack();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.hamcrest.generator.qdox.parser.impl.Parser$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/impl/Parser$1.class */
    public static class AnonymousClass1 {
    }

    void debug(String msg) {
        if (this.yydebug) {
            System.out.println(msg);
        }
    }

    void state_push(int state) {
        if (this.stateptr >= 500) {
            return;
        }
        int[] iArr = this.statestk;
        int i = this.stateptr + 1;
        this.stateptr = i;
        iArr[i] = state;
        if (this.stateptr > this.statemax) {
            this.statemax = state;
            this.stateptrmax = this.stateptr;
        }
    }

    int state_pop() {
        if (this.stateptr < 0) {
            return -1;
        }
        int[] iArr = this.statestk;
        int i = this.stateptr;
        this.stateptr = i - 1;
        return iArr[i];
    }

    void state_drop(int cnt) {
        int ptr = this.stateptr - cnt;
        if (ptr < 0) {
            return;
        }
        this.stateptr = ptr;
    }

    int state_peek(int relative) {
        int ptr = this.stateptr - relative;
        if (ptr < 0) {
            return -1;
        }
        return this.statestk[ptr];
    }

    boolean init_stacks() {
        this.statestk = new int[500];
        this.stateptr = -1;
        this.statemax = -1;
        this.stateptrmax = -1;
        val_init();
        return true;
    }

    void dump_stacks(int count) {
        System.out.println(new StringBuffer().append("=index==state====value=     s:").append(this.stateptr).append("  v:").append(this.valptr).toString());
        for (int i = 0; i < count; i++) {
            System.out.println(new StringBuffer().append(Instruction.argsep).append(i).append(ASTNode.TAB).append(this.statestk[i]).append("      ").append(this.valstk[i]).toString());
        }
        System.out.println("======================");
    }

    void val_init() {
        this.valstk = new Value[500];
        this.yyval = new Value(this, null);
        this.yylval = new Value(this, null);
        this.valptr = -1;
    }

    void val_push(Value val) {
        if (this.valptr >= 500) {
            return;
        }
        Value[] valueArr = this.valstk;
        int i = this.valptr + 1;
        this.valptr = i;
        valueArr[i] = val;
    }

    Value val_pop() {
        if (this.valptr < 0) {
            return null;
        }
        Value[] valueArr = this.valstk;
        int i = this.valptr;
        this.valptr = i - 1;
        return valueArr[i];
    }

    void val_drop(int cnt) {
        int ptr = this.valptr - cnt;
        if (ptr < 0) {
            return;
        }
        this.valptr = ptr;
    }

    Value val_peek(int relative) {
        int ptr = this.valptr - relative;
        if (ptr < 0) {
            return null;
        }
        return this.valstk[ptr];
    }

    private void appendToBuffer(String word) {
        if (this.textBuffer.length() > 0) {
            char lastChar = this.textBuffer.charAt(this.textBuffer.length() - 1);
            if (!Character.isWhitespace(lastChar)) {
                this.textBuffer.append(' ');
            }
        }
        this.textBuffer.append(word);
    }

    private String buffer() {
        String result = this.textBuffer.toString().trim();
        this.textBuffer.setLength(0);
        return result;
    }

    public Parser(Lexer lexer, Builder builder) {
        this.lexer = lexer;
        this.builder = builder;
    }

    public void setDebugParser(boolean debug) {
        this.yydebug = debug;
    }

    public void setDebugLexer(boolean debug) {
        this.debugLexer = debug;
    }

    public boolean parse() {
        return yyparse() == 0;
    }

    private int yylex() {
        try {
            int result = this.lexer.lex();
            this.yylval = new Value(this, null);
            this.yylval.sval = this.lexer.text();
            if (this.debugLexer) {
                System.err.println(new StringBuffer().append("Token: ").append(yyname[result]).append(" \"").append(this.yylval.sval).append("\"").toString());
            }
            return result;
        } catch (IOException e) {
            return 0;
        }
    }

    private void yyerror(String msg) {
        throw new ParseException(msg, this.lexer.getLine(), this.lexer.getColumn());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/parser/impl/Parser$Value.class */
    public class Value {
        Object oval;
        String sval;
        int ival;
        boolean bval;
        TypeDef type;
        AnnotationValue annoval;
        private final Parser this$0;

        private Value(Parser parser) {
            this.this$0 = parser;
        }

        Value(Parser x0, AnonymousClass1 x1) {
            this(x0);
        }
    }

    private void makeField(TypeDef field, String body) {
        FieldDef fd = new FieldDef();
        fd.lineNumber = this.line;
        fd.modifiers.addAll(this.modifiers);
        fd.name = field.name;
        fd.type = this.fieldType;
        fd.dimensions = field.dimensions;
        fd.body = body;
        this.builder.addField(fd);
    }

    private String convertString(String str) {
        StringBuffer buf = new StringBuffer();
        boolean escaped = false;
        int unicode = 0;
        int value = 0;
        int octal = 0;
        boolean consumed = false;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (octal > 0) {
                if (value >= 48 && value <= 55) {
                    value = (value << 3) | Character.digit(ch, 8);
                    octal--;
                    consumed = true;
                } else {
                    octal = 0;
                }
                if (octal == 0) {
                    buf.append((char) value);
                    value = 0;
                }
            }
            if (!consumed) {
                if (unicode > 0) {
                    value = (value << 4) | Character.digit(ch, 16);
                    unicode--;
                    if (unicode == 0) {
                        buf.append((char) value);
                        value = 0;
                    }
                } else if (ch == '\\') {
                    escaped = true;
                } else if (escaped) {
                    if (ch == 'u' || ch == 'U') {
                        unicode = 4;
                    } else if (ch >= '0' && ch <= '7') {
                        octal = ch > '3' ? 1 : 2;
                        value = Character.digit(ch, 8);
                    } else {
                        switch (ch) {
                            case '\"':
                                buf.append('\"');
                                break;
                            case '\'':
                                buf.append('\'');
                                break;
                            case '\\':
                                buf.append('\\');
                                break;
                            case 'b':
                                buf.append('\b');
                                break;
                            case 'f':
                                buf.append('\f');
                                break;
                            case 'n':
                                buf.append('\n');
                                break;
                            case 'r':
                                buf.append('\r');
                                break;
                            case 't':
                                buf.append('\t');
                                break;
                            default:
                                yyerror(new StringBuffer().append("Illegal escape character '").append(ch).append("'").toString());
                                break;
                        }
                    }
                    escaped = false;
                } else {
                    buf.append(ch);
                }
            }
        }
        return buf.toString();
    }

    private Boolean toBoolean(String str) {
        return new Boolean(str.trim());
    }

    private Integer toInteger(String str) {
        Integer result;
        String str2 = str.trim();
        if (str2.startsWith("0x") || str2.startsWith("0X")) {
            result = new Integer(Integer.parseInt(str2.substring(2), 16));
        } else if (str2.length() > 1 && str2.startsWith(WorkException.UNDEFINED)) {
            result = new Integer(Integer.parseInt(str2.substring(1), 8));
        } else {
            result = new Integer(str2);
        }
        return result;
    }

    private Long toLong(String str) {
        Long result;
        String str2 = str.trim();
        if (!str2.endsWith("l") && !str2.endsWith("L")) {
            yyerror("Long literal must end with 'l' or 'L'.");
        }
        int len = str2.length() - 1;
        if (str2.startsWith("0x") || str2.startsWith("0X")) {
            result = new Long(Long.parseLong(str2.substring(2, len), 16));
        } else if (str2.startsWith(WorkException.UNDEFINED)) {
            result = new Long(Long.parseLong(str2.substring(1, len), 8));
        } else {
            result = new Long(str2.substring(0, len));
        }
        return result;
    }

    private Float toFloat(String str) {
        return new Float(str.trim());
    }

    private Double toDouble(String str) {
        String str2 = str.trim();
        if (!str2.endsWith("d") && !str2.endsWith("D")) {
            yyerror("Double literal must end with 'd' or 'D'.");
        }
        return new Double(str2.substring(0, str2.length() - 1));
    }

    private Character toChar(String str) {
        String str2 = str.trim();
        if (!str2.startsWith("'") && !str2.endsWith("'")) {
            yyerror("Character must be single quoted.");
        }
        String str22 = convertString(str2.substring(1, str2.length() - 1));
        if (str22.length() != 1) {
            yyerror("Only one character allowed in character constants.");
        }
        return new Character(str22.charAt(0));
    }

    private String toString(String str) {
        String str2 = str.trim();
        if (str2.length() < 2 && !str2.startsWith("\"") && !str2.endsWith("\"")) {
            yyerror("String must be double quoted.");
        }
        String str22 = convertString(str2.substring(1, str2.length() - 1));
        return str22;
    }

    void yylexdebug(int state, int ch) {
        String s = null;
        if (ch < 0) {
            ch = 0;
        }
        if (ch <= 339) {
            s = yyname[ch];
        }
        if (s == null) {
            s = "illegal-symbol";
        }
        debug(new StringBuffer().append("state ").append(state).append(", reading ").append(ch).append(" (").append(s).append(")").toString());
    }

    /* JADX WARN: Code restructure failed: missing block: B:101:0x03d7, code lost:
        if (r8.yym <= 0) goto L46;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x03da, code lost:
        r8.yyval = val_peek(r8.yym - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x03ec, code lost:
        switch(r8.yyn) {
            case 2: goto L48;
            case 3: goto L216;
            case 4: goto L49;
            case 5: goto L216;
            case 6: goto L216;
            case 7: goto L216;
            case 8: goto L216;
            case 9: goto L216;
            case 10: goto L216;
            case 11: goto L50;
            case 12: goto L51;
            case 13: goto L52;
            case 14: goto L216;
            case 15: goto L216;
            case 16: goto L216;
            case 17: goto L53;
            case 18: goto L216;
            case 19: goto L216;
            case 20: goto L54;
            case 21: goto L216;
            case 22: goto L216;
            case 23: goto L55;
            case 24: goto L56;
            case 25: goto L57;
            case 26: goto L58;
            case 27: goto L59;
            case 28: goto L60;
            case 29: goto L61;
            case 30: goto L62;
            case 31: goto L63;
            case 32: goto L64;
            case 33: goto L65;
            case 34: goto L66;
            case 35: goto L67;
            case 36: goto L68;
            case 37: goto L69;
            case 38: goto L70;
            case 39: goto L71;
            case 40: goto L72;
            case 41: goto L73;
            case 42: goto L74;
            case 43: goto L75;
            case 44: goto L216;
            case 45: goto L216;
            case 46: goto L76;
            case 47: goto L77;
            case 48: goto L216;
            case 49: goto L78;
            case 50: goto L216;
            case 51: goto L216;
            case 52: goto L216;
            case 53: goto L216;
            case 54: goto L79;
            case 55: goto L80;
            case 56: goto L81;
            case 57: goto L216;
            case 58: goto L216;
            case 59: goto L82;
            case 60: goto L83;
            case 61: goto L84;
            case 62: goto L85;
            case 63: goto L86;
            case 64: goto L87;
            case 65: goto L88;
            case 66: goto L89;
            case 67: goto L90;
            case 68: goto L91;
            case 69: goto L92;
            case 70: goto L93;
            case 71: goto L94;
            case 72: goto L95;
            case 73: goto L96;
            case 74: goto L97;
            case 75: goto L98;
            case 76: goto L99;
            case 77: goto L100;
            case 78: goto L101;
            case 79: goto L102;
            case 80: goto L103;
            case 81: goto L104;
            case 82: goto L105;
            case 83: goto L106;
            case 84: goto L107;
            case 85: goto L108;
            case 86: goto L109;
            case 87: goto L110;
            case 88: goto L111;
            case 89: goto L112;
            case 90: goto L113;
            case 91: goto L114;
            case 92: goto L115;
            case 93: goto L116;
            case 94: goto L117;
            case 95: goto L118;
            case 96: goto L119;
            case 97: goto L120;
            case 98: goto L121;
            case 99: goto L122;
            case 100: goto L123;
            case 101: goto L216;
            case 102: goto L124;
            case 103: goto L125;
            case 104: goto L126;
            case 105: goto L127;
            case 106: goto L128;
            case 107: goto L129;
            case 108: goto L130;
            case 109: goto L131;
            case 110: goto L132;
            case 111: goto L133;
            case 112: goto L134;
            case 113: goto L135;
            case 114: goto L136;
            case 115: goto L137;
            case 116: goto L138;
            case 117: goto L139;
            case 118: goto L140;
            case 119: goto L141;
            case 120: goto L142;
            case 121: goto L143;
            case 122: goto L144;
            case 123: goto L145;
            case 124: goto L146;
            case 125: goto L147;
            case 126: goto L148;
            case 127: goto L149;
            case 128: goto L150;
            case 129: goto L151;
            case 130: goto L152;
            case 131: goto L153;
            case 132: goto L154;
            case 133: goto L155;
            case 134: goto L156;
            case 135: goto L157;
            case 136: goto L158;
            case 137: goto L159;
            case 138: goto L160;
            case 139: goto L161;
            case 140: goto L162;
            case 141: goto L163;
            case 142: goto L164;
            case 143: goto L165;
            case 144: goto L166;
            case 145: goto L167;
            case 146: goto L168;
            case 147: goto L216;
            case 148: goto L216;
            case 149: goto L169;
            case 150: goto L216;
            case 151: goto L216;
            case 152: goto L216;
            case 153: goto L170;
            case 154: goto L171;
            case 155: goto L172;
            case 156: goto L173;
            case 157: goto L174;
            case 158: goto L175;
            case 159: goto L176;
            case 160: goto L216;
            case 161: goto L216;
            case 162: goto L216;
            case 163: goto L216;
            case 164: goto L216;
            case 165: goto L216;
            case 166: goto L216;
            case 167: goto L177;
            case 168: goto L178;
            case 169: goto L179;
            case 170: goto L180;
            case 171: goto L181;
            case 172: goto L182;
            case 173: goto L183;
            case 174: goto L184;
            case 175: goto L185;
            case 176: goto L216;
            case 177: goto L216;
            case 178: goto L186;
            case 179: goto L187;
            case 180: goto L216;
            case 181: goto L216;
            case 182: goto L188;
            case 183: goto L189;
            case 184: goto L216;
            case 185: goto L190;
            case 186: goto L216;
            case 187: goto L216;
            case 188: goto L216;
            case 189: goto L216;
            case 190: goto L216;
            case 191: goto L216;
            case 192: goto L216;
            case 193: goto L216;
            case 194: goto L216;
            case 195: goto L191;
            case 196: goto L192;
            case 197: goto L193;
            case 198: goto L194;
            case 199: goto L195;
            case 200: goto L216;
            case 201: goto L196;
            case 202: goto L197;
            case 203: goto L198;
            case 204: goto L199;
            case 205: goto L200;
            case 206: goto L201;
            case 207: goto L202;
            case 208: goto L203;
            case 209: goto L204;
            case 210: goto L205;
            case 211: goto L206;
            case 212: goto L207;
            case 213: goto L216;
            case 214: goto L216;
            case 215: goto L216;
            case 216: goto L208;
            case 217: goto L209;
            case 218: goto L216;
            case 219: goto L216;
            case 220: goto L216;
            case 221: goto L216;
            case 222: goto L210;
            case 223: goto L211;
            case 224: goto L212;
            case 225: goto L216;
            case 226: goto L213;
            case 227: goto L216;
            case 228: goto L214;
            case 229: goto L215;
            default: goto L216;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x078c, code lost:
        r8.line = r8.lexer.getLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x079c, code lost:
        r8.builder.addAnnotation((org.hamcrest.generator.qdox.model.Annotation) val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:107:0x07b3, code lost:
        r8.builder.addPackage(new org.hamcrest.generator.qdox.parser.structs.PackageDef(val_peek(1).sval, r8.line));
     */
    /* JADX WARN: Code restructure failed: missing block: B:108:0x07d2, code lost:
        r8.builder.addImport(val_peek(1).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:109:0x07e6, code lost:
        r8.builder.addImport(val_peek(1).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:110:0x07fa, code lost:
        r8.builder.addJavaDoc(buffer());
     */
    /* JADX WARN: Code restructure failed: missing block: B:111:0x080a, code lost:
        appendToBuffer(val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:112:0x0819, code lost:
        r8.line = r8.lexer.getLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:0x0829, code lost:
        r8.builder.addJavaDocTag(new org.hamcrest.generator.qdox.parser.structs.TagDef(val_peek(2).sval.substring(1), buffer(), r8.line));
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x0850, code lost:
        r8.yyval.sval = val_peek(0).sval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x0862, code lost:
        r8.yyval.sval = new java.lang.StringBuffer().append(val_peek(2).sval).append('.').append(val_peek(0).sval).toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:116:0x0891, code lost:
        r8.yyval.sval = new java.lang.StringBuffer().append(val_peek(2).sval).append(".*").toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:117:0x08b6, code lost:
        r8.yyval.type = new org.hamcrest.generator.qdox.parser.structs.TypeDef(val_peek(1).sval, val_peek(0).ival);
     */
    /* JADX WARN: Code restructure failed: missing block: B:118:0x08d7, code lost:
        r8.yyval.ival = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:119:0x08e2, code lost:
        r8.yyval.ival = val_peek(2).ival + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:120:0x08f6, code lost:
        r8.yyval.sval = soot.jimple.Jimple.PUBLIC;
     */
    /* JADX WARN: Code restructure failed: missing block: B:121:0x0903, code lost:
        r8.yyval.sval = soot.jimple.Jimple.PROTECTED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:122:0x0910, code lost:
        r8.yyval.sval = soot.jimple.Jimple.PRIVATE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:123:0x091d, code lost:
        r8.yyval.sval = soot.jimple.Jimple.STATIC;
     */
    /* JADX WARN: Code restructure failed: missing block: B:124:0x092a, code lost:
        r8.yyval.sval = soot.jimple.Jimple.FINAL;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x0937, code lost:
        r8.yyval.sval = soot.jimple.Jimple.ABSTRACT;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x0944, code lost:
        r8.yyval.sval = soot.jimple.Jimple.NATIVE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x0951, code lost:
        r8.yyval.sval = soot.jimple.Jimple.SYNCHRONIZED;
     */
    /* JADX WARN: Code restructure failed: missing block: B:128:0x095e, code lost:
        r8.yyval.sval = soot.jimple.Jimple.VOLATILE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x096b, code lost:
        r8.yyval.sval = soot.jimple.Jimple.TRANSIENT;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x0978, code lost:
        r8.yyval.sval = soot.jimple.Jimple.STRICTFP;
     */
    /* JADX WARN: Code restructure failed: missing block: B:131:0x0985, code lost:
        r8.modifiers.add(val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x099a, code lost:
        r8.builder.addAnnotation((org.hamcrest.generator.qdox.model.Annotation) val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x09b1, code lost:
        r8.annotationStack.add(r8.annotation);
        r8.annotation = new org.hamcrest.generator.qdox.model.Annotation(r8.builder.createType(val_peek(0).sval, 0), r8.lexer.getLine());
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x09e8, code lost:
        r8.yyval.annoval = r8.annotation;
        r8.annotation = (org.hamcrest.generator.qdox.model.Annotation) r8.annotationStack.remove(r8.annotationStack.size() - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x0a11, code lost:
        r8.annotation.setProperty("value", val_peek(1).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x0a26, code lost:
        r8.annotation.setProperty(val_peek(2).sval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0a40, code lost:
        r8.annoValueListStack.add(r8.annoValueList);
        r8.annoValueList = new java.util.ArrayList();
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x0a5c, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationValueList(r8.annoValueList);
        r8.annoValueList = (java.util.List) r8.annoValueListStack.remove(r8.annoValueListStack.size() - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x0a8c, code lost:
        r8.annoValueList.add(val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:140:0x0aa1, code lost:
        r8.annoValueList.add(val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x0ab6, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x0ac8, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:143:0x0ada, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x0aec, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x0afe, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:146:0x0b10, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationQuery(val_peek(4).annoval, val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x0b39, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x0b4b, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationLogicalOr(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x0b6c, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x0b7e, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationLogicalAnd(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:151:0x0b9f, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x0bb1, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationOr(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x0bd2, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:154:0x0be4, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationExclusiveOr(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0c05, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x0c17, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationAnd(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:157:0x0c38, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x0c4a, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationEquals(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x0c6b, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationNotEquals(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x0c8c, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x0c9e, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationLessEquals(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0cbf, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationGreaterEquals(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0ce0, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationLessThan(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:164:0x0d01, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationGreaterThan(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x0d22, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x0d34, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationShiftLeft(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0d55, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationUnsignedShiftRight(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0d76, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationShiftRight(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:169:0x0d97, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0da9, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationAdd(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:171:0x0dca, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationSubtract(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x0deb, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0dfd, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationMultiply(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:174:0x0e1e, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationDivide(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0e3f, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationRemainder(val_peek(2).annoval, val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x0e60, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationPlusSign(val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x0e79, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationMinusSign(val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x0e92, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x0ea4, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationNot(val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x0ebd, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationLogicalNot(val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:181:0x0ed6, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationCast(r8.builder.createType(val_peek(2).sval, 0), val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x0f01, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationCast(r8.builder.createType(val_peek(3).sval, val_peek(2).ival), val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:183:0x0f33, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationCast(r8.builder.createType(val_peek(3).sval, val_peek(2).ival), val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x0f65, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationCast(r8.builder.createType(val_peek(2).sval, 0), val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x0f90, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationParenExpression(val_peek(1).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:186:0x0fa9, code lost:
        r8.yyval.annoval = val_peek(0).annoval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x0fbb, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationTypeRef(r8.builder.createType(val_peek(3).sval, 0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x0fde, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationTypeRef(r8.builder.createType(val_peek(2).sval, 0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x1001, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationTypeRef(r8.builder.createType(val_peek(2).sval, 0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x1024, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationTypeRef(r8.builder.createType(val_peek(3).sval, 0));
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x1047, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationFieldRef(val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:192:0x1060, code lost:
        r8.yyval.ival = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:193:0x106b, code lost:
        r8.yyval.ival = val_peek(2).ival + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:194:0x107f, code lost:
        r8.yyval.sval = val_peek(0).sval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:195:0x1091, code lost:
        r8.yyval.sval = new java.lang.StringBuffer().append(val_peek(2).sval).append(".").append(val_peek(0).sval).toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:196:0x10c1, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationConstant(toDouble(val_peek(0).sval), val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:197:0x10e6, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationConstant(toFloat(val_peek(0).sval), val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:198:0x110b, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationConstant(toLong(val_peek(0).sval), val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x1130, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationConstant(toInteger(val_peek(0).sval), val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:200:0x1155, code lost:
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationConstant(toBoolean(val_peek(0).sval), val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:201:0x117a, code lost:
        r0 = r8.lexer.getCodeBody();
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationConstant(toChar(r0), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:202:0x119b, code lost:
        r0 = r8.lexer.getCodeBody();
        r8.yyval.annoval = new org.hamcrest.generator.qdox.model.annotation.AnnotationConstant(toString(r0), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x11bc, code lost:
        r8.yyval.sval = "boolean";
     */
    /* JADX WARN: Code restructure failed: missing block: B:204:0x11c9, code lost:
        r8.yyval.sval = "byte";
     */
    /* JADX WARN: Code restructure failed: missing block: B:205:0x11d6, code lost:
        r8.yyval.sval = "short";
     */
    /* JADX WARN: Code restructure failed: missing block: B:206:0x11e3, code lost:
        r8.yyval.sval = "int";
     */
    /* JADX WARN: Code restructure failed: missing block: B:207:0x11f0, code lost:
        r8.yyval.sval = "long";
     */
    /* JADX WARN: Code restructure failed: missing block: B:208:0x11fd, code lost:
        r8.yyval.sval = "char";
     */
    /* JADX WARN: Code restructure failed: missing block: B:209:0x120a, code lost:
        r8.yyval.sval = soot.jimple.Jimple.FLOAT;
     */
    /* JADX WARN: Code restructure failed: missing block: B:210:0x1217, code lost:
        r8.yyval.sval = "double";
     */
    /* JADX WARN: Code restructure failed: missing block: B:211:0x1224, code lost:
        r0 = val_peek(1).type;
        r0.dimensions = val_peek(0).ival;
        r8.yyval.type = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:212:0x1244, code lost:
        r0 = new org.hamcrest.generator.qdox.parser.structs.TypeDef(val_peek(1).sval, 0);
        r0.actualArgumentTypes = new java.util.ArrayList();
        r8.yyval.type = (org.hamcrest.generator.qdox.parser.structs.TypeDef) r8.typeStack.push(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:213:0x1275, code lost:
        r8.yyval.type = (org.hamcrest.generator.qdox.parser.structs.TypeDef) r8.typeStack.pop();
     */
    /* JADX WARN: Code restructure failed: missing block: B:214:0x1289, code lost:
        r8.yyval.type = val_peek(1).type;
     */
    /* JADX WARN: Code restructure failed: missing block: B:215:0x129b, code lost:
        r8.yyval.type = new org.hamcrest.generator.qdox.parser.structs.TypeDef(val_peek(0).sval, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:216:0x12b5, code lost:
        r8.yyval.sval = val_peek(0).sval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:217:0x12c7, code lost:
        r8.yyval.sval = new java.lang.StringBuffer().append(val_peek(2).type.name).append('.').append(val_peek(0).sval).toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:218:0x12f9, code lost:
        r8.yyval.sval = val_peek(0).sval;
     */
    /* JADX WARN: Code restructure failed: missing block: B:219:0x130b, code lost:
        r8.yyval.sval = new java.lang.StringBuffer().append(val_peek(2).sval).append('.').append(val_peek(0).sval).toString();
     */
    /* JADX WARN: Code restructure failed: missing block: B:220:0x133a, code lost:
        ((org.hamcrest.generator.qdox.parser.structs.TypeDef) r8.typeStack.peek()).actualArgumentTypes.add(val_peek(0).type);
     */
    /* JADX WARN: Code restructure failed: missing block: B:221:0x1358, code lost:
        ((org.hamcrest.generator.qdox.parser.structs.TypeDef) r8.typeStack.peek()).actualArgumentTypes.add(val_peek(0).type);
     */
    /* JADX WARN: Code restructure failed: missing block: B:222:0x1376, code lost:
        r8.yyval.type = val_peek(0).type;
     */
    /* JADX WARN: Code restructure failed: missing block: B:223:0x1388, code lost:
        r8.yyval.type = new org.hamcrest.generator.qdox.parser.structs.WildcardTypeDef();
     */
    /* JADX WARN: Code restructure failed: missing block: B:224:0x1399, code lost:
        r8.yyval.type = new org.hamcrest.generator.qdox.parser.structs.WildcardTypeDef(val_peek(0).type, soot.jimple.Jimple.EXTENDS);
     */
    /* JADX WARN: Code restructure failed: missing block: B:225:0x13b5, code lost:
        r8.yyval.type = new org.hamcrest.generator.qdox.parser.structs.WildcardTypeDef(val_peek(0).type, "super");
     */
    /* JADX WARN: Code restructure failed: missing block: B:226:0x13d1, code lost:
        r8.typeParams = new java.util.ArrayList();
     */
    /* JADX WARN: Code restructure failed: missing block: B:227:0x13df, code lost:
        r8.typeParams.add(new org.hamcrest.generator.qdox.parser.structs.TypeVariableDef(val_peek(0).sval));
     */
    /* JADX WARN: Code restructure failed: missing block: B:228:0x13fb, code lost:
        r8.typeVariable = new org.hamcrest.generator.qdox.parser.structs.TypeVariableDef(val_peek(1).sval);
        r8.typeVariable.bounds = new java.util.ArrayList();
     */
    /* JADX WARN: Code restructure failed: missing block: B:229:0x141f, code lost:
        r8.typeParams.add(r8.typeVariable);
        r8.typeVariable = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:230:0x1435, code lost:
        r8.typeVariable.bounds.add(val_peek(0).type);
     */
    /* JADX WARN: Code restructure failed: missing block: B:231:0x144d, code lost:
        r8.typeVariable.bounds.add(val_peek(0).type);
     */
    /* JADX WARN: Code restructure failed: missing block: B:232:0x1465, code lost:
        r8.builder.endClass();
        r8.fieldType = null;
        r8.modifiers.clear();
     */
    /* JADX WARN: Code restructure failed: missing block: B:233:0x147f, code lost:
        r8.cls.lineNumber = r8.line;
        r8.cls.modifiers.addAll(r8.modifiers);
        r8.cls.name = val_peek(1).sval;
        r8.cls.type = "enum";
        r8.builder.beginClass(r8.cls);
        r8.cls = new org.hamcrest.generator.qdox.parser.structs.ClassDef();
        r8.fieldType = new org.hamcrest.generator.qdox.parser.structs.TypeDef(val_peek(1).sval, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:234:0x14e3, code lost:
        makeField(new org.hamcrest.generator.qdox.parser.structs.TypeDef(val_peek(0).sval, 0), "");
     */
    /* JADX WARN: Code restructure failed: missing block: B:235:0x14fd, code lost:
        makeField(new org.hamcrest.generator.qdox.parser.structs.TypeDef(val_peek(1).sval, 0), "");
     */
    /* JADX WARN: Code restructure failed: missing block: B:236:0x1517, code lost:
        makeField(new org.hamcrest.generator.qdox.parser.structs.TypeDef(val_peek(1).sval, 0), "");
     */
    /* JADX WARN: Code restructure failed: missing block: B:237:0x1531, code lost:
        makeField(new org.hamcrest.generator.qdox.parser.structs.TypeDef(val_peek(2).sval, 0), "");
     */
    /* JADX WARN: Code restructure failed: missing block: B:238:0x154b, code lost:
        r8.builder.endClass();
     */
    /* JADX WARN: Code restructure failed: missing block: B:239:0x1557, code lost:
        r8.cls.lineNumber = r8.line;
        r8.cls.modifiers.addAll(r8.modifiers);
        r8.modifiers.clear();
        r8.cls.name = val_peek(3).sval;
        r8.cls.typeParams = r8.typeParams;
        r8.builder.beginClass(r8.cls);
        r8.cls = new org.hamcrest.generator.qdox.parser.structs.ClassDef();
     */
    /* JADX WARN: Code restructure failed: missing block: B:240:0x15b1, code lost:
        r8.cls.type = "class";
     */
    /* JADX WARN: Code restructure failed: missing block: B:241:0x15be, code lost:
        r8.cls.type = "interface";
     */
    /* JADX WARN: Code restructure failed: missing block: B:242:0x15cb, code lost:
        r8.cls.type = org.hamcrest.generator.qdox.parser.structs.ClassDef.ANNOTATION_TYPE;
     */
    /* JADX WARN: Code restructure failed: missing block: B:243:0x15d8, code lost:
        r8.cls.extendz.add(val_peek(0).type);
     */
    /* JADX WARN: Code restructure failed: missing block: B:244:0x15f0, code lost:
        r8.cls.extendz.add(val_peek(0).type);
     */
    /* JADX WARN: Code restructure failed: missing block: B:245:0x1608, code lost:
        r8.cls.implementz.add(val_peek(0).type);
     */
    /* JADX WARN: Code restructure failed: missing block: B:246:0x1620, code lost:
        r8.cls.implementz.add(val_peek(0).type);
     */
    /* JADX WARN: Code restructure failed: missing block: B:247:0x1638, code lost:
        r8.line = r8.lexer.getLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:248:0x1648, code lost:
        r8.yyval.sval = "";
     */
    /* JADX WARN: Code restructure failed: missing block: B:249:0x1655, code lost:
        r8.yyval.sval = r8.lexer.getCodeBody();
     */
    /* JADX WARN: Code restructure failed: missing block: B:250:0x1668, code lost:
        r8.lexer.getCodeBody();
        r8.modifiers.clear();
     */
    /* JADX WARN: Code restructure failed: missing block: B:251:0x167e, code lost:
        r8.fieldType = val_peek(1).type;
        makeField(val_peek(0).type, r8.lexer.getCodeBody());
     */
    /* JADX WARN: Code restructure failed: missing block: B:252:0x16a2, code lost:
        r8.modifiers.clear();
     */
    /* JADX WARN: Code restructure failed: missing block: B:253:0x16ae, code lost:
        r8.line = r8.lexer.getLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:254:0x16be, code lost:
        makeField(val_peek(0).type, r8.lexer.getCodeBody());
     */
    /* JADX WARN: Code restructure failed: missing block: B:255:0x16d6, code lost:
        r8.line = r8.lexer.getLine();
     */
    /* JADX WARN: Code restructure failed: missing block: B:256:0x16e6, code lost:
        makeField(val_peek(0).type, r8.lexer.getCodeBody());
     */
    /* JADX WARN: Code restructure failed: missing block: B:257:0x16fe, code lost:
        r8.builder.beginMethod();
        r8.mth.typeParams = r8.typeParams;
     */
    /* JADX WARN: Code restructure failed: missing block: B:258:0x1715, code lost:
        r8.mth.lineNumber = r8.line;
        r8.mth.modifiers.addAll(r8.modifiers);
        r8.modifiers.clear();
        r8.mth.returnType = val_peek(6).type;
        r8.mth.dimensions = val_peek(2).ival;
        r8.mth.name = val_peek(5).sval;
        r8.mth.body = val_peek(0).sval;
        r8.builder.endMethod(r8.mth);
        r8.mth = new org.hamcrest.generator.qdox.parser.structs.MethodDef();
     */
    /* JADX WARN: Code restructure failed: missing block: B:259:0x1792, code lost:
        r8.builder.beginMethod();
     */
    /* JADX WARN: Code restructure failed: missing block: B:260:0x179e, code lost:
        r8.mth.lineNumber = r8.line;
        r8.mth.modifiers.addAll(r8.modifiers);
        r8.modifiers.clear();
        r8.mth.returnType = val_peek(6).type;
        r8.mth.dimensions = val_peek(2).ival;
        r8.mth.name = val_peek(5).sval;
        r8.mth.body = val_peek(0).sval;
        r8.builder.endMethod(r8.mth);
        r8.mth = new org.hamcrest.generator.qdox.parser.structs.MethodDef();
     */
    /* JADX WARN: Code restructure failed: missing block: B:261:0x181b, code lost:
        r8.builder.beginMethod();
     */
    /* JADX WARN: Code restructure failed: missing block: B:262:0x1827, code lost:
        r8.mth.lineNumber = r8.line;
        r8.mth.modifiers.addAll(r8.modifiers);
        r8.modifiers.clear();
        r8.mth.constructor = true;
        r8.mth.name = val_peek(4).sval;
        r8.mth.body = val_peek(0).sval;
        r8.builder.endMethod(r8.mth);
        r8.mth = new org.hamcrest.generator.qdox.parser.structs.MethodDef();
     */
    /* JADX WARN: Code restructure failed: missing block: B:263:0x188d, code lost:
        r8.builder.beginMethod();
        r8.mth.typeParams = r8.typeParams;
     */
    /* JADX WARN: Code restructure failed: missing block: B:264:0x18a4, code lost:
        r8.mth.lineNumber = r8.line;
        r8.mth.modifiers.addAll(r8.modifiers);
        r8.modifiers.clear();
        r8.mth.constructor = true;
        r8.mth.name = val_peek(4).sval;
        r8.mth.body = val_peek(0).sval;
        r8.builder.endMethod(r8.mth);
        r8.mth = new org.hamcrest.generator.qdox.parser.structs.MethodDef();
     */
    /* JADX WARN: Code restructure failed: missing block: B:265:0x190a, code lost:
        r8.mth.exceptions.add(val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:266:0x1922, code lost:
        r8.mth.exceptions.add(val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:267:0x193a, code lost:
        r8.param.name = val_peek(0).type.name;
        r8.param.type = val_peek(2).type;
        r8.param.dimensions = val_peek(0).type.dimensions;
        r8.param.isVarArgs = val_peek(1).bval;
        r8.builder.addParameter(r8.param);
        r8.param = new org.hamcrest.generator.qdox.parser.structs.FieldDef();
     */
    /* JADX WARN: Code restructure failed: missing block: B:268:0x1997, code lost:
        r8.yyval.bval = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:269:0x19a2, code lost:
        r8.yyval.bval = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:270:0x19ad, code lost:
        r8.builder.addAnnotation((org.hamcrest.generator.qdox.model.Annotation) val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:271:0x19c4, code lost:
        r8.param.modifiers.add(val_peek(0).sval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:272:0x19dc, code lost:
        r8.builder.addAnnotation((org.hamcrest.generator.qdox.model.Annotation) val_peek(0).annoval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x19f4, code lost:
        if (r8.yydebug == false) goto L219;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x19f7, code lost:
        debug("reduce");
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x19fe, code lost:
        state_drop(r8.yym);
        r8.yystate = state_peek(0);
        val_drop(r8.yym);
        r8.yym = org.hamcrest.generator.qdox.parser.impl.Parser.yylhs[r8.yyn];
     */
    /* JADX WARN: Code restructure failed: missing block: B:277:0x1a27, code lost:
        if (r8.yystate != 0) goto L252;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x1a2e, code lost:
        if (r8.yym != 0) goto L223;
     */
    /* JADX WARN: Code restructure failed: missing block: B:280:0x1a31, code lost:
        debug("After reduction, shifting from state 0 to state 1");
        r8.yystate = 1;
        state_push(1);
        val_push(r8.yyval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x1a4e, code lost:
        if (r8.yychar >= 0) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:282:0x1a51, code lost:
        r8.yychar = yylex();
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x1a5d, code lost:
        if (r8.yychar >= 0) goto L243;
     */
    /* JADX WARN: Code restructure failed: missing block: B:284:0x1a60, code lost:
        r8.yychar = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:286:0x1a69, code lost:
        if (r8.yydebug == false) goto L246;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x1a6c, code lost:
        yylexdebug(r8.yystate, r8.yychar);
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x1a7c, code lost:
        if (r8.yychar != 0) goto L251;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x1a82, code lost:
        r8.yyn = org.hamcrest.generator.qdox.parser.impl.Parser.yygindex[r8.yym];
     */
    /* JADX WARN: Code restructure failed: missing block: B:292:0x1a92, code lost:
        if (r8.yyn == 0) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:293:0x1a95, code lost:
        r1 = r8.yyn + r8.yystate;
        r8.yyn = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:294:0x1aa3, code lost:
        if (r1 < 0) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x1aad, code lost:
        if (r8.yyn > org.hamcrest.generator.qdox.parser.impl.Parser.YYTABLESIZE) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x1abc, code lost:
        if (org.hamcrest.generator.qdox.parser.impl.Parser.yycheck[r8.yyn] != r8.yystate) goto L236;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x1abf, code lost:
        r8.yystate = org.hamcrest.generator.qdox.parser.impl.Parser.yytable[r8.yyn];
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x1ace, code lost:
        r8.yystate = org.hamcrest.generator.qdox.parser.impl.Parser.yydgoto[r8.yym];
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x1ada, code lost:
        debug(new java.lang.StringBuffer().append("after reduction, shifting from state ").append(state_peek(0)).append(" to state ").append(r8.yystate).append("").toString());
        state_push(r8.yystate);
        val_push(r8.yyval);
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x1b1c, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x036e, code lost:
        if (r9 != false) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x0374, code lost:
        r8.yym = org.hamcrest.generator.qdox.parser.impl.Parser.yylen[r8.yyn];
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0384, code lost:
        if (r8.yydebug == false) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0387, code lost:
        debug(new java.lang.StringBuffer().append("state ").append(r8.yystate).append(", reducing ").append(r8.yym).append(" by rule ").append(r8.yyn).append(" (").append(org.hamcrest.generator.qdox.parser.impl.Parser.yyrule[r8.yyn]).append(")").toString());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int yyparse() {
        /*
            Method dump skipped, instructions count: 6942
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: org.hamcrest.generator.qdox.parser.impl.Parser.yyparse():int");
    }
}
