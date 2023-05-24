package soot.dava.internal.javaRep;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import soot.AbstractUnit;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.coffi.Instruction;
import soot.dava.DavaBody;
import soot.dava.DavaUnitPrinter;
import soot.dava.toolkits.base.renamer.RemoveFullyQualifiedName;
import soot.grimp.Grimp;
import soot.jimple.ArrayRef;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.util.IterableSet;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DVariableDeclarationStmt.class */
public class DVariableDeclarationStmt extends AbstractUnit implements Stmt {
    Type declarationType;
    List declarations;
    DavaBody davaBody;

    public DVariableDeclarationStmt(Type decType, DavaBody davaBody) {
        this.declarationType = null;
        this.declarations = null;
        this.davaBody = null;
        if (this.declarationType != null) {
            throw new RuntimeException("creating a VariableDeclaration which has already been created");
        }
        this.declarationType = decType;
        this.declarations = new ArrayList();
        this.davaBody = davaBody;
    }

    public List getDeclarations() {
        return this.declarations;
    }

    public void addLocal(Local add) {
        this.declarations.add(add);
    }

    public void removeLocal(Local remove) {
        for (int i = 0; i < this.declarations.size(); i++) {
            Local temp = (Local) this.declarations.get(i);
            if (temp.getName().compareTo(remove.getName()) == 0) {
                this.declarations.remove(i);
                return;
            }
        }
    }

    public Type getType() {
        return this.declarationType;
    }

    public boolean isOfType(Type type) {
        if (type.toString().compareTo(this.declarationType.toString()) == 0) {
            return true;
        }
        return false;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public Object clone() {
        DVariableDeclarationStmt temp = new DVariableDeclarationStmt(this.declarationType, this.davaBody);
        for (Local obj : this.declarations) {
            Value temp1 = Grimp.cloneIfNecessary(obj);
            if (temp1 instanceof Local) {
                temp.addLocal((Local) temp1);
            }
        }
        return temp;
    }

    public String toString() {
        StringBuffer b = new StringBuffer();
        if (this.declarations.size() == 0) {
            return b.toString();
        }
        String type = this.declarationType.toString();
        if (type.equals(Jimple.NULL_TYPE)) {
            b.append("Object");
        } else {
            b.append(type);
        }
        b.append(Instruction.argsep);
        Iterator decIt = this.declarations.iterator();
        while (decIt.hasNext()) {
            Local tempDec = (Local) decIt.next();
            b.append(tempDec.getName());
            if (decIt.hasNext()) {
                b.append(", ");
            }
        }
        return b.toString();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        if (this.declarations.size() == 0) {
            return;
        }
        if (!(up instanceof DavaUnitPrinter)) {
            throw new RuntimeException("DavaBody should always be printed using the DavaUnitPrinter");
        }
        DavaUnitPrinter dup = (DavaUnitPrinter) up;
        String type = this.declarationType.toString();
        if (type.equals(Jimple.NULL_TYPE)) {
            dup.printString("Object");
        } else {
            IterableSet importSet = this.davaBody.getImportList();
            if (!importSet.contains(type)) {
                this.davaBody.addToImportList(type);
            }
            dup.printString(RemoveFullyQualifiedName.getReducedName(this.davaBody.getImportList(), type, this.declarationType));
        }
        dup.printString(Instruction.argsep);
        Iterator decIt = this.declarations.iterator();
        while (decIt.hasNext()) {
            Local tempDec = (Local) decIt.next();
            dup.printString(tempDec.getName());
            if (decIt.hasNext()) {
                dup.printString(", ");
            }
        }
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        return true;
    }

    @Override // soot.Unit
    public boolean branches() {
        return false;
    }

    @Override // soot.jimple.Stmt
    public boolean containsInvokeExpr() {
        return false;
    }

    @Override // soot.jimple.Stmt
    public InvokeExpr getInvokeExpr() {
        throw new RuntimeException("getInvokeExpr() called with no invokeExpr present!");
    }

    @Override // soot.jimple.Stmt
    public ValueBox getInvokeExprBox() {
        throw new RuntimeException("getInvokeExprBox() called with no invokeExpr present!");
    }

    @Override // soot.jimple.Stmt
    public boolean containsArrayRef() {
        return false;
    }

    @Override // soot.jimple.Stmt
    public ArrayRef getArrayRef() {
        throw new RuntimeException("getArrayRef() called with no ArrayRef present!");
    }

    @Override // soot.jimple.Stmt
    public ValueBox getArrayRefBox() {
        throw new RuntimeException("getArrayRefBox() called with no ArrayRef present!");
    }

    @Override // soot.jimple.Stmt
    public boolean containsFieldRef() {
        return false;
    }

    @Override // soot.jimple.Stmt
    public FieldRef getFieldRef() {
        throw new RuntimeException("getFieldRef() called with no FieldRef present!");
    }

    @Override // soot.jimple.Stmt
    public ValueBox getFieldRefBox() {
        throw new RuntimeException("getFieldRefBox() called with no FieldRef present!");
    }
}
