#!/bin/sh

if [ $2 -ne 1 ] ;then
	#打开host的port 可读写的socket连接，与文件描述符6连接 
	exec 6<>/dev/tcp/127.0.0.1/5566
	#将信息发送给socket连接 
	echo $1>&6
	returnStr=`cat<&6`
	#关闭socket的输入，输出 
	exec 6<&-
	exec 6>&- 
fi

exec 6<>/dev/tcp/127.0.0.1/5566
echo "Get the next step.">&6;
#从socket读取返回信息，显示为标准输出 
returnStr=`cat<&6`
#关闭socket的输入，输出 
exec 6<&-
exec 6>&-
	

echo $returnStr

exit 0

