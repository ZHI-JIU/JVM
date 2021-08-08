package MemAndGC.ClassLoader.Ch1;

public class ClinitTest {
    // 任何类声明后，内部至少存在一个构造器
    private int a = 1;
    private static int c = 3;

    static{
        c = 5;
        // num可以先赋值再定义的原因：
        // num是static修饰的，在linking.prepare阶段已经分配了内存，并默认初始化为零值0，在初始化阶段可以对它再次赋值了
        num = 20;
        // 如果是非static的变量，是没有办法在定义前赋值的。
        // 会报错Non-static field 'number' cannot be referenced from a static context
//        number = 20;

        // 因为还没有地你故意，这里只能给static变量赋值，不能引用它
        // 会报错Illegal forward reference
//        System.out.println(num);
    }

    private static int num = 10;
//    private int number = 10;

    public static void main(String[] args) {
        int b = 2;
        System.out.println(c);
        System.out.println(num);
    }

    public ClinitTest() {
        a = 10;
        int d = 20;
    }
}
