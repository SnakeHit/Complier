package test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/12/7 23:47
 */
public class MiniTableTest {
    public static void main(String[] args) {
        StringBuilder s = new StringBuilder();
        List<StringBuilder> list = new ArrayList<>();
        list.add(new StringBuilder("block"));
        list.set(0, list.get(0).append(", {"));

//        list.set(0, list.get(0))
        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("program", String.valueOf(list.get(0)));
        hashtable.put("block", "{");

        MiniTable m = new MiniTable("First Collection");
        m.addHeaders("Non-terminal", "First");
        for (String key : hashtable.keySet()) {
            m.addDatas(key, hashtable.get(key));
        }
        System.out.println(m.render());
    }
}
