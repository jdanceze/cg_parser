package org.hamcrest.generator.qdox.junit;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import junit.framework.TestCase;
import org.hamcrest.generator.qdox.JavaDocBuilder;
import org.hamcrest.generator.qdox.model.AbstractBaseJavaEntity;
import org.hamcrest.generator.qdox.model.AbstractJavaEntity;
import org.hamcrest.generator.qdox.model.JavaClass;
import org.hamcrest.generator.qdox.model.JavaField;
import org.hamcrest.generator.qdox.model.JavaSource;
/* loaded from: gencallgraphv3.jar:hamcrest-all-1.3.jar:org/hamcrest/generator/qdox/junit/APITestCase.class */
public abstract class APITestCase extends TestCase {
    private static Comparator ENTITY_COMPARATOR = new Comparator() { // from class: org.hamcrest.generator.qdox.junit.APITestCase.1
        @Override // java.util.Comparator
        public int compare(Object o1, Object o2) {
            AbstractBaseJavaEntity entity1 = (AbstractBaseJavaEntity) o1;
            AbstractBaseJavaEntity entity2 = (AbstractBaseJavaEntity) o2;
            return entity1.getName().compareTo(entity2.getName());
        }
    };

    public static void assertApiEquals(URL expected, URL actual) throws IOException {
        JavaDocBuilder builder = new JavaDocBuilder();
        builder.addSource(new InputStreamReader(expected.openStream()), expected.toExternalForm());
        builder.addSource(new InputStreamReader(actual.openStream()), actual.toExternalForm());
        JavaSource expectedSource = builder.getSources()[0];
        JavaSource actualsource = builder.getSources()[1];
        assertApiEquals(expectedSource, actualsource);
    }

    private static void assertApiEquals(JavaSource expected, JavaSource actual) {
        List expectedClasses = Arrays.asList(expected.getClasses());
        Collections.sort(expectedClasses, ENTITY_COMPARATOR);
        List actualClasses = Arrays.asList(actual.getClasses());
        Collections.sort(actualClasses, ENTITY_COMPARATOR);
        assertEquals("Number of classes should be equal", expectedClasses.size(), actualClasses.size());
        for (int i = 0; i < expectedClasses.size(); i++) {
            assertClassesEqual((JavaClass) expectedClasses.get(i), (JavaClass) actualClasses.get(i));
        }
    }

    private static void assertClassesEqual(JavaClass expected, JavaClass actual) {
        assertEquals("Package names should be equal", expected.getPackage(), actual.getPackage());
        assertModifiersEquals("Class modifiers should be equal", expected, actual);
        assertEquals("Class names should be equal", expected.getName(), actual.getName());
        if (expected.getSuperJavaClass() != null && actual.getSuperJavaClass() != null) {
            assertEquals("Super class should be equal", expected.getSuperJavaClass().getName(), actual.getSuperJavaClass().getName());
        }
        if ((expected.getSuperJavaClass() == null) ^ (actual.getSuperJavaClass() == null)) {
            fail("Super class should be equal");
        }
        assertInterfacesEqual(expected, actual);
        assertInnerClassesEquals(expected, actual);
        assertFieldsEqual(expected, actual);
        assertMethodsEqual(expected, actual);
    }

    private static void assertFieldEquals(JavaField expected, JavaField actual) {
        StringBuffer message = new StringBuffer("-> assertFieldEquals");
        message.append("\n\tExcepted : ");
        message.append(expected);
        message.append("\n\tActual : ");
        message.append(actual);
        message.append("\n");
        assertEquals(new StringBuffer().append(message.toString()).append("Field types should be equal").toString(), expected.getType(), actual.getType());
        assertEquals(new StringBuffer().append(message.toString()).append("Field names should be equal").toString(), expected.getName(), actual.getName());
        assertModifiersEquals(new StringBuffer().append(message.toString()).append("Field modifiers should be equal").toString(), expected, actual);
    }

    private static void assertFieldsEqual(JavaClass expected, JavaClass actual) {
        List expectedFields = Arrays.asList(expected.getFields());
        Collections.sort(expectedFields, ENTITY_COMPARATOR);
        List actualFields = Arrays.asList(actual.getFields());
        Collections.sort(actualFields, ENTITY_COMPARATOR);
        StringBuffer message = new StringBuffer("-> assertFieldsEqual");
        message.append("\n\tExcepted : ");
        message.append(expectedFields);
        message.append("\n\tActual : ");
        message.append(actualFields);
        message.append("\n");
        assertEquals(new StringBuffer().append(message.toString()).append("Number of fields should be equal").toString(), expectedFields.size(), actualFields.size());
        for (int i = 0; i < expectedFields.size(); i++) {
            assertFieldEquals((JavaField) expectedFields.get(i), (JavaField) actualFields.get(i));
        }
    }

