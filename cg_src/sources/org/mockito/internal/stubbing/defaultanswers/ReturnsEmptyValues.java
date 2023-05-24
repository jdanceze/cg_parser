package org.mockito.internal.stubbing.defaultanswers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.mockito.internal.util.JavaEightUtil;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/stubbing/defaultanswers/ReturnsEmptyValues.class */
public class ReturnsEmptyValues implements Answer<Object>, Serializable {
    private static final long serialVersionUID = 1998191268711234347L;

    @Override // org.mockito.stubbing.Answer
    public Object answer(InvocationOnMock invocation) {
        if (ObjectMethodsGuru.isToStringMethod(invocation.getMethod())) {
            Object mock = invocation.getMock();
            MockName name = MockUtil.getMockName(mock);
            if (name.isDefault()) {
                return "Mock for " + MockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode();
            }
            return name.toString();
        } else if (ObjectMethodsGuru.isCompareToMethod(invocation.getMethod())) {
            return Integer.valueOf(invocation.getMock() == invocation.getArgument(0) ? 0 : 1);
        } else {
            Class<?> returnType = invocation.getMethod().getReturnType();
            return returnValueFor(returnType);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object returnValueFor(Class<?> type) {
        if (Primitives.isPrimitiveOrWrapper(type)) {
            return Primitives.defaultValue(type);
        }
        if (type == Iterable.class) {
            return new ArrayList(0);
        }
        if (type == Collection.class) {
            return new LinkedList();
        }
        if (type == Set.class) {
            return new HashSet();
        }
        if (type == HashSet.class) {
            return new HashSet();
        }
        if (type == SortedSet.class) {
            return new TreeSet();
        }
        if (type == TreeSet.class) {
            return new TreeSet();
        }
        if (type == LinkedHashSet.class) {
            return new LinkedHashSet();
        }
        if (type == List.class) {
            return new LinkedList();
        }
        if (type == LinkedList.class) {
            return new LinkedList();
        }
        if (type == ArrayList.class) {
            return new ArrayList();
        }
        if (type == Map.class) {
            return new HashMap();
        }
        if (type == HashMap.class) {
            return new HashMap();
        }
        if (type == SortedMap.class) {
            return new TreeMap();
        }
        if (type == TreeMap.class) {
            return new TreeMap();
        }
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if ("java.util.Optional".equals(type.getName())) {
            return JavaEightUtil.emptyOptional();
        }
        if ("java.util.OptionalDouble".equals(type.getName())) {
            return JavaEightUtil.emptyOptionalDouble();
        }
        if ("java.util.OptionalInt".equals(type.getName())) {
            return JavaEightUtil.emptyOptionalInt();
        }
        if ("java.util.OptionalLong".equals(type.getName())) {
            return JavaEightUtil.emptyOptionalLong();
        }
        if ("java.util.stream.Stream".equals(type.getName())) {
            return JavaEightUtil.emptyStream();
        }
        if ("java.util.stream.DoubleStream".equals(type.getName())) {
            return JavaEightUtil.emptyDoubleStream();
        }
        if ("java.util.stream.IntStream".equals(type.getName())) {
            return JavaEightUtil.emptyIntStream();
        }
        if ("java.util.stream.LongStream".equals(type.getName())) {
            return JavaEightUtil.emptyLongStream();
        }
        if ("java.time.Duration".equals(type.getName())) {
            return JavaEightUtil.emptyDuration();
        }
        if ("java.time.Period".equals(type.getName())) {
            return JavaEightUtil.emptyPeriod();
        }
        return null;
    }
}
