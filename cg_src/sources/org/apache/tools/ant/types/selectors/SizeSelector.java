package org.apache.tools.ant.types.selectors;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.types.Comparison;
import org.apache.tools.ant.types.EnumeratedAttribute;
import org.apache.tools.ant.types.Parameter;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/SizeSelector.class */
public class SizeSelector extends BaseExtendSelector {
    private static final int KILO = 1000;
    private static final int KIBI = 1024;
    private static final int KIBI_POS = 4;
    private static final int MEGA = 1000000;
    private static final int MEGA_POS = 9;
    private static final int MEBI = 1048576;
    private static final int MEBI_POS = 13;
    private static final long GIGA = 1000000000;
    private static final int GIGA_POS = 18;
    private static final long GIBI = 1073741824;
    private static final int GIBI_POS = 22;
    private static final long TERA = 1000000000000L;
    private static final int TERA_POS = 27;
    private static final long TEBI = 1099511627776L;
    private static final int TEBI_POS = 31;
    private static final int END_POS = 36;
    public static final String SIZE_KEY = "value";
    public static final String UNITS_KEY = "units";
    public static final String WHEN_KEY = "when";
    private long size = -1;
    private long multiplier = 1;
    private long sizelimit = -1;
    private Comparison when = Comparison.EQUAL;

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/SizeSelector$SizeComparisons.class */
    public static class SizeComparisons extends Comparison {
    }

    @Override // org.apache.tools.ant.types.DataType
    public String toString() {
        return String.format("{sizeselector value: %d compare: %s}", Long.valueOf(this.sizelimit), this.when.getValue());
    }

    public void setValue(long size) {
        this.size = size;
        if (this.multiplier != 0 && size > -1) {
            this.sizelimit = size * this.multiplier;
        }
    }

    public void setUnits(ByteUnits units) {
        int i = units.getIndex();
        this.multiplier = 0L;
        if (i > -1 && i < 4) {
            this.multiplier = 1000L;
        } else if (i < 9) {
            this.multiplier = FileUtils.ONE_KB;
        } else if (i < 13) {
            this.multiplier = 1000000L;
        } else if (i < 18) {
            this.multiplier = FileUtils.ONE_MB;
        } else if (i < 22) {
            this.multiplier = GIGA;
        } else if (i < 27) {
            this.multiplier = 1073741824L;
        } else if (i < 31) {
            this.multiplier = TERA;
        } else if (i < 36) {
            this.multiplier = 1099511627776L;
        }
        if (this.multiplier > 0 && this.size > -1) {
            this.sizelimit = this.size * this.multiplier;
        }
    }

    public void setWhen(SizeComparisons when) {
        this.when = when;
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.Parameterizable
    public void setParameters(Parameter... parameters) {
        super.setParameters(parameters);
        if (parameters != null) {
            for (Parameter parameter : parameters) {
                String paramname = parameter.getName();
                if ("value".equalsIgnoreCase(paramname)) {
                    try {
                        setValue(Long.parseLong(parameter.getValue()));
                    } catch (NumberFormatException e) {
                        setError("Invalid size setting " + parameter.getValue());
                    }
                } else if (UNITS_KEY.equalsIgnoreCase(paramname)) {
                    ByteUnits units = new ByteUnits();
                    units.setValue(parameter.getValue());
                    setUnits(units);
                } else if ("when".equalsIgnoreCase(paramname)) {
                    SizeComparisons scmp = new SizeComparisons();
                    scmp.setValue(parameter.getValue());
                    setWhen(scmp);
                } else {
                    setError("Invalid parameter " + paramname);
                }
            }
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseSelector
    public void verifySettings() {
        if (this.size < 0) {
            setError("The value attribute is required, and must be positive");
        } else if (this.multiplier < 1) {
            setError("Invalid Units supplied, must be K,Ki,M,Mi,G,Gi,T,or Ti");
        } else if (this.sizelimit < 0) {
            setError("Internal error: Code is not setting sizelimit correctly");
        }
    }

    @Override // org.apache.tools.ant.types.selectors.BaseExtendSelector, org.apache.tools.ant.types.selectors.BaseSelector, org.apache.tools.ant.types.selectors.FileSelector
    public boolean isSelected(File basedir, String filename, File file) {
        validate();
        if (file.isDirectory()) {
            return true;
        }
        long diff = file.length() - this.sizelimit;
        return this.when.evaluate(diff == 0 ? 0 : (int) (diff / Math.abs(diff)));
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/types/selectors/SizeSelector$ByteUnits.class */
    public static class ByteUnits extends EnumeratedAttribute {
        @Override // org.apache.tools.ant.types.EnumeratedAttribute
        public String[] getValues() {
            return new String[]{"K", "k", "kilo", "KILO", "Ki", "KI", "ki", "kibi", "KIBI", "M", "m", "mega", "MEGA", "Mi", "MI", "mi", "mebi", "MEBI", "G", "g", "giga", "GIGA", "Gi", "GI", "gi", "gibi", "GIBI", "T", "t", "tera", "TERA", "Ti", "TI", "ti", "tebi", "TEBI"};
        }
    }
}
