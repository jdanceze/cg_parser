package org.apache.commons.cli;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import javax.resource.spi.work.WorkException;
/* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/Parser.class */
public abstract class Parser implements CommandLineParser {
    protected CommandLine cmd;
    private Options options;
    private List requiredOptions;

    protected abstract String[] flatten(Options options, String[] strArr, boolean z);

    protected void setOptions(Options options) {
        this.options = options;
        this.requiredOptions = new ArrayList(options.getRequiredOptions());
    }

    protected Options getOptions() {
        return this.options;
    }

    protected List getRequiredOptions() {
        return this.requiredOptions;
    }

    @Override // org.apache.commons.cli.CommandLineParser
    public CommandLine parse(Options options, String[] arguments) throws ParseException {
        return parse(options, arguments, null, false);
    }

    public CommandLine parse(Options options, String[] arguments, Properties properties) throws ParseException {
        return parse(options, arguments, properties, false);
    }

    @Override // org.apache.commons.cli.CommandLineParser
    public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException {
        return parse(options, arguments, null, stopAtNonOption);
    }

    public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption) throws ParseException {
        for (Option opt : options.helpOptions()) {
            opt.clearValues();
        }
        setOptions(options);
        this.cmd = new CommandLine();
        boolean eatTheRest = false;
        if (arguments == null) {
            arguments = new String[0];
        }
        List tokenList = Arrays.asList(flatten(getOptions(), arguments, stopAtNonOption));
        ListIterator iterator = tokenList.listIterator();
        while (iterator.hasNext()) {
            String t = (String) iterator.next();
            if (HelpFormatter.DEFAULT_LONG_OPT_PREFIX.equals(t)) {
                eatTheRest = true;
            } else if (HelpFormatter.DEFAULT_OPT_PREFIX.equals(t)) {
                if (stopAtNonOption) {
                    eatTheRest = true;
                } else {
                    this.cmd.addArg(t);
                }
            } else if (t.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                if (stopAtNonOption && !getOptions().hasOption(t)) {
                    eatTheRest = true;
                    this.cmd.addArg(t);
                } else {
                    processOption(t, iterator);
                }
            } else {
                this.cmd.addArg(t);
                if (stopAtNonOption) {
                    eatTheRest = true;
                }
            }
            if (eatTheRest) {
                while (iterator.hasNext()) {
                    String str = (String) iterator.next();
                    if (!HelpFormatter.DEFAULT_LONG_OPT_PREFIX.equals(str)) {
                        this.cmd.addArg(str);
                    }
                }
            }
        }
        processProperties(properties);
        checkRequiredOptions();
        return this.cmd;
    }

    protected void processProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String option = e.nextElement().toString();
            if (!this.cmd.hasOption(option)) {
                Option opt = getOptions().getOption(option);
                String value = properties.getProperty(option);
                if (opt.hasArg()) {
                    if (opt.getValues() == null || opt.getValues().length == 0) {
                        try {
                            opt.addValueForProcessing(value);
                        } catch (RuntimeException e2) {
                        }
                    }
                } else if (!"yes".equalsIgnoreCase(value) && !"true".equalsIgnoreCase(value) && !WorkException.START_TIMED_OUT.equalsIgnoreCase(value)) {
                    return;
                }
                this.cmd.addOption(opt);
            }
        }
    }

    protected void checkRequiredOptions() throws MissingOptionException {
        if (!getRequiredOptions().isEmpty()) {
            throw new MissingOptionException(getRequiredOptions());
        }
    }

    public void processArgs(Option opt, ListIterator iter) throws ParseException {
        while (true) {
            if (!iter.hasNext()) {
                break;
            }
            String str = (String) iter.next();
            if (getOptions().hasOption(str) && str.startsWith(HelpFormatter.DEFAULT_OPT_PREFIX)) {
                iter.previous();
                break;
            } else {
                try {
                    opt.addValueForProcessing(Util.stripLeadingAndTrailingQuotes(str));
                } catch (RuntimeException e) {
                    iter.previous();
                }
            }
        }
        if (opt.getValues() == null && !opt.hasOptionalArg()) {
            throw new MissingArgumentException(opt);
        }
    }

    protected void processOption(String arg, ListIterator iter) throws ParseException {
        boolean hasOption = getOptions().hasOption(arg);
        if (!hasOption) {
            throw new UnrecognizedOptionException(new StringBuffer().append("Unrecognized option: ").append(arg).toString(), arg);
        }
        Option opt = (Option) getOptions().getOption(arg).clone();
        if (opt.isRequired()) {
            getRequiredOptions().remove(opt.getKey());
        }
        if (getOptions().getOptionGroup(opt) != null) {
            OptionGroup group = getOptions().getOptionGroup(opt);
            if (group.isRequired()) {
                getRequiredOptions().remove(group);
            }
            group.setSelected(opt);
        }
        if (opt.hasArg()) {
            processArgs(opt, iter);
        }
        this.cmd.addOption(opt);
    }
}
