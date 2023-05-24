package soot.dotnet.instructions;

import java.util.ArrayList;
import java.util.List;
import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootClass;
import soot.SootMethodRef;
import soot.Type;
import soot.UnitPatchingChain;
import soot.Value;
import soot.coffi.Instruction;
import soot.dotnet.members.AbstractDotnetMember;
import soot.dotnet.members.DotnetMethod;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetBasicTypes;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.toolkits.scalar.Pair;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilCallVirtInstruction.class */
public class CilCallVirtInstruction extends AbstractCilnstruction {
    private SootClass clazz;
    private DotnetMethod method;
    private final List<Pair<Local, Local>> localsToCastForCall;

    public CilCallVirtInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
        this.localsToCastForCall = new ArrayList();
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        CilInstruction cilExpr = CilInstructionFactory.fromInstructionMsg(this.instruction, this.dotnetBody, this.cilBlock);
        Value value = cilExpr.jimplifyExpr(jb);
        InvokeStmt invokeStmt = Jimple.v().newInvokeStmt(value);
        if (cilExpr instanceof CilCallVirtInstruction) {
            List<Pair<Local, Local>> locals = ((CilCallVirtInstruction) cilExpr).getLocalsToCastForCall();
            if (locals.size() != 0) {
                for (Pair<Local, Local> pair : locals) {
                    CastExpr castExpr = Jimple.v().newCastExpr(pair.getO1(), pair.getO2().getType());
                    AssignStmt assignStmt = Jimple.v().newAssignStmt(pair.getO2(), castExpr);
                    jb.getUnits().add((UnitPatchingChain) assignStmt);
                }
            }
        }
        jb.getUnits().add((UnitPatchingChain) invokeStmt);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        this.clazz = Scene.v().getSootClass(this.instruction.getMethod().getDeclaringType().getFullname());
        this.method = new DotnetMethod(this.instruction.getMethod(), this.clazz);
        if (this.method.isStatic()) {
            checkMethodAvailable();
            List<Local> argsVariables = new ArrayList<>();
            List<Type> argsTypes = new ArrayList<>();
            Value rewriteField = AbstractDotnetMember.checkRewriteCilSpecificMember(this.clazz, this.method.getName());
            if (rewriteField != null) {
                return rewriteField;
            }
            for (int z = 0; z < this.instruction.getArgumentsCount(); z++) {
                Value variableValue = CilInstructionFactory.fromInstructionMsg(this.instruction.getArguments(z), this.dotnetBody, this.cilBlock).jimplifyExpr(jb);
                checkVariabelIsLocal(variableValue, z, false);
                Local variable = (Local) variableValue;
                argsVariables.add(variable);
            }
            for (ProtoAssemblyAllTypes.ParameterDefinition parameterDefinition : this.method.getParameterDefinitions()) {
                argsTypes.add(DotnetTypeFactory.toSootType(parameterDefinition.getType()));
            }
            String methodName = this.method.getUniqueName();
            SootMethodRef methodRef = Scene.v().makeMethodRef(this.clazz, DotnetMethod.convertCtorName(methodName), argsTypes, DotnetTypeFactory.toSootType(this.method.getReturnType()), true);
            return Jimple.v().newStaticInvokeExpr(methodRef, argsVariables);
        } else if (this.clazz.isInterface()) {
            MethodParams methodParams = getMethodCallParams(jb);
            return Jimple.v().newInterfaceInvokeExpr(methodParams.Base, methodParams.MethodRef, methodParams.ArgumentVariables);
        } else if (this.instruction.getMethod().getIsConstructor() || this.instruction.getMethod().getAccessibility().equals(ProtoAssemblyAllTypes.Accessibility.PRIVATE)) {
            MethodParams methodParams2 = getMethodCallParams(jb);
            return Jimple.v().newSpecialInvokeExpr(methodParams2.Base, methodParams2.MethodRef, methodParams2.ArgumentVariables);
        } else {
            MethodParams methodParams3 = getMethodCallParams(jb);
            return Jimple.v().newVirtualInvokeExpr(methodParams3.Base, methodParams3.MethodRef, methodParams3.ArgumentVariables);
        }
    }

    public List<Pair<Local, Local>> getLocalsToCastForCall() {
        return this.localsToCastForCall;
    }

    private MethodParams getMethodCallParams(Body jb) {
        Type return_type;
        checkMethodAvailable();
        List<Local> argsVariables = new ArrayList<>();
        List<Type> methodParamTypes = new ArrayList<>();
        if (this.instruction.getArgumentsCount() == 0) {
            throw new RuntimeException("Opcode: " + this.instruction.getOpCode() + ": Given method " + this.method.getName() + " of declared type " + this.method.getDeclaringClass().getName() + " has no arguments! This means there is no base variable for the virtual invoke!");
        }
        Value baseValue = CilInstructionFactory.fromInstructionMsg(this.instruction.getArguments(0), this.dotnetBody, this.cilBlock).jimplifyExpr(jb);
        checkVariabelIsLocal(baseValue, 0, true);
        Local base = (Local) baseValue;
        if (this.instruction.getArgumentsCount() > 1) {
            for (int z = 1; z < this.instruction.getArgumentsCount(); z++) {
                Value variableValue = CilInstructionFactory.fromInstructionMsg(this.instruction.getArguments(z), this.dotnetBody, this.cilBlock).jimplifyExpr(jb);
                checkVariabelIsLocal(variableValue, z, false);
                Local variable = (Local) variableValue;
                argsVariables.add(variable);
            }
            for (ProtoAssemblyAllTypes.ParameterDefinition parameterDefinition : this.instruction.getMethod().getParameterList()) {
                methodParamTypes.add(DotnetTypeFactory.toSootType(parameterDefinition.getType()));
            }
        }
        for (int i = 0; i < argsVariables.size(); i++) {
            Local arg = argsVariables.get(i);
            Type methodParam = methodParamTypes.get(i);
            if (arg.getType().toString().equals(DotnetBasicTypes.SYSTEM_OBJECT) && !methodParam.toString().equals(DotnetBasicTypes.SYSTEM_OBJECT)) {
                Local castLocal = this.dotnetBody.variableManager.localGenerator.generateLocal(methodParam);
                this.localsToCastForCall.add(new Pair<>(arg, castLocal));
                argsVariables.set(i, castLocal);
            }
        }
        String methodName = this.method.getUniqueName();
        if (this.method.getReturnType().getTypeKind().equals(ProtoAssemblyAllTypes.TypeKindDef.POINTER) && this.method.getReturnType().getFullname().equals(DotnetBasicTypes.SYSTEM_VOID)) {
            return_type = DotnetTypeFactory.toSootType(this.method.getProtoMessage().getDeclaringType());
        } else {
            return_type = DotnetTypeFactory.toSootType(this.method.getProtoMessage().getReturnType());
        }
        SootMethodRef methodRef = Scene.v().makeMethodRef(this.clazz, DotnetMethod.convertCtorName(methodName), methodParamTypes, return_type, false);
        return new MethodParams(base, methodRef, argsVariables);
    }

    private void checkMethodAvailable() {
        if (this.method.getName().trim().isEmpty()) {
            throw new RuntimeException("Opcode: " + this.instruction.getOpCode() + ": Given method " + this.method.getName() + " of declared type " + this.method.getDeclaringClass().getName() + " has no method name!");
        }
    }

    private void checkVariabelIsLocal(Value var, int argPos, boolean isBase) {
        String err = String.valueOf(String.valueOf("CALL: The given argument ") + argPos) + Instruction.argsep;
        if (isBase) {
            err = String.valueOf(err) + "(base variable)";
        }
        String err2 = String.valueOf(err) + " of invoked method " + this.method.getName() + " declared in " + this.clazz.getName() + " is not a local! The value is: " + var.toString() + " of type " + var.getType() + "! The resolving method body is: " + this.dotnetBody.getDotnetMethodSig().getSootMethodSignature().getSignature();
        if (!(var instanceof Local)) {
            throw new RuntimeException(err2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilCallVirtInstruction$MethodParams.class */
    public static class MethodParams {
        public Local Base;
        public SootMethodRef MethodRef;
        public List<Local> ArgumentVariables;

        public MethodParams(Local base, SootMethodRef methodRef, List<Local> argumentVariables) {
            this.Base = base;
            this.MethodRef = methodRef;
            this.ArgumentVariables = argumentVariables;
        }
    }
}
