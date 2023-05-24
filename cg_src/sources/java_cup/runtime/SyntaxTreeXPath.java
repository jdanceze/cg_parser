package java_cup.runtime;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java_cup.runtime.XMLElement;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/SyntaxTreeXPath.class */
public class SyntaxTreeXPath {
    public static List<XMLElement> query(String query, XMLElement element) {
        if (query.startsWith("/")) {
            query = query.substring(1);
        }
        return query0(new LinkedList(Arrays.asList(query.split("/"))), 0, element, 0);
    }

    private static List<XMLElement> query0(List<String> q, int idx, XMLElement element, int seq) {
        if (q.get(idx).isEmpty()) {
            return matchDeeperDescendant(q, idx + 1, element, seq);
        }
        List<XMLElement> l = new LinkedList<>();
        if (match(q.get(idx), element, seq)) {
            if (q.size() - 1 == idx) {
                return singleton(element);
            }
            List<XMLElement> children = element.getChildren();
            for (int i = 0; i < children.size(); i++) {
                XMLElement child = children.get(i);
                l.addAll(query0(q, idx + 1, child, i));
            }
            return l;
        }
        return new LinkedList();
    }

    private static List<XMLElement> matchDeeperDescendant(List<String> query, int idx, XMLElement element, int seq) {
        if (query.size() <= idx) {
            return singleton(element);
        }
        boolean matches = match(query.get(idx), element, seq);
        List<XMLElement> l = new LinkedList<>();
        List<XMLElement> children = element.getChildren();
        if (matches) {
            return query0(query, idx, element, seq);
        }
        for (int i = 0; i < children.size(); i++) {
            XMLElement child = children.get(i);
            l.addAll(matchDeeperDescendant(query, idx, child, i));
        }
        return l;
    }

    private static boolean match(String m, XMLElement elem, int seq) {
        boolean result;
        boolean z;
        boolean z2;
        String[] name = m.split("\\[");
        String[] tag = name[0].split("\\*");
        if (tag[0].isEmpty()) {
            if (tag.length > 2) {
                result = true & elem.tagname.contains(tag[1]);
            } else if (tag.length == 2) {
                result = true & elem.tagname.endsWith(tag[1]);
            } else {
                result = true & false;
            }
        } else if (tag.length == 2) {
            result = true & elem.tagname.startsWith(tag[1]);
        } else {
            result = elem.tagname.equals(tag[0]);
        }
        for (int i = 1; i < name.length; i++) {
            String predicate = name[i];
            if (!predicate.endsWith("]")) {
                return false;
            }
            String predicate2 = predicate.substring(0, predicate.length() - 1);
            if (predicate2.startsWith("@")) {
                if (predicate2.substring(1).startsWith("variant") && (elem instanceof XMLElement.NonTerminal) && Integer.parseInt(predicate2.substring(9)) == ((XMLElement.NonTerminal) elem).getVariant()) {
                    z = result;
                    z2 = true;
                } else {
                    return false;
                }
            } else if (predicate2.matches("\\d+")) {
                z = result;
                z2 = Integer.parseInt(predicate2) == seq;
            } else {
                return false;
            }
            result = z & z2;
        }
        return result;
    }

    private static List<XMLElement> singleton(XMLElement elem) {
        LinkedList<XMLElement> l = new LinkedList<>();
        l.add(elem);
        return l;
    }
}
