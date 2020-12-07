package parse;

import java.util.Scanner;
import java.util.Stack;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/12/1 20:28
 */
public class CalculatorStack {
    public static void main(String[] args) {
        Stack<String> ops = new Stack<>();
        Stack<Double> vals = new Stack<>();
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            String s = in.next();
            if (s.equals("(")) ;
            else if (s.equals("+")) ops.push("+");
            else if (s.equals("-")) ops.push("-");
            else if (s.equals("*")) ops.push("*");
            else if (s.equals("/")) ops.push("/");
            else if (s.equals(")")) {
                String s1 = ops.pop();
                Double v1 = vals.pop();
                Double v2 = vals.pop();
                double result = 0.0;
                if (s1.equals("+")) result = v1 + v2;
                else if (s1.equals("-")) result = v1 - v2;
                else if (s1.equals("*")) result = v1 * v2;
                else if (s1.equals("/")) result = v1 / v2;
                vals.push(result);
            } else vals.push(Double.parseDouble(s));
        }
        System.out.println(vals.pop());
    }
}
