package soot.jimple.spark.ondemand.genericutil;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.security.Permission;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/genericutil/Util.class */
public class Util {
    private static final Logger logger;
    public static final BitSet EMPTY_BITSET;
    public static final boolean FULLY_QUALIFIED_NAMES = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Util.class.desiredAssertionStatus();
        logger = LoggerFactory.getLogger(Util.class);
        EMPTY_BITSET = new BitSet();
    }

    public static long fact(long n_) {
        long result = 1;
        long j = 1;
        while (true) {
            long i = j;
            if (i <= n_) {
                result *= i;
                j = i + 1;
            } else {
                return result;
            }
        }
    }

    public static BigInteger fact(BigInteger n_) {
        BigInteger result = BigInteger.ONE;
        BigInteger bigInteger = BigInteger.ONE;
        while (true) {
            BigInteger i = bigInteger;
            if (i.compareTo(n_) <= 0) {
                result = result.multiply(i);
                bigInteger = i.add(BigInteger.ONE);
            } else {
                return result;
            }
        }
    }

    public static double fact(double n_) {
        double result = 1.0d;
        double d = 1.0d;
        while (true) {
            double i = d;
            if (i <= n_ + 1.0E-6d) {
                result *= i;
                d = i + 1.0d;
            } else {
                return result;
            }
        }
    }

    public static int fact(int n_) {
        int result = 1;
        for (int i = 1; i <= n_; i++) {
            result *= i;
        }
        return result;
    }

    public static int binaryLogUp(int n_) {
        int k = 0;
        while ((1 << k) < n_) {
            k++;
        }
        return k;
    }

    public static int binaryLogUp(long n_) {
        int k = 0;
        while ((1 << k) < n_) {
            k++;
        }
        return k;
    }

    public static String str(int[] ints_) {
        StringBuffer s = new StringBuffer();
        s.append("[");
        for (int i = 0; i < ints_.length; i++) {
            if (i > 0) {
                s.append(", ");
            }
            s.append(ints_[i]);
        }
        s.append("]");
        return s.toString();
    }

    public static String objArrayToString(Object[] o) {
        return objArrayToString(o, "[", "]", ", ");
    }

    public static String objArrayToString(Object[] o, String start, String end, String sep) {
        StringBuffer s = new StringBuffer();
        s.append(start);
        for (int i = 0; i < o.length; i++) {
            if (o[i] != null) {
                if (i > 0) {
                    s.append(sep);
                }
                s.append(o[i].toString());
            }
        }
        s.append(end);
        return s.toString();
    }

    public static String str(Throwable thrown_) {
        ByteArrayOutputStream traceDump = new ByteArrayOutputStream();
        PrintWriter w = new PrintWriter(traceDump);
        logger.error(thrown_.getMessage(), thrown_);
        w.close();
        return traceDump.toString();
    }

    public static <T> boolean forSome(Collection<T> c_, Predicate<T> p_) {
        for (T t : c_) {
            if (p_.test(t)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T find(Collection<T> c_, Predicate<T> p_) {
        for (T obj : c_) {
            if (p_.test(obj)) {
                return obj;
            }
        }
        return null;
    }

    public static <T> Collection<T> findAll(Collection<T> c_, Predicate<T> p_) {
        Collection<T> result = new LinkedList<>();
        for (T obj : c_) {
            if (p_.test(obj)) {
                result.add(obj);
            }
        }
        return result;
    }

    public static <T> boolean forAll(Collection<T> c_, Predicate<T> p_) {
        for (T t : c_) {
            if (!p_.test(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> void doForAll(Collection<T> c_, ObjectVisitor<T> v_) {
        for (T t : c_) {
            v_.visit(t);
        }
    }

    public static <T, U> List<U> map(List<T> srcList, Mapper<T, U> mapper_) {
        ArrayList<U> result = new ArrayList<>();
        for (T t : srcList) {
            result.add(mapper_.map(t));
        }
        return result;
    }

    public static <T> List<T> filter(Collection<T> src_, Predicate<T> pred_) {
        ArrayList<T> result = new ArrayList<>();
        for (T curElem : src_) {
            if (pred_.test(curElem)) {
                result.add(curElem);
            }
        }
        return result;
    }

    public static <T> void filter(Collection<T> src_, Predicate<T> pred_, List<T> result_) {
        for (T t : src_) {
            if (pred_.test(t)) {
                result_.add(t);
            }
        }
    }

    public static <T, U> Set<U> mapToSet(Collection<T> srcSet, Mapper<T, U> mapper_) {
        HashSet<U> result = new HashSet<>();
        for (T t : srcSet) {
            result.add(mapper_.map(t));
        }
        return result;
    }

    public static int[] realloc(int[] data_, int newSize_) {
        if (data_.length < newSize_) {
            int[] newData = new int[newSize_];
            System.arraycopy(data_, 0, newData, 0, data_.length);
            return newData;
        }
        return data_;
    }

    public static void clear(BitSet bitSet_) {
        bitSet_.and(EMPTY_BITSET);
    }

    public static String replaceAll(String str_, String sub_, String newSub_) {
        if (str_.indexOf(sub_) == -1) {
            return str_;
        }
        int subLen = sub_.length();
        StringBuffer result = new StringBuffer(str_);
        while (true) {
            int idx = result.toString().indexOf(sub_);
            if (idx >= 0) {
                result.replace(idx, idx + subLen, newSub_);
            } else {
                return result.toString();
            }
        }
    }

    public static String removeAll(String str_, String sub_) {
        return replaceAll(str_, sub_, "");
    }

    public static String objectFieldsToString(Object obj) {
        SecurityManager oldsecurity = System.getSecurityManager();
        System.setSecurityManager(new SecurityManager() { // from class: soot.jimple.spark.ondemand.genericutil.Util.1
            @Override // java.lang.SecurityManager
            public void checkPermission(Permission perm) {
            }
        });
        Class c = obj.getClass();
        StringBuffer buf = new StringBuffer(removePackageName(c.getName()));
        while (c != Object.class) {
            Field[] fields = c.getDeclaredFields();
            if (fields.length > 0) {
                buf = buf.append(" (");
            }
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                try {
                    Class type = fields[i].getType();
                    String name = fields[i].getName();
                    Object value = fields[i].get(obj);
                    buf = buf.append(name).append("=");
                    buf = buf.append(value == null ? Jimple.NULL : value.toString()).append(" : ").append(removePackageName(type.getName()));
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), (Throwable) e);
                }
                buf = buf.append(i + 1 >= fields.length ? ")" : ",");
            }
            c = c.getSuperclass();
        }
        System.setSecurityManager(oldsecurity);
        return buf.toString();
    }

    public static String removePackageName(String fully_qualified_name_) {
        if (fully_qualified_name_ == null) {
            return null;
        }
        int lastdot = fully_qualified_name_.lastIndexOf(46);
        if (lastdot < 0) {
            return "";
        }
        return fully_qualified_name_.substring(lastdot + 1);
    }

    public static int hashArray(Object[] objs) {
        int ret = 1;
        for (int i = 0; i < objs.length; i++) {
            ret = (31 * ret) + (objs[i] == null ? 0 : objs[i].hashCode());
        }
        return ret;
    }

    public static boolean arrayContains(Object[] arr, Object obj, int size) {
        if ($assertionsDisabled || obj != null) {
            for (int i = 0; i < size; i++) {
                if (arr[i] != null && arr[i].equals(obj)) {
                    return true;
                }
            }
            return false;
        }
        throw new AssertionError();
    }

    public static String toStringNull(Object o) {
        return o == null ? "" : "[" + o.toString() + "]";
    }

    public static <T> boolean intersecting(Set<T> s1, final Set<T> s2) {
        return forSome(s1, new Predicate<T>() { // from class: soot.jimple.spark.ondemand.genericutil.Util.2
            @Override // soot.jimple.spark.ondemand.genericutil.Predicate
            public boolean test(T obj) {
                return s2.contains(obj);
            }
        });
    }

    public static boolean stringContains(String str, String subStr) {
        return str.indexOf(subStr) != -1;
    }

    public static int getInt(Integer i) {
        if (i == null) {
            return 0;
        }
        return i.intValue();
    }

    public static String topLevelTypeString(String typeStr) {
        int dollarIndex = typeStr.indexOf(36);
        String topLevelTypeStr = dollarIndex == -1 ? typeStr : typeStr.substring(0, dollarIndex);
        return topLevelTypeStr;
    }

    public static <T> void addIfNotNull(T val, Collection<T> vals) {
        if (val != null) {
            vals.add(val);
        }
    }

    public static <T> List<T> pickNAtRandom(List<T> vals, int n, long seed) {
        boolean added;
        if (vals.size() <= n) {
            return vals;
        }
        HashSet<T> elems = new HashSet<>();
        Random rand = new Random(seed);
        for (int i = 0; i < n; i++) {
            do {
                int randIndex = rand.nextInt(n);
                added = elems.add(vals.get(randIndex));
            } while (!added);
        }
        return new ArrayList(elems);
    }
}
