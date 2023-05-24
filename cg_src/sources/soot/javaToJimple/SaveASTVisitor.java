package soot.javaToJimple;

import java.util.HashMap;
import polyglot.frontend.AbstractPass;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Job;
import polyglot.frontend.Pass;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/SaveASTVisitor.class */
public class SaveASTVisitor extends AbstractPass {
    private Job job;
    private ExtensionInfo extInfo;

    public SaveASTVisitor(Pass.ID id, Job job, ExtensionInfo extInfo) {
        super(id);
        this.job = job;
        this.extInfo = extInfo;
    }

    @Override // polyglot.frontend.AbstractPass, polyglot.frontend.Pass
    public boolean run() {
        if (this.extInfo instanceof soot.javaToJimple.jj.ExtensionInfo) {
            soot.javaToJimple.jj.ExtensionInfo jjInfo = (soot.javaToJimple.jj.ExtensionInfo) this.extInfo;
            if (jjInfo.sourceJobMap() == null) {
                jjInfo.sourceJobMap(new HashMap<>());
            }
            jjInfo.sourceJobMap().put(this.job.source(), this.job);
            return true;
        }
        return false;
    }
}
