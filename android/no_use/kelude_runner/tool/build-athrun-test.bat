set ANDROID_AVD_DEVICE=%1

rem ======= build package ==========

cd /d C:\AthrunLog\code\Athrun-example\main_demo_app
svn up
cd /d C:\AthrunLog\code\Athrun-example\main_demo_app\tool
call test-packet.bat

cd /d C:\AthrunLog\code\Athrun-example\main_demo_app\tool
if exist apk/*.apk (echo "apk build success") else (
echo "apk build failed"
echo "==========log.txt========"
type log.txt
exit 1
)

rem ======= install package ==========

for /F "delims=" %%i in ('dir apk\*.apk /b') do (

adb -s %ANDROID_AVD_DEVICE% uninstall org.athrun.android.app
adb -s %ANDROID_AVD_DEVICE% install "apk\%%i"

)

rem ======= build test package ==========

cd /d C:\AthrunLog\code\Athrun-example\test_demo
svn up
cd /d C:\AthrunLog\code\Athrun-example\test_demo\tool
call test-packet.bat

cd /d C:\AthrunLog\code\Athrun-example\test_demo\tool
if exist apk/*.apk (echo "apk build success") else (
echo "apk build failed"
echo "==========log.txt========"
type log.txt
exit 1
)

rem ======= install test package ==========

for /F "delims=" %%i in ('dir apk\*.apk /b') do (

adb -s %ANDROID_AVD_DEVICE% uninstall org.athrun.android.test
adb -s %ANDROID_AVD_DEVICE% install "apk\%%i"

)

