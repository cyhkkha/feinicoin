# feinicoin

by Feinimouse 2019-04-12

---

本项目用Java线程的实现了区块链节点

可以通过实现节点间的通信和共识等来模拟区块链运行，从而测试一些区块链设计的运行效率

---
## 新版本
---

### 项目入口

`src/main/java/name/feinimouse/feinicoinplus/Main.java`

测试的内容写在 `src/main/java/name/feinimouse/feinicoinplus/BaseRunner.java` 

### 实际操作

测试的实际操作类为 `src/main/java/name/feinimouse/feinicoinplus/base/sim/ExperimentManager.java`

1. 预处理
    * `preRunBlockchainNode()` 进行节点实时交易处理实验的预处理
    * `preRunConsensus()` 进行共识实验的预运行
    
2. 节点实时交易处理
    * `sendRandomMixTransClassical(double rate)` 进行传统比特币区块链节点的实时交易实验，rate表示资产类（合约）交易所占比例
    * `sendRandomMixTransFetch(double rate)` 进行改进后的联盟链节点的实时交易实验，rate表示资产类（合约）交易所占比例
    
3. 共识实验
    * `runPBFT()` 进行PBFT共识算法的实验
    * `runDPOS_BFT()` 进行改进后的DPOS共识算法的实验

### 配置项

所有的配置项在 `src/main/resource/feinicoinplus-config-base.properties` 中，非UTF-8编码，请用GBK编码打开阅读注释

### 需注意

目前还不支持生成可执行文件，需要直接运行main方法

目前还不支持数据存储，是通过时延来模拟存储的。

目前还不支持分布式网络，网络通信是通过时延来模拟的

### 其他

模拟实时交易处理的区块链节点接口在 `name.feinimouse.feinicoinplus.core.node` ， 实现类在 `src/main/java/name/feinimouse/feinicoinplus/base/node`

共识的测试有专门的共识节点来测试，接口在 `name.feinimouse.feinicoinplus.consensus` ，实现类在 `src/main/java/name/feinimouse/feinicoinplus/base/consensus`

`src/main/java/name/feinimouse/feinicoinplus/deprecated` 包下的文件为废弃的实现

---
## 旧版本
---

**目前已经不在维护**

**注意：** 需要在本地安装mongoDB

该版本的实现更为简单，但不易于理解

* 项目入口

`src/main/java/name/feinimouse/simplecoin/Main.java`

* 生成可执行文件

`mav package`： 打包（代码更新后需要在pom.xml中更新版本号）

通过 `java -jar feinicoin-0.7.3.jar -h` 查看帮助
