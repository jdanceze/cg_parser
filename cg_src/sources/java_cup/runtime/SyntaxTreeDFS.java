package java_cup.runtime;

import java.util.HashMap;
import java.util.List;
/* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/SyntaxTreeDFS.class */
public class SyntaxTreeDFS {

    /* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/SyntaxTreeDFS$ElementHandler.class */
    public interface ElementHandler {
        void handle(XMLElement xMLElement, List<XMLElement> list);
    }

    /* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/SyntaxTreeDFS$Visitor.class */
    public interface Visitor {
        void preVisit(XMLElement xMLElement);

        void postVisit(XMLElement xMLElement);
    }

    /* loaded from: gencallgraphv3.jar:java_cup-0.9.2.jar:java_cup/runtime/SyntaxTreeDFS$AbstractVisitor.class */
    public static abstract class AbstractVisitor implements Visitor {
        private HashMap<String, ElementHandler> preMap = new HashMap<>();
        private HashMap<String, ElementHandler> postMap = new HashMap<>();

        public abstract void defaultPre(XMLElement xMLElement, List<XMLElement> list);

        public abstract void defaultPost(XMLElement xMLElement, List<XMLElement> list);

        @Override // java_cup.runtime.SyntaxTreeDFS.Visitor
        public void preVisit(XMLElement element) {
            ElementHandler handler = this.preMap.get(element.tagname);
            if (handler == null) {
                defaultPre(element, element.getChildren());
            } else {
                handler.handle(element, element.getChildren());
            }
        }

        @Override // java_cup.runtime.SyntaxTreeDFS.Visitor
        public void postVisit(XMLElement element) {
            ElementHandler handler = this.postMap.get(element.tagname);
            if (handler == null) {
                defaultPost(element, element.getChildren());
            } else {
                handler.handle(element, element.getChildren());
            }
        }

        public void registerPreVisit(String s, ElementHandler h) {
            this.preMap.put(s, h);
        }

        public void registerPostVisit(String s, ElementHandler h) {
            this.postMap.put(s, h);
        }
    }

    public static void dfs(XMLElement element, Visitor visitor) {
        visitor.preVisit(element);
        for (XMLElement el : element.getChildren()) {
            dfs(el, visitor);
        }
        visitor.postVisit(element);
    }
}
