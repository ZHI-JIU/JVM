# 堆

## 堆的核心概述
- 一个进程对应一个jvm实例，只有一个堆内存。堆是java内存管理的核心区域。
- java堆在jvm启动时即被创建，其大小有确定了（可调节的）。是jvm中最大的一块内存空间。
    - Xms 初始堆空间大小
    - Xmx 最大堆空间大小
- 堆可以物理上不连续，逻辑上是连续的。物理内存和虚拟内存有映射表，虚拟内存中连续的部分在实际的物理内存上是不连续的。
- 堆是线程共享的，但是可以在堆上划分出线程私有的**缓冲区**（Thread Local Allocation Buffer，TLAB），为了提升并发性能。
- jvm规范：所有的对象实例、数组都应当在运行时分配在堆上。实际使用时可以具体分体具体分析。也有可能存储在栈上。
- 数组和对象可能永远不会存储在栈上，因为栈帧保存引用，引用指向对象、数组在堆中的位置。
- 方法结束后，堆中的对象不会马上被移除（栈帧会出栈，堆中的对象只是没有局部变量表中的引用指向它），在GC时才会被移除。
- 堆是GC的重点区域。

### 堆的内存细分
现代的GC大部分都是基于分代收集理论设计的。
- jdk7及以前，逻辑上分为三部分：
    - 新生区
        - Eden区
        - Survivor区
          - survivor0区（from区）
          - survivor1区（to区）
    - 养老区
    - 永久区（方法区的落地方案）
- jdk8及以后，逻辑上分为三部分：
    - 新生区
        - Eden区
        - Survivor区
          - survivor0区（from区）
          - survivor1区（to区）
    - 养老区
    - 元空间（方法区的落地方案）

## 设置堆内存大小与OOM
- Xms 初始堆空间（新生代+老年代）大小，等价于-XX:InitialHeapSize
    - -X：jvm的运行参数
    - ms：member start的缩写
- Xmx 最大堆空间大小，等价于-XX:MaxHeapSize
- 默认值：
    - 初始内存： 物理内存 / 64
    - 最大内存： 物理存储 / 4
- 获取jvm中的堆内存大小
    ``Runtime.getRuntime().totalMemory()``  
- 获取jvm中的最大堆内存大小 
    ``Runtime.getRuntime().maxMemory()``
- 这两个值最好设置成一样，减少扩缩容对服务器的压力。    
- 查看堆空间的大小：
    - 方式一： jps查看进程号 + jstat -gc 进程号 查看各个区的大小和已使用空间
    - 方式二： -XX：+PrintGCDetails，程序执行完后会输出。  
    ![image](GCdetail)
    
## 年轻代与老年代
- jvm中的java对象按生命周期来看分为两类：
  - 生命周期较短的瞬时对象。这类对象的创建和消亡都非常快。
  - 生命周期长，在极端情况下可以与jvm的生命周期保持一致。
  - 因此jvm的内存可以再细分一下，把生命周期长的放在回收周期的区块可以减少损耗。
- 堆进一步细分，可以分为年轻代和老年代。
- 年轻代又可以分成eden区、s0区、s1区。

![image](heap)

### 分配比例
#### 新生代和老年代
- 默认``-XX:NewRatio=2``，新生代1，老年代2。
- 可以改为``-XX:NewRatio=4``，新生代1，老年代4。
- 一般不会调，生命周期长的对象多时可以考虑调整。

#### 新生代中三个区
- 默认eden：s0：s1是8:1:1
  - 实际看到的结果可能不是8:1:1，因为jvm有自适应机制
  - 关闭自适应的内存分配策略：``-XX:-UseAdaptiveSizePolicy``（实际不起作用）
  - 需要手动通过``-XX:SurvivorRatio=8``来设置。数值是eden区的比重，s0、s1默认是1。
- 几乎所有的java对象都是在Eden区被new出来的。除非特别大，eden区放不下，直接放到老年代。
- 绝大部分的java对象的销毁都在新生代进行。
- ``-Xmn``：设置新生代的空间大小。
- ``-Xmn``和``-XX:NewRatio``都指定时按``-Xmn``生效。

## 对象分配过程
### 一般过程
1. new出来的对象先放eden区（实体放在堆空间，变量放在jvm栈的局部变量表中）
2. eden区满了以后触发young gc/Minor GC。
3. 已经没有引用指向的回收，还有指向的放在s0区，并赋予age 1。（此时eden区清空）
4. 再次满了照样触发young gc。
5. eden区已经没有引用指向的回收，还有指向的放在s1区（谁空放哪里，空的也叫to区，to区是不确定的），并赋予age 1。
6. s0区也触发gc，还有引用的放到s1区，age递增1岁。（此时eden、s0是空的，s0变成下一轮的to区）
7. 依次触发，当s0、s1区有age到15时，放入老年区。

