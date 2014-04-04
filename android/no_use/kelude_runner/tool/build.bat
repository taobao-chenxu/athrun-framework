set ANDROID_AVD_DEVICE=%1

rem ======= build package ==========

cd /d C:\AthrunLog\code\tao\
svn up
cd /d C:\AthrunLog\code\tao\tools
²»»ìÏý4.0.bat

cd /d C:\AthrunLog\code\tao\tools
if exist apk/*.apk (echo "apk build success") else (
echo "apk build failed"
echo "==========log.txt========"
type log.txt
exit 1
)

rem ======= install package ==========

for /F "delims=" %%i in ('dir apk\*.apk /b') do (

adb -s %ANDROID_AVD_DEVICE% uninstall com.taobao.taobao
adb -s %ANDROID_AVD_DEVICE% install "apk\%%i"

)

rem ======= build test package ==========

cd /d C:\AthrunLog\code\TaobaoAndroidTestTMTS\
svn up
cd /d C:\AthrunLog\code\TaobaoAndroidTestTMTS\tools
start /WAIT test-packet.bat

cd /d C:\AthrunLog\code\TaobaoAndroidTestTMTS\tools
if exist apk/*.apk (echo "apk build success") else (
echo "apk build failed"
echo "==========log.txt========"
type log.txt
exit 1
)

rem ======= install test package ==========

for /F "delims=" %%i in ('dir apk\*.apk /b') do (

adb -s %ANDROID_AVD_DEVICE% uninstall com.taobao.taobao
adb -s %ANDROID_AVD_DEVICE% install "apk\%%i"

)

