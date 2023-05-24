package soot.tagkit;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Unit;
import soot.UnitBox;
import soot.baf.Baf;
import soot.options.Options;
/* loaded from: gencallgraphv3.jar:soot/tagkit/CodeAttribute.class */
public class CodeAttribute extends JasminAttribute {
    private static final Logger logger = LoggerFactory.getLogger(CodeAttribute.class);
    protected List<Unit> mUnits;
    protected List<Tag> mTags;
    private final String name;
    private byte[] value;

    public CodeAttribute() {
        this("CodeAtribute");
    }

    public CodeAttribute(String name) {
        this.name = name;
    }

    public CodeAttribute(String name, List<Unit> units, List<Tag> tags) {
        this.name = name;
        this.mUnits = units;
        this.mTags = tags;
    }

    public String toString() {
        return this.name;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return this.name;
    }

    @Override // soot.tagkit.Attribute
    public void setValue(byte[] v) {
        this.value = v;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() throws AttributeValueException {
        if (this.value == null) {
            throw new AttributeValueException();
        }
        return this.value;
    }

    @Override // soot.tagkit.JasminAttribute
    public String getJasminValue(Map<Unit, String> instToLabel) {
        if (this.mTags.size() != this.mUnits.size()) {
            throw new RuntimeException("Sizes must match!");
        }
        StringBuilder buf = new StringBuilder();
        Iterator<Unit> unitIt = this.mUnits.iterator();
        for (Tag tag : this.mTags) {
            Unit unit = unitIt.next();
            buf.append('%').append(instToLabel.get(unit));
            buf.append('%').append(new String(Base64.encode(tag.getValue())));
        }
        return buf.toString();
    }

    public List<UnitBox> getUnitBoxes() {
        List<UnitBox> unitBoxes = new ArrayList<>(this.mUnits.size());
        for (Unit next : this.mUnits) {
            unitBoxes.add(Baf.v().newInstBox(next));
        }
        return unitBoxes;
    }

    @Override // soot.tagkit.JasminAttribute
    public byte[] decode(String attr, Hashtable<String, Integer> labelToPc) {
        if (Options.v().verbose()) {
            logger.debug("[] JasminAttribute decode...");
        }
        List<byte[]> attributeHunks = new LinkedList<>();
        int attributeSize = 0;
        int tablesize = 0;
        boolean isLabel = attr.startsWith("%");
        StringTokenizer st = new StringTokenizer(attr, "%");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (isLabel) {
                Integer pc = labelToPc.get(token);
                if (pc == null) {
                    throw new RuntimeException("PC is null, the token is " + token);
                }
                int pcvalue = pc.intValue();
                if (pcvalue > 65535) {
                    throw new RuntimeException("PC great than 65535, the token is " + token + " : " + pcvalue);
                }
                attributeHunks.add(new byte[]{(byte) (pcvalue & 255), (byte) ((pcvalue >> 8) & 255)});
                attributeSize += 2;
                tablesize++;
            } else {
                byte[] hunk = Base64.decode(token.toCharArray());
                attributeSize += hunk.length;
                attributeHunks.add(hunk);
            }
            isLabel = !isLabel;
        }
        int attributeSize2 = attributeSize + 2;
        byte[] attributeValue = new byte[attributeSize2];
        attributeValue[0] = (byte) ((tablesize >> 8) & 255);
        attributeValue[1] = (byte) (tablesize & 255);
        int index = 2;
        for (byte[] hunk2 : attributeHunks) {
            for (byte element : hunk2) {
                int i = index;
                index++;
                attributeValue[i] = element;
            }
        }
        if (index != attributeSize2) {
            throw new RuntimeException("Index does not euqal to attrubute size : " + index + " -- " + attributeSize2);
        }
        if (Options.v().verbose()) {
            logger.debug("[] Jasmin.decode finished...");
        }
        return attributeValue;
    }
}
