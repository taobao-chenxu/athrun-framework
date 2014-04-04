#! /bin/sh

#@author mijun@douban.com
#@author taichan@taobao.com

XCODE_PATH=`xcode-select -print-path`
XCODE_VERSION=`xcodebuild -version | grep 'Xcode 4.2' || xcodebuild -version | grep 'Xcode 4.3' || xcodebuild -version | grep 'Xcode 4.4'`
if [ -z "$XCODE_VERSION" ] ; then
	TRACETEMPLATE="$XCODE_PATH/../Applications/Instruments.app/Contents/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate"
else
	TRACETEMPLATE="$XCODE_PATH/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate"
fi

APP_LOCATION=$1
INSTRUMENT_ROOT=$2
isSimulator=$3
UDID=$4

#APP_LOCATION="/Users/komejun/Library/Application Support/iPhone Simulator/5.0/Applications/1622F505-8C07-47E0-B0F0-3A125A88B329/Recipes.app/"
#APP_LOCATION="/Users/athrun/Desktop/TaoTest/build/Debug-iphonesimulator/TaoTest.app"

echo $XCODE_PATH

if [ "$isSimulator" = "true" ] ; then
  echo instruments -t $TRACETEMPLATE -D instrumentsDriver "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/ -v
  instruments -t $TRACETEMPLATE -D instrumentsDriver "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/ -v
else
  echo instruments -w $UDID -t $TRACETEMPLATE -D instrumentsDriver "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/ -v
  instruments -w $UDID -t $TRACETEMPLATE -D instrumentsDriver "$APP_LOCATION" -e UIASCRIPT  "$INSTRUMENT_ROOT"/CSRunner.js -e UIARESULTSPATH  "$INSTRUMENT_ROOT"/log/ -v	
fi



