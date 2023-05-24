package soot.jimple.spark.geom.dataRep;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/geom/dataRep/SimpleInterval.class */
public class SimpleInterval implements Comparable<SimpleInterval> {
    public long L;
    public long R;

    public SimpleInterval() {
        this.L = 0L;
        this.R = 1L;
    }

    public SimpleInterval(long l, long r) {
        this.L = l;
        this.R = r;
    }

    public SimpleInterval(SimpleInterval o) {
        this.L = o.L;
        this.R = o.R;
    }

    public String toString() {
        return "[" + this.L + ", " + this.R + ")";
    }

    public boolean equals(Object o) {
        SimpleInterval other = (SimpleInterval) o;
        return other.L == this.L && other.R == this.R;
    }

    public int hashCode() {
        int ans = (int) ((this.L + this.R) % 2147483647L);
        if (ans < 0) {
            ans = -ans;
        }
        return ans;
    }

    @Override // java.lang.Comparable
    public int compareTo(SimpleInterval o) {
        return this.L == o.L ? this.R < o.R ? -1 : 1 : this.L < o.L ? -1 : 1;
    }

    public boolean contains(SimpleInterval o) {
        if (this.L <= o.L && this.R >= o.R) {
            return true;
        }
        return false;
    }

    public boolean merge(SimpleInterval o) {
        if (o.L < this.L) {
            if (this.L <= o.R) {
                this.L = o.L;
                if (this.R < o.R) {
                    this.R = o.R;
                    return true;
                }
                return true;
            }
            return false;
        } else if (o.L <= this.R) {
            if (this.R < o.R) {
                this.R = o.R;
                return true;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean intersect(SimpleInterval o) {
        if (this.L > o.L || o.L >= this.R) {
            if (o.L <= this.L && this.L < o.R) {
                return true;
            }
            return false;
        }
        return true;
    }
}
