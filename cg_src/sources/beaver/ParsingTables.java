package beaver;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;
/* loaded from: gencallgraphv3.jar:beaver/ParsingTables.class */
public final class ParsingTables {
    private final short[] actions;
    final short[] lookaheads;
    final int[] actn_offsets;
    private final int[] goto_offsets;
    private final short[] default_actions;
    final int[] rule_infos;
    final short error_symbol_id;
    final boolean compressed;
    final int n_term;
    static final int UNUSED_OFFSET = Integer.MIN_VALUE;

    public ParsingTables(Class impl_class) {
        this(getSpecAsResourceStream(impl_class));
    }

    public ParsingTables(String spec) {
        this(new ByteArrayInputStream(decode(spec)));
    }

    private ParsingTables(InputStream in) {
        try {
            DataInputStream data = new DataInputStream(new InflaterInputStream(in));
            int len = data.readInt();
            this.actions = new short[len];
            for (int i = 0; i < len; i++) {
                this.actions[i] = data.readShort();
            }
            this.lookaheads = new short[len];
            for (int i2 = 0; i2 < len; i2++) {
                this.lookaheads[i2] = data.readShort();
            }
            int len2 = data.readInt();
            this.actn_offsets = new int[len2];
            for (int i3 = 0; i3 < len2; i3++) {
                this.actn_offsets[i3] = data.readInt();
            }
            this.goto_offsets = new int[len2];
            for (int i4 = 0; i4 < len2; i4++) {
                this.goto_offsets[i4] = data.readInt();
            }
            int len3 = data.readInt();
            this.compressed = len3 != 0;
            if (this.compressed) {
                this.default_actions = new short[len3];
                for (int i5 = 0; i5 < len3; i5++) {
                    this.default_actions[i5] = data.readShort();
                }
            } else {
                this.default_actions = null;
            }
            int min_nt_id = Integer.MAX_VALUE;
            int len4 = data.readInt();
            this.rule_infos = new int[len4];
            for (int i6 = 0; i6 < len4; i6++) {
                this.rule_infos[i6] = data.readInt();
                min_nt_id = Math.min(min_nt_id, this.rule_infos[i6] >>> 16);
            }
            this.n_term = min_nt_id;
            this.error_symbol_id = data.readShort();
            data.close();
        } catch (IOException e) {
            throw new IllegalStateException("cannot initialize parser tables: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final short findFirstTerminal(int state) {
        int index;
        int offset = this.actn_offsets[state];
        short s = offset < 0 ? (short) (-offset) : (short) 0;
        while (true) {
            short term_id = s;
            if (term_id < this.n_term && (index = offset + term_id) < this.lookaheads.length) {
                if (this.lookaheads[index] != term_id) {
                    s = (short) (term_id + 1);
                } else {
                    return term_id;
                }
            } else {
                return (short) -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final short findParserAction(int state, short lookahead) {
        int index;
        int index2 = this.actn_offsets[state];
        if (index2 != Integer.MIN_VALUE && (index = index2 + lookahead) >= 0 && index < this.actions.length && this.lookaheads[index] == lookahead) {
            return this.actions[index];
        }
        if (this.compressed) {
            return this.default_actions[state];
        }
        return (short) 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final short findNextState(int state, short lookahead) {
        int index;
        int index2 = this.goto_offsets[state];
        if (index2 != Integer.MIN_VALUE && (index = index2 + lookahead) >= 0 && index < this.actions.length && this.lookaheads[index] == lookahead) {
            return this.actions[index];
        }
        if (this.compressed) {
            return this.default_actions[state];
        }
        return (short) 0;
    }

    static byte[] decode(String spec) {
        char[] chars = spec.toCharArray();
        if (chars.length % 4 != 0) {
            throw new IllegalArgumentException("corrupted encoding");
        }
        int len = (chars.length / 4) * 3;
        byte[] bytes = new byte[chars[chars.length - 1] == '=' ? chars[chars.length - 2] == '=' ? len - 2 : len - 1 : len];
        int len2 = len - 3;
        int ci = 0;
        int bi = 0;
        while (bi < len2) {
            int i = ci;
            int ci2 = ci + 1;
            int ci3 = ci2 + 1;
            int ci4 = ci3 + 1;
            ci = ci4 + 1;
            int acc = (decode(chars[i]) << 18) | (decode(chars[ci2]) << 12) | (decode(chars[ci3]) << 6) | decode(chars[ci4]);
            int i2 = bi;
            int bi2 = bi + 1;
            bytes[i2] = (byte) (acc >> 16);
            int bi3 = bi2 + 1;
            bytes[bi2] = (byte) ((acc >> 8) & 255);
            bi = bi3 + 1;
            bytes[bi3] = (byte) (acc & 255);
        }
        int i3 = ci;
        int ci5 = ci + 1;
        int ci6 = ci5 + 1;
        int ci7 = ci6 + 1;
        int i4 = ci7 + 1;
        int acc2 = (decode(chars[i3]) << 18) | (decode(chars[ci5]) << 12) | (decode(chars[ci6]) << 6) | decode(chars[ci7]);
        int i5 = bi;
        int bi4 = bi + 1;
        bytes[i5] = (byte) (acc2 >> 16);
        if (bi4 < bytes.length) {
            int bi5 = bi4 + 1;
            bytes[bi4] = (byte) ((acc2 >> 8) & 255);
            if (bi5 < bytes.length) {
                int i6 = bi5 + 1;
                bytes[bi5] = (byte) (acc2 & 255);
            }
        }
        return bytes;
    }

    static int decode(char c) {
        if (c <= '9') {
            if (c >= '0') {
                return c - '0';
            }
            if (c == '#') {
                return 62;
            }
            if (c == '$') {
                return 63;
            }
        } else if (c <= 'Z') {
            if (c >= 'A') {
                return (c - 'A') + 10;
            }
            if (c == '=') {
                return 0;
            }
        } else if ('a' <= c && c <= 'z') {
            return (c - 'a') + 36;
        }
        throw new IllegalStateException("illegal encoding character '" + c + "'");
    }

    static InputStream getSpecAsResourceStream(Class impl_class) {
        String name = impl_class.getName();
        InputStream spec_stream = impl_class.getResourceAsStream(String.valueOf(name.substring(name.lastIndexOf(46) + 1)) + ".spec");
        if (spec_stream == null) {
            throw new IllegalStateException("parser specification not found");
        }
        return spec_stream;
    }
}
