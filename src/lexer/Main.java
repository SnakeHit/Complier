package lexer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/5 0:47
 */
public class Main {
    public static void main(String[] args) {
        try {
            File file = new File("D:\\Colleage\\LexAnalyze\\src\\test.c");
            FileReader fileReader = new FileReader(file);
            int length = (int) file.length();
            Lexer lexer = new Lexer(fileReader, length);
            int n = length;
            while (n-- != 0) {
                Token token = lexer.scan();
                if (token.tag != 0) {
                    System.out.println(lexer.line+"行：< " + token.toString() + " , " + token.tag + " >" );
                } else {
                    break;
                }
            }
            System.out.println(lexer.words);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
