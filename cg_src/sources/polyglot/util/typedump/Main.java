package polyglot.util.typedump;

import java.io.IOException;
import polyglot.ext.jl.Topics;
import polyglot.frontend.ExtensionInfo;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/util/typedump/Main.class */
public class Main {
    public static void main(String[] args) {
        String extension = Topics.jl;
        if (args.length == 3 && args[0].equals("-ext")) {
            extension = args[1];
        }
        if ((extension == null && args.length != 1) || (extension != null && args.length != 3)) {
            System.err.println("Usage: polyglot.util.typedump.Main [-ext <extension>] <classname>");
            System.exit(1);
        }
        String className = extension == null ? args[0] : args[2];
        ExtensionInfo extInfo = null;
        String extClassName = new StringBuffer().append("polyglot.ext.").append(extension).append(".ExtensionInfo").toString();
        Class extClass = null;
        try {
            extClass = Class.forName(extClassName);
        } catch (ClassNotFoundException e) {
            System.err.println(new StringBuffer().append("Extension ").append(extension).append(" not found: could not find class ").append(extClassName).append(".").toString());
            System.exit(1);
        }
        try {
            extInfo = (ExtensionInfo) extClass.newInstance();
        } catch (Exception e2) {
            System.err.println(new StringBuffer().append("Extension ").append(extension).append(" could not be loaded: ").append("could not instantiate ").append(extClassName).append(".").toString());
            System.exit(1);
        }
        try {
            TypeSystem ts = extInfo.typeSystem();
            TypeDumper t = TypeDumper.load(className, ts);
            CodeWriter cw = new CodeWriter(System.out, 72);
            t.dump(cw);
            cw.newline(0);
            try {
                cw.flush();
            } catch (IOException exn) {
                System.err.println(exn.getMessage());
            }
        } catch (IOException exn2) {
            System.err.println("IO errors.");
            System.err.println(exn2.getMessage());
        } catch (ClassNotFoundException exn3) {
            System.err.println(new StringBuffer().append("Could not load .class: ").append(className).toString());
            System.err.println(exn3.getMessage());
        } catch (NoSuchFieldException exn4) {
            System.err.println("Could not reflect jlc fields");
            System.err.println(exn4.getMessage());
        } catch (SecurityException exn5) {
            System.err.println("Security policy error.");
            System.err.println(exn5.getMessage());
        }
    }
}
