package lexer;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 21:27
 */
public class Token {
    public final int tag;

    public Token(int t) {
        tag = t;
    }

    @Override
    public String toString() {
        return "" + (char) tag;
    }
}
