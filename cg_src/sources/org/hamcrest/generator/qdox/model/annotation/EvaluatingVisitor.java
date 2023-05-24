package org.hamcrest.generator.qdox.model.annotation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import org.hamcrest.generator.qdox.model.Annotation;
import org.hamcrest.generator.qdox.model.JavaClass;
import org.hamcrest.generator.qdox.model.JavaField;
import soot.JavaBasicTypes;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/model/annotation/EvaluatingVisitor.class */
public abstract class EvaluatingVisitor implements AnnotationVisitor {
    static Class class$java$lang$String;
    static Class class$java$lang$Double;
    static Class class$java$lang$Float;
    static Class class$java$lang$Long;
    static Class class$java$lang$Integer;

    protected abstract Object getFieldReferenceValue(JavaField javaField);

    public Object getValue(Annotation annotation, String property) {
        Object result = null;
        AnnotationValue value = annotation.getProperty(property);
        if (value != null) {
            result = value.accept(this);
        }
        return result;
    }

    public List getListValue(Annotation annotation, String property) {
        Object value = getValue(annotation, property);
        List list = null;
        if (value != null) {
            if (value instanceof List) {
                list = (List) value;
            } else {
                list = Collections.singletonList(value);
            }
        }
        return list;
    }

    protected static Class resultType(Object left, Object right) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Class type = Void.TYPE;
        if ((left instanceof String) || (right instanceof String)) {
            if (class$java$lang$String == null) {
                cls = class$("java.lang.String");
                class$java$lang$String = cls;
            } else {
                cls = class$java$lang$String;
            }
            type = cls;
        } else if ((left instanceof Number) && (right instanceof Number)) {
            if ((left instanceof Double) || (right instanceof Double)) {
                if (class$java$lang$Double == null) {
                    cls2 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
                    class$java$lang$Double = cls2;
                } else {
                    cls2 = class$java$lang$Double;
                }
                type = cls2;
            } else if ((left instanceof Float) || (right instanceof Float)) {
                if (class$java$lang$Float == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                    class$java$lang$Float = cls3;
                } else {
                    cls3 = class$java$lang$Float;
                }
                type = cls3;
            } else if ((left instanceof Long) || (right instanceof Long)) {
                if (class$java$lang$Long == null) {
                    cls4 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls4;
                } else {
                    cls4 = class$java$lang$Long;
                }
                type = cls4;
            } else {
                if (class$java$lang$Integer == null) {
                    cls5 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                    class$java$lang$Integer = cls5;
                } else {
                    cls5 = class$java$lang$Integer;
                }
                type = cls5;
            }
        }
        return type;
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    protected static Class numericResultType(Object left, Object right) {
        Class cls;
        Class cls2;
        Class type = Void.TYPE;
        if ((left instanceof Number) && (right instanceof Number)) {
            if ((left instanceof Long) || (right instanceof Long)) {
                if (class$java$lang$Long == null) {
                    cls = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls;
                } else {
                    cls = class$java$lang$Long;
                }
                type = cls;
            } else if ((left instanceof Integer) || (right instanceof Integer)) {
                if (class$java$lang$Integer == null) {
                    cls2 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                    class$java$lang$Integer = cls2;
                } else {
                    cls2 = class$java$lang$Integer;
                }
                type = cls2;
            }
        }
        return type;
    }

    protected static Class unaryNumericResultType(Object value) {
        Class cls;
        Class cls2;
        Class type = Void.TYPE;
        if ((value instanceof Byte) || (value instanceof Short) || (value instanceof Character) || (value instanceof Integer)) {
            if (class$java$lang$Integer == null) {
                cls = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                class$java$lang$Integer = cls;
            } else {
                cls = class$java$lang$Integer;
            }
            type = cls;
        } else if (value instanceof Long) {
            if (class$java$lang$Long == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                class$java$lang$Long = cls2;
            } else {
                cls2 = class$java$lang$Long;
            }
        }
        return type;
    }