    private static void assertInnerClassesEquals(JavaClass expected, JavaClass actual) {
        List expectedInnerClasses = Arrays.asList(expected.getNestedClasses());
        Collections.sort(expectedInnerClasses, ENTITY_COMPARATOR);
        List actualInnerClasses = Arrays.asList(actual.getNestedClasses());
        Collections.sort(actualInnerClasses, ENTITY_COMPARATOR);
        StringBuffer message = new StringBuffer("-> assertInnerClassesEquals");
        message.append("\n\tExcepted : ");
        message.append(expectedInnerClasses);
        message.append("\n\tActual : ");
        message.append(actualInnerClasses);
        message.append("\n");
        assertEquals(new StringBuffer().append(message.toString()).append("Number of inner classes should be equal").toString(), expectedInnerClasses.size(), actualInnerClasses.size());
        for (int i = 0; i < expectedInnerClasses.size(); i++) {
            assertClassesEqual((JavaClass) expectedInnerClasses.get(i), (JavaClass) actualInnerClasses.get(i));
        }
    }

    private static void assertInterfacesEqual(JavaClass expected, JavaClass actual) {
        List expectedImplements = Arrays.asList(expected.getImplements());
        Collections.sort(expectedImplements);
        List actualImplements = Arrays.asList(actual.getImplements());
        Collections.sort(actualImplements);
        StringBuffer message = new StringBuffer("-> assertInnerClassesEquals");
        message.append("\n\tExcepted : ");
        message.append(expectedImplements);
        message.append("\n\tActual : ");
        message.append(actualImplements);
        message.append("\n");
        assertEquals(new StringBuffer().append(message.toString()).append("Number of implemented interface should be equal").toString(), expectedImplements.size(), actualImplements.size());
        for (int i = 0; i < expectedImplements.size(); i++) {
            assertEquals("Implemented interface should be equal", expectedImplements.get(i), actualImplements.get(i));
        }
    }

    private static void assertMethodsEqual(JavaClass expected, JavaClass actual) {
        List expectedMethods = Arrays.asList(expected.getMethods());
        Collections.sort(expectedMethods, ENTITY_COMPARATOR);
        List actualMethods = Arrays.asList(actual.getMethods());
        Collections.sort(actualMethods, ENTITY_COMPARATOR);
        StringBuffer message = new StringBuffer("-> assertMethodsEqual");
        message.append("\n\tExcepted : ");
        message.append(expectedMethods);
        message.append("\n\tActual : ");
        message.append(actualMethods);
        message.append("\n");
        assertEquals(new StringBuffer().append(message.toString()).append("Number of methods should be equal").toString(), expectedMethods.size(), actualMethods.size());
        for (int i = 0; i < expectedMethods.size(); i++) {
            assertEquals("Method should be equal", expectedMethods.get(i), actualMethods.get(i));
        }
    }

    private static void assertModifiersEquals(String msg, AbstractJavaEntity expected, AbstractJavaEntity actual) {
        List expectedModifiers = Arrays.asList(expected.getModifiers());
        Collections.sort(expectedModifiers);
        List actualModifiers = Arrays.asList(actual.getModifiers());
        Collections.sort(actualModifiers);
        StringBuffer message = new StringBuffer("-> assertModifiersEquals");
        message.append("\n\tExcepted : ");
        message.append(expectedModifiers);
        message.append("\n\tActual : ");
        message.append(actualModifiers);
        message.append("\n");
        assertEquals(new StringBuffer().append(message.toString()).append(msg).append("\nNumber of modifiers should be equal").toString(), expectedModifiers.size(), actualModifiers.size());
        for (int i = 0; i < expectedModifiers.size(); i++) {
            assertEquals(new StringBuffer().append(msg).append("\n").append(message.toString()).append("\nModifier should be equal").toString(), expectedModifiers.get(i), actualModifiers.get(i));
        }
    }

    private static void assertNotDir(File expected, File actual) {
        if (expected.isDirectory()) {
            fail(new StringBuffer().append(expected.getAbsolutePath()).append(" - should not have been a directory").toString());
        }
        if (actual.isDirectory()) {
            fail(new StringBuffer().append(actual.getAbsolutePath()).append(" - should not have been a directory").toString());
        }
    }

    protected File getDir() {
        return new File(getClass().getResource(new StringBuffer().append("/").append(getClass().getName().replace('.', '/')).append(".class").toString()).getFile()).getParentFile();
    }

    protected File getRootDir() {
        File dir = getDir();
        StringTokenizer st = new StringTokenizer(getClass().getName(), ".");
        for (int i = 0; i < st.countTokens() - 1; i++) {
            dir = dir.getParentFile();
        }
        return dir;
    }
}
