#!/bin/sh

#打开host的port 可读写的socket连接，与文件描述符6连接 
#exec 6<>/dev/tcp/10.13.47.44/5566
exec 6<>/dev/tcp/127.0.0.1/5566

#如果打开失败，$?返回不为0，终止程序 
if(($?!=0));then 
echo "open host error!"; 
exit 1; 
fi 

#将信息发送给socket连接 
echo $1>&6; 

#从socket读取返回信息，显示为标准输出 
returnStr=`cat<&6`;

exec 6<&-; 
exec 6>&-; 
#关闭socket的输入，输出 

if [ "null" = "$returnStr" ] ;then
exec 6<>/dev/tcp/127.0.0.1/5566
echo "Get the next step.">&6;
#从socket读取返回信息，显示为标准输出 
cat<&6;
exec 6<&-; 
exec 6>&-; 
#关闭socket的输入，输出 
else
echo $returnStr
fi

exit 0; 
