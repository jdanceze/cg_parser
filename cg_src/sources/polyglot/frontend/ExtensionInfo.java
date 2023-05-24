package polyglot.frontend;

import java.io.File;
import java.io.Reader;
import java.util.List;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Pass;
import polyglot.main.Options;
import polyglot.main.Version;
import polyglot.types.Context;
import polyglot.types.TypeSystem;
import polyglot.types.reflect.ClassFile;
import polyglot.util.ErrorQueue;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/ExtensionInfo.class */
public interface ExtensionInfo {
    String compilerName();

    Version version();

    Options getOptions();

    Stats getStats();

    void initCompiler(Compiler compiler);

    Compiler compiler();

    String[] fileExtensions();

    String[] defaultFileExtensions();

    String defaultFileExtension();

    TypeSystem typeSystem();

    NodeFactory nodeFactory();

    SourceLoader sourceLoader();

    void addDependencyToCurrentJob(Source source);

    SourceJob addJob(Source source);

    SourceJob addJob(Source source, Node node);

    Job spawnJob(Context context, Node node, Job job, Pass.ID id, Pass.ID id2);

    boolean runToCompletion();

    boolean runToPass(Job job, Pass.ID id) throws CyclicDependencyException;

    boolean runAllPasses(Job job);

    boolean readSource(FileSource fileSource);

    TargetFactory targetFactory();

    Parser parser(Reader reader, FileSource fileSource, ErrorQueue errorQueue);

    List passes(Job job);

    List passes(Job job, Pass.ID id, Pass.ID id2);

    void beforePass(List list, Pass.ID id, Pass pass);

    void beforePass(List list, Pass.ID id, List list2);

    void afterPass(List list, Pass.ID id, Pass pass);

    void afterPass(List list, Pass.ID id, List list2);

    void replacePass(List list, Pass.ID id, Pass pass);

    void replacePass(List list, Pass.ID id, List list2);

    void removePass(List list, Pass.ID id);

    ClassFile createClassFile(File file, byte[] bArr);
}
