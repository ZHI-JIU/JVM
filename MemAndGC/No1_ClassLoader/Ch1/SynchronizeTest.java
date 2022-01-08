package MemAndGC.No1_ClassLoader.Ch1;

public class SynchronizeTest {
    public static void main(String[] args) {
        // 实现runnable接口
        Runnable r = () -> {
            System.out.println(Thread.currentThread().getName() + "start");
            DeadThread deadThread = new DeadThread();
            System.out.println(Thread.currentThread().getName() + "end");
        };

        Thread t1 = new Thread(r, "thread-1");
        Thread t2 = new Thread(r, "thread-2");

        // 抢到资源的线程开始加载DeadThread后，因为加载会一直进行中，另一个线程永远抢不到锁
        t1.start();
        t2.start();
    }
}

class DeadThread {
    // 一个永远不会结束的静态代码块
    static {
        // 加if是为了能编译通过
        if (true) {
            System.out.println(Thread.currentThread().getName() + "is initializing DeadThread");
            while (true) {

            }
        }
    }
}
