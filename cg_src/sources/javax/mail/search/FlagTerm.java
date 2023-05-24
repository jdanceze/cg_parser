package javax.mail.search;

import javax.mail.Flags;
import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/search/FlagTerm.class */
public final class FlagTerm extends SearchTerm {
    protected boolean set;
    protected Flags flags;

    public FlagTerm(Flags flags, boolean set) {
        this.flags = flags;
        this.set = set;
    }

    public Flags getFlags() {
        return (Flags) this.flags.clone();
    }

    public boolean getTestSet() {
        return this.set;
    }

    @Override // javax.mail.search.SearchTerm
    public boolean match(Message msg) {
        try {
            Flags f = msg.getFlags();
            if (this.set) {
                if (f.contains(this.flags)) {
                    return true;
                }
                return false;
            }
            Flags.Flag[] sf = this.flags.getSystemFlags();
            for (Flags.Flag flag : sf) {
                if (f.contains(flag)) {
                    return false;
                }
            }
            String[] s = this.flags.getUserFlags();
            for (String str : s) {
                if (f.contains(str)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FlagTerm)) {
            return false;
        }
        FlagTerm ft = (FlagTerm) obj;
        return ft.set == this.set && ft.flags.equals(this.flags);
    }

    public int hashCode() {
        return this.set ? this.flags.hashCode() : this.flags.hashCode() ^ (-1);
    }
}
