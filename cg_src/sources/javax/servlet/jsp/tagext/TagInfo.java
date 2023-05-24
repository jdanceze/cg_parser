package javax.servlet.jsp.tagext;

import soot.JavaBasicTypes;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/jsp/tagext/TagInfo.class */
public class TagInfo {
    public static final String BODY_CONTENT_JSP = "JSP";
    public static final String BODY_CONTENT_TAG_DEPENDENT = "TAGDEPENDENT";
    public static final String BODY_CONTENT_EMPTY = "EMPTY";
    public static final String BODY_CONTENT_SCRIPTLESS = "SCRIPTLESS";
    private String tagName;
    private String tagClassName;
    private String bodyContent;
    private String infoString;
    private TagLibraryInfo tagLibrary;
    private TagExtraInfo tagExtraInfo;
    private TagAttributeInfo[] attributeInfo;
    private String displayName;
    private String smallIcon;
    private String largeIcon;
    private TagVariableInfo[] tagVariableInfo;
    private boolean dynamicAttributes;

    public TagInfo(String tagName, String tagClassName, String bodycontent, String infoString, TagLibraryInfo taglib, TagExtraInfo tagExtraInfo, TagAttributeInfo[] attributeInfo) {
        this.tagName = tagName;
        this.tagClassName = tagClassName;
        this.bodyContent = bodycontent;
        this.infoString = infoString;
        this.tagLibrary = taglib;
        this.tagExtraInfo = tagExtraInfo;
        this.attributeInfo = attributeInfo;
        if (tagExtraInfo != null) {
            tagExtraInfo.setTagInfo(this);
        }
    }

    public TagInfo(String tagName, String tagClassName, String bodycontent, String infoString, TagLibraryInfo taglib, TagExtraInfo tagExtraInfo, TagAttributeInfo[] attributeInfo, String displayName, String smallIcon, String largeIcon, TagVariableInfo[] tvi) {
        this.tagName = tagName;
        this.tagClassName = tagClassName;
        this.bodyContent = bodycontent;
        this.infoString = infoString;
        this.tagLibrary = taglib;
        this.tagExtraInfo = tagExtraInfo;
        this.attributeInfo = attributeInfo;
        this.displayName = displayName;
        this.smallIcon = smallIcon;
        this.largeIcon = largeIcon;
        this.tagVariableInfo = tvi;
        if (tagExtraInfo != null) {
            tagExtraInfo.setTagInfo(this);
        }
    }

    public TagInfo(String tagName, String tagClassName, String bodycontent, String infoString, TagLibraryInfo taglib, TagExtraInfo tagExtraInfo, TagAttributeInfo[] attributeInfo, String displayName, String smallIcon, String largeIcon, TagVariableInfo[] tvi, boolean dynamicAttributes) {
        this.tagName = tagName;
        this.tagClassName = tagClassName;
        this.bodyContent = bodycontent;
        this.infoString = infoString;
        this.tagLibrary = taglib;
        this.tagExtraInfo = tagExtraInfo;
        this.attributeInfo = attributeInfo;
        this.displayName = displayName;
        this.smallIcon = smallIcon;
        this.largeIcon = largeIcon;
        this.tagVariableInfo = tvi;
        this.dynamicAttributes = dynamicAttributes;
        if (tagExtraInfo != null) {
            tagExtraInfo.setTagInfo(this);
        }
    }

    public String getTagName() {
        return this.tagName;
    }

    public TagAttributeInfo[] getAttributes() {
        return this.attributeInfo;
    }

    public VariableInfo[] getVariableInfo(TagData data) {
        VariableInfo[] result = null;
        TagExtraInfo tei = getTagExtraInfo();
        if (tei != null) {
            result = tei.getVariableInfo(data);
        } else {
            String idValue = data.getId();
            if (idValue != null) {
                result = new VariableInfo[]{new VariableInfo(idValue, JavaBasicTypes.JAVA_LANG_OBJECT, true, 0)};
            }
        }
        return result;
    }

    public boolean isValid(TagData data) {
        TagExtraInfo tei = getTagExtraInfo();
        if (tei == null) {
            return true;
        }
        return tei.isValid(data);
    }

    public ValidationMessage[] validate(TagData data) {
        TagExtraInfo tei = getTagExtraInfo();
        if (tei == null) {
            return null;
        }
        return tei.validate(data);
    }

    public void setTagExtraInfo(TagExtraInfo tei) {
        this.tagExtraInfo = tei;
    }

    public TagExtraInfo getTagExtraInfo() {
        return this.tagExtraInfo;
    }

    public String getTagClassName() {
        return this.tagClassName;
    }

    public String getBodyContent() {
        return this.bodyContent;
    }

    public String getInfoString() {
        return this.infoString;
    }

    public void setTagLibrary(TagLibraryInfo tl) {
        this.tagLibrary = tl;
    }

    public TagLibraryInfo getTagLibrary() {
        return this.tagLibrary;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getSmallIcon() {
        return this.smallIcon;
    }

    public String getLargeIcon() {
        return this.largeIcon;
    }

    public TagVariableInfo[] getTagVariableInfos() {
        return this.tagVariableInfo;
    }

    public boolean hasDynamicAttributes() {
        return this.dynamicAttributes;
    }
}
