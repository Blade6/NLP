### Introduction
It's an simple implemention of some of the algorithm of NLP that I learned in class.It's used to process chinese sentences.

### How To Use It
1. Import into your eclipse.
2. Run Begin.java under NLP package.

### Envionment
JDK Version -- 1.8
Eclipse Version -- neon(4.6)
Encoding:GBK

### Packages Explanation
MaxMatchingMethod:最大匹配，此包下实现了双向匹配算法。
MaxProbability:实现了概率最大分词算法。
CLAWS:实现了CLAWS算法。
CLAWS_improved:改进了CLAWS算法。
viterbi:实现了viterbi算法。

### Prezentation
![双向匹配算法.PNG](http://oncw676n6.bkt.clouddn.com/1.PNG)

![概率最大分词算法.PNG](http://oncw676n6.bkt.clouddn.com/3.PNG)

![CLAWS改良.PNG](http://oncw676n6.bkt.clouddn.com/2.PNG)

### Update
2017/5/1 使用缓存等加快IO速度，使用单例模式为每个算法仅创造一个实例，减少不必要的IO，同时由于常驻内存的关系，算法运行速度基本维持在5ms以内，除了第一次运行外。CLAWS和viterbi处理复杂的句子可能导致假死。


