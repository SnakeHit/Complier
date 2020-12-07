package lexer;

/**
 * @author suyi
 * @version 1.0
 * @date 2020/11/4 21:01
 */
public class Tag {
    //标识符
    public final static int
            AUTO = 256, BREAK = 257, CASE = 258, CHAR = 259, CONST = 260, CONTINUE = 261,
            DEFAULT = 262, DO = 263, DOUBLE = 264, ELSE = 265, ENUM = 266, EXTERN = 267,
            FLOAT = 268, FOR = 269, GOTO = 270, IF = 271, INT = 272, LONG = 273,
            REGISTER = 274, RETURN = 275, SHORT = 276, SIGNED = 277, SIZEOF = 278, STATIC = 279,
            STRUCT = 280, SWITCH = 281, TYPEDEF = 282, UNION = 283, UNSIGNED = 284, VOID = 285,
            VOLATILE = 286, WHILE = 287;
    //运算符
    public final static int
            SHL = 288, SHR = 289,
            EQ = 290, NE = 291,
            PA = 292, SA = 293, MA = 294, DA = 295, RA = 296, AA = 297, OA = 298, XA = 299,
            GE = 300, LE = 301,
            AND = 302, OR = 303;
    //常数的各种数据类型
    public final static int
            NUM = 304, REAL = 305;

    public final static int
            INDEX = 306, MINUS = 307, TEMP = 308, BASIC = 309;
    public final static int
            TRUE = 310, FALSE = 311, SP = 312, SM = 313, ID = 314;
}
