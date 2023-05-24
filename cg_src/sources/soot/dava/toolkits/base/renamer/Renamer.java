package soot.dava.toolkits.base.renamer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import soot.ArrayType;
import soot.Local;
import soot.RefLikeType;
import soot.SootClass;
import soot.SootField;
import soot.Type;
import soot.dava.internal.AST.ASTMethodNode;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/renamer/Renamer.class */
public class Renamer {
    heuristicSet heuristics;
    Chain fields;
    ASTMethodNode methodNode;
    List<String> forLoopNames;
    public final boolean DEBUG = false;
    List locals = null;
    HashMap<Local, Boolean> changedOrNot = new HashMap<>();

    public Renamer(heuristicSet info, ASTMethodNode node) {
        this.heuristics = info;
        this.methodNode = node;
        Iterator<Local> localIt = info.getLocalsIterator();
        while (localIt.hasNext()) {
            this.changedOrNot.put(localIt.next(), new Boolean(false));
        }
        this.forLoopNames = new ArrayList();
        this.forLoopNames.add("i");
        this.forLoopNames.add("j");
        this.forLoopNames.add("k");
        this.forLoopNames.add("l");
    }

    public void rename() {
        debug("rename", "Renaming started");
        mainMethodArgument();
        forLoopIndexing();
        exceptionNaming();
        arraysGetTypeArray();
        assignedFromAField();
        newClassName();
        castedObject();
        objectsGetClassName();
        removeDollarSigns();
    }

