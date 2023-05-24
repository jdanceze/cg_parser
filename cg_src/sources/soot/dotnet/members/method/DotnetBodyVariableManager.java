package soot.dotnet.members.method;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import soot.ArrayType;
import soot.Body;
import soot.Local;
import soot.LocalGenerator;
import soot.NullType;
import soot.PrimType;
import soot.RefType;
import soot.Type;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.UnknownType;
import soot.Value;
import soot.dotnet.members.DotnetMethod;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetBasicTypes;
import soot.dotnet.types.DotnetTypeFactory;
import soot.javaToJimple.DefaultLocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.internal.JAssignStmt;
import soot.jimple.internal.JimpleLocal;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/method/DotnetBodyVariableManager.class */
public class DotnetBodyVariableManager {
    private final DotnetBody dotnetBody;
    private final Body mainJb;
    public final LocalGenerator localGenerator;
    private final HashSet<String> localsToCast = new HashSet<>();

    public DotnetBodyVariableManager(DotnetBody dotnetBody, Body mainJb) {
        this.dotnetBody = dotnetBody;
        this.mainJb = mainJb;
        this.localGenerator = new DefaultLocalGenerator(mainJb);
    }

    public void fillMethodParameter() {
        DotnetMethod dotnetMethodSig = this.dotnetBody.getDotnetMethodSig();
        fillMethodParameter(this.mainJb, dotnetMethodSig.getParameterDefinitions());
    }

    public void fillMethodParameter(Body jb, List<ProtoAssemblyAllTypes.ParameterDefinition> parameters) {
        for (int i = 0; i < parameters.size(); i++) {
            ProtoAssemblyAllTypes.ParameterDefinition parameter = parameters.get(i);
            Local paramLocal = Jimple.v().newLocal(parameter.getParameterName(), DotnetTypeFactory.toSootType(parameter.getType()));
            jb.getLocals().add(paramLocal);
            jb.getUnits().add((UnitPatchingChain) Jimple.v().newIdentityStmt(paramLocal, Jimple.v().newParameterRef(DotnetTypeFactory.toSootType(parameter.getType()), i)));
        }
    }

    public void addInitLocalVariables(List<ProtoIlInstructions.IlVariableMsg> variableMsgList) {
        List<ProtoIlInstructions.IlVariableMsg> initLocalValueTypes = new ArrayList<>();
        for (ProtoIlInstructions.IlVariableMsg v : variableMsgList) {
            if (v.getVariableKind() == ProtoIlInstructions.IlVariableMsg.IlVariableKind.LOCAL) {
                addOrGetVariable(v, this.mainJb);
                if (v.getHasInitialValue()) {
                    initLocalValueTypes.add(v);
                }
                if (!v.getType().getFullname().equals(DotnetBasicTypes.SYSTEM_OBJECT)) {
                    initLocalValueTypes.add(v);
                }
            }
        }
        initLocalVariables(initLocalValueTypes);
    }

    private void initLocalVariables(List<ProtoIlInstructions.IlVariableMsg> locals) {
        for (ProtoIlInstructions.IlVariableMsg v : locals) {
            if (v.getVariableKind() == ProtoIlInstructions.IlVariableMsg.IlVariableKind.LOCAL) {
                Local variable = addOrGetVariable(v, this.mainJb);
                if (variable.getType() instanceof PrimType) {
                    AssignStmt assignStmt = Jimple.v().newAssignStmt(variable, DotnetTypeFactory.initType(variable));
                    this.mainJb.getUnits().add((UnitPatchingChain) assignStmt);
                } else if (variable.getType() instanceof ArrayType) {
                    AssignStmt assignStmt2 = Jimple.v().newAssignStmt(variable, NullConstant.v());
                    this.mainJb.getUnits().add((UnitPatchingChain) assignStmt2);
                } else {
                    AssignStmt assignStmt3 = Jimple.v().newAssignStmt(variable, Jimple.v().newNewExpr(RefType.v(v.getType().getFullname())));
                    this.mainJb.getUnits().add((UnitPatchingChain) assignStmt3);
                }
            }
        }
    }

    public Local addOrGetVariable(ProtoIlInstructions.IlVariableMsg v, Body jbTmp) {
        return addOrGetVariable(v, null, jbTmp);
    }

    public Local addOrGetVariable(ProtoIlInstructions.IlVariableMsg v, Type type, Body jbTmp) {
        Type sootType;
        if (v == null) {
            return null;
        }
        if (v.getName().equals("this")) {
            return this.mainJb.getThisLocal();
        }
        if (this.mainJb.getLocals().stream().anyMatch(x -> {
            return x.getName().equals(v.getName());
        })) {
            return this.mainJb.getLocals().stream().filter(x2 -> {
                return x2.getName().equals(v.getName());
            }).findFirst().orElse(null);
        }
        if (type == null || (type instanceof UnknownType) || (type instanceof NullType)) {
            sootType = DotnetTypeFactory.toSootType(v.getType());
        } else {
            sootType = DotnetTypeFactory.toSootType(type);
        }
        Type localType = sootType;
        Local newLocal = Jimple.v().newLocal(v.getName(), localType);
        this.mainJb.getLocals().add(newLocal);
        if (jbTmp != null && jbTmp != this.mainJb) {
            jbTmp.getLocals().add(newLocal);
        }
        return newLocal;
    }

    public void addLocalVariable(Local local) {
        if (this.mainJb.getLocals().contains(local)) {
            return;
        }
        this.mainJb.getLocals().add(local);
    }

    public static Value inlineLocals(Value v, Body jb) {
        Unit unit = (Unit) jb.getUnits().stream().filter(x -> {
            return (x instanceof JAssignStmt) && ((JAssignStmt) x).getLeftOp().equals(v);
        }).findFirst().orElse(null);
        if (unit instanceof AssignStmt) {
            if (((AssignStmt) unit).getRightOp() instanceof JimpleLocal) {
                return inlineLocals(((JAssignStmt) unit).getRightOp(), jb);
            }
            if (((AssignStmt) unit).getRightOp() instanceof CastExpr) {
                CastExpr ce = (CastExpr) ((AssignStmt) unit).getRightOp();
                return inlineLocals(ce.getOp(), jb);
            }
            return ((AssignStmt) unit).getRightOp();
        }
        return null;
    }

    public void addLocalsToCast(String local) {
        this.localsToCast.add(local);
    }

    public boolean localsToCastContains(String local) {
        return this.localsToCast.contains(local);
    }
}
