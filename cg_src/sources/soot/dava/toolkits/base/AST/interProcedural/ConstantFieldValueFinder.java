package soot.dava.toolkits.base.AST.interProcedural;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.PrimType;
import soot.ShortType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.dava.DavaBody;
import soot.dava.DecompilationException;
import soot.dava.internal.AST.ASTNode;
import soot.dava.toolkits.base.AST.traversals.AllDefinitionsFinder;
import soot.jimple.DefinitionStmt;
import soot.jimple.DoubleConstant;
import soot.jimple.FieldRef;
import soot.jimple.FloatConstant;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NumericConstant;
import soot.tagkit.DoubleConstantValueTag;
import soot.tagkit.FloatConstantValueTag;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.LongConstantValueTag;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/dava/toolkits/base/AST/interProcedural/ConstantFieldValueFinder.class */
public class ConstantFieldValueFinder {
    public static String combiner = "_$p$g_";
    private final Chain<SootClass> appClasses;
    public final boolean DEBUG = false;
    private final HashMap<String, SootField> classNameFieldNameToSootFieldMapping = new HashMap<>();
    private final HashMap<String, ArrayList<Value>> fieldToValues = new HashMap<>();
    private final HashMap<String, Object> primTypeFieldValueToUse = new HashMap<>();

    public ConstantFieldValueFinder(Chain<SootClass> classes) {
        this.appClasses = classes;
        debug("ConstantFieldValueFinder -- applyAnalyses", "computing Method Summaries");
        computeFieldToValuesAssignedList();
        valuesForPrimTypeFields();
    }

    public HashMap<String, Object> getFieldsWithConstantValues() {
        return this.primTypeFieldValueToUse;
    }

    public HashMap<String, SootField> getClassNameFieldNameToSootFieldMapping() {
        return this.classNameFieldNameToSootFieldMapping;
    }

    private void valuesForPrimTypeFields() {
        IntegerConstantValueTag t;
        int value;
        for (SootClass s : this.appClasses) {
            debug("\nvaluesforPrimTypeFields", "Processing class " + s.getName());
            String declaringClass = s.getName();
            for (SootField f : s.getFields()) {
                Type fieldType = f.getType();
                if (fieldType instanceof PrimType) {
                    String combined = String.valueOf(declaringClass) + combiner + f.getName();
                    this.classNameFieldNameToSootFieldMapping.put(combined, f);
                    Object value2 = null;
                    if (fieldType instanceof DoubleType) {
                        DoubleConstantValueTag t2 = (DoubleConstantValueTag) f.getTag(DoubleConstantValueTag.NAME);
                        if (t2 != null) {
                            value2 = Double.valueOf(t2.getDoubleValue());
                        }
                    } else if (fieldType instanceof FloatType) {
                        FloatConstantValueTag t3 = (FloatConstantValueTag) f.getTag(FloatConstantValueTag.NAME);
                        if (t3 != null) {
                            value2 = Float.valueOf(t3.getFloatValue());
                        }
                    } else if (fieldType instanceof LongType) {
                        LongConstantValueTag t4 = (LongConstantValueTag) f.getTag(LongConstantValueTag.NAME);
                        if (t4 != null) {
                            value2 = Long.valueOf(t4.getLongValue());
                        }
                    } else if (fieldType instanceof CharType) {
                        IntegerConstantValueTag t5 = (IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME);
                        if (t5 != null) {
                            value2 = Integer.valueOf(t5.getIntValue());
                        }
                    } else if (fieldType instanceof BooleanType) {
                        IntegerConstantValueTag t6 = (IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME);
                        if (t6 != null) {
                            value2 = Boolean.valueOf(t6.getIntValue() != 0);
                        }
                    } else if (((fieldType instanceof IntType) || (fieldType instanceof ByteType) || (fieldType instanceof ShortType)) && (t = (IntegerConstantValueTag) f.getTag(IntegerConstantValueTag.NAME)) != null) {
                        value2 = Integer.valueOf(t.getIntValue());
                    }
                    if (value2 != null) {
                        debug("TAGGED value found for field: " + combined);
                        this.primTypeFieldValueToUse.put(combined, value2);
                    } else {
                        ArrayList<Value> values = this.fieldToValues.get(combined);
                        if (values == null) {
                            if (fieldType instanceof DoubleType) {
                                value = Double.valueOf((double) Const.default_value_double);
                            } else if (fieldType instanceof FloatType) {
                                value = Float.valueOf(0.0f);
                            } else if (fieldType instanceof LongType) {
                                value = 0L;
                            } else if (fieldType instanceof BooleanType) {
                                value = false;
                            } else if ((fieldType instanceof IntType) || (fieldType instanceof ByteType) || (fieldType instanceof ShortType) || (fieldType instanceof CharType)) {
                                value = 0;
                            } else {
                                throw new DecompilationException("Unknown primitive type...please report to developer");
                            }
                            this.primTypeFieldValueToUse.put(combined, value);
                            debug("DEFAULT value for field: " + combined);
                        } else {
                            debug("CHECKING USER ASSIGNED VALUES FOR: " + combined);
                            NumericConstant tempConstant = null;
                            Iterator<Value> it = values.iterator();
                            while (true) {
                                if (!it.hasNext()) {
                                    break;
                                }
                                Value val = it.next();
                                if (!(val instanceof NumericConstant)) {
                                    tempConstant = null;
                                    debug("Not numeric constant hence giving up");
                                    break;
                                } else if (tempConstant == null) {
                                    tempConstant = (NumericConstant) val;
                                } else if (!tempConstant.equals(val)) {
                                    tempConstant = null;
                                    break;
                                }
                            }
                            if (tempConstant == null) {
                                continue;
                            } else if (tempConstant instanceof LongConstant) {
                                long tempVal = ((LongConstant) tempConstant).value;
                                if (Long.compare(tempVal, 0L) == 0) {
                                    this.primTypeFieldValueToUse.put(combined, Long.valueOf(tempVal));
                                } else {
                                    debug("Not assigning the agreed value since that is not the default value for " + combined);
                                }
                            } else if (tempConstant instanceof DoubleConstant) {
                                double tempVal2 = ((DoubleConstant) tempConstant).value;
                                if (Double.compare(tempVal2, Const.default_value_double) == 0) {
                                    this.primTypeFieldValueToUse.put(combined, Double.valueOf(tempVal2));
                                } else {
                                    debug("Not assigning the agreed value since that is not the default value for " + combined);
                                }
                            } else if (tempConstant instanceof FloatConstant) {
                                float tempVal3 = ((FloatConstant) tempConstant).value;
                                if (Float.compare(tempVal3, 0.0f) == 0) {
                                    this.primTypeFieldValueToUse.put(combined, Float.valueOf(tempVal3));
                                } else {
                                    debug("Not assigning the agreed value since that is not the default value for " + combined);
                                }
                            } else if (tempConstant instanceof IntConstant) {
                                int tempVal4 = ((IntConstant) tempConstant).value;
                                if (Integer.compare(tempVal4, 0) == 0) {
                                    SootField tempField = this.classNameFieldNameToSootFieldMapping.get(combined);
                                    if (tempField.getType() instanceof BooleanType) {
                                        this.primTypeFieldValueToUse.put(combined, false);
                                    } else {
                                        this.primTypeFieldValueToUse.put(combined, Integer.valueOf(tempVal4));
                                    }
                                } else {
                                    debug("Not assigning the agreed value since that is not the default value for " + combined);
                                }
                            } else {
                                throw new DecompilationException("Un handled Numberic Constant....report to programmer");
                            }
                        }
                    }
                }
            }
        }
    }

