package soot.toolkits.astmetrics.DataHandlingApplication;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import soot.CompilationDeathException;
import soot.coffi.Instruction;
import soot.jimple.infoflow.results.xml.XmlConstants;
/* loaded from: gencallgraphv3.jar:soot/toolkits/astmetrics/DataHandlingApplication/ProcessData.class */
public class ProcessData {
    private static final int CLASSNAMESIZE = 15;
    private static final int CLASS = 0;
    private static final int BENCHMARK = 1;
    private static String metricListFileName = null;
    private static final ArrayList<String> xmlFileList = new ArrayList<>();
    private static int aggregationMechanism = -1;
    private static OutputStream streamOut;
    private static PrintWriter bench;
    private static final boolean CSV = true;
    private static final boolean decompiler = false;

    public static void main(String[] args) {
        int argLength = args.length;
        if (argLength == 0) {
            printIntro();
            useHelp();
            System.exit(1);
        }
        if (args[0].equals("--help")) {
            printHelp();
            System.exit(1);
        } else if (args[0].equals("-metricList")) {
            metricListFileName(args);
            System.out.println("A list of metrics will be stored in: " + metricListFileName);
            readXMLFileNames(2, args);
            try {
                OutputStream streamOut2 = new FileOutputStream(metricListFileName);
                PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut2));
                writeMetricLists(writerOut);
                writerOut.flush();
                streamOut2.close();
            } catch (IOException e) {
                throw new CompilationDeathException("Cannot output file " + metricListFileName);
            }
        } else if (args[0].equals("-tables")) {
            metricListFileName(args);
            System.out.println("Will read column table headings from: " + metricListFileName);
            aggregationOption(args);
            if (aggregationMechanism == 1) {
                System.out.println("Aggregating over benchmarks...each row is one of the xml files");
                System.out.println("Only one tex file with the name" + metricListFileName + ".tex will be created");
            } else if (aggregationMechanism == 0) {
                System.out.println("Aggregating over class...each row is one class...");
                System.out.println("Each benchmark (xml file) will have its own tex file");
            }
            readXMLFileNames(3, args);
            generateMetricsTables();
        } else {
            System.out.println("Incorrect argument number 1: expecting -metricList or -tables");
            System.exit(1);
        }
    }

    private static void aggregationOption(String[] args) {
        if (args.length < 3) {
            System.out.println("Expecting -class or -benchmark at argument number 3");
            System.exit(1);
        }
        if (args[2].equals("-class")) {
            aggregationMechanism = 0;
        } else if (args[2].equals("-benchmark")) {
            aggregationMechanism = 1;
        } else {
            System.out.println("Expecting -class or -benchmark at argument number 3");
            System.exit(1);
        }
    }

    private static void readXMLFileNames(int startIndex, String[] args) {
        if (args.length < startIndex + 1) {
            System.out.println("Expecting an xml file OR * symbol as argument number" + (startIndex + 1));
            System.exit(1);
        }
        if (args[startIndex].equals("*")) {
            System.out.println("Will read all xml files from directory");
            readStar();
        } else {
            for (int i = startIndex; i < args.length; i++) {
                String temp = args[i];
                if (temp.endsWith(".xml")) {
                    xmlFileList.add(temp);
                }
            }
        }
        Iterator<String> it = xmlFileList.iterator();
        while (it.hasNext()) {
            System.out.println("Will be reading: " + it.next());
        }
    }

    private static void metricListFileName(String[] args) {
        if (args.length < 2) {
            System.out.println("Expecting name of metricList as argumnet number 2");
            System.exit(1);
        }
        metricListFileName = args[1];
    }

    public static void printHelp() {
        printIntro();
        System.out.println("There are two main modes of execution");
        System.out.println("To execute the program the first argument should be one of these modes");
        System.out.println("-metricList and -tables");
        System.out.println("\n\n The -metricList mode");
        System.out.println("The argument at location 1 should be name of a file where the list of metrics will be stored");
        System.out.println("All arguments following argument 1 have to be xml files to be processed");
        System.out.println("If argument at location 2 is * then the current directory is searched and all xml files will be processed");
        System.out.println("\n\n The -tables mode");
        System.out.println("The argument at location 1 should be name of a file where the list of metrics are stored");
        System.out.println("These metrics will become the COLUMNS in the tables created");
        System.out.println("Argument at location 2 is the choice of aggregation");
        System.out.println("\t -class for class level metrics");
        System.out.println("\t -benchmark for benchmark level metrics");
        System.out.println("Each xml file is considered to be a benchmark with a bunch of classes in it");
        System.out.println("All arguments following argument 2 have to be xml files to be processed");
        System.out.println("If argument at location 3 is * then the current directory is searched and all xml files will be processed");
    }

    public static void printIntro() {
        System.out.println("Welcome to the processData application");
        System.out.println("The application is an xml document parser.");
        System.out.println("Its primary aim is to create pretty tex tables");
    }

    public static void useHelp() {
        System.out.println("Use the --help flag for more details");
    }

    private static void readStar() {
        String curDir = System.getProperty("user.dir");
        System.out.println("Current system directory is" + curDir);
        File dir = new File(curDir);
        if (dir.list() != null) {
            FilenameFilter filter = new FilenameFilter() { // from class: soot.toolkits.astmetrics.DataHandlingApplication.ProcessData.1
                @Override // java.io.FilenameFilter
                public boolean accept(File dir2, String name) {
                    return name.endsWith(".xml");
                }
            };
            String[] children = dir.list(filter);
            if (children != null) {
                for (String element : children) {
                    xmlFileList.add(element);
                }
            }
        }
    }

    private static void writeMetricLists(PrintWriter out) {
        ArrayList<String> metricList = new ArrayList<>();
        Iterator<String> it = xmlFileList.iterator();
        while (it.hasNext()) {
            String fileName = it.next();
            try {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(new File(fileName));
                System.out.println("Retrieving Metric List from xml file: " + fileName);
                doc.getDocumentElement().normalize();
                NodeList metrics = doc.getElementsByTagName("Metric");
                for (int s = 0; s < metrics.getLength(); s++) {
                    Node metricNode = metrics.item(s);
                    if (metricNode.getNodeType() == 1) {
                        Element metricElement = (Element) metricNode;
                        NodeList metricName = metricElement.getElementsByTagName("MetricName");
                        Element name = (Element) metricName.item(0);
                        NodeList textFNList = name.getChildNodes();
                        if (!metricList.contains(textFNList.item(0).getNodeValue().trim())) {
                            metricList.add(textFNList.item(0).getNodeValue().trim());
                        }
                    }
                }
            } catch (SAXParseException err) {
                System.out.println("** Parsing error, line " + err.getLineNumber() + ", uri " + err.getSystemId());
                System.out.println(Instruction.argsep + err.getMessage());
            } catch (SAXException e) {
                Exception x = e.getException();
                (x == null ? e : x).printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        Iterator<String> it2 = metricList.iterator();
        while (it2.hasNext()) {
            out.println(it2.next());
        }
        System.out.println(String.valueOf(metricListFileName) + " created.");
    }

    private static void generateMetricsTables() {
        List<String> tempList;
        Vector<String> columns = new Vector<>();
        try {
            FileReader file = new FileReader(metricListFileName);
            BufferedReader fileInput = new BufferedReader(file);
            while (true) {
                String text = fileInput.readLine();
                if (text == null) {
                    break;
                }
                columns.add(text);
            }
            fileInput.close();
        } catch (Exception e) {
            System.out.println("Exception while reading from metricList" + metricListFileName);
            System.exit(1);
        }
        Vector<String> allMetrics = new Vector<>();
        try {
            FileReader file2 = new FileReader("myList");
            BufferedReader fileInput2 = new BufferedReader(file2);
            while (true) {
                String text2 = fileInput2.readLine();
                if (text2 == null) {
                    break;
                }
                allMetrics.add(text2);
            }
            fileInput2.close();
        } catch (Exception e2) {
            System.out.println("Exception while reading from metricList" + metricListFileName);
            System.exit(1);
        }
        if (aggregationMechanism == 1) {
            String newClassName = String.valueOf(metricListFileName) + ".csv";
            System.out.println("Creating csv file" + newClassName + " from metrics info");
            bench = openWriteFile(newClassName);
            Map<String, List<String>> benchMarkToFiles = new HashMap<>();
            Iterator<String> it = xmlFileList.iterator();
            while (it.hasNext()) {
                String fileName = it.next();
                if (fileName.indexOf(45) < 0) {
                    System.out.println("XML files should have following syntax:\n <BENCHMARKNAME>-<PROPERTY>.xml\n PROPERTY should be enabled disabled etc");
                    return;
                }
                String benchmark = fileName.substring(0, fileName.indexOf(45));
                List<String> temp = benchMarkToFiles.get(benchmark);
                if (temp == null) {
                    tempList = new ArrayList<>();
                } else {
                    tempList = temp;
                }
                tempList.add(fileName);
                benchMarkToFiles.put(benchmark, tempList);
                if (fileName.indexOf(45) < 0 || fileName.lastIndexOf(".xml") < 0) {
                    System.out.println("XML files should have following syntax:\n <BENCHMARKNAME>-<PROPERTY>.xml\n PROPERTY should be enabled disabled etc");
                    return;
                }
                String xmlfileColumnType = fileName.substring(fileName.lastIndexOf(45) + 1, fileName.lastIndexOf(".xml"));
                System.out.println("XML FILE COLUMN TYPE" + xmlfileColumnType);
                if (!xmlfileColumnType.equals("Jad") && !xmlfileColumnType.equals("original") && !xmlfileColumnType.equals("SourceAgain") && !xmlfileColumnType.equals("disabled") && !xmlfileColumnType.equals("enabled")) {
                    throw new RuntimeException("XML FILE <property> not recognized");
                }
            }
            printCSVHeader(bench);
            for (String key : benchMarkToFiles.keySet()) {
                List<String> tempValue = benchMarkToFiles.get(key);
                if (tempValue != null) {
                    if (tempValue.size() == 5) {
                        System.out.println("old order" + tempValue.toString());
                        String[] newFileOrder = new String[tempValue.size()];
                        for (String fileSort : tempValue) {
                            if (fileSort.indexOf("original") > -1) {
                                newFileOrder[0] = fileSort;
                            } else if (fileSort.indexOf("jbco-enabled") > -1) {
                                newFileOrder[1] = fileSort;
                            } else if (fileSort.indexOf("jbco-disabled") > -1) {
                                newFileOrder[2] = fileSort;
                            } else if (fileSort.indexOf("klassmaster-enabled") > -1) {
                                newFileOrder[3] = fileSort;
                            } else if (fileSort.indexOf("klassmaster-disabled") > -1) {
                                newFileOrder[4] = fileSort;
                            } else {
                                throw new RuntimeException("property xml not correct");
                            }
                        }
                        List<String> files = new ArrayList<>();
                        files.add(newFileOrder[0]);
                        files.add(newFileOrder[1]);
                        files.add(newFileOrder[2]);
                        files.add(newFileOrder[3]);
                        files.add(newFileOrder[4]);
                        System.out.println("new order" + files.toString());
                        int count = -1;
                        for (String fileName2 : files) {
                            count++;
                            try {
                                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                                Document doc = docBuilder.parse(new File(fileName2));
                                System.out.println("Gethering metric info from from xml file: " + fileName2);
                                HashMap<String, Number> aggregatedValues = new HashMap<>();
                                Iterator<String> tempIt = allMetrics.iterator();
                                while (tempIt.hasNext()) {
                                    aggregatedValues.put(tempIt.next(), new Integer(0));
                                }
                                aggregateXMLFileMetrics(doc, aggregatedValues);
                                Object myTemp = aggregatedValues.get("Total-Conditionals");
                                if (myTemp == null) {
                                    System.out.println("Total-Conditionals not found in aggregatedValues");
                                    System.exit(1);
                                }
                                double total_if_ifelse = ((Integer) myTemp).doubleValue();
                                Object myTemp2 = aggregatedValues.get("Total Loops");
                                if (myTemp2 == null) {
                                    System.out.println("Total Loops not found in aggregatedValues");
                                    System.exit(1);
                                }
                                double totalLoops = ((Integer) myTemp2).doubleValue();
                                double totalConditional = total_if_ifelse + totalLoops;
                                Object myTemp3 = aggregatedValues.get("AST-Node-Count");
                                if (myTemp3 == null) {
                                    System.out.println("AST-Node-Count not found in aggregatedValues");
                                    System.exit(1);
                                }
                                double astCount = ((Integer) myTemp3).doubleValue();
                                Object myTemp4 = aggregatedValues.get("NameCount");
                                if (myTemp4 == null) {
                                    System.out.println("NameCount not found in aggregatedValues");
                                    System.exit(1);
                                }
                                double nameCount = ((Double) myTemp4).doubleValue();
                                Object myTemp5 = aggregatedValues.get("Expr-Count");
                                if (myTemp5 == null) {
                                    System.out.println("ExprCount not found in aggregatedValues");
                                    System.exit(1);
                                }
                                double exprCount = ((Double) myTemp5).doubleValue();
                                Iterator<String> tempIt2 = columns.iterator();
                                while (tempIt2.hasNext()) {
                                    String nexttempit = tempIt2.next();
                                    Object temp2 = aggregatedValues.get(nexttempit);
                                    if (temp2 instanceof Integer) {
                                        int val = ((Integer) temp2).intValue();
                                        switch (count) {
                                            case 0:
                                                bench.print(fileName2.substring(0, fileName2.indexOf(45)));
                                            case 1:
                                            case 2:
                                            case 3:
                                            case 4:
                                                if (nexttempit.equals("Total-Abrupt")) {
                                                    bench.print("," + val);
                                                    break;
                                                } else if (nexttempit.equals("Total-Cond-Complexity")) {
                                                    if (totalConditional != Const.default_value_double) {
                                                        System.out.println("conditional complexit is" + val);
                                                        System.out.println("totalConditionals are" + totalConditional);
                                                        bench.print("," + (val / totalConditional));
                                                        break;
                                                    } else if (val == 0) {
                                                        bench.print("," + val);
                                                        break;
                                                    } else {
                                                        System.out.println("Val not 0 but totalConditionals are zero!!!");
                                                        System.exit(1);
                                                        break;
                                                    }
                                                } else if (nexttempit.equals("D-W-Complexity")) {
                                                    if (astCount != Const.default_value_double) {
                                                        bench.print("," + (val / astCount));
                                                        break;
                                                    } else if (val == 0) {
                                                        bench.print("," + val);
                                                        break;
                                                    } else {
                                                        System.out.println("Val not 0 but astcount is zero!!!");
                                                        System.exit(1);
                                                        break;
                                                    }
                                                } else if (nexttempit.equals("Expr-Complexity")) {
                                                    if (exprCount != Const.default_value_double) {
                                                        bench.print("," + (val / exprCount));
                                                        break;
                                                    } else if (val == 0) {
                                                        bench.print("," + val);
                                                        break;
                                                    } else {
                                                        System.out.println("Val not 0 but exprcount is zero!!!");
                                                        System.exit(1);
                                                        break;
                                                    }
                                                } else if (nexttempit.equals("Name-Complexity")) {
                                                    if (nameCount != Const.default_value_double) {
                                                        bench.print("," + (val / nameCount));
                                                        break;
                                                    } else if (val == 0) {
                                                        bench.print("," + val);
                                                        break;
                                                    } else {
                                                        System.out.println("Val not 0 but name-count is zero!!!");
                                                        System.exit(1);
                                                        break;
                                                    }
                                                } else {
                                                    bench.print("," + val);
                                                    break;
                                                }
                                            default:
                                                System.out.println("unhandled count value");
                                                System.exit(1);
                                                break;
                                        }
                                    } else if (temp2 instanceof Double) {
                                        double val2 = ((Double) temp2).doubleValue();
                                        switch (count) {
                                            case 0:
                                                bench.print(fileName2.substring(0, fileName2.indexOf(45)));
                                            case 1:
                                            case 2:
                                            case 3:
                                            case 4:
                                                if (nexttempit.equals("Total-Abrupt")) {
                                                    bench.print("," + val2);
                                                    break;
                                                } else if (nexttempit.equals("Total-Cond-Complexity")) {
                                                    if (totalConditional != Const.default_value_double) {
                                                        System.out.println("conditional complexit is" + val2);
                                                        System.out.println("totalConditionals are" + totalConditional);
                                                        bench.print("," + (val2 / totalConditional));
                                                        break;
                                                    } else if (val2 == Const.default_value_double) {
                                                        bench.print("," + val2);
                                                        break;
                                                    } else {
                                                        System.out.println("Val not 0 but totalConditionals are zero!!!");
                                                        System.exit(1);
                                                        break;
                                                    }
                                                } else if (nexttempit.equals("D-W-Complexity")) {
                                                    if (astCount != Const.default_value_double) {
                                                        bench.print("," + (val2 / astCount));
                                                        break;
                                                    } else if (val2 == Const.default_value_double) {
                                                        bench.print("," + val2);
                                                        break;
                                                    } else {
                                                        System.out.println("Val not 0 but astcount is zero!!!");
                                                        System.exit(1);
                                                        break;
                                                    }
                                                } else if (nexttempit.equals("Expr-Complexity")) {
                                                    if (exprCount != Const.default_value_double) {
                                                        bench.print("," + (val2 / exprCount));
                                                        break;
                                                    } else if (val2 == Const.default_value_double) {
                                                        bench.print("," + val2);
                                                        break;
                                                    } else {
                                                        System.out.println("Val not 0 but exprcount is zero!!!");
                                                        System.exit(1);
                                                        break;
                                                    }
                                                } else if (nexttempit.equals("Name-Complexity")) {
                                                    if (nameCount != Const.default_value_double) {
                                                        bench.print("," + (val2 / nameCount));
                                                        break;
                                                    } else if (val2 == Const.default_value_double) {
                                                        bench.print("," + val2);
                                                        break;
                                                    } else {
                                                        System.out.println("Val not 0 but name-count is zero!!!");
                                                        System.exit(1);
                                                        break;
                                                    }
                                                } else {
                                                    bench.print("," + val2);
                                                    break;
                                                }
                                            default:
                                                System.out.println("unhandled count value");
                                                System.exit(1);
                                                break;
                                        }
                                    } else {
                                        throw new RuntimeException("Unknown type of object stored!!!");
                                    }
                                    if (tempIt2.hasNext()) {
                                        System.out.println("Only allowed one metric for CSV");
                                        System.exit(1);
                                    }
                                }
                            } catch (SAXParseException err) {
                                System.out.println("** Parsing error, line " + err.getLineNumber() + ", uri " + err.getSystemId());
                                System.out.println(Instruction.argsep + err.getMessage());
                            } catch (SAXException e3) {
                                Exception x = e3.getException();
                                (x == null ? e3 : x).printStackTrace();
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                        bench.println("");
                    } else {
                        throw new RuntimeException("not all xml files available for this benchmark!!");
                    }
                }
            }
            closeWriteFile(bench, newClassName);
            return;
        }
        Iterator<String> it2 = xmlFileList.iterator();
        while (it2.hasNext()) {
            String fileName3 = it2.next();
            try {
                DocumentBuilderFactory docBuilderFactory2 = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder2 = docBuilderFactory2.newDocumentBuilder();
                Document doc2 = docBuilder2.parse(new File(fileName3));
                System.out.println("Gethering metric info from from xml file: " + fileName3);
                doc2.getDocumentElement().normalize();
                if (aggregationMechanism == 0) {
                    getClassMetrics(fileName3, doc2, columns);
                } else {
                    System.out.println("Unknown aggregation Mechanism");
                    System.exit(1);
                }
            } catch (SAXParseException err2) {
                System.out.println("** Parsing error, line " + err2.getLineNumber() + ", uri " + err2.getSystemId());
                System.out.println(Instruction.argsep + err2.getMessage());
            } catch (SAXException e4) {
                Exception x2 = e4.getException();
                (x2 == null ? e4 : x2).printStackTrace();
            } catch (Throwable t2) {
                t2.printStackTrace();
            }
        }
    }

    private static PrintWriter openWriteFile(String fileName) {
        try {
            streamOut = new FileOutputStream(fileName);
            PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
            return writerOut;
        } catch (IOException e) {
            throw new CompilationDeathException("Cannot output file " + fileName);
        }
    }

    private static void closeWriteFile(PrintWriter writerOut, String fileName) {
        try {
            writerOut.flush();
            streamOut.close();
        } catch (IOException e) {
            throw new CompilationDeathException("Cannot output file " + fileName);
        }
    }

    private static int aggregateXMLFileMetrics(Document doc, HashMap<String, Number> aggregated) {
        NodeList classes = doc.getElementsByTagName("Class");
        int numClasses = classes.getLength();
        System.out.println("NumClasses for this document are" + numClasses);
        NodeList metrics = doc.getElementsByTagName("Metric");
        for (int s = 0; s < metrics.getLength(); s++) {
            Node metricNode = metrics.item(s);
            if (metricNode.getNodeType() == 1) {
                Element metricElement = (Element) metricNode;
                NodeList metricName = metricElement.getElementsByTagName("MetricName");
                Element name = (Element) metricName.item(0);
                NodeList textFNList = name.getChildNodes();
                String tempName = textFNList.item(0).getNodeValue().trim();
                Object tempObj = aggregated.get(tempName);
                if (tempObj == null) {
                    continue;
                } else {
                    NodeList value = metricElement.getElementsByTagName(XmlConstants.Attributes.value);
                    Element name1 = (Element) value.item(0);
                    NodeList textFNList1 = name1.getChildNodes();
                    String valToPrint = textFNList1.item(0).getNodeValue().trim();
                    boolean notInt = false;
                    try {
                        int temp = Integer.parseInt(valToPrint);
                        if (tempObj instanceof Integer) {
                            Integer valSoFar = (Integer) tempObj;
                            aggregated.put(tempName, new Integer(valSoFar.intValue() + temp));
                        } else if (tempObj instanceof Double) {
                            Double valSoFar2 = (Double) tempObj;
                            aggregated.put(tempName, new Double(valSoFar2.doubleValue() + temp));
                        } else {
                            throw new RuntimeException("\n\nobject type not found");
                            break;
                        }
                    } catch (Exception e) {
                        notInt = true;
                    }
                    if (notInt) {
                        try {
                            double temp2 = Double.parseDouble(valToPrint);
                            if (tempObj instanceof Integer) {
                                Integer valSoFar3 = (Integer) tempObj;
                                aggregated.put(tempName, new Double(valSoFar3.intValue() + temp2));
                            } else if (tempObj instanceof Double) {
                                Double valSoFar4 = (Double) tempObj;
                                aggregated.put(tempName, new Double(valSoFar4.doubleValue() + temp2));
                            } else {
                                throw new RuntimeException("\n\nobject type not found");
                            }
                        } catch (Exception e2) {
                            throw new RuntimeException("\n\n not an integer not a double unhandled!!!!");
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        return numClasses;
    }

    private static void getClassMetrics(String fileName, Document doc, Vector<String> columns) {
        String newClassName = fileName;
        if (newClassName.endsWith(".xml")) {
            newClassName = newClassName.substring(0, newClassName.length() - 4);
        }
        String newClassName2 = String.valueOf(newClassName) + ".tex";
        System.out.println("Creating tex file" + newClassName2 + " from metrics info in file" + fileName);
        PrintWriter writerOut = openWriteFile(newClassName2);
        printTexTableHeader(writerOut, "Classes", columns);
        ArrayList<String> classNames = new ArrayList<>();
        HashMap<String, String> classData = new HashMap<>();
        NodeList classes = doc.getElementsByTagName("Class");
        for (int cl = 0; cl < classes.getLength(); cl++) {
            Node classNode = classes.item(cl);
            if (classNode.getNodeType() == 1) {
                Element classElement = (Element) classNode;
                NodeList classNameNodeList = classElement.getElementsByTagName("ClassName");
                Element classNameElement = (Element) classNameNodeList.item(0);
                NodeList classNameTextFNList = classNameElement.getChildNodes();
                String className = classNameTextFNList.item(0).getNodeValue().trim().replace('_', '-');
                if (className.length() > 15) {
                    className = className.substring(0, 15);
                    classNames.add(className);
                } else {
                    classNames.add(className);
                }
                System.out.print("\nclassName " + className);
                String data = "   ";
                NodeList metrics = classElement.getElementsByTagName("Metric");
                int columnIndex = 0;
                for (int s = 0; s < metrics.getLength() && columnIndex < columns.size(); s++) {
                    Node metricNode = metrics.item(s);
                    if (metricNode.getNodeType() == 1) {
                        Element metricElement = (Element) metricNode;
                        NodeList metricName = metricElement.getElementsByTagName("MetricName");
                        Element name = (Element) metricName.item(0);
                        NodeList textFNList = name.getChildNodes();
                        String tempName = textFNList.item(0).getNodeValue().trim();
                        if (tempName.equals(columns.elementAt(columnIndex))) {
                            NodeList value = metricElement.getElementsByTagName(XmlConstants.Attributes.value);
                            Element name1 = (Element) value.item(0);
                            NodeList textFNList1 = name1.getChildNodes();
                            String valToPrint = textFNList1.item(0).getNodeValue().trim();
                            System.out.print(Instruction.argsep + valToPrint);
                            String data2 = String.valueOf(data) + "&" + valToPrint;
                            columnIndex++;
                            if (columns.size() > columnIndex) {
                                data = String.valueOf(data2) + "   ";
                            } else {
                                data = String.valueOf(data2) + "\\\\";
                            }
                        }
                    }
                }
                classData.put(className, data);
            }
        }
        Collections.sort(classNames);
        Iterator<String> tempIt = classNames.iterator();
        while (tempIt.hasNext()) {
            String className2 = tempIt.next();
            String data3 = classData.get(className2);
            writerOut.print(className2);
            writerOut.println(data3);
        }
        printTexTableFooter(writerOut, fileName);
        closeWriteFile(writerOut, metricListFileName);
    }

    private static void printTexTableFooter(PrintWriter out, String tableCaption) {
        out.println("");
        out.println("\\hline");
        out.println("\\end{tabular}");
        out.println("\\caption{ ..." + tableCaption + "..... }");
        out.println("\\end{table}");
    }

    private static void printCSVHeader(PrintWriter out) {
        out.println(",Original,JBCO-enabled,JBCO-disabled,klassmaster-enabled,klassmaster-disabled");
    }

    private static void printTexTableHeader(PrintWriter out, String rowHeading, Vector<String> columns) {
        out.println("\\begin{table}[hbtp]");
        out.print("\\begin{tabular}{");
        for (int i = 0; i <= columns.size(); i++) {
            out.print("|l");
        }
        out.println("|}");
        out.println("\\hline");
        out.print(String.valueOf(rowHeading) + "   ");
        Iterator<String> it = columns.iterator();
        while (it.hasNext()) {
            out.print("&" + it.next());
            if (it.hasNext()) {
                out.print("   ");
            }
        }
        out.println("\\\\");
        out.println("\\hline");
    }
}
