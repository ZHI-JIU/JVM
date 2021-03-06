# 本地方法栈

虚拟机栈用于管理java方法的调用，本地方法栈用于管理本地方法的调用。

- 本地方法栈也是线程私有的。
- 本地方法栈允许固定长度或动态扩展。也会有StackOverFlow或oom。
- 本地方法是通过C语言实现的。
- 执行到本地方法时，在本地方法stack中登记native方法，通过动态链接的方式去调用C的库，在执行引擎执行时加载本地方法库。
- 当线程调用本地方法时，它就进入了全新的并且不受虚拟机限制的世界。它和虚拟机拥有同样的权限。
    - 本地方法可以通过本地接口来访问虚拟机内部的运行时数据区。
    - 可以直接使用本地处理器中的寄存器。
    - 直接从本地内存的堆中分配任意数量的内存。
- 不是所有的jvm都支持本地方法。hotspot中，直接将本地方法栈和虚拟机栈合二为一。    