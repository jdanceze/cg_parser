package org.apache.tools.ant.dispatch;

import android.hardware.Camera;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/dispatch/DispatchTask.class */
public abstract class DispatchTask extends Task implements Dispatchable {
    private String action;

    @Override // org.apache.tools.ant.dispatch.Dispatchable
    public String getActionParameterName() {
        return Camera.Parameters.SCENE_MODE_ACTION;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return this.action;
    }
}
