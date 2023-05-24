package soot.jimple.infoflow.android.axml.flags;

import java.util.Collection;
import net.bytebuddy.agent.VirtualMachine;
import org.jf.dexlib2.dexbacked.raw.ItemType;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/flags/InputType.class */
public class InputType {
    private static final BitwiseFlagSystem<Integer> FLAG_SYSTEM = new BitwiseFlagSystem<>();
    public static int textFilter = 177;
    public static int textPostalAddress = 113;
    public static int textWebEmailAddress = 209;
    public static int textWebPassword = 225;
    public static int textEmailSubject = 49;
    public static int textLongMessage = 81;
    public static int textPersonName = 97;
    public static int textPhonetic = 193;
    public static int textVisiblePassword = 145;
    public static int textWebEditText = 161;
    public static int date = 20;
    public static int numberDecimal = 8194;
    public static int numberPassword = 225;
    public static int numberSigned = 4098;
    public static int phone = 3;
    public static int textAutoComplete = VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.Factory.MacLibrary.CS_DARWIN_USER_TEMP_DIR;
    public static int textAutoCorrect = 32769;
    public static int textCapCharacters = 4097;
    public static int textCapWords = ItemType.CODE_ITEM;
    public static int textEmailAddress = 33;
    public static int textCapSentences = 16385;
    public static int textImeMultiLine = 262145;
    public static int textMultiLine = 131073;
    public static int textNoSuggestions = 524289;
    public static int textPassword = 129;
    public static int textShortMessage = 65;
    public static int textUri = 27;
    public static int time = 36;
    public static int datetime = 4;
    public static int number = 2;
    public static int text = 1;

    static {
        FLAG_SYSTEM.register(Integer.valueOf(textFilter), 177);
        FLAG_SYSTEM.register(Integer.valueOf(textPostalAddress), 113);
        FLAG_SYSTEM.register(Integer.valueOf(textWebEmailAddress), 209);
        FLAG_SYSTEM.register(Integer.valueOf(textWebPassword), 225);
        FLAG_SYSTEM.register(Integer.valueOf(textEmailSubject), 49);
        FLAG_SYSTEM.register(Integer.valueOf(textLongMessage), 81);
        FLAG_SYSTEM.register(Integer.valueOf(textPersonName), 97);
        FLAG_SYSTEM.register(Integer.valueOf(textPhonetic), 193);
        FLAG_SYSTEM.register(Integer.valueOf(textVisiblePassword), 145);
        FLAG_SYSTEM.register(Integer.valueOf(textWebEditText), 161);
        FLAG_SYSTEM.register(Integer.valueOf(date), 20);
        FLAG_SYSTEM.register(Integer.valueOf(numberDecimal), 8194);
        FLAG_SYSTEM.register(Integer.valueOf(numberPassword), 18);
        FLAG_SYSTEM.register(Integer.valueOf(numberSigned), 4098);
        FLAG_SYSTEM.register(Integer.valueOf(phone), 3);
        FLAG_SYSTEM.register(Integer.valueOf(textAutoComplete), VirtualMachine.ForHotSpot.Connection.ForJnaPosixSocket.Factory.MacLibrary.CS_DARWIN_USER_TEMP_DIR);
        FLAG_SYSTEM.register(Integer.valueOf(textAutoCorrect), 32769);
        FLAG_SYSTEM.register(Integer.valueOf(textCapCharacters), 4097);
        FLAG_SYSTEM.register(Integer.valueOf(textCapSentences), 16385);
        FLAG_SYSTEM.register(Integer.valueOf(textCapWords), ItemType.CODE_ITEM);
        FLAG_SYSTEM.register(Integer.valueOf(textEmailAddress), 33);
        FLAG_SYSTEM.register(Integer.valueOf(textImeMultiLine), 262145);
        FLAG_SYSTEM.register(Integer.valueOf(textMultiLine), 131073);
        FLAG_SYSTEM.register(Integer.valueOf(textNoSuggestions), 524289);
        FLAG_SYSTEM.register(Integer.valueOf(textPassword), 129);
        FLAG_SYSTEM.register(Integer.valueOf(textShortMessage), 65);
        FLAG_SYSTEM.register(Integer.valueOf(textUri), 17);
        FLAG_SYSTEM.register(Integer.valueOf(time), 36);
        FLAG_SYSTEM.register(Integer.valueOf(datetime), 4);
        FLAG_SYSTEM.register(Integer.valueOf(number), 2);
        FLAG_SYSTEM.register(Integer.valueOf(text), 1);
    }

    public static boolean isSet(int inputValue, Integer... flag) {
        return FLAG_SYSTEM.isSet(inputValue, flag);
    }

    public static Collection<Integer> getFlags(int inputValue) {
        return FLAG_SYSTEM.getFlags(inputValue);
    }
}
