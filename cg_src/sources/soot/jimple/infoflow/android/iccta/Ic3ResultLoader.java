package soot.jimple.infoflow.android.iccta;

import com.google.protobuf.TextFormat;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.android.iccta.Ic3Data;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3ResultLoader.class */
public class Ic3ResultLoader {
    private static final Logger logger = LoggerFactory.getLogger(Ic3ResultLoader.class);
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind;

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[Ic3Data.AttributeKind.valuesCustom().length];
        try {
            iArr2[Ic3Data.AttributeKind.ACTION.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.AUTHORITY.ordinal()] = 9;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.CATEGORY.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.CLASS.ordinal()] = 4;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.EXTRA.ordinal()] = 8;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.FLAG.ordinal()] = 15;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.HOST.ordinal()] = 10;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.PACKAGE.ordinal()] = 3;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.PATH.ordinal()] = 11;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.PORT.ordinal()] = 12;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.PRIORITY.ordinal()] = 16;
        } catch (NoSuchFieldError unused11) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.QUERY.ordinal()] = 14;
        } catch (NoSuchFieldError unused12) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.SCHEME.ordinal()] = 7;
        } catch (NoSuchFieldError unused13) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.SSP.ordinal()] = 13;
        } catch (NoSuchFieldError unused14) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.TYPE.ordinal()] = 5;
        } catch (NoSuchFieldError unused15) {
        }
        try {
            iArr2[Ic3Data.AttributeKind.URI.ordinal()] = 6;
        } catch (NoSuchFieldError unused16) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind = iArr2;
        return iArr2;
    }

    /* JADX WARN: Finally extract failed */
    public static App load(String resultConfigPath) {
        Ic3Data.Application application;
        try {
            FileInputStream inputStream = new FileInputStream(resultConfigPath);
            try {
                if (resultConfigPath.endsWith(".dat")) {
                    application = Ic3Data.Application.parseFrom(inputStream);
                } else {
                    Ic3Data.Application.Builder builder = Ic3Data.Application.newBuilder();
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    TextFormat.merge(reader, builder);
                    application = builder.build();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                Set<LoggingPoint> loggingPoints = new HashSet<>();
                App result = new App("IC3", application.getName());
                result.setComponentList(application.getComponentsList());
                for (Ic3Data.Application.Component component : application.getComponentsList()) {
                    for (Ic3Data.Application.Component.ExitPoint exitPoint : component.getExitPointsList()) {
                        LoggingPoint loggingPoint = new LoggingPoint(result);
                        Ic3Data.Application.Component.Instruction instruction = exitPoint.getInstruction();
                        loggingPoint.setCallerMethodSignature(instruction.getMethod());
                        loggingPoint.setStmtSequence(instruction.getId());
                        String stmt = instruction.getStatement();
                        int startPos = stmt.indexOf("<");
                        int endPos = stmt.lastIndexOf(">");
                        loggingPoint.setCalleeMethodSignature(stmt.substring(startPos, endPos + 1));
                        Set<Intent> intents = new HashSet<>();
                        loggingPoint.setIntents(intents);
                        for (Ic3Data.Application.Component.ExitPoint.Intent intent : exitPoint.getIntentsList()) {
                            Intent destinationIntent = new Intent(result, loggingPoint);
                            String componentPackage = null;
                            String componentClass = null;
                            Set<String> categories = new HashSet<>();
                            Set<String> extras = new HashSet<>();
                            Set<Integer> flags = new HashSet<>();
                            for (Ic3Data.Attribute attribute : intent.getAttributesList()) {
                                switch ($SWITCH_TABLE$soot$jimple$infoflow$android$iccta$Ic3Data$AttributeKind()[attribute.getKind().ordinal()]) {
                                    case 1:
                                        destinationIntent.setAction(attribute.getValue(0));
                                        break;
                                    case 2:
                                        categories.addAll(attribute.getValueList());
                                        break;
                                    case 3:
                                        componentPackage = attribute.getValue(0);
                                        destinationIntent.setComponent(componentPackage);
                                        break;
                                    case 4:
                                        componentClass = attribute.getValue(0).replace('/', '.');
                                        destinationIntent.setComponentClass(componentClass.replace('/', '.'));
                                        break;
                                    case 5:
                                        destinationIntent.setType(attribute.getValue(0));
                                        break;
                                    case 6:
                                        destinationIntent.setData(attribute.getValue(0));
                                        break;
                                    case 7:
                                        destinationIntent.setDataScheme(attribute.getValue(0));
                                        break;
                                    case 8:
                                        extras.addAll(attribute.getValueList());
                                        break;
                                    case 9:
                                        destinationIntent.setAuthority(attribute.getValue(0));
                                        break;
                                    case 10:
                                        destinationIntent.setDataHost(attribute.getValue(0));
                                        break;
                                    case 11:
                                        destinationIntent.setDataPath(attribute.getValue(0));
                                        break;
                                    case 12:
                                        destinationIntent.setDataPort(attribute.getIntValue(0));
                                        break;
                                    case 15:
                                        flags.addAll(attribute.getIntValueList());
                                        break;
                                }
                            }
                            if (categories.size() != 0) {
                                destinationIntent.setCategories(categories);
                            }
                            if (extras.size() != 0) {
                                Map<String, String> extrasMap = new HashMap<>();
                                for (String extra : extras) {
                                    extrasMap.put(extra, "(.*)");
                                }
                                destinationIntent.setExtras(extrasMap);
                            }
                            if (flags.size() != 0) {
                                int flagsInteger = 0;
                                for (Integer num : flags) {
                                    int flag = num.intValue();
                                    flagsInteger |= flag;
                                }
                                destinationIntent.setFlags(flagsInteger);
                            }
                            if (componentPackage != null && componentClass != null) {
                                destinationIntent.setComponent(String.valueOf(componentPackage) + "/" + componentClass);
                            }
                            intents.add(destinationIntent);
                        }
                        loggingPoints.add(loggingPoint);
                    }
                }
                result.setAnalysisTime((int) (application.getAnalysisEnd() - application.getAnalysisStart()));
                result.setLoggingPoints(loggingPoints);
                return result;
            } catch (Throwable th) {
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (IOException exception) {
            logger.error("Problem opening or reading from file " + resultConfigPath, (Throwable) exception);
            return null;
        }
    }
}
