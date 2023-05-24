package soot.jimple.infoflow.android.data;

import soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/CategoryDefinition.class */
public class CategoryDefinition implements ISourceSinkCategory {
    private CATEGORY systemCategory;
    private String customCategory;
    private String customDescription;
    public static final CategoryDefinition NO_CATEGORY = new CategoryDefinition(CATEGORY.NO_CATEGORY);
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$data$CategoryDefinition$CATEGORY;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/data/CategoryDefinition$CATEGORY.class */
    public enum CATEGORY {
        ALL,
        NO_CATEGORY,
        HARDWARE_INFO,
        UNIQUE_IDENTIFIER,
        LOCATION_INFORMATION,
        NETWORK_INFORMATION,
        ACCOUNT_INFORMATION,
        EMAIL_INFORMATION,
        FILE_INFORMATION,
        BLUETOOTH_INFORMATION,
        VOIP_INFORMATION,
        DATABASE_INFORMATION,
        PHONE_INFORMATION,
        PHONE_CONNECTION,
        INTER_APP_COMMUNICATION,
        VOIP,
        PHONE_STATE,
        EMAIL,
        BLUETOOTH,
        ACCOUNT_SETTINGS,
        VIDEO,
        SYNCHRONIZATION_DATA,
        NETWORK,
        EMAIL_SETTINGS,
        FILE,
        LOG,
        AUDIO,
        SMS_MMS,
        CONTACT_INFORMATION,
        CALENDAR_INFORMATION,
        SYSTEM_SETTINGS,
        IMAGE,
        BROWSER_INFORMATION,
        NFC;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static CATEGORY[] valuesCustom() {
            CATEGORY[] valuesCustom = values();
            int length = valuesCustom.length;
            CATEGORY[] categoryArr = new CATEGORY[length];
            System.arraycopy(valuesCustom, 0, categoryArr, 0, length);
            return categoryArr;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$android$data$CategoryDefinition$CATEGORY() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$android$data$CategoryDefinition$CATEGORY;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[CATEGORY.valuesCustom().length];
        try {
            iArr2[CATEGORY.ACCOUNT_INFORMATION.ordinal()] = 7;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[CATEGORY.ACCOUNT_SETTINGS.ordinal()] = 20;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[CATEGORY.ALL.ordinal()] = 1;
        } catch (NoSuchFieldError unused3) {
        }
        try {
            iArr2[CATEGORY.AUDIO.ordinal()] = 27;
        } catch (NoSuchFieldError unused4) {
        }
        try {
            iArr2[CATEGORY.BLUETOOTH.ordinal()] = 19;
        } catch (NoSuchFieldError unused5) {
        }
        try {
            iArr2[CATEGORY.BLUETOOTH_INFORMATION.ordinal()] = 10;
        } catch (NoSuchFieldError unused6) {
        }
        try {
            iArr2[CATEGORY.BROWSER_INFORMATION.ordinal()] = 33;
        } catch (NoSuchFieldError unused7) {
        }
        try {
            iArr2[CATEGORY.CALENDAR_INFORMATION.ordinal()] = 30;
        } catch (NoSuchFieldError unused8) {
        }
        try {
            iArr2[CATEGORY.CONTACT_INFORMATION.ordinal()] = 29;
        } catch (NoSuchFieldError unused9) {
        }
        try {
            iArr2[CATEGORY.DATABASE_INFORMATION.ordinal()] = 12;
        } catch (NoSuchFieldError unused10) {
        }
        try {
            iArr2[CATEGORY.EMAIL.ordinal()] = 18;
        } catch (NoSuchFieldError unused11) {
        }
        try {
            iArr2[CATEGORY.EMAIL_INFORMATION.ordinal()] = 8;
        } catch (NoSuchFieldError unused12) {
        }
        try {
            iArr2[CATEGORY.EMAIL_SETTINGS.ordinal()] = 24;
        } catch (NoSuchFieldError unused13) {
        }
        try {
            iArr2[CATEGORY.FILE.ordinal()] = 25;
        } catch (NoSuchFieldError unused14) {
        }
        try {
            iArr2[CATEGORY.FILE_INFORMATION.ordinal()] = 9;
        } catch (NoSuchFieldError unused15) {
        }
        try {
            iArr2[CATEGORY.HARDWARE_INFO.ordinal()] = 3;
        } catch (NoSuchFieldError unused16) {
        }
        try {
            iArr2[CATEGORY.IMAGE.ordinal()] = 32;
        } catch (NoSuchFieldError unused17) {
        }
        try {
            iArr2[CATEGORY.INTER_APP_COMMUNICATION.ordinal()] = 15;
        } catch (NoSuchFieldError unused18) {
        }
        try {
            iArr2[CATEGORY.LOCATION_INFORMATION.ordinal()] = 5;
        } catch (NoSuchFieldError unused19) {
        }
        try {
            iArr2[CATEGORY.LOG.ordinal()] = 26;
        } catch (NoSuchFieldError unused20) {
        }
        try {
            iArr2[CATEGORY.NETWORK.ordinal()] = 23;
        } catch (NoSuchFieldError unused21) {
        }
        try {
            iArr2[CATEGORY.NETWORK_INFORMATION.ordinal()] = 6;
        } catch (NoSuchFieldError unused22) {
        }
        try {
            iArr2[CATEGORY.NFC.ordinal()] = 34;
        } catch (NoSuchFieldError unused23) {
        }
        try {
            iArr2[CATEGORY.NO_CATEGORY.ordinal()] = 2;
        } catch (NoSuchFieldError unused24) {
        }
        try {
            iArr2[CATEGORY.PHONE_CONNECTION.ordinal()] = 14;
        } catch (NoSuchFieldError unused25) {
        }
        try {
            iArr2[CATEGORY.PHONE_INFORMATION.ordinal()] = 13;
        } catch (NoSuchFieldError unused26) {
        }
        try {
            iArr2[CATEGORY.PHONE_STATE.ordinal()] = 17;
        } catch (NoSuchFieldError unused27) {
        }
        try {
            iArr2[CATEGORY.SMS_MMS.ordinal()] = 28;
        } catch (NoSuchFieldError unused28) {
        }
        try {
            iArr2[CATEGORY.SYNCHRONIZATION_DATA.ordinal()] = 22;
        } catch (NoSuchFieldError unused29) {
        }
        try {
            iArr2[CATEGORY.SYSTEM_SETTINGS.ordinal()] = 31;
        } catch (NoSuchFieldError unused30) {
        }
        try {
            iArr2[CATEGORY.UNIQUE_IDENTIFIER.ordinal()] = 4;
        } catch (NoSuchFieldError unused31) {
        }
        try {
            iArr2[CATEGORY.VIDEO.ordinal()] = 21;
        } catch (NoSuchFieldError unused32) {
        }
        try {
            iArr2[CATEGORY.VOIP.ordinal()] = 16;
        } catch (NoSuchFieldError unused33) {
        }
        try {
            iArr2[CATEGORY.VOIP_INFORMATION.ordinal()] = 11;
        } catch (NoSuchFieldError unused34) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$android$data$CategoryDefinition$CATEGORY = iArr2;
        return iArr2;
    }

