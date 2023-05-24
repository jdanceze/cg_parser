package soot.dotnet.members;

import soot.SootClass;
import soot.Value;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/dotnet/members/AbstractDotnetMember.class */
public abstract class AbstractDotnetMember implements DotnetTypeMember {
    public static Value checkRewriteCilSpecificMember(SootClass declaringClass, String fieldMethodName) {
        if (declaringClass.getName().equals(DotnetBasicTypes.SYSTEM_STRING) && fieldMethodName.equals("Empty")) {
            return StringConstant.v("");
        }
        if (declaringClass.getName().equals(DotnetBasicTypes.SYSTEM_ARRAY) && fieldMethodName.equals("Empty")) {
            return NullConstant.v();
        }
        return null;
    }
}
