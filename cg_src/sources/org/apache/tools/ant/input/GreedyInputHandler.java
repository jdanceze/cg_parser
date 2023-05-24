package org.apache.tools.ant.input;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.StreamPumper;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/input/GreedyInputHandler.class */
public class GreedyInputHandler extends DefaultInputHandler {
    @Override // org.apache.tools.ant.input.DefaultInputHandler, org.apache.tools.ant.input.InputHandler
    public void handleInput(InputRequest request) throws BuildException {
        String prompt = getPrompt(request);
        InputStream in = null;
        try {
            in = getInputStream();
            System.err.println(prompt);
            System.err.flush();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamPumper p = new StreamPumper(in, baos);
            Thread t = new Thread(p);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                try {
                    t.join();
                } catch (InterruptedException e2) {
                }
            }
            request.setInput(new String(baos.toByteArray()));
            if (!request.isInputValid()) {
                throw new BuildException("Received invalid console input");
            }
            if (p.getException() != null) {
                throw new BuildException("Failed to read input from console", p.getException());
            }
        } finally {
            FileUtils.close(in);
        }
    }
}
