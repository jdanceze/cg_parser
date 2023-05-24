package soot.util.cfgcmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.CompilationDeathException;
/* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/CFGOptionMatcher.class */
public class CFGOptionMatcher {
    private static final Logger logger = LoggerFactory.getLogger(CFGOptionMatcher.class);
    private final CFGOption[] options;

    /* loaded from: gencallgraphv3.jar:soot/util/cfgcmd/CFGOptionMatcher$CFGOption.class */
    public static abstract class CFGOption {
        private final String name;

        /* JADX INFO: Access modifiers changed from: protected */
        public CFGOption(String name) {
            this.name = name;
        }

        public String name() {
            return this.name;
        }
    }

    public CFGOptionMatcher(CFGOption[] options) {
        this.options = options;
    }

    public CFGOption match(String quarry) throws CompilationDeathException {
        String uncasedQuarry = quarry.toLowerCase();
        int match = -1;
        for (int i = 0; i < this.options.length; i++) {
            String uncasedName = this.options[i].name().toLowerCase();
            if (uncasedName.startsWith(uncasedQuarry)) {
                if (match == -1) {
                    match = i;
                } else {
                    logger.debug(quarry + " is ambiguous; it matches " + this.options[match].name() + " and " + this.options[i].name());
                    throw new CompilationDeathException(0, "Option parse error");
                }
            }
        }
        if (match == -1) {
            logger.debug("\"" + quarry + "\" does not match any value.");
            throw new CompilationDeathException(0, "Option parse error");
        }
        return this.options[match];
    }

    public String help(int initialIndent, int rightMargin, int hangingIndent) {
        StringBuilder newLineBuf = new StringBuilder(2 + rightMargin);
        newLineBuf.append('\n');
        if (hangingIndent < 0) {
            hangingIndent = 0;
        }
        for (int i = 0; i < hangingIndent; i++) {
            newLineBuf.append(' ');
        }
        String newLine = newLineBuf.toString();
        StringBuilder result = new StringBuilder();
        int lineLength = 0;
        for (int i2 = 0; i2 < initialIndent; i2++) {
            lineLength++;
            result.append(' ');
        }
        for (int i3 = 0; i3 < this.options.length; i3++) {
            if (i3 > 0) {
                result.append('|');
                lineLength++;
            }
            String name = this.options[i3].name();
            int nameLength = name.length();
            if (lineLength + nameLength > rightMargin) {
                result.append(newLine);
                lineLength = hangingIndent;
            }
            result.append(name);
            lineLength += nameLength;
        }
        return result.toString();
    }
}
