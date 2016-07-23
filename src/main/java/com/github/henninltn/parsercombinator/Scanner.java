package com.github.henninltn.parsercombinator;

public class Scanner {

    private final String src;
    private int pos, line, col;

    public Scanner(String s) {
        this.src = s;
        line = col = 1;
    }

    @Override
    public Scanner clone() {
        Scanner ret = new Scanner(src);
        ret.pos = pos;
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Scanner)) {
            return false;
        }
        Scanner scnr = (Scanner) obj;
        return src.equals(scnr.src) && pos == scnr.pos;
    }

    public final char peek() throws Exception {
        if (pos >= src.length()) {
            throw new Exception(ex("too short"));
        }
        return src.charAt(pos);
    }

    public final void next() throws Exception {
        char chr = peek();
        if (chr == '\n') {
            ++line;
            col = 0;
        }
        ++pos;
        ++col;
    }

    // バックトラック
    public final void revert(Scanner scnr) throws Exception {
        if (!src.equals(scnr.src)) {
            throw new Exception(ex("can not revert"));
        }
        pos = scnr.pos;
    }

    // エラーメッセージのカスタマイズ
    public final String ex(String errMsg) {
        String ret = "[line " + line + ",col " + col + "] " + errMsg;
        if (src != null && 0 <= pos && pos < src.length()) {
            ret += ": '" + src.charAt(pos) + "'";
        }
        return ret;
    }
}
