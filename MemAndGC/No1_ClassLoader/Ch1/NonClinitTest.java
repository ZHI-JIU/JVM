package MemAndGC.No1_ClassLoader.Ch1;

public class NonClinitTest {
    // 虽然有类变量，但是没有赋值
    public static int num;

    public static void main(String[] args) {
        System.out.println(num);
    }

    public void add() {
        num++;
    }
}
