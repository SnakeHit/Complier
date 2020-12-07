package lexer;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 21:35
 */
/* Word 用于管理保留字、标识符以及复合词法单元的词素*/
public class Word extends Token {
    public static final Word
            and = new Word("&&", Tag.AND), or = new Word("||", Tag.OR),
            eq = new Word("==", Tag.EQ), ne = new Word("!=", Tag.NE),
            le = new Word("<=", Tag.LE), ge = new Word(">=", Tag.GE),
            shl = new Word("<<", Tag.SHL), shr = new Word(">>", Tag.SHR),
            pa = new Word("+=", Tag.PA), sa = new Word("-=", Tag.SA),
            ma = new Word("*=", Tag.MA), da = new Word("/=", Tag.DA),
            ra = new Word("%=", Tag.RA), aa = new Word("&=", Tag.AA),
            oa = new Word("|=", Tag.OA), xa = new Word("^=", Tag.XA),
            sp = new Word("++", Tag.SP), sm = new Word("--", Tag.SM),
            minus = new Word("minus", Tag.MINUS),
            True = new Word("true", Tag.TRUE),
            False = new Word("false", Tag.FALSE),
            temp = new Word("t", Tag.TEMP);
    public String lexeme = "";

    public Word(String s, int tag) {
        super(tag);
        lexeme = s;
    }

    @Override
    public String toString() {
        return lexeme;
    }
}
