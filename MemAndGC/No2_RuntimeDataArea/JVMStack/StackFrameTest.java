package MemAndGC.No2_RuntimeDataArea.JVMStack;

public class StackFrameTest {
    public static void main(String[] args) {
        StackFrameTest test = new StackFrameTest();
        test.method1();
    }

    private void method1() {
        System.out.println("method1开始执行");
        method2();
        System.out.println("method1执行结束");
    }

    private int method2() {
        System.out.println("method2开始执行");
        int i = 10;
        int m = (int) method3();
        System.out.println("method2即将结束");
        return i + m;
    }

    private double method3() {
        System.out.println("method2开始执行");
        double j = 20.0;
        System.out.println("method2即将结束");
        return j;
    }
}
