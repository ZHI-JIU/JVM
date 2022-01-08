package MemAndGC.No1_ClassLoader.Ch1;

public class ExtendClinitTest {
    static class Father {
        public static int father = 1;
        static {
            father = 2;
        }
    }

    static class Son extends Father {
        public static int son = father;
    }

    public static void main(String[] args) {
        // 1. 加载ExtendClinitTest
        // 2. 调用静态方法main
        // 3. 使用到Son时加载类Son
        // 4. Son有父类，先执行Father的clinit方法，father = 2
        // 5. 执行Son的clinit，son=2
        System.out.println(Son.son);
    }
}
