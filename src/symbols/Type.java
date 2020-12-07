package symbols;

import lexer.Tag;
import lexer.Word;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 21:53
 */

public class Type extends Word {
    public static final Type
            Int = new Type("int", Tag.INT, 4),
            Float = new Type("float", Tag.FLOAT, 8),
            Char = new Type("char", Tag.CHAR, 1);
    public int width = 0;

    public Type(String s, int tag, int w) {
        super(s, tag);
        width = w;
    }

    public static boolean numeric(Type p) {
        return p == Type.Char || p == Type.Int || p == Type.Float;
    }

    public static Type max(Type p1, Type p2) {
        if (!numeric(p1) || !numeric(p2))
            return null;
        else if (p1 == Type.Float || p2 == Type.Float)
            return Type.Float;
        else if (p1 == Type.Int || p2 == Type.Int)
            return Type.Int;
        else
            return Type.Char;
    }
}