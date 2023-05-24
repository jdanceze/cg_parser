package soot.jimple.infoflow.android.iccta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.Local;
import soot.LocalGenerator;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointCollection;
import soot.jimple.infoflow.entryPointCreators.SimulatedCodeElementTag;
import soot.jimple.infoflow.handlers.PreAnalysisHandler;
import soot.util.Chain;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/IccInstrumenter.class */
public class IccInstrumenter implements PreAnalysisHandler {
    protected final String iccModel;
    protected final SootClass dummyMainClass;
    protected final ComponentEntryPointCollection componentToEntryPoint;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected IccRedirectionCreator redirectionCreator = null;
    protected final Set<SootMethod> processedMethods = new HashSet();
    protected final MultiMap<Body, Unit> instrumentedUnits = new HashMultiMap();
    protected final SootMethod smMessengerSend = Scene.v().grabMethod("<android.os.Messenger: void send(android.os.Message)>");
    protected final SootMethod smSendMessage = Scene.v().grabMethod("<android.os.Handler: boolean sendMessage(android.os.Message)>");

    public IccInstrumenter(String iccModel, SootClass dummyMainClass, ComponentEntryPointCollection componentToEntryPoint) {
        this.iccModel = iccModel;
        this.dummyMainClass = dummyMainClass;
        this.componentToEntryPoint = componentToEntryPoint;
    }

    @Override // soot.jimple.infoflow.handlers.PreAnalysisHandler
    public void onBeforeCallgraphConstruction() {
        this.logger.info("[IccTA] Launching IccTA Transformer...");
        this.logger.info("[IccTA] Loading the ICC Model...");
        Ic3Provider provider = new Ic3Provider(this.iccModel);
        List<IccLink> iccLinks = provider.getIccLinks();
        this.logger.info("[IccTA] ...End Loading the ICC Model");
        if (this.redirectionCreator == null) {
            this.redirectionCreator = new IccRedirectionCreator(this.dummyMainClass, this.componentToEntryPoint);
        } else {
            this.redirectionCreator.undoInstrumentation();
        }
        this.logger.info("[IccTA] Lauching ICC Redirection Creation...");
        for (IccLink link : iccLinks) {
            if (link.fromU != null) {
                this.redirectionCreator.redirectToDestination(link);
            }
        }
        undoInstrumentation();
        instrumentMessenger();
        this.processedMethods.clear();
        this.logger.info("[IccTA] ...End ICC Redirection Creation");
    }

    protected void undoInstrumentation() {
        for (Body body : this.instrumentedUnits.keySet()) {
            for (Unit u : this.instrumentedUnits.get(body)) {
                body.getUnits().remove(u);
            }
        }
        this.instrumentedUnits.clear();
    }

    protected void instrumentMessenger() {
        this.logger.info("Launching Messenger Transformer...");
        Chain<SootClass> applicationClasses = Scene.v().getApplicationClasses();
        Map<SootClass, Map<Value, String>> appClasses = new HashMap<>();
        Map<String, String> handlerInner = new HashMap<>();
        Iterator<SootClass> iter = applicationClasses.snapshotIterator();
        while (iter.hasNext()) {
            SootClass sootClass = iter.next();
            Map<Value, String> handlerClass = new HashMap<>();
            List<SootMethod> methodCopyList = new ArrayList<>(sootClass.getMethods());
            for (SootMethod sootMethod : methodCopyList) {
                if (sootMethod.isConcrete()) {
                    Body body = sootMethod.retrieveActiveBody();
                    if (this.processedMethods.add(sootMethod)) {
                        Iterator<Unit> unitIter = body.getUnits().snapshotIterator();
                        while (unitIter.hasNext()) {
                            Unit unit = unitIter.next();
                            Stmt stmt = (Stmt) unit;
                            if (stmt.containsFieldRef() && (stmt.getFieldRef().getType() instanceof RefType) && ((RefType) stmt.getFieldRef().getType()).getSootClass().getName().contains("android.os.Handler")) {
                                if (stmt.getUseBoxes().size() > 1) {
                                    handlerInner.putIfAbsent(stmt.getFieldRef().getField().getSignature(), stmt.getUseBoxes().get(1).getValue().getType().toString());
                                }
                                handlerClass.put(stmt.getDefBoxes().get(0).getValue(), stmt.getFieldRef().getField().getSignature());
                            }
                        }
                    }
                }
            }
            appClasses.putIfAbsent(sootClass, handlerClass);
        }
        for (SootClass sc : appClasses.keySet()) {
            if (sc != null) {
                generateSendMessage(sc, appClasses.get(sc), handlerInner);
            }
        }
    }

    public void generateSendMessage(SootClass sootClass, Map<Value, String> appClasses, Map<String, String> handlerInner) {
        SootMethod callee;
        if (appClasses.isEmpty() || handlerInner.isEmpty()) {
            return;
        }
        List<SootMethod> methodCopyList = new ArrayList<>(sootClass.getMethods());
        for (SootMethod sootMethod : methodCopyList) {
            if (sootMethod.isConcrete()) {
                Body body = sootMethod.retrieveActiveBody();
                LocalGenerator lg = Scene.v().createLocalGenerator(body);
                Iterator<Unit> unitIter = body.getUnits().snapshotIterator();
                while (unitIter.hasNext()) {
                    Unit unit = unitIter.next();
                    Stmt stmt = (Stmt) unit;
                    if (stmt.containsInvokeExpr() && ((callee = stmt.getInvokeExpr().getMethod()) == this.smMessengerSend || callee == this.smSendMessage)) {
                        String hc = appClasses.get(stmt.getInvokeExpr().getUseBoxes().get(1).getValue());
                        Set<SootClass> handlers = MessageHandler.v().getAllHandlers();
                        Iterator<SootClass> it = handlers.iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            SootClass handler = it.next();
                            if (hc != null && handlerInner.get(hc) == handler.getName()) {
                                Local handlerLocal = lg.generateLocal(handler.getType());
                                SootMethod hmMethod = handler.getMethod("void handleMessage(android.os.Message)");
                                InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(handlerLocal, hmMethod.makeRef(), stmt.getInvokeExpr().getArg(0)));
                                newInvokeStmt.addTag(SimulatedCodeElementTag.TAG);
                                body.getUnits().insertAfter(newInvokeStmt, (InvokeStmt) stmt);
                                this.instrumentedUnits.put(body, newInvokeStmt);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.handlers.PreAnalysisHandler
    public void onAfterCallgraphConstruction() {
    }
}