- survivor区的gc是在eden区满时被动触发的，它自己满时不会触发young gc。满了以后会直接进到老年区。
- 频繁收集新生代（80%会被回收），很少在老年代收集，几乎不在永久区/元空间收集。

### 特殊情况
- 要分配内存时，eden放不下，并且eden区触发gc后仍然放不下，会去判断老年区是否能放下，老年区能放就放，不能放触发老年区的full gc/major gc，之后还是放不下就OOM。
- YGC时，eden区有部分对象会需要移动到s0、s1区（已经随着YGC回收过了），如果survivor区放不下，直接放到老年代。

![image](specialGC)

## Minor GC，Major GC，Full GC
性能调优主要的手段就是减少GC，方式GC的线程过多影响用户线程。
- jvm在进行GC时，并非每次都对新生代、老年代、方法区一起回收，大部分都发生在新生代。
- hotspot的实现，GC按照回收区域分为两类：
    - partial GC：部分收集，不完整收集整个java堆得垃圾收集，又分为：
        - 新生代收集（Minor GC/Young GC0）：新生代（Eden、S0、S1）的垃圾收集
        - 老年代收集（Major GC/Old GC）：老年代的垃圾收集
          - 只有CMS GC（一种并发的垃圾回收器）会有单独收集老年代的行为。
          - 很多时候Major GC和Full GC混淆使用，需要具体分辨是哪种。
        - 混合收集（Mixed GC）：收集整个新生代、老年代的垃圾收集。
          - 只有G1 GC会有这种行为
    - full GC：整堆GC，收集整个java堆和方法区的垃圾收集。

### Minor GC的触发机制
- 年轻代（更确切地说是eden区）空间不足时会触发。每次触发时顺便会回收s0和s1区。
- java对象大多朝生暮死，Minor GC非常频繁，回收速度快。
- Minor GC会引发STW(stop to world)，暂停其他用户的线程，等垃圾回收结束，用户线程才恢复运行。

### Major GC的触发机制
- Major GC一般会伴随一次Minor GC（在Parallel Scavenge GC的收集策略里不会有Minor GC）。
- Major GC的速度比Minor GC慢至少10倍以上（老年代空间大），STW的时间更长。
- Major GC后，内存还不足，会报OOM。

### Full GC的触发机制
- 调用System.gc()，系统建议执行full gc，但不是必然执行
- 老年代空间不足
- 方法区空间不足
- 通过Minor GC后进入老年代的平均大小大于老年代的可用内存
- 由Eden区、from区向to区复制时，对象大小大于to区可能内存，则把该对象存到老年代，且老年代的可用内存小于该对象大小

full gc是开发或调优中要尽量避免的，这样STW的时间会短。

## 内存分配策略
### 一般情况
- 对象在eden出生并经过一次MinorGC后仍然存活，并且能被survivor区容纳的话，被移动到Survivor区中。并将年龄设为1。
- 对象在survivor区每熬过一次MinorGC，年龄增加一岁。
- 年龄达到阈值时（默认15，每个JVM、每个GC都有所不同），晋升到老年代。
- 阈值可以通过``-XX:MaxTenuringThreshold``来设置。

### 对象提升规则
不同年龄段的对象分配原则：
- 优先分配到eden区
- 大对象直接分配到老年代，应尽量避免程序中出现过多的大对象
  - 大对象：在物理内存中比较长的连续的对象。如长字符串、数组
- 长期存活的对象分配到老年代
- 动态年龄判断
  - 如果survivor区中相同年龄的所有对象大小的总和大于survivor区空间的一半，大于等于该年龄的对象可以直接进入到老年代，无需等到MaxTenuringThreshold。
- 空间分配担保
  - 大量对象在MinorGC之后还是存活的， survivor区剩余空间又比较小，需要老年代来担保，把survivor区容纳不下的直接放到老年代。
  - ``-XX: HandlePromotionFailure``
  - 在发生minorGC之前，jvm会检查**老年代最大可用的连续内存空间**是否大于**新生代所有对象的总空间**：
    - 大于：此次minor GC是安全的。
    - 小于：jvm会查看``-XX: HandlePromotionFailure``，是否设置了允许担保失败。
      - 值为true，会继续检查老年代最大可用连续空间是否大于**历次晋升到老年代的对象的平均大小**。
        - 大于：尝试进行一次minorGC，这次minorGC依然是有风险的。
        - 小于：进行一次Full GC。
      - 值为false：进行一次full gc。
  
  - JDK7及之后该参数无意义，只要**老年代最大可用的连续内存空间**大于**新生代所有对象的总空间**或**历次晋升到老年代的对象的平均大小**，就会进行full gc。

