package soot.javaToJimple.jj;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.frontend.Source;
import polyglot.main.Options;
import polyglot.types.TypeSystem;
import soot.javaToJimple.jj.ast.JjNodeFactory_c;
import soot.javaToJimple.jj.types.JjTypeSystem_c;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/jj/ExtensionInfo.class */
public class ExtensionInfo extends polyglot.ext.jl.ExtensionInfo {
    private HashMap<Source, Job> sourceJobMap;

    static {
        new Topics();
    }

    @Override // polyglot.ext.jl.ExtensionInfo, polyglot.frontend.ExtensionInfo
    public String defaultFileExtension() {
        return Topics.jj;
    }

    @Override // polyglot.ext.jl.ExtensionInfo, polyglot.frontend.ExtensionInfo
    public String compilerName() {
        return "jjc";
    }

    @Override // polyglot.ext.jl.ExtensionInfo, polyglot.frontend.AbstractExtensionInfo
    protected NodeFactory createNodeFactory() {
        return new JjNodeFactory_c();
    }

    @Override // polyglot.ext.jl.ExtensionInfo, polyglot.frontend.AbstractExtensionInfo
    protected TypeSystem createTypeSystem() {
        return new JjTypeSystem_c();
    }

    @Override // polyglot.ext.jl.ExtensionInfo, polyglot.frontend.AbstractExtensionInfo, polyglot.frontend.ExtensionInfo
    public List passes(Job job) {
        List passes = super.passes(job);
        return passes;
    }

    public HashMap<Source, Job> sourceJobMap() {
        return this.sourceJobMap;
    }

    public void sourceJobMap(HashMap<Source, Job> map) {
        this.sourceJobMap = map;
    }

    @Override // polyglot.frontend.AbstractExtensionInfo
    protected Options createOptions() {
        return new Options(this) { // from class: soot.javaToJimple.jj.ExtensionInfo.1
            @Override // polyglot.main.Options
            public String constructFullClasspath() {
                String cp = super.constructFullClasspath();
                return String.valueOf(cp) + File.pathSeparator + soot.options.Options.v().soot_classpath();
            }
        };
    }
}
