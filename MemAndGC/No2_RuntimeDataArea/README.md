# 运行时数据区
- [程序计数器](https://github.com/ZHI-JIU/JVM/tree/main/MemAndGC/No2_RuntimeDataArea/PcRegister)
- [虚拟机栈](https://github.com/ZHI-JIU/JVM/tree/main/MemAndGC/No2_RuntimeDataArea/JVMStack)
- [本地方法接口](https://github.com/ZHI-JIU/JVM/tree/main/MemAndGC/No2_RuntimeDataArea/NativeMethodInterface) (实际上并不在运行时数据区中)
- [本地方法栈]()
- [堆]()
- [方法区]()
- [直接内存]()
- [StringTable]()

## 概述
运行时数据区在JVM中的位置：

![image]()

class文件通过类加载器子系统进行加载，经历加载、链接、初始化的过程，加载完成后在内存的方法区中保存了运行时类它本身，再通过执行引擎去运行这个类。

运行时数据区即内存，是cpu和硬盘的桥梁（网络、硬盘中的资源想要被CPU运算，需要先加载到内存中，cpu只和内存交互）。jvm的内存布局规定java在运行时内存申请、分配、管理的策略。

具体内存划分：

![image]()

一个栈帧对应一个方法

元数据区和代码缓存区统称非堆空间（No-Heap）

每个区的生命空间：
- 红色（堆、非堆空间（方法区、JDK8以后叫元空间（方法区一个落地的实现）和代码缓存））：线程共享，跟进程同步
- 灰色（程序计数器、虚拟机栈、本地方法栈）：线程私有。

线程共有的问题：
- 线程安全
- 优化主要优化线程共有的内存部分

一个JVM实例对应一个Runtime实例。即运行时环境。

## 线程
线程是程序的运行单元。
JVM允许多线程并行。
HotSpot JVM中每个线程都与OS中的本地线程直接映射。
- java线程准备好后，OS中的线程创建。
- java线程回收后，OS中的线程回收。
OS负责将线程调度到可用的cpu上。一旦本地线程初始化成功后，就会调用java线程的run方法。

### 后台系统线程
不包括main线程以及main线程中创建的线程。
主要有：
- 虚拟机线程
- 周期任务线程
- GC线程
- 编译线程
- 信号调度线程