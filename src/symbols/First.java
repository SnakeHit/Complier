package symbols;

import lexer.Lexer;
import util.MiniTable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/12/8 17:27
 */
public class First {
    public static Hashtable<String, List<String>> hashtable;
    public static List<String> first; //First集合

    static {
        hashtable = new Hashtable<>();
        //每一个文法有一个 list, 首先初始化该hashtable，把终结符加入到
        for (List<String> list : Symbol.listCollection) {
            for (String s : list) {
                first = new ArrayList<>();
                if (Lexer.words.get(s) != null) { //证明是终结符
                    first.add(s);
                    hashtable.put(s, first);
                } else if (s.length() == 1 && s.charAt(0) > 0 && s.charAt(0) < 255) {
                    first.add(s);
                    hashtable.put(s, first);
                } else if (Objects.equals(s, "empty")) {
                    first.add(s);
                    hashtable.put(s, first);
                } else {
                    //first为空
                    hashtable.put(s, first);
                }
            }
        }
        //进行类似树遍历的操作，直到所有的符号的First集合不再改变
        while (true) {
            int flag = 0;
            for (List<String> list : Symbol.listCollection) {
                String one = list.get(0);
                first = hashtable.get(one);//得到非终结符的目前的first集合
                for (int i = 1; i < list.size(); i++) {
                    String tempS = list.get(i);
                    List<String> tempL = hashtable.get(tempS);
                    if (!tempL.contains("empty")) {
                        //如果第一个符号的first集合不为空，将其first集合加入其中
                        for (String s : tempL) {
                            if (!first.contains(s)) {
                                first.add(s);
                                flag = 1;
                            }
                        }
                        break;
                    } else if (tempL.contains("empty") && list.size() == 2) { //E->F, F包含empty, First(E) = First(F)
                        for (String s : tempL) {
                            if (!first.contains(s)) {
                                first.add(s);
                                flag = 1;
                            }
                        }
                        break;
                    } else {
                        tempL.remove("empty");
                        for (String s : tempL) {
                            if (!first.contains(s)) {
                                first.add(s);
                                flag = 1;
                            }
                        }
                    }
                }
                for (String s : first) {
                    if (!hashtable.get(one).contains(s)) {
                        hashtable.get(one).add(s);
                    }
                }

            }
            if (flag == 0)
                break;
        }

//        for (String s : hashtable.keySet()) {
//            System.out.println(s + ": " + hashtable.get(s).toString());
//        }
    }

    public static void print() {
        MiniTable miniTable = new MiniTable("First Collection");
        miniTable.addHeaders("non-terminal", "first");
        for (String s : hashtable.keySet()) {
            String str = hashtable.get(s).toString();
            miniTable.addDatas(s, str.substring(1, str.length() - 1));
        }

        System.out.println(miniTable.render());
    }

    public static void main(String[] args) {
        First.print();
    }
}

/*
+----------------------------------------------------+
|                  First Collection                  |
+--------------+-------------------------------------+
| non-terminal |                first                |
+--------------+-------------------------------------+
|    empty     |                empty                |
|   program    |                basic                |
|     bool     | !, -, (, id, num, real, true, false |
|      ]       |                  ]                  |
|      [       |                  [                  |
|    decls1    |            empty, basic             |
|    bool1     |              ||, empty              |
|     true     |                true                 |
|    decls     |            empty, basic             |
|      &&      |                 &&                  |
|     equ      | !, -, (, id, num, real, true, false |
|      do      |                 do                  |
|      ||      |                 ||                  |
|    block     |                  {                  |
|     join     | !, -, (, id, num, real, true, false |
|     real     |                real                 |
|      >       |                  >                  |
|      =       |                  =                  |
|      <       |                  <                  |
|     else     |                else                 |
|      ;       |                  ;                  |
|    basic     |                basic                |
|    term1     |             *, /, empty             |
|    unary     | !, -, (, id, num, real, true, false |
|      ==      |                 ==                  |
|    stmts     | empty, if, while, do, break, {, id  |
|    type1     |              [, empty               |
|      /       |                  /                  |
|    break     |                break                |
|      -       |                  -                  |
|      +       |                  +                  |
|      *       |                  *                  |
|      )       |                  )                  |
|     term     | !, -, (, id, num, real, true, false |
|      (       |                  (                  |
|      !=      |                 !=                  |
|      !       |                  !                  |
|    while     |                while                |
|      if      |                 if                  |
|     equ1     |            ==, !=, empty            |
|      id      |                 id                  |
|      }       |                  }                  |
|     rel      | !, -, (, id, num, real, true, false |
|      {       |                  {                  |
|      <=      |                 <=                  |
|    false     |                false                |
|     expr     | !, -, (, id, num, real, true, false |
|    stmts1    | empty, if, while, do, break, {, id  |
|    factor    |    (, id, num, real, true, false    |
|     lval     |                 id                  |
|    lval1     |              [, empty               |
|     decl     |                basic                |
|    expr1     |             +, -, empty             |
|     num      |                 num                 |
|     stmt     |     if, while, do, break, {, id     |
|    join1     |              &&, empty              |
|     type     |                basic                |
+--------------+-------------------------------------+
*/
