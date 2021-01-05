package parse;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/12/7 20:14
 */


public class Parse {
    private static final File file = new File("src/test.c");
    private Lexer lexer;
    private Token token;
    private List<StringBuilder> list; //存储First集合
    private Hashtable<String, String> hashtable; //存储终结符和与之对应的First集合

    /*
     * 递归下降程序应该先对后面的非终结符创建对应的子程序
     * 第一个版本写的不太好，下面应该考虑能不能自动生成first集合
     * */
    public static void main(String[] args) {
        try {
            FileReader fileReader = new FileReader(file);
            int length = (int) file.length();
            Parse parse = new Parse();
            parse.lexer = new Lexer(fileReader, length);
            int n = length;
            parse.move();//读入第一个词法单元 (
            parse.program();
            System.out.println("语法分析成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*
     * program->basic id block
     * */
    public void program() {
        if (token.tag == Tag.BASIC || token.tag == Tag.VOID) {
            move();
            if (token.tag == Tag.ID) {
                move();
                match('(');
                match(')');
                block();
            } else {
                error();
            }
        } else {
            error();
        }
    }

    private void block() {
        match('{');
        decls();
        stmts();
        match('}');
    }

    private void stmts() {
        stmts1();
    }

    private void stmts1() {
        if (token.tag == Tag.IF || token.tag == Tag.WHILE ||
                token.tag == Tag.DO || token.tag == Tag.ID || token.tag == '{' || token.tag == Tag.BREAK) {
            stmt();
            stmts1();
        }

    }

    private void stmt() {
        if (token.tag == Tag.ID) {
            //不在该block中移动，因为文法这里是前往lval
            lval();
            match('=');
            bool();
            match(';');
        } else if (token.tag == Tag.IF) {
            move();//在这里移动，因为文法这里是终结符
            match('(');
            bool();
            match(')');
            stmt();
            if (token.tag == Tag.ELSE) {
                move();
                stmt();
            }
        } else if (token.tag == Tag.WHILE) {
            move();
            match('(');
            bool();
            match(')');
            stmt();
        } else if (token.tag == Tag.DO) {
            move();
            stmt();
            if (token.tag == Tag.WHILE)
                move();
            else
                error();
            match('(');
            bool();
            match(')');
            match(';');
        } else if (token.tag == Tag.BREAK) {
            move();
            match(';');
        } else if (token.tag == '{') {
            move();
            decls();
            stmts();
            match('}');
        }
    }

    private void lval() {
        if (token.tag == Tag.ID)
            move();
        else
            error();
        lval1();

    }

    private void lval1() {
        if (token.tag == '[') {
            match('[');
            bool();
            match(']');
            lval1();
        }
        //else do nothing
    }

    private void decls() {
        decls1();
    }

    private void decls1() {
        if (token.tag == Tag.BASIC) {
            decl();
            decls1();
        }
        //else do nothing

    }

    private void decl() {
        type();
        if (token.tag == Tag.ID)
            move();
        else
            error();
        match(';');
    }

    private void type() {
        if (token.tag == Tag.BASIC)
            move();
        else
            error();
        type1();
    }

    private void type1() {
        if (token.tag == '[') {
            match('[');
            if (token.tag == Tag.NUM)
                move();
            else
                error();
            match(']');
            type1();
        }
        //else do nothing
    }


    /*
     * bool作为表达式的入口，对于任何的终结符都只有一个出口
     * */
    public void bool() {
        join();
        bool1();
    }

    private void bool1() {
        if (token.tag == Tag.OR) {
            move();
            join();
            bool1();
        }
        //else do nothing
    }

    private void join() {
        equ();
        join1();
    }

    /*
     * First(join')={&&} or First(join')=Follow(join)=First(bool')={||}
     * */
    private void join1() {
        if (token.tag == Tag.AND) {
            move();
            equ();
            join1();
        }
        //else do nothing
    }

    private void equ() {
        rel();
        equ1();
    }

    private void equ1() {
        if (token.tag == Tag.EQ) {
            move();
            rel();
            equ1();
        } else if (token.tag == Tag.NE) {
            move();
            rel();
            equ1();
        }
        //else do nothing
    }

    private void rel() {
        expr();
        /*
         * 之所以写成重复的样子，是感觉之后的语义分析可能得具体情况具体分析
         * */
        if (token.tag == '<') {
            match('<');
            expr();
        } else if (token.tag == '>') {
            match('>');
            expr();
        } else if (token.tag == Tag.LE) {
            move();
            expr();
        } else if (token.tag == Tag.GE) {
            move();
            expr();
        }
        //else do nothing
    }

    private void expr() {
        term();
        expr1();
    }

    private void expr1() {
        if (token.tag == '+') {
            match('+');
            term();
            expr1();
        } else if (token.tag == '-') {
            match('-');
            term();
            expr1();
        }

        //else do nothing
    }

    private void term() {
        unary();
        term1();
    }

    private void term1() {
        if (token.tag == '*') {
            match('*');
            unary();
            term1();
        } else if (token.tag == '/') {
            match('/');
            unary();
            term1();
        }
    }

    private void unary() {
        if (token.tag == '!') {
            match('!');
            unary();
        } else if (token.tag == Tag.MINUS) {
            move();
            unary();
        } else {
            factor();
        }
    }

    private void factor() {
        switch (token.tag) {
            case '(':
                match('(');
                bool();
                match(')');
                break;
            case Tag.NUM:
            case Tag.FALSE:
            case Tag.ID:
            case Tag.TRUE:
            case Tag.REAL:
                move();
                break;
        }
    }


    public void move() {
        token = lexer.scan();
    }

    public void error() {
        throw new Error("near line " + lexer.line + ": sys error");
    }

    public void error(int t) {
        char s = (char) t;
        throw new Error("near line " + lexer.line + ": expected "+s);
    }

    //匹配当前词法单元，然后移动到下一个词法单元
    public void match(int t) {
        if (token.tag == t) move();
        else error(t);
    }
}
