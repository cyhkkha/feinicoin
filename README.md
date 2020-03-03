# feinicoin

by Feinimouse 2019-04-12

---

本项目用Java线程的实现了区块链节点

可以通过实现节点间的通信和共识等来模拟区块链运行，从而测试一些区块链设计的运行效率

---
## 新版本
---

* 项目入口

`src/main/java/name/feinimouse/feinicoinplus/Main.java`

测试内容控制在 `src/main/java/name/feinimouse/feinicoinplus/BaseRunner.java` 中

目前还不支持生成可执行文件

模拟通信的节点在 `name.feinimouse.feinicoinplus.core.node` ， 目前还不支持数据存储，是通过时延来模拟存储的。

共识的测试有专门的共识节点 `name.feinimouse.feinicoinplus.consensus` 来测试

---
## 旧版本
---

目前已经不在维护

需要在本地安装mongoDB

* 项目入口

`src/main/java/name/feinimouse/simplecoin/Main.java`

* 生成可执行文件

`mav package`： 打包（代码更新后需要在pom.xml中更新版本号）

通过 `java -jar feinicoin-0.7.3.jar -h` 查看帮助
