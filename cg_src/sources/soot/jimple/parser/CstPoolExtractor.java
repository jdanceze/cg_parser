package soot.jimple.parser;

import java.util.HashSet;
import java.util.Set;
import soot.Scene;
import soot.jimple.parser.analysis.DepthFirstAdapter;
import soot.jimple.parser.node.AFullIdentClassName;
import soot.jimple.parser.node.AFullIdentNonvoidType;
import soot.jimple.parser.node.AIdentClassName;
import soot.jimple.parser.node.AIdentNonvoidType;
import soot.jimple.parser.node.AQuotedClassName;
import soot.jimple.parser.node.AQuotedNonvoidType;
import soot.jimple.parser.node.Start;
import soot.util.StringTools;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/CstPoolExtractor.class */
public class CstPoolExtractor {
    private Set<String> mRefTypes = null;
    private Start mParseTree;

    public CstPoolExtractor(Start parseTree) {
        this.mParseTree = parseTree;
    }

    public Set<String> getCstPool() {
        if (this.mRefTypes == null) {
            this.mRefTypes = new HashSet();
            CstPoolExtractorWalker walker = new CstPoolExtractorWalker();
            this.mParseTree.apply(walker);
            this.mParseTree = null;
        }
        return this.mRefTypes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/parser/CstPoolExtractor$CstPoolExtractorWalker.class */
    public class CstPoolExtractorWalker extends DepthFirstAdapter {
        CstPoolExtractorWalker() {
        }

        @Override // soot.jimple.parser.analysis.DepthFirstAdapter
        public void inStart(Start node) {
            defaultIn(node);
        }

        @Override // soot.jimple.parser.analysis.DepthFirstAdapter
        public void outAQuotedClassName(AQuotedClassName node) {
            String tokenString = node.getQuotedName().getText();
            CstPoolExtractor.this.mRefTypes.add(StringTools.getUnEscapedStringOf(tokenString.substring(1, tokenString.length() - 1)));
        }

        @Override // soot.jimple.parser.analysis.DepthFirstAdapter
        public void outAIdentClassName(AIdentClassName node) {
            String tokenString = node.getIdentifier().getText();
            CstPoolExtractor.this.mRefTypes.add(StringTools.getUnEscapedStringOf(tokenString));
        }

        @Override // soot.jimple.parser.analysis.DepthFirstAdapter
        public void outAFullIdentClassName(AFullIdentClassName node) {
            String tokenString = node.getFullIdentifier().getText();
            Scene.v();
            CstPoolExtractor.this.mRefTypes.add(StringTools.getUnEscapedStringOf(Scene.unescapeName(tokenString)));
        }

        @Override // soot.jimple.parser.analysis.DepthFirstAdapter
        public void outAQuotedNonvoidType(AQuotedNonvoidType node) {
            String tokenString = node.getQuotedName().getText();
            CstPoolExtractor.this.mRefTypes.add(StringTools.getUnEscapedStringOf(tokenString.substring(1, tokenString.length() - 1)));
        }

        @Override // soot.jimple.parser.analysis.DepthFirstAdapter
        public void outAFullIdentNonvoidType(AFullIdentNonvoidType node) {
            String tokenString = node.getFullIdentifier().getText();
            Scene.v();
            CstPoolExtractor.this.mRefTypes.add(StringTools.getUnEscapedStringOf(Scene.unescapeName(tokenString)));
        }

        @Override // soot.jimple.parser.analysis.DepthFirstAdapter
        public void outAIdentNonvoidType(AIdentNonvoidType node) {
            String tokenString = node.getIdentifier().getText();
            CstPoolExtractor.this.mRefTypes.add(StringTools.getUnEscapedStringOf(tokenString));
        }
    }
}
