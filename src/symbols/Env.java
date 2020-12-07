package symbols;

import java.util.Hashtable;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 23:38
 */
public class Env {
    protected Env prev;
    private Hashtable table;

    public Env(Env n) {
        table = new Hashtable();
        prev = n;
    }
}
