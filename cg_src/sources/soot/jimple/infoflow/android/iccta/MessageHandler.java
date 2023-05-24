package soot.jimple.infoflow.android.iccta;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.Scene;
import soot.SootClass;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/MessageHandler.class */
public class MessageHandler {
    private static MessageHandler instance = new MessageHandler();
    private Set<SootClass> handlerImpls = null;

    private MessageHandler() {
    }

    public static MessageHandler v() {
        return instance;
    }

    public Set<SootClass> getAllHandlers() {
        if (this.handlerImpls == null) {
            this.handlerImpls = new HashSet();
            SootClass handler = Scene.v().getSootClass("android.os.Handler");
            Iterator<SootClass> iter = Scene.v().getApplicationClasses().snapshotIterator();
            while (iter.hasNext()) {
                SootClass sootClass = iter.next();
                while (true) {
                    if (sootClass != null) {
                        if (sootClass.getName().equals(handler.getName())) {
                            this.handlerImpls.add(sootClass);
                            break;
                        }
                        sootClass = sootClass.getSuperclassUnsafe();
                    }
                }
            }
        }
        return this.handlerImpls;
    }
}
