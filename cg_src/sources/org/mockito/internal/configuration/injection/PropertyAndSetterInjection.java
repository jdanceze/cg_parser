package org.mockito.internal.configuration.injection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TerminalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.collections.Sets;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;
/* loaded from: gencallgraphv3.jar:mockito-core-3.6.0.jar:org/mockito/internal/configuration/injection/PropertyAndSetterInjection.class */
public class PropertyAndSetterInjection extends MockInjectionStrategy {
    private final MockCandidateFilter mockCandidateFilter = new TypeBasedCandidateFilter(new NameBasedCandidateFilter(new TerminalMockCandidateFilter()));
    private final ListUtil.Filter<Field> notFinalOrStatic = new ListUtil.Filter<Field>() { // from class: org.mockito.internal.configuration.injection.PropertyAndSetterInjection.1
        @Override // org.mockito.internal.util.collections.ListUtil.Filter
        public boolean isOut(Field object) {
            return Modifier.isFinal(object.getModifiers()) || Modifier.isStatic(object.getModifiers());
        }
    };

    @Override // org.mockito.internal.configuration.injection.MockInjectionStrategy
    public boolean processInjection(Field injectMocksField, Object injectMocksFieldOwner, Set<Object> mockCandidates) {
        FieldInitializationReport report = initializeInjectMocksField(injectMocksField, injectMocksFieldOwner);
        boolean injectionOccurred = false;
        Object fieldInstanceNeedingInjection = report.fieldInstance();
        for (Class<?> fieldClass = report.fieldClass(); fieldClass != Object.class; fieldClass = fieldClass.getSuperclass()) {
            injectionOccurred |= injectMockCandidates(fieldClass, fieldInstanceNeedingInjection, Sets.newMockSafeHashSet(mockCandidates));
        }
        return injectionOccurred;
    }

    private FieldInitializationReport initializeInjectMocksField(Field field, Object fieldOwner) {
        try {
            return new FieldInitializer(fieldOwner, field).initialize();
        } catch (MockitoException e) {
            if (e.getCause() instanceof InvocationTargetException) {
                Throwable realCause = e.getCause().getCause();
                throw Reporter.fieldInitialisationThrewException(field, realCause);
            }
            throw Reporter.cannotInitializeForInjectMocksAnnotation(field.getName(), e.getMessage());
        }
    }

    private boolean injectMockCandidates(Class<?> awaitingInjectionClazz, Object injectee, Set<Object> mocks) {
        List<Field> orderedCandidateInjecteeFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
        boolean injectionOccurred = injectMockCandidatesOnFields(mocks, injectee, false, orderedCandidateInjecteeFields);
        return injectionOccurred | injectMockCandidatesOnFields(mocks, injectee, injectionOccurred, orderedCandidateInjecteeFields);
    }

    private boolean injectMockCandidatesOnFields(Set<Object> mocks, Object injectee, boolean injectionOccurred, List<Field> orderedCandidateInjecteeFields) {
        Iterator<Field> it = orderedCandidateInjecteeFields.iterator();
        while (it.hasNext()) {
            Field candidateField = it.next();
            Object injected = this.mockCandidateFilter.filterCandidate(mocks, candidateField, orderedCandidateInjecteeFields, injectee).thenInject();
            if (injected != null) {
                injectionOccurred |= true;
                mocks.remove(injected);
                it.remove();
            }
        }
        return injectionOccurred;
    }

    private List<Field> orderedInstanceFieldsFrom(Class<?> awaitingInjectionClazz) {
        List<Field> declaredFields = Arrays.asList(awaitingInjectionClazz.getDeclaredFields());
        return SuperTypesLastSorter.sortSuperTypesLast(ListUtil.filter(declaredFields, this.notFinalOrStatic));
    }
}
