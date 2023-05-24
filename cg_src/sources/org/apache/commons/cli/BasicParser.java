package org.apache.commons.cli;
/* loaded from: gencallgraphv3.jar:commons-cli-1.2.jar:org/apache/commons/cli/BasicParser.class */
public class BasicParser extends Parser {
    @Override // org.apache.commons.cli.Parser
    protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
        return arguments;
    }
}
