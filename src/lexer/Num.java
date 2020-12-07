package lexer;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 21:30
 */
public class Num extends Token {
    public final int value;

    public Num(int v) {
        super(Tag.NUM);
        value = v;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
