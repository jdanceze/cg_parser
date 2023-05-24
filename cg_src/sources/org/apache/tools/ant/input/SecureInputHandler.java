package org.apache.tools.ant.input;

import java.util.Arrays;
import org.apache.tools.ant.BuildException;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/input/SecureInputHandler.class */
public class SecureInputHandler extends DefaultInputHandler {
    @Override // org.apache.tools.ant.input.DefaultInputHandler, org.apache.tools.ant.input.InputHandler
    public void handleInput(InputRequest request) throws BuildException {
        String prompt = getPrompt(request);
        do {
            char[] input = System.console().readPassword(prompt, new Object[0]);
            if (input == null) {
                throw new BuildException("unexpected end of stream while reading input");
            }
            request.setInput(new String(input));
            Arrays.fill(input, ' ');
        } while (!request.isInputValid());
    }
}
