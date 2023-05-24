package soot.javaToJimple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.apache.tools.tar.TarConstants;
import polyglot.ast.ArrayAccess;
import polyglot.ast.ArrayInit;
import polyglot.ast.Assert;
import polyglot.ast.Assign;
import polyglot.ast.Binary;
import polyglot.ast.Block;
import polyglot.ast.BooleanLit;
import polyglot.ast.Branch;
import polyglot.ast.Call;
import polyglot.ast.Case;
import polyglot.ast.Cast;
import polyglot.ast.Catch;
import polyglot.ast.CharLit;
import polyglot.ast.ClassLit;
import polyglot.ast.Conditional;
import polyglot.ast.ConstructorCall;
import polyglot.ast.Do;
import polyglot.ast.Empty;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.Field;
import polyglot.ast.FieldDecl;
import polyglot.ast.FloatLit;
import polyglot.ast.For;
import polyglot.ast.Formal;
import polyglot.ast.If;
import polyglot.ast.Instanceof;
import polyglot.ast.IntLit;
import polyglot.ast.Labeled;
import polyglot.ast.Lit;
import polyglot.ast.LocalClassDecl;
import polyglot.ast.LocalDecl;
import polyglot.ast.New;
import polyglot.ast.NewArray;
import polyglot.ast.Node;
import polyglot.ast.NullLit;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Receiver;
import polyglot.ast.Return;
import polyglot.ast.Special;
import polyglot.ast.StringLit;
import polyglot.ast.Switch;
import polyglot.ast.SwitchBlock;
import polyglot.ast.Synchronized;
import polyglot.ast.Throw;
import polyglot.ast.Try;
import polyglot.ast.TypeNode;
import polyglot.ast.Unary;
import polyglot.ast.While;
import polyglot.types.ArrayType;
import polyglot.types.ClassType;
import polyglot.types.ConstructorInstance;
import polyglot.types.LocalInstance;
import polyglot.types.MemberInstance;
import polyglot.types.MethodInstance;
import polyglot.types.NullType;
import polyglot.types.PrimitiveType;
import polyglot.util.IdentityKey;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FastHierarchy;
import soot.FloatType;
import soot.IntType;
import soot.JavaBasicTypes;
import soot.Local;
import soot.LocalGenerator;
import soot.LongType;
import soot.MethodSource;
import soot.Modifier;
import soot.PrimType;
import soot.RefType;
import soot.Scene;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootFieldRef;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Trap;
import soot.Type;
import soot.UnitPatchingChain;
import soot.Value;
import soot.VoidType;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.ConditionExpr;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.EnterMonitorStmt;
import soot.jimple.EqExpr;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.GeExpr;
import soot.jimple.GotoStmt;
import soot.jimple.GtExpr;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.LeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.LongConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.LtExpr;
import soot.jimple.NeExpr;
import soot.jimple.NegExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NopStmt;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.TableSwitchStmt;
import soot.jimple.ThisRef;
import soot.jimple.ThrowStmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.XorExpr;
import soot.jimple.infoflow.android.source.parsers.xml.XMLConstants;
import soot.tagkit.EnclosingTag;
import soot.tagkit.ParamNamesTag;
import soot.tagkit.SyntheticTag;
import soot.tagkit.ThrowCreatedByCompilerTag;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/JimpleBodyBuilder.class */
public class JimpleBodyBuilder extends AbstractJimpleBodyBuilder {
    protected List<List<Stmt>> beforeReturn;
    protected List<List<Stmt>> afterReturn;
    protected ArrayList<Trap> exceptionTable;
    protected Stack<Value> monitorStack;
    protected Stack<Try> tryStack;
    protected Stack<Try> catchStack;
    protected HashMap<String, Stmt> labelBreakMap;
    protected HashMap<String, Stmt> labelContinueMap;
    protected HashMap<polyglot.ast.Stmt, Stmt> labelMap;
    protected Local specialThisLocal;
    protected Local outerClassParamLocal;
    protected LocalGenerator lg;
    protected Stack<Stmt> endControlNoop = new Stack<>();
    protected Stack<Stmt> condControlNoop = new Stack<>();
    protected Stack<Stmt> trueNoop = new Stack<>();
    protected Stack<Stmt> falseNoop = new Stack<>();
    protected HashMap<IdentityKey, Local> localsMap = new HashMap<>();
    protected HashMap getThisMap = new HashMap();
    protected int paramRefCount = 0;
    int inLeftOr = 0;

    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public JimpleBody createJimpleBody(Block block, List formals, SootMethod sootMethod) {
        SootField this0Field;
        createBody(sootMethod);
        this.lg = Scene.v().createLocalGenerator(this.body);
        if (!Modifier.isStatic(sootMethod.getModifiers())) {
            RefType type = sootMethod.getDeclaringClass().getType();
            this.specialThisLocal = Jimple.v().newLocal("this", type);
            this.body.getLocals().add(this.specialThisLocal);
            ThisRef thisRef = Jimple.v().newThisRef(type);
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(this.specialThisLocal, thisRef));
        }
        int formalsCounter = 0;
        int outerIndex = sootMethod.getDeclaringClass().getName().lastIndexOf("$");
        if (outerIndex != -1 && sootMethod.getName().equals("<init>") && (this0Field = sootMethod.getDeclaringClass().getFieldByNameUnsafe("this$0")) != null) {
            SootClass outerClass = ((RefType) this0Field.getType()).getSootClass();
            Local outerLocal = this.lg.generateLocal(outerClass.getType());
            ParameterRef paramRef = Jimple.v().newParameterRef(outerClass.getType(), 0);
            this.paramRefCount++;
            IdentityStmt newIdentityStmt = Jimple.v().newIdentityStmt(outerLocal, paramRef);
            newIdentityStmt.addTag(new EnclosingTag());
            this.body.getUnits().add((UnitPatchingChain) newIdentityStmt);
            ((PolyglotMethodSource) sootMethod.getSource()).setOuterClassThisInit(outerLocal);
            this.outerClassParamLocal = outerLocal;
            formalsCounter = 0 + 1;
        }
        if (formals != null) {
            String[] formalNames = new String[formals.size()];
            Iterator formalsIt = formals.iterator();
            while (formalsIt.hasNext()) {
                Formal formal = (Formal) formalsIt.next();
                createFormal(formal, formalsCounter);
                formalNames[formalsCounter] = formal.name();
                formalsCounter++;
            }
            this.body.getMethod().addTag(new ParamNamesTag(formalNames));
        }
        ArrayList<SootField> finalsList = ((PolyglotMethodSource) this.body.getMethod().getSource()).getFinalsList();
        if (finalsList != null) {
            Iterator<SootField> finalsIt = finalsList.iterator();
            while (finalsIt.hasNext()) {
                SootField sf = finalsIt.next();
                ParameterRef paramRef2 = Jimple.v().newParameterRef(sf.getType(), formalsCounter);
                this.paramRefCount++;
                this.body.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(this.lg.generateLocal(sf.getType()), paramRef2));
                formalsCounter++;
            }
        }
        createBlock(block);
        if (sootMethod.getName().equals("<clinit>")) {
            handleAssert(sootMethod);
            handleStaticFieldInits(sootMethod);
            handleStaticInitializerBlocks(sootMethod);
        }
        boolean hasReturn = false;
        if (block != null) {
            for (Object next : block.statements()) {
                if (next instanceof Return) {
                    hasReturn = true;
                }
            }
        }
        Type retType = this.body.getMethod().getReturnType();
        if (!hasReturn && (retType instanceof VoidType)) {
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newReturnVoidStmt());
        }
        if (this.exceptionTable != null) {
            Iterator<Trap> trapsIt = this.exceptionTable.iterator();
            while (trapsIt.hasNext()) {
                this.body.getTraps().add(trapsIt.next());
            }
        }
        return this.body;
    }

    private void handleAssert(SootMethod sootMethod) {
        if (!((PolyglotMethodSource) sootMethod.getSource()).hasAssert()) {
            return;
        }
        ((PolyglotMethodSource) sootMethod.getSource()).addAssertInits(this.body);
    }

    private void handleFieldInits(SootMethod sootMethod) {
        ArrayList<FieldDecl> fieldInits = ((PolyglotMethodSource) sootMethod.getSource()).getFieldInits();
        if (fieldInits != null) {
            handleFieldInits(fieldInits);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleFieldInits(ArrayList<FieldDecl> fieldInits) {
        Value sootExpr;
        AssignStmt newAssignStmt;
        Iterator<FieldDecl> fieldInitsIt = fieldInits.iterator();
        while (fieldInitsIt.hasNext()) {
            FieldDecl field = fieldInitsIt.next();
            String fieldName = field.name();
            Expr initExpr = field.init();
            SootClass currentClass = this.body.getMethod().getDeclaringClass();
            SootFieldRef sootField = Scene.v().makeFieldRef(currentClass, fieldName, Util.getSootType(field.type().type()), field.flags().isStatic());
            Local base = this.specialThisLocal;
            FieldRef fieldRef = Jimple.v().newInstanceFieldRef(base, sootField);
            if (initExpr instanceof ArrayInit) {
                sootExpr = getArrayInitLocal((ArrayInit) initExpr, field.type().type());
            } else {
                sootExpr = base().createAggressiveExpr(initExpr, false, false);
            }
            if (sootExpr instanceof ConditionExpr) {
                sootExpr = handleCondBinExpr((ConditionExpr) sootExpr);
            }
            if (sootExpr instanceof Local) {
                newAssignStmt = Jimple.v().newAssignStmt(fieldRef, sootExpr);
            } else if (sootExpr instanceof Constant) {
                newAssignStmt = Jimple.v().newAssignStmt(fieldRef, sootExpr);
            } else {
                throw new RuntimeException("fields must assign to local or constant only");
            }
            AssignStmt assign = newAssignStmt;
            this.body.getUnits().add((UnitPatchingChain) assign);
            Util.addLnPosTags(assign, initExpr.position());
            Util.addLnPosTags(assign.getRightOpBox(), initExpr.position());
        }
    }

    private void handleOuterClassThisInit(SootMethod sootMethod) {
        SootField this0Field = this.body.getMethod().getDeclaringClass().getFieldByNameUnsafe("this$0");
        if (this0Field != null) {
            FieldRef fieldRef = Jimple.v().newInstanceFieldRef(this.specialThisLocal, this0Field.makeRef());
            AssignStmt stmt = Jimple.v().newAssignStmt(fieldRef, this.outerClassParamLocal);
            this.body.getUnits().add((UnitPatchingChain) stmt);
        }
    }

    private void handleStaticFieldInits(SootMethod sootMethod) {
        Value sootExpr;
        ArrayList<FieldDecl> staticFieldInits = ((PolyglotMethodSource) sootMethod.getSource()).getStaticFieldInits();
        if (staticFieldInits != null) {
            Iterator<FieldDecl> staticFieldInitsIt = staticFieldInits.iterator();
            while (staticFieldInitsIt.hasNext()) {
                FieldDecl field = staticFieldInitsIt.next();
                String fieldName = field.name();
                Expr initExpr = field.init();
                SootClass currentClass = this.body.getMethod().getDeclaringClass();
                SootFieldRef sootField = Scene.v().makeFieldRef(currentClass, fieldName, Util.getSootType(field.type().type()), field.flags().isStatic());
                FieldRef fieldRef = Jimple.v().newStaticFieldRef(sootField);
                if (initExpr instanceof ArrayInit) {
                    sootExpr = getArrayInitLocal((ArrayInit) initExpr, field.type().type());
                } else {
                    sootExpr = base().createAggressiveExpr(initExpr, false, false);
                    if (sootExpr instanceof ConditionExpr) {
                        sootExpr = handleCondBinExpr((ConditionExpr) sootExpr);
                    }
                }
                AssignStmt newAssignStmt = Jimple.v().newAssignStmt(fieldRef, sootExpr);
                this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
                Util.addLnPosTags(newAssignStmt, initExpr.position());
            }
        }
    }

    private void handleInitializerBlocks(SootMethod sootMethod) {
        ArrayList<Block> initializerBlocks = ((PolyglotMethodSource) sootMethod.getSource()).getInitializerBlocks();
        if (initializerBlocks != null) {
            handleStaticBlocks(initializerBlocks);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handleStaticBlocks(ArrayList<Block> initializerBlocks) {
        Iterator<Block> initBlocksIt = initializerBlocks.iterator();
        while (initBlocksIt.hasNext()) {
            createBlock(initBlocksIt.next());
        }
    }

    private void handleStaticInitializerBlocks(SootMethod sootMethod) {
        ArrayList<Block> staticInitializerBlocks = ((PolyglotMethodSource) sootMethod.getSource()).getStaticInitializerBlocks();
        if (staticInitializerBlocks != null) {
            Iterator<Block> staticInitBlocksIt = staticInitializerBlocks.iterator();
            while (staticInitBlocksIt.hasNext()) {
                createBlock(staticInitBlocksIt.next());
            }
        }
    }

    private void createBody(SootMethod sootMethod) {
        this.body = Jimple.v().newBody(sootMethod);
        sootMethod.setActiveBody(this.body);
    }

    private void createBlock(Block block) {
        if (block == null) {
            return;
        }
        for (Object next : block.statements()) {
            if (next instanceof polyglot.ast.Stmt) {
                createStmt((polyglot.ast.Stmt) next);
            } else {
                throw new RuntimeException("Unexpected - Unhandled Node");
            }
        }
    }

    private Local createCatchFormal(Formal formal) {
        Util.getSootType(formal.type().type());
        Local formalLocal = createLocal(formal.localInstance());
        CaughtExceptionRef exceptRef = Jimple.v().newCaughtExceptionRef();
        IdentityStmt newIdentityStmt = Jimple.v().newIdentityStmt(formalLocal, exceptRef);
        this.body.getUnits().add((UnitPatchingChain) newIdentityStmt);
        Util.addLnPosTags(newIdentityStmt, formal.position());
        Util.addLnPosTags(newIdentityStmt.getRightOpBox(), formal.position());
        String[] names = {formal.name()};
        newIdentityStmt.addTag(new ParamNamesTag(names));
        return formalLocal;
    }

    private void createFormal(Formal formal, int counter) {
        Type sootType = Util.getSootType(formal.type().type());
        Local formalLocal = createLocal(formal.localInstance());
        ParameterRef paramRef = Jimple.v().newParameterRef(sootType, counter);
        this.paramRefCount++;
        IdentityStmt newIdentityStmt = Jimple.v().newIdentityStmt(formalLocal, paramRef);
        this.body.getUnits().add((UnitPatchingChain) newIdentityStmt);
        Util.addLnPosTags(newIdentityStmt.getRightOpBox(), formal.position());
        Util.addLnPosTags(newIdentityStmt, formal.position());
    }

    private Value createLiteral(Lit lit) {
        if (lit instanceof IntLit) {
            IntLit intLit = (IntLit) lit;
            long litValue = intLit.value();
            if (intLit.kind() == IntLit.INT) {
                return IntConstant.v((int) litValue);
            }
            return LongConstant.v(litValue);
        } else if (lit instanceof StringLit) {
            String litValue2 = ((StringLit) lit).value();
            return StringConstant.v(litValue2);
        } else if (lit instanceof NullLit) {
            return NullConstant.v();
        } else {
            if (lit instanceof FloatLit) {
                FloatLit floatLit = (FloatLit) lit;
                floatLit.value();
                if (floatLit.kind() == FloatLit.DOUBLE) {
                    return DoubleConstant.v(floatLit.value());
                }
                return FloatConstant.v((float) floatLit.value());
            } else if (lit instanceof CharLit) {
                char litValue3 = ((CharLit) lit).value();
                return IntConstant.v(litValue3);
            } else if (lit instanceof BooleanLit) {
                boolean litValue4 = ((BooleanLit) lit).value();
                if (litValue4) {
                    return IntConstant.v(1);
                }
                return IntConstant.v(0);
            } else if (lit instanceof ClassLit) {
                return getSpecialClassLitLocal((ClassLit) lit);
            } else {
                throw new RuntimeException("Unknown Literal - Unhandled: " + lit.getClass());
            }
        }
    }

    private Local createLocal(LocalInstance localInst) {
        Type sootType = Util.getSootType(localInst.type());
        String name = localInst.name();
        Local sootLocal = createLocal(name, sootType);
        this.localsMap.put(new IdentityKey(localInst), sootLocal);
        return sootLocal;
    }

    private Local createLocal(String name, Type sootType) {
        Local sootLocal = Jimple.v().newLocal(name, sootType);
        this.body.getLocals().add(sootLocal);
        return sootLocal;
    }

    private Local getLocal(polyglot.ast.Local local) {
        return getLocal(local.localInstance());
    }

    private Local getLocal(LocalInstance li) {
        if (this.localsMap.containsKey(new IdentityKey(li))) {
            Local sootLocal = this.localsMap.get(new IdentityKey(li));
            return sootLocal;
        } else if (this.body.getMethod().getDeclaringClass().declaresField("val$" + li.name(), Util.getSootType(li.type()))) {
            Local fieldLocal = generateLocal(li.type());
            SootFieldRef field = Scene.v().makeFieldRef(this.body.getMethod().getDeclaringClass(), "val$" + li.name(), Util.getSootType(li.type()), false);
            FieldRef fieldRef = Jimple.v().newInstanceFieldRef(this.specialThisLocal, field);
            AssignStmt assign = Jimple.v().newAssignStmt(fieldLocal, fieldRef);
            this.body.getUnits().add((UnitPatchingChain) assign);
            return fieldLocal;
        } else {
            SootClass currentClass = this.body.getMethod().getDeclaringClass();
            boolean fieldFound = false;
            while (!fieldFound) {
                if (!currentClass.declaresFieldByName("this$0")) {
                    throw new RuntimeException("Trying to get field val$" + li.name() + " from some outer class but can't access the outer class of: " + currentClass.getName() + "! current class contains fields: " + currentClass.getFields());
                }
                SootClass outerClass = ((RefType) currentClass.getFieldByName("this$0").getType()).getSootClass();
                if (outerClass.declaresField("val$" + li.name(), Util.getSootType(li.type()))) {
                    fieldFound = true;
                }
                currentClass = outerClass;
            }
            SootMethod methToInvoke = makeLiFieldAccessMethod(currentClass, li);
            ArrayList methParams = new ArrayList();
            methParams.add(getThis(currentClass.getType()));
            Local res = Util.getPrivateAccessFieldInvoke(methToInvoke.makeRef(), methParams, this.body, this.lg);
            return res;
        }
    }

    private SootMethod makeLiFieldAccessMethod(SootClass classToInvoke, LocalInstance li) {
        String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + TarConstants.VERSION_POSIX;
        ArrayList paramTypes = new ArrayList();
        paramTypes.add(classToInvoke.getType());
        SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(li.type()), 8);
        classToInvoke.addMethod(meth);
        PrivateFieldAccMethodSource src = new PrivateFieldAccMethodSource(Util.getSootType(li.type()), "val$" + li.name(), false, classToInvoke);
        meth.setActiveBody(src.getBody(meth, null));
        meth.addTag(new SyntheticTag());
        return meth;
    }

    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    protected void createStmt(polyglot.ast.Stmt stmt) {
        if (stmt instanceof Eval) {
            base().createAggressiveExpr(((Eval) stmt).expr(), false, false);
        } else if (stmt instanceof If) {
            createIf2((If) stmt);
        } else if (stmt instanceof LocalDecl) {
            createLocalDecl((LocalDecl) stmt);
        } else if (stmt instanceof Block) {
            createBlock((Block) stmt);
        } else if (stmt instanceof While) {
            createWhile2((While) stmt);
        } else if (stmt instanceof Do) {
            createDo2((Do) stmt);
        } else if (stmt instanceof For) {
            createForLoop2((For) stmt);
        } else if (stmt instanceof Switch) {
            createSwitch((Switch) stmt);
        } else if (stmt instanceof Return) {
            createReturn((Return) stmt);
        } else if (stmt instanceof Branch) {
            createBranch((Branch) stmt);
        } else if (stmt instanceof ConstructorCall) {
            createConstructorCall((ConstructorCall) stmt);
        } else if (!(stmt instanceof Empty)) {
            if (stmt instanceof Throw) {
                createThrow((Throw) stmt);
            } else if (stmt instanceof Try) {
                createTry((Try) stmt);
            } else if (stmt instanceof Labeled) {
                createLabeled((Labeled) stmt);
            } else if (stmt instanceof Synchronized) {
                createSynchronized((Synchronized) stmt);
            } else if (stmt instanceof Assert) {
                createAssert((Assert) stmt);
            } else if (stmt instanceof LocalClassDecl) {
                createLocalClassDecl((LocalClassDecl) stmt);
            } else {
                throw new RuntimeException("Unhandled Stmt: " + stmt.getClass());
            }
        }
    }

    private boolean needSootIf(Value sootCond) {
        if ((sootCond instanceof IntConstant) && ((IntConstant) sootCond).value == 1) {
            return false;
        }
        return true;
    }

    private void createIf2(If ifExpr) {
        NopStmt endTgt = Jimple.v().newNopStmt();
        NopStmt brchTgt = Jimple.v().newNopStmt();
        Expr condition = ifExpr.cond();
        createBranchingExpr(condition, brchTgt, false);
        polyglot.ast.Stmt consequence = ifExpr.consequent();
        createStmt(consequence);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(endTgt));
        this.body.getUnits().add((UnitPatchingChain) brchTgt);
        polyglot.ast.Stmt alternative = ifExpr.alternative();
        if (alternative != null) {
            createStmt(alternative);
        }
        this.body.getUnits().add((UnitPatchingChain) endTgt);
    }

    private void createBranchingExpr(Expr expr, Stmt tgt, boolean boto) {
        Value sootCond;
        if ((expr instanceof Binary) && ((Binary) expr).operator() == Binary.COND_AND) {
            Binary cond_and = (Binary) expr;
            if (boto) {
                NopStmt newNopStmt = Jimple.v().newNopStmt();
                createBranchingExpr(cond_and.left(), newNopStmt, false);
                createBranchingExpr(cond_and.right(), tgt, true);
                this.body.getUnits().add((UnitPatchingChain) newNopStmt);
                return;
            }
            createBranchingExpr(cond_and.left(), tgt, false);
            createBranchingExpr(cond_and.right(), tgt, false);
        } else if ((expr instanceof Binary) && ((Binary) expr).operator() == Binary.COND_OR) {
            Binary cond_or = (Binary) expr;
            if (boto) {
                createBranchingExpr(cond_or.left(), tgt, true);
                createBranchingExpr(cond_or.right(), tgt, true);
                return;
            }
            NopStmt newNopStmt2 = Jimple.v().newNopStmt();
            createBranchingExpr(cond_or.left(), newNopStmt2, true);
            createBranchingExpr(cond_or.right(), tgt, false);
            this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        } else if ((expr instanceof Unary) && ((Unary) expr).operator() == Unary.NOT) {
            Unary not = (Unary) expr;
            createBranchingExpr(not.expr(), tgt, !boto);
        } else {
            Value sootCond2 = base().createAggressiveExpr(expr, false, false);
            boolean needIf = needSootIf(sootCond2);
            if (needIf) {
                if (!(sootCond2 instanceof ConditionExpr)) {
                    if (!boto) {
                        sootCond = Jimple.v().newEqExpr(sootCond2, IntConstant.v(0));
                    } else {
                        sootCond = Jimple.v().newNeExpr(sootCond2, IntConstant.v(0));
                    }
                } else {
                    sootCond = handleDFLCond((ConditionExpr) sootCond2);
                    if (!boto) {
                        sootCond = reverseCondition((ConditionExpr) sootCond);
                    }
                }
                IfStmt ifStmt = Jimple.v().newIfStmt(sootCond, tgt);
                this.body.getUnits().add((UnitPatchingChain) ifStmt);
                Util.addLnPosTags(ifStmt.getConditionBox(), expr.position());
                Util.addLnPosTags(ifStmt, expr.position());
            } else if (sootCond2 instanceof IntConstant) {
                if ((((IntConstant) sootCond2).value == 1) == boto) {
                    GotoStmt gotoStmt = Jimple.v().newGotoStmt(tgt);
                    this.body.getUnits().add((UnitPatchingChain) gotoStmt);
                    Util.addLnPosTags(gotoStmt, expr.position());
                }
            }
        }
    }

    private void createWhile2(While whileStmt) {
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        this.endControlNoop.push(Jimple.v().newNopStmt());
        this.condControlNoop.push(Jimple.v().newNopStmt());
        Stmt continueStmt = this.condControlNoop.pop();
        this.body.getUnits().add((UnitPatchingChain) continueStmt);
        this.condControlNoop.push(continueStmt);
        Expr condition = whileStmt.cond();
        createBranchingExpr(condition, newNopStmt, false);
        createStmt(whileStmt.body());
        GotoStmt gotoLoop = Jimple.v().newGotoStmt(newNopStmt2);
        this.body.getUnits().add((UnitPatchingChain) gotoLoop);
        this.body.getUnits().add((UnitPatchingChain) this.endControlNoop.pop());
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        this.condControlNoop.pop();
    }

    private void createDo2(Do doStmt) {
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        this.endControlNoop.push(Jimple.v().newNopStmt());
        this.condControlNoop.push(Jimple.v().newNopStmt());
        createStmt(doStmt.body());
        Stmt continueStmt = this.condControlNoop.pop();
        this.body.getUnits().add((UnitPatchingChain) continueStmt);
        this.condControlNoop.push(continueStmt);
        if (this.labelMap != null && this.labelMap.containsKey(doStmt)) {
            this.body.getUnits().add((UnitPatchingChain) this.labelMap.get(doStmt));
        }
        Expr condition = doStmt.cond();
        createBranchingExpr(condition, newNopStmt, true);
        this.body.getUnits().add((UnitPatchingChain) this.endControlNoop.pop());
        this.condControlNoop.pop();
    }

    private void createForLoop2(For forStmt) {
        this.endControlNoop.push(Jimple.v().newNopStmt());
        this.condControlNoop.push(Jimple.v().newNopStmt());
        for (polyglot.ast.Stmt stmt : forStmt.inits()) {
            createStmt(stmt);
        }
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        Expr condition = forStmt.cond();
        if (condition != null) {
            createBranchingExpr(condition, newNopStmt, false);
        }
        createStmt(forStmt.body());
        this.body.getUnits().add((UnitPatchingChain) this.condControlNoop.pop());
        if (this.labelMap != null && this.labelMap.containsKey(forStmt)) {
            this.body.getUnits().add((UnitPatchingChain) this.labelMap.get(forStmt));
        }
        for (polyglot.ast.Stmt stmt2 : forStmt.iters()) {
            createStmt(stmt2);
        }
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt2));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        this.body.getUnits().add((UnitPatchingChain) this.endControlNoop.pop());
    }

    private void createLocalDecl(LocalDecl localDecl) {
        Value rhs;
        String name = localDecl.name();
        LocalInstance localInst = localDecl.localInstance();
        Value lhs = createLocal(localInst);
        Expr expr = localDecl.init();
        if (expr != null) {
            if (expr instanceof ArrayInit) {
                rhs = getArrayInitLocal((ArrayInit) expr, localInst.type());
            } else {
                rhs = base().createAggressiveExpr(expr, false, false);
            }
            if (rhs instanceof ConditionExpr) {
                rhs = handleCondBinExpr((ConditionExpr) rhs);
            }
            AssignStmt stmt = Jimple.v().newAssignStmt(lhs, rhs);
            this.body.getUnits().add((UnitPatchingChain) stmt);
            Util.addLnPosTags(stmt, localDecl.position());
            if (localDecl.position() != null) {
                Util.addLnPosTags(stmt.getLeftOpBox(), localDecl.position().line(), localDecl.position().endLine(), localDecl.position().endColumn() - name.length(), localDecl.position().endColumn());
                if (expr != null) {
                    Util.addLnPosTags(stmt, localDecl.position().line(), expr.position().endLine(), localDecl.position().column(), expr.position().endColumn());
                } else {
                    Util.addLnPosTags(stmt, localDecl.position().line(), localDecl.position().endLine(), localDecl.position().column(), localDecl.position().endColumn());
                }
            }
            if (expr != null) {
                Util.addLnPosTags(stmt.getRightOpBox(), expr.position());
            }
        }
    }

    private void createSwitch(Switch switchStmt) {
        LookupSwitchStmt lookupSwitchStmt;
        Expr value = switchStmt.expr();
        Value sootValue = base().createAggressiveExpr(value, false, false);
        if (switchStmt.elements().size() == 0) {
            return;
        }
        Stmt defaultTarget = null;
        Case[] caseArray = new Case[switchStmt.elements().size()];
        Stmt[] targetsArray = new Stmt[switchStmt.elements().size()];
        ArrayList<Stmt> targets = new ArrayList<>();
        HashMap<Object, Stmt> targetsMap = new HashMap<>();
        int counter = 0;
        for (Object next : switchStmt.elements()) {
            if (next instanceof Case) {
                Stmt noop = Jimple.v().newNopStmt();
                if (!((Case) next).isDefault()) {
                    targets.add(noop);
                    caseArray[counter] = (Case) next;
                    targetsArray[counter] = noop;
                    counter++;
                    targetsMap.put(next, noop);
                } else {
                    defaultTarget = noop;
                }
            }
        }
        for (int i = 0; i < counter; i++) {
            for (int j = i + 1; j < counter; j++) {
                if (caseArray[j].value() < caseArray[i].value()) {
                    Case tempCase = caseArray[i];
                    Stmt tempTarget = targetsArray[i];
                    caseArray[i] = caseArray[j];
                    targetsArray[i] = targetsArray[j];
                    caseArray[j] = tempCase;
                    targetsArray[j] = tempTarget;
                }
            }
        }
        ArrayList sortedTargets = new ArrayList();
        for (int i2 = 0; i2 < counter; i2++) {
            sortedTargets.add(targetsArray[i2]);
        }
        boolean hasDefaultTarget = true;
        if (defaultTarget == null) {
            defaultTarget = Jimple.v().newNopStmt();
            hasDefaultTarget = false;
        }
        if (isLookupSwitch(switchStmt)) {
            ArrayList values = new ArrayList();
            for (int i3 = 0; i3 < counter; i3++) {
                if (!caseArray[i3].isDefault()) {
                    values.add(IntConstant.v((int) caseArray[i3].value()));
                }
            }
            LookupSwitchStmt lookupStmt = Jimple.v().newLookupSwitchStmt(sootValue, values, sortedTargets, defaultTarget);
            Util.addLnPosTags(lookupStmt.getKeyBox(), value.position());
            lookupSwitchStmt = lookupStmt;
        } else {
            long lowVal = 0;
            long highVal = 0;
            boolean unknown = true;
            for (Object next2 : switchStmt.elements()) {
                if ((next2 instanceof Case) && !((Case) next2).isDefault()) {
                    long temp = ((Case) next2).value();
                    if (unknown) {
                        highVal = temp;
                        lowVal = temp;
                        unknown = false;
                    }
                    if (temp > highVal) {
                        highVal = temp;
                    }
                    if (temp < lowVal) {
                        lowVal = temp;
                    }
                }
            }
            TableSwitchStmt tableStmt = Jimple.v().newTableSwitchStmt(sootValue, (int) lowVal, (int) highVal, sortedTargets, defaultTarget);
            Util.addLnPosTags(tableStmt.getKeyBox(), value.position());
            lookupSwitchStmt = tableStmt;
        }
        this.body.getUnits().add((UnitPatchingChain) lookupSwitchStmt);
        Util.addLnPosTags(lookupSwitchStmt, switchStmt.position());
        this.endControlNoop.push(Jimple.v().newNopStmt());
        targets.iterator();
        for (Object next3 : switchStmt.elements()) {
            if (next3 instanceof Case) {
                if (!((Case) next3).isDefault()) {
                    this.body.getUnits().add((UnitPatchingChain) targetsMap.get(next3));
                } else {
                    this.body.getUnits().add((UnitPatchingChain) defaultTarget);
                }
            } else {
                SwitchBlock blockStmt = (SwitchBlock) next3;
                createBlock(blockStmt);
            }
        }
        if (!hasDefaultTarget) {
            this.body.getUnits().add((UnitPatchingChain) defaultTarget);
        }
        this.body.getUnits().add((UnitPatchingChain) this.endControlNoop.pop());
    }

    private boolean isLookupSwitch(Switch switchStmt) {
        int lowest = 0;
        int highest = 0;
        int counter = 0;
        for (Object next : switchStmt.elements()) {
            if (next instanceof Case) {
                Case caseStmt = (Case) next;
                if (!caseStmt.isDefault()) {
                    int caseValue = (int) caseStmt.value();
                    if (caseValue <= lowest || counter == 0) {
                        lowest = caseValue;
                    }
                    if (caseValue >= highest || counter == 0) {
                        highest = caseValue;
                    }
                    counter++;
                }
            }
        }
        if (counter - 1 == highest - lowest) {
            return false;
        }
        return true;
    }

    private void createBranch(Branch branchStmt) {
        if (this.tryStack != null && !this.tryStack.isEmpty()) {
            Try currentTry = this.tryStack.pop();
            if (currentTry.finallyBlock() != null) {
                createBlock(currentTry.finallyBlock());
                this.tryStack.push(currentTry);
            } else {
                this.tryStack.push(currentTry);
            }
        }
        if (this.catchStack != null && !this.catchStack.isEmpty()) {
            Try currentTry2 = this.catchStack.pop();
            if (currentTry2.finallyBlock() != null) {
                createBlock(currentTry2.finallyBlock());
                this.catchStack.push(currentTry2);
            } else {
                this.catchStack.push(currentTry2);
            }
        }
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newNopStmt());
        if (branchStmt.kind() == Branch.BREAK) {
            if (branchStmt.label() == null) {
                Stmt gotoEndNoop = this.endControlNoop.pop();
                if (this.monitorStack != null) {
                    Stack<Local> putBack = new Stack<>();
                    while (!this.monitorStack.isEmpty()) {
                        Local exitVal = (Local) this.monitorStack.pop();
                        putBack.push(exitVal);
                        ExitMonitorStmt emStmt = Jimple.v().newExitMonitorStmt(exitVal);
                        this.body.getUnits().add((UnitPatchingChain) emStmt);
                    }
                    while (!putBack.isEmpty()) {
                        this.monitorStack.push(putBack.pop());
                    }
                }
                GotoStmt newGotoStmt = Jimple.v().newGotoStmt(gotoEndNoop);
                this.endControlNoop.push(gotoEndNoop);
                this.body.getUnits().add((UnitPatchingChain) newGotoStmt);
                Util.addLnPosTags(newGotoStmt, branchStmt.position());
                return;
            }
            GotoStmt newGotoStmt2 = Jimple.v().newGotoStmt(this.labelBreakMap.get(branchStmt.label()));
            this.body.getUnits().add((UnitPatchingChain) newGotoStmt2);
            Util.addLnPosTags(newGotoStmt2, branchStmt.position());
        } else if (branchStmt.kind() == Branch.CONTINUE) {
            if (branchStmt.label() == null) {
                Stmt gotoCondNoop = this.condControlNoop.pop();
                if (this.monitorStack != null) {
                    Stack<Local> putBack2 = new Stack<>();
                    while (!this.monitorStack.isEmpty()) {
                        Local exitVal2 = (Local) this.monitorStack.pop();
                        putBack2.push(exitVal2);
                        ExitMonitorStmt emStmt2 = Jimple.v().newExitMonitorStmt(exitVal2);
                        this.body.getUnits().add((UnitPatchingChain) emStmt2);
                    }
                    while (!putBack2.isEmpty()) {
                        this.monitorStack.push(putBack2.pop());
                    }
                }
                GotoStmt newGotoStmt3 = Jimple.v().newGotoStmt(gotoCondNoop);
                this.condControlNoop.push(gotoCondNoop);
                this.body.getUnits().add((UnitPatchingChain) newGotoStmt3);
                Util.addLnPosTags(newGotoStmt3, branchStmt.position());
                return;
            }
            GotoStmt newGotoStmt4 = Jimple.v().newGotoStmt(this.labelContinueMap.get(branchStmt.label()));
            this.body.getUnits().add((UnitPatchingChain) newGotoStmt4);
            Util.addLnPosTags(newGotoStmt4, branchStmt.position());
        }
    }

    private void createLabeled(Labeled labeledStmt) {
        String label = labeledStmt.label();
        polyglot.ast.Stmt stmt = labeledStmt.statement();
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        if (!(stmt instanceof For) && !(stmt instanceof Do)) {
            this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        }
        if (this.labelMap == null) {
            this.labelMap = new HashMap<>();
        }
        this.labelMap.put(stmt, newNopStmt);
        if (this.labelBreakMap == null) {
            this.labelBreakMap = new HashMap<>();
        }
        if (this.labelContinueMap == null) {
            this.labelContinueMap = new HashMap<>();
        }
        this.labelContinueMap.put(label, newNopStmt);
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.labelBreakMap.put(label, newNopStmt2);
        createStmt(stmt);
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
    }

    private void createAssert(Assert assertStmt) {
        Value sootCond;
        Local testLocal = this.lg.generateLocal(BooleanType.v());
        SootFieldRef assertField = Scene.v().makeFieldRef(this.body.getMethod().getDeclaringClass(), "$assertionsDisabled", BooleanType.v(), true);
        FieldRef assertFieldRef = Jimple.v().newStaticFieldRef(assertField);
        AssignStmt fieldAssign = Jimple.v().newAssignStmt(testLocal, assertFieldRef);
        this.body.getUnits().add((UnitPatchingChain) fieldAssign);
        NopStmt nop1 = Jimple.v().newNopStmt();
        ConditionExpr cond1 = Jimple.v().newNeExpr(testLocal, IntConstant.v(0));
        IfStmt testIf = Jimple.v().newIfStmt(cond1, nop1);
        this.body.getUnits().add((UnitPatchingChain) testIf);
        if (!(assertStmt.cond() instanceof BooleanLit) || ((BooleanLit) assertStmt.cond()).value()) {
            Value sootCond2 = base().createAggressiveExpr(assertStmt.cond(), false, false);
            boolean needIf = needSootIf(sootCond2);
            if (!(sootCond2 instanceof ConditionExpr)) {
                sootCond = Jimple.v().newEqExpr(sootCond2, IntConstant.v(1));
            } else {
                sootCond = handleDFLCond((ConditionExpr) sootCond2);
            }
            if (needIf) {
                IfStmt ifStmt = Jimple.v().newIfStmt(sootCond, nop1);
                this.body.getUnits().add((UnitPatchingChain) ifStmt);
                Util.addLnPosTags(ifStmt.getConditionBox(), assertStmt.cond().position());
                Util.addLnPosTags(ifStmt, assertStmt.position());
            }
        }
        Local failureLocal = this.lg.generateLocal(RefType.v("java.lang.AssertionError"));
        NewExpr newExpr = Jimple.v().newNewExpr(RefType.v("java.lang.AssertionError"));
        AssignStmt newAssign = Jimple.v().newAssignStmt(failureLocal, newExpr);
        this.body.getUnits().add((UnitPatchingChain) newAssign);
        ArrayList paramTypes = new ArrayList();
        ArrayList params = new ArrayList();
        if (assertStmt.errorMessage() != null) {
            Value errorExpr = base().createAggressiveExpr(assertStmt.errorMessage(), false, false);
            if (errorExpr instanceof ConditionExpr) {
                errorExpr = handleCondBinExpr((ConditionExpr) errorExpr);
            }
            Type errorType = errorExpr.getType();
            if (assertStmt.errorMessage().type().isChar()) {
                errorType = CharType.v();
            }
            if (errorType instanceof IntType) {
                paramTypes.add(IntType.v());
            } else if (errorType instanceof LongType) {
                paramTypes.add(LongType.v());
            } else if (errorType instanceof FloatType) {
                paramTypes.add(FloatType.v());
            } else if (errorType instanceof DoubleType) {
                paramTypes.add(DoubleType.v());
            } else if (errorType instanceof CharType) {
                paramTypes.add(CharType.v());
            } else if (errorType instanceof BooleanType) {
                paramTypes.add(BooleanType.v());
            } else if (errorType instanceof ShortType) {
                paramTypes.add(IntType.v());
            } else if (errorType instanceof ByteType) {
                paramTypes.add(IntType.v());
            } else {
                paramTypes.add(Scene.v().getSootClass(Scene.v().getObjectType().toString()).getType());
            }
            params.add(errorExpr);
        }
        SootMethodRef methToInvoke = Scene.v().makeMethodRef(Scene.v().getSootClass("java.lang.AssertionError"), "<init>", paramTypes, VoidType.v(), false);
        SpecialInvokeExpr invokeExpr = Jimple.v().newSpecialInvokeExpr(failureLocal, methToInvoke, params);
        InvokeStmt invokeStmt = Jimple.v().newInvokeStmt(invokeExpr);
        this.body.getUnits().add((UnitPatchingChain) invokeStmt);
        if (assertStmt.errorMessage() != null) {
            Util.addLnPosTags(invokeExpr.getArgBox(0), assertStmt.errorMessage().position());
        }
        ThrowStmt throwStmt = Jimple.v().newThrowStmt(failureLocal);
        this.body.getUnits().add((UnitPatchingChain) throwStmt);
        this.body.getUnits().add((UnitPatchingChain) nop1);
    }

    private void createSynchronized(Synchronized synchStmt) {
        Value sootExpr = base().createAggressiveExpr(synchStmt.expr(), false, false);
        EnterMonitorStmt enterMon = Jimple.v().newEnterMonitorStmt(sootExpr);
        this.body.getUnits().add((UnitPatchingChain) enterMon);
        if (this.beforeReturn == null) {
            this.beforeReturn = new ArrayList();
        }
        if (this.afterReturn == null) {
            this.afterReturn = new ArrayList();
        }
        this.beforeReturn.add(new ArrayList());
        this.afterReturn.add(new ArrayList());
        if (this.monitorStack == null) {
            this.monitorStack = new Stack<>();
        }
        this.monitorStack.push(sootExpr);
        Util.addLnPosTags(enterMon.getOpBox(), synchStmt.expr().position());
        Util.addLnPosTags(enterMon, synchStmt.expr().position());
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        createBlock(synchStmt.body());
        ExitMonitorStmt exitMon = Jimple.v().newExitMonitorStmt(sootExpr);
        this.body.getUnits().add((UnitPatchingChain) exitMon);
        this.monitorStack.pop();
        Util.addLnPosTags(exitMon.getOpBox(), synchStmt.expr().position());
        Util.addLnPosTags(exitMon, synchStmt.expr().position());
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        GotoStmt newGotoStmt = Jimple.v().newGotoStmt(newNopStmt2);
        NopStmt newNopStmt3 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt3);
        this.body.getUnits().add((UnitPatchingChain) newGotoStmt);
        NopStmt newNopStmt4 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt4);
        Local formalLocal = this.lg.generateLocal(RefType.v("java.lang.Throwable"));
        CaughtExceptionRef exceptRef = Jimple.v().newCaughtExceptionRef();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(formalLocal, exceptRef));
        NopStmt newNopStmt5 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt5);
        Local local = this.lg.generateLocal(RefType.v("java.lang.Throwable"));
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(local, formalLocal));
        ExitMonitorStmt catchExitMon = Jimple.v().newExitMonitorStmt(sootExpr);
        this.body.getUnits().add((UnitPatchingChain) catchExitMon);
        Util.addLnPosTags(catchExitMon.getOpBox(), synchStmt.expr().position());
        NopStmt newNopStmt6 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt6);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newThrowStmt(local));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        List<Stmt> before = this.beforeReturn.get(this.beforeReturn.size() - 1);
        List<Stmt> after = this.afterReturn.get(this.afterReturn.size() - 1);
        if (before.size() > 0) {
            addToExceptionList(newNopStmt, before.get(0), newNopStmt4, Scene.v().getSootClass("java.lang.Throwable"));
            for (int i = 1; i < before.size(); i++) {
                addToExceptionList(after.get(i - 1), before.get(i), newNopStmt4, Scene.v().getSootClass("java.lang.Throwable"));
            }
            addToExceptionList(after.get(after.size() - 1), newNopStmt3, newNopStmt4, Scene.v().getSootClass("java.lang.Throwable"));
        } else {
            addToExceptionList(newNopStmt, newNopStmt3, newNopStmt4, Scene.v().getSootClass("java.lang.Throwable"));
        }
        this.beforeReturn.remove(before);
        this.afterReturn.remove(after);
        addToExceptionList(newNopStmt5, newNopStmt6, newNopStmt4, Scene.v().getSootClass("java.lang.Throwable"));
    }

    private void createReturn(Return retStmt) {
        Expr expr = retStmt.expr();
        Value sootLocal = null;
        if (expr != null) {
            sootLocal = base().createAggressiveExpr(expr, false, false);
        }
        if (this.monitorStack != null) {
            Stack<Local> putBack = new Stack<>();
            while (!this.monitorStack.isEmpty()) {
                Local exitVal = (Local) this.monitorStack.pop();
                putBack.push(exitVal);
                ExitMonitorStmt emStmt = Jimple.v().newExitMonitorStmt(exitVal);
                this.body.getUnits().add((UnitPatchingChain) emStmt);
            }
            while (!putBack.isEmpty()) {
                this.monitorStack.push(putBack.pop());
            }
            NopStmt newNopStmt = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) newNopStmt);
            if (this.beforeReturn != null) {
                for (List<Stmt> v : this.beforeReturn) {
                    v.add(newNopStmt);
                }
            }
        }
        if (this.tryStack != null && !this.tryStack.isEmpty()) {
            Try currentTry = this.tryStack.pop();
            if (currentTry.finallyBlock() != null) {
                createBlock(currentTry.finallyBlock());
                this.tryStack.push(currentTry);
            } else {
                this.tryStack.push(currentTry);
            }
        }
        if (this.catchStack != null && !this.catchStack.isEmpty()) {
            Try currentTry2 = this.catchStack.pop();
            if (currentTry2.finallyBlock() != null) {
                createBlock(currentTry2.finallyBlock());
                this.catchStack.push(currentTry2);
            } else {
                this.catchStack.push(currentTry2);
            }
        }
        if (expr == null) {
            ReturnVoidStmt newReturnVoidStmt = Jimple.v().newReturnVoidStmt();
            this.body.getUnits().add((UnitPatchingChain) newReturnVoidStmt);
            Util.addLnPosTags(newReturnVoidStmt, retStmt.position());
        } else {
            if (sootLocal instanceof ConditionExpr) {
                sootLocal = handleCondBinExpr((ConditionExpr) sootLocal);
            }
            ReturnStmt retStmtLocal = Jimple.v().newReturnStmt(sootLocal);
            this.body.getUnits().add((UnitPatchingChain) retStmtLocal);
            Util.addLnPosTags(retStmtLocal.getOpBox(), expr.position());
            Util.addLnPosTags(retStmtLocal, retStmt.position());
        }
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        if (this.afterReturn != null) {
            for (List<Stmt> v2 : this.afterReturn) {
                v2.add(newNopStmt2);
            }
        }
    }

    private void createThrow(Throw throwStmt) {
        Value toThrow = base().createAggressiveExpr(throwStmt.expr(), false, false);
        ThrowStmt throwSt = Jimple.v().newThrowStmt(toThrow);
        this.body.getUnits().add((UnitPatchingChain) throwSt);
        Util.addLnPosTags(throwSt, throwStmt.position());
        Util.addLnPosTags(throwSt.getOpBox(), throwStmt.expr().position());
    }

    private void createTry(Try tryStmt) {
        Block finallyBlock = tryStmt.finallyBlock();
        if (finallyBlock == null) {
            createTryCatch(tryStmt);
        } else {
            createTryCatchFinally(tryStmt);
        }
    }

    private void createTryCatch(Try tryStmt) {
        Block tryBlock = tryStmt.tryBlock();
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        if (this.tryStack == null) {
            this.tryStack = new Stack<>();
        }
        this.tryStack.push(tryStmt);
        createBlock(tryBlock);
        this.tryStack.pop();
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        NopStmt newNopStmt3 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt3));
        for (Catch catchBlock : tryStmt.catchBlocks()) {
            NopStmt newNopStmt4 = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) newNopStmt4);
            createCatchFormal(catchBlock.formal());
            if (this.catchStack == null) {
                this.catchStack = new Stack<>();
            }
            this.catchStack.push(tryStmt);
            createBlock(catchBlock.body());
            this.catchStack.pop();
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt3));
            Type sootType = Util.getSootType(catchBlock.catchType());
            addToExceptionList(newNopStmt, newNopStmt2, newNopStmt4, Scene.v().getSootClass(sootType.toString()));
        }
        this.body.getUnits().add((UnitPatchingChain) newNopStmt3);
    }

    private void createTryCatchFinally(Try tryStmt) {
        HashMap<Stmt, Stmt> gotoMap = new HashMap<>();
        Block tryBlock = tryStmt.tryBlock();
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        if (this.tryStack == null) {
            this.tryStack = new Stack<>();
        }
        this.tryStack.push(tryStmt);
        createBlock(tryBlock);
        this.tryStack.pop();
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        NopStmt newNopStmt3 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newNopStmt());
        Stmt tryFinallyNoop = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(tryFinallyNoop));
        NopStmt newNopStmt4 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt4);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt3));
        gotoMap.put(tryFinallyNoop, newNopStmt4);
        NopStmt newNopStmt5 = Jimple.v().newNopStmt();
        for (Catch catchBlock : tryStmt.catchBlocks()) {
            NopStmt newNopStmt6 = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) newNopStmt6);
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newNopStmt());
            createCatchFormal(catchBlock.formal());
            NopStmt newNopStmt7 = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) newNopStmt7);
            if (this.catchStack == null) {
                this.catchStack = new Stack<>();
            }
            this.catchStack.push(tryStmt);
            createBlock(catchBlock.body());
            this.catchStack.pop();
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newNopStmt());
            Stmt catchFinallyNoop = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(catchFinallyNoop));
            NopStmt newNopStmt8 = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) newNopStmt8);
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt3));
            gotoMap.put(catchFinallyNoop, newNopStmt8);
            Type sootType = Util.getSootType(catchBlock.catchType());
            addToExceptionList(newNopStmt, newNopStmt2, newNopStmt6, Scene.v().getSootClass(sootType.toString()));
            addToExceptionList(newNopStmt7, newNopStmt8, newNopStmt5, Scene.v().getSootClass("java.lang.Throwable"));
        }
        Local formalLocal = this.lg.generateLocal(RefType.v("java.lang.Throwable"));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt5);
        CaughtExceptionRef exceptRef = Jimple.v().newCaughtExceptionRef();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(formalLocal, exceptRef));
        NopStmt newNopStmt9 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt9);
        Local catchAllAssignLocal = this.lg.generateLocal(RefType.v("java.lang.Throwable"));
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(catchAllAssignLocal, formalLocal));
        Stmt catchAllFinallyNoop = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(catchAllFinallyNoop));
        NopStmt newNopStmt10 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) newNopStmt10);
        ThrowStmt newThrowStmt = Jimple.v().newThrowStmt(catchAllAssignLocal);
        newThrowStmt.addTag(new ThrowCreatedByCompilerTag());
        this.body.getUnits().add((UnitPatchingChain) newThrowStmt);
        gotoMap.put(catchAllFinallyNoop, newNopStmt10);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt3));
        addToExceptionList(newNopStmt9, newNopStmt10, newNopStmt5, Scene.v().getSootClass("java.lang.Throwable"));
        for (Stmt noopStmt : gotoMap.keySet()) {
            this.body.getUnits().add((UnitPatchingChain) noopStmt);
            createBlock(tryStmt.finallyBlock());
            Stmt backToStmt = gotoMap.get(noopStmt);
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(backToStmt));
        }
        this.body.getUnits().add((UnitPatchingChain) newNopStmt3);
        addToExceptionList(newNopStmt, newNopStmt4, newNopStmt5, Scene.v().getSootClass("java.lang.Throwable"));
    }

    private void addToExceptionList(Stmt from, Stmt to, Stmt with, SootClass exceptionClass) {
        if (this.exceptionTable == null) {
            this.exceptionTable = new ArrayList<>();
        }
        Trap trap = Jimple.v().newTrap(exceptionClass, from, to, with);
        this.exceptionTable.add(trap);
    }

    public Constant createConstant(Expr expr) {
        Object constantVal = expr.constantValue();
        return getConstant(constantVal, expr.type());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Value createAggressiveExpr(Expr expr, boolean reduceAggressively, boolean reverseCondIfNec) {
        if (expr.isConstant() && expr.constantValue() != null && expr.type() != null && (!(expr instanceof Binary) || !expr.type().toString().equals("java.lang.String"))) {
            return createConstant(expr);
        }
        if (expr instanceof Assign) {
            return getAssignLocal((Assign) expr);
        }
        if (expr instanceof Lit) {
            return createLiteral((Lit) expr);
        }
        if (expr instanceof polyglot.ast.Local) {
            return getLocal((polyglot.ast.Local) expr);
        }
        if (expr instanceof Binary) {
            return getBinaryLocal2((Binary) expr, reduceAggressively);
        }
        if (expr instanceof Unary) {
            return getUnaryLocal((Unary) expr);
        }
        if (expr instanceof Cast) {
            return getCastLocal((Cast) expr);
        }
        if (expr instanceof ArrayAccess) {
            return getArrayRefLocal((ArrayAccess) expr);
        }
        if (expr instanceof NewArray) {
            return getNewArrayLocal((NewArray) expr);
        }
        if (expr instanceof Call) {
            return getCallLocal((Call) expr);
        }
        if (expr instanceof New) {
            return getNewLocal((New) expr);
        }
        if (expr instanceof Special) {
            return getSpecialLocal((Special) expr);
        }
        if (expr instanceof Instanceof) {
            return getInstanceOfLocal((Instanceof) expr);
        }
        if (expr instanceof Conditional) {
            return getConditionalLocal((Conditional) expr);
        }
        if (expr instanceof Field) {
            return getFieldLocal((Field) expr);
        }
        throw new RuntimeException("Unhandled Expression: " + expr);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Local handlePrivateFieldUnarySet(Unary unary) {
        BinopExpr binExpr;
        Field fLeft = (Field) unary.expr();
        Value base = base().getBaseLocal(fLeft.target());
        Value fieldGetLocal = getPrivateAccessFieldLocal(fLeft, base);
        Local tmp = generateLocal(fLeft.type());
        AssignStmt stmt1 = Jimple.v().newAssignStmt(tmp, fieldGetLocal);
        this.body.getUnits().add((UnitPatchingChain) stmt1);
        Util.addLnPosTags(stmt1, unary.position());
        Value incVal = base().getConstant(Util.getSootType(fLeft.type()), 1);
        if (unary.operator() == Unary.PRE_INC || unary.operator() == Unary.POST_INC) {
            binExpr = Jimple.v().newAddExpr(tmp, incVal);
        } else {
            binExpr = Jimple.v().newSubExpr(tmp, incVal);
        }
        Local tmp2 = generateLocal(fLeft.type());
        AssignStmt assign = Jimple.v().newAssignStmt(tmp2, binExpr);
        this.body.getUnits().add((UnitPatchingChain) assign);
        if (unary.operator() == Unary.PRE_INC || unary.operator() == Unary.PRE_DEC) {
            return base().handlePrivateFieldSet(fLeft, tmp2, base);
        }
        base().handlePrivateFieldSet(fLeft, tmp2, base);
        return tmp;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Local handlePrivateFieldAssignSet(Assign assign) {
        Value right;
        Field fLeft = (Field) assign.left();
        Value fieldBase = base().getBaseLocal(fLeft.target());
        if (assign.operator() == Assign.ASSIGN) {
            right = base().getSimpleAssignRightLocal(assign);
        } else if (assign.operator() == Assign.ADD_ASSIGN && assign.type().toString().equals("java.lang.String")) {
            right = getStringConcatAssignRightLocal(assign);
        } else {
            Local leftLocal = getPrivateAccessFieldLocal(fLeft, fieldBase);
            right = base().getAssignRightLocal(assign, leftLocal);
        }
        return handlePrivateFieldSet(fLeft, right, fieldBase);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Local handlePrivateFieldSet(Expr expr, Value right, Value base) {
        Field fLeft = (Field) expr;
        SootClass containClass = ((RefType) Util.getSootType(fLeft.target().type())).getSootClass();
        SootMethod methToUse = addSetAccessMeth(containClass, fLeft, right);
        ArrayList params = new ArrayList();
        if (!fLeft.flags().isStatic()) {
            params.add(base);
        }
        params.add(right);
        InvokeExpr invoke = Jimple.v().newStaticInvokeExpr(methToUse.makeRef(), params);
        Local retLocal = this.lg.generateLocal(right.getType());
        AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, invoke);
        this.body.getUnits().add((UnitPatchingChain) assignStmt);
        return retLocal;
    }

    private SootMethod addSetAccessMeth(SootClass conClass, Field field, Value param) {
        if (InitialResolver.v().getPrivateFieldSetAccessMap() != null && InitialResolver.v().getPrivateFieldSetAccessMap().containsKey(new IdentityKey(field.fieldInstance()))) {
            return InitialResolver.v().getPrivateFieldSetAccessMap().get(new IdentityKey(field.fieldInstance()));
        }
        String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + TarConstants.VERSION_POSIX;
        ArrayList paramTypes = new ArrayList();
        if (!field.flags().isStatic()) {
            paramTypes.add(conClass.getType());
        }
        paramTypes.add(Util.getSootType(field.type()));
        Type retType = Util.getSootType(field.type());
        SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, retType, 8);
        PrivateFieldSetMethodSource pfsms = new PrivateFieldSetMethodSource(Util.getSootType(field.type()), field.name(), field.flags().isStatic());
        conClass.addMethod(meth);
        meth.setActiveBody(pfsms.getBody(meth, null));
        InitialResolver.v().addToPrivateFieldSetAccessMap(field, meth);
        meth.addTag(new SyntheticTag());
        return meth;
    }

    private SootMethod addGetFieldAccessMeth(SootClass conClass, Field field) {
        if (InitialResolver.v().getPrivateFieldGetAccessMap() != null && InitialResolver.v().getPrivateFieldGetAccessMap().containsKey(new IdentityKey(field.fieldInstance()))) {
            return InitialResolver.v().getPrivateFieldGetAccessMap().get(new IdentityKey(field.fieldInstance()));
        }
        String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + TarConstants.VERSION_POSIX;
        ArrayList paramTypes = new ArrayList();
        if (!field.flags().isStatic()) {
            paramTypes.add(conClass.getType());
        }
        SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(field.type()), 8);
        PrivateFieldAccMethodSource pfams = new PrivateFieldAccMethodSource(Util.getSootType(field.type()), field.name(), field.flags().isStatic(), conClass);
        conClass.addMethod(meth);
        meth.setActiveBody(pfams.getBody(meth, null));
        InitialResolver.v().addToPrivateFieldGetAccessMap(field, meth);
        meth.addTag(new SyntheticTag());
        return meth;
    }

    private SootMethod addGetMethodAccessMeth(SootClass conClass, Call call) {
        if (InitialResolver.v().getPrivateMethodGetAccessMap() != null && InitialResolver.v().getPrivateMethodGetAccessMap().containsKey(new IdentityKey(call.methodInstance()))) {
            return InitialResolver.v().getPrivateMethodGetAccessMap().get(new IdentityKey(call.methodInstance()));
        }
        String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + TarConstants.VERSION_POSIX;
        ArrayList paramTypes = new ArrayList();
        if (!call.methodInstance().flags().isStatic()) {
            paramTypes.add(conClass.getType());
        }
        ArrayList sootParamsTypes = getSootParamsTypes(call);
        paramTypes.addAll(sootParamsTypes);
        SootMethod meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(call.methodInstance().returnType()), 8);
        PrivateMethodAccMethodSource pmams = new PrivateMethodAccMethodSource(call.methodInstance());
        conClass.addMethod(meth);
        meth.setActiveBody(pmams.getBody(meth, null));
        InitialResolver.v().addToPrivateMethodGetAccessMap(call, meth);
        meth.addTag(new SyntheticTag());
        return meth;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Value getAssignRightLocal(Assign assign, Local leftLocal) {
        if (assign.operator() == Assign.ASSIGN) {
            return base().getSimpleAssignRightLocal(assign);
        }
        if (assign.operator() == Assign.ADD_ASSIGN && assign.type().toString().equals("java.lang.String")) {
            return getStringConcatAssignRightLocal(assign);
        }
        return getComplexAssignRightLocal(assign, leftLocal);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Value getSimpleAssignRightLocal(Assign assign) {
        boolean repush = false;
        Stmt tNoop = null;
        Stmt fNoop = null;
        if (!this.trueNoop.empty() && !this.falseNoop.empty()) {
            tNoop = this.trueNoop.pop();
            fNoop = this.falseNoop.pop();
            repush = true;
        }
        Value right = base().createAggressiveExpr(assign.right(), false, false);
        if (repush) {
            this.trueNoop.push(tNoop);
            this.falseNoop.push(fNoop);
        }
        if (right instanceof ConditionExpr) {
            right = handleCondBinExpr((ConditionExpr) right);
        }
        return right;
    }

    private Local getStringConcatAssignRightLocal(Assign assign) {
        Local sb = createStringBuffer(assign);
        Local rLocal = createToString(generateAppends(assign.right(), generateAppends(assign.left(), sb)), assign);
        return rLocal;
    }

    private Local getComplexAssignRightLocal(Assign assign, Local leftLocal) {
        Value right = base().createAggressiveExpr(assign.right(), false, false);
        if (right instanceof ConditionExpr) {
            right = handleCondBinExpr((ConditionExpr) right);
        }
        BinopExpr binop = null;
        if (assign.operator() == Assign.ADD_ASSIGN) {
            binop = Jimple.v().newAddExpr(leftLocal, right);
        } else if (assign.operator() == Assign.SUB_ASSIGN) {
            binop = Jimple.v().newSubExpr(leftLocal, right);
        } else if (assign.operator() == Assign.MUL_ASSIGN) {
            binop = Jimple.v().newMulExpr(leftLocal, right);
        } else if (assign.operator() == Assign.DIV_ASSIGN) {
            binop = Jimple.v().newDivExpr(leftLocal, right);
        } else if (assign.operator() == Assign.MOD_ASSIGN) {
            binop = Jimple.v().newRemExpr(leftLocal, right);
        } else if (assign.operator() == Assign.SHL_ASSIGN) {
            binop = Jimple.v().newShlExpr(leftLocal, right);
        } else if (assign.operator() == Assign.SHR_ASSIGN) {
            binop = Jimple.v().newShrExpr(leftLocal, right);
        } else if (assign.operator() == Assign.USHR_ASSIGN) {
            binop = Jimple.v().newUshrExpr(leftLocal, right);
        } else if (assign.operator() == Assign.BIT_AND_ASSIGN) {
            binop = Jimple.v().newAndExpr(leftLocal, right);
        } else if (assign.operator() == Assign.BIT_OR_ASSIGN) {
            binop = Jimple.v().newOrExpr(leftLocal, right);
        } else if (assign.operator() == Assign.BIT_XOR_ASSIGN) {
            binop = Jimple.v().newXorExpr(leftLocal, right);
        }
        Local retLocal = this.lg.generateLocal(leftLocal.getType());
        AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, binop);
        this.body.getUnits().add((UnitPatchingChain) assignStmt);
        Util.addLnPosTags(binop.getOp1Box(), assign.left().position());
        Util.addLnPosTags(binop.getOp2Box(), assign.right().position());
        return retLocal;
    }

    private Value getSimpleAssignLocal(Assign assign) {
        Value left = base().createLHS(assign.left());
        Value right = base().getSimpleAssignRightLocal(assign);
        AssignStmt stmt = Jimple.v().newAssignStmt(left, right);
        this.body.getUnits().add((UnitPatchingChain) stmt);
        Util.addLnPosTags(stmt, assign.position());
        Util.addLnPosTags(stmt.getRightOpBox(), assign.right().position());
        Util.addLnPosTags(stmt.getLeftOpBox(), assign.left().position());
        if (left instanceof Local) {
            return left;
        }
        return right;
    }

    private Value getStrConAssignLocal(Assign assign) {
        Value left = base().createLHS(assign.left());
        Value right = getStringConcatAssignRightLocal(assign);
        AssignStmt stmt = Jimple.v().newAssignStmt(left, right);
        this.body.getUnits().add((UnitPatchingChain) stmt);
        Util.addLnPosTags(stmt, assign.position());
        Util.addLnPosTags(stmt.getRightOpBox(), assign.right().position());
        Util.addLnPosTags(stmt.getLeftOpBox(), assign.left().position());
        if (left instanceof Local) {
            return left;
        }
        return right;
    }

    protected Value getAssignLocal(Assign assign) {
        Local leftLocal;
        if (base().needsAccessor(assign.left())) {
            return base().handlePrivateFieldAssignSet(assign);
        }
        if (assign.operator() == Assign.ASSIGN) {
            return getSimpleAssignLocal(assign);
        }
        if (assign.operator() == Assign.ADD_ASSIGN && assign.type().toString().equals("java.lang.String")) {
            return getStrConAssignLocal(assign);
        }
        Value left = base().createLHS(assign.left());
        Value left2 = (Value) left.clone();
        if (left instanceof Local) {
            leftLocal = (Local) left;
        } else {
            leftLocal = this.lg.generateLocal(left.getType());
            AssignStmt stmt1 = Jimple.v().newAssignStmt(leftLocal, left);
            this.body.getUnits().add((UnitPatchingChain) stmt1);
            Util.addLnPosTags(stmt1, assign.position());
        }
        Value right = base().getAssignRightLocal(assign, leftLocal);
        AssignStmt stmt2 = Jimple.v().newAssignStmt(leftLocal, right);
        this.body.getUnits().add((UnitPatchingChain) stmt2);
        Util.addLnPosTags(stmt2, assign.position());
        Util.addLnPosTags(stmt2.getRightOpBox(), assign.right().position());
        Util.addLnPosTags(stmt2.getLeftOpBox(), assign.left().position());
        if (!(left instanceof Local)) {
            AssignStmt stmt3 = Jimple.v().newAssignStmt(left2, leftLocal);
            this.body.getUnits().add((UnitPatchingChain) stmt3);
            Util.addLnPosTags(stmt3, assign.position());
            Util.addLnPosTags(stmt3.getRightOpBox(), assign.right().position());
            Util.addLnPosTags(stmt3.getLeftOpBox(), assign.left().position());
        }
        return leftLocal;
    }

    private Value getFieldLocalLeft(Field field) {
        Receiver receiver = field.target();
        if (field.name().equals(XMLConstants.LENGTH_ATTRIBUTE) && (receiver.type() instanceof ArrayType)) {
            return getSpecialArrayLengthLocal(field);
        }
        return getFieldRef(field);
    }

    private Value getFieldLocal(Field field) {
        Receiver receiver = field.target();
        PolyglotMethodSource polyglotMethodSource = (PolyglotMethodSource) this.body.getMethod().getSource();
        if (field.name().equals(XMLConstants.LENGTH_ATTRIBUTE) && (receiver.type() instanceof ArrayType)) {
            return getSpecialArrayLengthLocal(field);
        }
        if (field.name().equals("class")) {
            throw new RuntimeException("Should go through ClassLit");
        }
        if (base().needsAccessor(field)) {
            Value base = base().getBaseLocal(field.target());
            return getPrivateAccessFieldLocal(field, base);
        } else if ((field.target() instanceof Special) && ((Special) field.target()).kind() == Special.SUPER && ((Special) field.target()).qualifier() != null) {
            return getSpecialSuperQualifierLocal(field);
        } else {
            if (shouldReturnConstant(field)) {
                return getReturnConstant(field);
            }
            FieldRef fieldRef = getFieldRef(field);
            Local baseLocal = generateLocal(field.type());
            AssignStmt fieldAssignStmt = Jimple.v().newAssignStmt(baseLocal, fieldRef);
            this.body.getUnits().add((UnitPatchingChain) fieldAssignStmt);
            Util.addLnPosTags(fieldAssignStmt, field.position());
            Util.addLnPosTags(fieldAssignStmt.getRightOpBox(), field.position());
            return baseLocal;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public boolean needsAccessor(Expr expr) {
        if (!(expr instanceof Field) && !(expr instanceof Call)) {
            return false;
        }
        if (expr instanceof Field) {
            return needsAccessor(((Field) expr).fieldInstance());
        }
        return needsAccessor(((Call) expr).methodInstance());
    }

    protected boolean needsAccessor(MemberInstance inst) {
        if (inst.flags().isPrivate()) {
            if (!Util.getSootType(inst.container()).equals(this.body.getMethod().getDeclaringClass().getType())) {
                return true;
            }
            return false;
        } else if (!inst.flags().isProtected() || Util.getSootType(inst.container()).equals(this.body.getMethod().getDeclaringClass().getType())) {
            return false;
        } else {
            SootClass currentClass = this.body.getMethod().getDeclaringClass();
            if (currentClass.getSuperclass().getType().equals(Util.getSootType(inst.container()))) {
                return false;
            }
            while (currentClass.hasOuterClass()) {
                currentClass = currentClass.getOuterClass();
                if (Util.getSootType(inst.container()).equals(currentClass.getType())) {
                    return false;
                }
                if (Util.getSootType(inst.container()).equals(currentClass.getSuperclass().getType())) {
                    return true;
                }
            }
            return false;
        }
    }

    private Constant getReturnConstant(Field field) {
        return getConstant(field.constantValue(), field.type());
    }

    private Constant getConstant(Object constVal, polyglot.types.Type type) {
        char val;
        if (constVal instanceof String) {
            return StringConstant.v((String) constVal);
        }
        if (constVal instanceof Boolean) {
            boolean val2 = ((Boolean) constVal).booleanValue();
            return IntConstant.v(val2 ? 1 : 0);
        } else if (type.isChar()) {
            if (constVal instanceof Integer) {
                val = (char) ((Integer) constVal).intValue();
            } else {
                val = ((Character) constVal).charValue();
            }
            return IntConstant.v(val);
        } else {
            Number num = createConstantCast(type, (Number) constVal);
            if (num instanceof Long) {
                return LongConstant.v(((Long) num).longValue());
            }
            if (num instanceof Double) {
                return DoubleConstant.v(((Double) num).doubleValue());
            }
            if (num instanceof Float) {
                return FloatConstant.v(((Float) num).floatValue());
            }
            if (num instanceof Byte) {
                return IntConstant.v(((Byte) num).byteValue());
            }
            if (num instanceof Short) {
                return IntConstant.v(((Short) num).shortValue());
            }
            return IntConstant.v(((Integer) num).intValue());
        }
    }

    private Number createConstantCast(polyglot.types.Type fieldType, Number constant) {
        if (constant instanceof Integer) {
            if (fieldType.isDouble()) {
                return new Double(((Integer) constant).intValue());
            }
            if (fieldType.isFloat()) {
                return new Float(((Integer) constant).intValue());
            }
            if (fieldType.isLong()) {
                return new Long(((Integer) constant).intValue());
            }
        }
        return constant;
    }

    private boolean shouldReturnConstant(Field field) {
        if (field.isConstant() && field.constantValue() != null) {
            return true;
        }
        return false;
    }

    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    protected FieldRef getFieldRef(Field field) {
        FieldRef fieldRef;
        SootClass receiverClass = ((RefType) Util.getSootType(field.target().type())).getSootClass();
        SootFieldRef receiverField = Scene.v().makeFieldRef(receiverClass, field.name(), Util.getSootType(field.type()), field.flags().isStatic());
        if (field.fieldInstance().flags().isStatic()) {
            fieldRef = Jimple.v().newStaticFieldRef(receiverField);
        } else {
            Local base = (Local) base().getBaseLocal(field.target());
            fieldRef = Jimple.v().newInstanceFieldRef(base, receiverField);
        }
        if ((field.target() instanceof polyglot.ast.Local) && (fieldRef instanceof InstanceFieldRef)) {
            Util.addLnPosTags(((InstanceFieldRef) fieldRef).getBaseBox(), field.target().position());
        }
        return fieldRef;
    }

    private Local getPrivateAccessFieldLocal(Field field, Value base) {
        SootClass addToClass;
        SootMethod toInvoke;
        if (field.fieldInstance().flags().isPrivate()) {
            toInvoke = addGetFieldAccessMeth(((RefType) Util.getSootType(field.fieldInstance().container())).getSootClass(), field);
            ((RefType) Util.getSootType(field.fieldInstance().container())).getSootClass();
        } else {
            if (InitialResolver.v().hierarchy() == null) {
                InitialResolver.v().hierarchy(new FastHierarchy());
            }
            SootClass containingClass = ((RefType) Util.getSootType(field.fieldInstance().container())).getSootClass();
            if (this.body.getMethod().getDeclaringClass().hasOuterClass()) {
                SootClass outerClass = this.body.getMethod().getDeclaringClass().getOuterClass();
                while (true) {
                    addToClass = outerClass;
                    if (InitialResolver.v().hierarchy().canStoreType(containingClass.getType(), addToClass.getType()) || !addToClass.hasOuterClass()) {
                        break;
                    }
                    outerClass = addToClass.getOuterClass();
                }
            } else {
                addToClass = containingClass;
            }
            toInvoke = addGetFieldAccessMeth(addToClass, field);
        }
        ArrayList params = new ArrayList();
        if (!field.fieldInstance().flags().isStatic()) {
            params.add(base);
        }
        return Util.getPrivateAccessFieldInvoke(toInvoke.makeRef(), params, this.body, this.lg);
    }

    private Local getSpecialClassLitLocal(ClassLit lit) {
        SootFieldRef sootField;
        SootMethodRef invokeMeth;
        if (lit.typeNode().type().isPrimitive()) {
            PrimitiveType primType = (PrimitiveType) lit.typeNode().type();
            Local retLocal = this.lg.generateLocal(RefType.v("java.lang.Class"));
            SootFieldRef primField = null;
            if (primType.isBoolean()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_BOOLEAN), "TYPE", RefType.v("java.lang.Class"), true);
            } else if (primType.isByte()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_BYTE), "TYPE", RefType.v("java.lang.Class"), true);
            } else if (primType.isChar()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_CHARACTER), "TYPE", RefType.v("java.lang.Class"), true);
            } else if (primType.isDouble()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_DOUBLE), "TYPE", RefType.v("java.lang.Class"), true);
            } else if (primType.isFloat()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_FLOAT), "TYPE", RefType.v("java.lang.Class"), true);
            } else if (primType.isInt()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_INTEGER), "TYPE", RefType.v("java.lang.Class"), true);
            } else if (primType.isLong()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_LONG), "TYPE", RefType.v("java.lang.Class"), true);
            } else if (primType.isShort()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass(JavaBasicTypes.JAVA_LANG_SHORT), "TYPE", RefType.v("java.lang.Class"), true);
            } else if (primType.isVoid()) {
                primField = Scene.v().makeFieldRef(Scene.v().getSootClass("java.lang.Void"), "TYPE", RefType.v("java.lang.Class"), true);
            }
            AssignStmt assignStmt = Jimple.v().newAssignStmt(retLocal, Jimple.v().newStaticFieldRef(primField));
            this.body.getUnits().add((UnitPatchingChain) assignStmt);
            return retLocal;
        }
        SootClass thisClass = this.body.getMethod().getDeclaringClass();
        String fieldName = Util.getFieldNameForClassLit(lit.typeNode().type());
        Type fieldType = RefType.v("java.lang.Class");
        Local fieldLocal = this.lg.generateLocal(RefType.v("java.lang.Class"));
        if (thisClass.isInterface()) {
            HashMap<SootClass, SootClass> specialAnonMap = InitialResolver.v().specialAnonMap();
            if (specialAnonMap != null && specialAnonMap.containsKey(thisClass)) {
                SootClass specialClass = specialAnonMap.get(thisClass);
                sootField = Scene.v().makeFieldRef(specialClass, fieldName, fieldType, true);
            } else {
                throw new RuntimeException("Class is interface so it must have an anon class to handle class lits but its anon class cannot be found.");
            }
        } else {
            sootField = Scene.v().makeFieldRef(thisClass, fieldName, fieldType, true);
        }
        StaticFieldRef fieldRef = Jimple.v().newStaticFieldRef(sootField);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(fieldLocal, fieldRef));
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        soot.jimple.Expr neExpr = Jimple.v().newNeExpr(fieldLocal, NullConstant.v());
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newIfStmt(neExpr, newNopStmt));
        ArrayList paramTypes = new ArrayList();
        paramTypes.add(RefType.v("java.lang.String"));
        if (thisClass.isInterface()) {
            HashMap<SootClass, SootClass> specialAnonMap2 = InitialResolver.v().specialAnonMap();
            if (specialAnonMap2 != null && specialAnonMap2.containsKey(thisClass)) {
                SootClass specialClass2 = specialAnonMap2.get(thisClass);
                invokeMeth = Scene.v().makeMethodRef(specialClass2, "class$", paramTypes, RefType.v("java.lang.Class"), true);
            } else {
                throw new RuntimeException("Class is interface so it must have an anon class to handle class lits but its anon class cannot be found.");
            }
        } else {
            invokeMeth = Scene.v().makeMethodRef(thisClass, "class$", paramTypes, RefType.v("java.lang.Class"), true);
        }
        ArrayList params = new ArrayList();
        params.add(StringConstant.v(Util.getParamNameForClassLit(lit.typeNode().type())));
        soot.jimple.Expr classInvoke = Jimple.v().newStaticInvokeExpr(invokeMeth, params);
        Local methLocal = this.lg.generateLocal(RefType.v("java.lang.Class"));
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(methLocal, classInvoke));
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(fieldRef, methLocal));
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt2));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(methLocal, fieldRef));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        return methLocal;
    }

    private Local getSpecialArrayLengthLocal(Field field) {
        Local localField;
        Receiver receiver = field.target();
        if (receiver instanceof polyglot.ast.Local) {
            localField = getLocal((polyglot.ast.Local) receiver);
        } else if (receiver instanceof Expr) {
            localField = (Local) base().createAggressiveExpr((Expr) receiver, false, false);
        } else {
            localField = generateLocal(receiver.type());
        }
        LengthExpr lengthExpr = Jimple.v().newLengthExpr(localField);
        Local retLocal = this.lg.generateLocal(IntType.v());
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(retLocal, lengthExpr);
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        Util.addLnPosTags(newAssignStmt, field.position());
        Util.addLnPosTags(lengthExpr.getOpBox(), field.target().position());
        return retLocal;
    }

    private Value getBinaryLocal2(Binary binary, boolean reduceAggressively) {
        Value rhs;
        if (binary.operator() == Binary.COND_AND) {
            return createCondAnd(binary);
        }
        if (binary.operator() == Binary.COND_OR) {
            return createCondOr(binary);
        }
        if (binary.type().toString().equals("java.lang.String")) {
            if (areAllStringLits(binary)) {
                String result = createStringConstant(binary);
                return StringConstant.v(result);
            }
            Local sb = createStringBuffer(binary);
            return createToString(generateAppends(binary.right(), generateAppends(binary.left(), sb)), binary);
        }
        Value lVal = base().createAggressiveExpr(binary.left(), true, false);
        Value rVal = base().createAggressiveExpr(binary.right(), true, false);
        if (isComparisonBinary(binary.operator())) {
            rhs = getBinaryComparisonExpr(lVal, rVal, binary.operator());
        } else {
            rhs = getBinaryExpr(lVal, rVal, binary.operator());
        }
        if (rhs instanceof BinopExpr) {
            Util.addLnPosTags(((BinopExpr) rhs).getOp1Box(), binary.left().position());
            Util.addLnPosTags(((BinopExpr) rhs).getOp2Box(), binary.right().position());
        }
        if ((rhs instanceof ConditionExpr) && !reduceAggressively) {
            return rhs;
        }
        if (rhs instanceof ConditionExpr) {
            rhs = handleCondBinExpr((ConditionExpr) rhs, true);
        }
        Local lhs = generateLocal(binary.type());
        AssignStmt assignStmt = Jimple.v().newAssignStmt(lhs, rhs);
        this.body.getUnits().add((UnitPatchingChain) assignStmt);
        Util.addLnPosTags(assignStmt.getRightOpBox(), binary.position());
        Util.addLnPosTags(assignStmt, binary.position());
        return lhs;
    }

    private boolean areAllStringLits(Node node) {
        if (node instanceof StringLit) {
            return true;
        }
        if (node instanceof Field) {
            if (shouldReturnConstant((Field) node)) {
                return true;
            }
            return false;
        } else if (node instanceof Binary) {
            if (areAllStringLitsBinary((Binary) node)) {
                return true;
            }
            return false;
        } else if (node instanceof Unary) {
            Unary unary = (Unary) node;
            if (unary.isConstant()) {
                return true;
            }
            return false;
        } else if (node instanceof Cast) {
            Cast cast = (Cast) node;
            if (cast.isConstant()) {
                return true;
            }
            return false;
        } else if (node instanceof Lit) {
            Lit lit = (Lit) node;
            if (lit.isConstant()) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    private boolean areAllStringLitsBinary(Binary binary) {
        if (areAllStringLits(binary.left()) && areAllStringLits(binary.right())) {
            return true;
        }
        return false;
    }

    private String createStringConstant(Node node) {
        String s;
        if (node instanceof StringLit) {
            s = ((StringLit) node).value();
        } else if (node instanceof Cast) {
            Cast cast = (Cast) node;
            if (cast.type().isChar()) {
                s = new StringBuilder().append(((Character) cast.constantValue()).charValue()).toString();
            } else {
                s = new StringBuilder().append(cast.constantValue()).toString();
            }
        } else if (node instanceof Unary) {
            Unary unary = (Unary) node;
            s = new StringBuilder().append(unary.constantValue()).toString();
        } else if (node instanceof CharLit) {
            s = new StringBuilder().append(((CharLit) node).value()).toString();
        } else if (node instanceof BooleanLit) {
            s = new StringBuilder().append(((BooleanLit) node).value()).toString();
        } else if (node instanceof IntLit) {
            s = new StringBuilder().append(((IntLit) node).value()).toString();
        } else if (node instanceof FloatLit) {
            s = new StringBuilder().append(((FloatLit) node).value()).toString();
        } else if (node instanceof NullLit) {
            s = Jimple.NULL;
        } else if (node instanceof Field) {
            Field field = (Field) node;
            if (field.fieldInstance().constantValue() instanceof String) {
                s = (String) field.constantValue();
            } else if (field.fieldInstance().constantValue() instanceof Boolean) {
                boolean val = ((Boolean) field.constantValue()).booleanValue();
                int temp = val ? 1 : 0;
                s = new StringBuilder().append(temp).toString();
            } else if (field.type().isChar()) {
                char val2 = (char) ((Integer) field.constantValue()).intValue();
                s = new StringBuilder().append(val2).toString();
            } else {
                Number num = createConstantCast(field.type(), (Number) field.fieldInstance().constantValue());
                if (num instanceof Long) {
                    s = new StringBuilder().append(((Long) num).longValue()).toString();
                } else if (num instanceof Double) {
                    s = new StringBuilder().append(((Double) num).doubleValue()).toString();
                } else if (num instanceof Float) {
                    s = new StringBuilder().append(((Float) num).floatValue()).toString();
                } else if (num instanceof Byte) {
                    s = new StringBuilder().append((int) ((Byte) num).byteValue()).toString();
                } else if (num instanceof Short) {
                    s = new StringBuilder().append((int) ((Short) num).shortValue()).toString();
                } else {
                    s = new StringBuilder().append(((Integer) num).intValue()).toString();
                }
            }
        } else if (node instanceof Binary) {
            s = createStringConstantBinary((Binary) node);
        } else {
            throw new RuntimeException("No other string constant folding done");
        }
        return s;
    }

    private String createStringConstantBinary(Binary binary) {
        String s;
        if (Util.getSootType(binary.type()).toString().equals("java.lang.String")) {
            s = String.valueOf(createStringConstant(binary.left())) + createStringConstant(binary.right());
        } else {
            s = binary.constantValue().toString();
        }
        return s;
    }

    private boolean isComparisonBinary(Binary.Operator op) {
        if (op == Binary.EQ || op == Binary.NE || op == Binary.GE || op == Binary.GT || op == Binary.LE || op == Binary.LT) {
            return true;
        }
        return false;
    }

    private Value getBinaryExpr(Value lVal, Value rVal, Binary.Operator operator) {
        Value rValue;
        if (lVal instanceof ConditionExpr) {
            lVal = handleCondBinExpr((ConditionExpr) lVal);
        }
        if (rVal instanceof ConditionExpr) {
            rVal = handleCondBinExpr((ConditionExpr) rVal);
        }
        if (operator == Binary.ADD) {
            rValue = Jimple.v().newAddExpr(lVal, rVal);
        } else if (operator == Binary.SUB) {
            rValue = Jimple.v().newSubExpr(lVal, rVal);
        } else if (operator == Binary.MUL) {
            rValue = Jimple.v().newMulExpr(lVal, rVal);
        } else if (operator == Binary.DIV) {
            rValue = Jimple.v().newDivExpr(lVal, rVal);
        } else if (operator == Binary.SHR) {
            if (rVal.getType().equals(LongType.v())) {
                Local intVal = this.lg.generateLocal(IntType.v());
                CastExpr castExpr = Jimple.v().newCastExpr(rVal, IntType.v());
                AssignStmt assignStmt = Jimple.v().newAssignStmt(intVal, castExpr);
                this.body.getUnits().add((UnitPatchingChain) assignStmt);
                rValue = Jimple.v().newShrExpr(lVal, intVal);
            } else {
                rValue = Jimple.v().newShrExpr(lVal, rVal);
            }
        } else if (operator == Binary.USHR) {
            if (rVal.getType().equals(LongType.v())) {
                Local intVal2 = this.lg.generateLocal(IntType.v());
                CastExpr castExpr2 = Jimple.v().newCastExpr(rVal, IntType.v());
                AssignStmt assignStmt2 = Jimple.v().newAssignStmt(intVal2, castExpr2);
                this.body.getUnits().add((UnitPatchingChain) assignStmt2);
                rValue = Jimple.v().newUshrExpr(lVal, intVal2);
            } else {
                rValue = Jimple.v().newUshrExpr(lVal, rVal);
            }
        } else if (operator == Binary.SHL) {
            if (rVal.getType().equals(LongType.v())) {
                Local intVal3 = this.lg.generateLocal(IntType.v());
                CastExpr castExpr3 = Jimple.v().newCastExpr(rVal, IntType.v());
                AssignStmt assignStmt3 = Jimple.v().newAssignStmt(intVal3, castExpr3);
                this.body.getUnits().add((UnitPatchingChain) assignStmt3);
                rValue = Jimple.v().newShlExpr(lVal, intVal3);
            } else {
                rValue = Jimple.v().newShlExpr(lVal, rVal);
            }
        } else if (operator == Binary.BIT_AND) {
            rValue = Jimple.v().newAndExpr(lVal, rVal);
        } else if (operator == Binary.BIT_OR) {
            rValue = Jimple.v().newOrExpr(lVal, rVal);
        } else if (operator == Binary.BIT_XOR) {
            rValue = Jimple.v().newXorExpr(lVal, rVal);
        } else if (operator == Binary.MOD) {
            rValue = Jimple.v().newRemExpr(lVal, rVal);
        } else {
            throw new RuntimeException("Binary not yet handled!");
        }
        return rValue;
    }

    private Value getBinaryComparisonExpr(Value lVal, Value rVal, Binary.Operator operator) {
        Value rValue;
        if (operator == Binary.EQ) {
            rValue = Jimple.v().newEqExpr(lVal, rVal);
        } else if (operator == Binary.GE) {
            rValue = Jimple.v().newGeExpr(lVal, rVal);
        } else if (operator == Binary.GT) {
            rValue = Jimple.v().newGtExpr(lVal, rVal);
        } else if (operator == Binary.LE) {
            rValue = Jimple.v().newLeExpr(lVal, rVal);
        } else if (operator == Binary.LT) {
            rValue = Jimple.v().newLtExpr(lVal, rVal);
        } else if (operator == Binary.NE) {
            rValue = Jimple.v().newNeExpr(lVal, rVal);
        } else {
            throw new RuntimeException("Unknown Comparison Expr");
        }
        return rValue;
    }

    private Value reverseCondition(ConditionExpr cond) {
        ConditionExpr newExpr;
        if (cond instanceof EqExpr) {
            newExpr = Jimple.v().newNeExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof NeExpr) {
            newExpr = Jimple.v().newEqExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof GtExpr) {
            newExpr = Jimple.v().newLeExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof GeExpr) {
            newExpr = Jimple.v().newLtExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof LtExpr) {
            newExpr = Jimple.v().newGeExpr(cond.getOp1(), cond.getOp2());
        } else if (cond instanceof LeExpr) {
            newExpr = Jimple.v().newGtExpr(cond.getOp1(), cond.getOp2());
        } else {
            throw new RuntimeException("Unknown Condition Expr");
        }
        newExpr.getOp1Box().addAllTagsOf(cond.getOp1Box());
        newExpr.getOp2Box().addAllTagsOf(cond.getOp2Box());
        return newExpr;
    }

    private Value handleDFLCond(ConditionExpr cond) {
        soot.jimple.Expr cmExpr;
        ConditionExpr cond2;
        Local result = this.lg.generateLocal(ByteType.v());
        if (isDouble(cond.getOp1()) || isDouble(cond.getOp2()) || isFloat(cond.getOp1()) || isFloat(cond.getOp2())) {
            if ((cond instanceof GeExpr) || (cond instanceof GtExpr)) {
                cmExpr = Jimple.v().newCmpgExpr(cond.getOp1(), cond.getOp2());
            } else {
                cmExpr = Jimple.v().newCmplExpr(cond.getOp1(), cond.getOp2());
            }
        } else if (isLong(cond.getOp1()) || isLong(cond.getOp2())) {
            cmExpr = Jimple.v().newCmpExpr(cond.getOp1(), cond.getOp2());
        } else {
            return cond;
        }
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(result, cmExpr));
        if (cond instanceof EqExpr) {
            cond2 = Jimple.v().newEqExpr(result, IntConstant.v(0));
        } else if (cond instanceof GeExpr) {
            cond2 = Jimple.v().newGeExpr(result, IntConstant.v(0));
        } else if (cond instanceof GtExpr) {
            cond2 = Jimple.v().newGtExpr(result, IntConstant.v(0));
        } else if (cond instanceof LeExpr) {
            cond2 = Jimple.v().newLeExpr(result, IntConstant.v(0));
        } else if (cond instanceof LtExpr) {
            cond2 = Jimple.v().newLtExpr(result, IntConstant.v(0));
        } else if (cond instanceof NeExpr) {
            cond2 = Jimple.v().newNeExpr(result, IntConstant.v(0));
        } else {
            throw new RuntimeException("Unknown Comparison Expr");
        }
        return cond2;
    }

    private boolean isDouble(Value val) {
        if (val.getType() instanceof DoubleType) {
            return true;
        }
        return false;
    }

    private boolean isFloat(Value val) {
        if (val.getType() instanceof FloatType) {
            return true;
        }
        return false;
    }

    private boolean isLong(Value val) {
        if (val.getType() instanceof LongType) {
            return true;
        }
        return false;
    }

    private Value createCondAnd(Binary binary) {
        Value lVal;
        Value rVal;
        Local retLocal = this.lg.generateLocal(BooleanType.v());
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        Value lVal2 = base().createAggressiveExpr(binary.left(), false, false);
        boolean leftNeedIf = needSootIf(lVal2);
        if (!(lVal2 instanceof ConditionExpr)) {
            lVal = Jimple.v().newEqExpr(lVal2, IntConstant.v(0));
        } else {
            lVal = handleDFLCond((ConditionExpr) reverseCondition((ConditionExpr) lVal2));
        }
        if (leftNeedIf) {
            IfStmt ifLeft = Jimple.v().newIfStmt(lVal, newNopStmt);
            this.body.getUnits().add((UnitPatchingChain) ifLeft);
            Util.addLnPosTags(ifLeft.getConditionBox(), binary.left().position());
            Util.addLnPosTags(ifLeft, binary.left().position());
        }
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        Value rVal2 = base().createAggressiveExpr(binary.right(), false, false);
        boolean rightNeedIf = needSootIf(rVal2);
        if (!(rVal2 instanceof ConditionExpr)) {
            rVal = Jimple.v().newEqExpr(rVal2, IntConstant.v(0));
        } else {
            rVal = handleDFLCond((ConditionExpr) reverseCondition((ConditionExpr) rVal2));
        }
        if (rightNeedIf) {
            IfStmt ifRight = Jimple.v().newIfStmt(rVal, newNopStmt);
            this.body.getUnits().add((UnitPatchingChain) ifRight);
            Util.addLnPosTags(ifRight.getConditionBox(), binary.right().position());
            Util.addLnPosTags(ifRight, binary.right().position());
        }
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(retLocal, IntConstant.v(1));
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt2));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        AssignStmt newAssignStmt2 = Jimple.v().newAssignStmt(retLocal, IntConstant.v(0));
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt2);
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        Util.addLnPosTags(newAssignStmt, binary.position());
        Util.addLnPosTags(newAssignStmt2, binary.position());
        return retLocal;
    }

    private Value createCondOr(Binary binary) {
        Value lVal;
        Value rVal;
        Local retLocal = this.lg.generateLocal(BooleanType.v());
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        NopStmt newNopStmt3 = Jimple.v().newNopStmt();
        Value lVal2 = base().createAggressiveExpr(binary.left(), false, false);
        boolean leftNeedIf = needSootIf(lVal2);
        if (!(lVal2 instanceof ConditionExpr)) {
            lVal = Jimple.v().newNeExpr(lVal2, IntConstant.v(0));
        } else {
            lVal = handleDFLCond((ConditionExpr) lVal2);
        }
        if (leftNeedIf) {
            IfStmt ifLeft = Jimple.v().newIfStmt(lVal, newNopStmt2);
            this.body.getUnits().add((UnitPatchingChain) ifLeft);
            Util.addLnPosTags(ifLeft, binary.left().position());
            Util.addLnPosTags(ifLeft.getConditionBox(), binary.left().position());
        }
        Value rVal2 = base().createAggressiveExpr(binary.right(), false, false);
        boolean rightNeedIf = needSootIf(rVal2);
        if (!(rVal2 instanceof ConditionExpr)) {
            rVal = Jimple.v().newEqExpr(rVal2, IntConstant.v(0));
        } else {
            if (this.inLeftOr == 0) {
                rVal2 = reverseCondition((ConditionExpr) rVal2);
            }
            rVal = handleDFLCond((ConditionExpr) rVal2);
        }
        if (rightNeedIf) {
            IfStmt ifRight = Jimple.v().newIfStmt(rVal, newNopStmt3);
            this.body.getUnits().add((UnitPatchingChain) ifRight);
            Util.addLnPosTags(ifRight, binary.right().position());
            Util.addLnPosTags(ifRight.getConditionBox(), binary.right().position());
        }
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(retLocal, IntConstant.v(1));
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        Util.addLnPosTags(newAssignStmt, binary.position());
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt3);
        AssignStmt newAssignStmt2 = Jimple.v().newAssignStmt(retLocal, IntConstant.v(0));
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt2);
        Util.addLnPosTags(newAssignStmt2, binary.position());
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        Util.addLnPosTags(newAssignStmt, binary.position());
        Util.addLnPosTags(newAssignStmt2, binary.position());
        return retLocal;
    }

    private Local handleCondBinExpr(ConditionExpr condExpr) {
        Local boolLocal = this.lg.generateLocal(BooleanType.v());
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        Value newVal = reverseCondition(condExpr);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newIfStmt(handleDFLCond((ConditionExpr) newVal), newNopStmt));
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(boolLocal, IntConstant.v(1)));
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt2));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(boolLocal, IntConstant.v(0)));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        return boolLocal;
    }

    private Local handleCondBinExpr(ConditionExpr condExpr, boolean reverse) {
        Local boolLocal = this.lg.generateLocal(BooleanType.v());
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        Value newVal = condExpr;
        if (reverse) {
            newVal = reverseCondition(condExpr);
        }
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newIfStmt(handleDFLCond((ConditionExpr) newVal), newNopStmt));
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(boolLocal, IntConstant.v(1)));
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt2));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(boolLocal, IntConstant.v(0)));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        return boolLocal;
    }

    private Local createStringBuffer(Expr expr) {
        Local local = this.lg.generateLocal(RefType.v("java.lang.StringBuffer"));
        NewExpr newExpr = Jimple.v().newNewExpr(RefType.v("java.lang.StringBuffer"));
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(local, newExpr);
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        Util.addLnPosTags(newAssignStmt, expr.position());
        SootClass classToInvoke1 = Scene.v().getSootClass("java.lang.StringBuffer");
        SootMethodRef methodToInvoke1 = Scene.v().makeMethodRef(classToInvoke1, "<init>", new ArrayList(), VoidType.v(), false);
        SpecialInvokeExpr invoke = Jimple.v().newSpecialInvokeExpr(local, methodToInvoke1);
        InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(invoke);
        this.body.getUnits().add((UnitPatchingChain) newInvokeStmt);
        Util.addLnPosTags(newInvokeStmt, expr.position());
        return local;
    }

    private Local createToString(Local sb, Expr expr) {
        Local newString = this.lg.generateLocal(RefType.v("java.lang.String"));
        SootClass classToInvoke2 = Scene.v().getSootClass("java.lang.StringBuffer");
        SootMethodRef methodToInvoke2 = Scene.v().makeMethodRef(classToInvoke2, "toString", new ArrayList(), RefType.v("java.lang.String"), false);
        VirtualInvokeExpr toStringInvoke = Jimple.v().newVirtualInvokeExpr(sb, methodToInvoke2);
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(newString, toStringInvoke);
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        Util.addLnPosTags(newAssignStmt, expr.position());
        return newString;
    }

    private boolean isStringConcat(Expr expr) {
        if (expr instanceof Binary) {
            Binary bin = (Binary) expr;
            if (bin.operator() == Binary.ADD && bin.type().toString().equals("java.lang.String")) {
                return true;
            }
            return false;
        } else if (expr instanceof Assign) {
            Assign assign = (Assign) expr;
            if (assign.operator() == Assign.ADD_ASSIGN && assign.type().toString().equals("java.lang.String")) {
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    private Local generateAppends(Expr expr, Local sb) {
        Local sb2;
        if (isStringConcat(expr)) {
            if (expr instanceof Binary) {
                sb2 = generateAppends(((Binary) expr).right(), generateAppends(((Binary) expr).left(), sb));
            } else {
                sb2 = generateAppends(((Assign) expr).right(), generateAppends(((Assign) expr).left(), sb));
            }
        } else {
            Value toApp = base().createAggressiveExpr(expr, false, false);
            RefType appendType = null;
            if (toApp instanceof StringConstant) {
                appendType = RefType.v("java.lang.String");
            } else if (toApp instanceof NullConstant) {
                appendType = Scene.v().getObjectType();
            } else if (toApp instanceof Constant) {
                appendType = toApp.getType();
            } else if (toApp instanceof Local) {
                if (((Local) toApp).getType() instanceof PrimType) {
                    appendType = ((Local) toApp).getType();
                } else if (((Local) toApp).getType() instanceof RefType) {
                    if (((Local) toApp).getType().toString().equals("java.lang.String")) {
                        appendType = RefType.v("java.lang.String");
                    } else if (((Local) toApp).getType().toString().equals("java.lang.StringBuffer")) {
                        appendType = RefType.v("java.lang.StringBuffer");
                    } else {
                        appendType = Scene.v().getObjectType();
                    }
                } else {
                    appendType = Scene.v().getObjectType();
                }
            } else if (toApp instanceof ConditionExpr) {
                toApp = handleCondBinExpr((ConditionExpr) toApp);
                appendType = BooleanType.v();
            }
            if ((appendType instanceof ShortType) || (appendType instanceof ByteType)) {
                Value intLocal = this.lg.generateLocal(IntType.v());
                soot.jimple.Expr cast = Jimple.v().newCastExpr(toApp, IntType.v());
                this.body.getUnits().add((UnitPatchingChain) Jimple.v().newAssignStmt(intLocal, cast));
                toApp = intLocal;
                appendType = IntType.v();
            }
            ArrayList paramsTypes = new ArrayList();
            paramsTypes.add(appendType);
            ArrayList params = new ArrayList();
            params.add(toApp);
            SootClass classToInvoke = Scene.v().getSootClass("java.lang.StringBuffer");
            SootMethodRef methodToInvoke = Scene.v().makeMethodRef(classToInvoke, "append", paramsTypes, RefType.v("java.lang.StringBuffer"), false);
            VirtualInvokeExpr appendInvoke = Jimple.v().newVirtualInvokeExpr(sb, methodToInvoke, params);
            Util.addLnPosTags(appendInvoke.getArgBox(0), expr.position());
            Local tmp = this.lg.generateLocal(RefType.v("java.lang.StringBuffer"));
            AssignStmt newAssignStmt = Jimple.v().newAssignStmt(tmp, appendInvoke);
            sb2 = tmp;
            this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
            Util.addLnPosTags(newAssignStmt, expr.position());
        }
        return sb2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private Value getUnaryLocal(Unary unary) {
        BinopExpr binExpr;
        IfStmt ifStmt;
        Value sootExpr;
        Expr expr = unary.expr();
        Unary.Operator op = unary.operator();
        if (op == Unary.POST_INC || op == Unary.PRE_INC || op == Unary.POST_DEC || op == Unary.PRE_DEC) {
            if (base().needsAccessor(unary.expr())) {
                return base().handlePrivateFieldUnarySet(unary);
            }
            Value left = base().createLHS(unary.expr());
            Value leftClone = Jimple.cloneIfNecessary(left);
            Local tmp = this.lg.generateLocal(left.getType());
            AssignStmt stmt1 = Jimple.v().newAssignStmt(tmp, left);
            this.body.getUnits().add((UnitPatchingChain) stmt1);
            Util.addLnPosTags(stmt1, unary.position());
            Value incVal = base().getConstant(left.getType(), 1);
            if (unary.operator() == Unary.PRE_INC || unary.operator() == Unary.POST_INC) {
                binExpr = Jimple.v().newAddExpr(tmp, incVal);
            } else {
                binExpr = Jimple.v().newSubExpr(tmp, incVal);
            }
            Local tmp2 = this.lg.generateLocal(left.getType());
            AssignStmt assign = Jimple.v().newAssignStmt(tmp2, binExpr);
            this.body.getUnits().add((UnitPatchingChain) assign);
            AssignStmt stmt3 = Jimple.v().newAssignStmt(leftClone, tmp2);
            this.body.getUnits().add((UnitPatchingChain) stmt3);
            if (unary.operator() == Unary.POST_DEC || unary.operator() == Unary.POST_INC) {
                return tmp;
            }
            return tmp2;
        } else if (op == Unary.BIT_NOT) {
            IntConstant.v(-1);
            Local retLocal = generateLocal(expr.type());
            Value sootExpr2 = base().createAggressiveExpr(expr, false, false);
            XorExpr xor = Jimple.v().newXorExpr(sootExpr2, base().getConstant(sootExpr2.getType(), -1));
            Util.addLnPosTags(xor.getOp1Box(), expr.position());
            AssignStmt newAssignStmt = Jimple.v().newAssignStmt(retLocal, xor);
            this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
            Util.addLnPosTags(newAssignStmt, unary.position());
            return retLocal;
        } else if (op == Unary.NEG) {
            if (expr instanceof IntLit) {
                long longVal = ((IntLit) expr).value();
                if (((IntLit) expr).kind() == IntLit.LONG) {
                    sootExpr = LongConstant.v(-longVal);
                } else {
                    sootExpr = IntConstant.v(-((int) longVal));
                }
            } else if (expr instanceof FloatLit) {
                double doubleVal = ((FloatLit) expr).value();
                if (((FloatLit) expr).kind() == FloatLit.DOUBLE) {
                    sootExpr = DoubleConstant.v(-doubleVal);
                } else {
                    sootExpr = FloatConstant.v(-((float) doubleVal));
                }
            } else {
                NegExpr negExpr = Jimple.v().newNegExpr(base().createAggressiveExpr(expr, false, false));
                sootExpr = negExpr;
                Util.addLnPosTags(negExpr.getOpBox(), expr.position());
            }
            Local retLocal2 = generateLocal(expr.type());
            AssignStmt newAssignStmt2 = Jimple.v().newAssignStmt(retLocal2, sootExpr);
            this.body.getUnits().add((UnitPatchingChain) newAssignStmt2);
            Util.addLnPosTags(newAssignStmt2, expr.position());
            return retLocal2;
        } else if (op == Unary.POS) {
            Local retLocal3 = generateLocal(expr.type());
            Value sootExpr3 = base().createAggressiveExpr(expr, false, false);
            AssignStmt newAssignStmt3 = Jimple.v().newAssignStmt(retLocal3, sootExpr3);
            this.body.getUnits().add((UnitPatchingChain) newAssignStmt3);
            Util.addLnPosTags(newAssignStmt3, expr.position());
            return retLocal3;
        } else if (op == Unary.NOT) {
            boolean repush = false;
            Stmt tNoop = null;
            Stmt fNoop = null;
            if (!this.trueNoop.empty() && !this.falseNoop.empty()) {
                tNoop = this.trueNoop.pop();
                fNoop = this.falseNoop.pop();
                repush = true;
            }
            Value local = base().createAggressiveExpr(expr, false, false);
            if (repush) {
                this.trueNoop.push(tNoop);
                this.falseNoop.push(fNoop);
            }
            boolean z = local instanceof ConditionExpr;
            Value local2 = local;
            if (z) {
                local2 = handleCondBinExpr((ConditionExpr) local);
            }
            NeExpr neExpr = Jimple.v().newNeExpr(local2, base().getConstant(local2.getType(), 0));
            NopStmt newNopStmt = Jimple.v().newNopStmt();
            if (!this.falseNoop.empty()) {
                ifStmt = Jimple.v().newIfStmt(neExpr, this.falseNoop.peek());
            } else {
                ifStmt = Jimple.v().newIfStmt(neExpr, newNopStmt);
            }
            this.body.getUnits().add((UnitPatchingChain) ifStmt);
            Util.addLnPosTags(ifStmt, expr.position());
            Util.addLnPosTags(ifStmt.getConditionBox(), expr.position());
            if (!this.falseNoop.empty()) {
                return IntConstant.v(1);
            }
            Local retLocal4 = this.lg.generateLocal(local2.getType());
            AssignStmt newAssignStmt4 = Jimple.v().newAssignStmt(retLocal4, base().getConstant(retLocal4.getType(), 1));
            this.body.getUnits().add((UnitPatchingChain) newAssignStmt4);
            Util.addLnPosTags(newAssignStmt4, expr.position());
            NopStmt newNopStmt2 = Jimple.v().newNopStmt();
            this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt2));
            this.body.getUnits().add((UnitPatchingChain) newNopStmt);
            AssignStmt newAssignStmt5 = Jimple.v().newAssignStmt(retLocal4, base().getConstant(retLocal4.getType(), 0));
            this.body.getUnits().add((UnitPatchingChain) newAssignStmt5);
            Util.addLnPosTags(newAssignStmt5, expr.position());
            this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
            return retLocal4;
        } else {
            throw new RuntimeException("Unhandled Unary Expr");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Constant getConstant(Type type, int val) {
        if (type instanceof DoubleType) {
            return DoubleConstant.v(val);
        }
        if (type instanceof FloatType) {
            return FloatConstant.v(val);
        }
        if (type instanceof LongType) {
            return LongConstant.v(val);
        }
        return IntConstant.v(val);
    }

    private Value getCastLocal(Cast castExpr) {
        if (castExpr.expr().type().equals(castExpr.type()) || (castExpr.type().isClass() && Util.getSootType(castExpr.type()).toString().equals(Scene.v().getObjectType().toString()))) {
            return base().createAggressiveExpr(castExpr.expr(), false, false);
        }
        Value val = base().createAggressiveExpr(castExpr.expr(), false, false);
        Type type = Util.getSootType(castExpr.type());
        CastExpr cast = Jimple.v().newCastExpr(val, type);
        Util.addLnPosTags(cast.getOpBox(), castExpr.expr().position());
        Local retLocal = this.lg.generateLocal(cast.getCastType());
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(retLocal, cast);
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        Util.addLnPosTags(newAssignStmt, castExpr.position());
        return retLocal;
    }

    private ArrayList getSootParams(ProcedureCall call) {
        ArrayList sootParams = new ArrayList();
        for (Expr next : call.arguments()) {
            Value nextExpr = base().createAggressiveExpr(next, false, false);
            if (nextExpr instanceof ConditionExpr) {
                nextExpr = handleCondBinExpr((ConditionExpr) nextExpr);
            }
            sootParams.add(nextExpr);
        }
        return sootParams;
    }

    private ArrayList getSootParamsTypes(ProcedureCall call) {
        ArrayList sootParamsTypes = new ArrayList();
        for (Object next : call.procedureInstance().formalTypes()) {
            sootParamsTypes.add(Util.getSootType((polyglot.types.Type) next));
        }
        return sootParamsTypes;
    }

    private SootMethodRef getMethodFromClass(SootClass sootClass, String name, ArrayList paramTypes, Type returnType, boolean isStatic) {
        SootMethodRef ref = Scene.v().makeMethodRef(sootClass, name, paramTypes, returnType, isStatic);
        return ref;
    }

    private void handleFinalLocalParams(ArrayList sootParams, ArrayList sootParamTypes, ClassType keyType) {
        HashMap<IdentityKey, AnonLocalClassInfo> finalLocalInfo = InitialResolver.v().finalLocalInfo();
        if (finalLocalInfo != null && finalLocalInfo.containsKey(new IdentityKey(keyType))) {
            AnonLocalClassInfo alci = finalLocalInfo.get(new IdentityKey(keyType));
            ArrayList<IdentityKey> finalLocals = alci.finalLocalsUsed();
            if (finalLocals != null) {
                Iterator<IdentityKey> it = finalLocals.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    LocalInstance li = (LocalInstance) ((IdentityKey) next).object();
                    sootParamTypes.add(Util.getSootType(li.type()));
                    sootParams.add(getLocal(li));
                }
            }
        }
    }

    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    protected Local getThis(Type sootType) {
        return Util.getThis(sootType, this.body, this.getThisMap, this.lg);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean needsOuterClassRef(ClassType typeToInvoke) {
        AnonLocalClassInfo info = InitialResolver.v().finalLocalInfo().get(new IdentityKey(typeToInvoke));
        if (InitialResolver.v().isAnonInCCall(typeToInvoke)) {
            return false;
        }
        if (info != null && !info.inStaticMethod()) {
            return true;
        }
        if (typeToInvoke.isNested() && !typeToInvoke.flags().isStatic() && !typeToInvoke.isAnonymous() && !typeToInvoke.isLocal()) {
            return true;
        }
        return false;
    }

    private void handleOuterClassParams(ArrayList sootParams, Value qVal, ArrayList sootParamsTypes, ClassType typeToInvoke) {
        InitialResolver.v().getHasOuterRefInInit();
        boolean addRef = needsOuterClassRef(typeToInvoke);
        if (addRef) {
            SootClass outerClass = ((RefType) Util.getSootType(typeToInvoke.outer())).getSootClass();
            sootParamsTypes.add(outerClass.getType());
        }
        if (addRef && !typeToInvoke.isAnonymous() && qVal != null) {
            sootParams.add(qVal);
        } else if (addRef && !typeToInvoke.isAnonymous()) {
            SootClass outerClass2 = ((RefType) Util.getSootType(typeToInvoke.outer())).getSootClass();
            sootParams.add(getThis(outerClass2.getType()));
        } else if (addRef && typeToInvoke.isAnonymous()) {
            SootClass outerClass3 = ((RefType) Util.getSootType(typeToInvoke.outer())).getSootClass();
            sootParams.add(getThis(outerClass3.getType()));
        }
        if (typeToInvoke.isAnonymous() && qVal != null) {
            sootParamsTypes.add(qVal.getType());
            sootParams.add(qVal);
        }
    }

    private void createConstructorCall(ConstructorCall cCall) {
        SootClass classToInvoke;
        ArrayList sootParams = new ArrayList();
        ArrayList sootParamsTypes = new ArrayList();
        ConstructorInstance cInst = cCall.constructorInstance();
        if (cInst.container() instanceof ClassType) {
            ((ClassType) cInst.container()).fullName();
        }
        if (cCall.kind() == ConstructorCall.SUPER) {
            classToInvoke = ((RefType) Util.getSootType(cInst.container())).getSootClass();
        } else if (cCall.kind() == ConstructorCall.THIS) {
            classToInvoke = this.body.getMethod().getDeclaringClass();
        } else {
            throw new RuntimeException("Unknown kind of Constructor Call");
        }
        Local base = this.specialThisLocal;
        ClassType objType = (ClassType) cInst.container();
        Local qVal = null;
        if (cCall.qualifier() != null) {
            qVal = (Local) base().createAggressiveExpr(cCall.qualifier(), false, false);
        }
        handleOuterClassParams(sootParams, qVal, sootParamsTypes, objType);
        sootParams.addAll(getSootParams(cCall));
        sootParamsTypes.addAll(getSootParamsTypes(cCall));
        handleFinalLocalParams(sootParams, sootParamsTypes, (ClassType) cCall.constructorInstance().container());
        SootMethodRef methodToInvoke = getMethodFromClass(classToInvoke, "<init>", sootParamsTypes, VoidType.v(), false);
        SpecialInvokeExpr specialInvokeExpr = Jimple.v().newSpecialInvokeExpr(base, methodToInvoke, sootParams);
        InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(specialInvokeExpr);
        this.body.getUnits().add((UnitPatchingChain) newInvokeStmt);
        Util.addLnPosTags(newInvokeStmt, cCall.position());
        int numParams = 0;
        for (Expr expr : cCall.arguments()) {
            Util.addLnPosTags(specialInvokeExpr.getArgBox(numParams), expr.position());
            numParams++;
        }
        if (this.body.getMethod().getName().equals("<init>") && cCall.kind() == ConstructorCall.SUPER) {
            handleOuterClassThisInit(this.body.getMethod());
            handleFinalLocalInits();
            handleFieldInits(this.body.getMethod());
            handleInitializerBlocks(this.body.getMethod());
        }
    }

    private void handleFinalLocalInits() {
        ArrayList<SootField> finalsList = ((PolyglotMethodSource) this.body.getMethod().getSource()).getFinalsList();
        if (finalsList == null) {
            return;
        }
        int paramCount = this.paramRefCount - finalsList.size();
        Iterator<SootField> it = finalsList.iterator();
        while (it.hasNext()) {
            SootField sf = it.next();
            FieldRef fieldRef = Jimple.v().newInstanceFieldRef(this.specialThisLocal, sf.makeRef());
            AssignStmt stmt = Jimple.v().newAssignStmt(fieldRef, this.body.getParameterLocal(paramCount));
            this.body.getUnits().add((UnitPatchingChain) stmt);
            paramCount++;
        }
    }

    private void createLocalClassDecl(LocalClassDecl cDecl) {
        InitialResolver.v().getLocalClassMap();
        String name = Util.getSootType(cDecl.decl().type()).toString();
        if (!InitialResolver.v().hasClassInnerTag(this.body.getMethod().getDeclaringClass(), name)) {
            Util.addInnerClassTag(this.body.getMethod().getDeclaringClass(), name, null, cDecl.decl().name(), Util.getModifier(cDecl.decl().flags()));
        }
    }

    private Local getNewLocal(New newExpr) {
        ArrayList sootParams = new ArrayList();
        ArrayList sootParamsTypes = new ArrayList();
        ClassType objType = (ClassType) newExpr.objectType().type();
        if (newExpr.anonType() != null) {
            objType = newExpr.anonType();
            String name = Util.getSootType(objType).toString();
            ClassType outerType = objType.outer();
            if (!InitialResolver.v().hasClassInnerTag(this.body.getMethod().getDeclaringClass(), name)) {
                Util.addInnerClassTag(this.body.getMethod().getDeclaringClass(), name, null, null, outerType.flags().isInterface() ? 9 : Util.getModifier(objType.flags()));
            }
        } else if (!objType.isTopLevel()) {
            String name2 = Util.getSootType(objType).toString();
            ClassType outerType2 = objType.outer();
            if (!InitialResolver.v().hasClassInnerTag(this.body.getMethod().getDeclaringClass(), name2)) {
                Util.addInnerClassTag(this.body.getMethod().getDeclaringClass(), name2, Util.getSootType(outerType2).toString(), objType.name(), outerType2.flags().isInterface() ? 9 : Util.getModifier(objType.flags()));
            }
        }
        RefType sootType = (RefType) Util.getSootType(objType);
        Local retLocal = this.lg.generateLocal(sootType);
        NewExpr sootNew = Jimple.v().newNewExpr(sootType);
        AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, sootNew);
        this.body.getUnits().add((UnitPatchingChain) stmt);
        Util.addLnPosTags(stmt, newExpr.position());
        Util.addLnPosTags(stmt.getRightOpBox(), newExpr.position());
        SootClass classToInvoke = sootType.getSootClass();
        Value qVal = null;
        if (newExpr.qualifier() != null) {
            qVal = base().createAggressiveExpr(newExpr.qualifier(), false, false);
        }
        handleOuterClassParams(sootParams, qVal, sootParamsTypes, objType);
        boolean repush = false;
        Stmt tNoop = null;
        Stmt fNoop = null;
        if (!this.trueNoop.empty() && !this.falseNoop.empty()) {
            tNoop = this.trueNoop.pop();
            fNoop = this.falseNoop.pop();
            repush = true;
        }
        sootParams.addAll(getSootParams(newExpr));
        if (repush) {
            this.trueNoop.push(tNoop);
            this.falseNoop.push(fNoop);
        }
        sootParamsTypes.addAll(getSootParamsTypes(newExpr));
        handleFinalLocalParams(sootParams, sootParamsTypes, objType);
        SootMethodRef methodToInvoke = getMethodFromClass(classToInvoke, "<init>", sootParamsTypes, VoidType.v(), false);
        SpecialInvokeExpr specialInvokeExpr = Jimple.v().newSpecialInvokeExpr(retLocal, methodToInvoke, sootParams);
        InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(specialInvokeExpr);
        this.body.getUnits().add((UnitPatchingChain) newInvokeStmt);
        Util.addLnPosTags(newInvokeStmt, newExpr.position());
        int numParams = 0;
        for (Expr expr : newExpr.arguments()) {
            Util.addLnPosTags(specialInvokeExpr.getArgBox(numParams), expr.position());
            numParams++;
        }
        return retLocal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public SootMethodRef getSootMethodRef(Call call) {
        Type sootRecType;
        SootClass receiverTypeClass;
        if (Util.getSootType(call.methodInstance().container()).equals(Scene.v().getObjectType())) {
            Scene.v().getObjectType();
            receiverTypeClass = Scene.v().getSootClass(Scene.v().getObjectType().toString());
        } else {
            if (call.target().type() == null) {
                sootRecType = Util.getSootType(call.methodInstance().container());
            } else {
                sootRecType = Util.getSootType(call.target().type());
            }
            if (sootRecType instanceof RefType) {
                receiverTypeClass = ((RefType) sootRecType).getSootClass();
            } else if (sootRecType instanceof soot.ArrayType) {
                receiverTypeClass = Scene.v().getSootClass(Scene.v().getObjectType().toString());
            } else {
                throw new RuntimeException("call target problem: " + call);
            }
        }
        MethodInstance methodInstance = call.methodInstance();
        Type sootRetType = Util.getSootType(methodInstance.returnType());
        ArrayList sootParamsTypes = getSootParamsTypes(call);
        SootMethodRef callMethod = Scene.v().makeMethodRef(receiverTypeClass, methodInstance.name(), sootParamsTypes, sootRetType, methodInstance.flags().isStatic());
        return callMethod;
    }

    private Local getCallLocal(Call call) {
        Type sootRecType;
        SootClass receiverTypeClass;
        InvokeExpr invokeExpr;
        SootClass addToClass;
        call.name();
        Receiver receiver = call.target();
        if ((receiver instanceof Special) && ((Special) receiver).kind() == Special.SUPER && ((Special) receiver).qualifier() != null) {
            return getSpecialSuperQualifierLocal(call);
        }
        Local baseLocal = (Local) base().getBaseLocal(receiver);
        boolean repush = false;
        Stmt tNoop = null;
        Stmt fNoop = null;
        if (!this.trueNoop.empty() && !this.falseNoop.empty()) {
            tNoop = this.trueNoop.pop();
            fNoop = this.falseNoop.pop();
            repush = true;
        }
        ArrayList sootParams = getSootParams(call);
        if (repush) {
            this.trueNoop.push(tNoop);
            this.falseNoop.push(fNoop);
        }
        SootMethodRef callMethod = base().getSootMethodRef(call);
        if (Util.getSootType(call.methodInstance().container()).equals(Scene.v().getObjectType())) {
            Scene.v().getObjectType();
            receiverTypeClass = Scene.v().getSootClass(Scene.v().getObjectType().toString());
        } else {
            if (call.target().type() == null) {
                sootRecType = Util.getSootType(call.methodInstance().container());
            } else {
                sootRecType = Util.getSootType(call.target().type());
            }
            if (sootRecType instanceof RefType) {
                receiverTypeClass = ((RefType) sootRecType).getSootClass();
            } else if (sootRecType instanceof soot.ArrayType) {
                receiverTypeClass = Scene.v().getSootClass(Scene.v().getObjectType().toString());
            } else {
                throw new RuntimeException("call target problem: " + call);
            }
        }
        MethodInstance methodInstance = call.methodInstance();
        boolean isPrivateAccess = false;
        if (needsAccessor(call)) {
            SootClass containingClass = ((RefType) Util.getSootType(call.methodInstance().container())).getSootClass();
            SootClass classToAddMethTo = containingClass;
            if (call.methodInstance().flags().isProtected()) {
                if (InitialResolver.v().hierarchy() == null) {
                    InitialResolver.v().hierarchy(new FastHierarchy());
                }
                if (this.body.getMethod().getDeclaringClass().hasOuterClass()) {
                    SootClass outerClass = this.body.getMethod().getDeclaringClass().getOuterClass();
                    while (true) {
                        addToClass = outerClass;
                        if (InitialResolver.v().hierarchy().canStoreType(containingClass.getType(), addToClass.getType()) || !addToClass.hasOuterClass()) {
                            break;
                        }
                        outerClass = addToClass.getOuterClass();
                    }
                } else {
                    addToClass = containingClass;
                }
                classToAddMethTo = addToClass;
            }
            callMethod = addGetMethodAccessMeth(classToAddMethTo, call).makeRef();
            if (!call.methodInstance().flags().isStatic()) {
                if (call.target() instanceof Expr) {
                    sootParams.add(0, baseLocal);
                } else if (this.body.getMethod().getDeclaringClass().declaresFieldByName("this$0")) {
                    sootParams.add(0, getThis(Util.getSootType(call.methodInstance().container())));
                } else {
                    sootParams.add(0, baseLocal);
                }
            }
            isPrivateAccess = true;
        }
        if (isPrivateAccess) {
            invokeExpr = Jimple.v().newStaticInvokeExpr(callMethod, sootParams);
        } else if (Modifier.isInterface(receiverTypeClass.getModifiers()) && methodInstance.flags().isAbstract()) {
            invokeExpr = Jimple.v().newInterfaceInvokeExpr(baseLocal, callMethod, sootParams);
        } else if (methodInstance.flags().isStatic()) {
            invokeExpr = Jimple.v().newStaticInvokeExpr(callMethod, sootParams);
        } else if (methodInstance.flags().isPrivate()) {
            invokeExpr = Jimple.v().newSpecialInvokeExpr(baseLocal, callMethod, sootParams);
        } else if ((receiver instanceof Special) && ((Special) receiver).kind() == Special.SUPER) {
            invokeExpr = Jimple.v().newSpecialInvokeExpr(baseLocal, callMethod, sootParams);
        } else {
            invokeExpr = Jimple.v().newVirtualInvokeExpr(baseLocal, callMethod, sootParams);
        }
        int numParams = 0;
        for (Expr expr : call.arguments()) {
            Util.addLnPosTags(invokeExpr.getArgBox(numParams), expr.position());
            numParams++;
        }
        if (invokeExpr instanceof InstanceInvokeExpr) {
            Util.addLnPosTags(((InstanceInvokeExpr) invokeExpr).getBaseBox(), call.target().position());
        }
        if (invokeExpr.getMethodRef().returnType().equals(VoidType.v())) {
            InvokeStmt newInvokeStmt = Jimple.v().newInvokeStmt(invokeExpr);
            this.body.getUnits().add((UnitPatchingChain) newInvokeStmt);
            Util.addLnPosTags(newInvokeStmt, call.position());
            return null;
        }
        Local retLocal = this.lg.generateLocal(invokeExpr.getMethodRef().returnType());
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(retLocal, invokeExpr);
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        Util.addLnPosTags(newAssignStmt, call.position());
        return retLocal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Value getBaseLocal(Receiver receiver) {
        if (receiver instanceof TypeNode) {
            return generateLocal(((TypeNode) receiver).type());
        }
        Value val = base().createAggressiveExpr((Expr) receiver, false, false);
        if (val instanceof Constant) {
            Local retLocal = this.lg.generateLocal(val.getType());
            AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, val);
            this.body.getUnits().add((UnitPatchingChain) stmt);
            return retLocal;
        }
        return val;
    }

    private Local getNewArrayLocal(NewArray newArrExpr) {
        NewArrayExpr newArrayExpr;
        Value dimLocal;
        Type sootType = Util.getSootType(newArrExpr.type());
        if (newArrExpr.numDims() == 1) {
            if (newArrExpr.additionalDims() == 1) {
                dimLocal = IntConstant.v(1);
            } else {
                dimLocal = base().createAggressiveExpr((Expr) newArrExpr.dims().get(0), false, false);
            }
            NewArrayExpr newArrayExpr2 = Jimple.v().newNewArrayExpr(((soot.ArrayType) sootType).getElementType(), dimLocal);
            newArrayExpr = newArrayExpr2;
            if (newArrExpr.additionalDims() != 1) {
                Util.addLnPosTags(newArrayExpr2.getSizeBox(), ((Expr) newArrExpr.dims().get(0)).position());
            }
        } else {
            ArrayList valuesList = new ArrayList();
            for (Expr expr : newArrExpr.dims()) {
                valuesList.add(base().createAggressiveExpr(expr, false, false));
            }
            if (newArrExpr.additionalDims() != 0) {
                valuesList.add(IntConstant.v(newArrExpr.additionalDims()));
            }
            NewMultiArrayExpr newMultiArrayExpr = Jimple.v().newNewMultiArrayExpr((soot.ArrayType) sootType, valuesList);
            newArrayExpr = newMultiArrayExpr;
            int counter = 0;
            for (Expr expr2 : newArrExpr.dims()) {
                Util.addLnPosTags(newMultiArrayExpr.getSizeBox(counter), expr2.position());
                counter++;
            }
        }
        Local retLocal = this.lg.generateLocal(sootType);
        AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, newArrayExpr);
        this.body.getUnits().add((UnitPatchingChain) stmt);
        Util.addLnPosTags(stmt, newArrExpr.position());
        Util.addLnPosTags(stmt.getRightOpBox(), newArrExpr.position());
        if (newArrExpr.init() != null) {
            Value initVal = getArrayInitLocal(newArrExpr.init(), newArrExpr.type());
            AssignStmt initStmt = Jimple.v().newAssignStmt(retLocal, initVal);
            this.body.getUnits().add((UnitPatchingChain) initStmt);
        }
        return retLocal;
    }

    private Local getArrayInitLocal(ArrayInit arrInit, polyglot.types.Type lhsType) {
        Value createAggressiveExpr;
        Local local = generateLocal(lhsType);
        NewArrayExpr arrExpr = Jimple.v().newNewArrayExpr(((soot.ArrayType) local.getType()).getElementType(), IntConstant.v(arrInit.elements().size()));
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(local, arrExpr);
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        Util.addLnPosTags(newAssignStmt, arrInit.position());
        int index = 0;
        for (Expr elemExpr : arrInit.elements()) {
            if (elemExpr instanceof ArrayInit) {
                if (((ArrayInit) elemExpr).type() instanceof NullType) {
                    if (lhsType instanceof ArrayType) {
                        createAggressiveExpr = getArrayInitLocal((ArrayInit) elemExpr, ((ArrayType) lhsType).base());
                    } else {
                        createAggressiveExpr = getArrayInitLocal((ArrayInit) elemExpr, lhsType);
                    }
                } else {
                    createAggressiveExpr = getArrayInitLocal((ArrayInit) elemExpr, ((ArrayType) lhsType).base());
                }
            } else {
                createAggressiveExpr = base().createAggressiveExpr(elemExpr, false, false);
            }
            Value elem = createAggressiveExpr;
            ArrayRef arrRef = Jimple.v().newArrayRef(local, IntConstant.v(index));
            AssignStmt elemAssign = Jimple.v().newAssignStmt(arrRef, elem);
            this.body.getUnits().add((UnitPatchingChain) elemAssign);
            Util.addLnPosTags(elemAssign, elemExpr.position());
            Util.addLnPosTags(elemAssign.getRightOpBox(), elemExpr.position());
            index++;
        }
        return local;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Value createLHS(Expr expr) {
        if (expr instanceof polyglot.ast.Local) {
            return getLocal((polyglot.ast.Local) expr);
        }
        if (expr instanceof ArrayAccess) {
            return getArrayRefLocalLeft((ArrayAccess) expr);
        }
        if (expr instanceof Field) {
            return getFieldLocalLeft((Field) expr);
        }
        throw new RuntimeException("Unhandled LHS");
    }

    private Value getArrayRefLocalLeft(ArrayAccess arrayRefExpr) {
        Expr array = arrayRefExpr.array();
        Expr access = arrayRefExpr.index();
        Local arrLocal = (Local) base().createAggressiveExpr(array, false, false);
        Value arrAccess = base().createAggressiveExpr(access, false, false);
        generateLocal(arrayRefExpr.type());
        ArrayRef ref = Jimple.v().newArrayRef(arrLocal, arrAccess);
        Util.addLnPosTags(ref.getBaseBox(), arrayRefExpr.array().position());
        Util.addLnPosTags(ref.getIndexBox(), arrayRefExpr.index().position());
        return ref;
    }

    private Value getArrayRefLocal(ArrayAccess arrayRefExpr) {
        Expr array = arrayRefExpr.array();
        Expr access = arrayRefExpr.index();
        Local arrLocal = (Local) base().createAggressiveExpr(array, false, false);
        Value arrAccess = base().createAggressiveExpr(access, false, false);
        Local retLocal = generateLocal(arrayRefExpr.type());
        ArrayRef ref = Jimple.v().newArrayRef(arrLocal, arrAccess);
        Util.addLnPosTags(ref.getBaseBox(), arrayRefExpr.array().position());
        Util.addLnPosTags(ref.getIndexBox(), arrayRefExpr.index().position());
        AssignStmt newAssignStmt = Jimple.v().newAssignStmt(retLocal, ref);
        this.body.getUnits().add((UnitPatchingChain) newAssignStmt);
        Util.addLnPosTags(newAssignStmt, arrayRefExpr.position());
        return retLocal;
    }

    private Local getSpecialSuperQualifierLocal(Expr expr) {
        SootClass classToInvoke;
        ArrayList methodParams = new ArrayList();
        if (expr instanceof Call) {
            Special target = (Special) ((Call) expr).target();
            classToInvoke = ((RefType) Util.getSootType(target.qualifier().type())).getSootClass();
            methodParams = getSootParams((Call) expr);
        } else if (expr instanceof Field) {
            Special target2 = (Special) ((Field) expr).target();
            classToInvoke = ((RefType) Util.getSootType(target2.qualifier().type())).getSootClass();
        } else {
            throw new RuntimeException("Trying to create special super qualifier for: " + expr + " which is not a field or call");
        }
        SootMethod methToInvoke = makeSuperAccessMethod(classToInvoke, expr);
        Local classToInvokeLocal = Util.getThis(classToInvoke.getType(), this.body, this.getThisMap, this.lg);
        methodParams.add(0, classToInvokeLocal);
        InvokeExpr invokeExpr = Jimple.v().newStaticInvokeExpr(methToInvoke.makeRef(), methodParams);
        if (!methToInvoke.getReturnType().equals(VoidType.v())) {
            Local retLocal = this.lg.generateLocal(methToInvoke.getReturnType());
            AssignStmt stmt = Jimple.v().newAssignStmt(retLocal, invokeExpr);
            this.body.getUnits().add((UnitPatchingChain) stmt);
            return retLocal;
        }
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newInvokeStmt(invokeExpr));
        return null;
    }

    private Local getSpecialLocal(Special specialExpr) {
        if (specialExpr.kind() == Special.SUPER) {
            if (specialExpr.qualifier() == null) {
                return this.specialThisLocal;
            }
            return getThis(Util.getSootType(specialExpr.qualifier().type()));
        } else if (specialExpr.kind() == Special.THIS) {
            if (specialExpr.qualifier() == null) {
                return this.specialThisLocal;
            }
            return getThis(Util.getSootType(specialExpr.qualifier().type()));
        } else {
            throw new RuntimeException("Unknown Special");
        }
    }

    private SootMethod makeSuperAccessMethod(SootClass classToInvoke, Object memberToAccess) {
        SootMethod meth;
        MethodSource src;
        String name = "access$" + InitialResolver.v().getNextPrivateAccessCounter() + TarConstants.VERSION_POSIX;
        ArrayList paramTypes = new ArrayList();
        paramTypes.add(classToInvoke.getType());
        if (memberToAccess instanceof Field) {
            Field fieldToAccess = (Field) memberToAccess;
            meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(fieldToAccess.type()), 8);
            MethodSource fSrc = new PrivateFieldAccMethodSource(Util.getSootType(fieldToAccess.type()), fieldToAccess.name(), fieldToAccess.flags().isStatic(), ((RefType) Util.getSootType(fieldToAccess.target().type())).getSootClass());
            src = fSrc;
        } else if (memberToAccess instanceof Call) {
            Call methToAccess = (Call) memberToAccess;
            paramTypes.addAll(getSootParamsTypes(methToAccess));
            meth = Scene.v().makeSootMethod(name, paramTypes, Util.getSootType(methToAccess.methodInstance().returnType()), 8);
            MethodSource mSrc = new PrivateMethodAccMethodSource(methToAccess.methodInstance());
            src = mSrc;
        } else {
            throw new RuntimeException("trying to access unhandled member type: " + memberToAccess);
        }
        classToInvoke.addMethod(meth);
        meth.setActiveBody(src.getBody(meth, null));
        meth.addTag(new SyntheticTag());
        return meth;
    }

    private Local getInstanceOfLocal(Instanceof instExpr) {
        Type sootType = Util.getSootType(instExpr.compareType().type());
        Value local = base().createAggressiveExpr(instExpr.expr(), false, false);
        InstanceOfExpr instOfExpr = Jimple.v().newInstanceOfExpr(local, sootType);
        Local lhs = this.lg.generateLocal(BooleanType.v());
        AssignStmt instAssign = Jimple.v().newAssignStmt(lhs, instOfExpr);
        this.body.getUnits().add((UnitPatchingChain) instAssign);
        Util.addLnPosTags(instAssign, instExpr.position());
        Util.addLnPosTags(instAssign.getRightOpBox(), instExpr.position());
        Util.addLnPosTags(instOfExpr.getOpBox(), instExpr.expr().position());
        return lhs;
    }

    private Local getConditionalLocal(Conditional condExpr) {
        NopStmt newNopStmt = Jimple.v().newNopStmt();
        Expr condition = condExpr.cond();
        createBranchingExpr(condition, newNopStmt, false);
        Local retLocal = generateLocal(condExpr.type());
        Expr consequence = condExpr.consequent();
        Value conseqVal = base().createAggressiveExpr(consequence, false, false);
        if (conseqVal instanceof ConditionExpr) {
            conseqVal = handleCondBinExpr((ConditionExpr) conseqVal);
        }
        AssignStmt conseqAssignStmt = Jimple.v().newAssignStmt(retLocal, conseqVal);
        this.body.getUnits().add((UnitPatchingChain) conseqAssignStmt);
        Util.addLnPosTags(conseqAssignStmt, condExpr.position());
        Util.addLnPosTags(conseqAssignStmt.getRightOpBox(), consequence.position());
        NopStmt newNopStmt2 = Jimple.v().newNopStmt();
        this.body.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(newNopStmt2));
        this.body.getUnits().add((UnitPatchingChain) newNopStmt);
        Expr alternative = condExpr.alternative();
        if (alternative != null) {
            Value altVal = base().createAggressiveExpr(alternative, false, false);
            if (altVal instanceof ConditionExpr) {
                altVal = handleCondBinExpr((ConditionExpr) altVal);
            }
            AssignStmt altAssignStmt = Jimple.v().newAssignStmt(retLocal, altVal);
            this.body.getUnits().add((UnitPatchingChain) altAssignStmt);
            Util.addLnPosTags(altAssignStmt, condExpr.position());
            Util.addLnPosTags(altAssignStmt, alternative.position());
            Util.addLnPosTags(altAssignStmt.getRightOpBox(), alternative.position());
        }
        this.body.getUnits().add((UnitPatchingChain) newNopStmt2);
        return retLocal;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    public Local generateLocal(polyglot.types.Type polyglotType) {
        Type type = Util.getSootType(polyglotType);
        return this.lg.generateLocal(type);
    }

    @Override // soot.javaToJimple.AbstractJimpleBodyBuilder
    protected Local generateLocal(Type sootType) {
        return this.lg.generateLocal(sootType);
    }
}
