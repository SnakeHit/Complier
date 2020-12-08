package lexer;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 21:50
 */

import symbols.Type;

import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class Lexer {
    public static Hashtable<String, Word> words = new Hashtable<String, Word>();

    //方便求解first集合
    static {
        reserve(new Word("auto", Tag.AUTO));
        reserve(new Word("break", Tag.BREAK));
        reserve(new Word("case", Tag.CASE));
        reserve(new Word("const", Tag.CONST));
        reserve(new Word("continue", Tag.CONTINUE));
        reserve(new Word("default", Tag.DEFAULT));
        reserve(new Word("do", Tag.DO));
        reserve(new Word("double", Tag.DOUBLE));
        reserve(new Word("else", Tag.ELSE));
        reserve(new Word("enum", Tag.ENUM));
        reserve(new Word("extern", Tag.EXTERN));
        reserve(new Word("for", Tag.FOR));
        reserve(new Word("goto", Tag.GOTO));
        reserve(new Word("if", Tag.IF));
        reserve(new Word("long", Tag.LONG));
        reserve(new Word("while", Tag.WHILE));
        reserve(new Word("register", Tag.REGISTER));
        reserve(new Word("return", Tag.RETURN));
        reserve(new Word("short", Tag.SHORT));
        reserve(new Word("signed", Tag.SIGNED));
        reserve(new Word("sizeof", Tag.SIZEOF));
        reserve(new Word("static", Tag.STATIC));
        reserve(new Word("struct", Tag.STRUCT));
        reserve(new Word("switch", Tag.SWITCH));
        reserve(new Word("typedef", Tag.TYPEDEF));
        reserve(new Word("union", Tag.UNION));
        reserve(new Word("unsigned", Tag.UNSIGNED));
        reserve(new Word("void", Tag.VOID));
        reserve(new Word("volatile", Tag.VOLATILE));
        reserve(new Word("basic", Tag.BASIC));
        reserve(new Word("id", Tag.ID));
        reserve(Word.eq);
        reserve(Word.or);
        reserve(Word.and);
        reserve(Word.ne);
        reserve(Word.le);
        reserve(Word.ge);
        reserve(Word.shl);
        reserve(Word.shr);
        reserve(Word.pa);
        reserve(Word.sa);
        reserve(Word.ma);
        reserve(Word.da);
        reserve(Word.ra);
        reserve(Word.aa);
        reserve(Word.oa);
        reserve(Word.xa);
        reserve(Word.sp);
        reserve(Word.sm);
        reserve(new Word("num", Tag.NUM));
        reserve(new Word("real", Tag.REAL));
        //这里放非终结符
        reserve(Word.True);
        reserve(Word.False);
        reserve(Type.Int);
        reserve(Type.Char);
        reserve(Type.Float);

    }

    public int line = 1;
    public char[] chars;
    public int lookahead = 0; //指针
    public char peek = ' ';

    public Lexer(FileReader stream, int length) throws IOException {
        chars = new char[length + 1];
        chars[length] = '\0';
        stream.read(chars);
    }

    public static void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    public char readChar() {
        peek = chars[lookahead++];
        return peek;
    }

    /*向前看指针，如果匹配则指针向前移动一位，下次readChar()时指向下一个词法单元*/
    public boolean readChar(char c) {
        if (chars[lookahead] == c) {
            lookahead++;
            return true;
        } else {
            return false;
        }
    }

    public Token scan() {
        peek = readChar();
        while (true) {
            if (peek == ' ' || peek == '\t' || peek == '\r') {
                peek = readChar();
            } else if (peek == '\n') {
                line = line + 1;
                peek = readChar();
            } else
                break;
        }
        switch (peek) {
            case '%':
                if (readChar('=')) return Word.ra;
                else return new Token('%');
            case '^':
                if (readChar('=')) return Word.xa;
                else return new Token('^');
            case '~':
                return new Token('~');
            case '&':
                if (readChar('&')) return Word.and;
                else if (readChar('=')) return Word.aa;
                else return new Token('&');
            case '|':
                if (readChar('|')) return Word.or;
                else if (readChar('=')) return Word.oa;
                else return new Token('|');
            case '=':
                if (readChar('=')) return Word.eq;
                else return new Token('=');
            case '!':
                if (readChar('=')) return Word.ne;
                else return new Token('!');
            case '<':
                if (readChar('<')) return Word.shl;
                else if (readChar('=')) return Word.le;
                else return new Token('<');
            case '>':
                if (readChar('>')) return Word.shr;
                else if (readChar('=')) return Word.ge;
                else return new Token('>');
            case '+':
                if (readChar('+')) return Word.sp;
                else if (readChar('=')) return Word.pa;
                else return new Token('+');
            case '-':
                if (readChar('-')) return Word.sm;
                else if (readChar('=')) return Word.sa;
                else return new Token('-');
            case '*':
                if (readChar('=')) return Word.ma;
                else return new Token('*');
            case '/':
                if (readChar('/')) {
                    while (peek != '\n') {
                        readChar();
                    }
                    line++;
                    /*继续寻找*/
                    return scan();
                } else if (readChar('*')) {
                    readChar();
                    while (!(peek == '*' && chars[lookahead] == '/')) {
                        readChar();
                        if (peek == '\n') {
                            line++;
                        }
                    }
                    readChar();/* peek == '/' */
                    return scan();
                } else if (readChar('='))
                    return Word.da;
                else {
                    return new Token('/');
                }
            case '#':
                while (peek != '\n') {
                    readChar();
                }
                line++;
                return scan();
            case '{':
                return new Token('{');
            case '}':
                return new Token('}');
            case '[':
                return new Token('[');
        }

        if (Character.isDigit(peek)) {
            int v = 0;
            /*如果下一个字符是数字*/
            while (true) {
                v = 10 * v + Character.digit(peek, 10);
                if (Character.isDigit(chars[lookahead])) {
                    readChar();
                } else {
                    break;
                }
            }
            if (chars[lookahead] != '.') return new Num(v);
            readChar();/*  '.'  */
            float x = v;
            float d = 10;
            while (true) {
                if (Character.isDigit(chars[lookahead])) {
                    readChar();
                    x = x + Character.digit(peek, 10) / d;
                    d = d * 10;
                } else break;
            }
            return new Real(x);
        }

        // handle word
        if (Character.isLetter(peek)) {
            StringBuilder b = new StringBuilder();
            while (true) {
                b.append(peek);
                if (Character.isLetterOrDigit(chars[lookahead])) {
                    readChar();
                } else {
                    break;
                }
            }
            String s = b.toString();
            Word w = words.get(s);
            if (w == null) {
                w = new Word(s, Tag.ID);
                words.put(s, w);
            }
            return w;
        }
        /*其他字符*/
        Token t = new Token(peek);
        peek = ' ';
        return t;
    }
}