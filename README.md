### netty学习
> netty是一个基于NIO的异步事件驱动的网络应用程序框架，用于快速开发可维护的高性能协议服务器和客户端。它极大的简化我们使用NIO进行的开发
> 优点：并发高、传输快、封装好
> 我们平常经常接触的 Dubbo、RocketMQ、Elasticsearch、gRPC、Spark、Elasticsearch 等等热门开源项目都用到了 Netty。

#### Java的IO模型
**BIO**:
BIO就是我们传统的IO，也是一种阻塞同步的IO。在线程发起请求之后会一直阻塞IO直到缓冲数据就绪后再进行下一步操作，在此之间线程无法进行其他操作，是一种一问一答的形式。这也意味着每处理一个请求就要新开启一个线程去处理。因此即使它编程简单，但是并发是BIO的瓶颈。

**NIO**:
NIO是一种同步非阻塞的IO。线程发起请求之后会立即返回，然后进行其他的处理，过一段时间查看缓冲区的数据是否就绪，就绪再进行处理。因此再NIO中一个线程可以轮询处理多个请求。（得益于buff和selector）

**AIO**:
AIO是真正意义上的异步非阻塞IO模型。
上述NIO实现中，需要用户线程定时轮询，去检查IO缓冲区数据是否就绪，占用应用程序线程资源，其实轮询相当于还是阻塞的，并非真正解放当前线程，因为它还是需要去查询哪些IO就绪。而真正的理想的异步非阻塞IO应该让内核系统完成，用户线程只需要告诉内核，当缓冲区就绪后，通知我或者执行我交给你的回调函数。

### 核心组件
- Bytebuf
	Bytebuf是对NIO中bytebuff的一个封装，因为NIO中bytebuffer使用起来比较麻烦。
	bytebuffer不能自动扩容，API提供的功能有限。读写需要手动切换等
	bytebuf可以自动扩容，API丰富，底层有两个指证读写不需要切换。
	
- Bootstrap 和 ServerBootstrap
	分别为客户端和服务端启动的辅助配置类！

- channel 和 pipleLine



### 零拷贝、mmp、SendFile
传统网络IO：
第一次：将磁盘文件读取到操作系统的内核区域
第二次：将内核区数据读取到用户空间区域
第三次：将用户空间读取到socket缓冲区（也是位于内核）
第四次：将socket数据读取到网卡缓冲区
在这四次拷贝中花费了大量资源和时间。
因此零拷贝技术就是通过减少其中的拷贝次数达到优化的效果

sendfile技术：数据直接在内核区域完成输入和输出，不需要拷贝到用户空间进行操作。
mmap文件映射：在进程的非堆内存开辟一块内存空间，OS内存空间进行一一映射。上述操作中copy操作由四次变为三次

### Reactor模型
单Reactor单线程

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/149d34cc36b14d0c8ac580e659c13855~tplv-k3u1fbpfcp-zoom-1.image)



单Reactor多线程

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/90aff395dd904592a2d4c6d8bbc407ca~tplv-k3u1fbpfcp-zoom-1.image)

多Reactro多线程模型

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/486ea22274734af98e810846f63fd3eb~tplv-k3u1fbpfcp-zoom-1.image)

### netty模型

![](https://pic3.zhimg.com/v2-cc6bf4ea10291ba5f51d469b49d25a4e_r.jpg)


### 粘包拆包:自定义协议

面试常见问题：https://xiaozhuanlan.com/topic/4028536971#sectionbionioaio