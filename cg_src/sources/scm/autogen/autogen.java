package scm.autogen;

import jas.RuntimeConstants;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:scm/autogen/autogen.class */
class autogen implements RuntimeConstants {
    static String[][] procs = {new String[]{"ClassEnv", MSVSSConstants.COMMAND_CP}, new String[]{"jas-class-addcpe", "addCPItem"}, new String[]{"ClassEnv", "Var"}, new String[]{"jas-class-addfield", "addField"}, new String[]{"ClassEnv", MSVSSConstants.COMMAND_CP}, new String[]{"jas-class-addinterface", "addInterface"}, new String[]{"ClassEnv", MSVSSConstants.COMMAND_CP}, new String[]{"jas-class-setclass", "setClass"}, new String[]{"ClassEnv", MSVSSConstants.COMMAND_CP}, new String[]{"jas-class-setsuperclass", "setSuperClass"}, new String[]{"ClassEnv", "short", "String", "String", "CodeAttr", "ExceptAttr"}, new String[]{"jas-class-addmethod", "addMethod"}, new String[]{"ClassEnv", "short"}, new String[]{"jas-class-setaccess", "setClassAccess"}, new String[]{"ClassEnv", "String"}, new String[]{"jas-class-setsource", "setSource"}, new String[]{"ClassEnv", "scmOutputStream"}, new String[]{"jas-class-write", "write"}, new String[]{"ExceptAttr", MSVSSConstants.COMMAND_CP}, new String[]{"jas-exception-add", "addException"}, new String[]{"CodeAttr", "Insn"}, new String[]{"jas-code-addinsn", "addInsn"}, new String[]{"CodeAttr", "short"}, new String[]{"jas-code-stack-size", "setStackSize"}, new String[]{"CodeAttr", "short"}, new String[]{"jas-code-var-size", "setVarSize"}, new String[]{"CodeAttr", "Catchtable"}, new String[]{"jas-set-catchtable", "setCatchtable"}, new String[]{"Catchtable", "CatchEntry"}, new String[]{"jas-add-catch-entry", "addEntry"}};
    static String[][] types = {new String[]{"String"}, new String[]{"make-ascii-cpe", "AsciiCP"}, new String[]{"String"}, new String[]{"make-class-cpe", "ClassCP"}, new String[]{"String", "String"}, new String[]{"make-name-type-cpe", "NameTypeCP"}, new String[]{"String", "String", "String"}, new String[]{"make-field-cpe", "FieldCP"}, new String[]{"String", "String", "String"}, new String[]{"make-interface-cpe", "InterfaceCP"}, new String[]{"String", "String", "String"}, new String[]{"make-method-cpe", "MethodCP"}, new String[]{"int"}, new String[]{"make-integer-cpe", "IntegerCP"}, new String[]{Jimple.FLOAT}, new String[]{"make-float-cpe", "FloatCP"}, new String[]{"long"}, new String[]{"make-long-cpe", "LongCP"}, new String[]{"double"}, new String[]{"make-double-cpe", "DoubleCP"}, new String[]{"String"}, new String[]{"make-string-cpe", "StringCP"}, new String[]{"short", MSVSSConstants.COMMAND_CP, MSVSSConstants.COMMAND_CP, "ConstAttr"}, new String[]{"make-field", "Var"}, new String[]{MSVSSConstants.COMMAND_CP}, new String[]{"make-const", "ConstAttr"}, new String[]{"String"}, new String[]{"make-outputstream", "scmOutputStream"}, new String[]{"String"}, new String[]{"make-label", MSVSSConstants.COMMAND_LABEL}, new String[0], new String[]{"make-class-env", "ClassEnv"}, new String[0], new String[]{"make-code", "CodeAttr"}, new String[0], new String[]{"make-exception", "ExceptAttr"}, new String[0], new String[]{"make-catchtable", "Catchtable"}, new String[]{MSVSSConstants.COMMAND_LABEL, MSVSSConstants.COMMAND_LABEL, MSVSSConstants.COMMAND_LABEL, MSVSSConstants.COMMAND_CP}, new String[]{"make-catch-entry", "CatchEntry"}, new String[]{"int", "int"}, new String[]{"iinc", "IincInsn"}, new String[]{MSVSSConstants.COMMAND_CP, "int"}, new String[]{"multianewarray", "MultiarrayInsn"}, new String[]{MSVSSConstants.COMMAND_CP, "int"}, new String[]{"invokeinterface", "InvokeinterfaceInsn"}};

    autogen() {
    }

    public static void main(String[] argv) throws IOException {
        PrintStream initer = new PrintStream(new FileOutputStream("AutoInit.java"));
        initer.println("package scm;\n\nimport jas.*;");
        initer.println("class AutoInit\n{\n  static void fillit(Env e)\n  {");
        PrintStream doit = new PrintStream(new FileOutputStream("AutoTypes.java"));
        doit.println("package scm;\n\nimport jas.*;");
        for (int x = 0; x < types.length; x += 2) {
            typeinfo tp = new typeinfo(types[x + 1][0], types[x + 1][1], types[x]);
            tp.write(doit);
            initer.println("e.definevar(Symbol.intern(\"" + types[x + 1][0] + "\"), new scm" + types[x + 1][1] + "());");
        }
        PrintStream doit2 = new PrintStream(new FileOutputStream("AutoProcs.java"));
        doit2.println("package scm;\n\nimport jas.*;");
        for (int x2 = 0; x2 < procs.length; x2 += 2) {
            procinfo p = new procinfo(procs[x2 + 1][0], procs[x2 + 1][1], procs[x2]);
            initer.println("e.definevar(Symbol.intern(\"" + procs[x2 + 1][0] + "\"), new scm" + procs[x2 + 1][1] + "());");
            p.write(doit2);
        }
        initer.println("  }\n}");
    }
}
