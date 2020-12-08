# complier

### 词法分析器

词法分析程序在 `\src\lexer\Lexer.java`, 源程序在`test.c`中，分析出来的结果在 `output.txt` 中

### 语法分析器

#### 递归下降分析

语法分析器在 `D:\Colleage\LexAnalyze\src\parse` 中，使用的是**无回溯的递归下降分析法**，因为时间原因没有写中间输出，但测试了几次，都可以测试出结果或者表明语法错误的行数

#### LL(1)

文法在 `Grammer.txt` 中，目前只实现了 `Frist集合` 的自动求解程序，在 `\src\symbols\First.java`中 。使用了 `mini-table` 进行输出，可以生成比较漂亮的表格。感谢[@blinkfox](https://github.com/blinkfox/mini-table)

输出文件在 `\src\symbols\first.txt`。

### 语义分析

为了更好理解语义分析的内容，写了一个四则运算器求值的翻译模式，也是使用递归下降分析程序写的。写完之后感觉文法是骨架，语义规则是灵魂，你可以写成求值的语义规则，也可以写成生成三地址代码的语义规则。

测试文件在 `\src\testCal.txt`。