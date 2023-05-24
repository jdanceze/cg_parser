package junit.framework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
/* JADX WARN: Classes with same name are omitted:
  gencallgraphv3.jar:junit-4.13.2.jar:junit/framework/TestCase.class
 */
/* loaded from: gencallgraphv3.jar:org.junit_4.13.2.v20211018-1956.jar:junit/framework/TestCase.class */
public abstract class TestCase extends Assert implements Test {
    private String fName;

    public TestCase() {
        this.fName = null;
    }

    public TestCase(String name) {
        this.fName = name;
    }

    @Override // junit.framework.Test
    public int countTestCases() {
        return 1;
    }

    protected TestResult createResult() {
        return new TestResult();
    }

    public TestResult run() {
        TestResult result = createResult();
        run(result);
        return result;
    }

    @Override // junit.framework.Test
    public void run(TestResult result) {
        result.run(this);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Unreachable block: B:20:0x002f
        	at jadx.core.dex.visitors.blocks.BlockProcessor.checkForUnreachableBlocks(BlockProcessor.java:81)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:47)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:39)
        */
    public void runBare() throws java.lang.Throwable {
        /*
            r2 = this;
            r0 = 0
            r3 = r0
            r0 = r2
            r0.setUp()
            r0 = r2
            r0.runTest()     // Catch: java.lang.Throwable -> L1b
            r0 = r2
            r0.tearDown()     // Catch: java.lang.Throwable -> L11
            goto L42
        L11:
            r4 = move-exception
            r0 = r3
            if (r0 != 0) goto L18
            r0 = r4
            r3 = r0
        L18:
            goto L42
        L1b:
            r4 = move-exception
            r0 = r4
            r3 = r0
            r0 = r2
            r0.tearDown()     // Catch: java.lang.Throwable -> L25
            goto L42
        L25:
            r4 = move-exception
            r0 = r3
            if (r0 != 0) goto L2c
            r0 = r4
            r3 = r0
        L2c:
            goto L42
        L2f:
            r5 = move-exception
            r0 = r2
            r0.tearDown()     // Catch: java.lang.Throwable -> L37
            goto L40
        L37:
            r6 = move-exception
            r0 = r3
            if (r0 != 0) goto L40
            r0 = r6
            r3 = r0
        L40:
            r0 = r5
            throw r0
        L42:
            r0 = r3
            if (r0 == 0) goto L48
            r0 = r3
            throw r0
        L48:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: junit.framework.TestCase.runBare():void");
    }

    protected void runTest() throws Throwable {
        assertNotNull("TestCase.fName cannot be null", this.fName);
        Method runMethod = null;
        try {
            runMethod = getClass().getMethod(this.fName, null);
        } catch (NoSuchMethodException e) {
            fail("Method \"" + this.fName + "\" not found");
        }
        if (!Modifier.isPublic(runMethod.getModifiers())) {
            fail("Method \"" + this.fName + "\" should be public");
        }
        try {
            runMethod.invoke(this, new Object[0]);
        } catch (IllegalAccessException e2) {
            e2.fillInStackTrace();
            throw e2;
        } catch (InvocationTargetException e3) {
            e3.fillInStackTrace();
            throw e3.getTargetException();
        }
    }

    public static void assertTrue(String message, boolean condition) {
        Assert.assertTrue(message, condition);
    }

    public static void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

    public static void assertFalse(String message, boolean condition) {
        Assert.assertFalse(message, condition);
    }

    public static void assertFalse(boolean condition) {
        Assert.assertFalse(condition);
    }

    public static void fail(String message) {
        Assert.fail(message);
    }

    public static void fail() {
        Assert.fail();
    }

    public static void assertEquals(String message, Object expected, Object actual) {
        Assert.assertEquals(message, expected, actual);
    }

    public static void assertEquals(Object expected, Object actual) {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(String message, String expected, String actual) {
        Assert.assertEquals(message, expected, actual);
    }

    public static void assertEquals(String expected, String actual) {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(String message, double expected, double actual, double delta) {
        Assert.assertEquals(message, expected, actual, delta);
    }

    public static void assertEquals(double expected, double actual, double delta) {
        Assert.assertEquals(expected, actual, delta);
    }

    public static void assertEquals(String message, float expected, float actual, float delta) {
        Assert.assertEquals(message, expected, actual, delta);
    }

    public static void assertEquals(float expected, float actual, float delta) {
        Assert.assertEquals(expected, actual, delta);
    }

    public static void assertEquals(String message, long expected, long actual) {
        Assert.assertEquals(message, expected, actual);
    }

    public static void assertEquals(long expected, long actual) {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(String message, boolean expected, boolean actual) {
        Assert.assertEquals(message, expected, actual);
    }

    public static void assertEquals(boolean expected, boolean actual) {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(String message, byte expected, byte actual) {
        Assert.assertEquals(message, expected, actual);
    }

    public static void assertEquals(byte expected, byte actual) {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(String message, char expected, char actual) {
        Assert.assertEquals(message, expected, actual);
    }

    public static void assertEquals(char expected, char actual) {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(String message, short expected, short actual) {
        Assert.assertEquals(message, expected, actual);
    }

    public static void assertEquals(short expected, short actual) {
        Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(String message, int expected, int actual) {
        Assert.assertEquals(message, expected, actual);
    }

    public static void assertEquals(int expected, int actual) {
        Assert.assertEquals(expected, actual);
    }

    public static void assertNotNull(Object object) {
        Assert.assertNotNull(object);
    }

    public static void assertNotNull(String message, Object object) {
        Assert.assertNotNull(message, object);
    }

    public static void assertNull(Object object) {
        Assert.assertNull(object);
    }

    public static void assertNull(String message, Object object) {
        Assert.assertNull(message, object);
    }

    public static void assertSame(String message, Object expected, Object actual) {
        Assert.assertSame(message, expected, actual);
    }

    public static void assertSame(Object expected, Object actual) {
        Assert.assertSame(expected, actual);
    }

    public static void assertNotSame(String message, Object expected, Object actual) {
        Assert.assertNotSame(message, expected, actual);
    }

    public static void assertNotSame(Object expected, Object actual) {
        Assert.assertNotSame(expected, actual);
    }

    public static void failSame(String message) {
        Assert.failSame(message);
    }

    public static void failNotSame(String message, Object expected, Object actual) {
        Assert.failNotSame(message, expected, actual);
    }

    public static void failNotEquals(String message, Object expected, Object actual) {
        Assert.failNotEquals(message, expected, actual);
    }

    public static String format(String message, Object expected, Object actual) {
        return Assert.format(message, expected, actual);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    public String toString() {
        return getName() + "(" + getClass().getName() + ")";
    }

    public String getName() {
        return this.fName;
    }

    public void setName(String name) {
        this.fName = name;
    }
}
