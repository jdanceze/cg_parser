package soot.jimple.infoflow.android.iccta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.iccta.Ic3Data;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Provider.class */
public class Ic3Provider implements IccLinkProvider {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String ic3Model;

    public Ic3Provider(String ic3Model) {
        this.ic3Model = null;
        this.ic3Model = ic3Model;
    }

    @Override // soot.jimple.infoflow.android.iccta.IccLinkProvider
    public List<IccLink> getIccLinks() {
        SootMethod fromSM;
        List<IccLink> iccLinks = new ArrayList<>();
        App app = Ic3ResultLoader.load(this.ic3Model);
        if (app == null) {
            this.logger.error("[IccTA] %s is not a valid IC3 model", this.ic3Model);
            return iccLinks;
        }
        Set<Intent> intents = app.getIntents();
        for (Intent intent : intents) {
            if (intent.isImplicit()) {
                if (intent.getAction() != null) {
                    List<Ic3Data.Application.Component> targetedComps = intent.resolve(app.getComponentList());
                    for (Ic3Data.Application.Component targetComp : targetedComps) {
                        if (availableTargetedComponent(intent.getApp(), targetComp.getName())) {
                            SootMethod fromSM2 = Scene.v().grabMethod(intent.getLoggingPoint().getCallerMethodSignature());
                            Stmt fromU = linkWithTarget(fromSM2, intent.getLoggingPoint().getStmtSequence());
                            IccLink iccLink = new IccLink(fromSM2, fromU, Scene.v().getSootClassUnsafe(targetComp.getName()));
                            iccLink.setExit_kind(targetComp.getKind().name());
                            iccLinks.add(iccLink);
                        }
                    }
                }
            } else {
                String targetCompName = intent.getComponentClass();
                if (availableTargetedComponent(intent.getApp(), targetCompName) && (fromSM = Scene.v().grabMethod(intent.getLoggingPoint().getCallerMethodSignature())) != null) {
                    Stmt fromU2 = linkWithTarget(fromSM, intent.getLoggingPoint().getStmtSequence());
                    IccLink iccLink2 = new IccLink(fromSM, fromU2, Scene.v().getSootClassUnsafe(targetCompName));
                    for (Ic3Data.Application.Component comp : intent.getApp().getComponentList()) {
                        if (comp.getName().equals(targetCompName)) {
                            iccLink2.setExit_kind(comp.getKind().name());
                        }
                    }
                    iccLinks.add(iccLink2);
                }
            }
        }
        return iccLinks;
    }

    private Stmt linkWithTarget(SootMethod fromSM, int stmtIdx) {
        Body body = fromSM.retrieveActiveBody();
        int i = 0;
        Iterator<Unit> iter = body.getUnits().snapshotIterator();
        while (iter.hasNext()) {
            Stmt stmt = (Stmt) iter.next();
            if (i == stmtIdx) {
                return stmt;
            }
            i++;
        }
        return null;
    }

    private boolean availableTargetedComponent(App app, String targetedComponentName) {
        for (Ic3Data.Application.Component comp : app.getComponentList()) {
            if (comp.getName().equals(targetedComponentName)) {
                return true;
            }
        }
        return false;
    }
}
