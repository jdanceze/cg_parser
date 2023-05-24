package polyglot.ast;

import java.util.List;
import polyglot.frontend.Source;
import polyglot.types.ImportTable;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/SourceFile.class */
public interface SourceFile extends Node {
    PackageNode package_();

    SourceFile package_(PackageNode packageNode);

    List imports();

    SourceFile imports(List list);

    List decls();

    SourceFile decls(List list);

    ImportTable importTable();

    SourceFile importTable(ImportTable importTable);

    Source source();

    SourceFile source(Source source);
}
