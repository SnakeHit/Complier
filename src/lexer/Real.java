package lexer;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 21:33
 */

public class Real extends Token {
    public final float value;

    public Real(float v) {
        super(Tag.REAL);
        value = v;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
