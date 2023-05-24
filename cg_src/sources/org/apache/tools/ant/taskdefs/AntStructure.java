package org.apache.tools.ant.taskdefs;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.jsp.tagext.TagInfo;
import net.bytebuddy.implementation.auxiliary.TypeProxy;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Reference;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AntStructure.class */
public class AntStructure extends Task {
    private File output;
    private StructurePrinter printer = new DTDPrinter();

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AntStructure$StructurePrinter.class */
    public interface StructurePrinter {
        void printHead(PrintWriter printWriter, Project project, Hashtable<String, Class<?>> hashtable, Hashtable<String, Class<?>> hashtable2);

        void printTargetDecl(PrintWriter printWriter);

        void printElementDecl(PrintWriter printWriter, Project project, String str, Class<?> cls);

        void printTail(PrintWriter printWriter);
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public void add(StructurePrinter p) {
        this.printer = p;
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.output == null) {
            throw new BuildException("output attribute is required", getLocation());
        }
        try {
            PrintWriter out = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(this.output.toPath(), new OpenOption[0]), StandardCharsets.UTF_8));
            this.printer.printHead(out, getProject(), new Hashtable<>(getProject().getTaskDefinitions()), new Hashtable<>(getProject().getDataTypeDefinitions()));
            this.printer.printTargetDecl(out);
            for (String typeName : getProject().getCopyOfDataTypeDefinitions().keySet()) {
                this.printer.printElementDecl(out, getProject(), typeName, getProject().getDataTypeDefinitions().get(typeName));
            }
            for (String tName : getProject().getCopyOfTaskDefinitions().keySet()) {
                this.printer.printElementDecl(out, getProject(), tName, getProject().getTaskDefinitions().get(tName));
            }
            this.printer.printTail(out);
            if (out.checkError()) {
                throw new IOException("Encountered an error writing Ant structure");
            }
            out.close();
        } catch (IOException ioe) {
            throw new BuildException("Error writing " + this.output.getAbsolutePath(), ioe, getLocation());
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/AntStructure$DTDPrinter.class */
    private static class DTDPrinter implements StructurePrinter {
        private static final String BOOLEAN = "%boolean;";
        private static final String TASKS = "%tasks;";
        private static final String TYPES = "%types;";
        private final Hashtable<String, String> visited;

        private DTDPrinter() {
            this.visited = new Hashtable<>();
        }

        @Override // org.apache.tools.ant.taskdefs.AntStructure.StructurePrinter
        public void printTail(PrintWriter out) {
            this.visited.clear();
        }

        @Override // org.apache.tools.ant.taskdefs.AntStructure.StructurePrinter
        public void printHead(PrintWriter out, Project p, Hashtable<String, Class<?>> tasks, Hashtable<String, Class<?>> types) {
            printHead(out, tasks.keySet(), types.keySet());
        }

        private void printHead(PrintWriter out, Set<String> tasks, Set<String> types) {
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            out.println("<!ENTITY % boolean \"(true|false|on|off|yes|no)\">");
            out.println((String) tasks.stream().collect(Collectors.joining(" | ", "<!ENTITY % tasks \"", "\">")));
            out.println((String) types.stream().collect(Collectors.joining(" | ", "<!ENTITY % types \"", "\">")));
            out.println();
            out.print("<!ELEMENT project (target | extension-point | ");
            out.print(TASKS);
            out.print(" | ");
            out.print(TYPES);
            out.println(")*>");
            out.println("<!ATTLIST project");
            out.println("          name    CDATA #IMPLIED");
            out.println("          default CDATA #IMPLIED");
            out.println("          basedir CDATA #IMPLIED>");
            out.println("");
        }

        @Override // org.apache.tools.ant.taskdefs.AntStructure.StructurePrinter
        public void printTargetDecl(PrintWriter out) {
            out.print("<!ELEMENT target (");
            out.print(TASKS);
            out.print(" | ");
            out.print(TYPES);
            out.println(")*>");
            out.println("");
            printTargetAttrs(out, TypeProxy.INSTANCE_FIELD);
            out.println("<!ELEMENT extension-point EMPTY>");
            out.println("");
            printTargetAttrs(out, "extension-point");
        }

        private void printTargetAttrs(PrintWriter out, String tag) {
            out.print("<!ATTLIST ");
            out.println(tag);
            out.println("          id                      ID    #IMPLIED");
            out.println("          name                    CDATA #REQUIRED");
            out.println("          if                      CDATA #IMPLIED");
            out.println("          unless                  CDATA #IMPLIED");
            out.println("          depends                 CDATA #IMPLIED");
            out.println("          extensionOf             CDATA #IMPLIED");
            out.println("          onMissingExtensionPoint CDATA #IMPLIED");
            out.println("          description             CDATA #IMPLIED>");
            out.println("");
        }

        @Override // org.apache.tools.ant.taskdefs.AntStructure.StructurePrinter
        public void printElementDecl(PrintWriter out, Project p, String name, Class<?> element) {
            if (this.visited.containsKey(name)) {
                return;
            }
            this.visited.put(name, "");
            try {
                IntrospectionHelper ih = IntrospectionHelper.getHelper(p, element);
                StringBuilder sb = new StringBuilder("<!ELEMENT ").append(name).append(Instruction.argsep);
                if (Reference.class.equals(element)) {
                    sb.append(String.format("EMPTY>%n<!ATTLIST %s%n          id ID #IMPLIED%n          refid IDREF #IMPLIED>%n", name));
                    out.println(sb);
                    return;
                }
                List<String> v = new ArrayList<>();
                if (ih.supportsCharacters()) {
                    v.add("#PCDATA");
                }
                if (TaskContainer.class.isAssignableFrom(element)) {
                    v.add(TASKS);
                }
                v.addAll(Collections.list(ih.getNestedElements()));
                Collector<CharSequence, ?, String> joinAlts = Collectors.joining(" | ", "(", ")");
                if (v.isEmpty()) {
                    sb.append(TagInfo.BODY_CONTENT_EMPTY);
                } else {
                    sb.append((String) v.stream().collect(joinAlts));
                    if (v.size() > 1 || !"#PCDATA".equals(v.get(0))) {
                        sb.append("*");
                    }
                }
                sb.append(">");
                out.println(sb);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(String.format("<!ATTLIST %s%n          id ID #IMPLIED", name));
                Iterator it = Collections.list(ih.getAttributes()).iterator();
                while (it.hasNext()) {
                    String attrName = (String) it.next();
                    if (!"id".equals(attrName)) {
                        sb2.append(String.format("%n          %s ", attrName));
                        Class<?> type = ih.getAttributeType(attrName);
                        if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
                            sb2.append(BOOLEAN).append(Instruction.argsep);
                        } else if (Reference.class.isAssignableFrom(type)) {
                            sb2.append("IDREF ");
                        } else if (EnumeratedAttribute.class.isAssignableFrom(type)) {
                            try {
                                EnumeratedAttribute ea = (EnumeratedAttribute) type.asSubclass(EnumeratedAttribute.class).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                                String[] values = ea.getValues();
                                if (values == null || values.length == 0 || !areNmtokens(values)) {
                                    sb2.append("CDATA ");
                                } else {
                                    sb2.append((String) Stream.of((Object[]) values).collect(joinAlts)).append(Instruction.argsep);
                                }
                            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                                sb2.append("CDATA ");
                            }
                        } else if (Enum.class.isAssignableFrom(type)) {
                            try {
                                Enum<?>[] values2 = (Enum[]) type.getMethod("values", new Class[0]).invoke(null, new Object[0]);
                                if (values2.length == 0) {
                                    sb2.append("CDATA ");
                                } else {
                                    sb2.append((String) Stream.of((Object[]) values2).map((v0) -> {
                                        return v0.name();
                                    }).collect(joinAlts)).append(Instruction.argsep);
                                }
                            } catch (Exception e2) {
                                sb2.append("CDATA ");
                            }
                        } else {
                            sb2.append("CDATA ");
                        }
                        sb2.append("#IMPLIED");
                    }
                }
                sb2.append(String.format(">%n", new Object[0]));
                out.println(sb2);
                for (String nestedName : v) {
                    if (!"#PCDATA".equals(nestedName) && !TASKS.equals(nestedName) && !TYPES.equals(nestedName)) {
                        printElementDecl(out, p, nestedName, ih.getElementType(nestedName));
                    }
                }
            } catch (Throwable th) {
            }
        }

        public static final boolean isNmtoken(String s) {
            int length = s.length();
            for (int i = 0; i < length; i++) {
                char c = s.charAt(i);
                if (!Character.isLetterOrDigit(c) && c != '.' && c != '-' && c != '_' && c != ':') {
                    return false;
                }
            }
            return true;
        }

        public static final boolean areNmtokens(String[] s) {
            for (String value : s) {
                if (!isNmtoken(value)) {
                    return false;
                }
            }
            return true;
        }
    }

    protected boolean isNmtoken(String s) {
        return DTDPrinter.isNmtoken(s);
    }

    protected boolean areNmtokens(String[] s) {
        return DTDPrinter.areNmtokens(s);
    }
}