    private void computeFieldToValuesAssignedList() {
        for (SootClass s : this.appClasses) {
            debug("\ncomputeMethodSummaries", "Processing class " + s.getName());
            Iterator<SootMethod> methodIt = s.methodIterator();
            while (methodIt.hasNext()) {
                SootMethod m = methodIt.next();
                if (m.hasActiveBody()) {
                    DavaBody body = (DavaBody) m.getActiveBody();
                    ASTNode AST = (ASTNode) body.getUnits().getFirst();
                    AllDefinitionsFinder defFinder = new AllDefinitionsFinder();
                    AST.apply(defFinder);
                    for (DefinitionStmt stmt : defFinder.getAllDefs()) {
                        Value left = stmt.getLeftOp();
                        if (left instanceof FieldRef) {
                            debug("computeMethodSummaries method: " + m.getName(), "Field ref is: " + left);
                            FieldRef ref = (FieldRef) left;
                            SootField field = ref.getField();
                            if (field.getType() instanceof PrimType) {
                                String fieldName = field.getName();
                                String declaringClass = field.getDeclaringClass().getName();
                                debug("\tField Name: " + fieldName);
                                debug("\tField DeclaringClass: " + declaringClass);
                                String combined = String.valueOf(declaringClass) + combiner + fieldName;
                                ArrayList<Value> valueList = this.fieldToValues.get(combined);
                                if (valueList == null) {
                                    valueList = new ArrayList<>();
                                    this.fieldToValues.put(combined, valueList);
                                }
                                valueList.add(stmt.getRightOp());
                            }
                        }
                    }
                }
            }
        }
    }

    public void printConstantValueFields() {
        System.out.println("\n\n Printing Constant Value Fields (method: printConstantValueFields)");
        for (String combined : this.primTypeFieldValueToUse.keySet()) {
            int temp = combined.indexOf(combiner, 0);
            if (temp > 0) {
                System.out.println("Class: " + combined.substring(0, temp) + " Field: " + combined.substring(temp + combiner.length()) + " Value: " + this.primTypeFieldValueToUse.get(combined));
            }
        }
    }

    public void debug(String methodName, String debug) {
    }

    public void debug(String debug) {
    }
}
