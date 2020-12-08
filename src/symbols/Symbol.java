package symbols;

import test.MiniTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/12/8 16:03
 */
public class Symbol {
    public static List<List<String>> listCollection;
    public static List<String> list;

    static {
        listCollection = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File("D:\\Colleage\\LexAnalyze\\src\\Grammer.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int i = 0;
        while (scanner.hasNextLine()) {
            list = new ArrayList<>();
            String s = scanner.nextLine();
            char[] chars = s.toCharArray();
            for (int j = 0; j < chars.length; j++) {
                StringBuilder temp = new StringBuilder();
                if (chars[j] == '<') {
                    j++;
                    while (chars[j] != '>') {
                        temp.append(chars[j]);
                        j++;
                    }
                    if (temp.length() == 0) {
                        temp.append('>');
                    }
                    list.add(temp.toString());
                }
            }
            listCollection.add(list);
        }
//        for (List<String> strings : listCollection) {
//            System.out.println(strings);
//        }
    }

    public static void print() {
        MiniTable miniTable = new MiniTable();
        miniTable.addHeaders("Symbol");
        for (List<String> strings : listCollection) {
            StringBuilder s = new StringBuilder();
            s.append(strings.get(0));
            s.append(" -> ");
            for (int i = 1; i < strings.size(); i++) {
                s.append(strings.get(i));
                s.append(" ");
            }
            miniTable.addDatas(s);
        }
        System.out.println(miniTable.render());
    }
}
