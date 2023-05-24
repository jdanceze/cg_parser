package soot.JastAddJ;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.taskdefs.optional.SchemaValidate;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/Options.class */
public class Options {
    private Map options = new HashMap();
    private Map optionDescriptions = new HashMap();
    private HashSet files = new HashSet();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/JastAddJ/Options$Option.class */
    public static class Option {
        public String name;
        public boolean hasValue;
        public boolean isCollection;

        public Option(String name, boolean hasValue, boolean isCollection) {
            this.name = name;
            this.hasValue = hasValue;
            this.isCollection = isCollection;
        }
    }

    public Collection files() {
        return this.files;
    }

    public void initOptions() {
        this.options = new HashMap();
        this.optionDescriptions = new HashMap();
        this.files = new HashSet();
    }

    public void addKeyOption(String name) {
        if (this.optionDescriptions.containsKey(name)) {
            throw new Error("Command line definition error: option description for " + name + " is multiply declared");
        }
        this.optionDescriptions.put(name, new Option(name, false, false));
    }

    public void addKeyValueOption(String name) {
        if (this.optionDescriptions.containsKey(name)) {
            throw new Error("Command line definition error: option description for " + name + " is multiply declared");
        }
        this.optionDescriptions.put(name, new Option(name, true, false));
    }

    public void addKeyCollectionOption(String name) {
        if (this.optionDescriptions.containsKey(name)) {
            throw new Error("Command line definition error: option description for " + name + " is multiply declared");
        }
        this.optionDescriptions.put(name, new Option(name, true, true));
    }

    public void addOptionDescription(String name, boolean value) {
        if (this.optionDescriptions.containsKey(name)) {
            throw new Error("Command line definition error: option description for " + name + " is multiply declared");
        }
        this.optionDescriptions.put(name, new Option(name, value, false));
    }

    public void addOptionDescription(String name, boolean value, boolean isCollection) {
        if (this.optionDescriptions.containsKey(name)) {
            throw new Error("Command line definition error: option description for " + name + " is multiply declared");
        }
        this.optionDescriptions.put(name, new Option(name, value, isCollection));
    }

    public void addOptions(String[] args) {
        int i = 0;
        while (i < args.length) {
            String arg = args[i];
            if (arg.startsWith("@")) {
                try {
                    String fileName = arg.substring(1, arg.length());
                    FileReader r = new FileReader(fileName);
                    StreamTokenizer tokenizer = new StreamTokenizer(r);
                    tokenizer.resetSyntax();
                    tokenizer.whitespaceChars(32, 32);
                    tokenizer.whitespaceChars(9, 9);
                    tokenizer.whitespaceChars(12, 12);
                    tokenizer.whitespaceChars(10, 10);
                    tokenizer.whitespaceChars(13, 13);
                    tokenizer.wordChars(33, 255);
                    ArrayList list = new ArrayList();
                    for (int next = tokenizer.nextToken(); next != -1; next = tokenizer.nextToken()) {
                        if (next == -3) {
                            list.add(tokenizer.sval);
                        }
                    }
                    String[] newArgs = new String[list.size()];
                    int index = 0;
                    Iterator iter = list.iterator();
                    while (iter.hasNext()) {
                        newArgs[index] = (String) iter.next();
                        index++;
                    }
                    addOptions(newArgs);
                    r.close();
                } catch (FileNotFoundException e) {
                    System.err.println(SchemaValidate.SchemaLocation.ERROR_NO_FILE + arg.substring(1));
                } catch (IOException e2) {
                    System.err.println("Exception: " + e2.getMessage());
                }
            } else if (arg.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                if (!this.optionDescriptions.containsKey(arg)) {
                    throw new Error("Command line argument error: option " + arg + " is not defined");
                }
                Option o = (Option) this.optionDescriptions.get(arg);
                if (!o.isCollection && this.options.containsKey(arg)) {
                    throw new Error("Command line argument error: option " + arg + " is multiply defined");
                }
                if (o.hasValue && !o.isCollection) {
                    if (i + 1 > args.length - 1) {
                        throw new Error("Command line argument error: value missing for key " + arg);
                    }
                    String value = args[i + 1];
                    if (value.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                        throw new Error("Command line argument error: value missing for key " + arg);
                    }
                    i++;
                    this.options.put(arg, value);
                } else if (o.hasValue && o.isCollection) {
                    if (i + 1 > args.length - 1) {
                        throw new Error("Command line argument error: value missing for key " + arg);
                    }
                    String value2 = args[i + 1];
                    if (value2.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                        throw new Error("Command line argument error: value missing for key " + arg);
                    }
                    i++;
                    Collection c = (Collection) this.options.get(arg);
                    if (c == null) {
                        c = new ArrayList();
                    }
                    c.add(value2);
                    this.options.put(arg, c);
                } else {
                    this.options.put(arg, null);
                }
            } else {
                this.files.add(arg);
            }
            i++;
        }
    }

    public boolean hasOption(String name) {
        return this.options.containsKey(name);
    }

    public void setOption(String name) {
        this.options.put(name, null);
    }

    public boolean hasValueForOption(String name) {
        return this.options.containsKey(name) && this.options.get(name) != null;
    }

    public String getValueForOption(String name) {
        if (!hasValueForOption(name)) {
            throw new Error("Command line argument error: key " + name + " does not have a value");
        }
        return (String) this.options.get(name);
    }

    public void setValueForOption(String value, String option) {
        this.options.put(option, value);
    }

    public Collection getValueCollectionForOption(String name) {
        if (!hasValueForOption(name)) {
            throw new Error("Command line argument error: key " + name + " does not have a value");
        }
        return (Collection) this.options.get(name);
    }

    public boolean verbose() {
        return hasOption(SOSCmd.FLAG_VERBOSE);
    }
}
