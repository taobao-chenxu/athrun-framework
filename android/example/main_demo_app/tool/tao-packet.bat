del apk\tao.apk
aapt p -f -u -M AndroidManifest.xml -S res -A assets -I android.jar -F apk/tao.apk
aapt a  apk/tao.apk classes.dex
jarsigner -verbose -storepass android -keystore debug.keystore -signedjar apk/tao_signed.apk apk/tao.apk androiddebugkey
zipalign -v -f 4 apk/tao_signed.apk apk/tao_signed_align.apk
del apk\tao.apk
del apk\tao_signed.apk
rename apk\tao_signed_align.apk tao.apk