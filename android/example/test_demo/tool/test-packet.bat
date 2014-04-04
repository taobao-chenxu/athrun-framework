@echo off
(
rmdir /s /q apk
md apk
xcopy ..\res\*.* res\ /s /e /Y
xcopy ..\assets\*.* assets\ /s /e /Y
copy ..\AndroidManifest.xml AndroidManifest.xml
xcopy ..\src\*.* src\ /s /e /Y
xcopy ..\libs\*.* libs\ /s /e /Y
ant -f build_compile.xml

call tao-packet.bat 
rename apk\tao.apk TmtsApp.apk
ECHO. TmtsTest.apk打包完成
copy ..\res\values\config.xml res\values\config.xml

echo. 删除所有文件
rmdir /s /q res
rmdir /s /q assets
rmdir /s /q bin
rmdir /s /q src
rmdir /s /q libs
del AndroidManifest.xml
del classes.dex
echo. 全部打包已完成
)>log.txt

echo. --------------
echo. 全部打包已完成
echo. --------------