    private void arraysGetTypeArray() {
        String newName;
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            if (!alreadyChanged(tempLocal)) {
                debug("arraysGetTypeArray", "checking " + tempLocal);
                Type type = tempLocal.getType();
                if (type instanceof ArrayType) {
                    debug("arraysGetTypeArray", "Local:" + tempLocal + " is an Array Type: " + type.toString());
                    String tempClassName = type.toString();
                    if (tempClassName.indexOf(91) >= 0) {
                        tempClassName = tempClassName.substring(0, tempClassName.indexOf(91));
                    }
                    if (tempClassName.indexOf(46) != -1) {
                        tempClassName = tempClassName.substring(tempClassName.lastIndexOf(46) + 1);
                    }
                    String newName2 = tempClassName.toLowerCase();
                    int count = 0;
                    String str = String.valueOf(String.valueOf(newName2) + "Array") + 0;
                    while (true) {
                        newName = str;
                        count++;
                        if (isUniqueName(newName)) {
                            break;
                        }
                        str = String.valueOf(newName.substring(0, newName.length() - 1)) + count;
                    }
                    setName(tempLocal, newName);
                }
            }
        }
    }

    private void objectsGetClassName() {
        String newName;
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            if (!alreadyChanged(tempLocal)) {
                debug("objectsGetClassName", "checking " + tempLocal);
                Type type = tempLocal.getType();
                if (!(type instanceof ArrayType) && (type instanceof RefLikeType)) {
                    debug("objectsGetClassName", "Local:" + tempLocal + " Type: " + type.toString());
                    String tempClassName = type.toString();
                    if (tempClassName.indexOf(46) != -1) {
                        tempClassName = tempClassName.substring(tempClassName.lastIndexOf(46) + 1);
                    }
                    int count = 0;
                    String str = String.valueOf(tempClassName.toLowerCase()) + 0;
                    while (true) {
                        newName = str;
                        count++;
                        if (isUniqueName(newName)) {
                            break;
                        }
                        str = String.valueOf(newName.substring(0, newName.length() - 1)) + count;
                    }
                    setName(tempLocal, newName);
                }
            }
        }
    }

    private void castedObject() {
        String newName;
        debug("castedObject", "");
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            if (!alreadyChanged(tempLocal)) {
                debug("castedObject", "checking " + tempLocal);
                List<String> classes = this.heuristics.getCastStrings(tempLocal);
                Iterator<String> itClass = classes.iterator();
                String classNameToUse = null;
                while (true) {
                    if (!itClass.hasNext()) {
                        break;
                    }
                    String tempClassName = itClass.next();
                    if (tempClassName.indexOf(46) != -1) {
                        tempClassName = tempClassName.substring(tempClassName.lastIndexOf(46) + 1);
                    }
                    if (classNameToUse == null) {
                        classNameToUse = tempClassName;
                    } else if (!classNameToUse.equals(tempClassName)) {
                        classNameToUse = null;
                        break;
                    }
                }
                if (classNameToUse != null) {
                    debug("castedObject", "found a classNametoUse through cast expr");
                    int count = 0;
                    String str = String.valueOf(classNameToUse.toLowerCase()) + 0;
                    while (true) {
                        newName = str;
                        count++;
                        if (isUniqueName(newName)) {
                            break;
                        }
                        str = String.valueOf(newName.substring(0, newName.length() - 1)) + count;
                    }
                    setName(tempLocal, newName);
                }
            }
        }
    }

    private void newClassName() {
        String newName;
        debug("newClassName", "");
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            if (!alreadyChanged(tempLocal)) {
                debug("newClassName", "checking " + tempLocal);
                List<String> classes = this.heuristics.getObjectClassName(tempLocal);
                Iterator<String> itClass = classes.iterator();
                String classNameToUse = null;
                while (true) {
                    if (!itClass.hasNext()) {
                        break;
                    }
                    String tempClassName = itClass.next();
                    if (tempClassName.indexOf(46) != -1) {
                        tempClassName = tempClassName.substring(tempClassName.lastIndexOf(46) + 1);
                    }
                    if (classNameToUse == null) {
                        classNameToUse = tempClassName;
                    } else if (!classNameToUse.equals(tempClassName)) {
                        classNameToUse = null;
                        break;
                    }
                }
                if (classNameToUse != null) {
                    debug("newClassName", "found a classNametoUse");
                    int count = 0;
                    String str = String.valueOf(classNameToUse.toLowerCase()) + 0;
                    while (true) {
                        newName = str;
                        count++;
                        if (isUniqueName(newName)) {
                            break;
                        }
                        str = String.valueOf(newName.substring(0, newName.length() - 1)) + count;
                    }
                    setName(tempLocal, newName);
                }
            }
        }
    }

    private void assignedFromAField() {
        String str;
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            if (!alreadyChanged(tempLocal)) {
                debug("assignedFromField", "checking " + tempLocal);
                List<String> fieldNames = this.heuristics.getFieldName(tempLocal);
                if (fieldNames.size() <= 1 && fieldNames.size() == 1) {
                    String fieldName = fieldNames.get(0);
                    int count = 0;
                    while (!isUniqueName(fieldName)) {
                        if (count == 0) {
                            str = String.valueOf(fieldName) + count;
                        } else {
                            str = String.valueOf(fieldName.substring(0, fieldName.length() - 1)) + count;
                        }
                        fieldName = str;
                        count++;
                    }
                    setName(tempLocal, fieldName);
                }
            }
        }
    }

    private void removeDollarSigns() {
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            String currentName = tempLocal.getName();
            int dollarIndex = currentName.indexOf(36);
            if (dollarIndex == 0) {
                String newName = currentName.substring(1, currentName.length());
                if (isUniqueName(newName)) {
                    setName(tempLocal, newName);
                }
            }
        }
    }

    private void exceptionNaming() {
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            Type localType = tempLocal.getType();
            String typeString = localType.toString();
            if (typeString.indexOf("Exception") >= 0) {
                debug("exceptionNaming", "Type is an exception" + tempLocal);
                String newName = "";
                for (int i = 0; i < typeString.length(); i++) {
                    char character = typeString.charAt(i);
                    if (Character.isUpperCase(character)) {
                        newName = String.valueOf(newName) + Character.toLowerCase(character);
                    }
                }
                int count = 0;
                if (!isUniqueName(newName)) {
                    while (true) {
                        count++;
                        if (isUniqueName(String.valueOf(newName) + count)) {
                            break;
                        }
                    }
                }
                if (count != 0) {
                    newName = String.valueOf(newName) + count;
                }
                setName(tempLocal, newName);
            }
        }
    }

    private void forLoopIndexing() {
        String newName;
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            debug("foeLoopIndexing", "Checking local" + tempLocal.getName());
            if (this.heuristics.getHeuristic(tempLocal, 9)) {
                int count = -1;
                while (true) {
                    count++;
                    if (count >= this.forLoopNames.size()) {
                        newName = null;
                        break;
                    }
                    newName = this.forLoopNames.get(count);
                    if (isUniqueName(newName)) {
                        break;
                    }
                }
                if (newName != null) {
                    setName(tempLocal, newName);
                }
            }
        }
    }

    private void mainMethodArgument() {
        String str;
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        while (it.hasNext()) {
            Local tempLocal = it.next();
            if (this.heuristics.getHeuristic(tempLocal, 7)) {
                String newName = "args";
                int count = 0;
                while (!isUniqueName(newName)) {
                    if (count == 0) {
                        str = String.valueOf(newName) + count;
                    } else {
                        str = String.valueOf(newName.substring(0, newName.length() - 1)) + count;
                    }
                    newName = str;
                    count++;
                }
                setName(tempLocal, newName);
                return;
            }
        }
    }

    private void setName(Local var, String newName) {
        Object truthValue = this.changedOrNot.get(var);
        if (truthValue == null) {
            this.changedOrNot.put(var, new Boolean(false));
        } else if (((Boolean) truthValue).booleanValue()) {
            debug("setName", "Var: " + var + " had already been renamed");
            return;
        }
        debug("setName", "Changed " + var.getName() + " to " + newName);
        var.setName(newName);
        this.changedOrNot.put(var, new Boolean(true));
    }

    private boolean alreadyChanged(Local var) {
        Object truthValue = this.changedOrNot.get(var);
        if (truthValue == null) {
            this.changedOrNot.put(var, new Boolean(false));
            return false;
        } else if (((Boolean) truthValue).booleanValue()) {
            debug("alreadyChanged", "Var: " + var + " had already been renamed");
            return true;
        } else {
            return false;
        }
    }

    private boolean isUniqueName(String name) {
        Iterator it = getScopedLocals();
        while (it.hasNext()) {
            Local tempLocal = (Local) it.next();
            if (tempLocal.getName().equals(name)) {
                debug("isUniqueName", "New Name " + name + " is not unique (matches some local)..changing");
                return false;
            }
            debug("isUniqueName", "New Name " + name + " is different from local " + tempLocal.getName());
        }
        Iterator it2 = getScopedFields();
        while (it2.hasNext()) {
            SootField tempField = (SootField) it2.next();
            if (tempField.getName().equals(name)) {
                debug("isUniqueName", "New Name " + name + " is not unique (matches field)..changing");
                return false;
            }
            debug("isUniqueName", "New Name " + name + " is different from field " + tempField.getName());
        }
        return true;
    }

    private Iterator getScopedFields() {
        SootClass sootClass = this.methodNode.getDavaBody().getMethod().getDeclaringClass();
        this.fields = sootClass.getFields();
        return this.fields.iterator();
    }

    private Iterator getScopedLocals() {
        Iterator<Local> it = this.heuristics.getLocalsIterator();
        this.locals = new ArrayList();
        while (it.hasNext()) {
            this.locals.add(it.next());
        }
        return this.locals.iterator();
    }

    public void debug(String methodName, String debug) {
    }
}
