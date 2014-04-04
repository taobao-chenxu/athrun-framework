#!/bin/sh

##  There are two run solutions:
##          1.  Using terminal  model just type : osascript change.scrpt your-argv
##                  if you don't know osascript. please search it with google or study "man osascript "
##          2.  Run change.sh file.
##          dylan.zhang@ringcentral.com
chmod 777 change.scpt
osascript change.scpt "iPad"
sleep 5