package soot;

import android.os.DropBoxManager;
import com.sun.xml.fastinfoset.EncodingConstants;
import java.util.ArrayList;
import java.util.List;
import javax.resource.spi.work.WorkException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Definer;
import org.apache.tools.ant.taskdefs.MatchingTask;
import org.apache.tools.ant.taskdefs.optional.sos.SOSCmd;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.util.JavaEnvUtils;
import soot.javaToJimple.jj.Topics;
import soot.jimple.Jimple;
import soot.shimple.Shimple;
/* loaded from: gencallgraphv3.jar:soot/AntTask.class */
public class AntTask extends MatchingTask {
    public static final boolean DEBUG = true;
    private ArrayList args = new ArrayList();
    private List phaseopts = new ArrayList();
    private Path phase_help = null;
    private Path process_dir = null;
    private Path process_jar_dir = null;
    private Path dump_body = null;
    private Path dump_cfg = null;
    private Path plugin = null;
    private Path include = null;
    private Path exclude = null;
    private Path dynamic_class = null;
    private Path dynamic_dir = null;
    private Path dynamic_package = null;

    private void debug(String s) {
        System.err.println(s);
    }

    public List args() {
        return this.args;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addArg(String s) {
        this.args.add(s);
    }

    private void addArg(String s, String s2) {
        this.args.add(s);
        this.args.add(s2);
    }

    private Path appendToPath(Path old, Path newPath) {
        if (old == null) {
            return newPath;
        }
        old.append(newPath);
        return old;
    }

    private void addPath(String option, Path path) {
        if (path.size() == 0) {
            return;
        }
        addArg(option);
        addArg(path.toString());
    }

    @Override // org.apache.tools.ant.Task
    public void execute() throws BuildException {
        if (this.phase_help != null) {
            addPath("-phase-help", this.phase_help);
        }
        if (this.process_dir != null) {
            addPath("-process-dir", this.process_dir);
        }
        if (this.process_jar_dir != null) {
            addPath("-process-jar-dir", this.process_jar_dir);
        }
        if (this.dump_body != null) {
            addPath("-dump-body", this.dump_body);
        }
        if (this.dump_cfg != null) {
            addPath("-dump-cfg", this.dump_cfg);
        }
        if (this.plugin != null) {
            addPath("-plugin", this.plugin);
        }
        if (this.include != null) {
            addPath("-include", this.include);
        }
        if (this.exclude != null) {
            addPath("-exclude", this.exclude);
        }
        if (this.dynamic_class != null) {
            addPath("-dynamic-class", this.dynamic_class);
        }
        if (this.dynamic_dir != null) {
            addPath("-dynamic-dir", this.dynamic_dir);
        }
        if (this.dynamic_package != null) {
            addPath("-dynamic-package", this.dynamic_package);
        }
        System.out.println(this.args);
        try {
            Main.main((String[]) this.args.toArray(new String[0]));
            G.v();
            G.reset();
        } catch (Exception e) {
            e.printStackTrace();
            throw new BuildException(e);
        }
    }

    public void setcoffi(boolean arg) {
        if (arg) {
            addArg("-coffi");
        }
    }

    public void setjasmin_backend(boolean arg) {
        if (arg) {
            addArg("-jasmin-backend");
        }
    }

    public void sethelp(boolean arg) {
        if (arg) {
            addArg("-help");
        }
    }

    public void setphase_list(boolean arg) {
        if (arg) {
            addArg("-phase-list");
        }
    }

    public void setphase_help(Path arg) {
        if (this.phase_help == null) {
            this.phase_help = new Path(getProject());
        }
        this.phase_help = appendToPath(this.phase_help, arg);
    }

    public Path createphase_help() {
        if (this.phase_help == null) {
            this.phase_help = new Path(getProject());
        }
        return this.phase_help.createPath();
    }

    public void setversion(boolean arg) {
        if (arg) {
            addArg("-version");
        }
    }

    public void setverbose(boolean arg) {
        if (arg) {
            addArg(SOSCmd.FLAG_VERBOSE);
        }
    }

    public void setinteractive_mode(boolean arg) {
        if (arg) {
            addArg("-interactive-mode");
        }
    }

    public void setunfriendly_mode(boolean arg) {
        if (arg) {
            addArg("-unfriendly-mode");
        }
    }

    public void setapp(boolean arg) {
        if (arg) {
            addArg("-app");
        }
    }

    public void setwhole_program(boolean arg) {
        if (arg) {
            addArg("-whole-program");
        }
    }

    public void setwhole_shimple(boolean arg) {
        if (arg) {
            addArg("-whole-shimple");
        }
    }

    public void seton_the_fly(boolean arg) {
        if (arg) {
            addArg("-on-the-fly");
        }
    }

    public void setvalidate(boolean arg) {
        if (arg) {
            addArg("-validate");
        }
    }

    public void setdebug(boolean arg) {
        if (arg) {
            addArg("-debug");
        }
    }

    public void setdebug_resolver(boolean arg) {
        if (arg) {
            addArg("-debug-resolver");
        }
    }

    public void setignore_resolving_levels(boolean arg) {
        if (arg) {
            addArg("-ignore-resolving-levels");
        }
    }

    public void setweak_map_structures(boolean arg) {
        if (arg) {
            addArg("-weak-map-structures");
        }
    }

    public void setsoot_classpath(String arg) {
        addArg("-soot-classpath");
        addArg(arg);
    }

    public void setsoot_modulepath(String arg) {
        addArg("-soot-modulepath");
        addArg(arg);
    }

    public void setdotnet_nativehost_path(String arg) {
        addArg("-dotnet-nativehost-path");
        addArg(arg);
    }

    public void setprepend_classpath(boolean arg) {
        if (arg) {
            addArg("-prepend-classpath");
        }
    }

    public void setignore_classpath_errors(boolean arg) {
        if (arg) {
            addArg("-ignore-classpath-errors");
        }
    }

    public void setprocess_multiple_dex(boolean arg) {
        if (arg) {
            addArg("-process-multiple-dex");
        }
    }

    public void setsearch_dex_in_archives(boolean arg) {
        if (arg) {
            addArg("-search-dex-in-archives");
        }
    }

    public void setprocess_dir(Path arg) {
        if (this.process_dir == null) {
            this.process_dir = new Path(getProject());
        }
        this.process_dir = appendToPath(this.process_dir, arg);
    }

    public Path createprocess_dir() {
        if (this.process_dir == null) {
            this.process_dir = new Path(getProject());
        }
        return this.process_dir.createPath();
    }

    public void setprocess_jar_dir(Path arg) {
        if (this.process_jar_dir == null) {
            this.process_jar_dir = new Path(getProject());
        }
        this.process_jar_dir = appendToPath(this.process_jar_dir, arg);
    }

    public Path createprocess_jar_dir() {
        if (this.process_jar_dir == null) {
            this.process_jar_dir = new Path(getProject());
        }
        return this.process_jar_dir.createPath();
    }

    public void setderive_java_version(boolean arg) {
        if (arg) {
            addArg("-derive-java-version");
        }
    }

    public void setoaat(boolean arg) {
        if (arg) {
            addArg("-oaat");
        }
    }

    public void setandroid_jars(String arg) {
        addArg("-android-jars");
        addArg(arg);
    }

    public void setforce_android_jar(String arg) {
        addArg("-force-android-jar");
        addArg(arg);
    }

    public void setast_metrics(boolean arg) {
        if (arg) {
            addArg("-ast-metrics");
        }
    }

    public void setsrc_prec(String arg) {
        if (arg.equals("c") || arg.equals("class") || arg.equals("only-class") || arg.equals("J") || arg.equals("jimple") || arg.equals("java") || arg.equals("apk") || arg.equals("apk-class-jimple") || arg.equals("apk-c-j") || arg.equals("dotnet")) {
            addArg("-src-prec");
            addArg(arg);
            return;
        }
        throw new BuildException("Bad value " + arg + " for option src_prec");
    }

    public void setfull_resolver(boolean arg) {
        if (arg) {
            addArg("-full-resolver");
        }
    }

    public void setignore_methodsource_error(boolean arg) {
        if (arg) {
            addArg("-ignore-methodsource-error");
        }
    }

    public void setresolve_all_dotnet_methods(boolean arg) {
        if (arg) {
            addArg("-resolve-all-dotnet-methods");
        }
    }

    public void setallow_phantom_refs(boolean arg) {
        if (arg) {
            addArg("-allow-phantom-refs");
        }
    }

    public void setallow_phantom_elms(boolean arg) {
        if (arg) {
            addArg("-allow-phantom-elms");
        }
    }

    public void setallow_cg_errors(boolean arg) {
        if (arg) {
            addArg("-allow-cg-errors");
        }
    }

    public void setno_bodies_for_excluded(boolean arg) {
        if (arg) {
            addArg("-no-bodies-for-excluded");
        }
    }

    public void setj2me(boolean arg) {
        if (arg) {
            addArg("-j2me");
        }
    }

    public void setmain_class(String arg) {
        addArg("-main-class");
        addArg(arg);
    }

    public void setpolyglot(boolean arg) {
        if (arg) {
            addArg("-polyglot");
        }
    }

    public void setpermissive_resolving(boolean arg) {
        if (arg) {
            addArg("-permissive-resolving");
        }
    }

    public void setdrop_bodies_after_load(boolean arg) {
        if (arg) {
            addArg("-drop-bodies-after-load");
        }
    }

    public void setoutput_dir(String arg) {
        addArg("-output-dir");
        addArg(arg);
    }

    public void setoutput_format(String arg) {
        if (arg.equals("J") || arg.equals("jimple") || arg.equals("j") || arg.equals("jimp") || arg.equals("S") || arg.equals(Shimple.PHASE) || arg.equals("s") || arg.equals("shimp") || arg.equals("B") || arg.equals("baf") || arg.equals("b") || arg.equals("G") || arg.equals("grimple") || arg.equals("g") || arg.equals("grimp") || arg.equals("X") || arg.equals(EncodingConstants.XML_NAMESPACE_PREFIX) || arg.equals("dex") || arg.equals("force-dex") || arg.equals("n") || arg.equals("none") || arg.equals("jasmin") || arg.equals("c") || arg.equals("class") || arg.equals("d") || arg.equals("dava") || arg.equals("t") || arg.equals("template") || arg.equals("a") || arg.equals("asm")) {
            addArg("-output-format");
            addArg(arg);
            return;
        }
        throw new BuildException("Bad value " + arg + " for option output_format");
    }

    public void setjava_version(String arg) {
        if (arg.equals("default") || arg.equals("1.1") || arg.equals(WorkException.START_TIMED_OUT) || arg.equals("1.2") || arg.equals(WorkException.TX_CONCURRENT_WORK_DISALLOWED) || arg.equals("1.3") || arg.equals(WorkException.TX_RECREATE_FAILED) || arg.equals("1.4") || arg.equals("4") || arg.equals(JavaEnvUtils.JAVA_1_5) || arg.equals("5") || arg.equals(JavaEnvUtils.JAVA_1_6) || arg.equals("6") || arg.equals(JavaEnvUtils.JAVA_1_7) || arg.equals("7") || arg.equals(JavaEnvUtils.JAVA_1_8) || arg.equals("8") || arg.equals(JavaEnvUtils.JAVA_1_9) || arg.equals(JavaEnvUtils.JAVA_9) || arg.equals("1.10") || arg.equals(JavaEnvUtils.JAVA_10) || arg.equals("1.11") || arg.equals(JavaEnvUtils.JAVA_11) || arg.equals("1.12") || arg.equals(JavaEnvUtils.JAVA_12)) {
            addArg("-java-version");
            addArg(arg);
            return;
        }
        throw new BuildException("Bad value " + arg + " for option java_version");
    }

    public void setoutput_jar(boolean arg) {
        if (arg) {
            addArg("-output-jar");
        }
    }

    public void sethierarchy_dirs(boolean arg) {
        if (arg) {
            addArg("-hierarchy-dirs");
        }
    }

    public void setxml_attributes(boolean arg) {
        if (arg) {
            addArg("-xml-attributes");
        }
    }

    public void setprint_tags_in_output(boolean arg) {
        if (arg) {
            addArg("-print-tags-in-output");
        }
    }

    public void setno_output_source_file_attribute(boolean arg) {
        if (arg) {
            addArg("-no-output-source-file-attribute");
        }
    }

    public void setno_output_inner_classes_attribute(boolean arg) {
        if (arg) {
            addArg("-no-output-inner-classes-attribute");
        }
    }

    public void setdump_body(Path arg) {
        if (this.dump_body == null) {
            this.dump_body = new Path(getProject());
        }
        this.dump_body = appendToPath(this.dump_body, arg);
    }

    public Path createdump_body() {
        if (this.dump_body == null) {
            this.dump_body = new Path(getProject());
        }
        return this.dump_body.createPath();
    }

    public void setdump_cfg(Path arg) {
        if (this.dump_cfg == null) {
            this.dump_cfg = new Path(getProject());
        }
        this.dump_cfg = appendToPath(this.dump_cfg, arg);
    }

    public Path createdump_cfg() {
        if (this.dump_cfg == null) {
            this.dump_cfg = new Path(getProject());
        }
        return this.dump_cfg.createPath();
    }

    public void setshow_exception_dests(boolean arg) {
        if (arg) {
            addArg("-show-exception-dests");
        }
    }

    public void setgzip(boolean arg) {
        if (arg) {
            addArg("-gzip");
        }
    }

    public void setforce_overwrite(boolean arg) {
        if (arg) {
            addArg("-force-overwrite");
        }
    }

    public void setplugin(Path arg) {
        if (this.plugin == null) {
            this.plugin = new Path(getProject());
        }
        this.plugin = appendToPath(this.plugin, arg);
    }

    public Path createplugin() {
        if (this.plugin == null) {
            this.plugin = new Path(getProject());
        }
        return this.plugin.createPath();
    }

    public void setwrong_staticness(String arg) {
        if (arg.equals("fail") || arg.equals(Definer.OnError.POLICY_IGNORE) || arg.equals("fix") || arg.equals("fixstrict")) {
            addArg("-wrong-staticness");
            addArg(arg);
            return;
        }
        throw new BuildException("Bad value " + arg + " for option wrong_staticness");
    }

    public void setfield_type_mismatches(String arg) {
        if (arg.equals("fail") || arg.equals(Definer.OnError.POLICY_IGNORE) || arg.equals(Jimple.NULL)) {
            addArg("-field-type-mismatches");
            addArg(arg);
            return;
        }
        throw new BuildException("Bad value " + arg + " for option field_type_mismatches");
    }

    public void setoptimize(boolean arg) {
        if (arg) {
            addArg("-optimize");
        }
    }

    public void setwhole_optimize(boolean arg) {
        if (arg) {
            addArg("-whole-optimize");
        }
    }

    public void setvia_grimp(boolean arg) {
        if (arg) {
            addArg("-via-grimp");
        }
    }

    public void setvia_shimple(boolean arg) {
        if (arg) {
            addArg("-via-shimple");
        }
    }

    public void setthrow_analysis(String arg) {
        if (arg.equals("pedantic") || arg.equals("unit") || arg.equals("dalvik") || arg.equals("dotnet") || arg.equals("auto-select")) {
            addArg("-throw-analysis");
            addArg(arg);
            return;
        }
        throw new BuildException("Bad value " + arg + " for option throw_analysis");
    }

    public void setcheck_init_throw_analysis(String arg) {
        if (arg.equals("auto") || arg.equals("pedantic") || arg.equals("unit") || arg.equals("dalvik") || arg.equals("dotnet")) {
            addArg("-check-init-throw-analysis");
            addArg(arg);
            return;
        }
        throw new BuildException("Bad value " + arg + " for option check_init_throw_analysis");
    }

    public void setomit_excepting_unit_edges(boolean arg) {
        if (arg) {
            addArg("-omit-excepting-unit-edges");
        }
    }

    public void settrim_cfgs(boolean arg) {
        if (arg) {
            addArg("-trim-cfgs");
        }
    }

    public void setignore_resolution_errors(boolean arg) {
        if (arg) {
            addArg("-ignore-resolution-errors");
        }
    }

    public void setinclude(Path arg) {
        if (this.include == null) {
            this.include = new Path(getProject());
        }
        this.include = appendToPath(this.include, arg);
    }

    public Path createinclude() {
        if (this.include == null) {
            this.include = new Path(getProject());
        }
        return this.include.createPath();
    }

    public void setexclude(Path arg) {
        if (this.exclude == null) {
            this.exclude = new Path(getProject());
        }
        this.exclude = appendToPath(this.exclude, arg);
    }

    public Path createexclude() {
        if (this.exclude == null) {
            this.exclude = new Path(getProject());
        }
        return this.exclude.createPath();
    }

    public void setinclude_all(boolean arg) {
        if (arg) {
            addArg("-include-all");
        }
    }

    public void setdynamic_class(Path arg) {
        if (this.dynamic_class == null) {
            this.dynamic_class = new Path(getProject());
        }
        this.dynamic_class = appendToPath(this.dynamic_class, arg);
    }

    public Path createdynamic_class() {
        if (this.dynamic_class == null) {
            this.dynamic_class = new Path(getProject());
        }
        return this.dynamic_class.createPath();
    }

    public void setdynamic_dir(Path arg) {
        if (this.dynamic_dir == null) {
            this.dynamic_dir = new Path(getProject());
        }
        this.dynamic_dir = appendToPath(this.dynamic_dir, arg);
    }

    public Path createdynamic_dir() {
        if (this.dynamic_dir == null) {
            this.dynamic_dir = new Path(getProject());
        }
        return this.dynamic_dir.createPath();
    }

    public void setdynamic_package(Path arg) {
        if (this.dynamic_package == null) {
            this.dynamic_package = new Path(getProject());
        }
        this.dynamic_package = appendToPath(this.dynamic_package, arg);
    }

    public Path createdynamic_package() {
        if (this.dynamic_package == null) {
            this.dynamic_package = new Path(getProject());
        }
        return this.dynamic_package.createPath();
    }

    public void setkeep_line_number(boolean arg) {
        if (arg) {
            addArg("-keep-line-number");
        }
    }

    public void setkeep_offset(boolean arg) {
        if (arg) {
            addArg("-keep-offset");
        }
    }

    public void setwrite_local_annotations(boolean arg) {
        if (arg) {
            addArg("-write-local-annotations");
        }
    }

    public void setannot_purity(boolean arg) {
        if (arg) {
            addArg("-annot-purity");
        }
    }

    public void setannot_nullpointer(boolean arg) {
        if (arg) {
            addArg("-annot-nullpointer");
        }
    }

    public void setannot_arraybounds(boolean arg) {
        if (arg) {
            addArg("-annot-arraybounds");
        }
    }

    public void setannot_side_effect(boolean arg) {
        if (arg) {
            addArg("-annot-side-effect");
        }
    }

    public void setannot_fieldrw(boolean arg) {
        if (arg) {
            addArg("-annot-fieldrw");
        }
    }

    public void settime(boolean arg) {
        if (arg) {
            addArg("-time");
        }
    }

    public void setsubtract_gc(boolean arg) {
        if (arg) {
            addArg("-subtract-gc");
        }
    }

    public void setno_writeout_body_releasing(boolean arg) {
        if (arg) {
            addArg("-no-writeout-body-releasing");
        }
    }

    public Object createp_jb() {
        PhaseOptjb phaseOptjb = new PhaseOptjb();
        this.phaseopts.add(phaseOptjb);
        return phaseOptjb;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb.class */
    public class PhaseOptjb {
        public PhaseOptjb() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setuse_original_names(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb");
            AntTask.this.addArg("use-original-names:" + (arg ? "true" : "false"));
        }

        public void setpreserve_source_annotations(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb");
            AntTask.this.addArg("preserve-source-annotations:" + (arg ? "true" : "false"));
        }

        public void setstabilize_local_names(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb");
            AntTask.this.addArg("stabilize-local-names:" + (arg ? "true" : "false"));
        }

        public void setmodel_lambdametafactory(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb");
            AntTask.this.addArg("model-lambdametafactory:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_dtr() {
        PhaseOptjb_dtr phaseOptjb_dtr = new PhaseOptjb_dtr();
        this.phaseopts.add(phaseOptjb_dtr);
        return phaseOptjb_dtr;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_dtr.class */
    public class PhaseOptjb_dtr {
        public PhaseOptjb_dtr() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.dtr");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_ese() {
        PhaseOptjb_ese phaseOptjb_ese = new PhaseOptjb_ese();
        this.phaseopts.add(phaseOptjb_ese);
        return phaseOptjb_ese;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_ese.class */
    public class PhaseOptjb_ese {
        public PhaseOptjb_ese() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.ese");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_ls() {
        PhaseOptjb_ls phaseOptjb_ls = new PhaseOptjb_ls();
        this.phaseopts.add(phaseOptjb_ls);
        return phaseOptjb_ls;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_ls.class */
    public class PhaseOptjb_ls {
        public PhaseOptjb_ls() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.ls");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_sils() {
        PhaseOptjb_sils phaseOptjb_sils = new PhaseOptjb_sils();
        this.phaseopts.add(phaseOptjb_sils);
        return phaseOptjb_sils;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_sils.class */
    public class PhaseOptjb_sils {
        public PhaseOptjb_sils() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.sils");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_a() {
        PhaseOptjb_a phaseOptjb_a = new PhaseOptjb_a();
        this.phaseopts.add(phaseOptjb_a);
        return phaseOptjb_a;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_a.class */
    public class PhaseOptjb_a {
        public PhaseOptjb_a() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.a");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.a");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_ule() {
        PhaseOptjb_ule phaseOptjb_ule = new PhaseOptjb_ule();
        this.phaseopts.add(phaseOptjb_ule);
        return phaseOptjb_ule;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_ule.class */
    public class PhaseOptjb_ule {
        public PhaseOptjb_ule() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.ule");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_tr() {
        PhaseOptjb_tr phaseOptjb_tr = new PhaseOptjb_tr();
        this.phaseopts.add(phaseOptjb_tr);
        return phaseOptjb_tr;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_tr.class */
    public class PhaseOptjb_tr {
        public PhaseOptjb_tr() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.tr");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setuse_older_type_assigner(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.tr");
            AntTask.this.addArg("use-older-type-assigner:" + (arg ? "true" : "false"));
        }

        public void setcompare_type_assigners(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.tr");
            AntTask.this.addArg("compare-type-assigners:" + (arg ? "true" : "false"));
        }

        public void setignore_nullpointer_dereferences(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.tr");
            AntTask.this.addArg("ignore-nullpointer-dereferences:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_ulp() {
        PhaseOptjb_ulp phaseOptjb_ulp = new PhaseOptjb_ulp();
        this.phaseopts.add(phaseOptjb_ulp);
        return phaseOptjb_ulp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_ulp.class */
    public class PhaseOptjb_ulp {
        public PhaseOptjb_ulp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.ulp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setunsplit_original_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.ulp");
            AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_lns() {
        PhaseOptjb_lns phaseOptjb_lns = new PhaseOptjb_lns();
        this.phaseopts.add(phaseOptjb_lns);
        return phaseOptjb_lns;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_lns.class */
    public class PhaseOptjb_lns {
        public PhaseOptjb_lns() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.lns");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.lns");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }

        public void setsort_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.lns");
            AntTask.this.addArg("sort-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_cp() {
        PhaseOptjb_cp phaseOptjb_cp = new PhaseOptjb_cp();
        this.phaseopts.add(phaseOptjb_cp);
        return phaseOptjb_cp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_cp.class */
    public class PhaseOptjb_cp {
        public PhaseOptjb_cp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.cp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_regular_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.cp");
            AntTask.this.addArg("only-regular-locals:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.cp");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_dae() {
        PhaseOptjb_dae phaseOptjb_dae = new PhaseOptjb_dae();
        this.phaseopts.add(phaseOptjb_dae);
        return phaseOptjb_dae;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_dae.class */
    public class PhaseOptjb_dae {
        public PhaseOptjb_dae() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.dae");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.dae");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_cp_ule() {
        PhaseOptjb_cp_ule phaseOptjb_cp_ule = new PhaseOptjb_cp_ule();
        this.phaseopts.add(phaseOptjb_cp_ule);
        return phaseOptjb_cp_ule;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_cp_ule.class */
    public class PhaseOptjb_cp_ule {
        public PhaseOptjb_cp_ule() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.cp-ule");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_lp() {
        PhaseOptjb_lp phaseOptjb_lp = new PhaseOptjb_lp();
        this.phaseopts.add(phaseOptjb_lp);
        return phaseOptjb_lp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_lp.class */
    public class PhaseOptjb_lp {
        public PhaseOptjb_lp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.lp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setunsplit_original_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.lp");
            AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_ne() {
        PhaseOptjb_ne phaseOptjb_ne = new PhaseOptjb_ne();
        this.phaseopts.add(phaseOptjb_ne);
        return phaseOptjb_ne;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_ne.class */
    public class PhaseOptjb_ne {
        public PhaseOptjb_ne() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.ne");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_uce() {
        PhaseOptjb_uce phaseOptjb_uce = new PhaseOptjb_uce();
        this.phaseopts.add(phaseOptjb_uce);
        return phaseOptjb_uce;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_uce.class */
    public class PhaseOptjb_uce {
        public PhaseOptjb_uce() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.uce");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setremove_unreachable_traps(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.uce");
            AntTask.this.addArg("remove-unreachable-traps:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_tt() {
        PhaseOptjb_tt phaseOptjb_tt = new PhaseOptjb_tt();
        this.phaseopts.add(phaseOptjb_tt);
        return phaseOptjb_tt;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_tt.class */
    public class PhaseOptjb_tt {
        public PhaseOptjb_tt() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.tt");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jb_cbf() {
        PhaseOptjb_cbf phaseOptjb_cbf = new PhaseOptjb_cbf();
        this.phaseopts.add(phaseOptjb_cbf);
        return phaseOptjb_cbf;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjb_cbf.class */
    public class PhaseOptjb_cbf {
        public PhaseOptjb_cbf() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jb.cbf");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj() {
        PhaseOptjj phaseOptjj = new PhaseOptjj();
        this.phaseopts.add(phaseOptjj);
        return phaseOptjj;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj.class */
    public class PhaseOptjj {
        public PhaseOptjj() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg(Topics.jj);
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setuse_original_names(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg(Topics.jj);
            AntTask.this.addArg("use-original-names:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_ls() {
        PhaseOptjj_ls phaseOptjj_ls = new PhaseOptjj_ls();
        this.phaseopts.add(phaseOptjj_ls);
        return phaseOptjj_ls;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_ls.class */
    public class PhaseOptjj_ls {
        public PhaseOptjj_ls() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.ls");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_sils() {
        PhaseOptjj_sils phaseOptjj_sils = new PhaseOptjj_sils();
        this.phaseopts.add(phaseOptjj_sils);
        return phaseOptjj_sils;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_sils.class */
    public class PhaseOptjj_sils {
        public PhaseOptjj_sils() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.sils");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_a() {
        PhaseOptjj_a phaseOptjj_a = new PhaseOptjj_a();
        this.phaseopts.add(phaseOptjj_a);
        return phaseOptjj_a;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_a.class */
    public class PhaseOptjj_a {
        public PhaseOptjj_a() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.a");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.a");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_ule() {
        PhaseOptjj_ule phaseOptjj_ule = new PhaseOptjj_ule();
        this.phaseopts.add(phaseOptjj_ule);
        return phaseOptjj_ule;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_ule.class */
    public class PhaseOptjj_ule {
        public PhaseOptjj_ule() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.ule");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_tr() {
        PhaseOptjj_tr phaseOptjj_tr = new PhaseOptjj_tr();
        this.phaseopts.add(phaseOptjj_tr);
        return phaseOptjj_tr;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_tr.class */
    public class PhaseOptjj_tr {
        public PhaseOptjj_tr() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.tr");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_ulp() {
        PhaseOptjj_ulp phaseOptjj_ulp = new PhaseOptjj_ulp();
        this.phaseopts.add(phaseOptjj_ulp);
        return phaseOptjj_ulp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_ulp.class */
    public class PhaseOptjj_ulp {
        public PhaseOptjj_ulp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.ulp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setunsplit_original_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.ulp");
            AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_lns() {
        PhaseOptjj_lns phaseOptjj_lns = new PhaseOptjj_lns();
        this.phaseopts.add(phaseOptjj_lns);
        return phaseOptjj_lns;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_lns.class */
    public class PhaseOptjj_lns {
        public PhaseOptjj_lns() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.lns");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.lns");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_cp() {
        PhaseOptjj_cp phaseOptjj_cp = new PhaseOptjj_cp();
        this.phaseopts.add(phaseOptjj_cp);
        return phaseOptjj_cp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_cp.class */
    public class PhaseOptjj_cp {
        public PhaseOptjj_cp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.cp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_regular_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.cp");
            AntTask.this.addArg("only-regular-locals:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.cp");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_dae() {
        PhaseOptjj_dae phaseOptjj_dae = new PhaseOptjj_dae();
        this.phaseopts.add(phaseOptjj_dae);
        return phaseOptjj_dae;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_dae.class */
    public class PhaseOptjj_dae {
        public PhaseOptjj_dae() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.dae");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.dae");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_cp_ule() {
        PhaseOptjj_cp_ule phaseOptjj_cp_ule = new PhaseOptjj_cp_ule();
        this.phaseopts.add(phaseOptjj_cp_ule);
        return phaseOptjj_cp_ule;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_cp_ule.class */
    public class PhaseOptjj_cp_ule {
        public PhaseOptjj_cp_ule() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.cp-ule");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_lp() {
        PhaseOptjj_lp phaseOptjj_lp = new PhaseOptjj_lp();
        this.phaseopts.add(phaseOptjj_lp);
        return phaseOptjj_lp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_lp.class */
    public class PhaseOptjj_lp {
        public PhaseOptjj_lp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.lp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setunsplit_original_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.lp");
            AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_ne() {
        PhaseOptjj_ne phaseOptjj_ne = new PhaseOptjj_ne();
        this.phaseopts.add(phaseOptjj_ne);
        return phaseOptjj_ne;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_ne.class */
    public class PhaseOptjj_ne {
        public PhaseOptjj_ne() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.ne");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jj_uce() {
        PhaseOptjj_uce phaseOptjj_uce = new PhaseOptjj_uce();
        this.phaseopts.add(phaseOptjj_uce);
        return phaseOptjj_uce;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjj_uce.class */
    public class PhaseOptjj_uce {
        public PhaseOptjj_uce() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jj.uce");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjpp() {
        PhaseOptwjpp phaseOptwjpp = new PhaseOptwjpp();
        this.phaseopts.add(phaseOptwjpp);
        return phaseOptwjpp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjpp.class */
    public class PhaseOptwjpp {
        public PhaseOptwjpp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjpp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjpp_cimbt() {
        PhaseOptwjpp_cimbt phaseOptwjpp_cimbt = new PhaseOptwjpp_cimbt();
        this.phaseopts.add(phaseOptwjpp_cimbt);
        return phaseOptwjpp_cimbt;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjpp_cimbt.class */
    public class PhaseOptwjpp_cimbt {
        public PhaseOptwjpp_cimbt() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjpp.cimbt");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setverbose(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjpp.cimbt");
            AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wspp() {
        PhaseOptwspp phaseOptwspp = new PhaseOptwspp();
        this.phaseopts.add(phaseOptwspp);
        return phaseOptwspp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwspp.class */
    public class PhaseOptwspp {
        public PhaseOptwspp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wspp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_cg() {
        PhaseOptcg phaseOptcg = new PhaseOptcg();
        this.phaseopts.add(phaseOptcg);
        return phaseOptcg;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptcg.class */
    public class PhaseOptcg {
        public PhaseOptcg() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setsafe_forname(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("safe-forname:" + (arg ? "true" : "false"));
        }

        public void setsafe_newinstance(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("safe-newinstance:" + (arg ? "true" : "false"));
        }

        public void setverbose(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
        }

        public void setall_reachable(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("all-reachable:" + (arg ? "true" : "false"));
        }

        public void setimplicit_entry(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("implicit-entry:" + (arg ? "true" : "false"));
        }

        public void settrim_clinit(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("trim-clinit:" + (arg ? "true" : "false"));
        }

        public void settypes_for_invoke(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("types-for-invoke:" + (arg ? "true" : "false"));
        }

        public void setresolve_all_abstract_invokes(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("resolve-all-abstract-invokes:" + (arg ? "true" : "false"));
        }

        public void setlibrary(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("library:" + arg);
        }

        public void setjdkver(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("jdkver:" + arg);
        }

        public void setreflection_log(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("reflection-log:" + arg);
        }

        public void setguards(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg");
            AntTask.this.addArg("guards:" + arg);
        }
    }

    public Object createp_cg_cha() {
        PhaseOptcg_cha phaseOptcg_cha = new PhaseOptcg_cha();
        this.phaseopts.add(phaseOptcg_cha);
        return phaseOptcg_cha;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptcg_cha.class */
    public class PhaseOptcg_cha {
        public PhaseOptcg_cha() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.cha");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setverbose(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.cha");
            AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
        }

        public void setapponly(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.cha");
            AntTask.this.addArg("apponly:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_cg_spark() {
        PhaseOptcg_spark phaseOptcg_spark = new PhaseOptcg_spark();
        this.phaseopts.add(phaseOptcg_spark);
        return phaseOptcg_spark;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptcg_spark.class */
    public class PhaseOptcg_spark {
        public PhaseOptcg_spark() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setverbose(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
        }

        public void setignore_types(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("ignore-types:" + (arg ? "true" : "false"));
        }

        public void setforce_gc(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("force-gc:" + (arg ? "true" : "false"));
        }

        public void setpre_jimplify(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("pre-jimplify:" + (arg ? "true" : "false"));
        }

        public void setapponly(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("apponly:" + (arg ? "true" : "false"));
        }

        public void setvta(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("vta:" + (arg ? "true" : "false"));
        }

        public void setrta(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("rta:" + (arg ? "true" : "false"));
        }

        public void setfield_based(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("field-based:" + (arg ? "true" : "false"));
        }

        public void settypes_for_sites(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("types-for-sites:" + (arg ? "true" : "false"));
        }

        public void setmerge_stringbuffer(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("merge-stringbuffer:" + (arg ? "true" : "false"));
        }

        public void setstring_constants(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("string-constants:" + (arg ? "true" : "false"));
        }

        public void setsimulate_natives(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("simulate-natives:" + (arg ? "true" : "false"));
        }

        public void setempties_as_allocs(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("empties-as-allocs:" + (arg ? "true" : "false"));
        }

        public void setsimple_edges_bidirectional(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("simple-edges-bidirectional:" + (arg ? "true" : "false"));
        }

        public void seton_fly_cg(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("on-fly-cg:" + (arg ? "true" : "false"));
        }

        public void setsimplify_offline(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("simplify-offline:" + (arg ? "true" : "false"));
        }

        public void setsimplify_sccs(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("simplify-sccs:" + (arg ? "true" : "false"));
        }

        public void setignore_types_for_sccs(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("ignore-types-for-sccs:" + (arg ? "true" : "false"));
        }

        public void setdump_html(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("dump-html:" + (arg ? "true" : "false"));
        }

        public void setdump_pag(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("dump-pag:" + (arg ? "true" : "false"));
        }

        public void setdump_solution(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("dump-solution:" + (arg ? "true" : "false"));
        }

        public void settopo_sort(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("topo-sort:" + (arg ? "true" : "false"));
        }

        public void setdump_types(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("dump-types:" + (arg ? "true" : "false"));
        }

        public void setclass_method_var(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("class-method-var:" + (arg ? "true" : "false"));
        }

        public void setdump_answer(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("dump-answer:" + (arg ? "true" : "false"));
        }

        public void setadd_tags(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("add-tags:" + (arg ? "true" : "false"));
        }

        public void setset_mass(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("set-mass:" + (arg ? "true" : "false"));
        }

        public void setcs_demand(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("cs-demand:" + (arg ? "true" : "false"));
        }

        public void setlazy_pts(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("lazy-pts:" + (arg ? "true" : "false"));
        }

        public void setgeom_pta(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-pta:" + (arg ? "true" : "false"));
        }

        public void setgeom_trans(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-trans:" + (arg ? "true" : "false"));
        }

        public void setgeom_blocking(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-blocking:" + (arg ? "true" : "false"));
        }

        public void setgeom_app_only(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-app-only:" + (arg ? "true" : "false"));
        }

        public void setpropagator(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("propagator:" + arg);
        }

        public void setset_impl(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("set-impl:" + arg);
        }

        public void setdouble_set_old(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("double-set-old:" + arg);
        }

        public void setdouble_set_new(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("double-set-new:" + arg);
        }

        public void settraversal(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("traversal:" + arg);
        }

        public void setpasses(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("passes:" + arg);
        }

        public void setgeom_encoding(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-encoding:" + arg);
        }

        public void setgeom_worklist(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-worklist:" + arg);
        }

        public void setgeom_dump_verbose(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-dump-verbose:" + arg);
        }

        public void setgeom_verify_name(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-verify-name:" + arg);
        }

        public void setgeom_eval(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-eval:" + arg);
        }

        public void setgeom_frac_base(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-frac-base:" + arg);
        }

        public void setgeom_runs(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.spark");
            AntTask.this.addArg("geom-runs:" + arg);
        }
    }

    public Object createp_cg_paddle() {
        PhaseOptcg_paddle phaseOptcg_paddle = new PhaseOptcg_paddle();
        this.phaseopts.add(phaseOptcg_paddle);
        return phaseOptcg_paddle;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptcg_paddle.class */
    public class PhaseOptcg_paddle {
        public PhaseOptcg_paddle() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setverbose(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
        }

        public void setbdd(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("bdd:" + (arg ? "true" : "false"));
        }

        public void setdynamic_order(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("dynamic-order:" + (arg ? "true" : "false"));
        }

        public void setprofile(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("profile:" + (arg ? "true" : "false"));
        }

        public void setverbosegc(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("verbosegc:" + (arg ? "true" : "false"));
        }

        public void setignore_types(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("ignore-types:" + (arg ? "true" : "false"));
        }

        public void setpre_jimplify(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("pre-jimplify:" + (arg ? "true" : "false"));
        }

        public void setcontext_heap(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("context-heap:" + (arg ? "true" : "false"));
        }

        public void setrta(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("rta:" + (arg ? "true" : "false"));
        }

        public void setfield_based(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("field-based:" + (arg ? "true" : "false"));
        }

        public void settypes_for_sites(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("types-for-sites:" + (arg ? "true" : "false"));
        }

        public void setmerge_stringbuffer(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("merge-stringbuffer:" + (arg ? "true" : "false"));
        }

        public void setstring_constants(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("string-constants:" + (arg ? "true" : "false"));
        }

        public void setsimulate_natives(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("simulate-natives:" + (arg ? "true" : "false"));
        }

        public void setglobal_nodes_in_natives(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("global-nodes-in-natives:" + (arg ? "true" : "false"));
        }

        public void setsimple_edges_bidirectional(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("simple-edges-bidirectional:" + (arg ? "true" : "false"));
        }

        public void setthis_edges(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("this-edges:" + (arg ? "true" : "false"));
        }

        public void setprecise_newinstance(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("precise-newinstance:" + (arg ? "true" : "false"));
        }

        public void setcontext_counts(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("context-counts:" + (arg ? "true" : "false"));
        }

        public void settotal_context_counts(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("total-context-counts:" + (arg ? "true" : "false"));
        }

        public void setmethod_context_counts(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("method-context-counts:" + (arg ? "true" : "false"));
        }

        public void setset_mass(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("set-mass:" + (arg ? "true" : "false"));
        }

        public void setnumber_nodes(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("number-nodes:" + (arg ? "true" : "false"));
        }

        public void setconf(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("conf:" + arg);
        }

        public void setorder(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("order:" + arg);
        }

        public void setq(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("q:" + arg);
        }

        public void setbackend(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("backend:" + arg);
        }

        public void setbdd_nodes(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("bdd-nodes:" + arg);
        }

        public void setcontext(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("context:" + arg);
        }

        public void setk(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("k:" + arg);
        }

        public void setpropagator(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("propagator:" + arg);
        }

        public void setset_impl(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("set-impl:" + arg);
        }

        public void setdouble_set_old(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("double-set-old:" + arg);
        }

        public void setdouble_set_new(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("cg.paddle");
            AntTask.this.addArg("double-set-new:" + arg);
        }
    }

    public Object createp_wstp() {
        PhaseOptwstp phaseOptwstp = new PhaseOptwstp();
        this.phaseopts.add(phaseOptwstp);
        return phaseOptwstp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwstp.class */
    public class PhaseOptwstp {
        public PhaseOptwstp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wstp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wsop() {
        PhaseOptwsop phaseOptwsop = new PhaseOptwsop();
        this.phaseopts.add(phaseOptwsop);
        return phaseOptwsop;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwsop.class */
    public class PhaseOptwsop {
        public PhaseOptwsop() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wsop");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjtp() {
        PhaseOptwjtp phaseOptwjtp = new PhaseOptwjtp();
        this.phaseopts.add(phaseOptwjtp);
        return phaseOptwjtp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjtp.class */
    public class PhaseOptwjtp {
        public PhaseOptwjtp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjtp_mhp() {
        PhaseOptwjtp_mhp phaseOptwjtp_mhp = new PhaseOptwjtp_mhp();
        this.phaseopts.add(phaseOptwjtp_mhp);
        return phaseOptwjtp_mhp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjtp_mhp.class */
    public class PhaseOptwjtp_mhp {
        public PhaseOptwjtp_mhp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.mhp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjtp_tn() {
        PhaseOptwjtp_tn phaseOptwjtp_tn = new PhaseOptwjtp_tn();
        this.phaseopts.add(phaseOptwjtp_tn);
        return phaseOptwjtp_tn;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjtp_tn.class */
    public class PhaseOptwjtp_tn {
        public PhaseOptwjtp_tn() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setavoid_deadlock(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("avoid-deadlock:" + (arg ? "true" : "false"));
        }

        public void setopen_nesting(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("open-nesting:" + (arg ? "true" : "false"));
        }

        public void setdo_mhp(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("do-mhp:" + (arg ? "true" : "false"));
        }

        public void setdo_tlo(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("do-tlo:" + (arg ? "true" : "false"));
        }

        public void setprint_graph(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("print-graph:" + (arg ? "true" : "false"));
        }

        public void setprint_table(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("print-table:" + (arg ? "true" : "false"));
        }

        public void setprint_debug(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("print-debug:" + (arg ? "true" : "false"));
        }

        public void setlocking_scheme(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.tn");
            AntTask.this.addArg("locking-scheme:" + arg);
        }
    }

    public Object createp_wjtp_rdc() {
        PhaseOptwjtp_rdc phaseOptwjtp_rdc = new PhaseOptwjtp_rdc();
        this.phaseopts.add(phaseOptwjtp_rdc);
        return phaseOptwjtp_rdc;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjtp_rdc.class */
    public class PhaseOptwjtp_rdc {
        public PhaseOptwjtp_rdc() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.rdc");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setfixed_class_names(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjtp.rdc");
            AntTask.this.addArg("fixed-class-names:" + arg);
        }
    }

    public Object createp_wjop() {
        PhaseOptwjop phaseOptwjop = new PhaseOptwjop();
        this.phaseopts.add(phaseOptwjop);
        return phaseOptwjop;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjop.class */
    public class PhaseOptwjop {
        public PhaseOptwjop() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjop_smb() {
        PhaseOptwjop_smb phaseOptwjop_smb = new PhaseOptwjop_smb();
        this.phaseopts.add(phaseOptwjop_smb);
        return phaseOptwjop_smb;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjop_smb.class */
    public class PhaseOptwjop_smb {
        public PhaseOptwjop_smb() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.smb");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setinsert_null_checks(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.smb");
            AntTask.this.addArg("insert-null-checks:" + (arg ? "true" : "false"));
        }

        public void setinsert_redundant_casts(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.smb");
            AntTask.this.addArg("insert-redundant-casts:" + (arg ? "true" : "false"));
        }

        public void setallowed_modifier_changes(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.smb");
            AntTask.this.addArg("allowed-modifier-changes:" + arg);
        }
    }

    public Object createp_wjop_si() {
        PhaseOptwjop_si phaseOptwjop_si = new PhaseOptwjop_si();
        this.phaseopts.add(phaseOptwjop_si);
        return phaseOptwjop_si;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjop_si.class */
    public class PhaseOptwjop_si {
        public PhaseOptwjop_si() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.si");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setrerun_jb(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.si");
            AntTask.this.addArg("rerun-jb:" + (arg ? "true" : "false"));
        }

        public void setinsert_null_checks(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.si");
            AntTask.this.addArg("insert-null-checks:" + (arg ? "true" : "false"));
        }

        public void setinsert_redundant_casts(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.si");
            AntTask.this.addArg("insert-redundant-casts:" + (arg ? "true" : "false"));
        }

        public void setallowed_modifier_changes(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.si");
            AntTask.this.addArg("allowed-modifier-changes:" + arg);
        }

        public void setexpansion_factor(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.si");
            AntTask.this.addArg("expansion-factor:" + arg);
        }

        public void setmax_container_size(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.si");
            AntTask.this.addArg("max-container-size:" + arg);
        }

        public void setmax_inlinee_size(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjop.si");
            AntTask.this.addArg("max-inlinee-size:" + arg);
        }
    }

    public Object createp_wjap() {
        PhaseOptwjap phaseOptwjap = new PhaseOptwjap();
        this.phaseopts.add(phaseOptwjap);
        return phaseOptwjap;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjap.class */
    public class PhaseOptwjap {
        public PhaseOptwjap() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjap_ra() {
        PhaseOptwjap_ra phaseOptwjap_ra = new PhaseOptwjap_ra();
        this.phaseopts.add(phaseOptwjap_ra);
        return phaseOptwjap_ra;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjap_ra.class */
    public class PhaseOptwjap_ra {
        public PhaseOptwjap_ra() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.ra");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjap_umt() {
        PhaseOptwjap_umt phaseOptwjap_umt = new PhaseOptwjap_umt();
        this.phaseopts.add(phaseOptwjap_umt);
        return phaseOptwjap_umt;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjap_umt.class */
    public class PhaseOptwjap_umt {
        public PhaseOptwjap_umt() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.umt");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjap_uft() {
        PhaseOptwjap_uft phaseOptwjap_uft = new PhaseOptwjap_uft();
        this.phaseopts.add(phaseOptwjap_uft);
        return phaseOptwjap_uft;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjap_uft.class */
    public class PhaseOptwjap_uft {
        public PhaseOptwjap_uft() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.uft");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjap_tqt() {
        PhaseOptwjap_tqt phaseOptwjap_tqt = new PhaseOptwjap_tqt();
        this.phaseopts.add(phaseOptwjap_tqt);
        return phaseOptwjap_tqt;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjap_tqt.class */
    public class PhaseOptwjap_tqt {
        public PhaseOptwjap_tqt() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.tqt");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjap_cgg() {
        PhaseOptwjap_cgg phaseOptwjap_cgg = new PhaseOptwjap_cgg();
        this.phaseopts.add(phaseOptwjap_cgg);
        return phaseOptwjap_cgg;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjap_cgg.class */
    public class PhaseOptwjap_cgg {
        public PhaseOptwjap_cgg() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.cgg");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setshow_lib_meths(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.cgg");
            AntTask.this.addArg("show-lib-meths:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_wjap_purity() {
        PhaseOptwjap_purity phaseOptwjap_purity = new PhaseOptwjap_purity();
        this.phaseopts.add(phaseOptwjap_purity);
        return phaseOptwjap_purity;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptwjap_purity.class */
    public class PhaseOptwjap_purity {
        public PhaseOptwjap_purity() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.purity");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setdump_summaries(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.purity");
            AntTask.this.addArg("dump-summaries:" + (arg ? "true" : "false"));
        }

        public void setdump_cg(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.purity");
            AntTask.this.addArg("dump-cg:" + (arg ? "true" : "false"));
        }

        public void setdump_intra(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.purity");
            AntTask.this.addArg("dump-intra:" + (arg ? "true" : "false"));
        }

        public void setprint(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.purity");
            AntTask.this.addArg("print:" + (arg ? "true" : "false"));
        }

        public void setannotate(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.purity");
            AntTask.this.addArg("annotate:" + (arg ? "true" : "false"));
        }

        public void setverbose(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("wjap.purity");
            AntTask.this.addArg("verbose:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_shimple() {
        PhaseOptshimple phaseOptshimple = new PhaseOptshimple();
        this.phaseopts.add(phaseOptshimple);
        return phaseOptshimple;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptshimple.class */
    public class PhaseOptshimple {
        public PhaseOptshimple() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg(Shimple.PHASE);
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setnode_elim_opt(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg(Shimple.PHASE);
            AntTask.this.addArg("node-elim-opt:" + (arg ? "true" : "false"));
        }

        public void setstandard_local_names(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg(Shimple.PHASE);
            AntTask.this.addArg("standard-local-names:" + (arg ? "true" : "false"));
        }

        public void setextended(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg(Shimple.PHASE);
            AntTask.this.addArg("extended:" + (arg ? "true" : "false"));
        }

        public void setdebug(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg(Shimple.PHASE);
            AntTask.this.addArg("debug:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_stp() {
        PhaseOptstp phaseOptstp = new PhaseOptstp();
        this.phaseopts.add(phaseOptstp);
        return phaseOptstp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptstp.class */
    public class PhaseOptstp {
        public PhaseOptstp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("stp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_sop() {
        PhaseOptsop phaseOptsop = new PhaseOptsop();
        this.phaseopts.add(phaseOptsop);
        return phaseOptsop;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptsop.class */
    public class PhaseOptsop {
        public PhaseOptsop() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("sop");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_sop_cpf() {
        PhaseOptsop_cpf phaseOptsop_cpf = new PhaseOptsop_cpf();
        this.phaseopts.add(phaseOptsop_cpf);
        return phaseOptsop_cpf;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptsop_cpf.class */
    public class PhaseOptsop_cpf {
        public PhaseOptsop_cpf() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("sop.cpf");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setprune_cfg(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("sop.cpf");
            AntTask.this.addArg("prune-cfg:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jtp() {
        PhaseOptjtp phaseOptjtp = new PhaseOptjtp();
        this.phaseopts.add(phaseOptjtp);
        return phaseOptjtp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjtp.class */
    public class PhaseOptjtp {
        public PhaseOptjtp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jtp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop() {
        PhaseOptjop phaseOptjop = new PhaseOptjop();
        this.phaseopts.add(phaseOptjop);
        return phaseOptjop;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop.class */
    public class PhaseOptjop {
        public PhaseOptjop() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_cse() {
        PhaseOptjop_cse phaseOptjop_cse = new PhaseOptjop_cse();
        this.phaseopts.add(phaseOptjop_cse);
        return phaseOptjop_cse;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_cse.class */
    public class PhaseOptjop_cse {
        public PhaseOptjop_cse() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.cse");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setnaive_side_effect(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.cse");
            AntTask.this.addArg("naive-side-effect:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_bcm() {
        PhaseOptjop_bcm phaseOptjop_bcm = new PhaseOptjop_bcm();
        this.phaseopts.add(phaseOptjop_bcm);
        return phaseOptjop_bcm;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_bcm.class */
    public class PhaseOptjop_bcm {
        public PhaseOptjop_bcm() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.bcm");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setnaive_side_effect(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.bcm");
            AntTask.this.addArg("naive-side-effect:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_lcm() {
        PhaseOptjop_lcm phaseOptjop_lcm = new PhaseOptjop_lcm();
        this.phaseopts.add(phaseOptjop_lcm);
        return phaseOptjop_lcm;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_lcm.class */
    public class PhaseOptjop_lcm {
        public PhaseOptjop_lcm() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.lcm");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setunroll(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.lcm");
            AntTask.this.addArg("unroll:" + (arg ? "true" : "false"));
        }

        public void setnaive_side_effect(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.lcm");
            AntTask.this.addArg("naive-side-effect:" + (arg ? "true" : "false"));
        }

        public void setsafety(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.lcm");
            AntTask.this.addArg("safety:" + arg);
        }
    }

    public Object createp_jop_cp() {
        PhaseOptjop_cp phaseOptjop_cp = new PhaseOptjop_cp();
        this.phaseopts.add(phaseOptjop_cp);
        return phaseOptjop_cp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_cp.class */
    public class PhaseOptjop_cp {
        public PhaseOptjop_cp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.cp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_regular_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.cp");
            AntTask.this.addArg("only-regular-locals:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.cp");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_cpf() {
        PhaseOptjop_cpf phaseOptjop_cpf = new PhaseOptjop_cpf();
        this.phaseopts.add(phaseOptjop_cpf);
        return phaseOptjop_cpf;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_cpf.class */
    public class PhaseOptjop_cpf {
        public PhaseOptjop_cpf() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.cpf");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_cbf() {
        PhaseOptjop_cbf phaseOptjop_cbf = new PhaseOptjop_cbf();
        this.phaseopts.add(phaseOptjop_cbf);
        return phaseOptjop_cbf;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_cbf.class */
    public class PhaseOptjop_cbf {
        public PhaseOptjop_cbf() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.cbf");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_dae() {
        PhaseOptjop_dae phaseOptjop_dae = new PhaseOptjop_dae();
        this.phaseopts.add(phaseOptjop_dae);
        return phaseOptjop_dae;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_dae.class */
    public class PhaseOptjop_dae {
        public PhaseOptjop_dae() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.dae");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_tag(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.dae");
            AntTask.this.addArg("only-tag:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.dae");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_nce() {
        PhaseOptjop_nce phaseOptjop_nce = new PhaseOptjop_nce();
        this.phaseopts.add(phaseOptjop_nce);
        return phaseOptjop_nce;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_nce.class */
    public class PhaseOptjop_nce {
        public PhaseOptjop_nce() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.nce");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_uce1() {
        PhaseOptjop_uce1 phaseOptjop_uce1 = new PhaseOptjop_uce1();
        this.phaseopts.add(phaseOptjop_uce1);
        return phaseOptjop_uce1;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_uce1.class */
    public class PhaseOptjop_uce1 {
        public PhaseOptjop_uce1() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.uce1");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setremove_unreachable_traps(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.uce1");
            AntTask.this.addArg("remove-unreachable-traps:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_ubf1() {
        PhaseOptjop_ubf1 phaseOptjop_ubf1 = new PhaseOptjop_ubf1();
        this.phaseopts.add(phaseOptjop_ubf1);
        return phaseOptjop_ubf1;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_ubf1.class */
    public class PhaseOptjop_ubf1 {
        public PhaseOptjop_ubf1() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.ubf1");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_uce2() {
        PhaseOptjop_uce2 phaseOptjop_uce2 = new PhaseOptjop_uce2();
        this.phaseopts.add(phaseOptjop_uce2);
        return phaseOptjop_uce2;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_uce2.class */
    public class PhaseOptjop_uce2 {
        public PhaseOptjop_uce2() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.uce2");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setremove_unreachable_traps(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.uce2");
            AntTask.this.addArg("remove-unreachable-traps:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_ubf2() {
        PhaseOptjop_ubf2 phaseOptjop_ubf2 = new PhaseOptjop_ubf2();
        this.phaseopts.add(phaseOptjop_ubf2);
        return phaseOptjop_ubf2;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_ubf2.class */
    public class PhaseOptjop_ubf2 {
        public PhaseOptjop_ubf2() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.ubf2");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jop_ule() {
        PhaseOptjop_ule phaseOptjop_ule = new PhaseOptjop_ule();
        this.phaseopts.add(phaseOptjop_ule);
        return phaseOptjop_ule;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjop_ule.class */
    public class PhaseOptjop_ule {
        public PhaseOptjop_ule() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jop.ule");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap() {
        PhaseOptjap phaseOptjap = new PhaseOptjap();
        this.phaseopts.add(phaseOptjap);
        return phaseOptjap;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap.class */
    public class PhaseOptjap {
        public PhaseOptjap() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_npc() {
        PhaseOptjap_npc phaseOptjap_npc = new PhaseOptjap_npc();
        this.phaseopts.add(phaseOptjap_npc);
        return phaseOptjap_npc;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_npc.class */
    public class PhaseOptjap_npc {
        public PhaseOptjap_npc() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.npc");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_array_ref(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.npc");
            AntTask.this.addArg("only-array-ref:" + (arg ? "true" : "false"));
        }

        public void setprofiling(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.npc");
            AntTask.this.addArg("profiling:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_npcolorer() {
        PhaseOptjap_npcolorer phaseOptjap_npcolorer = new PhaseOptjap_npcolorer();
        this.phaseopts.add(phaseOptjap_npcolorer);
        return phaseOptjap_npcolorer;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_npcolorer.class */
    public class PhaseOptjap_npcolorer {
        public PhaseOptjap_npcolorer() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.npcolorer");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_abc() {
        PhaseOptjap_abc phaseOptjap_abc = new PhaseOptjap_abc();
        this.phaseopts.add(phaseOptjap_abc);
        return phaseOptjap_abc;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_abc.class */
    public class PhaseOptjap_abc {
        public PhaseOptjap_abc() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setwith_all(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("with-all:" + (arg ? "true" : "false"));
        }

        public void setwith_cse(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("with-cse:" + (arg ? "true" : "false"));
        }

        public void setwith_arrayref(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("with-arrayref:" + (arg ? "true" : "false"));
        }

        public void setwith_fieldref(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("with-fieldref:" + (arg ? "true" : "false"));
        }

        public void setwith_classfield(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("with-classfield:" + (arg ? "true" : "false"));
        }

        public void setwith_rectarray(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("with-rectarray:" + (arg ? "true" : "false"));
        }

        public void setprofiling(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("profiling:" + (arg ? "true" : "false"));
        }

        public void setadd_color_tags(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.abc");
            AntTask.this.addArg("add-color-tags:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_profiling() {
        PhaseOptjap_profiling phaseOptjap_profiling = new PhaseOptjap_profiling();
        this.phaseopts.add(phaseOptjap_profiling);
        return phaseOptjap_profiling;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_profiling.class */
    public class PhaseOptjap_profiling {
        public PhaseOptjap_profiling() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.profiling");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setnotmainentry(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.profiling");
            AntTask.this.addArg("notmainentry:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_sea() {
        PhaseOptjap_sea phaseOptjap_sea = new PhaseOptjap_sea();
        this.phaseopts.add(phaseOptjap_sea);
        return phaseOptjap_sea;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_sea.class */
    public class PhaseOptjap_sea {
        public PhaseOptjap_sea() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.sea");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setnaive(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.sea");
            AntTask.this.addArg("naive:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_fieldrw() {
        PhaseOptjap_fieldrw phaseOptjap_fieldrw = new PhaseOptjap_fieldrw();
        this.phaseopts.add(phaseOptjap_fieldrw);
        return phaseOptjap_fieldrw;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_fieldrw.class */
    public class PhaseOptjap_fieldrw {
        public PhaseOptjap_fieldrw() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.fieldrw");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setthreshold(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.fieldrw");
            AntTask.this.addArg("threshold:" + arg);
        }
    }

    public Object createp_jap_cgtagger() {
        PhaseOptjap_cgtagger phaseOptjap_cgtagger = new PhaseOptjap_cgtagger();
        this.phaseopts.add(phaseOptjap_cgtagger);
        return phaseOptjap_cgtagger;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_cgtagger.class */
    public class PhaseOptjap_cgtagger {
        public PhaseOptjap_cgtagger() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.cgtagger");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_parity() {
        PhaseOptjap_parity phaseOptjap_parity = new PhaseOptjap_parity();
        this.phaseopts.add(phaseOptjap_parity);
        return phaseOptjap_parity;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_parity.class */
    public class PhaseOptjap_parity {
        public PhaseOptjap_parity() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.parity");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_pat() {
        PhaseOptjap_pat phaseOptjap_pat = new PhaseOptjap_pat();
        this.phaseopts.add(phaseOptjap_pat);
        return phaseOptjap_pat;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_pat.class */
    public class PhaseOptjap_pat {
        public PhaseOptjap_pat() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.pat");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_lvtagger() {
        PhaseOptjap_lvtagger phaseOptjap_lvtagger = new PhaseOptjap_lvtagger();
        this.phaseopts.add(phaseOptjap_lvtagger);
        return phaseOptjap_lvtagger;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_lvtagger.class */
    public class PhaseOptjap_lvtagger {
        public PhaseOptjap_lvtagger() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.lvtagger");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_rdtagger() {
        PhaseOptjap_rdtagger phaseOptjap_rdtagger = new PhaseOptjap_rdtagger();
        this.phaseopts.add(phaseOptjap_rdtagger);
        return phaseOptjap_rdtagger;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_rdtagger.class */
    public class PhaseOptjap_rdtagger {
        public PhaseOptjap_rdtagger() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.rdtagger");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_che() {
        PhaseOptjap_che phaseOptjap_che = new PhaseOptjap_che();
        this.phaseopts.add(phaseOptjap_che);
        return phaseOptjap_che;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_che.class */
    public class PhaseOptjap_che {
        public PhaseOptjap_che() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.che");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_umt() {
        PhaseOptjap_umt phaseOptjap_umt = new PhaseOptjap_umt();
        this.phaseopts.add(phaseOptjap_umt);
        return phaseOptjap_umt;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_umt.class */
    public class PhaseOptjap_umt {
        public PhaseOptjap_umt() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.umt");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_lit() {
        PhaseOptjap_lit phaseOptjap_lit = new PhaseOptjap_lit();
        this.phaseopts.add(phaseOptjap_lit);
        return phaseOptjap_lit;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_lit.class */
    public class PhaseOptjap_lit {
        public PhaseOptjap_lit() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.lit");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_jap_aet() {
        PhaseOptjap_aet phaseOptjap_aet = new PhaseOptjap_aet();
        this.phaseopts.add(phaseOptjap_aet);
        return phaseOptjap_aet;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_aet.class */
    public class PhaseOptjap_aet {
        public PhaseOptjap_aet() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.aet");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setkind(String arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.aet");
            AntTask.this.addArg("kind:" + arg);
        }
    }

    public Object createp_jap_dmt() {
        PhaseOptjap_dmt phaseOptjap_dmt = new PhaseOptjap_dmt();
        this.phaseopts.add(phaseOptjap_dmt);
        return phaseOptjap_dmt;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptjap_dmt.class */
    public class PhaseOptjap_dmt {
        public PhaseOptjap_dmt() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("jap.dmt");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_gb() {
        PhaseOptgb phaseOptgb = new PhaseOptgb();
        this.phaseopts.add(phaseOptgb);
        return phaseOptgb;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptgb.class */
    public class PhaseOptgb {
        public PhaseOptgb() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("gb");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_gb_a1() {
        PhaseOptgb_a1 phaseOptgb_a1 = new PhaseOptgb_a1();
        this.phaseopts.add(phaseOptgb_a1);
        return phaseOptgb_a1;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptgb_a1.class */
    public class PhaseOptgb_a1 {
        public PhaseOptgb_a1() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("gb.a1");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("gb.a1");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_gb_cf() {
        PhaseOptgb_cf phaseOptgb_cf = new PhaseOptgb_cf();
        this.phaseopts.add(phaseOptgb_cf);
        return phaseOptgb_cf;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptgb_cf.class */
    public class PhaseOptgb_cf {
        public PhaseOptgb_cf() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("gb.cf");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_gb_a2() {
        PhaseOptgb_a2 phaseOptgb_a2 = new PhaseOptgb_a2();
        this.phaseopts.add(phaseOptgb_a2);
        return phaseOptgb_a2;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptgb_a2.class */
    public class PhaseOptgb_a2 {
        public PhaseOptgb_a2() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("gb.a2");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setonly_stack_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("gb.a2");
            AntTask.this.addArg("only-stack-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_gb_ule() {
        PhaseOptgb_ule phaseOptgb_ule = new PhaseOptgb_ule();
        this.phaseopts.add(phaseOptgb_ule);
        return phaseOptgb_ule;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptgb_ule.class */
    public class PhaseOptgb_ule {
        public PhaseOptgb_ule() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("gb.ule");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_gop() {
        PhaseOptgop phaseOptgop = new PhaseOptgop();
        this.phaseopts.add(phaseOptgop);
        return phaseOptgop;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptgop.class */
    public class PhaseOptgop {
        public PhaseOptgop() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("gop");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_bb() {
        PhaseOptbb phaseOptbb = new PhaseOptbb();
        this.phaseopts.add(phaseOptbb);
        return phaseOptbb;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptbb.class */
    public class PhaseOptbb {
        public PhaseOptbb() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_bb_lso() {
        PhaseOptbb_lso phaseOptbb_lso = new PhaseOptbb_lso();
        this.phaseopts.add(phaseOptbb_lso);
        return phaseOptbb_lso;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptbb_lso.class */
    public class PhaseOptbb_lso {
        public PhaseOptbb_lso() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lso");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setdebug(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lso");
            AntTask.this.addArg("debug:" + (arg ? "true" : "false"));
        }

        public void setinter(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lso");
            AntTask.this.addArg("inter:" + (arg ? "true" : "false"));
        }

        public void setsl(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lso");
            AntTask.this.addArg("sl:" + (arg ? "true" : "false"));
        }

        public void setsl2(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lso");
            AntTask.this.addArg("sl2:" + (arg ? "true" : "false"));
        }

        public void setsll(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lso");
            AntTask.this.addArg("sll:" + (arg ? "true" : "false"));
        }

        public void setsll2(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lso");
            AntTask.this.addArg("sll2:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_bb_sco() {
        PhaseOptbb_sco phaseOptbb_sco = new PhaseOptbb_sco();
        this.phaseopts.add(phaseOptbb_sco);
        return phaseOptbb_sco;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptbb_sco.class */
    public class PhaseOptbb_sco {
        public PhaseOptbb_sco() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.sco");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_bb_pho() {
        PhaseOptbb_pho phaseOptbb_pho = new PhaseOptbb_pho();
        this.phaseopts.add(phaseOptbb_pho);
        return phaseOptbb_pho;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptbb_pho.class */
    public class PhaseOptbb_pho {
        public PhaseOptbb_pho() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.pho");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_bb_ule() {
        PhaseOptbb_ule phaseOptbb_ule = new PhaseOptbb_ule();
        this.phaseopts.add(phaseOptbb_ule);
        return phaseOptbb_ule;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptbb_ule.class */
    public class PhaseOptbb_ule {
        public PhaseOptbb_ule() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.ule");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_bb_lp() {
        PhaseOptbb_lp phaseOptbb_lp = new PhaseOptbb_lp();
        this.phaseopts.add(phaseOptbb_lp);
        return phaseOptbb_lp;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptbb_lp.class */
    public class PhaseOptbb_lp {
        public PhaseOptbb_lp() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lp");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setunsplit_original_locals(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.lp");
            AntTask.this.addArg("unsplit-original-locals:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_bb_ne() {
        PhaseOptbb_ne phaseOptbb_ne = new PhaseOptbb_ne();
        this.phaseopts.add(phaseOptbb_ne);
        return phaseOptbb_ne;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptbb_ne.class */
    public class PhaseOptbb_ne {
        public PhaseOptbb_ne() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bb.ne");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_bop() {
        PhaseOptbop phaseOptbop = new PhaseOptbop();
        this.phaseopts.add(phaseOptbop);
        return phaseOptbop;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptbop.class */
    public class PhaseOptbop {
        public PhaseOptbop() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("bop");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_tag() {
        PhaseOpttag phaseOpttag = new PhaseOpttag();
        this.phaseopts.add(phaseOpttag);
        return phaseOpttag;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOpttag.class */
    public class PhaseOpttag {
        public PhaseOpttag() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg(DropBoxManager.EXTRA_TAG);
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_tag_ln() {
        PhaseOpttag_ln phaseOpttag_ln = new PhaseOpttag_ln();
        this.phaseopts.add(phaseOpttag_ln);
        return phaseOpttag_ln;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOpttag_ln.class */
    public class PhaseOpttag_ln {
        public PhaseOpttag_ln() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("tag.ln");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_tag_an() {
        PhaseOpttag_an phaseOpttag_an = new PhaseOpttag_an();
        this.phaseopts.add(phaseOpttag_an);
        return phaseOpttag_an;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOpttag_an.class */
    public class PhaseOpttag_an {
        public PhaseOpttag_an() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("tag.an");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_tag_dep() {
        PhaseOpttag_dep phaseOpttag_dep = new PhaseOpttag_dep();
        this.phaseopts.add(phaseOpttag_dep);
        return phaseOpttag_dep;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOpttag_dep.class */
    public class PhaseOpttag_dep {
        public PhaseOpttag_dep() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("tag.dep");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_tag_fieldrw() {
        PhaseOpttag_fieldrw phaseOpttag_fieldrw = new PhaseOpttag_fieldrw();
        this.phaseopts.add(phaseOpttag_fieldrw);
        return phaseOpttag_fieldrw;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOpttag_fieldrw.class */
    public class PhaseOpttag_fieldrw {
        public PhaseOpttag_fieldrw() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("tag.fieldrw");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_db() {
        PhaseOptdb phaseOptdb = new PhaseOptdb();
        this.phaseopts.add(phaseOptdb);
        return phaseOptdb;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptdb.class */
    public class PhaseOptdb {
        public PhaseOptdb() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("db");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }

        public void setsource_is_javac(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("db");
            AntTask.this.addArg("source-is-javac:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_db_transformations() {
        PhaseOptdb_transformations phaseOptdb_transformations = new PhaseOptdb_transformations();
        this.phaseopts.add(phaseOptdb_transformations);
        return phaseOptdb_transformations;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptdb_transformations.class */
    public class PhaseOptdb_transformations {
        public PhaseOptdb_transformations() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("db.transformations");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_db_renamer() {
        PhaseOptdb_renamer phaseOptdb_renamer = new PhaseOptdb_renamer();
        this.phaseopts.add(phaseOptdb_renamer);
        return phaseOptdb_renamer;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptdb_renamer.class */
    public class PhaseOptdb_renamer {
        public PhaseOptdb_renamer() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("db.renamer");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_db_deobfuscate() {
        PhaseOptdb_deobfuscate phaseOptdb_deobfuscate = new PhaseOptdb_deobfuscate();
        this.phaseopts.add(phaseOptdb_deobfuscate);
        return phaseOptdb_deobfuscate;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptdb_deobfuscate.class */
    public class PhaseOptdb_deobfuscate {
        public PhaseOptdb_deobfuscate() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("db.deobfuscate");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }

    public Object createp_db_force_recompile() {
        PhaseOptdb_force_recompile phaseOptdb_force_recompile = new PhaseOptdb_force_recompile();
        this.phaseopts.add(phaseOptdb_force_recompile);
        return phaseOptdb_force_recompile;
    }

    /* loaded from: gencallgraphv3.jar:soot/AntTask$PhaseOptdb_force_recompile.class */
    public class PhaseOptdb_force_recompile {
        public PhaseOptdb_force_recompile() {
        }

        public void setenabled(boolean arg) {
            AntTask.this.addArg("-p");
            AntTask.this.addArg("db.force-recompile");
            AntTask.this.addArg("enabled:" + (arg ? "true" : "false"));
        }
    }
}
