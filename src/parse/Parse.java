package parse;

import lexer.Lexer;
import lexer.Num;
import lexer.Tag;
import lexer.Token;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/12/1 16:52
 */
public class Parse {
    private static final File file = new File("D:\\Colleage\\LexAnalyze\\src\\test.c");
    private Lexer lexer;
    private Token token;

    public static void main(String[] args) {
        try {
            FileReader fileReader = new FileReader(file);
            int length = (int) file.length();
            Parse parse = new Parse();
            parse.lexer = new Lexer(fileReader, length);
            int n = length;
            parse.move();//读入第一个词法单元 (
            System.out.println(parse.E());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int F() {
        int val = 0;
        if (token.tag == Tag.NUM) {
            val = ((Num) token).value;
            match(Tag.NUM);//匹配，移动到下一个词法单元
        } else if (token.tag == '(') {
            match('(');
            val = E();
            if (token.tag == ')') {
                match(')');
            }
        } else {
            error();
        }
        return val; //强制类型转换
    }
//  (5-(2*3)*2)
    private int E() {
        int i = 0; // E1的继承属性
        int val = 0;
        i = T();
        val = E1(i);
        return val;
    }

    //E'->+TE'
    private int E1(int i) {
        int val = 0; //T的综合属性
        int i1 = 0; //E1的继承属性
        int s = 0; //综合属性
        if (token.tag == '+') {
            match('+');
            val = T();
            i1 = i + val;
            s = E1(i1);
        } else if (token.tag == '-') {
            match('-');
            val = T();
            i1 = i - val;
            s = E1(i1);
        } else {
            s = i;
        }
        return s;
    }

    private int T() {
        int i = 0;
        int val = 0;
        i = F();
        val = T1(i);
        return val;
    }

    private int T1(int i) {
        int val = 0; //F的综合属性
        int i1 = 0; //T1的继承属性
        int s = 0; //综合属性
        if (token.tag == '*') {
            match('*');
            val = F();
            i1 = i * val;
            s = T1(i1);
        } else if (token.tag == '/') {
            match('/');
            val = F();
            i1 = i / val;
            s = T1(i1);
        } else {
            s = i;
        }
        return s;
    }

    public void move() {
        token = lexer.scan();
    }

    public void error() {
        throw new Error("near line " + lexer.line + ": sys error");
    }
    //匹配当前词法单元，然后移动到下一个词法单元
    public void match(int t) {
        if (token.tag == t) move();
        else error();
    }
}
