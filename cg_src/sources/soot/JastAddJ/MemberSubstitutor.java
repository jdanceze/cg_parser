package soot.JastAddJ;

import java.util.Collection;
import java.util.HashMap;
/* loaded from: gencallgraphv3.jar:soot/JastAddJ/MemberSubstitutor.class */
public interface MemberSubstitutor extends Parameterization {
    TypeDecl original();

    void addBodyDecl(BodyDecl bodyDecl);

    @Override // soot.JastAddJ.Parameterization
    TypeDecl substitute(TypeVariable typeVariable);

    HashMap localMethodsSignatureMap();

    SimpleSet localFields(String str);

    SimpleSet localTypeDecls(String str);

    Collection constructors();
}
