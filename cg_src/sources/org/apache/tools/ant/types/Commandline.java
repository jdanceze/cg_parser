package org.apache.tools.ant.types;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.stream.Stream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.taskdefs.condition.Os;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Commandline.class */
public class Commandline implements Cloneable {
    private List<Argument> arguments = new ArrayList();
    private String executable = null;
    private static final boolean IS_WIN_9X = Os.isFamily(Os.FAMILY_9X);
    protected static final String DISCLAIMER = String.format("%nThe ' characters around the executable and arguments are%nnot part of the command.%n", new Object[0]);

    public Commandline(String toProcess) {
        String[] tmp = translateCommandline(toProcess);
        if (tmp != null && tmp.length > 0) {
            setExecutable(tmp[0]);
            for (int i = 1; i < tmp.length; i++) {
                createArgument().setValue(tmp[i]);
            }
        }
    }

    public Commandline() {
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Commandline$Argument.class */
    public static class Argument extends ProjectComponent {
        private String[] parts;
        private String prefix = "";
        private String suffix = "";

        public void setValue(String value) {
            this.parts = new String[]{value};
        }

        public void setLine(String line) {
            if (line == null) {
                return;
            }
            this.parts = Commandline.translateCommandline(line);
        }

        public void setPath(Path value) {
            this.parts = new String[]{value.toString()};
        }

        public void setPathref(Reference value) {
            Path p = new Path(getProject());
            p.setRefid(value);
            this.parts = new String[]{p.toString()};
        }

        public void setFile(File value) {
            this.parts = new String[]{value.getAbsolutePath()};
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix != null ? prefix : "";
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix != null ? suffix : "";
        }

        public void copyFrom(Argument other) {
            this.parts = other.parts;
            this.prefix = other.prefix;
            this.suffix = other.suffix;
        }

        public String[] getParts() {
            if (this.parts == null || this.parts.length == 0 || (this.prefix.isEmpty() && this.suffix.isEmpty())) {
                return this.parts;
            }
            String[] fullParts = new String[this.parts.length];
            for (int i = 0; i < fullParts.length; i++) {
                fullParts[i] = this.prefix + this.parts[i] + this.suffix;
            }
            return fullParts;
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/Commandline$Marker.class */
    public class Marker {
        private int position;
        private int realPos = -1;
        private String prefix = "";
        private String suffix = "";

        Marker(int position) {
            this.position = position;
        }

        public int getPosition() {
            if (this.realPos == -1) {
                this.realPos = (Commandline.this.executable == null ? 0 : 1) + ((int) Commandline.this.arguments.stream().limit(this.position).map((v0) -> {
                    return v0.getParts();
                }).flatMap((v0) -> {
                    return Stream.of(v0);
                }).count());
            }
            return this.realPos;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix != null ? prefix : "";
        }

        public String getPrefix() {
            return this.prefix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix != null ? suffix : "";
        }

        public String getSuffix() {
            return this.suffix;
        }
    }

    public Argument createArgument() {
        return createArgument(false);
    }

    public Argument createArgument(boolean insertAtStart) {
        Argument argument = new Argument();
        if (insertAtStart) {
            this.arguments.add(0, argument);
        } else {
            this.arguments.add(argument);
        }
        return argument;
    }

    public void setExecutable(String executable) {
        setExecutable(executable, true);
    }

    public void setExecutable(String executable, boolean translateFileSeparator) {
        String str;
        if (executable == null || executable.isEmpty()) {
            return;
        }
        if (translateFileSeparator) {
            str = executable.replace('/', File.separatorChar).replace('\\', File.separatorChar);
        } else {
            str = executable;
        }
        this.executable = str;
    }

    public String getExecutable() {
        return this.executable;
    }

    public void addArguments(String[] line) {
        for (String argument : line) {
            createArgument().setValue(argument);
        }
    }

    public String[] getCommandline() {
        List<String> commands = new LinkedList<>();
        addCommandToList(commands.listIterator());
        return (String[]) commands.toArray(new String[commands.size()]);
    }

    public void addCommandToList(ListIterator<String> list) {
        if (this.executable != null) {
            list.add(this.executable);
        }
        addArgumentsToList(list);
    }

    public String[] getArguments() {
        List<String> result = new ArrayList<>(this.arguments.size() * 2);
        addArgumentsToList(result.listIterator());
        return (String[]) result.toArray(new String[result.size()]);
    }

    public void addArgumentsToList(ListIterator<String> list) {
        for (Argument arg : this.arguments) {
            String[] s = arg.getParts();
            if (s != null) {
                for (String value : s) {
                    list.add(value);
                }
            }
        }
    }

    public String toString() {
        return toString(getCommandline());
    }

    public static String quoteArgument(String argument) {
        if (argument.contains("\"")) {
            if (argument.contains("'")) {
                throw new BuildException("Can't handle single and double quotes in same argument");
            }
            return '\'' + argument + '\'';
        } else if (argument.contains("'") || argument.contains(Instruction.argsep) || (IS_WIN_9X && argument.contains(";"))) {
            return '\"' + argument + '\"';
        } else {
            return argument;
        }
    }

    public static String toString(String[] line) {
        if (line == null || line.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (String argument : line) {
            if (result.length() > 0) {
                result.append(' ');
            }
            result.append(quoteArgument(argument));
        }
        return result.toString();
    }

    public static String[] translateCommandline(String toProcess) {
        if (toProcess == null || toProcess.isEmpty()) {
            return new String[0];
        }
        int state = 0;
        StringTokenizer tok = new StringTokenizer(toProcess, "\"' ", true);
        ArrayList<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean lastTokenHasBeenQuoted = false;
        while (tok.hasMoreTokens()) {
            String nextTok = tok.nextToken();
            switch (state) {
                case 1:
                    if ("'".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = 0;
                        break;
                    } else {
                        current.append(nextTok);
                        break;
                    }
                case 2:
                    if ("\"".equals(nextTok)) {
                        lastTokenHasBeenQuoted = true;
                        state = 0;
                        break;
                    } else {
                        current.append(nextTok);
                        break;
                    }
                default:
                    if ("'".equals(nextTok)) {
                        state = 1;
                    } else if ("\"".equals(nextTok)) {
                        state = 2;
                    } else if (Instruction.argsep.equals(nextTok)) {
                        if (lastTokenHasBeenQuoted || current.length() > 0) {
                            result.add(current.toString());
                            current.setLength(0);
                        }
                    } else {
                        current.append(nextTok);
                    }
                    lastTokenHasBeenQuoted = false;
                    break;
            }
        }
        if (lastTokenHasBeenQuoted || current.length() > 0) {
            result.add(current.toString());
        }
        if (state == 1 || state == 2) {
            throw new BuildException("unbalanced quotes in " + toProcess);
        }
        return (String[]) result.toArray(new String[result.size()]);
    }

    public int size() {
        return getCommandline().length;
    }

    public Object clone() {
        try {
            Commandline c = (Commandline) super.clone();
            c.arguments = new ArrayList(this.arguments);
            return c;
        } catch (CloneNotSupportedException e) {
            throw new BuildException(e);
        }
    }

    public void clear() {
        this.executable = null;
        this.arguments.clear();
    }

    public void clearArgs() {
        this.arguments.clear();
    }

    public Marker createMarker() {
        return new Marker(this.arguments.size());
    }

    public String describeCommand() {
        return describeCommand(this);
    }

    public String describeArguments() {
        return describeArguments(this);
    }

    public static String describeCommand(Commandline line) {
        return describeCommand(line.getCommandline());
    }

    public static String describeArguments(Commandline line) {
        return describeArguments(line.getArguments());
    }

    public static String describeCommand(String[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        StringBuilder buf = new StringBuilder("Executing '").append(args[0]).append("'");
        if (args.length > 1) {
            buf.append(" with ");
            buf.append(describeArguments(args, 1));
        } else {
            buf.append(DISCLAIMER);
        }
        return buf.toString();
    }

    public static String describeArguments(String[] args) {
        return describeArguments(args, 0);
    }

    protected static String describeArguments(String[] args, int offset) {
        if (args == null || args.length <= offset) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        Object[] objArr = new Object[1];
        objArr[0] = args.length > offset ? "s" : "";
        buf.append(String.format("argument%s:%n", objArr));
        for (int i = offset; i < args.length; i++) {
            buf.append(String.format("'%s'%n", args[i]));
        }
        buf.append(DISCLAIMER);
        return buf.toString();
    }

    public Iterator<Argument> iterator() {
        return this.arguments.iterator();
    }
}
