package org.apache.tools.ant.taskdefs;

import java.util.List;
import org.apache.http.cookie.ClientCookie;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.input.DefaultInputHandler;
import org.apache.tools.ant.input.GreedyInputHandler;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import org.apache.tools.ant.input.MultipleChoiceInputRequest;
import org.apache.tools.ant.input.PropertyFileInputHandler;
import org.apache.tools.ant.input.SecureInputHandler;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.util.ClasspathUtils;
import org.apache.tools.ant.util.StringUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Input.class */
public class Input extends Task {
    private String validargs = null;
    private String message = "";
    private String addproperty = null;
    private String defaultvalue = null;
    private Handler handler = null;
    private boolean messageAttribute;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Input$Handler.class */
    public class Handler extends DefBase {
        private String refid = null;
        private HandlerType type = null;
        private String classname = null;

        public Handler() {
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        public String getRefid() {
            return this.refid;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getClassname() {
            return this.classname;
        }

        public void setType(HandlerType type) {
            this.type = type;
        }

        public HandlerType getType() {
            return this.type;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public InputHandler getInputHandler() {
            if (this.type == null) {
                if (this.refid != null) {
                    try {
                        return (InputHandler) getProject().getReference(this.refid);
                    } catch (ClassCastException e) {
                        throw new BuildException(this.refid + " does not denote an InputHandler", e);
                    }
                } else if (this.classname != null) {
                    return (InputHandler) ClasspathUtils.newInstance(this.classname, createLoader(), InputHandler.class);
                } else {
                    throw new BuildException("Must specify refid, classname or type");
                }
            }
            return this.type.getInputHandler();
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/Input$HandlerType.class */
    public static class HandlerType extends EnumeratedAttribute {
        private static final String[] VALUES = {"default", "propertyfile", "greedy", ClientCookie.SECURE_ATTR};
        private static final InputHandler[] HANDLERS = {new DefaultInputHandler(), new PropertyFileInputHandler(), new GreedyInputHandler(), new SecureInputHandler()};

        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return VALUES;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public InputHandler getInputHandler() {
            return HANDLERS[getIndex()];
        }
    }

    public void setValidargs(String validargs) {
        this.validargs = validargs;
    }

    public void setAddproperty(String addproperty) {
        this.addproperty = addproperty;
    }

    public void setMessage(String message) {
        this.message = message;
        this.messageAttribute = true;
    }

    public void setDefaultvalue(String defaultvalue) {
        this.defaultvalue = defaultvalue;
    }

    public void addText(String msg) {
        if (this.messageAttribute && msg.trim().isEmpty()) {
            return;
        }
        this.message += getProject().replaceProperties(msg);
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        InputRequest request;
        InputHandler inputHandler;
        if (this.addproperty != null && getProject().getProperty(this.addproperty) != null) {
            log("skipping " + getTaskName() + " as property " + this.addproperty + " has already been set.");
            return;
        }
        if (this.validargs != null) {
            List<String> accept = StringUtils.split(this.validargs, 44);
            request = new MultipleChoiceInputRequest(this.message, accept);
        } else {
            request = new InputRequest(this.message);
        }
        request.setDefaultValue(this.defaultvalue);
        if (this.handler != null) {
            inputHandler = this.handler.getInputHandler();
        } else {
            inputHandler = getProject().getInputHandler();
        }
        InputHandler h = inputHandler;
        h.handleInput(request);
        String value = request.getInput();
        if ((value == null || value.trim().isEmpty()) && this.defaultvalue != null) {
            value = this.defaultvalue;
        }
        if (this.addproperty != null && value != null) {
            getProject().setNewProperty(this.addproperty, value);
        }
    }

    public Handler createHandler() {
        if (this.handler != null) {
            throw new BuildException("Cannot define > 1 nested input handler");
        }
        this.handler = new Handler();
        return this.handler;
    }
}
