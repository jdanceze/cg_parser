package soot.dotnet.instructions;

import java.util.ArrayList;
import java.util.List;
import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootFieldRef;
import soot.SootMethodRef;
import soot.SootResolver;
import soot.Type;
import soot.Value;
import soot.dotnet.exceptions.NoStatementInstructionException;
import soot.dotnet.members.DotnetMethod;
import soot.dotnet.members.method.DotnetBody;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.dotnet.proto.ProtoIlInstructions;
import soot.dotnet.types.DotnetTypeFactory;
import soot.jimple.MethodHandle;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/dotnet/instructions/CilLdMemberTokenInstruction.class */
public class CilLdMemberTokenInstruction extends AbstractCilnstruction {
    public CilLdMemberTokenInstruction(ProtoIlInstructions.IlInstructionMsg instruction, DotnetBody dotnetBody, CilBlock cilBlock) {
        super(instruction, dotnetBody, cilBlock);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public void jimplify(Body jb) {
        throw new NoStatementInstructionException(this.instruction);
    }

    @Override // soot.dotnet.instructions.CilInstruction
    public Value jimplifyExpr(Body jb) {
        int kind;
        if (this.instruction.hasField()) {
            ProtoAssemblyAllTypes.FieldDefinition field = this.instruction.getField();
            SootFieldRef sootFieldRef = Scene.v().makeFieldRef(SootResolver.v().makeClassRef(field.getDeclaringType().getFullname()), field.getName(), DotnetTypeFactory.toSootType(field.getType()), field.getIsStatic());
            int kind2 = field.getIsStatic() ? MethodHandle.Kind.REF_GET_FIELD_STATIC.getValue() : MethodHandle.Kind.REF_GET_FIELD.getValue();
            return MethodHandle.v(sootFieldRef, kind2);
        } else if (this.instruction.hasMethod()) {
            DotnetMethod method = new DotnetMethod(this.instruction.getMethod());
            SootClass declaringClass = method.getDeclaringClass();
            if (method.getName().trim().isEmpty()) {
                throw new RuntimeException("Opcode: " + this.instruction.getOpCode() + ": Given method " + method.getName() + " of declared type " + method.getDeclaringClass().getName() + " has no method name!");
            }
            String methodName = method.getUniqueName();
            List<Type> paramTypes = new ArrayList<>();
            for (ProtoAssemblyAllTypes.ParameterDefinition parameterDefinition : method.getParameterDefinitions()) {
                paramTypes.add(DotnetTypeFactory.toSootType(parameterDefinition.getType()));
            }
            SootMethodRef methodRef = Scene.v().makeMethodRef(declaringClass, DotnetMethod.convertCtorName(methodName), paramTypes, DotnetTypeFactory.toSootType(method.getReturnType()), method.isStatic());
            if (method.isConstructor()) {
                kind = MethodHandle.Kind.REF_INVOKE_CONSTRUCTOR.getValue();
            } else if (method.isStatic()) {
                kind = MethodHandle.Kind.REF_INVOKE_STATIC.getValue();
            } else if (declaringClass.isInterface()) {
                kind = MethodHandle.Kind.REF_INVOKE_INTERFACE.getValue();
            } else {
                kind = MethodHandle.Kind.REF_INVOKE_VIRTUAL.getValue();
            }
            return MethodHandle.v(methodRef, kind);
        } else {
            return StringConstant.v(this.instruction.getValueConstantString());
        }
    }
}
