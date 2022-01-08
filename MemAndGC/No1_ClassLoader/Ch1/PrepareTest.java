package MemAndGC.No1_ClassLoader.Ch1;

public class PrepareTest {
    // a在准备阶段赋值为0，在初始化阶段赋值为1；
    private static int a = 1;

    public static void main(String[] args){
        System.out.println(a);
    }
}
