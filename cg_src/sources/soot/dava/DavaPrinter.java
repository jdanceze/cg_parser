package soot.dava;

import java.io.PrintWriter;
import soot.Body;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.G;
import soot.IntType;
import soot.LongType;
import soot.ShortType;
import soot.Singletons;
import soot.SootField;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.dava.internal.AST.ASTNode;
import soot.options.Options;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.tagkit.StringConstantValueTag;
import soot.tagkit.Tag;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/dava/DavaPrinter.class */
public class DavaPrinter {
    public DavaPrinter(Singletons.Global g) {
    }

    public static DavaPrinter v() {
        return G.v().soot_dava_DavaPrinter();
    }

    private void printStatementsInBody(Body body, PrintWriter out) {
        if (Options.v().verbose()) {
            System.out.println("Printing " + body.getMethod().getName());
        }
        Chain<Unit> units = body.getUnits();
        if (units.size() != 1) {
            throw new RuntimeException("DavaBody AST doesn't have single root.");
        }
        UnitPrinter up = new DavaUnitPrinter((DavaBody) body);
        ((ASTNode) units.getFirst()).toString(up);
        out.print(up.toString());
    }

    /* JADX WARN: Removed duplicated region for block: B:138:0x0595  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void printTo(soot.SootClass r7, java.io.PrintWriter r8) {
        /*
            Method dump skipped, instructions count: 1500
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.dava.DavaPrinter.printTo(soot.SootClass, java.io.PrintWriter):void");
    }

    private void printTags(SootField f, String declaration, PrintWriter out) {
        Type fieldType = f.getType();
        if (fieldType instanceof DoubleType) {
            DoubleConstantValueTag t = (DoubleConstantValueTag) f.getTag(DoubleConstantValueTag.NAME);
            if (t != null) {
                out.println(ASTNode.TAB + declaration + " = " + t.getDoubleValue() + ';');
                return;
            }
        } else if (fieldType instanceof FloatType) {
            FloatConstantValueTag t2 = (FloatConstantValueTag) f.getTag(FloatConstantValueTag.NAME);
            if (t2 != null) {
                out.println(ASTNode.TAB + declaration + " = " + t2.getFloatValue() + "f;");
                return;
            }
        } else if (fieldType instanceof LongType) {
            LongConstantValueTag t3 = (LongConstantValueTag) f.getTag(LongConstantValueTag.NAME);
            if (t3 != null) {
                out.println(ASTNode.TAB + declaration + " = " + t3.getLongValue() + "l;");
                return;
            }
        } else if (fieldType instanceof CharType) {
            IntegerConstantValueTag t4 = (IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME);
            if (t4 != null) {
                out.println(ASTNode.TAB + declaration + " = '" + ((char) t4.getIntValue()) + "';");
                return;
            }
        } else if (fieldType instanceof BooleanType) {
            IntegerConstantValueTag t5 = (IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME);
            if (t5 != null) {
                out.println(ASTNode.TAB + declaration + (t5.getIntValue() == 0 ? " = false;" : " = true;"));
                return;
            }
        } else if ((fieldType instanceof IntType) || (fieldType instanceof ByteType) || (fieldType instanceof ShortType)) {
            IntegerConstantValueTag t6 = (IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME);
            if (t6 != null) {
                out.println(ASTNode.TAB + declaration + " = " + t6.getIntValue() + ';');
                return;
            }
        } else {
            StringConstantValueTag t7 = (StringConstantValueTag) f.getTag(StringConstantValueTag.NAME);
            if (t7 != null) {
                out.println(ASTNode.TAB + declaration + " = \"" + t7.getStringValue() + "\";");
                return;
            }
        }
        out.println(ASTNode.TAB + declaration + ';');
    }

    private void printTo(Body b, PrintWriter out) {
        b.validate();
        out.println(ASTNode.TAB + b.getMethod().getDavaDeclaration());
        if (Options.v().print_tags_in_output()) {
            for (Tag t : b.getMethod().getTags()) {
                out.println(t);
            }
        }
        out.println("    {");
        printStatementsInBody(b, out);
        out.println("    }");
    }
}
