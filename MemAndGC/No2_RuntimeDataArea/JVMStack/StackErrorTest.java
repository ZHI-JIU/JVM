package MemAndGC.No2_RuntimeDataArea.JVMStack;

public class StackErrorTest {
    private static int count = 1;
    // 演示stackOverFlow
    public static void main(String[] args) {
        System.out.println("count: " + count);
        count++;
        main(args);
    }
}
