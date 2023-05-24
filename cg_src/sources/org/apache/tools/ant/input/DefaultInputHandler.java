package org.apache.tools.ant.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.util.KeepAliveInputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/input/DefaultInputHandler.class */
public class DefaultInputHandler implements InputHandler {
    @Override // org.apache.tools.ant.input.InputHandler
    public void handleInput(InputRequest request) throws BuildException {
        String prompt = getPrompt(request);
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(getInputStream()));
            do {
                System.err.println(prompt);
                System.err.flush();
                try {
                    String input = r.readLine();
                    if (input == null) {
                        throw new BuildException("unexpected end of stream while reading input");
                    }
                    request.setInput(input);
                } catch (IOException e) {
                    throw new BuildException("Failed to read input from Console.", e);
                }
            } while (!request.isInputValid());
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e2) {
                    if (1 != 0) {
                        throw new BuildException("Failed to close input.", e2);
                    }
                }
            }
        } catch (Throwable th) {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException e3) {
                    if (0 != 0) {
                        throw new BuildException("Failed to close input.", e3);
                    }
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getPrompt(InputRequest request) {
        String prompt = request.getPrompt();
        String def = request.getDefaultValue();
        if (request instanceof MultipleChoiceInputRequest) {
            StringBuilder sb = new StringBuilder(prompt).append(" (");
            boolean first = true;
            Iterator<String> it = ((MultipleChoiceInputRequest) request).getChoices().iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (!first) {
                    sb.append(", ");
                }
                if (next.equals(def)) {
                    sb.append('[');
                }
                sb.append(next);
                if (next.equals(def)) {
                    sb.append(']');
                }
                first = false;
            }
            sb.append(")");
            return sb.toString();
        } else if (def != null) {
            return prompt + " [" + def + "]";
        } else {
            return prompt;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public InputStream getInputStream() {
        return KeepAliveInputStream.wrapSystemIn();
    }
}