## 为对象分配内存：TLAB
### 什么是TLAB
- Thread Local Allocation Buffer
- 从内存模型而不是垃圾回收的角度，对eden区继续划分，jvm为每个线程分配一个私有缓存区域。
- 多线程同时分配内存时，使用TLAB可以避免线程安全问题，提升内存分配的速度。
- 所有openJDK衍生出来的jvm都提供了TLAB的设计。

### 为什么要有这个结构
  - 堆区是线程共享的，任何线程都可以访问堆区中的共享数据，方便线程间通信。
  - 对象的创建都是在堆中，有线程不安全问题。
  - 为了避免多个线程操作同一对象，需要加锁，这会影响分配速度。

### 其他
- 不是所有对象都能够在TLAB成功分配，因为这块区域较小，但是jvm还是把TLAB作为首选的分配区域。
- 不够分以后，jvm尝试通过**加锁机制**确保数据操作的原子性，直接在eden区中分配内存。
- ``-XX:UseTLAB``：设置是否开启TLAB空间。默认开启。
- 默认情况下，TLAB空间较小，是整个eden区的**1%**。这样才能保证可以支持多个线程。不会几个线程就把eden区占满了。
- ``-XX:TLABWasteTargetPercent``：设置TLAB占用eden空间的百分比。

### 分配过程
![image](TLAB)

## 堆空间的参数设置
- ``-XX:+PrintFlagsInitial``：查看jvm中所有的参数的默认初始值
- ``-XX:+PrintFlagsFinal``：查看所有的参数的最终值
- 查看jvm中具体某个参数的值：
  1. 将程序运行起来
  2. 在运行期间执行jps拿到进程号
  3. jinfo -flag [参数名] [进程号]
- ``-Xms``: 初始堆空间（默认是物理内存的1/64）
  - The initial size of the heap for the young generation can be set using the -Xmn option or the ``-XX:NewSize`` option.
- ``-Xmx``: 最大堆空间（默认是物理内存的1/4）
  - The -Xmx option is equivalent to ``-XX:MaxHeapSize``
- ``-Xmn``: Sets the initial and maximum size (in bytes) of the heap for the young generation (nursery)
  - Instead of the -Xmn option to set both the initial and maximum size of the heap for the young generation, you can use ``-XX:NewSize`` to set the initial size and  ``-XX:MaxNewSize`` to set the maximum size.
- ``-XX:NewRatio=ratio``: Sets the ratio between young and old generation sizes. **By default, this option is set to 2**. The following example shows how to set the young/old ratio to 1:
  ``-XX:NewRatio=1``
- ``-XX:InitialSurvivorRatio=ratio``：新生代和S0、s1的比例
- ``-XX:MaxTenuringThreshold=threshold``：Sets the maximum tenuring threshold for use in adaptive GC sizing. **The largest value is 15**. **The default value is 15 for** the parallel (throughput) collector, and **6 for the CMS collector**.
- ``-XX:+PrintGCDetails``：
  Enables printing of detailed messages at every GC. By default, this option is disabled.
- ``-XX:+PrintGC``：
  Enables printing of messages at every GC. By default, this option is disabled.
- ``-verbose:gc``：
  Displays information about each garbage collection (GC) event.
- ``-XX: HandlePromotionFailure``：是否设置空间担保
- ``-XX:+DoEscapeAnalysis``：是否开启逃逸分析（JDK 6u23之后默认开启）
- ``-XX:+PrintEscapeAnalysis``：查看套系分析的筛选结果。
- jstat -gc 进程号 查看gc情况
- jps 查看当前进程
- -XX：+PrintGCDetails
- jinfo

## 堆是分配对象的唯一选择么
堆是GC的重点区域，是影响性能的主要原因。
主要是要降低GC的频率，再主要一点，是降低老年代的GC。

两种优化思路：
1. 调整jvm参数，减少gc。
2. 将对象分配在堆以外的空间。

随着JIT（编译器）的发展和逃逸分析技术的成熟，**栈上分配、标量替换优化技术**会导致微妙的变化，对象分配到堆上不再这么绝对。
- 如果经过逃逸分析后发现，一个对象没有逃逸处方法的话，可能被优化成栈上分配。
- taobaoVM的GCIH（GC invisible heap）技术实现了堆外存储，将生命周期较长的java对象分配在堆外，不由GC管理，降低GC的频率。

### 逃逸分析
编译器能够分析处一个对象的引用的使用范围，从而判断是否发生了逃逸。

- 逃逸分析的基本行为就是分析对象的动态作用域：
  - 对象在方法中被定义，对象只在方法内部使用，则认为没有发生逃逸。
  - 对象在方法中被定义，又被外部方法引用，则认为发生了逃逸。（例如作为参数传到其他方法中的对象）
    ![image](escapeAnalyse)