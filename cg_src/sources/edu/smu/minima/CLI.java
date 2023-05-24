package edu.smu.minima;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
/* loaded from: gencallgraphv3.jar:edu/smu/minima/CLI.class */
public class CLI {
    private Options options = new Options();

    public CLI() {
        addOption(new Option("a", "apk", true, "the apk path"));
        addOption(new Option("s", "sdk", true, "Android platforms directory path, e.g., /usr/lib/android-sdk/platforms/"));
        addOption(new Option("o", "output_file", true, "file to output the call graph"));
    }

    private void addOption(Option option) {
        this.options.addOption(option);
    }

    public Options getOptions() {
        return this.options;
    }
}
