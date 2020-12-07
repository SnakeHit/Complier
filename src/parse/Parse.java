package parse;

import lexer.Lexer;
import lexer.Tag;
import lexer.Token;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/12/7 20:14
 */
public class Parse {
    private static final File file = new File("D:\\Colleage\\LexAnalyze\\src\\test.c");
    private Lexer lexer;
    private Token token;

    /*
     * 递归下降程序应该先对后面的非终结符创建对应的子程序
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
            match(Tag.WHILE);
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
        match(Tag.ID);
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
        match(Tag.ID);
        match(';');
    }

    private void type() {
        match(Tag.BASIC);
        type1();
    }

    private void type1() {
        if (token.tag == '[') {
            match('[');
            match(Tag.NUM);
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
            match(Tag.OR);
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
            match(Tag.AND);
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
            match(Tag.EQ);
            rel();
            equ1();
        } else if (token.tag == Tag.NE) {
            match(Tag.NE);
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
            match(Tag.LE);
            expr();
        } else if (token.tag == Tag.GE) {
            match(Tag.GE);
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
            match(Tag.MINUS);
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
                match(Tag.NUM);
                break;
            case Tag.REAL:
                match(Tag.REAL);
                break;
            case Tag.TRUE:
                match(Tag.TRUE);
                break;
            case Tag.FALSE:
                match(Tag.FALSE);
                break;
            case Tag.ID:
                match(Tag.ID);
        }
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
