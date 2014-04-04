#!/bin/bash

echo "The script $1 is  running!!"

oneCmd="instruments -t /Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate  /Users/jerryding/taobao4iphone/build/Distribution-iphonesimulator/taobao4iphone.app -e UIASCRIPT $1 -e UIARESULTSPATH /Athrun/log/"
$oneCmd

echo "$1 run end!"


delCmd="rm -rf ./*.trace"
$delCmd