package org.apache.tools.ant.taskdefs.optional.extension;

import java.io.File;
import java.text.ParseException;
import java.util.jar.Manifest;
import org.apache.commons.cli.HelpFormatter;
import org.apache.tools.ant.BuildException;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/taskdefs/optional/extension/LibraryDisplayer.class */
class LibraryDisplayer {
    /* JADX INFO: Access modifiers changed from: package-private */
    public void displayLibrary(File file) throws BuildException {
        Manifest manifest = ExtensionUtil.getManifest(file);
        displayLibrary(file, manifest);
    }

    void displayLibrary(File file, Manifest manifest) throws BuildException {
        Extension[] available = Extension.getAvailable(manifest);
        Extension[] required = Extension.getRequired(manifest);
        Extension[] options = Extension.getOptions(manifest);
        Specification[] specifications = getSpecifications(manifest);
        if (0 == available.length && 0 == required.length && 0 == options.length && 0 == specifications.length) {
            return;
        }
        String message = "File: " + file;
        int size = message.length();
        printLine(size);
        System.out.println(message);
        printLine(size);
        if (0 != available.length) {
            System.out.println("Extensions Supported By Library:");
            for (Extension extension : available) {
                System.out.println(extension);
            }
        }
        if (0 != required.length) {
            System.out.println("Extensions Required By Library:");
            for (Extension extension2 : required) {
                System.out.println(extension2);
            }
        }
        if (0 != options.length) {
            System.out.println("Extensions that will be used by Library if present:");
            for (Extension option : options) {
                System.out.println(option);
            }
        }
        if (0 != specifications.length) {
            System.out.println("Specifications Supported By Library:");
            for (Specification specification : specifications) {
                displaySpecification(specification);
            }
        }
    }

    private void printLine(int size) {
        for (int i = 0; i < size; i++) {
            System.out.print(HelpFormatter.DEFAULT_OPT_PREFIX);
        }
        System.out.println();
    }

    private Specification[] getSpecifications(Manifest manifest) throws BuildException {
        try {
            return Specification.getSpecifications(manifest);
        } catch (ParseException pe) {
            throw new BuildException(pe.getMessage(), pe);
        }
    }

    private void displaySpecification(Specification specification) {
        String[] sections = specification.getSections();
        if (null != sections) {
            System.out.print("Sections:  ");
            System.out.println(String.join(Instruction.argsep, sections));
        }
        System.out.println(specification.toString());
    }
}
