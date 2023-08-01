import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodSourceCodeRetriever {
    public static void main(String[] args) {
//        String methodSignaturesFile = "/Users/jdanceze/test/methods_reference.txt";
//        String sourceFilesDirectory = "/Users/jdanceze/test/sources";
//        String outputCsvFile = "/Users/jdanceze/test/output.csv";

    	if (args.length < 3) {
            System.out.println("Usage: java MethodSourceCodeRetriever <methodSignaturesFile> <sourceFilesDirectory> <outputCsvFile>");
            return;
        }

        String methodSignaturesFile = args[0];
        String sourceFilesDirectory = args[1];
        String outputCsvFile = args[2];
        
        List<String> methodSignatures = readMethodSignatures(methodSignaturesFile);
        List<String[]> resultRows = new ArrayList<>();

        searchForMethods(methodSignatures, sourceFilesDirectory, resultRows);

        writeResultToCsv(resultRows, outputCsvFile);
    }

    private static List<String> readMethodSignatures(String methodSignaturesFile) {
        List<String> methodSignatures = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(methodSignaturesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                methodSignatures.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return methodSignatures;
    }

    private static void searchForMethods(List<String> methodSignatures, String sourceFilesDirectory, List<String[]> resultRows) {
        File directory = new File(sourceFilesDirectory);
        if (!directory.isDirectory()) {
            System.out.println("Invalid directory path.");
            return;
        }

        Set<String> foundMethods = new HashSet<>();

        searchDirectoryForMethods(directory, methodSignatures, resultRows, foundMethods);

        for (String methodSignature : methodSignatures) {
            if (!foundMethods.contains(methodSignature)) {
                String[] resultRow = {methodSignature, "NOTFOUNDSOURCE"};
                resultRows.add(resultRow);
            }
        }
    }

    private static void searchDirectoryForMethods(File directory, List<String> methodSignatures, List<String[]> resultRows, Set<String> foundMethods) {
        File[] files = directory.listFiles();
        if (files == null) {
            System.out.println("No files found in the directory.");
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                searchDirectoryForMethods(file, methodSignatures, resultRows, foundMethods);
            } else if (file.getName().endsWith(".java")) {
                searchFileForMethods(file, methodSignatures, resultRows, foundMethods);
            }
        }
    }

    private static void searchFileForMethods(File file, List<String> methodSignatures, List<String[]> resultRows, Set<String> foundMethods) {
        try {
            CompilationUnit cu = StaticJavaParser.parse(file);

            for (String methodSignature : methodSignatures) {
                String fullClassName = extractClassName(methodSignature);
                String methodName = extractMethodName(methodSignature);
                String returnType = extractReturnType(methodSignature);

                ClassOrInterfaceDeclaration classDeclaration = cu.findFirst(ClassOrInterfaceDeclaration.class,
                        cd -> cd.getFullyQualifiedName().orElse("").equals(fullClassName)).orElse(null);

                if (classDeclaration != null) {
                    MethodDeclaration methodDeclaration = classDeclaration.findFirst(MethodDeclaration.class, (md) ->
                            md.getNameAsString().equals(methodName)
                                    && isReturnType(md, returnType)
                    ).orElse(null);

                    if (methodDeclaration != null) {
                        String sourceCode = methodDeclaration.toString();
                        String[] resultRow = {methodSignature, escapeNewlines(sourceCode)};
                        resultRows.add(resultRow);
                        foundMethods.add(methodSignature);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean isReturnType(MethodDeclaration methodDeclaration, String returnType) {
        Type methodReturnType = methodDeclaration.getType();
        return methodReturnType.asString().equals(returnType);
    }

    private static String extractClassName(String methodSignature) {
        int startIndex = methodSignature.indexOf("<") + 1;
        int endIndex = methodSignature.indexOf(":");
        String classWithPackage = methodSignature.substring(startIndex, endIndex).trim();
        return classWithPackage.substring(classWithPackage.lastIndexOf(" ") + 1);
    }

    private static String extractMethodName(String methodSignature) {
        int startIndex = methodSignature.lastIndexOf(" ") + 1;
        int endIndex = methodSignature.indexOf("(");
        return methodSignature.substring(startIndex, endIndex).trim();
    }

    private static String extractReturnType(String methodSignature) {
        int startIndex = methodSignature.indexOf(":") + 1;
        int endIndex = methodSignature.lastIndexOf(")");
        String returnTypeWithArgs = methodSignature.substring(startIndex, endIndex).trim();
        return returnTypeWithArgs.split(" ")[0];
    }

    private static void writeResultToCsv(List<String[]> rows, String outputCsvFile) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(outputCsvFile))) {
            writer.println("Method Signature\tSource Code");
            for (String[] row : rows) {
                writer.println(row[0] + "\t" + row[1]);
            }
            System.out.println("Result CSV file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String escapeNewlines(String sourceCode) {
        return sourceCode.replace("\n", "\\n");
    }
}
