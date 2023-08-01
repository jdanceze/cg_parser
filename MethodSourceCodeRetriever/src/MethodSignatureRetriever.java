import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class MethodSignatureRetriever {
    public static void main(String[] args) {
        String filePath = "/Users/jdanceze/eclipse-workspace/MethodSourceCodeRetriever/src/AutoStartActivity.java";

        try {
            // Parse the Java source file
            CompilationUnit cu = StaticJavaParser.parse(new File(filePath));

            // Retrieve all method declarations
            List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

            // Print the method signatures
            for (MethodDeclaration method : methods) {
                String methodSignature = method.getDeclarationAsString();
                System.out.println("Method signature: " + methodSignature);
            }
            
            // Check if any methods were found
            if (methods.isEmpty()) {
                System.out.println("No methods found in the file.");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

//String methodSignaturesFile = "/Users/jdanceze/test/methods_reference.txt";
//String sourceFilesDirectory = "/Users/jdanceze/eclipse-workspace/MethodSourceCodeRetriever";
//String outputCsvFile = "/Users/jdanceze/test/output.csv";