    protected static Class unaryResultType(Object value) {
        Class cls;
        Class cls2;
        Class type = unaryNumericResultType(value);
        if (type == Void.TYPE) {
            if (value instanceof Float) {
                if (class$java$lang$Float == null) {
                    cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                    class$java$lang$Float = cls2;
                } else {
                    cls2 = class$java$lang$Float;
                }
            } else if (value instanceof Double) {
                if (class$java$lang$Double == null) {
                    cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
                    class$java$lang$Double = cls;
                } else {
                    cls = class$java$lang$Double;
                }
            }
        }
        return type;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotation(Annotation annotation) {
        throw new UnsupportedOperationException(new StringBuffer().append("Illegal annotation value '").append(annotation).append("'.").toString());
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationAdd(AnnotationAdd op) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Class cls5;
        Object result;
        Object left = op.getLeft().accept(this);
        Object right = op.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$String == null) {
            cls = class$("java.lang.String");
            class$java$lang$String = cls;
        } else {
            cls = class$java$lang$String;
        }
        if (type == cls) {
            result = new StringBuffer().append(left.toString()).append(right.toString()).toString();
        } else {
            if (class$java$lang$Double == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
                class$java$lang$Double = cls2;
            } else {
                cls2 = class$java$lang$Double;
            }
            if (type == cls2) {
                result = new Double(((Number) left).doubleValue() + ((Number) right).doubleValue());
            } else {
                if (class$java$lang$Float == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                    class$java$lang$Float = cls3;
                } else {
                    cls3 = class$java$lang$Float;
                }
                if (type == cls3) {
                    result = new Float(((Number) left).floatValue() + ((Number) right).floatValue());
                } else {
                    if (class$java$lang$Long == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                        class$java$lang$Long = cls4;
                    } else {
                        cls4 = class$java$lang$Long;
                    }
                    if (type == cls4) {
                        result = new Long(((Number) left).longValue() + ((Number) right).longValue());
                    } else {
                        if (class$java$lang$Integer == null) {
                            cls5 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                            class$java$lang$Integer = cls5;
                        } else {
                            cls5 = class$java$lang$Integer;
                        }
                        if (type == cls5) {
                            result = new Integer(((Number) left).intValue() + ((Number) right).intValue());
                        } else {
                            throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(op).append("'.").toString());
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationConstant(AnnotationConstant constant) {
        return constant.getValue();
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationDivide(AnnotationDivide op) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Object result;
        Object left = op.getLeft().accept(this);
        Object right = op.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = new Double(((Number) left).doubleValue() / ((Number) right).doubleValue());
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = new Float(((Number) left).floatValue() / ((Number) right).floatValue());
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = new Long(((Number) left).longValue() / ((Number) right).longValue());
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = new Integer(((Number) left).intValue() / ((Number) right).intValue());
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(op).append("'.").toString());
                    }
                }
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationFieldRef(AnnotationFieldRef fieldRef) {
        JavaField javaField = fieldRef.getField();
        if (javaField == null) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot resolve field reference '").append(fieldRef).append("'.").toString());
        }
        if (!javaField.isFinal() || !javaField.isStatic()) {
            throw new IllegalArgumentException(new StringBuffer().append("Field reference '").append(fieldRef).append("' must be static and final.").toString());
        }
        Object result = getFieldReferenceValue(javaField);
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationGreaterThan(AnnotationGreaterThan op) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        boolean result;
        Object left = op.getLeft().accept(this);
        Object right = op.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = ((Number) left).doubleValue() > ((Number) right).doubleValue();
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = ((Number) left).floatValue() > ((Number) right).floatValue();
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = ((Number) left).longValue() > ((Number) right).longValue();
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = ((Number) left).intValue() > ((Number) right).intValue();
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(op).append("'.").toString());
                    }
                }
            }
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLessThan(AnnotationLessThan op) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        boolean result;
        Object left = op.getLeft().accept(this);
        Object right = op.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = ((Number) left).doubleValue() < ((Number) right).doubleValue();
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = ((Number) left).floatValue() < ((Number) right).floatValue();
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = ((Number) left).longValue() < ((Number) right).longValue();
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = ((Number) left).intValue() < ((Number) right).intValue();
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(op).append("'.").toString());
                    }
                }
            }
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationMultiply(AnnotationMultiply op) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Object result;
        Object left = op.getLeft().accept(this);
        Object right = op.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = new Double(((Number) left).doubleValue() * ((Number) right).doubleValue());
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = new Float(((Number) left).floatValue() * ((Number) right).floatValue());
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = new Long(((Number) left).longValue() * ((Number) right).longValue());
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = new Integer(((Number) left).intValue() * ((Number) right).intValue());
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(op).append("'.").toString());
                    }
                }
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationParenExpression(AnnotationParenExpression parenExpression) {
        return parenExpression.getValue().accept(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationSubtract(AnnotationSubtract op) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Object result;
        Object left = op.getLeft().accept(this);
        Object right = op.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = new Double(((Number) left).doubleValue() - ((Number) right).doubleValue());
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = new Float(((Number) left).floatValue() - ((Number) right).floatValue());
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = new Long(((Number) left).longValue() - ((Number) right).longValue());
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = new Integer(((Number) left).intValue() - ((Number) right).intValue());
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(op).append("'.").toString());
                    }
                }
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationTypeRef(AnnotationTypeRef typeRef) {
        JavaClass javaClass = typeRef.getType().getJavaClass();
        return javaClass;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationValueList(AnnotationValueList valueList) {
        List list = new ArrayList();
        ListIterator i = valueList.getValueList().listIterator();
        while (i.hasNext()) {
            AnnotationValue value = (AnnotationValue) i.next();
            Object v = value.accept(this);
            list.add(v);
        }
        return list;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationAnd(AnnotationAnd and) {
        Class cls;
        Class cls2;
        Object result;
        Object left = and.getLeft().accept(this);
        Object right = and.getRight().accept(this);
        Class type = numericResultType(left, right);
        if (class$java$lang$Long == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls;
        } else {
            cls = class$java$lang$Long;
        }
        if (type == cls) {
            result = new Long(((Number) left).longValue() & ((Number) right).longValue());
        } else {
            if (class$java$lang$Integer == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                class$java$lang$Integer = cls2;
            } else {
                cls2 = class$java$lang$Integer;
            }
            if (type == cls2) {
                result = new Integer(((Number) left).intValue() & ((Number) right).intValue());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(and).append("'.").toString());
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationGreaterEquals(AnnotationGreaterEquals greaterEquals) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        boolean result;
        Object left = greaterEquals.getLeft().accept(this);
        Object right = greaterEquals.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = ((Number) left).doubleValue() >= ((Number) right).doubleValue();
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = ((Number) left).floatValue() >= ((Number) right).floatValue();
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = ((Number) left).longValue() >= ((Number) right).longValue();
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = ((Number) left).intValue() >= ((Number) right).intValue();
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(greaterEquals).append("'.").toString());
                    }
                }
            }
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLessEquals(AnnotationLessEquals lessEquals) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        boolean result;
        Object left = lessEquals.getLeft().accept(this);
        Object right = lessEquals.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = ((Number) left).doubleValue() <= ((Number) right).doubleValue();
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = ((Number) left).floatValue() <= ((Number) right).floatValue();
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = ((Number) left).longValue() <= ((Number) right).longValue();
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = ((Number) left).intValue() <= ((Number) right).intValue();
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(lessEquals).append("'.").toString());
                    }
                }
            }
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLogicalAnd(AnnotationLogicalAnd and) {
        Object left = and.getLeft().accept(this);
        Object right = and.getRight().accept(this);
        if ((left instanceof Boolean) && (right instanceof Boolean)) {
            boolean result = ((Boolean) left).booleanValue() && ((Boolean) right).booleanValue();
            return result ? Boolean.TRUE : Boolean.FALSE;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(and).append("'.").toString());
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLogicalNot(AnnotationLogicalNot not) {
        Object value = not.getValue().accept(this);
        if (value instanceof Boolean) {
            boolean result = !((Boolean) value).booleanValue();
            return result ? Boolean.TRUE : Boolean.FALSE;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(not).append("'.").toString());
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationLogicalOr(AnnotationLogicalOr or) {
        Object left = or.getLeft().accept(this);
        Object right = or.getRight().accept(this);
        if ((left instanceof Boolean) && (right instanceof Boolean)) {
            boolean result = ((Boolean) left).booleanValue() || ((Boolean) right).booleanValue();
            return result ? Boolean.TRUE : Boolean.FALSE;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(or).append("'.").toString());
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationMinusSign(AnnotationMinusSign sign) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Object result;
        Object value = sign.getValue().accept(this);
        Class type = unaryResultType(value);
        if (class$java$lang$Integer == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
            class$java$lang$Integer = cls;
        } else {
            cls = class$java$lang$Integer;
        }
        if (type == cls) {
            result = new Integer(-((Integer) value).intValue());
        } else {
            if (class$java$lang$Long == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                class$java$lang$Long = cls2;
            } else {
                cls2 = class$java$lang$Long;
            }
            if (type == cls2) {
                result = new Long(-((Long) value).longValue());
            } else {
                if (class$java$lang$Float == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                    class$java$lang$Float = cls3;
                } else {
                    cls3 = class$java$lang$Float;
                }
                if (type == cls3) {
                    result = new Float(-((Float) value).floatValue());
                } else {
                    if (class$java$lang$Double == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
                        class$java$lang$Double = cls4;
                    } else {
                        cls4 = class$java$lang$Double;
                    }
                    if (type == cls4) {
                        result = new Double(-((Double) value).intValue());
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(sign).append("'.").toString());
                    }
                }
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationNot(AnnotationNot not) {
        Class cls;
        Class cls2;
        Object result;
        Object value = not.getValue().accept(this);
        Object type = unaryNumericResultType(value);
        if (class$java$lang$Long == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls;
        } else {
            cls = class$java$lang$Long;
        }
        if (type == cls) {
            result = new Long(((Long) value).longValue() ^ (-1));
        } else {
            if (class$java$lang$Integer == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                class$java$lang$Integer = cls2;
            } else {
                cls2 = class$java$lang$Integer;
            }
            if (type == cls2) {
                result = new Integer(((Integer) value).intValue() ^ (-1));
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(not).append("'.").toString());
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationOr(AnnotationOr or) {
        Class cls;
        Class cls2;
        Object result;
        Object left = or.getLeft().accept(this);
        Object right = or.getRight().accept(this);
        Class type = numericResultType(left, right);
        if (class$java$lang$Long == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls;
        } else {
            cls = class$java$lang$Long;
        }
        if (type == cls) {
            result = new Long(((Number) left).longValue() | ((Number) right).longValue());
        } else {
            if (class$java$lang$Integer == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                class$java$lang$Integer = cls2;
            } else {
                cls2 = class$java$lang$Integer;
            }
            if (type == cls2) {
                result = new Integer(((Number) left).intValue() | ((Number) right).intValue());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(or).append("'.").toString());
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationPlusSign(AnnotationPlusSign sign) {
        Object value = sign.getValue().accept(this);
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(sign).append("'.").toString());
        }
        return value;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationRemainder(AnnotationRemainder remainder) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        Object result;
        Object left = remainder.getLeft().accept(this);
        Object right = remainder.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = new Double(((Number) left).doubleValue() % ((Number) right).doubleValue());
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = new Float(((Number) left).floatValue() % ((Number) right).floatValue());
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = new Long(((Number) left).longValue() % ((Number) right).longValue());
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = new Integer(((Number) left).intValue() % ((Number) right).intValue());
                    } else {
                        throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(remainder).append("'.").toString());
                    }
                }
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationShiftLeft(AnnotationShiftLeft shiftLeft) {
        Class cls;
        Class cls2;
        Object result;
        Object left = shiftLeft.getLeft().accept(this);
        Object right = shiftLeft.getRight().accept(this);
        Class type = numericResultType(left, right);
        if (class$java$lang$Long == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls;
        } else {
            cls = class$java$lang$Long;
        }
        if (type == cls) {
            result = new Long(((Number) left).longValue() << ((int) ((Number) right).longValue()));
        } else {
            if (class$java$lang$Integer == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                class$java$lang$Integer = cls2;
            } else {
                cls2 = class$java$lang$Integer;
            }
            if (type == cls2) {
                result = new Integer(((Number) left).intValue() << ((Number) right).intValue());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(shiftLeft).append("'.").toString());
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationShiftRight(AnnotationShiftRight shiftRight) {
        Class cls;
        Class cls2;
        Object result;
        Object left = shiftRight.getLeft().accept(this);
        Object right = shiftRight.getRight().accept(this);
        Class type = numericResultType(left, right);
        if (class$java$lang$Long == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls;
        } else {
            cls = class$java$lang$Long;
        }
        if (type == cls) {
            result = new Long(((Number) left).longValue() >> ((int) ((Number) right).longValue()));
        } else {
            if (class$java$lang$Integer == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                class$java$lang$Integer = cls2;
            } else {
                cls2 = class$java$lang$Integer;
            }
            if (type == cls2) {
                result = new Integer(((Number) left).intValue() >> ((Number) right).intValue());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(shiftRight).append("'.").toString());
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationUnsignedShiftRight(AnnotationUnsignedShiftRight shiftRight) {
        Class cls;
        Class cls2;
        Object result;
        Object left = shiftRight.getLeft().accept(this);
        Object right = shiftRight.getRight().accept(this);
        Class type = numericResultType(left, right);
        if (class$java$lang$Long == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls;
        } else {
            cls = class$java$lang$Long;
        }
        if (type == cls) {
            result = new Long(((Number) left).longValue() >>> ((int) ((Number) right).longValue()));
        } else {
            if (class$java$lang$Integer == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                class$java$lang$Integer = cls2;
            } else {
                cls2 = class$java$lang$Integer;
            }
            if (type == cls2) {
                result = new Integer(((Number) left).intValue() >>> ((Number) right).intValue());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(shiftRight).append("'.").toString());
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationEquals(AnnotationEquals annotationEquals) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        boolean result;
        Object left = annotationEquals.getLeft().accept(this);
        Object right = annotationEquals.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = ((Number) left).doubleValue() == ((Number) right).doubleValue();
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = ((Number) left).floatValue() == ((Number) right).floatValue();
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = ((Number) left).longValue() == ((Number) right).longValue();
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = ((Number) left).intValue() == ((Number) right).intValue();
                    } else {
                        result = left == right;
                    }
                }
            }
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationExclusiveOr(AnnotationExclusiveOr annotationExclusiveOr) {
        Class cls;
        Class cls2;
        Object result;
        Object left = annotationExclusiveOr.getLeft().accept(this);
        Object right = annotationExclusiveOr.getRight().accept(this);
        Class type = numericResultType(left, right);
        if (class$java$lang$Long == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_LONG);
            class$java$lang$Long = cls;
        } else {
            cls = class$java$lang$Long;
        }
        if (type == cls) {
            result = new Long(((Number) left).longValue() ^ ((Number) right).longValue());
        } else {
            if (class$java$lang$Integer == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                class$java$lang$Integer = cls2;
            } else {
                cls2 = class$java$lang$Integer;
            }
            if (type == cls2) {
                result = new Integer(((Number) left).intValue() ^ ((Number) right).intValue());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(annotationExclusiveOr).append("'.").toString());
            }
        }
        return result;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationNotEquals(AnnotationNotEquals annotationNotEquals) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        boolean result;
        Object left = annotationNotEquals.getLeft().accept(this);
        Object right = annotationNotEquals.getRight().accept(this);
        Class type = resultType(left, right);
        if (class$java$lang$Double == null) {
            cls = class$(JavaBasicTypes.JAVA_LANG_DOUBLE);
            class$java$lang$Double = cls;
        } else {
            cls = class$java$lang$Double;
        }
        if (type == cls) {
            result = ((Number) left).doubleValue() != ((Number) right).doubleValue();
        } else {
            if (class$java$lang$Float == null) {
                cls2 = class$(JavaBasicTypes.JAVA_LANG_FLOAT);
                class$java$lang$Float = cls2;
            } else {
                cls2 = class$java$lang$Float;
            }
            if (type == cls2) {
                result = ((Number) left).floatValue() != ((Number) right).floatValue();
            } else {
                if (class$java$lang$Long == null) {
                    cls3 = class$(JavaBasicTypes.JAVA_LANG_LONG);
                    class$java$lang$Long = cls3;
                } else {
                    cls3 = class$java$lang$Long;
                }
                if (type == cls3) {
                    result = ((Number) left).longValue() != ((Number) right).longValue();
                } else {
                    if (class$java$lang$Integer == null) {
                        cls4 = class$(JavaBasicTypes.JAVA_LANG_INTEGER);
                        class$java$lang$Integer = cls4;
                    } else {
                        cls4 = class$java$lang$Integer;
                    }
                    if (type == cls4) {
                        result = ((Number) left).intValue() != ((Number) right).intValue();
                    } else {
                        result = left == right;
                    }
                }
            }
        }
        return result ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationQuery(AnnotationQuery annotationQuery) {
        Object value = annotationQuery.getCondition().accept(this);
        if (value == null || !(value instanceof Boolean)) {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(annotationQuery).append("'.").toString());
        }
        AnnotationValue expression = ((Boolean) value).booleanValue() ? annotationQuery.getTrueExpression() : annotationQuery.getFalseExpression();
        return expression.accept(this);
    }

    @Override // org.hamcrest.generator.qdox.model.annotation.AnnotationVisitor
    public Object visitAnnotationCast(AnnotationCast annotationCast) {
        Object result;
        Object value = annotationCast.getValue().accept(this);
        String type = annotationCast.getType().getJavaClass().getFullyQualifiedName();
        if (value instanceof Number) {
            Number n = (Number) value;
            if (type.equals("byte")) {
                result = new Byte(n.byteValue());
            } else if (type.equals("char")) {
                result = new Character((char) n.intValue());
            } else if (type.equals("short")) {
                result = new Short(n.shortValue());
            } else if (type.equals("int")) {
                result = new Integer(n.intValue());
            } else if (type.equals("long")) {
                result = new Long(n.longValue());
            } else if (type.equals(Jimple.FLOAT)) {
                result = new Float(n.floatValue());
            } else if (type.equals("double")) {
                result = new Double(n.doubleValue());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(annotationCast).append("'.").toString());
            }
        } else if (value instanceof String) {
            if (type.equals("java.lang.String")) {
                result = value;
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(annotationCast).append("'.").toString());
            }
        } else {
            throw new IllegalArgumentException(new StringBuffer().append("Cannot evaluate '").append(annotationCast).append("'.").toString());
        }
        return result;
    }
}
