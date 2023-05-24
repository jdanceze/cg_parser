package android.telephony;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import java.util.Locale;
/* loaded from: gencallgraphv3.jar:android-4.1.1.4.jar:android/telephony/PhoneNumberUtils.class */
public class PhoneNumberUtils {
    public static final char PAUSE = ',';
    public static final char WAIT = ';';
    public static final char WILD = 'N';
    public static final int TOA_International = 145;
    public static final int TOA_Unknown = 129;
    public static final int FORMAT_UNKNOWN = 0;
    public static final int FORMAT_NANP = 1;
    public static final int FORMAT_JAPAN = 2;

    public PhoneNumberUtils() {
        throw new RuntimeException("Stub!");
    }

    public static boolean isISODigit(char c) {
        throw new RuntimeException("Stub!");
    }

    public static final boolean is12Key(char c) {
        throw new RuntimeException("Stub!");
    }

    public static final boolean isDialable(char c) {
        throw new RuntimeException("Stub!");
    }

    public static final boolean isReallyDialable(char c) {
        throw new RuntimeException("Stub!");
    }

    public static final boolean isNonSeparator(char c) {
        throw new RuntimeException("Stub!");
    }

    public static final boolean isStartsPostDial(char c) {
        throw new RuntimeException("Stub!");
    }

    public static String getNumberFromIntent(Intent intent, Context context) {
        throw new RuntimeException("Stub!");
    }

    public static String extractNetworkPortion(String phoneNumber) {
        throw new RuntimeException("Stub!");
    }

    public static String stripSeparators(String phoneNumber) {
        throw new RuntimeException("Stub!");
    }

    public static String extractPostDialPortion(String phoneNumber) {
        throw new RuntimeException("Stub!");
    }

    public static boolean compare(String a, String b) {
        throw new RuntimeException("Stub!");
    }

    public static boolean compare(Context context, String a, String b) {
        throw new RuntimeException("Stub!");
    }

    public static String toCallerIDMinMatch(String phoneNumber) {
        throw new RuntimeException("Stub!");
    }

    public static String getStrippedReversed(String phoneNumber) {
        throw new RuntimeException("Stub!");
    }

    public static String stringFromStringAndTOA(String s, int TOA) {
        throw new RuntimeException("Stub!");
    }

    public static int toaFromString(String s) {
        throw new RuntimeException("Stub!");
    }

    public static String calledPartyBCDToString(byte[] bytes, int offset, int length) {
        throw new RuntimeException("Stub!");
    }

    public static String calledPartyBCDFragmentToString(byte[] bytes, int offset, int length) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isWellFormedSmsAddress(String address) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isGlobalPhoneNumber(String phoneNumber) {
        throw new RuntimeException("Stub!");
    }

    public static byte[] networkPortionToCalledPartyBCD(String s) {
        throw new RuntimeException("Stub!");
    }

    public static byte[] networkPortionToCalledPartyBCDWithLength(String s) {
        throw new RuntimeException("Stub!");
    }

    public static byte[] numberToCalledPartyBCD(String number) {
        throw new RuntimeException("Stub!");
    }

    public static String formatNumber(String source) {
        throw new RuntimeException("Stub!");
    }

    public static int getFormatTypeForLocale(Locale locale) {
        throw new RuntimeException("Stub!");
    }

    public static void formatNumber(Editable text, int defaultFormattingType) {
        throw new RuntimeException("Stub!");
    }

    public static void formatNanpNumber(Editable text) {
        throw new RuntimeException("Stub!");
    }

    public static void formatJapaneseNumber(Editable text) {
        throw new RuntimeException("Stub!");
    }

    public static boolean isEmergencyNumber(String number) {
        throw new RuntimeException("Stub!");
    }

    public static String convertKeypadLettersToDigits(String input) {
        throw new RuntimeException("Stub!");
    }
}
