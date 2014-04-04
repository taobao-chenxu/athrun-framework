#!/bin/sh

echo "app:$1"
echo "script:$2"

mkdir /AppTestLog

if test file -f $2
then
oneCmd="instruments -t /Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate $1 -e UIASCRIPT $2 -e UIARESULTSPATH /AppTestLog/"
$oneCmd
echo "$2 run end!"

else

for file in $2/*
do
echo $file
cmd="instruments -t /Developer/Platforms/iPhoneOS.platform/Developer/Library/Instruments/PlugIns/AutomationInstrument.bundle/Contents/Resources/Automation.tracetemplate $1 -e UIASCRIPT $file -e UIARESULTSPATH /AppTestLog/"

$cmd
echo "$file run end!"

done
fi

delCmd="rm -f /Users/jerryding/Desktop/*.trace"
$delCmd

echo "all script run end!"
