/*简易C语言的语法分析器(C--)，下面是限制条件
 *变量声明只能放在开头，变量赋值只能放在全体变量声明之后
 *语句块只能放在变量赋值之后
 *如果出现语法错误，则会报出异常并指出行号
 * */
void main(){
    int i;
    int j;
    float v;
    float x;
    int[100] arr;
    //变量声明完毕，下面进入变量赋值
    i = 100;
    j = 200;
    v = 200.123;
    x = 123.123;
    //变量赋值完毕，下面进入语句部分
    while(1) {
        if(i >= j){
            break
        }
        j = 10;
        i = j + 1;
        do i = i + 1; while (i < 10);
    }
}
