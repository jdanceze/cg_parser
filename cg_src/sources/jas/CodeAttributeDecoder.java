package jas;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
/* loaded from: gencallgraphv3.jar:jasmin-3.0.3-SNAPSHOT.jar:jas/CodeAttributeDecoder.class */
class CodeAttributeDecoder {
    CodeAttributeDecoder() {
    }

    public static byte[] decode(String attr, Hashtable labelToPc) {
        List<byte[]> attributeHunks = new LinkedList();
        int attributeSize = 0;
        int tableSize = 0;
        StringTokenizer st = new StringTokenizer(attr, "%", true);
        boolean isLabel = false;
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.equals("%")) {
                isLabel = !isLabel;
            } else if (isLabel) {
                Integer pc = (Integer) labelToPc.get(token);
                if (pc == null) {
                    throw new RuntimeException("PC is null, the token is " + token);
                }
                int pcvalue = pc.intValue();
                if (pcvalue > 65535) {
                    throw new RuntimeException("PC great than 65535, the token is " + token + " : " + pcvalue);
                }
                byte[] pcArray = {(byte) ((pcvalue >> 8) & 255), (byte) (pcvalue & 255)};
                attributeHunks.add(pcArray);
                attributeSize += 2;
                tableSize++;
            } else {
                byte[] hunk = Base64.decode(token.toCharArray());
                attributeSize += hunk.length;
                attributeHunks.add(hunk);
            }
        }
        int attributeSize2 = attributeSize + 2;
        byte[] attributeValue = new byte[attributeSize2];
        attributeValue[0] = (byte) ((tableSize >> 8) & 255);
        attributeValue[1] = (byte) (tableSize & 255);
        int index = 2;
        for (byte[] hunk2 : attributeHunks) {
            for (byte b : hunk2) {
                int i = index;
                index++;
                attributeValue[i] = b;
            }
        }
        if (index != attributeSize2) {
            throw new RuntimeException("Index does not euqal to attrubute size :" + index + " -- " + attributeSize2);
        }
        return attributeValue;
    }
}
