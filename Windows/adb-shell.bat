@echo off
echo device %1

adb -s %1 shell "nohup sh xxx.sh >/dev/null 2>&1 &"

exit