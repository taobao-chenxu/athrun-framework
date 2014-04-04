简单介绍：

1.InstrumentDriver 尝试让instrument uiautomation实现C/S 模式的方式运行脚本，mobile端作为Client，PC端作为Server，我们在PC端编写类似录制javascript脚本格式的用例，以Junit单元测试方式运行测试用例。


2.原理：instrumentuiautomation 提供了在脚本中调用外部shell的功能，并且能获取到shell运行的输出。
据此，我们在Mac上启动instrument运行一个脚本，让这个脚本调用shell与远程的PC进行通信获取发送过来的脚本并执行。

  因为没法在instrument javascript脚本里建立服务端，所以只能以Mobile作为客户端，测试用例作为socket的服务端，客户端循环请求服务端(消费用例步骤)，让用例不停往下运行。直至出现发送结束符或者抛出异常。

相关文件：
  客户端主要有两个个文件，CSRunner.js(JSLib目录)文件调用 TcpSocket.sh 与 服务端Junit 测试用例进行通信逐步运行。TcpSocket.sh 作为C/S 双方的通信中介，供 CSRunner.js 调用以请求服务端的用例步骤。(TcpSocket.sh 在Athrun目录下)

3.提供调用shell 的方法如下：host.performTaskWithPathArgumentsTimeout("/Athrun/Test.sh", ["null"], 3);在Test.sh 中通过建立tcp连接与PC端的Server通信，请求需要执行的步骤。运行时，需要先启动单元测试，然后启动instrument uiautomation 运行，向server端请求命令并获取返回的响应执行。

4.问题：host.performTaskWithPathArgumentsTimeout() 方法运行速度有点慢，因为会不停的调用这个shell请求server端命令，性能有些瓶颈。




改进：
	
    目前通过减少服务端与客户端的通信次数提升脚本运行速度。脚本分debug模式和非debug模式两种方式运行，debug模式运行速度较慢，可以调试脚本，查看相关对象的变量值。非debug模式在需要操作UI元素或获取UI元素属性的时候才通信，较大的提升了运行速度(debug模式的2倍左右)。
    正常情况下，可以这么认为：debug通过后，非debug模式也能运行通过。
     
    提供了查找元素的方法: findElemenetByText(String text)，findElementArrayByText(String text) 以及相关重载。

