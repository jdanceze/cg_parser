package org.powermock.modules.junit4.common.internal.impl;
/* loaded from: gencallgraphv3.jar:powermock-module-junit4-common-2.0.9.jar:org/powermock/modules/junit4/common/internal/impl/VersionTokenizer.class */
class VersionTokenizer {
    private final String _versionString;
    private final int _length;
    private int _position;
    private int _number;
    private String _suffix;

    public int getNumber() {
        return this._number;
    }

    public String getSuffix() {
        return this._suffix;
    }

    public VersionTokenizer(String versionString) {
        if (versionString == null) {
            throw new IllegalArgumentException("versionString is null");
        }
        this._versionString = versionString;
        this._length = versionString.length();
    }

    public boolean MoveNext() {
        char c;
        this._number = 0;
        this._suffix = "";
        if (this._position >= this._length) {
            return false;
        }
        while (this._position < this._length && (c = this._versionString.charAt(this._position)) >= '0' && c <= '9') {
            this._number = (this._number * 10) + (c - '0');
            this._position++;
        }
        int suffixStart = this._position;
        while (this._position < this._length && this._versionString.charAt(this._position) != '.') {
            this._position++;
        }
        this._suffix = this._versionString.substring(suffixStart, this._position);
        if (this._position < this._length) {
            this._position++;
            return true;
        }
        return true;
    }
}
