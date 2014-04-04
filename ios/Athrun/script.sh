#!/bin/sh

filename="${1}.js"
touch $filename

echo "#import \"../LibJS/imports.js\"" > $filename
echo   >> $filename

str="appTest(\"${1}\",function(target,application)"
echo $str >> $filename

echo "{" >> $filename
echo   >> $filename
echo "})" >> $filename