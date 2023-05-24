package soot.validation;

import soot.Body;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/validation/UnitValidationException.class */
public class UnitValidationException extends ValidationException {
    private static final long serialVersionUID = 1;

    public UnitValidationException(Unit concerned, Body body, String strMessage, boolean isWarning) {
        super(concerned, strMessage, formatMsg(strMessage, concerned, body), isWarning);
    }

    public UnitValidationException(Unit concerned, Body body, String strMessage) {
        super(concerned, strMessage, formatMsg(strMessage, concerned, body), false);
    }

    private static String formatMsg(String s, Unit u, Body b) {
        StringBuilder sb = new StringBuilder();
        sb.append(s).append('\n');
        sb.append("in unit: ").append(u).append('\n');
        sb.append("in body: \n ").append(b).append('\n');
        return sb.toString();
    }
}