    public CategoryDefinition(CATEGORY systemCategory) {
        this(systemCategory, null);
    }

    public CategoryDefinition(CATEGORY systemCategory, String customCategory) {
        this(systemCategory, customCategory, null);
    }

    public CategoryDefinition(CATEGORY systemCategory, String customCategory, String customDescription) {
        this.systemCategory = null;
        this.systemCategory = systemCategory;
        this.customCategory = customCategory;
        this.customDescription = customDescription;
    }

    public CATEGORY getSystemCategory() {
        return this.systemCategory;
    }

    public void setSystemCategory(CATEGORY systemCategory) {
        this.systemCategory = systemCategory;
    }

    public String getCustomCategory() {
        return this.customCategory;
    }

    public void setCustomCategory(String customCategory) {
        this.customCategory = customCategory;
    }

    public String getCustomDescription() {
        return this.customDescription;
    }

    public void setCustomDescription(String customDescription) {
        this.customDescription = customDescription;
    }

    public int hashCode() {
        int result = (31 * 1) + (this.customCategory == null ? 0 : this.customCategory.hashCode());
        return (31 * ((31 * result) + (this.customDescription == null ? 0 : this.customDescription.hashCode()))) + (this.systemCategory == null ? 0 : this.systemCategory.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CategoryDefinition other = (CategoryDefinition) obj;
        if (this.customCategory == null) {
            if (other.customCategory != null) {
                return false;
            }
        } else if (!this.customCategory.equals(other.customCategory)) {
            return false;
        }
        if (this.customDescription == null) {
            if (other.customDescription != null) {
                return false;
            }
        } else if (!this.customDescription.equals(other.customDescription)) {
            return false;
        }
        if (this.systemCategory != other.systemCategory) {
            return false;
        }
        return true;
    }

    public String toString() {
        if (this.customCategory == null || this.customCategory.isEmpty()) {
            if (this.systemCategory != null) {
                return this.systemCategory.toString();
            }
            return "<invalid>";
        }
        return this.customCategory;
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
    public String getHumanReadableDescription() {
        if (this.customCategory != null && !this.customCategory.isEmpty()) {
            if (this.customDescription != null && !this.customDescription.isEmpty()) {
                return this.customDescription;
            }
            return this.customCategory;
        } else if (this.systemCategory != null) {
            switch ($SWITCH_TABLE$soot$jimple$infoflow$android$data$CategoryDefinition$CATEGORY()[this.systemCategory.ordinal()]) {
                case 1:
                    return "All Categories";
                case 2:
                    return "No Category";
                case 3:
                    return "Hardware Information";
                case 4:
                    return "Unique Identifier";
                case 5:
                    return "Location Information";
                case 6:
                    return "Network Information";
                case 7:
                    return "Account Information";
                case 8:
                    return "E-Mail Information";
                case 9:
                    return "File Information";
                case 10:
                    return "Bluetooth Information";
                case 11:
                    return "Voice-over-IP Information";
                case 12:
                    return "Database Information";
                case 13:
                    return "Phone Information";
                case 14:
                    return "Phone (Line) Connection";
                case 15:
                    return "Inter-App Communication";
                case 16:
                    return "Voice-over-IP";
                case 17:
                    return "Phone State";
                case 18:
                    return "E-Mail";
                case 19:
                    return "Bluetooth";
                case 20:
                    return "Account Settings";
                case 21:
                    return "Video";
                case 22:
                    return "Synchronization Data";
                case 23:
                    return "Network";
                case 24:
                    return "E-Mail Settings";
                case 25:
                    return "File";
                case 26:
                    return "Log Files";
                case 27:
                    return "Audio";
                case 28:
                    return "SMS / MMS";
                case 29:
                    return "Contact Information";
                case 30:
                    return "Calendar Information";
                case 31:
                    return "System Settings";
                case 32:
                    return "Image";
                case 33:
                    return "Browser Information";
                case 34:
                    return "NFC";
                default:
                    return "<invalid system category>";
            }
        } else {
            return "<invalid>";
        }
    }

    public CategoryDefinition getIdOnlyDescription() {
        return new CategoryDefinition(this.systemCategory, this.customCategory);
    }

    @Override // soot.jimple.infoflow.sourcesSinks.definitions.ISourceSinkCategory
    public String getID() {
        if (this.systemCategory == null || this.systemCategory == CATEGORY.NO_CATEGORY) {
            return this.customCategory;
        }
        return this.systemCategory.name();
    }
}
