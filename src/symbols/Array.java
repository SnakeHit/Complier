package symbols;

import lexer.Tag;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 22:47
 */
public class Array extends Type {
    public Type of;
    public int size = 1;

    public Array(int sz, Type p) {
        super("[]", Tag.INDEX, sz * p.width);
        size = sz;
        of = p;
    }

    public String toString() {
        return "[" + size + "]" + of.toString();
    }
}
